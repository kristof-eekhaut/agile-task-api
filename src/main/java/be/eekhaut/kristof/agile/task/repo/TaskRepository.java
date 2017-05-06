package be.eekhaut.kristof.agile.task.repo;

import be.eekhaut.kristof.agile.task.domain.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, String> {

}
