package ru.yandex.practicum.constants;

/**
 * Статус задачи.
 */
public enum TaskStatus {
    /**
     * Задача только создана, но к её выполнению ещё не приступили.
     */
    NEW,

    /**
     * Над задачей ведётся работа.
     */
    IN_PROGRESS,

    /**
     * Задача выполнена.
     */
    DONE
}
