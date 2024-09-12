package ru.yandex.practicum.abstractions;

// region imports

import java.util.List;

// endregion

public interface HistoryManager<K, V> {
    /**
     * Добавить элемент в историю.
     * @param key ключ элемента истории.
     * @param value значение элемента истории.
     */
    void add(K key, V value);

    /**
     * Удалить элемент из истории.
     * @param key ключ элемента истории.
     */
    void remove(K key);

    /**
     * Получить историю.
     * @return список элементов, отражающий историю.
     */
    List<V> getHistory();
}
