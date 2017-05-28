package be.eekhaut.kristof.agile.task.rules;

import be.eekhaut.kristof.agile.task.domain.Task;
import be.eekhaut.kristof.agile.task.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ParentTaskMustExistRule {

    @Autowired
    private TaskRepository taskRepository;

    public void check(Task task, Errors errors) {
        if(task.getParentTaskId().isPresent()) {
            Task parentTask = taskRepository.findOne(task.getParentTaskId().get());
            if (parentTask == null) {
                errors.rejectValue("parentTaskId", "parent-task.not-found");
            }
        }
    }
}
