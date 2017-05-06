package be.eekhaut.kristof.agile.task;

import be.eekhaut.kristof.agile.task.api.CreateTaskTO;
import be.eekhaut.kristof.agile.task.api.TaskTO;
import be.eekhaut.kristof.agile.task.domain.Task;
import be.eekhaut.kristof.agile.task.repo.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.Arrays;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
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
        task1 = Task.builder().id("PRJ-1").name("First story name").description("First story description").build();
        task2 = Task.builder().id("PRJ-2").name("Second story name").description("Second story description").build();

        subtask = Task.builder().id("PRJ-11").name("Sub-task name").description("Sub-task description").parentTask(task1).build();

        taskRepository.save(Arrays.asList(task1, task2, subtask));
    }

    @Test
    public void testGetTask_withId1() {

        ResponseEntity<TaskTO> responseEntity = restTemplate.getForEntity("/tasks/PRJ-1", TaskTO.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));

        TaskTO expectedTask = TaskTO.builder()
                .id("PRJ-1")
                .name("First story name")
                .description("First story description")
                .build();
        assertThat(responseEntity.getBody(), equalTo(expectedTask));
    }

    @Test
    public void testGetTask_withId2() {

        ResponseEntity<TaskTO> responseEntity = restTemplate.getForEntity("/tasks/PRJ-2", TaskTO.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));

        TaskTO expectedTask = TaskTO.builder()
                .id("PRJ-2")
                .name("Second story name")
                .description("Second story description")
                .build();
        assertThat(responseEntity.getBody(), equalTo(expectedTask));
    }

    @Test
    public void testGetTask_withSubTask() {

        ResponseEntity<TaskTO> responseEntity = restTemplate.getForEntity("/tasks/PRJ-11", TaskTO.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));

        TaskTO expectedTask = TaskTO.builder()
                .id("PRJ-11")
                .name("Sub-task name")
                .description("Sub-task description")
                .parentTaskId("PRJ-1")
                .build();
        assertThat(responseEntity.getBody(), equalTo(expectedTask));
    }

    @Test
    public void testGetTask_notFound() {

        ResponseEntity<TaskTO> responseEntity = restTemplate.getForEntity("/tasks/NOT-FOUND", TaskTO.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void testCreateTask() {

        CreateTaskTO newTaskTO = CreateTaskTO.builder().id("PRJ-3").name("Third story name").description("Third story description").build();

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/tasks", newTaskTO, Void.class);

        URI uriForCreatedTask = responseEntity.getHeaders().getLocation();
        assertThat(uriForCreatedTask.toString(), endsWith("/tasks/PRJ-3"));

        Task createdTask = taskRepository.findOne("PRJ-3");
        assertThat(createdTask.getName(), equalTo("Third story name"));
        assertThat(createdTask.getDescription(), equalTo("Third story description"));
    }
}
