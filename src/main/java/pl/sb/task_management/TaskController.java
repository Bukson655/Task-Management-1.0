package pl.sb.task_management;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/tasks")
    public String tasks(Model model,
                        @RequestParam(required = false, defaultValue = "all-tasks") String show) {

        List<TaskDto> tasksDto;
        switch (show) {
            case "done-tasks" -> tasksDto = taskService.getAllTasksByStatusSortedByDeadline(Status.DONE);
            case "active-tasks" -> tasksDto = taskService.getAllTasksByStatusSortedByDeadline(Status.TODO);
            default -> tasksDto = taskService.getAllSortedByDeadline();
        }
        if (tasksDto.isEmpty()) {
            return "emptylist";
        }

        model.addAttribute("tasks", tasksDto);
        return "tasks";
    }

    @GetMapping("/add-task")
    String addTask(Model model) {
        TaskDto taskDto = new TaskDto();
        model.addAttribute("task", taskDto);
        return "modify";
    }

    @GetMapping("/modify-task")
    String modify(@RequestParam Long id, Model model) {
        TaskDto taskDto = taskService.findTaskById(id);
        model.addAttribute("task", taskDto);
        return "modify";
    }

    @PostMapping("/modify")
    String modify(TaskDto taskDto) {
        if (taskDto.getId() == null) {
            taskService.addTask(taskDto);
        } else {
            taskService.modify(taskDto.getId(),
                    taskDto.getName(),
                    taskDto.getDescription(),
                    taskDto.getCategory(),
                    taskDto.getStatus(),
                    taskDto.getDeadline());
        }
        return "redirect:/tasks";
    }

    @GetMapping("/delete-task")
    String delete(@RequestParam Long id) {
        taskService.deleteTaskById(id);
        return "redirect:/tasks";
    }

    @GetMapping("/switch-status")
    String changeStatus(@RequestParam Long id) {
        taskService.changeStatus(id);
        return "redirect:/tasks";
    }

}