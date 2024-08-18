package ru.yandex.practicum.abstractions;

// region imports

import java.util.List;

// endregion

public interface HistoryManager<T> {
    /**
     * Добавить элемент в историю.
     * @param item элемент истории.
     */
    void add(T item);

    /**
     * Получить историю.
     * @return список элементов, отражающий историю.
     */
    List<T> getHistory();
}