package be.eekhaut.kristof.agile.task;

import be.eekhaut.kristof.agile.task.api.CreateTaskTO;
import be.eekhaut.kristof.agile.task.api.TaskTO;
import be.eekhaut.kristof.agile.task.domain.TaskService;
import be.eekhaut.kristof.agile.task.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class TaskRestController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks/{id}")
    public TaskTO getTask(@PathVariable String id) {
        TaskTO foundTask = taskService.getTask(id);

        if(foundTask == null) {
            throw new NotFoundException();
        }
        return foundTask;
    }

    @PostMapping("/tasks")
    public ResponseEntity<Void> createTask(@RequestBody CreateTaskTO createTaskTO) {
        TaskTO createdTask = taskService.createTask(createTaskTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTask.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }
}
