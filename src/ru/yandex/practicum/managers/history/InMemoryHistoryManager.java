package ru.yandex.practicum.managers.history;

// region imports

import ru.yandex.practicum.abstractions.HistoryManager;

import java.util.ArrayList;
import java.util.List;

// endregion

public class InMemoryHistoryManager<T> implements HistoryManager<T> {
    /**
     * История.
     */
    public final List<T> history;

    /**
     * Конструктор.
     */
    public InMemoryHistoryManager() {
        this.history = new ArrayList<>();
    }

    /**
     * Добавить элемент в историю.
     * @param item элемент истории.
     */
    public void add(T item) {
        if (item == null) {
            return;
        }

        this.history.add(item);
    }

    /**
     * Получить историю.
     * @return список элементов, составляющих историю.
     */
    public List<T> getHistory() {
        return new ArrayList<>(this.history);
    }
}
