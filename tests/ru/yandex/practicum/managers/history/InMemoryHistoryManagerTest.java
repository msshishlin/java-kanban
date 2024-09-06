package ru.yandex.practicum.managers.history;

// region imports

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.abstractions.HistoryManager;

import java.util.Arrays;
import java.util.List;

// endregion

public class InMemoryHistoryManagerTest {
    @Test
    public void addTest() {
        HistoryManager<Integer, Integer> historyManager = new InMemoryHistoryManager<>();
        historyManager.add(1, 1);

        Assertions.assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    public void addDuplicateTest() {
        HistoryManager<Integer, Integer> historyManager = new InMemoryHistoryManager<>();
        Assertions.assertEquals(0, historyManager.getHistory().size());

        historyManager.add(1, 1);
        historyManager.add(1, 1);

        Assertions.assertEquals(1, historyManager.getHistory().size());
        Assertions.assertArrayEquals(List.of(1).toArray(), historyManager.getHistory().toArray());
    }

    @Test
    public void removeTest() {
        HistoryManager<Integer, Integer> historyManager = new InMemoryHistoryManager<>();
        Assertions.assertEquals(0, historyManager.getHistory().size());

        historyManager.add(1, 1);
        historyManager.add(2, 2);
        historyManager.add(3, 3);

        historyManager.remove(2);

        Assertions.assertEquals(2, historyManager.getHistory().size());
        Assertions.assertArrayEquals(Arrays.asList(1, 3).toArray(), historyManager.getHistory().toArray());
    }

    @Test
    public void removeUnknownTest() {
        HistoryManager<Integer, Integer> historyManager = new InMemoryHistoryManager<>();
        Assertions.assertEquals(0, historyManager.getHistory().size());

        historyManager.add(1, 1);
        historyManager.add(2, 2);
        historyManager.add(3, 3);

        historyManager.remove(5);

        Assertions.assertEquals(3, historyManager.getHistory().size());
        Assertions.assertArrayEquals(Arrays.asList(1, 2, 3).toArray(), historyManager.getHistory().toArray());
    }

    @Test
    public void getHistoryTest() {
        HistoryManager<Integer, Integer> historyManager = new InMemoryHistoryManager<>();
        Assertions.assertEquals(0, historyManager.getHistory().size());

        historyManager.add(1, 1);
        historyManager.add(2, 2);
        historyManager.add(3, 3);

        Assertions.assertEquals(3, historyManager.getHistory().size());
        Assertions.assertArrayEquals(Arrays.asList(1, 2, 3).toArray(), historyManager.getHistory().toArray());
    }
}