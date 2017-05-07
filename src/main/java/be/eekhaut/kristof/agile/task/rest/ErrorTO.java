package be.eekhaut.kristof.agile.task.rest;

public class ErrorTO {

    private String errorKey;

    public ErrorTO(String errorKey) {
        this.errorKey = errorKey;
    }

    public String getErrorKey() {
        return errorKey;
    }
}
