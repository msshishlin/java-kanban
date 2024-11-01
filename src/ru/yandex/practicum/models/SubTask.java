package ru.yandex.practicum.models;

// region imports

import ru.yandex.practicum.constants.TaskStatus;
import ru.yandex.practicum.constants.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

// endregion

/**
 * Подзадача.
 */
public final class SubTask extends Task {
    /**
     * Идентификатор эпика.
     */
    private final int epicId;

    /**
     * Конструктор.
     *
     * @param name        название задачи.
     * @param description описание задачи.
     * @param startTime   дата и время, когда предполагается приступить к выполнению задачи.
     * @param duration    продолжительность задачи - оценка того, сколько времени она займёт в минутах.
     * @param epicId      идентификатор эпик.
     */
    public SubTask(String name, String description, LocalDateTime startTime, Duration duration, int epicId) {
        super(name, description, startTime, duration);

        if (epicId <= 0) {
            throw new IllegalArgumentException("Parameter 'epicId' can't zero or negative");
        }

        this.epicId = epicId;
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
     * @param epicId      идентификатор эпик.
     */
    public SubTask(int id, String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration, int epicId) {
        super(id, name, description, status, startTime, duration);

        if (epicId <= 0) {
            throw new IllegalArgumentException("Parameter 'epicId' can't zero or negative");
        }

        this.epicId = epicId;
    }

    /**
     * Клонировать подзадачу.
     *
     * @param subTask подзадача.
     * @return клон подзадачи.
     */
    public static SubTask clone(SubTask subTask) {
        return new SubTask(subTask.id, subTask.name, subTask.description, subTask.status, subTask.startTime, subTask.duration, subTask.epicId);
    }

    /**
     * Получить идентификатор эпик.
     *
     * @return идентификатор эпика.
     */
    public int getEpicId() {
        return this.epicId;
    }

    // region Overrides of ru.yandex.practicum.models.Task

    /**
     * Преобразовать объект подзадачи в строку в формате CSV.
     *
     * @return объект подзадачи в виде строки в формате CSV.
     */
    @Override
    public String toCsvString() {
        return String.join(",", String.valueOf(this.id), TaskType.SUBTASK.name(), this.name, this.status.name(), this.description, this.startTime.toString(), this.duration.toString(), String.valueOf(this.epicId));
    }

    // endregion

    // region Overrides of java.lang.Object

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + "id: " + this.id + ", name: " + this.name + ", description: " + this.description + ", status: " + this.status.name() + ", startTime: " + this.startTime + ", duration: " + this.duration + ", epicId: " + this.epicId + "}";
    }

    // endregion
}
