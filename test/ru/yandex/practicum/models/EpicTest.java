package ru.yandex.practicum.models;

// region imports

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.constants.TaskStatus;

// endregion

public class EpicTest {
    @Test
    public void createEpicTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        Assertions.assertEquals("Эпик", epic.getName());
        Assertions.assertEquals("Описание эпика", epic.getDescription());
        Assertions.assertEquals(TaskStatus.NEW, epic.getStatus());
        Assertions.assertEquals(0, epic.getAllSubTasks().size());
    }

    @Test
    public void addSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);
        epic.addSubTask(subTask);

        Assertions.assertEquals(subTask, epic.getAllSubTasks().getFirst());
    }

    @Test
    public void addNullInsteadSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        Assertions.assertThrows(IllegalArgumentException.class, () -> epic.addSubTask(null));
    }

    @Test
    public void addSubTaskTwiceTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);
        epic.addSubTask(subTask);

        Assertions.assertThrows(IllegalStateException.class, () -> epic.addSubTask(subTask));
    }

    @Test
    public void addSubTaskWithAnotherEpicTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        Epic anotherEpic = new Epic("Другой эпик", "Описание другого эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", anotherEpic);

        Assertions.assertThrows(IllegalStateException.class, () -> epic.addSubTask(subTask));
    }

    @Test
    public void getSubTaskByIdTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);
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

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", epic);
        epic.addSubTask(subTask1);

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", epic);
        epic.addSubTask(subTask2);

        SubTask subTask3 = new SubTask("Подзадача 3", "Описание подзадачи 3", epic);
        epic.addSubTask(subTask3);

        Assertions.assertEquals(3, epic.getAllSubTasks().size());
    }

    @Test
    public void updateSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);
        epic.addSubTask(subTask);

        Assertions.assertEquals(TaskStatus.NEW, epic.getAllSubTasks().getFirst().getStatus());

        SubTask subTaskClone = subTask.clone();
        subTaskClone.setStatus(TaskStatus.IN_PROGRESS);

        epic.updateSubTask(subTaskClone);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getAllSubTasks().getFirst().getStatus());
    }

    @Test
    public void updateNullInsteadSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        Assertions.assertThrows(IllegalArgumentException.class, () -> epic.updateSubTask(null));
    }

    @Test
    public void updateUnknownSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);

        Assertions.assertThrows(IllegalStateException.class, () -> epic.updateSubTask(subTask));
    }

    @Test
    public void updateSubTaskWithAnotherEpicTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        Epic anotherEpic = new Epic("Другой эпик", "Описание другого эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", anotherEpic);

        Assertions.assertThrows(IllegalStateException.class, () -> epic.updateSubTask(subTask));
    }

    @Test
    public void removeSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);
        epic.addSubTask(subTask);

        Assertions.assertEquals(1, epic.getAllSubTasks().size());

        epic.removeSubTask(subTask);

        Assertions.assertEquals(0, epic.getAllSubTasks().size());
    }

    @Test
    public void removeNullInsteadOfSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        Assertions.assertThrows(IllegalArgumentException.class, () -> epic.removeSubTask(null));
    }

    @Test
    public void removeUnknownSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);

        Assertions.assertThrows(IllegalStateException.class, () -> epic.removeSubTask(subTask));
    }

    @Test
    public void removeSubTaskWithAnotherEpicTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        Epic anotherEpic = new Epic("Другой эпик", "Описание другого эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", anotherEpic);

        Assertions.assertThrows(IllegalStateException.class, () -> epic.removeSubTask(subTask));
    }

    @Test
    public void removeAllSubTasksTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", epic);
        epic.addSubTask(subTask1);

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", epic);
        epic.addSubTask(subTask2);

        SubTask subTask3 = new SubTask("Подзадача 3", "Описание подзадачи 3", epic);
        epic.addSubTask(subTask3);

        Assertions.assertEquals(3, epic.getAllSubTasks().size());

        epic.removeAllSubTasks();

        Assertions.assertEquals(0, epic.getAllSubTasks().size());
    }

    @Test
    public void updateStatusTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        Assertions.assertEquals(TaskStatus.NEW, epic.getStatus());

        epic.updateStatus();
        Assertions.assertEquals(TaskStatus.NEW, epic.getStatus());

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", epic);
        epic.addSubTask(subTask1);

        epic.updateStatus();
        Assertions.assertEquals(TaskStatus.NEW, epic.getStatus());

        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        epic.updateStatus();
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());

        subTask1.setStatus(TaskStatus.DONE);
        epic.updateStatus();
        Assertions.assertEquals(TaskStatus.DONE, epic.getStatus());

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", epic);
        epic.addSubTask(subTask2);

        epic.updateStatus();
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());

        subTask2.setStatus(TaskStatus.IN_PROGRESS);
        epic.updateStatus();
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());

        subTask2.setStatus(TaskStatus.DONE);
        epic.updateStatus();
        Assertions.assertEquals(TaskStatus.DONE, epic.getStatus());

        epic.removeAllSubTasks();
        epic.updateStatus();
        Assertions.assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    public void toStringTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);
        epic.addSubTask(subTask);

        String expected = epic.getClass().getSimpleName() + "{" + "id: " + epic.getId() + ", " + "name: "
                + epic.getName() + ", " + "description: " + epic.getDescription() + ", " + "status: "
                + epic.status.name() + ", " + "sub_task_count: " + epic.getAllSubTasks().size() + "}";

        Assertions.assertEquals(expected, epic.toString());
    }

    @Test
    public void cloneEpicTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);
        epic.addSubTask(subTask);

        Epic epicClone = epic.clone();

        Assertions.assertEquals(epic.getId(), epicClone.getId());
        Assertions.assertEquals(epic.getName(), epicClone.getName());
        Assertions.assertEquals(epic.getDescription(), epicClone.getDescription());
        Assertions.assertEquals(epic.getStatus(), epicClone.getStatus());
        Assertions.assertArrayEquals(epic.getAllSubTasks().toArray(), epicClone.getAllSubTasks().toArray());
    }
}