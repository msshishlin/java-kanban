package ru.yandex.practicum.managers.history;

// region imports

import ru.yandex.practicum.abstractions.HistoryManager;

import java.util.ArrayList;
import java.util.List;

// endregion

public class InMemoryHistoryManager<T> implements HistoryManager<T> {
    /**
     * Максимальное количество элементов в истории.
     */
    private final int historyMaxLength;

    /**
     * История.
     */
    public final List<T> history;

    /**
     * Конструктор.
     */
    public InMemoryHistoryManager(int historyMaxLength) {
        this.historyMaxLength = historyMaxLength;
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

        if (this.history.size() == historyMaxLength) {
            this.history.removeFirst();
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
