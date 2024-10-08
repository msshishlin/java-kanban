package ru.yandex.practicum.models;

// region imports

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.constants.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

// endregion

public class EpicTest {
    @Test
    public void createEpicTest() {
        Epic epic1 = new Epic("Эпик", "Описание эпика");

        Assertions.assertEquals("Эпик", epic1.getName());
        Assertions.assertEquals("Описание эпика", epic1.getDescription());
        Assertions.assertEquals(TaskStatus.NEW, epic1.getStatus());
        Assertions.assertTrue(epic1.getStartTime().isEmpty());
        Assertions.assertTrue(epic1.getDuration().isEmpty());
        Assertions.assertEquals(0, epic1.getAllSubTasks().size());

        Epic epic2 = new Epic(5, "Эпик", "Описание эпика", new HashMap<>());

        Assertions.assertEquals("Эпик", epic2.getName());
        Assertions.assertEquals("Описание эпика", epic2.getDescription());
        Assertions.assertEquals(TaskStatus.NEW, epic2.getStatus());
        Assertions.assertTrue(epic2.getStartTime().isEmpty());
        Assertions.assertTrue(epic2.getDuration().isEmpty());
        Assertions.assertEquals(0, epic2.getAllSubTasks().size());
    }

    @Test
    public void createEpicWithoutSubTasksTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Epic(5, "Эпик", "Описание эпика", null));
    }

    @Test
    public void addNullInsteadOfSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        Assertions.assertThrows(IllegalArgumentException.class, () -> epic.addSubTask(null));
    }

    @Test
    public void addSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic);
        epic.addSubTask(subTask);

        Assertions.assertEquals(subTask, epic.getAllSubTasks().getFirst());
    }

    @Test
    public void addSubTaskTwiceTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic);
        epic.addSubTask(subTask);

        Assertions.assertThrows(IllegalStateException.class, () -> epic.addSubTask(subTask));
    }

    @Test
    public void addSubTaskWithAnotherEpicTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        Epic anotherEpic = new Epic("Другой эпик", "Описание другого эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), anotherEpic);

        Assertions.assertThrows(IllegalStateException.class, () -> epic.addSubTask(subTask));
    }

    @Test
    public void getSubTaskByIdTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic);
        epic.addSubTask(subTask);

        Assertions.assertEquals(subTask, epic.getSubTaskById(subTask.getId()));
    }

    @Test
    public void getUnknownSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        Assertions.assertNull(epic.getSubTaskById(5));
    }

    @Test
    public void getAllSubTasksTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", LocalDateTime.now(), Duration.ofHours(8), epic);
        epic.addSubTask(subTask1);

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", LocalDateTime.now(), Duration.ofHours(8), epic);
        epic.addSubTask(subTask2);

        SubTask subTask3 = new SubTask("Подзадача 3", "Описание подзадачи 3", LocalDateTime.now(), Duration.ofHours(8), epic);
        epic.addSubTask(subTask3);

        Assertions.assertEquals(3, epic.getAllSubTasks().size());
    }

    @Test
    public void updateNullInsteadOfSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        Assertions.assertThrows(IllegalArgumentException.class, () -> epic.updateSubTask(null));
    }

    @Test
    public void updateSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic);
        epic.addSubTask(subTask);

        Assertions.assertEquals(TaskStatus.NEW, epic.getAllSubTasks().getFirst().getStatus());

        SubTask subTaskClone = SubTask.clone(subTask);
        subTaskClone.setStatus(TaskStatus.IN_PROGRESS);

        epic.updateSubTask(subTaskClone);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getAllSubTasks().getFirst().getStatus());
    }

    @Test
    public void updateUnknownSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic);

        Assertions.assertThrows(IllegalStateException.class, () -> epic.updateSubTask(subTask));
    }

    @Test
    public void updateSubTaskWithAnotherEpicTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        Epic anotherEpic = new Epic("Другой эпик", "Описание другого эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), anotherEpic);

        Assertions.assertThrows(IllegalStateException.class, () -> epic.updateSubTask(subTask));
    }

    @Test
    public void removeNullInsteadOfSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        Assertions.assertThrows(IllegalArgumentException.class, () -> epic.removeSubTask(null));
    }

    @Test
    public void removeSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic);
        epic.addSubTask(subTask);
        epic.removeSubTask(subTask);

        Assertions.assertEquals(0, epic.getAllSubTasks().size());
    }

    @Test
    public void removeUnknownSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic);

        Assertions.assertThrows(IllegalStateException.class, () -> epic.removeSubTask(subTask));
    }

    @Test
    public void removeSubTaskWithAnotherEpicTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        Epic anotherEpic = new Epic("Другой эпик", "Описание другого эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), anotherEpic);

        Assertions.assertThrows(IllegalStateException.class, () -> epic.removeSubTask(subTask));
    }

    @Test
    public void removeAllSubTasksTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", LocalDateTime.now(), Duration.ofHours(8), epic);
        epic.addSubTask(subTask1);

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", LocalDateTime.now(), Duration.ofHours(8), epic);
        epic.addSubTask(subTask2);

        SubTask subTask3 = new SubTask("Подзадача 3", "Описание подзадачи 3", LocalDateTime.now(), Duration.ofHours(8), epic);
        epic.addSubTask(subTask3);

        epic.removeAllSubTasks();

        Assertions.assertEquals(0, epic.getAllSubTasks().size());
    }

    @Test
    public void getStatusTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        Assertions.assertEquals(TaskStatus.NEW, epic.getStatus());

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", LocalDateTime.now(), Duration.ofHours(8), epic);
        epic.addSubTask(subTask1);

        Assertions.assertEquals(TaskStatus.NEW, epic.getStatus());

        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());

        subTask1.setStatus(TaskStatus.DONE);
        Assertions.assertEquals(TaskStatus.DONE, epic.getStatus());

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", LocalDateTime.now(), Duration.ofHours(8), epic);
        epic.addSubTask(subTask2);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());

        subTask2.setStatus(TaskStatus.IN_PROGRESS);
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());

        subTask2.setStatus(TaskStatus.DONE);
        Assertions.assertEquals(TaskStatus.DONE, epic.getStatus());

        epic.removeAllSubTasks();
        Assertions.assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    public void getStartTimeTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        Assertions.assertTrue(epic.getStartTime().isEmpty());

        LocalDateTime startTime = LocalDateTime.now();

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", startTime, Duration.ofHours(1), epic);
        epic.addSubTask(subTask1);

        Assertions.assertTrue(epic.getStartTime().isPresent());
        Assertions.assertEquals(startTime, epic.getStartTime().get());

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", startTime.plusHours(1), Duration.ofHours(1), epic);
        epic.addSubTask(subTask2);

        Assertions.assertTrue(epic.getStartTime().isPresent());
        Assertions.assertEquals(startTime, epic.getStartTime().get());
    }

    @Test
    public void getDurationTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        Assertions.assertTrue(epic.getDuration().isEmpty());

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", LocalDateTime.now(), Duration.ofHours(1), epic);
        epic.addSubTask(subTask1);

        Assertions.assertTrue(epic.getDuration().isPresent());
        Assertions.assertEquals(1, epic.getDuration().get().toHours());

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", LocalDateTime.now().plusHours(1), Duration.ofHours(1), epic);
        epic.addSubTask(subTask2);

        Assertions.assertTrue(epic.getDuration().isPresent());
        Assertions.assertEquals(2, epic.getDuration().get().toHours());
    }

    @Test
    public void getEndTimeTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        Assertions.assertTrue(epic.getEndTime().isEmpty());

        LocalDateTime startTime = LocalDateTime.now();

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", startTime, Duration.ofHours(1), epic);
        epic.addSubTask(subTask1);

        Assertions.assertTrue(epic.getEndTime().isPresent());
        Assertions.assertEquals(startTime.plusHours(1), epic.getEndTime().get());

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", startTime.plusHours(1), Duration.ofHours(5), epic);
        epic.addSubTask(subTask2);

        Assertions.assertTrue(epic.getEndTime().isPresent());
        Assertions.assertEquals(startTime.plusHours(6), epic.getEndTime().get());
    }

    @Test
    public void toStringTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        Assertions.assertTrue(epic.getStartTime().isEmpty());
        Assertions.assertTrue(epic.getDuration().isEmpty());

        String expected = epic.getClass().getSimpleName() + "{" + "id: " + epic.getId() + ", name: " + epic.getName() + ", description: " + epic.getDescription() + ", status: " + epic.getStatus().name() + ", startTime: null" + ", duration: null" + ", sub_task_count: " + epic.getAllSubTasks().size() + "}";
        Assertions.assertEquals(expected, epic.toString());

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic);
        epic.addSubTask(subTask);

        Assertions.assertTrue(epic.getStartTime().isPresent());
        Assertions.assertTrue(epic.getDuration().isPresent());

        expected = epic.getClass().getSimpleName() + "{" + "id: " + epic.getId() + ", name: " + epic.getName() + ", description: " + epic.getDescription() + ", status: " + epic.getStatus().name() + ", startTime: " + epic.getStartTime().get() + ", duration: " + epic.getDuration().get() + ", sub_task_count: " + epic.getAllSubTasks().size() + "}";
        Assertions.assertEquals(expected, epic.toString());
    }

    @Test
    public void cloneEpicTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic);
        epic.addSubTask(subTask);

        Epic epicClone = Epic.clone(epic);

        Assertions.assertEquals(epic.getId(), epicClone.getId());
        Assertions.assertEquals(epic.getName(), epicClone.getName());
        Assertions.assertEquals(epic.getDescription(), epicClone.getDescription());
        Assertions.assertEquals(epic.getStatus(), epicClone.getStatus());
        Assertions.assertArrayEquals(epic.getAllSubTasks().toArray(), epicClone.getAllSubTasks().toArray());
    }
}