package ru.yandex.practicum.api.dto;

// region imports

import ru.yandex.practicum.constants.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

// endregion

public class TaskDto {
    /**
     * Идентификатор задачи.
     */
    public Integer id;

    /**
     * Название задачи.
     */
    public String name;

    /**
     * Описание задачи.
     */
    public String description;

    /**
     * Дата и время, когда предполагается приступить к выполнению задачи
     */
    public LocalDateTime startTime;

    /**
     * Продолжительность задачи - оценка того, сколько времени она займёт в минутах
     */
    public Duration duration;

    /**
     * Статус задачи.
     */
    public TaskStatus status;
}
