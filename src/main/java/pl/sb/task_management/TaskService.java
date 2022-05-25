package pl.sb.task_management;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sb.task_management.exceptions.TaskNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    Task addTask(TaskDto taskDto) {
        Task task = new Task(taskDto.getId(),
                taskDto.getName(),
                taskDto.getDescription(),
                taskDto.getCategory(),
                taskDto.getStatus(),
                taskDto.getDeadline());
        return taskRepository.save(task);
    }

    TaskDto findTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
        return convertToDto(task);
    }

    private TaskDto convertToDto(Task task) {
        return new TaskDto(task.getId(),
                task.getName(),
                task.getDescription(),
                task.getCategory(),
                task.getStatus(),
                task.getDeadline());
    }

    @Transactional
    void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    @Transactional
    void modify(Long id, String name, String description, TaskCategory category, Status status, LocalDate deadline) {
        TaskDto taskDto = findTaskById(id);
        taskDto.setName(name);
        taskDto.setDescription(description);
        taskDto.setCategory(category);
        taskDto.setStatus(status);
        taskDto.setDeadline(deadline);
        Task task = addTask(taskDto);
        taskRepository.save(task);
    }

    @Transactional
    void changeStatus(Long id) {
        TaskDto taskDto = findTaskById(id);
        Status status = taskDto.getStatus();
        if (status == Status.DONE) {
            taskDto.setStatus(Status.TODO);
        } else {
            taskDto.setStatus(Status.DONE);
        }
        Task task = addTask(taskDto);
        taskRepository.save(task);
    }

    public List<TaskDto> getAllSortedByDeadline() {
        return new ArrayList<>(taskRepository.findAllByOrderByDeadlineAsc()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getAllTasksByStatusSortedByDeadline(Status status) {
        return new ArrayList<>(taskRepository.findAllByStatusEqualsOrderByDeadlineAsc(status)).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}