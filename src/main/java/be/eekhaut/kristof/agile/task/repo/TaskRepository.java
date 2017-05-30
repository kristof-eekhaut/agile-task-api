package be.eekhaut.kristof.agile.task.repo;

import be.eekhaut.kristof.agile.task.domain.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {

    @RestResource(path = "byParentTaskId")
    List<Task> findTasksByParentTaskId(@Param("id") String parentTaskId);
}
