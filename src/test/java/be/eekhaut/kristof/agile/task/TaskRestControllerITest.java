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
import org.springframework.http.RequestEntity;
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

        Task newTask = Task.builder().id("PRJ-3").name("New story name").description("New story description").build();

        ResponseEntity<Task> responseEntity = restTemplate.postForEntity("/tasks", newTask, Task.class);

        URI uriForCreatedTask = responseEntity.getHeaders().getLocation();
        assertThat(uriForCreatedTask.toString(), endsWith("/tasks/PRJ-3"));

        Task expectedTask = Task.builder()
                .id("PRJ-3")
                .name("New story name")
                .description("New story description")
                .build();

        assertThat(responseEntity.getBody(), equalsTask(expectedTask));
    }

    @Test
    public void testCreateTask_withParentTask() {

        Task newTask = Task.builder().id("PRJ-12").name("New sub-task").description("New sub-task description").parentTaskId("PRJ-1").build();

        ResponseEntity<Task> responseEntity = restTemplate.postForEntity("/tasks", newTask, Task.class);

        URI uriForCreatedTask = responseEntity.getHeaders().getLocation();
        assertThat(uriForCreatedTask.toString(), endsWith("/tasks/PRJ-12"));

        Task expectedTask = Task.builder()
                .id("PRJ-12")
                .name("New sub-task")
                .description("New sub-task description")
                .parentTaskId("PRJ-1")
                .build();

        assertThat(responseEntity.getBody(), equalsTask(expectedTask));
    }

    @Test
    public void testCreateTask_withMissingParentTask() throws Exception {

        Task newTask = Task.builder().id("PRJ-12").name("New sub-task").description("New sub-task description").parentTaskId("NOT-FOUND").build();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/tasks", newTask, String.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));

        String expected = "[{'field':'parentTaskId', 'code':'task.parent-task-not-found'}]";
        JSONAssert.assertEquals(expected, responseEntity.getBody(), false);
    }

    @Test
    public void testUpdateTask() {

        Task updatedTask = Task.builder().id("PRJ-1").name("Updated story name").description("Updated story description").build();

        RequestEntity<Task> requestEntity = RequestEntity.put(URI.create("/tasks/PRJ-1")).body(updatedTask);
        ResponseEntity<Task> responseEntity = restTemplate.exchange(requestEntity, Task.class);

        Task expectedTask = Task.builder()
                .id("PRJ-1")
                .name("Updated story name")
                .description("Updated story description")
                .build();
        assertThat(responseEntity.getBody(), equalsTask(expectedTask));
    }

    @Test
    public void testUpdateTask_withParentTask() {

        Task updatedTask = Task.builder()
                .id("PRJ-11")
                .name("Updated sub-task name")
                .description("Updated sub-task description")
                .parentTaskId(task2.getId())
                .build();

        RequestEntity<Task> requestEntity = RequestEntity.put(URI.create("/tasks/PRJ-11")).body(updatedTask);
        ResponseEntity<Task> responseEntity = restTemplate.exchange(requestEntity, Task.class);

        Task expectedTask = Task.builder()
                .id("PRJ-11")
                .name("Updated sub-task name")
                .description("Updated sub-task description")
                .parentTaskId(task2.getId())
                .build();
        assertThat(responseEntity.getBody(), equalsTask(expectedTask));
    }

    @Test
    public void testUpdateTask_withMissingParentTask() throws Exception {

        Task updatedTask = Task.builder()
                .id("PRJ-11")
                .name("Updated sub-task name")
                .description("Updated sub-task description")
                .parentTaskId("NOT-FOUND")
                .build();

        RequestEntity<Task> requestEntity = RequestEntity.put(URI.create("/tasks/PRJ-11")).body(updatedTask);
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));

        String expected = "[{'field':'parentTaskId', 'code':'task.parent-task-not-found'}]";
        JSONAssert.assertEquals(expected, responseEntity.getBody(), false);
    }

    @Test
    public void testDeleteTask() {

        restTemplate.delete("/tasks/PRJ-2");

        assertThat(taskRepository.exists("PRJ-2"), is(false));
    }

    @Test
    public void testDeleteTask_withSubTask() throws Exception {

        RequestEntity<Void> requestEntity = RequestEntity.delete(URI.create("/tasks/PRJ-11")).build();
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        assertThat(taskRepository.exists("PRJ-1"), is(true));
    }
}
