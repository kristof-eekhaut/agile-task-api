package be.eekhaut.kristof.agile.task.rest;

public class ErrorTO {

    private String field;
    private String code;

    public ErrorTO(String code) {
        this.code = code;
    }

    public ErrorTO(String code, String field) {
        this(code);
        this.field = field;
    }

    public String getCode() {
        return code;
    }

    public String getField() {
        return field;
    }
}
