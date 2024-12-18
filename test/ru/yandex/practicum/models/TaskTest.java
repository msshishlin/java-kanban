package ru.yandex.practicum.models;

// region imports

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.constants.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

// endregion

public class TaskTest {
    @Test
    public void createTaskTest() {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.ofHours(8);

        Task task1 = new Task("Задача", "Описание задачи", now, duration);

        Assertions.assertEquals("Задача", task1.getName());
        Assertions.assertEquals("Описание задачи", task1.getDescription());
        Assertions.assertEquals(TaskStatus.NEW, task1.getStatus());
        Assertions.assertEquals(now, task1.getStartTime());
        Assertions.assertEquals(duration, task1.getDuration());

        Task task2 = new Task(5, "Задача", "Описание задачи", TaskStatus.IN_PROGRESS, now, duration);

        Assertions.assertEquals(5, task2.getId());
        Assertions.assertEquals("Задача", task2.getName());
        Assertions.assertEquals("Описание задачи", task2.getDescription());
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, task2.getStatus());
        Assertions.assertEquals(now, task2.getStartTime());
        Assertions.assertEquals(duration, task2.getDuration());
    }

    @Test
    public void createTaskWithNegativeIdTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task(-1, "Задача", "Описание задачи", TaskStatus.NEW, LocalDateTime.now(), Duration.ofHours(8)));
    }

    @Test
    public void createTaskWithZeroIdTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task(0, "Задача", "Описание задачи", TaskStatus.NEW, LocalDateTime.now(), Duration.ofHours(8)));
    }

    @Test
    public void createTaskWithoutNameTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task(null, "Описание задачи", LocalDateTime.now(), Duration.ofHours(8)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task(5, null, "Описание задачи", TaskStatus.NEW, LocalDateTime.now(), Duration.ofHours(8)));
    }

    @Test
    public void createTaskWithWhitespaceNameTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task(" ", "Описание задачи", LocalDateTime.now(), Duration.ofHours(8)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task(5, " ", "Описание задачи", TaskStatus.NEW, LocalDateTime.now(), Duration.ofHours(8)));
    }

    @Test
    public void createTaskWithoutDescriptionTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task("Задача", null, LocalDateTime.now(), Duration.ofHours(8)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task(5, "Задача", null, TaskStatus.NEW, LocalDateTime.now(), Duration.ofHours(8)));
    }

    @Test
    public void createTaskWithWhitespaceDescriptionTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task("Задача", " ", LocalDateTime.now(), Duration.ofHours(8)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task(5, "Задача", " ", TaskStatus.NEW, LocalDateTime.now(), Duration.ofHours(8)));
    }

    @Test
    public void createTaskWithoutStatusTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task(1, "Задача", "Описание задачи", null, LocalDateTime.now(), Duration.ofHours(8)));
    }

    @Test
    public void createTaskWithoutStartTimeTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task("Задача", "Описание задачи", null, Duration.ofHours(8)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task(5, "Задача", "Описание задачи", TaskStatus.NEW, null, Duration.ofHours(8)));
    }

    @Test
    public void createTaskWithoutDurationTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task("Задача", "Описание задачи", LocalDateTime.now(), null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task(5, "Задача", "Описание задачи", TaskStatus.NEW, LocalDateTime.now(), null));
    }

    @Test
    public void createTwoTasksTest() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDateTime.now(), Duration.ofHours(8));
        Task task2 = new Task("Задача 2", "Описание задачи 2", LocalDateTime.now(), Duration.ofHours(8));

        Assertions.assertNotEquals(task1.getId(), task2.getId());
    }

    @Test
    public void setInProgressStatusTest() {
        Task task = new Task("Задача", "Описание задачи", LocalDateTime.now(), Duration.ofHours(8));
        task.setStatus(TaskStatus.IN_PROGRESS);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
    }

    @Test
    public void setDoneStatusTest() {
        Task task = new Task("Задача", "Описание задачи", LocalDateTime.now(), Duration.ofHours(8));
        task.setStatus(TaskStatus.DONE);

        Assertions.assertEquals(TaskStatus.DONE, task.getStatus());
    }

    @Test
    public void setNullStatusTest() {
        Task task = new Task("Задача", "Описание задачи", LocalDateTime.now(), Duration.ofHours(8));

        Assertions.assertThrows(IllegalArgumentException.class, () -> task.setStatus(null));
    }

    @Test
    public void cloneTaskTest() {
        Task task = new Task("Задача", "Описание задачи", LocalDateTime.now(), Duration.ofHours(8));
        Task taskClone = Task.clone(task);

        Assertions.assertEquals(task.getId(), taskClone.getId());
        Assertions.assertEquals(task.getName(), taskClone.getName());
        Assertions.assertEquals(task.getDescription(), taskClone.getDescription());
        Assertions.assertEquals(task.getStatus(), taskClone.getStatus());
    }

    @Test
    public void isCrossedTest() {
        LocalDateTime startTime = LocalDateTime.now();

        Task task1 = new Task("Задача 1", "Описание задачи 1", startTime, Duration.ofHours(8));
        Assertions.assertThrows(IllegalArgumentException.class, () -> task1.isCrossed(null));

        Task task2 = new Task("Задача 2", "Описание задачи 2", startTime, Duration.ofHours(8));
        Assertions.assertTrue(task1.isCrossed(task2));

        Task task3 = new Task("Задача 3", "Описание задачи 3", startTime.minusHours(4), Duration.ofHours(8));
        Assertions.assertTrue(task1.isCrossed(task3));

        Task task4 = new Task("Задача 4", "Описание задачи 4", startTime.plusHours(4), Duration.ofHours(8));
        Assertions.assertTrue(task1.isCrossed(task4));

        Task task5 = new Task("Задача 5", "Описание задачи 5", startTime.plusHours(16), Duration.ofHours(8));
        Assertions.assertFalse(task1.isCrossed(task5));
    }

    @Test
    public void compareTwoTasksWithSameIdTest() {
        Task task1 = new Task("Задача", "Описание задачи", LocalDateTime.now(), Duration.ofHours(8));
        Task task2 = Task.clone(task1);

        Assertions.assertEquals(task1, task2);
    }

    @Test
    void hashCodeTest() {
        Task task = new Task("Задача", "Описание задачи", LocalDateTime.now(), Duration.ofHours(8));

        Assertions.assertEquals(Objects.hashCode(task.getId()), task.hashCode());
    }

    @Test
    public void toStringTest() {
        Task task = new Task("Задача", "Описание задачи", LocalDateTime.now(), Duration.ofHours(8));

        String expected = task.getClass().getSimpleName() + "{" + "id: " + task.getId() + ", name: " + task.getName() + ", description: " + task.getDescription() + ", status: " + task.getStatus().name() + ", startTime: " + task.getStartTime() + ", duration: " + task.getDuration() + "}";

        Assertions.assertEquals(expected, task.toString());
    }
}