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
    protected final int id;

    /**
     * Название задачи.
     */
    protected String name;

    /**
     * Описание задачи.
     */
    protected String description;

    /**
     * Статус задачи.
     */
    protected TaskStatus status;

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

        this.id = ++INSTANCE_COUNT;
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
    }

    /**
     * Конструктор.
     *
     * @param otherTask другая задача.
     */
    public Task(Task otherTask) {
        if (otherTask == null) throw new IllegalArgumentException("Parameter 'otherTask' can't be null");

        this.id = otherTask.id;
        this.name = otherTask.name;
        this.description = otherTask.description;
        this.status = otherTask.status;
    }

    /**
     * Получить идентификатор задачи.
     *
     * @return идентификатор задачи.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Получить название задачи.
     *
     * @return название задачи.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Получить описание задачи.
     *
     * @return описание задачи.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Получить статус задачи.
     *
     * @return статус задачи.
     */
    public TaskStatus getStatus() {
        return this.status;
    }

    /**
     * Установить новый статус задачи.
     *
     * @param newStatus новый статус задачи.
     */
    public void setStatus(TaskStatus newStatus) {
        if (newStatus == null) throw new IllegalArgumentException("Parameter 'newStatus' can't be null");

        this.status = newStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        Task otherTask = (Task) obj;
        return this.id == otherTask.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + "id: " + this.id + ", " + "name: " + this.name + ", " + "description: " + this.description + ", " + "status: " + this.status.name() + "}";
    }
}
