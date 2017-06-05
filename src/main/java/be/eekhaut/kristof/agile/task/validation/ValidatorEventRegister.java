package be.eekhaut.kristof.agile.task.validation;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * TODO: this is a bypass due to a bug in Spring Data REST (https://jira.spring.io/browse/DATAREST-524)
 */

@Configuration
public class ValidatorEventRegister implements InitializingBean {

    private final ValidatingRepositoryEventListener validatingRepositoryEventListener;
    private final Map<String, Validator> validators;

    @Autowired
    public ValidatorEventRegister(ValidatingRepositoryEventListener validatingRepositoryEventListener,
                                  Map<String, Validator> validators) {
        this.validatingRepositoryEventListener = validatingRepositoryEventListener;
        this.validators = validators;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<String> events = Arrays.asList("beforeCreate", "beforeSave", "beforeDelete");
        for (Map.Entry<String, Validator> entry : validators.entrySet()) {
            events.stream()
                    .filter(p -> entry.getKey().startsWith(p))
                    .findFirst()
                    .ifPresent(
                            p -> validatingRepositoryEventListener
                                    .addValidator(p, entry.getValue()));
        }
    }
}
