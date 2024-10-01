package ru.yandex.practicum.models;

//region imports

import ru.yandex.practicum.constants.TaskStatus;
import ru.yandex.practicum.constants.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

//endregion

/**
 * Задача.
 */
public class Task extends AbstractTask {
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
     * @param startTime   дата и время, когда предполагается приступить к выполнению задачи.
     * @param duration    продолжительность задачи - оценка того, сколько времени она займёт в минутах.
     */
    public Task(String name, String description, LocalDateTime startTime, Duration duration) {
        super(name, description);

        if (startTime == null) {
            throw new IllegalArgumentException("Parameter 'startTime' can't be null");
        }

        if (duration == null) {
            throw new IllegalArgumentException("Parameter 'duration' can't be null");
        }

        this.status = TaskStatus.NEW;
        this.startTime = startTime;
        this.duration = duration;
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
        super(id, name, description);

        if (status == null) {
            throw new IllegalArgumentException("Parameter 'status' can't be null");
        }

        if (startTime == null) {
            throw new IllegalArgumentException("Parameter 'startTime' can't be null");
        }

        if (duration == null) {
            throw new IllegalArgumentException("Parameter 'duration' can't be null");
        }

        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    /**
     * Клонировать задачу.
     *
     * @param task задача.
     * @return клон задачи.
     */
    public static Task clone(Task task) {
        return new Task(task.id, task.name, task.description, task.status, task.startTime, task.duration);
    }

    // region Overrides of ru.yandex.practicum.models.AbstractTask

    /**
     * Получить статус задачи.
     *
     * @return статус задачи.
     */
    @Override
    public TaskStatus getStatus() {
        return this.status;
    }

    /**
     * Установить новый статус задачи.
     *
     * @param newStatus новый статус задачи.
     */
    public void setStatus(TaskStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Parameter 'newStatus' can't be null");
        }

        this.status = newStatus;
    }

    /**
     * Получить дату и время, когда предполагается приступить к выполнению задачи.
     *
     * @return дата и время, когда предполагается приступить к выполнению задачи.
     */
    @Override
    @SuppressWarnings("unchecked")
    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    /**
     * Получить продолжительность задачи - оценка того, сколько времени она займёт в минутах.
     *
     * @return продолжительность задачи - оценка того, сколько времени она займёт в минутах.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Duration getDuration() {
        return this.duration;
    }

    /**
     * Получить дату и время завершения задачи
     *
     * @return дата и время завершения задачи.
     */
    @Override
    @SuppressWarnings("unchecked")
    public LocalDateTime getEndTime() {
        return this.startTime.plus(this.duration);
    }

    /**
     * Преобразовать объект задачи в строку в формате CSV.
     *
     * @return объект задачи в виде строки в формате CSV.
     */
    @Override
    public String toCsvString() {
        return String.join(",", String.valueOf(this.id), TaskType.TASK.name(), this.name, this.status.name(), this.description, this.startTime.toString(), this.duration.toString());
    }

    // endregion

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

    // endregion
}
