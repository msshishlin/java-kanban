package ru.yandex.practicum.abstractions;

// region imports

import java.util.List;

// endregion

public interface HistoryManager<TKey, TValue> {
    /**
     * Добавить элемент в историю.
     * @param key ключ элемента истории.
     * @param value значение элемента истории.
     */
    void add(TKey key, TValue value);

    /**
     * Удалить элемент из истории.
     * @param key ключ элемента истории.
     */
    void remove(TKey key);

    /**
     * Получить историю.
     * @return список элементов, отражающий историю.
     */
    List<TValue> getHistory();
}
