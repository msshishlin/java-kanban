package ru.yandex.practicum.models;

//region imports

import ru.yandex.practicum.constants.TaskStatus;

import java.util.Collection;
import java.util.HashMap;

//endregion

/**
 * Эпик.
 */
public final class Epic extends Task {
    /**
     * Коллекция подзадач.
     */
    private final HashMap<Integer, SubTask> _subTasks;

    /**
     * Конструктор.
     *
     * @param name        название задачи.
     * @param description описание задачи.
     */
    public Epic(String name, String description) {
        super(name, description);

        this._subTasks = new HashMap<>();
    }

    /**
     * Добавить подзадачу.
     *
     * @param subTask подзадача.
     */
    public void addSubTask(SubTask subTask) {
        if (subTask == null) throw new IllegalArgumentException("Parameter subTask can't be null");

        if (this._subTasks.containsKey(subTask.getId())) {
            throw new IllegalStateException("Подзадача с идентификатором " + subTask.getId() + "уже добавлена в эпик");
        }

        if (subTask.getEpic() != null && subTask.getEpic().getId() != this._id) {
            throw new IllegalStateException("Подзадача с идентификатором " + subTask.getId() + "уже связана с другим эпиком");
        }

        subTask.setEpic(this);
        this._subTasks.put(subTask.getId(), subTask);
    }

    /**
     * Получить все подзадачи эпика.
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
        if (subTask == null) throw new IllegalArgumentException("Parameter subTask can't be null");

        subTask.setEpic(this);
        this._subTasks.put(subTask.getId(), subTask);
    }

    /**
     * Удалить подзадачу.
     *
     * @param subTask подзадача.
     */
    public void removeSubTask(SubTask subTask) {
        if (subTask == null) throw new IllegalArgumentException("Parameter subTask can't be null");

        this._subTasks.remove(subTask.getId());
    }

    /**
     * Удалить все подзадачи.
     */
    public void removeAllSubTasks() {
        this._subTasks.clear();
    }

    /**
     * Обновить статус эпика.
     */
    public void updateStatus() {
        if (this._subTasks.isEmpty()) {
            this._status = TaskStatus.NEW;
            return;
        }

        int newCount = 0;
        int doneCount = 0;

        for (SubTask subTask : this._subTasks.values()) {
            switch (subTask.getStatus()) {
                case NEW:
                    newCount++;
                    break;
                case DONE:
                    doneCount++;
                    break;
            }
        }

        if (newCount == this._subTasks.size()) {
            this._status = TaskStatus.NEW;
        } else if (doneCount == this._subTasks.size()) {
            this._status = TaskStatus.DONE;
        } else {
            this._status = TaskStatus.IN_PROGRESS;
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + "id: " + this._id + ", " + "name: " + this._name + ", " + "description: " + this._description + ", " + "status: " + this._status.name() + ", " + "sub_task_count: " + this._subTasks.size() + "}";
    }
}
