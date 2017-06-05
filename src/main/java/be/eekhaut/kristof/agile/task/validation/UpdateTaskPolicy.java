package be.eekhaut.kristof.agile.task.validation;

import be.eekhaut.kristof.agile.task.domain.Task;
import be.eekhaut.kristof.agile.task.validation.rules.ParentTaskMustExistRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("beforeSaveTaskValidator")
public class UpdateTaskPolicy implements Validator {

    private final ParentTaskMustExistRule parentTaskMustExistRule;

    @Autowired
    public UpdateTaskPolicy(ParentTaskMustExistRule parentTaskMustExistRule) {
        this.parentTaskMustExistRule = parentTaskMustExistRule;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Task.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Task task = (Task)o;

        parentTaskMustExistRule.check(task, errors);
    }
}
