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
     * Добавить подзадачу.
     *
     * @param subTask подзадача.
     */
    public void addSubTask(SubTask subTask) {
        if (subTask == null) throw new IllegalArgumentException("Parameter subTask can't be null");

        if (this.subTasks.containsKey(subTask.getId())) {
            throw new IllegalStateException("Подзадача с идентификатором " + subTask.getId() + "уже добавлена в эпик");
        }

        if (subTask.getEpic() != null && subTask.getEpic().getId() != this.id) {
            throw new IllegalStateException("Подзадача с идентификатором " + subTask.getId() + "уже связана с другим эпиком");
        }

        subTask.setEpic(this);
        this.subTasks.put(subTask.getId(), subTask);
    }

    /**
     * Получить все подзадачи эпика.
     * @return коллекция подзадач.
     */
    public Collection<SubTask> getAllSubTasks() {
        return this.subTasks.values();
    }

    /**
     * Обновить подзадачу.
     *
     * @param subTask подзадача.
     */
    public void updateSubTask(SubTask subTask) {
        if (subTask == null) throw new IllegalArgumentException("Parameter subTask can't be null");

        subTask.setEpic(this);
        this.subTasks.put(subTask.getId(), subTask);
    }

    /**
     * Удалить подзадачу.
     *
     * @param subTask подзадача.
     */
    public void removeSubTask(SubTask subTask) {
        if (subTask == null) throw new IllegalArgumentException("Parameter subTask can't be null");

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

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + "id: " + this.id + ", " + "name: " + this.name + ", " + "description: " + this.description + ", " + "status: " + this.status.name() + ", " + "sub_task_count: " + this.subTasks.size() + "}";
    }
}
