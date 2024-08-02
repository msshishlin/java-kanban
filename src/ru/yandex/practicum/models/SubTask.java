package ru.yandex.practicum.models;

/**
 * Подзадача.
 */
public final class SubTask extends Task {
    /**
     * Эпик.
     */
    private Epic _epic;

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
        this._epic = epic;
        this._epic.addSubTask(this);
    }

    /**
     * Конструктор.
     *
     * @param otherSubTask другая подзадача.
     */
    public SubTask(SubTask otherSubTask) {
        super(otherSubTask);

        this._epic = otherSubTask._epic;
    }

    /**
     * Получить эпик.
     *
     * @return эпик.
     */
    public Epic getEpic() {
        return this._epic;
    }

    /**
     * Установить новый эпик.
     *
     * @param newEpic новый эпик.
     */
    public void setEpic(Epic newEpic) {
        this._epic = newEpic;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + "id: " + this._id + ", " + "name: " + this._name + ", " + "description: " + this._description + ", " + "status: " + this._status.name() + ", " + "epic: " + this._epic + "}";
    }
}
