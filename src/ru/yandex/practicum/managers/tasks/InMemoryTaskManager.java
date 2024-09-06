package ru.yandex.practicum.managers.tasks;

//region imports

import ru.yandex.practicum.abstractions.HistoryManager;
import ru.yandex.practicum.abstractions.TaskManager;
import ru.yandex.practicum.constants.TaskStatus;
import ru.yandex.practicum.managers.history.InMemoryHistoryManager;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.util.ArrayList;
import java.util.HashMap;

// endregion

public final class InMemoryTaskManager implements TaskManager {
    /**
     * Список задач.
     */
    public final HashMap<Integer, Task> tasks;

    /**
     * Список подзадач.
     */
    public final HashMap<Integer, SubTask> subTasks;

    /**
     * Список эпиков.
     */
    public final HashMap<Integer, Epic> epics;

    /**
     * История просмотра задач.
     */
    private final HistoryManager<Integer, Task> historyManager;

    /**
     * Конструктор.
     */
    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();

        this.historyManager = new InMemoryHistoryManager<>();
    }

    //region Задачи

    /**
     * Создать задачу.
     *
     * @param task задача.
     */
    public void createTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Parameter 'task' can't be null");
        }

        if (task.getStatus() != TaskStatus.NEW) {
            throw new IllegalStateException("Создание задачи возможно только в статусе 'NEW'. Текущий статус: '" + task.getStatus().name() + "'");
        }

        if (this.tasks.containsKey(task.getId())) {
            throw new IllegalStateException("Задача с идентификатором " + task.getId() + " уже создана");
        }

        this.tasks.put(task.getId(), task);
    }

    /**
     * Получить задачу по её идентификатору.
     *
     * @param taskId идентификатор задачи.
     * @return задача.
     */
    public Task getTaskById(int taskId) {
        Task task = this.tasks.get(taskId);

        this.historyManager.add(taskId, task);

        return task;
    }

    /**
     * Получить все задачи.
     *
     * @return коллекция задач.
     */
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    /**
     * Обновить задачу.
     *
     * @param task задача.
     */
    public void updateTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Parameter 'task' can't be null");
        }

        if (!this.tasks.containsKey(task.getId())) {
            throw new IllegalStateException("Задача с идентификатором " + task.getId() + " не найден");
        }

        this.tasks.put(task.getId(), task);
    }

    /**
     * Удалить задачу по её идентификатору.
     *
     * @param taskId идентификатор задачи.
     */
    public void removeTaskById(int taskId) {
        if (!this.tasks.containsKey(taskId)) {
            throw new IllegalStateException("Задача с идентификатором " + taskId + " не найдена");
        }

        this.tasks.remove(taskId);
    }

    /**
     * Удалить все задачи.
     */
    public void removeAllTasks() {
        this.tasks.clear();
    }

    //endregion

    //region Подзадачи

    /**
     * Создать подзадачу.
     *
     * @param subTask подзадача.
     */
    public void createSubTask(SubTask subTask) {
        if (subTask == null) {
            throw new IllegalArgumentException("Parameter 'subTask' can't be null");
        }

        if (this.subTasks.containsKey(subTask.getId())) {
            throw new IllegalStateException("Подзадача с идентификатором " + subTask.getId() + " уже создана");
        }

        if (subTask.getStatus() != TaskStatus.NEW) {
            throw new IllegalStateException("Создание подзадачи возможно только в статусе 'NEW'. Текущий статус: '" + subTask.getStatus().name() + "'");
        }

        if (!this.epics.containsKey(subTask.getEpic().getId())) {
            throw new IllegalStateException("Создание подзадачи возможно только после создания эпика");
        }

        if (this.epics.get(subTask.getEpic().getId()).getSubTaskById(subTask.getId()) == null) {
            throw new IllegalStateException("Создание подзадачи возможно только после её добавления в эпик");
        }

        this.subTasks.put(subTask.getId(), subTask);
    }

    /**
     * Получить подзадачу по её идентификатору.
     *
     * @param subTaskId идентификатор подзадачи.
     * @return подзадача.
     */
    public SubTask getSubTaskById(int subTaskId) {
        SubTask subtask = this.subTasks.get(subTaskId);

        this.historyManager.add(subTaskId, subtask);

        return subtask;
    }

    /**
     * Получить коллекцию подзадач эпика.
     *
     * @param epic эпик.
     * @return коллекция подзадач.
     */
    public ArrayList<SubTask> getSubTasksByEpic(Epic epic) {
        ArrayList<SubTask> subTasks = new ArrayList<>();

        for (SubTask subTask : this.subTasks.values()) {
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
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(this.subTasks.values());
    }

    /**
     * Обновить подзадачу.
     *
     * @param subTask подзадача.
     */
    public void updateSubTask(SubTask subTask) {
        if (subTask == null) {
            throw new IllegalArgumentException("Parameter 'subTask' can't be null");
        }

        if (!this.subTasks.containsKey(subTask.getId())) {
            throw new IllegalStateException("Подзадача с идентификатором " + subTask.getId() + " не найдена");
        }

        subTask.getEpic().updateSubTask(subTask);
        subTask.getEpic().updateStatus();

        this.subTasks.put(subTask.getId(), subTask);
    }

    /**
     * Удалить подзадачу по её идентификаторку.
     *
     * @param subTaskId идентификатор подзадачи
     */
    public void removeSubTaskById(int subTaskId) {
        if (!this.subTasks.containsKey(subTaskId)) {
            throw new IllegalStateException("Подзадача с идентификатором " + subTaskId + " не найдена");
        }

        SubTask subTask = this.subTasks.get(subTaskId);

        subTask.getEpic().removeSubTask(subTask);
        subTask.getEpic().updateStatus();

        this.subTasks.remove(subTaskId);
    }

    /**
     * Удалить все подзадачи.
     */
    public void removeAllSubTasks() {
        for (SubTask subTask : this.subTasks.values()) {
            Epic epic = subTask.getEpic();
            if (epic != null) {
                epic.removeSubTask(subTask);
                epic.updateStatus();
            }
        }

        this.subTasks.clear();
    }

    //endregion

    // region Эпики

    /**
     * Создать эпик.
     *
     * @param epic эпик.
     */
    public void createEpic(Epic epic) {
        if (epic == null) {
            throw new IllegalArgumentException("Parameter 'epic' can't be null");
        }

        if (epic.getStatus() != TaskStatus.NEW) {
            throw new IllegalStateException("Создание эпика возможно только в статусе 'NEW'. Текущий статус: '" + epic.getStatus().name() + "'");
        }

        if (this.epics.containsKey(epic.getId())) {
            throw new IllegalStateException("Эпик с идентификатором " + epic.getId() + " уже создан");
        }

        this.epics.put(epic.getId(), epic);
    }

    /**
     * Получить эпик по его идентификатору.
     *
     * @param epicId идентификатор эпика.
     * @return эпик.
     */
    public Epic getEpicById(int epicId) {
        Epic epic = this.epics.get(epicId);

        this.historyManager.add(epicId, epic);

        return epic;
    }

    /**
     * Получить все эпики.
     *
     * @return коллекция эпиков.
     */
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(this.epics.values());
    }

    /**
     * Обновить эпик.
     *
     * @param epic эпик.
     */
    public void updateEpic(Epic epic) {
        if (epic == null) {
            throw new IllegalArgumentException("Parameter 'epic' can't be null");
        }

        if (!this.epics.containsKey(epic.getId())) {
            throw new IllegalStateException("Эпик с идентификатором " + epic.getId() + " не найден");
        }

        this.epics.put(epic.getId(), epic);
    }

    /**
     * Удалить эпик по его идентификатору.
     *
     * @param epicId идентификатор эпика.
     */
    public void removeEpicById(int epicId) {
        if (!this.epics.containsKey(epicId)) {
            throw new IllegalStateException("Эпик с идентификатором " + epicId + " не найден");
        }

        Epic epic = this.epics.get(epicId);

        for (SubTask subTask : epic.getAllSubTasks()) {
            this.subTasks.remove(subTask.getId());
        }

        epic.removeAllSubTasks();
        this.epics.remove(epicId);
    }

    /**
     * Удалить все эпики.
     */
    public void removeAllEpics() {
        this.subTasks.clear();
        this.epics.clear();
    }

    //endregion
}
