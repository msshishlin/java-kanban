package ru.yandex.practicum;

// region imports

import ru.yandex.practicum.abstractions.HistoryManager;
import ru.yandex.practicum.abstractions.TaskManager;
import ru.yandex.practicum.managers.history.InMemoryHistoryManager;
import ru.yandex.practicum.managers.tasks.InMemoryTaskManager;
import ru.yandex.practicum.models.Task;

// endregion

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager<Integer, Task> getDefaultHistory() {
        return new InMemoryHistoryManager<>();
    }
}
