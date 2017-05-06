package be.eekhaut.kristof.agile.task;

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

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TaskApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskRestControllerITest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaskRepository taskRepository;

    private Task task1, task2;

    @Before
    public void setupData() {
        task1 = Task.builder().id("PRJ-1").name("First story name").description("First story description").build();
        task2 = Task.builder().id("PRJ-2").name("Second story name").description("Second story description").build();

        taskRepository.save(Arrays.asList(task1, task2));
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
    public void testGetTask_notFound() {

        ResponseEntity<TaskTO> responseEntity = restTemplate.getForEntity("/tasks/NOT-FOUND", TaskTO.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }
}
