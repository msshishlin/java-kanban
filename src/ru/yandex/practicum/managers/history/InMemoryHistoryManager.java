package ru.yandex.practicum.managers.history;

// region imports

import ru.yandex.practicum.abstractions.HistoryManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// endregion

public class InMemoryHistoryManager<T> implements HistoryManager<T> {
    /**
     * История.
     */
    public final Set<T> history;

    /**
     * Конструктор.
     */
    public InMemoryHistoryManager() {
        this.history = new HashSet<>();
    }

    /**
     * Добавить элемент в историю.
     * @param item элемент истории.
     */
    public void add(T item) {
        if (item == null) {
            return;
        }

        this.history.remove(item);
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
