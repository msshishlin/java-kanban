package ru.yandex.practicum;

//region imports

import ru.yandex.practicum.constants.TaskStatus;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

// endregion

public final class TaskManager {
    /**
     * Список задач.
     */
    public final HashMap<Integer, Task> _tasks;

    /**
     * Список подзадач.
     */
    public final HashMap<Integer, SubTask> _subTasks;

    /**
     * Список эпиков.
     */
    public final HashMap<Integer, Epic> _epics;

    /**
     * Конструктор.
     */
    public TaskManager() {
        this._tasks = new HashMap<>();
        this._subTasks = new HashMap<>();
        this._epics = new HashMap<>();
    }

    //region Задачи

    /**
     * Создать задачу.
     *
     * @param task задача.
     */
    public void createTask(Task task) {
        if (task == null) throw new IllegalArgumentException("Parameter 'task' can't be null");

        if (task.getStatus() != TaskStatus.NEW) {
            throw new IllegalStateException("Создание задачи возможно только в статусе 'NEW'. Текущий статус: '" + task.getStatus().name() + "'");
        }

        if (this._tasks.containsKey(task.getId())) {
            throw new IllegalStateException("Задача с идентификатором " + task.getId() + " уже создана");
        }

        this._tasks.put(task.getId(), task);
    }

    /**
     * Получить задачу по её идентификатору.
     *
     * @param taskId идентификатор задачи.
     * @return задача.
     */
    public Task getTaskById(int taskId) {
        return this._tasks.get(taskId);
    }

    /**
     * Получить все задачи.
     *
     * @return коллекция задач.
     */
    public Collection<Task> getAllTasks() {
        return this._tasks.values();
    }

    /**
     * Обновить задачу.
     *
     * @param task задача.
     */
    public void updateTask(Task task) {
        if (task == null) throw new IllegalArgumentException("Parameter 'task' can't be null");

        if (!this._tasks.containsKey(task.getId())) {
            throw new IllegalStateException("Задачи с идентификатором " + task.getId() + " не существует");
        }

        this._tasks.put(task.getId(), task);
    }

    /**
     * Удалить задачу по её идентификатору.
     *
     * @param taskId идентификатор задачи.
     */
    public void removeTaskById(int taskId) {
        this._tasks.remove(taskId);
    }

    /**
     * Удалить все задачи.
     */
    public void removeAllTasks() {
        this._tasks.clear();
    }

    //endregion

    //region Подзадачи

    /**
     * Создать подзадачу.
     *
     * @param subTask подзадача.
     */
    public void createSubTask(SubTask subTask) {
        if (subTask == null) throw new IllegalArgumentException("Parameter 'subTask' can't be null");

        if (this._subTasks.containsKey(subTask.getId())) {
            throw new IllegalStateException("Подзадача с идентификатором " + subTask.getId() + " уже создана");
        }

        if (subTask.getStatus() != TaskStatus.NEW) {
            throw new IllegalStateException("Создание подзадачи возможно только в статусе 'NEW'. Текущий статус: '" + subTask.getStatus().name() + "'");
        }

        if (subTask.getEpic() == null) {
            throw new IllegalStateException("Создание подзадачи без эпика невозможно");
        }

        if (!this._epics.containsKey(subTask.getEpic().getId())) {
            throw new IllegalStateException("Создание подзадачи возможно только после создания эпика");
        }

        this._subTasks.put(subTask.getId(), subTask);
    }

    /**
     * Получить подзадачу по её идентификатору.
     *
     * @param subTaskId идентификатор подзадачи.
     * @return подзадача.
     */
    public SubTask getSubTaskById(int subTaskId) {
        return this._subTasks.get(subTaskId);
    }

    /**
     * Получить коллекцию подзадач эпика.
     *
     * @param epic эпик.
     * @return коллекция подзадач.
     */
    public Collection<SubTask> getSubTasksByEpic(Epic epic) {
        ArrayList<SubTask> subTasks = new ArrayList<>();

        for (SubTask subTask : this._subTasks.values()) {
            if (subTask.getEpic().equals(epic)) {
                subTasks.add(subTask);
            }
        }

        return subTasks;
    }

