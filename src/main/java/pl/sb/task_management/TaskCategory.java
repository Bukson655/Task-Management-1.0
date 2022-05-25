package pl.sb.task_management;

enum TaskCategory {

    DEFAULT("Ogólne"),
    HOME("Dom"),
    JOB("Praca"),
    HOBBY("Hobby");

    private final String description;

    TaskCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}