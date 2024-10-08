package ru.yandex.practicum.abstractions;

// region imports

import ru.yandex.practicum.models.AbstractTask;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.util.List;
import java.util.Optional;

// endregion

public interface TaskManager {
    //region Задачи

    /**
     * Создать задачу.
     *
     * @param task задача.
     */
    void createTask(Task task);

    /**
     * Получить задачу по её идентификатору.
     *
     * @param taskId идентификатор задачи.
     * @return задача.
     */
    Optional<Task> getTaskById(int taskId);

    /**
     * Получить все задачи.
     *
     * @return коллекция задач.
     */
    List<Task> getAllTasks();

    /**
     * Обновить задачу.
     *
     * @param task задача.
     */
    void updateTask(Task task);

    /**
     * Удалить задачу по её идентификатору.
     *
     * @param taskId идентификатор задачи.
     */
    void removeTaskById(int taskId);

    /**
     * Удалить все задачи.
     */
    void removeAllTasks();

    //endregion

    //region Подзадачи

    /**
     * Создать подзадачу.
     *
     * @param subTask подзадача.
     */
    void createSubTask(SubTask subTask);

    /**
     * Получить подзадачу по её идентификатору.
     *
     * @param subTaskId идентификатор подзадачи.
     * @return подзадача.
     */
    Optional<SubTask> getSubTaskById(int subTaskId);

    /**
     * Получить коллекцию подзадач эпика.
     *
     * @param epic эпик.
     * @return коллекция подзадач.
     */
    List<SubTask> getSubTasksByEpic(Epic epic);

    /**
     * Получить все подзадачи.
     *
     * @return коллекция подзадач.
     */
    List<SubTask> getAllSubTasks();

    /**
     * Обновить подзадачу.
     *
     * @param subTask подзадача.
     */
    void updateSubTask(SubTask subTask);

    /**
     * Удалить подзадачу по её идентификаторку.
     *
     * @param subTaskId идентификатор подзадачи
     */
    void removeSubTaskById(int subTaskId);

    /**
     * Удалить все подзадачи.
     */
    void removeAllSubTasks();

    //endregion

    /**
     * Получить список задач в порядке приоритета.
     *
     * @return список задач.
     */
    List<Task> getPrioritizedTasks();

    // region Эпики

    /**
     * Создать эпик.
     *
     * @param epic эпик.
     */
    void createEpic(Epic epic);

    /**
     * Получить эпик по его идентификатору.
     *
     * @param epicId идентификатор эпика.
     * @return эпик.
     */
    Optional<Epic> getEpicById(int epicId);

    /**
     * Получить все эпики.
     *
     * @return коллекция эпиков.
     */
    List<Epic> getAllEpics();

    /**
     * Обновить эпик.
     *
     * @param epic эпик.
     */
    void updateEpic(Epic epic);

    /**
     * Удалить эпик по его идентификатору.
     *
     * @param epicId идентификатор эпика.
     */
    void removeEpicById(int epicId);

    /**
     * Удалить все эпики.
     */
    void removeAllEpics();

    //endregion

    // region История просмотра

    /**
     * Получить историю просмотра задач.
     *
     * @return история просмотра задач.
     */
    List<AbstractTask> getHistory();

    // endregion
}
