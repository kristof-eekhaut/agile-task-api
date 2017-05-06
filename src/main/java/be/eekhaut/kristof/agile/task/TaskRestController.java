package be.eekhaut.kristof.agile.task;

import be.eekhaut.kristof.agile.task.api.TaskTO;
import be.eekhaut.kristof.agile.task.domain.TaskService;
import be.eekhaut.kristof.agile.task.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
}
