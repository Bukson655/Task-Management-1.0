package pl.sb.task_management;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface TaskRepository extends CrudRepository<Task, Long> {

    List<Task> findAllByStatusEqualsOrderByDeadlineAsc(Status status);

    List<Task> findAllByOrderByDeadlineAsc();
}
