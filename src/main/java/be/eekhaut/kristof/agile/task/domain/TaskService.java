package be.eekhaut.kristof.agile.task.domain;

import be.eekhaut.kristof.agile.task.api.TaskTO;
import be.eekhaut.kristof.agile.task.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public TaskTO getTask(String id) {
        Task task = taskRepository.findOne(id);

        if(task == null) {
            return null;
        }

        return TaskTO.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .parentTaskId(task.getParentTask().map(Task::getId).orElse(null))
                .build();
    }
}
