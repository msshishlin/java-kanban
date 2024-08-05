package ru.yandex.practicum.models;

/**
 * Подзадача.
 */
public final class SubTask extends Task {
    /**
     * Эпик.
     */
    private Epic epic;

    /**
     * Конструктор.
     *
     * @param name        название задачи.
     * @param description описание задачи.
     * @param epic        эпик.
     */
    public SubTask(String name, String description, Epic epic) {
        super(name, description);

        if (epic == null) throw new IllegalArgumentException("Parameter 'epic' can't be null");
        this.epic = epic;
        this.epic.addSubTask(this);
    }

    /**
     * Конструктор.
     *
     * @param otherSubTask другая подзадача.
     */
    public SubTask(SubTask otherSubTask) {
        super(otherSubTask);

        this.epic = otherSubTask.epic;
    }

    /**
     * Получить эпик.
     *
     * @return эпик.
     */
    public Epic getEpic() {
        return this.epic;
    }

    /**
     * Установить новый эпик.
     *
     * @param newEpic новый эпик.
     */
    public void setEpic(Epic newEpic) {
        this.epic = newEpic;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + "id: " + this.id + ", " + "name: " + this.name + ", " + "description: " + this.description + ", " + "status: " + this.status.name() + ", " + "epic: " + this.epic + "}";
    }
}
