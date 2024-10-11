package ru.yandex.practicum.models;

// region imports

import ru.yandex.practicum.constants.TaskStatus;

import java.util.Objects;

// endregion

/**
 * Базовая абстракция задачи.
 */
public abstract class AbstractTask {
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
     * Конструктор.
     *
     * @param name        название задачи.
     * @param description описание задачи.
     */
    protected AbstractTask(String name, String description) {
        this(++INSTANCE_COUNT, name, description);
    }

    /**
     * Конструктор.
     *
     * @param id          идентификатор задачи.
     * @param name        название задачи.
     * @param description описание задачи.
     */
    protected AbstractTask(int id, String name, String description) {
        if (id <= 0) {
            throw new IllegalArgumentException("Parameter 'id' should be positive number");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Parameter 'name' can't be null or empty or whitespace");
        }

        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Parameter 'description' can't be null or empty or whitespace");
        }

        this.id = id;
        this.name = name;
        this.description = description;
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
    public abstract TaskStatus getStatus();

    /**
     * Получить дату и время, когда предполагается приступить к выполнению задачи.
     *
     * @return дата и время, когда предполагается приступить к выполнению задачи.
     */
    public abstract <T> T getStartTime();

    /**
     * Получить продолжительность задачи - оценка того, сколько времени она займёт в минутах.
     *
     * @return продолжительность задачи - оценка того, сколько времени она займёт в минутах.
     */
    public abstract <T> T getDuration();

    /**
     * Получить дату и время завершения задачи
     *
     * @return дата и время завершения задачи.
     */
    public abstract <T> T getEndTime();

    /**
     * Преобразовать объект задачи в строку в формате CSV.
     *
     * @return объект задачи в виде строки в формате CSV.
     */
    public abstract String toCsvString();

    // region Overrides of java.lang.Object

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        AbstractTask otherTask = (AbstractTask) obj;
        return this.id == otherTask.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    // endregion
}
