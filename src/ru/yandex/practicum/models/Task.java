package ru.yandex.practicum.models;

//region imports

import ru.yandex.practicum.constants.TaskStatus;

import java.util.Objects;

//endregion

/**
 * Задача.
 */
public class Task {
    /**
     * Количество созданных экземпляров типа Task.
     */
    private static int INSTANCE_COUNT = 0;

    /**
     * Идентификатор задачи.
     */
    protected final int _id;

    /**
     * Название задачи.
     */
    protected String _name;

    /**
     * Описание задачи.
     */
    protected String _description;

    /**
     * Статус задачи.
     */
    protected TaskStatus _status;

    /**
     * Конструктор.
     *
     * @param name        название задачи.
     * @param description описание задачи.
     */
    public Task(String name, String description) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Parameter 'name' can't be null or empty or whitespace");
        if (description == null || description.trim().isEmpty())
            throw new IllegalArgumentException("Parameter 'description' can't be null or empty or whitespace");

        this._id = ++INSTANCE_COUNT;
        this._name = name;
        this._description = description;
        this._status = TaskStatus.NEW;
    }

    /**
     * Конструктор.
     *
     * @param otherTask другая задача.
     */
    public Task(Task otherTask) {
        if (otherTask == null) throw new IllegalArgumentException("Parameter 'otherTask' can't be null");

        this._id = otherTask._id;
        this._name = otherTask._name;
        this._description = otherTask._description;
        this._status = otherTask._status;
    }

    /**
     * Получить идентификатор задачи.
     *
     * @return идентификатор задачи.
     */
    public int getId() {
        return this._id;
    }

    /**
     * Получить название задачи.
     *
     * @return название задачи.
     */
    public String getName() {
        return this._name;
    }

    /**
     * Получить описание задачи.
     *
     * @return описание задачи.
     */
    public String getDescription() {
        return this._description;
    }

    /**
     * Получить статус задачи.
     *
     * @return статус задачи.
     */
    public TaskStatus getStatus() {
        return this._status;
    }

    /**
     * Установить новый статус задачи.
     *
     * @param newStatus новый статус задачи.
     */
    public void setStatus(TaskStatus newStatus) {
        if (newStatus == null) throw new IllegalArgumentException("Parameter 'newStatus' can't be null");

        this._status = newStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        Task otherTask = (Task) obj;
        return this._id == otherTask._id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this._id);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + "id: " + this._id + ", " + "name: " + this._name + ", " + "description: " + this._description + ", " + "status: " + this._status.name() + "}";
    }
}
