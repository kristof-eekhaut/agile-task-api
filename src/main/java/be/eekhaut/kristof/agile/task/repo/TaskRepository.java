package be.eekhaut.kristof.agile.task.repo;

import be.eekhaut.kristof.agile.task.domain.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, String> {

}
