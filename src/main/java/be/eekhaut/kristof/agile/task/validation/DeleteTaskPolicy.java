package be.eekhaut.kristof.agile.task.validation;

import be.eekhaut.kristof.agile.task.domain.Task;
import be.eekhaut.kristof.agile.task.validation.rules.DeleteOfTaskWithSubTasksNotAllowedRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("beforeDeleteTaskValidator")
public class DeleteTaskPolicy implements Validator {

    private final DeleteOfTaskWithSubTasksNotAllowedRule deleteOfTaskWithSubTasksNotAllowedRule;

    @Autowired
    public DeleteTaskPolicy(DeleteOfTaskWithSubTasksNotAllowedRule deleteOfTaskWithSubTasksNotAllowedRule) {
        this.deleteOfTaskWithSubTasksNotAllowedRule = deleteOfTaskWithSubTasksNotAllowedRule;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Task.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Task task = (Task)o;

        deleteOfTaskWithSubTasksNotAllowedRule.check(task, errors);
    }
}
