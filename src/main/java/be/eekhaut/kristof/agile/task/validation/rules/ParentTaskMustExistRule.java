package be.eekhaut.kristof.agile.task.validation.rules;

import be.eekhaut.kristof.agile.task.domain.Task;
import be.eekhaut.kristof.agile.task.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ParentTaskMustExistRule {

    private final TaskRepository taskRepository;

    @Autowired
    public ParentTaskMustExistRule(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void check(Task task, Errors errors) {
        if(task.getParentTaskId().isPresent()) {
            Task parentTask = taskRepository.findOne(task.getParentTaskId().get());
            if (parentTask == null) {
                errors.rejectValue("parentTaskId", "task.parent-task-not-found");
            }
        }
    }
}
