package ru.yandex.practicum.models;

//region imports

import ru.yandex.practicum.constants.TaskStatus;
import ru.yandex.practicum.constants.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

//endregion

/**
 * Эпик.
 */
public final class Epic extends AbstractTask {
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
     * Конструктор.
     *
     * @param id          идентификатор задачи.
     * @param name        название задачи.
     * @param description описание задачи.
     * @param subTasks    коллекция подзадач.
     */
    public Epic(int id, String name, String description, HashMap<Integer, SubTask> subTasks) {
        super(id, name, description);

        if (subTasks == null) {
            throw new IllegalArgumentException("Parameter 'subTasks' can't be null");
        }

        this.subTasks = subTasks;
    }

    /**
     * Клонировать эпик.
     *
     * @param epic эпик.
     * @return клон эпика.
     */
    public static Epic clone(Epic epic) {
        return new Epic(epic.id, epic.name, epic.description, epic.subTasks);
    }

    /**
     * Добавить подзадачу.
     *
     * @param subTask подзадача.
     */
    public void addSubTask(SubTask subTask) {
        if (subTask == null) {
            throw new IllegalArgumentException("Parameter subTask can't be null");
        }

        if (this.subTasks.containsKey(subTask.getId())) {
            throw new IllegalStateException("Подзадача с идентификатором " + subTask.getId() + "уже добавлена в эпик");
        }

        if (subTask.getEpicId() != this.id) {
            throw new IllegalStateException("Подзадача с идентификатором " + subTask.getId() + "уже связана с другим эпиком");
        }

        this.subTasks.put(subTask.getId(), subTask);
    }

    /**
     * Получить подзадачу по её идентификатору.
     *
     * @param subTaskId идентификатор подзадачи.
     * @return подзадача.
     */
    public Optional<SubTask> getSubTaskById(int subTaskId) {
        return Optional.ofNullable(this.subTasks.get(subTaskId));
    }

    /**
     * Получить все подзадачи эпика.
     *
     * @return коллекция подзадач.
     */
    public HashMap<Integer, SubTask> getSubTasks() {
        return new HashMap<>(this.subTasks);
    }

    /**
     * Получить все подзадачи эпика.
     *
     * @return коллекция подзадач.
     */
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(this.subTasks.values());
    }

    /**
     * Обновить подзадачу.
     *
     * @param subTask подзадача.
     */
    public void updateSubTask(SubTask subTask) {
        if (subTask == null) {
            throw new IllegalArgumentException("Parameter subTask can't be null");
        }

        if (!this.subTasks.containsKey(subTask.getId())) {
            throw new IllegalStateException("Подзадача с идентификатором " + subTask.getId() + " не найдена");
        }

        this.subTasks.put(subTask.getId(), subTask);
    }

    /**
     * Удалить подзадачу.
     *
     * @param subTask подзадача.
     */
    public void removeSubTask(SubTask subTask) {
        if (subTask == null) {
            throw new IllegalArgumentException("Parameter subTask can't be null");
        }

        if (!this.subTasks.containsKey(subTask.getId())) {
            throw new IllegalStateException("Подзадача с идентификатором " + subTask.getId() + " не найдена");
        }

        this.subTasks.remove(subTask.getId());
    }

    /**
     * Удалить все подзадачи.
     */
    public void removeAllSubTasks() {
        this.subTasks.clear();
    }

    // region Overrides of ru.yandex.practicum.models.AbstractTask

    /**
     * Получить статус задачи.
     *
     * @return статус задачи.
     */
    @Override
    public TaskStatus getStatus() {
        if (this.subTasks.isEmpty()) {
            return TaskStatus.NEW;
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
            return TaskStatus.NEW;
        } else if (doneCount == this.subTasks.size()) {
            return TaskStatus.DONE;
        } else {
            return TaskStatus.IN_PROGRESS;
        }
    }

    /**
     * Получить дату и время, когда предполагается приступить к выполнению задачи.
     *
     * @return дата и время, когда предполагается приступить к выполнению задачи.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Optional<LocalDateTime> getStartTime() {
        if (this.subTasks.isEmpty()) {
            return Optional.empty();
        }

        return this.subTasks.values().stream().map(st -> st.startTime).min(LocalDateTime::compareTo);
    }

    /**
     * Получить продолжительность задачи - оценка того, сколько времени она займёт в минутах.
     *
     * @return продолжительность задачи - оценка того, сколько времени она займёт в минутах.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Optional<Duration> getDuration() {
        if (this.subTasks.isEmpty()) {
            return Optional.empty();
        }

        return this.subTasks.values().stream().map(st -> st.duration).reduce(Duration::plus);
    }

    /**
     * Получить дату и время завершения задачи
     *
     * @return дата и время завершения задачи.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Optional<LocalDateTime> getEndTime() {
        if (this.subTasks.isEmpty()) {
            return Optional.empty();
        }

        return this.subTasks.values().stream().map(Task::getEndTime).max(LocalDateTime::compareTo);
    }

    /**
     * Преобразовать объект эпика в строку в формате CSV.
     *
     * @return объект эпика в виде строки в формате CSV.
     */
    @Override
    public String toCsvString() {
        String startTimeString;
        if (this.getStartTime().isEmpty()) {
            startTimeString = null;
        } else {
            startTimeString = this.getStartTime().get().toString();
        }

        String durationString;
        if (this.getDuration().isEmpty()) {
            durationString = null;
        } else {
            durationString = this.getDuration().get().toString();
        }

        return String.join(",", String.valueOf(this.id), TaskType.EPIC.name(), this.name, this.getStatus().name(), this.description, startTimeString, durationString);
    }

    // endregion

    // region Overrides of java.lang.Object

    @Override
    public String toString() {
        String startTimeString;
        if (this.getStartTime().isEmpty()) {
            startTimeString = null;
        } else {
            startTimeString = this.getStartTime().get().toString();
        }

        String durationString;
        if (this.getDuration().isEmpty()) {
            durationString = null;
        } else {
            durationString = this.getDuration().get().toString();
        }

        return this.getClass().getSimpleName() + "{" + "id: " + this.id + ", name: " + this.name + ", description: " + this.description + ", status: " + this.getStatus().name() + ", startTime: " + startTimeString + ", duration: " + durationString + ", sub_task_count: " + this.subTasks.size() + "}";
    }

    // endregion
}
