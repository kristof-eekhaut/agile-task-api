package be.eekhaut.kristof.agile.task.domain;

import javax.persistence.*;
import java.util.Optional;

@Entity
public class Task {

    @Id
    @Column(name = "TASK_ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "PARENT_TASK_ID")
    private Task parentTask;

    Task() {
        super();
    }

    private Task(Builder builder) {
        id = builder.id;
        name = builder.name;
        description = builder.description;
        parentTask = builder.parentTask;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Optional<Task> getParentTask() {
        return Optional.ofNullable(parentTask);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String id;
        private String name;
        private String description;
        private Task parentTask;

        public Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder parentTask(Task val) {
            parentTask = val;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }
}