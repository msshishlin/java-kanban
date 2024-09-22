package ru.yandex.practicum.models;

// region imports

import ru.yandex.practicum.constants.TaskStatus;
import ru.yandex.practicum.constants.TaskType;

// endregion

/**
 * Подзадача.
 */
public final class SubTask extends Task {
    /**
     * Эпик.
     */
    private final Epic epic;

    /**
     * Конструктор.
     *
     * @param name        название задачи.
     * @param description описание задачи.
     * @param epic        эпик.
     */
    public SubTask(String name, String description, Epic epic) {
        super(name, description);

        if (epic == null) {
            throw new IllegalArgumentException("Parameter 'epic' can't be null");
        }

        this.epic = epic;
    }

    /**
     * Конструктор.
     *
     * @param id          идентификатор задачи.
     * @param name        название задачи.
     * @param description описание задачи.
     * @param status      статус задачи.
     * @param epic        эпик.
     */
    public SubTask(int id, String name, String description, TaskStatus status, Epic epic) {
        super(id, name, description, status);

        this.epic = epic;
    }

    /**
     * Получить эпик.
     *
     * @return эпик.
     */
    public Epic getEpic() {
        return this.epic;
    }

    // region Overrides of ru.yandex.practicum.models.Task

    /**
     * Преобразовать объект подзадачи в строку в формате CSV.
     *
     * @return объект подзадачи в виде строки в формате CSV.
     */
    @Override
    public String toCsvString() {
        return String.join(",", String.valueOf(this.id), TaskType.SUBTASK.name(), this.name, this.status.name(), this.description, String.valueOf(this.epic.getId()));
    }

    // endregion

    // region Overrides of java.lang.Object

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + "id: " + this.id + ", " + "name: " + this.name + ", " + "description: " + this.description + ", " + "status: " + this.status.name() + ", " + "epic: " + this.epic + "}";
    }

    // region Implements of Cloneable

    @Override
    public SubTask clone() {
        return new SubTask(this.id, this.name, this.description, this.status, this.epic);
    }

    // endregion

    // endregion
}
