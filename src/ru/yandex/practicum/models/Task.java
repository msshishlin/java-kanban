package ru.yandex.practicum.models;

//region imports

import ru.yandex.practicum.constants.TaskStatus;
import ru.yandex.practicum.constants.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

//endregion

/**
 * Задача.
 */
public class Task implements Cloneable {
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
    protected final String name;

    /**
     * Описание задачи.
     */
    protected final String description;
    /**
     * Дата и время, когда предполагается приступить к выполнению задачи
     */
    protected final LocalDateTime startTime;
    /**
     * Продолжительность задачи - оценка того, сколько времени она займёт в минутах
     */
    protected final Duration duration;
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
        this(name, description, null, null);
    }

    /**
     * Конструктор.
     *
     * @param name        название задачи.
     * @param description описание задачи.
     * @param startTime   дата и время, когда предполагается приступить к выполнению задачи.
     * @param duration    продолжительность задачи - оценка того, сколько времени она займёт в минутах.
     */
    public Task(String name, String description, LocalDateTime startTime, Duration duration) {
        this(++INSTANCE_COUNT, name, description, TaskStatus.NEW, startTime, duration);
    }

    /**
     * Конструктор.
     *
     * @param id          идентификатор задачи.
     * @param name        название задачи.
     * @param description описание задачи.
     * @param status      статус задачи.
     * @param startTime   дата и время, когда предполагается приступить к выполнению задачи.
     * @param duration    продолжительность задачи - оценка того, сколько времени она займёт в минутах.
     */
    public Task(int id, String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        if (id <= 0) {
            throw new IllegalArgumentException("Parameter 'id' should be positive number");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Parameter 'name' can't be null or empty or whitespace");
        }

        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Parameter 'description' can't be null or empty or whitespace");
        }

        if (status == null) {
            throw new IllegalArgumentException("Parameter 'status' can't be null");
        }

        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
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

    /**
     * Получить дату и время, когда предполагается приступить к выполнению задачи.
     *
     * @return дата и время, когда предполагается приступить к выполнению задачи.
     */
    public Optional<LocalDateTime> getStartTime() {
        return Optional.ofNullable(this.startTime);
    }

    /**
     * Получить продолжительность задачи - оценка того, сколько времени она займёт в минутах.
     *
     * @return продолжительность задачи - оценка того, сколько времени она займёт в минутах.
     */
    public Optional<Duration> getDuration() {
        return Optional.ofNullable(this.duration);
    }

    /**
     * Преобразовать объект задачи в строку в формате CSV.
     *
     * @return объект задачи в виде строки в формате CSV.
     */
    public String toCsvString() {
        return String.join(",", String.valueOf(this.id), TaskType.TASK.name(), this.name, this.status.name(), this.description, this.startTime.toString(), this.duration.toString());
    }

    // region Overrides of java.lang.Object

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
        return this.getClass().getSimpleName() + "{" + "id: " + this.id + ", name: " + this.name + ", description: " + this.description + ", status: " + this.status.name() + ", startTime: " + this.startTime + ", duration: " + this.duration + "}";
    }

    // region Implements of Cloneable

    @Override
    public Task clone() {
        return new Task(this.id, this.name, this.description, this.status, this.startTime, this.duration);
    }

    // endregion

    // endregion
}
