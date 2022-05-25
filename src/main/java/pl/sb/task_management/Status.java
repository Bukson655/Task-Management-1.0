package pl.sb.task_management;

public enum Status {

    TODO("Niewykonane"),
    DONE("Wykonane");

    private final String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}