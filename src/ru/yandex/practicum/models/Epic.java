package ru.yandex.practicum.models;

//region imports

import ru.yandex.practicum.constants.TaskStatus;
import ru.yandex.practicum.constants.TaskType;

import java.util.ArrayList;
import java.util.HashMap;

//endregion

/**
 * Эпик.
 */
public final class Epic extends Task {
    /**
     * Коллекция подзадач.
     */
    private final HashMap<Integer, SubTask> subTasks;

    /**
     * Конструктор.
     *
     * @param name        название задачи.
     * @param description описание задачи.
     */
    public Epic(String name, String description) {
        super(name, description);

        this.subTasks = new HashMap<>();
    }

    /**
     * Конструктор.
     *
     * @param id          идентификатор задачи.
     * @param name        название задачи.
     * @param description описание задачи.
     * @param status      статус задачи.
     * @param subTasks    коллекция подзадач.
     */
    public Epic(int id, String name, String description, TaskStatus status, HashMap<Integer, SubTask> subTasks) {
        super(id, name, description, status);

        this.subTasks = subTasks;
    }

    /**
     * Добавить подзадачу.
     *
     * @param subTask подзадача.
     */
    public void addSubTask(SubTask subTask) {
        if (subTask == null) {
            throw new IllegalArgumentException("Parameter subTask can't be null");
        }

        if (this.subTasks.containsKey(subTask.getId())) {
            throw new IllegalStateException("Подзадача с идентификатором " + subTask.getId() + "уже добавлена в эпик");
        }

        if (subTask.getEpic() != null && subTask.getEpic().getId() != this.id) {
            throw new IllegalStateException("Подзадача с идентификатором " + subTask.getId() + "уже связана с другим эпиком");
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
        return this.subTasks.get(subTaskId);
    }

    /**
     * Получить все подзадачи эпика.
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
            throw new IllegalArgumentException("Parameter subTask can't be null");
        }

        if (!this.subTasks.containsKey(subTask.getId())) {
            throw new IllegalStateException("Подзадача с идентификатором " + subTask.getId() + " не найдена");
        }

        this.subTasks.put(subTask.getId(), subTask);
    }

    /**
     * Удалить подзадачу.
     *
     * @param subTask подзадача.
     */
    public void removeSubTask(SubTask subTask) {
        if (subTask == null) {
            throw new IllegalArgumentException("Parameter subTask can't be null");
        }

        if (!this.subTasks.containsKey(subTask.getId())) {
            throw new IllegalStateException("Подзадача с идентификатором " + subTask.getId() + " не найдена");
        }

        this.subTasks.remove(subTask.getId());
    }

    /**
     * Удалить все подзадачи.
     */
    public void removeAllSubTasks() {
        this.subTasks.clear();
    }

    /**
     * Обновить статус эпика.
     */
    public void updateStatus() {
        if (this.subTasks.isEmpty()) {
            this.status = TaskStatus.NEW;
            return;
        }

        int newCount = 0;
        int doneCount = 0;

        for (SubTask subTask : this.subTasks.values()) {
            switch (subTask.getStatus()) {
                case NEW:
                    newCount++;
                    break;
                case DONE:
                    doneCount++;
                    break;
            }
        }

        if (newCount == this.subTasks.size()) {
            this.status = TaskStatus.NEW;
        } else if (doneCount == this.subTasks.size()) {
            this.status = TaskStatus.DONE;
        } else {
            this.status = TaskStatus.IN_PROGRESS;
        }
    }

    // region Overrides of ru.yandex.practicum.models.Task

    /**
     * Преобразовать объект эпика в строку в формате CSV.
     *
     * @return объект эпика в виде строки в формате CSV.
     */
    @Override
    public String toCsvString() {
        return String.join(",", String.valueOf(this.id), TaskType.EPIC.name(), this.name, this.status.name(), this.description);
    }

    // endregion

    // region Overrides of java.lang.Object

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + "id: " + this.id + ", " + "name: " + this.name + ", " + "description: " + this.description + ", " + "status: " + this.status.name() + ", " + "sub_task_count: " + this.subTasks.size() + "}";
    }

    // region Implements of Cloneable

    @Override
    public Epic clone() {
        return new Epic(this.id, this.name, this.description, this.status, this.subTasks);
    }

    // endregion

    // endregion
}
