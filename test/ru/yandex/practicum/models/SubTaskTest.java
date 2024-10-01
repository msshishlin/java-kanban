package ru.yandex.practicum.models;

// region imports

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.constants.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

// endregion

public class SubTaskTest {
    @Test
    public void createSubTaskTest() {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.ofHours(8);

        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask1 = new SubTask("Подзадача", "Описание подзадачи", now, duration, epic);

        Assertions.assertEquals("Подзадача", subTask1.getName());
        Assertions.assertEquals("Описание подзадачи", subTask1.getDescription());
        Assertions.assertEquals(TaskStatus.NEW, subTask1.getStatus());
        Assertions.assertTrue(subTask1.getStartTime().isPresent());
        Assertions.assertEquals(now, subTask1.getStartTime().get());
        Assertions.assertTrue(subTask1.getDuration().isPresent());
        Assertions.assertEquals(duration, subTask1.getDuration().get());
        Assertions.assertEquals(epic, subTask1.getEpic());

        SubTask subTask2 = new SubTask(5, "Подзадача", "Описание подзадачи", TaskStatus.IN_PROGRESS, now, duration, epic);

        Assertions.assertEquals(5, subTask2.getId());
        Assertions.assertEquals("Подзадача", subTask2.getName());
        Assertions.assertEquals("Описание подзадачи", subTask2.getDescription());
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, subTask2.getStatus());
        Assertions.assertTrue(subTask2.getStartTime().isPresent());
        Assertions.assertEquals(now, subTask2.getStartTime().get());
        Assertions.assertTrue(subTask2.getDuration().isPresent());
        Assertions.assertEquals(duration, subTask2.getDuration().get());
        Assertions.assertEquals(epic, subTask2.getEpic());
    }

    @Test
    public void createSubTaskWithoutEpicTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new SubTask(5, "Подзадача", "Описание подзадачи", TaskStatus.NEW, LocalDateTime.now(), Duration.ofHours(8), null));
    }

    @Test
    public void toStringTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic);
        epic.addSubTask(subTask);

        Assertions.assertTrue(subTask.getStartTime().isPresent());
        Assertions.assertTrue(subTask.getDuration().isPresent());

        String expected = subTask.getClass().getSimpleName() + "{" + "id: " + subTask.getId() + ", name: " + subTask.getName() + ", description: " + subTask.getDescription() + ", status: " + subTask.getStatus().name() + ", startTime: " + subTask.getStartTime().get() + ", duration: " + subTask.getDuration().get() + ", epic: " + subTask.getEpic() + "}";

        Assertions.assertEquals(expected, subTask.toString());
    }

    @Test
    public void cloneSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic);
        SubTask subTaskClone = subTask.clone();

        Assertions.assertEquals(subTask.getId(), subTaskClone.getId());
        Assertions.assertEquals(subTask.getName(), subTaskClone.getName());
        Assertions.assertEquals(subTask.getDescription(), subTaskClone.getDescription());
        Assertions.assertEquals(subTask.getStatus(), subTaskClone.getStatus());
        Assertions.assertEquals(subTask.getEpic(), subTaskClone.getEpic());
    }
}