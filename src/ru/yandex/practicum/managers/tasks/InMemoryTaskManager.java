package ru.yandex.practicum.managers.tasks;

//region imports

import ru.yandex.practicum.abstractions.HistoryManager;
import ru.yandex.practicum.abstractions.TaskManager;
import ru.yandex.practicum.constants.TaskStatus;
import ru.yandex.practicum.managers.history.InMemoryHistoryManager;
import ru.yandex.practicum.models.AbstractTask;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.util.*;

// endregion

public class InMemoryTaskManager implements TaskManager {
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
    private final HistoryManager<Integer, AbstractTask> historyManager;

    /**
     * Конструктор.
     */
    public InMemoryTaskManager() {
        this.tasks = new LinkedHashMap<>();
        this.subTasks = new LinkedHashMap<>();
        this.epics = new LinkedHashMap<>();

        this.historyManager = new InMemoryHistoryManager<>();
    }

    //region Задачи

    /**
     * Создать задачу.
     *
     * @param task задача.
     */
    @Override
    public void createTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Parameter 'task' can't be null");
        }

        if (this.tasks.containsKey(task.getId())) {
            throw new IllegalStateException("Задача с идентификатором " + task.getId() + " уже создана");
        }

        if (task.getStatus() != TaskStatus.NEW) {
            throw new IllegalStateException("Создание задачи возможно только в статусе 'NEW'. Текущий статус: '" + task.getStatus().name() + "'");
        }

        if (this.tasks.values().stream().anyMatch(t -> t.isCrossed(task)) || this.subTasks.values().stream().anyMatch(st -> st.isCrossed(task))) {
            throw new IllegalStateException("Задача с идентификатором " + task.getId() + " пересекается с другой задачей по времени выполнения");
        }

        this.tasks.put(task.getId(), task);
    }

    /**
     * Получить задачу по её идентификатору.
     *
     * @param taskId идентификатор задачи.
     * @return задача.
     */
    @Override
    public Optional<Task> getTaskById(int taskId) {
        Task task = this.tasks.get(taskId);

        if (task != null) {
            this.historyManager.add(taskId, task);
        }

        return Optional.ofNullable(task);
    }

    /**
     * Получить все задачи.
     *
     * @return коллекция задач.
     */
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    /**
     * Обновить задачу.
     *
     * @param task задача.
     */
    @Override
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
    @Override
    public void removeTaskById(int taskId) {
        if (!this.tasks.containsKey(taskId)) {
            throw new IllegalStateException("Задача с идентификатором " + taskId + " не найдена");
        }

        this.historyManager.remove(taskId);
        this.tasks.remove(taskId);
    }

    /**
     * Удалить все задачи.
     */
    @Override
    public void removeAllTasks() {
        for (Integer taskId : this.tasks.keySet()) {
            this.historyManager.remove(taskId);
        }

        this.tasks.clear();
    }

    //endregion

    //region Подзадачи

    /**
     * Создать подзадачу.
     *
     * @param subTask подзадача.
     */
    @Override
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

        if (this.tasks.values().stream().anyMatch(t -> t.isCrossed(subTask)) || this.subTasks.values().stream().anyMatch(st -> st.isCrossed(subTask))) {
            throw new IllegalStateException("Подзадача с идентификатором " + subTask.getId() + " пересекается с другой задачей по времени выполнения");
        }

        if (!this.epics.containsKey(subTask.getEpic().getId())) {
            throw new IllegalStateException("Создание подзадачи возможно только после создания эпика");
        }

        if (this.epics.get(subTask.getEpic().getId()).getSubTaskById(subTask.getId()).isEmpty()) {
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
    @Override
    public Optional<SubTask> getSubTaskById(int subTaskId) {
        SubTask subTask = this.subTasks.get(subTaskId);

        if (subTask != null) {
            this.historyManager.add(subTaskId, subTask);
        }

        return Optional.ofNullable(subTask);
    }

    /**
     * Получить коллекцию подзадач эпика.
     *
     * @param epic эпик.
     * @return коллекция подзадач.
     */
    @Override
    public List<SubTask> getSubTasksByEpic(Epic epic) {
        return this.subTasks.values().stream().filter(st -> st.getEpic().equals(epic)).toList();
    }

    /**
     * Получить все подзадачи.
     *
     * @return коллекция подзадач.
     */
    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(this.subTasks.values());
    }

    /**
     * Обновить подзадачу.
     *
     * @param subTask подзадача.
     */
    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTask == null) {
            throw new IllegalArgumentException("Parameter 'subTask' can't be null");
        }

        if (!this.subTasks.containsKey(subTask.getId())) {
            throw new IllegalStateException("Подзадача с идентификатором " + subTask.getId() + " не найдена");
        }

        subTask.getEpic().updateSubTask(subTask);

        this.subTasks.put(subTask.getId(), subTask);
    }

    /**
     * Удалить подзадачу по её идентификатору.
     *
     * @param subTaskId идентификатор подзадачи
     */
    @Override
    public void removeSubTaskById(int subTaskId) {
        if (!this.subTasks.containsKey(subTaskId)) {
            throw new IllegalStateException("Подзадача с идентификатором " + subTaskId + " не найдена");
        }

        SubTask subTask = this.subTasks.get(subTaskId);

        subTask.getEpic().removeSubTask(subTask);

        this.historyManager.remove(subTaskId);
        this.subTasks.remove(subTaskId);
    }

    /**
     * Удалить все подзадачи.
     */
    @Override
    public void removeAllSubTasks() {
        for (SubTask subTask : this.subTasks.values()) {
            Epic epic = subTask.getEpic();

            epic.removeSubTask(subTask);

            this.historyManager.remove(subTask.getId());
        }

        this.subTasks.clear();
    }


    //endregion

    /**
     * Получить список задач в порядке приоритета.
     *
     * @return список задач.
     */
    @Override
    public List<Task> getPrioritizedTasks() {
        ArrayList<Task> result = new ArrayList<>();

        result.addAll(this.tasks.values().stream().filter(t -> t.getStartTime() != null).toList());
        result.addAll(this.subTasks.values().stream().filter(st -> st.getStartTime() != null).toList());

        result.sort(Comparator.comparing(Task::getStartTime));

        return result;
    }

    // region Эпики

    /**
     * Создать эпик.
     *
     * @param epic эпик.
     */
    @Override
    public void createEpic(Epic epic) {
        if (epic == null) {
            throw new IllegalArgumentException("Parameter 'epic' can't be null");
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
    @Override
    public Optional<Epic> getEpicById(int epicId) {
        Epic epic = this.epics.get(epicId);

        if (epic != null) {
            this.historyManager.add(epicId, epic);
        }

        return Optional.ofNullable(epic);
    }

    /**
     * Получить все эпики.
     *
     * @return коллекция эпиков.
     */
    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(this.epics.values());
    }

    /**
     * Обновить эпик.
     *
     * @param epic эпик.
     */
    @Override
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
    @Override
    public void removeEpicById(int epicId) {
        if (!this.epics.containsKey(epicId)) {
            throw new IllegalStateException("Эпик с идентификатором " + epicId + " не найден");
        }

        Epic epic = this.epics.get(epicId);

        for (SubTask subTask : epic.getAllSubTasks()) {
            this.historyManager.remove(subTask.getId());
            this.subTasks.remove(subTask.getId());
        }

        epic.removeAllSubTasks();

        this.historyManager.remove(epicId);
        this.epics.remove(epicId);
    }

    /**
     * Удалить все эпики.
     */
    @Override
    public void removeAllEpics() {
        for (Integer subTaskId : this.subTasks.keySet()) {
            this.historyManager.remove(subTaskId);
        }

        this.subTasks.clear();

        for (Integer epicId : this.epics.keySet()) {
            this.historyManager.remove(epicId);
        }

        this.epics.clear();
    }

    //endregion

    // region История просмотра

    /**
     * Получить историю просмотра задач.
     *
     * @return история просмотра задач.
     */
    @Override
    public List<AbstractTask> getHistory() {
        return this.historyManager.getHistory();
    }

    // endregion
}
