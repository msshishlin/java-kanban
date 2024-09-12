package ru.yandex.practicum;

// region imports

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.managers.history.InMemoryHistoryManager;
import ru.yandex.practicum.managers.tasks.InMemoryTaskManager;

// endregion

class ManagersTest {
    @Test
    public void getDefaultHistoryManagerTest() {
        Assertions.assertEquals(InMemoryHistoryManager.class, Managers.getDefaultHistory().getClass());
    }

    @Test
    public void getDefaultTaskManagerTest() {
        Assertions.assertEquals(InMemoryTaskManager.class, Managers.getDefault().getClass());
    }
}