    /**
     * Получить все подзадачи.
     *
     * @return коллекция подзадач.
     */
    public Collection<SubTask> getAllSubTasks() {
        return this._subTasks.values();
    }

    /**
     * Обновить подзадачу.
     *
     * @param subTask подзадача.
     */
    public void updateSubTask(SubTask subTask) {
        if (subTask == null) throw new IllegalArgumentException("Parameter 'subTask' can't be null");

        if (!this._subTasks.containsKey(subTask.getId())) {
            throw new IllegalStateException("Подзадачи с идентификатором " + subTask.getId() + " не существует");
        }

        Epic epic = subTask.getEpic();
        if (epic == null) {
            throw new IllegalStateException("Удаление эпика у подзадачи запрещено");
        }

        if (!this._epics.containsKey(epic.getId())) {
            throw new IllegalStateException("Обновление эпика подзадачи возможно только после создания эпика");
        }

        epic.updateSubTask(subTask);
        epic.updateStatus();

        this._subTasks.put(subTask.getId(), subTask);
    }

    /**
     * Удалить подзадачу по её идентификаторку.
     *
     * @param subTaskId идентификатор подзадачи
     */
    public void removeSubTaskById(int subTaskId) {
        SubTask subTask = this._subTasks.get(subTaskId);
        if (subTask == null) {
            return;
        }

        Epic epic = subTask.getEpic();
        if (epic != null) {
            epic.removeSubTask(subTask);
            epic.updateStatus();
        }

        this._subTasks.remove(subTaskId);
    }

    /**
     * Удалить все подзадачи.
     */
    public void removeAllSubTasks() {
        for (SubTask subTask : this._subTasks.values()) {
            Epic epic = subTask.getEpic();
            if (epic != null) {
                epic.removeSubTask(subTask);
                epic.updateStatus();
            }
        }

        this._subTasks.clear();
    }

    //endregion

    // region Эпики

    /**
     * Создать эпик.
     *
     * @param epic эпик.
     */
    public void createEpic(Epic epic) {
        if (epic == null) throw new IllegalArgumentException("Parameter 'epic' can't be null");

        if (epic.getStatus() != TaskStatus.NEW) {
            throw new IllegalStateException("Создание эпика возможно только в статусе 'NEW'. Текущий статус: '" + epic.getStatus().name() + "'");
        }

        if (this._epics.containsKey(epic.getId())) {
            throw new IllegalStateException("Эпик с идентификатором " + epic.getId() + " уже создан");
        }

        this._epics.put(epic.getId(), epic);
    }

    /**
     * Получить эпик по его идентификатору.
     *
     * @param epicId идентификатор эпика.
     * @return эпик.
     */
    public Epic getEpicById(int epicId) {
        return this._epics.get(epicId);
    }

    /**
     * Получить все эпики.
     *
     * @return коллекция эпиков.
     */
    public Collection<Epic> getAllEpics() {
        return this._epics.values();
    }

    /**
     * Обновить эпик.
     *
     * @param epic эпик.
     */
    public void updateEpic(Epic epic) {
        if (epic == null) throw new IllegalArgumentException("Parameter 'epic' can't be null");

        if (!this._epics.containsKey(epic.getId())) {
            throw new IllegalStateException("Эпик с идентификатором " + epic.getId() + " не существует");
        }

        this._epics.put(epic.getId(), epic);
    }

    /**
     * Удалить эпик по его идентификатору.
     *
     * @param epicId идентификатор эпика.
     */
    public void removeEpicById(int epicId) {
        Epic epic = this._epics.get(epicId);
        if (epic == null) {
            return;
        }

        for (SubTask subTask : epic.getAllSubTasks()) {
            this._subTasks.remove(subTask.getId());
        }

        epic.removeAllSubTasks();
        this._epics.remove(epicId);
    }

    /**
     * Удалить все эпики.
     */
    public void removeAllEpics() {
        this._subTasks.clear();
        this._epics.clear();
    }

    //endregion
}
