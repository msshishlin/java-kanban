package ru.yandex.practicum.managers.history;

// region imports

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.abstractions.HistoryManager;

import java.util.Arrays;

// endregion

public class InMemoryHistoryManagerTest {
    @Test
    public void addTest() {
        HistoryManager<Integer> historyManager = new InMemoryHistoryManager<>();
        historyManager.add(1);

        Assertions.assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    public void addOverSizeTest() {
        HistoryManager<Integer> historyManager = new InMemoryHistoryManager<>();

        for (int i = 0; i < 5; i++) {
            historyManager.add(i);
        }

        Assertions.assertEquals(5, historyManager.getHistory().size());
        Assertions.assertArrayEquals(Arrays.asList(0, 1, 2, 3, 4).toArray(), historyManager.getHistory().toArray());
    }

    @Test
    public void getHistoryTest() {
        HistoryManager<Integer> historyManager = new InMemoryHistoryManager<>();
        Assertions.assertEquals(0, historyManager.getHistory().size());

        historyManager.add(1);
        historyManager.add(1);
        historyManager.add(1);

        Assertions.assertEquals(3, historyManager.getHistory().size());
        Assertions.assertArrayEquals(Arrays.asList(1, 1, 1).toArray(), historyManager.getHistory().toArray());
    }
}