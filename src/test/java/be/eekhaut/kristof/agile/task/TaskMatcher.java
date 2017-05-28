package be.eekhaut.kristof.agile.task;

import be.eekhaut.kristof.agile.task.domain.Task;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class TaskMatcher extends TypeSafeMatcher<Task> {

    private Task expected;

    private TaskMatcher(Task expected) {
        this.expected = expected;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
    }

    @Override
    protected boolean matchesSafely(Task actual) {
        return EqualsBuilder.reflectionEquals(expected, actual);
    }

    public static TaskMatcher equalsTask(Task expected) {
        return new TaskMatcher(expected);
    }
}
