package ru.yandex.practicum.managers.history;

// region imports

import ru.yandex.practicum.abstractions.HistoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// endregion

public class InMemoryHistoryManager<TKey, TValue> implements HistoryManager<TKey, TValue> {
    /**
     * История.
     */
    public final Map<TKey, TValue> history;

    /**
     * Конструктор.
     */
    public InMemoryHistoryManager() {
        this.history = new HashMap<>();
    }

    /**
     * Добавить элемент в историю.
     * @param key ключ элемента истории.
     * @param value значение элемента истории.
     */
    public void add(TKey key, TValue value) {
        if (key == null) {
            return;
        }

        this.history.remove(key);
        this.history.put(key, value);
    }

    /**
     * Удалить элемент из истории.
     * @param key ключ элемента истории.
     */
    public void remove(TKey key) {
        if (key == null) {
            return;
        }

        this.history.remove(key);
    }

    /**
     * Получить историю.
     * @return список элементов, составляющих историю.
     */
    public List<TValue> getHistory() {
        return new ArrayList<>(this.history.values());
    }
}
