package be.eekhaut.kristof.agile.task;

import be.eekhaut.kristof.agile.task.domain.Task;
import be.eekhaut.kristof.agile.task.repo.TaskRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.Arrays;

import static be.eekhaut.kristof.agile.task.TaskMatcher.equalsTask;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TaskApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskRestControllerITest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaskRepository taskRepository;

    private Task task1, task2, subtask;

    @Before
    public void setupData() {
        cleanUpData();

        task1 = Task.builder().id("PRJ-1").name("First story name").description("First story description").build();
        task2 = Task.builder().id("PRJ-2").name("Second story name").description("Second story description").build();

        subtask = Task.builder().id("PRJ-11").name("Sub-task name").description("Sub-task description").parentTaskId(task1.getId()).build();

        taskRepository.save(Arrays.asList(task1, task2, subtask));
    }

    @After
    public void cleanUpData() {
        taskRepository.deleteAll();
    }

    @Test
    public void testGetTask_withId1() {

        ResponseEntity<Task> responseEntity = restTemplate.getForEntity("/tasks/PRJ-1", Task.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));

        Task expectedTask = Task.builder()
                .id("PRJ-1")
                .name("First story name")
                .description("First story description")
                .build();
        assertThat(responseEntity.getBody(), equalsTask(expectedTask));
    }

    @Test
    public void testGetTask_withId2() {

        ResponseEntity<Task> responseEntity = restTemplate.getForEntity("/tasks/PRJ-2", Task.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));

        Task expectedTask = Task.builder()
                .id("PRJ-2")
                .name("Second story name")
                .description("Second story description")
                .build();
        assertThat(responseEntity.getBody(), equalsTask(expectedTask));
    }

    @Test
    public void testGetTask_withSubTask() {

        ResponseEntity<Task> responseEntity = restTemplate.getForEntity("/tasks/PRJ-11", Task.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));

        Task expectedTask = Task.builder()
                .id("PRJ-11")
                .name("Sub-task name")
                .description("Sub-task description")
                .parentTaskId("PRJ-1")
                .build();
        assertThat(responseEntity.getBody(), equalsTask(expectedTask));
    }

    @Test
    public void testGetTask_notFound() {

        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/tasks/NOT-FOUND", String.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void testCreateTask() {

        Task newTaskTO = Task.builder().id("PRJ-3").name("New story name").description("New story description").build();

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/tasks", newTaskTO, Void.class);

        URI uriForCreatedTask = responseEntity.getHeaders().getLocation();
        assertThat(uriForCreatedTask.toString(), endsWith("/tasks/PRJ-3"));

        Task createdTask = taskRepository.findOne("PRJ-3");
        assertThat(createdTask.getName(), equalTo("New story name"));
        assertThat(createdTask.getDescription(), equalTo("New story description"));
        assertThat(createdTask.getParentTaskId().isPresent(), is(false));
    }

    @Test
    public void testCreateTask_withSubtask() {

        Task newTaskTO = Task.builder().id("PRJ-12").name("New sub-task").description("New sub-task description").parentTaskId("PRJ-1").build();

        ResponseEntity<Void> createResponseEntity = restTemplate.postForEntity("/tasks", newTaskTO, Void.class);

        URI uriForCreatedTask = createResponseEntity.getHeaders().getLocation();
        assertThat(uriForCreatedTask.toString(), endsWith("/tasks/PRJ-12"));

        ResponseEntity<Task> getResponseEntity = restTemplate.getForEntity(uriForCreatedTask, Task.class);
        Task expectedTask = Task.builder()
                .id("PRJ-12")
                .name("New sub-task")
                .description("New sub-task description")
                .parentTaskId("PRJ-1")
                .build();

        assertThat(getResponseEntity.getBody(), equalsTask(expectedTask));
    }

    @Test
    public void testCreateTask_withMissingSubtask() throws Exception {

        Task newTaskTO = Task.builder().id("PRJ-12").name("New sub-task").description("New sub-task description").parentTaskId("NOT-FOUND").build();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/tasks", newTaskTO, String.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));

        String expected = "[{'field':'parentTaskId', 'code':'parent-task.not-found'}]";
        JSONAssert.assertEquals(expected, responseEntity.getBody(), false);
    }
}
