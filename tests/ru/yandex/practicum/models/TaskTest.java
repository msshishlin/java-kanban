package ru.yandex.practicum.models;

// region imports

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.constants.TaskStatus;

import java.util.Objects;

// endregion

public class TaskTest {
    @Test
    public void createTaskTest() {
        Task task = new Task("Задача", "Описание задачи");

        Assertions.assertEquals("Задача", task.getName());
        Assertions.assertEquals("Описание задачи", task.getDescription());
        Assertions.assertEquals(TaskStatus.NEW, task.getStatus());
    }

    @Test
    public void createTaskWithoutNameTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task(null, "Описание задачи"));
    }

    @Test
    public void createTaskWithoutDescriptionTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Task("Задача", null));
    }

    @Test
    public void createTwoTasksTest() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");

        Assertions.assertNotEquals(task1.getId(), task2.getId());
    }

    @Test
    public void setInProgressStatusTest() {
        Task task = new Task("Задача", "Описание задачи");
        task.setStatus(TaskStatus.IN_PROGRESS);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
    }

    @Test
    public void setDoneStatusTest() {
        Task task = new Task("Задача", "Описание задачи");
        task.setStatus(TaskStatus.DONE);

        Assertions.assertEquals(TaskStatus.DONE, task.getStatus());
    }

    @Test
    public void setNullStatusTest() {
        Task task = new Task("Задача", "Описание задачи");

        Assertions.assertThrows(IllegalArgumentException.class, () -> task.setStatus(null));
    }

    @Test void hashCodeTest() {
        Task task = new Task("Задача", "Описание задачи");

        Assertions.assertEquals(Objects.hashCode(task.getId()), task.hashCode());
    }

    @Test
    public void toStringTest() {
        Task task = new Task("Задача", "Описание задачи");

        String expected = task.getClass().getSimpleName() + "{" + "id: " + task.getId() + ", " + "name: "
                + task.getName() + ", " + "description: " + task.getDescription() + ", " + "status: "
                + task.getStatus().name() + "}";

        Assertions.assertEquals(expected, task.toString());
    }

    @Test
    public void cloneTaskTest() {
        Task task = new Task("Задача", "Описание задачи");
        Task taskClone = task.clone();

        Assertions.assertEquals(task.getId(), taskClone.getId());
        Assertions.assertEquals(task.getName(), taskClone.getName());
        Assertions.assertEquals(task.getDescription(), taskClone.getDescription());
        Assertions.assertEquals(task.getStatus(), taskClone.getStatus());
    }

    @Test
    public void compareTwoTasksWithSameIdTest() {
        Task task1 = new Task("Задача", "Описание задачи");
        Task task2 = task1.clone();

        Assertions.assertEquals(task1, task2);
    }
}