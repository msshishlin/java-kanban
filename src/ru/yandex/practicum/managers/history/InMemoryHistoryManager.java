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
    private final Map<TKey, Node<TValue>> history;

    /**
     * Первый элемент в истории.
     */
    private Node<TValue> first;

    /**
     * Последний элемент в истории.
     */
    private Node<TValue> last;

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
            throw new IllegalArgumentException("Parameter 'key' can't be null");
        }

        if (value == null) {
            throw new IllegalArgumentException("Parameter 'value' can't be null");
        }

        // Если история пустая, просто создаем новый элемент. Он будет одновременно и первым и последним.
        if (this.history.isEmpty()) {
            Node<TValue> element = new Node<>(null, value, null);

            this.first = element;
            this.last = element;

            this.history.put(key, element);
            return;
        }

        // Если история ещё не содержит данный элемент.
        if (!this.history.containsKey(key)) {
            Node<TValue> element = new Node<>(this.last, value, null);
            this.last.next = element;
            this.last = element;

            this.history.put(key, element);
            return;
        }

        // Иначе получаем этот элемент из истории
        Node<TValue> element = this.history.get(key);

        // Если элемент, который мы пытаемся добавить повторно уже, итак, последний - просто выходим.
        if (element.equals(this.last)) {
            return;
        }

        // Связываем предыдущий элемент со следующим.
        Node<TValue> previous = element.previous;
        if (previous != null) {
            previous.next = element.next;
        }

        // Связываем следующий элемент с предыдущим.
        Node<TValue> next = element.next;
        if (next != null) {
            next.previous = element.previous;

            // Если предыдущего элемента нет, значит следующий - это новый первый элемент.
            if (next.previous == null) {
                this.first = next;
            }
        }

        // В предыдущий элемент текущего кладем последний и обнуляем следующий.
        element.previous = this.last;
        element.next = null;

        // Обновляем ссылку на последний элемент.
        this.last.next = element;
        this.last = element;
    }

    /**
     * Удалить элемент из истории.
     * @param key ключ элемента истории.
     */
    public void remove(TKey key) {
        if (key == null || !this.history.containsKey(key)) {
            return;
        }

        Node<TValue> element = this.history.get(key);

        Node<TValue> previous = element.previous;
        if (previous != null) {
            previous.next = element.next;
        }

        Node<TValue> next = element.next;
        if (next != null) {
            next.previous = element.previous;
        }

        // Если элемент, который мы пытаемся удалить, был первым - сдвигаем ссылку первого элемента.
        if (this.first.equals(element)) {
            this.first = this.first.next;
        }

        // Если элемент, который мы пытаемся удалить, был последним - сдвигаем ссылку последнего элемента.
        if (this.last.equals(element)) {
            this.last = this.last.previous;
        }

        this.history.remove(key);
    }

    /**
     * Получить историю.
     * @return список элементов, составляющих историю.
     */
    public List<TValue> getHistory() {
        List<TValue> result = new ArrayList<>();

        Node<TValue> element = this.first;

        while (element != null) {
            result.add(element.data);
            element = element.next;
        }

        return result;
    }

    // region Nested Types

    /**
     * Узел.
     * @param <E> тип данных
     */
    private static class Node<E> {
        /**
         * Предыдущий элемент с данными.
         */
        public Node<E> previous;

        /**
         * Данные.
         */
        public E data;

        /**
         * Следующий элемент с данными.
         */
        public Node<E> next;

        /**
         * Конструктор.
         * @param previous предыдущий элемент с данными.
         * @param data данные.
         * @param next следующий элемент с данными.
         */
        public Node(Node<E> previous, E data, Node<E> next) {
            this.data = data;
            this.next = next;
            this.previous = previous;
        }
    }

    // endregion
}
