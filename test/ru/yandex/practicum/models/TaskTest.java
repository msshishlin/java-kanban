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

        Task task1 = new Task("Задача", "Описание задачи");

        Assertions.assertEquals("Задача", task1.getName());
        Assertions.assertEquals("Описание задачи", task1.getDescription());
        Assertions.assertEquals(TaskStatus.NEW, task1.getStatus());
        Assertions.assertTrue(task1.getStartTime().isEmpty());
        Assertions.assertTrue(task1.getDuration().isEmpty());

        Task task2 = new Task("Задача", "Описание задачи", now, duration);

        Assertions.assertEquals("Задача", task2.getName());
        Assertions.assertEquals("Описание задачи", task2.getDescription());
        Assertions.assertEquals(TaskStatus.NEW, task2.getStatus());
        Assertions.assertTrue(task2.getStartTime().isPresent());
        Assertions.assertEquals(now, task2.getStartTime().get());
        Assertions.assertTrue(task2.getDuration().isPresent());
        Assertions.assertEquals(duration, task2.getDuration().get());

        Task task3 = new Task(5, "Задача", "Описание задачи", TaskStatus.IN_PROGRESS, now, duration);

        Assertions.assertEquals(5, task3.getId());
        Assertions.assertEquals("Задача", task3.getName());
        Assertions.assertEquals("Описание задачи", task3.getDescription());
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, task3.getStatus());
        Assertions.assertTrue(task3.getStartTime().isPresent());
        Assertions.assertEquals(now, task3.getStartTime().get());
        Assertions.assertTrue(task3.getDuration().isPresent());
        Assertions.assertEquals(duration, task3.getDuration().get());
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
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task(null, "Описание задачи"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task(null, "Описание задачи", LocalDateTime.now(), Duration.ofHours(8)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task(5, null, "Описание задачи", TaskStatus.NEW, LocalDateTime.now(), Duration.ofHours(8)));
    }

    @Test
    public void createTaskWithoutDescriptionTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task("Задача", null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task("Задача", null, LocalDateTime.now(), Duration.ofHours(8)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task(5, "Задача", null, TaskStatus.NEW, LocalDateTime.now(), Duration.ofHours(8)));
    }

    @Test
    public void createTaskWithoutStatusTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task(1, "Задача", "Описание задачи", null, LocalDateTime.now(), Duration.ofHours(8)));
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
    void hashCodeTest() {
        Task task = new Task("Задача", "Описание задачи", LocalDateTime.now(), Duration.ofHours(8));

        Assertions.assertEquals(Objects.hashCode(task.getId()), task.hashCode());
    }

    @Test
    public void toStringTest() {
        Task task = new Task("Задача", "Описание задачи", LocalDateTime.now(), Duration.ofHours(8));

        String startTime;
        if (task.getStartTime().isEmpty()) {
            startTime = null;
        } else {
            startTime = task.getStartTime().get().toString();
        }

        String duration;
        if (task.getDuration().isEmpty()) {
            duration = null;
        } else {
            duration = task.getDuration().get().toString();
        }

        String expected = task.getClass().getSimpleName() + "{" + "id: " + task.getId() + ", name: " + task.getName() + ", description: " + task.getDescription() + ", status: " + task.getStatus().name() + ", startTime: " + startTime + ", duration: " + duration + "}";

        Assertions.assertEquals(expected, task.toString());
    }

    @Test
    public void cloneTaskTest() {
        Task task = new Task("Задача", "Описание задачи", LocalDateTime.now(), Duration.ofHours(8));
        Task taskClone = task.clone();

        Assertions.assertEquals(task.getId(), taskClone.getId());
        Assertions.assertEquals(task.getName(), taskClone.getName());
        Assertions.assertEquals(task.getDescription(), taskClone.getDescription());
        Assertions.assertEquals(task.getStatus(), taskClone.getStatus());
    }

    @Test
    public void compareTwoTasksWithSameIdTest() {
        Task task1 = new Task("Задача", "Описание задачи", LocalDateTime.now(), Duration.ofHours(8));
        Task task2 = task1.clone();

        Assertions.assertEquals(task1, task2);
    }
}