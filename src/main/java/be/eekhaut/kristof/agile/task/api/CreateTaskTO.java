package be.eekhaut.kristof.agile.task.api;

public class CreateTaskTO {

    private String id;
    private String name;
    private String description;
    private String parentTaskId;

    private CreateTaskTO() {
        // For marshalling
    }

    private CreateTaskTO(Builder builder) {
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

    public String getParentTaskId() {
        return parentTaskId;
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

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder parentTaskId(String parentTaskId) {
            this.parentTaskId = parentTaskId;
            return this;
        }

        public CreateTaskTO build() {
            return new CreateTaskTO(this);
        }
    }
}
