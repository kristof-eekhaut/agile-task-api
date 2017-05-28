package be.eekhaut.kristof.agile.task.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Optional;

@Document
public class Task {

    @Id
    private String id;

    private String name;

    private String description;

    private String parentTaskId;

    Task() {
        super();
    }

    private Task(Builder builder) {
        id = builder.id;
        name = builder.name;
        description = builder.description;
        parentTaskId = builder.parentTaskId;
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

    public Optional<String> getParentTaskId() {
        return Optional.ofNullable(parentTaskId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Task other = (Task) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(id, other.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).hashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String id;
        private String name;
        private String description;
        private String parentTaskId;

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

        public Builder parentTaskId(String val) {
            parentTaskId = val;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }
}
