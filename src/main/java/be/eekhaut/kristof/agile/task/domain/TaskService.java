package be.eekhaut.kristof.agile.task.domain;

import be.eekhaut.kristof.agile.task.api.CreateTaskTO;
import be.eekhaut.kristof.agile.task.api.TaskTO;
import be.eekhaut.kristof.agile.task.exception.BusinessErrorCode;
import be.eekhaut.kristof.agile.task.exception.BusinessException;
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

        return mapToTO(task);
    }

    private TaskTO mapToTO(Task task) {
        return TaskTO.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .parentTaskId(task.getParentTask().map(Task::getId).orElse(null))
                .build();
    }

    public TaskTO createTask(CreateTaskTO createTaskTO) {
        Task task = Task.builder()
                .id(createTaskTO.getId())
                .name(createTaskTO.getName())
                .description(createTaskTO.getDescription())
                .parentTask(checkAndReturnParentTask(createTaskTO.getParentTaskId()))
                .build();

        return mapToTO(taskRepository.save(task));
    }

    private Task checkAndReturnParentTask(String parentTaskId) {
        if(parentTaskId == null) {
            return null;
        }
        Task parentTask = taskRepository.findOne(parentTaskId);
        if(parentTask == null) {
            throw new BusinessException(BusinessErrorCode.PARENT_TASK_NOT_FOUND, "Parent task could not be found.");
        }
        return parentTask;
    }
}
