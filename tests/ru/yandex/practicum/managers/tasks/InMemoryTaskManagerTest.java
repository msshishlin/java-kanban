package ru.yandex.practicum.managers.tasks;

// region imports

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.abstractions.TaskManager;
import ru.yandex.practicum.constants.TaskStatus;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.util.Arrays;

// endregion

public class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        this.taskManager = new InMemoryTaskManager();
    }

    @Test
    public void createTaskTest() {
        Task task = new Task("Задача", "Описание задачи");
        this.taskManager.createTask(task);

        Assertions.assertEquals(1, this.taskManager.getAllTasks().size());
    }

    @Test
    public void createNullInsteadOfTaskTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.taskManager.createTask(null));
    }

    @Test
    public void createTaskTwiceTest() {
        Task task = new Task("Задача", "Описание задачи");
        this.taskManager.createTask(task);

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createTask(task));
    }

    @Test
    public void createTaskWithInProgressStatusTest() {
        Task task = new Task("Задача", "Описание задачи");
        task.setStatus(TaskStatus.IN_PROGRESS);

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createTask(task));
    }

    @Test
    public void createTaskWithDoneStatusTest() {
        Task task = new Task("Задача", "Описание задачи");
        task.setStatus(TaskStatus.DONE);

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createTask(task));
    }

    @Test
    public void getTaskByIdTest() {
        Task task = new Task("Задача", "Описание задачи");
        this.taskManager.createTask(task);

        Assertions.assertEquals(task, this.taskManager.getTaskById(task.getId()));
    }

    @Test
    public void getUnknownTaskTest() {
        Assertions.assertNull(this.taskManager.getTaskById(5));
    }

    @Test
    public void getAllTasksTest() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        this.taskManager.createTask(task1);

        Task task2 = new Task("Задача 2", "Описание задачи 2");
        this.taskManager.createTask(task2);

        Task task3 = new Task("Задача 3", "Описание задачи 3");
        this.taskManager.createTask(task3);

        Assertions.assertEquals(3, this.taskManager.getAllTasks().size());
    }

    @Test
    public void updateTaskTest() {
        Task task = new Task("Задача", "Описание задачи");
        this.taskManager.createTask(task);

        Task taskClone = task.clone();
        taskClone.setStatus(TaskStatus.IN_PROGRESS);

        this.taskManager.updateTask(taskClone);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, this.taskManager.getTaskById(task.getId()).getStatus());
    }

    @Test
    public void updateNullInsteadOfTaskTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.taskManager.updateTask(null));
    }

    @Test
    public void updateUnknownTaskTest() {
        Task task = new Task("Задача", "Описание задачи");

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.updateTask(task));
    }

    @Test
    public void removeTaskByIdTest() {
        Task task = new Task("Задача", "Описание задачи");
        this.taskManager.createTask(task);

        Assertions.assertEquals(1, this.taskManager.getAllTasks().size());

        this.taskManager.removeTaskById(task.getId());

        Assertions.assertEquals(0, this.taskManager.getAllTasks().size());
    }

    @Test
    public void removeUnknownTaskTest() {
        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.removeTaskById(5));
    }

    @Test
    public void removeAllTasksTest() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        this.taskManager.createTask(task1);

        Task task2 = new Task("Задача 2", "Описание задачи 2");
        this.taskManager.createTask(task2);

        Task task3 = new Task("Задача 3", "Описание задачи 3");
        this.taskManager.createTask(task3);

        Assertions.assertEquals(3, this.taskManager.getAllTasks().size());

        this.taskManager.removeAllTasks();

        Assertions.assertEquals(0, this.taskManager.getAllTasks().size());
    }

    @Test
    public void createSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);
        epic.addSubTask(subTask);

        this.taskManager.createSubTask(subTask);

        Assertions.assertEquals(1, this.taskManager.getAllSubTasks().size());
    }

    @Test
    public void createNullInsteadOfSubTaskTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.taskManager.createSubTask(null));
    }

    @Test
    public void createSubTaskTwiceTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);
        epic.addSubTask(subTask);

        this.taskManager.createSubTask(subTask);

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createSubTask(subTask));
    }

    @Test
    public void createSubTaskWithInProgressStatusTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);
        subTask.setStatus(TaskStatus.IN_PROGRESS);

        epic.addSubTask(subTask);

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createSubTask(subTask));
    }

    @Test
    public void createSubTaskWithDoneStatusTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);
        subTask.setStatus(TaskStatus.DONE);

        epic.addSubTask(subTask);

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createSubTask(subTask));
    }

    @Test
    public void createSubTaskWithoutCreatingEpicTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);
        epic.addSubTask(subTask);

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createSubTask(subTask));
    }

    @Test
    public void createSubTaskWithoutAddingToEpicTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createSubTask(subTask));
    }

    @Test
    public void getSubTaskByIdTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);
        epic.addSubTask(subTask);

        this.taskManager.createSubTask(subTask);

        Assertions.assertEquals(subTask, this.taskManager.getSubTaskById(subTask.getId()));
    }

    @Test
    public void getUnknownSubTaskTest() {
        Assertions.assertNull(this.taskManager.getSubTaskById(5));
    }

    @Test
    public void getSubtasksByEpicTest() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        this.taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", epic1);
        epic1.addSubTask(subTask1);
        this.taskManager.createSubTask(subTask1);

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", epic1);
        epic1.addSubTask(subTask2);
        this.taskManager.createSubTask(subTask2);

        SubTask subTask3 = new SubTask("Подзадача 3", "Описание подзадачи 3", epic1);
        epic1.addSubTask(subTask3);
        this.taskManager.createSubTask(subTask3);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        this.taskManager.createEpic(epic2);

        SubTask subTask4 = new SubTask("Подзадача 4", "Описание подзадачи 4", epic2);
        epic2.addSubTask(subTask4);
        this.taskManager.createSubTask(subTask4);

        SubTask subTask5 = new SubTask("Подзадача 5", "Описание подзадачи 5", epic2);
        epic2.addSubTask(subTask5);
        this.taskManager.createSubTask(subTask5);

        Assertions.assertArrayEquals(Arrays.asList(subTask1, subTask2, subTask3).toArray(), this.taskManager.getSubTasksByEpic(epic1).toArray());
    }

    @Test
    public void getAllSubTasksTest() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        this.taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", epic1);
        epic1.addSubTask(subTask1);
        this.taskManager.createSubTask(subTask1);

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", epic1);
        epic1.addSubTask(subTask2);
        this.taskManager.createSubTask(subTask2);

        SubTask subTask3 = new SubTask("Подзадача 3", "Описание подзадачи 3", epic1);
        epic1.addSubTask(subTask3);
        this.taskManager.createSubTask(subTask3);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        this.taskManager.createEpic(epic2);

        SubTask subTask4 = new SubTask("Подзадача 4", "Описание подзадачи 4", epic2);
        epic2.addSubTask(subTask4);
        this.taskManager.createSubTask(subTask4);

        SubTask subTask5 = new SubTask("Подзадача 5", "Описание подзадачи 5", epic2);
        epic2.addSubTask(subTask5);
        this.taskManager.createSubTask(subTask5);

        Assertions.assertArrayEquals(Arrays.asList(subTask1, subTask2, subTask3, subTask4, subTask5).toArray(), this.taskManager.getAllSubTasks().toArray());
    }

    @Test
    public void updateSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);
        epic.addSubTask(subTask);

        this.taskManager.createSubTask(subTask);

        SubTask subTaskClone = subTask.clone();
        subTaskClone.setStatus(TaskStatus.IN_PROGRESS);

        this.taskManager.updateSubTask(subTaskClone);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, this.taskManager.getSubTaskById(subTask.getId()).getStatus());
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, this.taskManager.getEpicById(epic.getId()).getSubTaskById(subTask.getId()).getStatus());

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, this.taskManager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    public void updateNullInsteadOfSubTaskTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.taskManager.updateSubTask(null));
    }

    @Test
    public void updateUnknownSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.updateSubTask(subTask));
    }

    @Test
    public void removeSubTaskByIdTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);
        epic.addSubTask(subTask);

        this.taskManager.createSubTask(subTask);
        this.taskManager.removeSubTaskById(subTask.getId());

        Assertions.assertEquals(0, this.taskManager.getAllSubTasks().size());
        Assertions.assertEquals(0, this.taskManager.getEpicById(epic.getId()).getAllSubTasks().size());
    }

    @Test
    public void removeUnknownSubTaskTest() {
        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.removeSubTaskById(5));
    }

    @Test
    public void removeAllSubTasksTest() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        this.taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", epic1);
        epic1.addSubTask(subTask1);
        this.taskManager.createSubTask(subTask1);

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", epic1);
        epic1.addSubTask(subTask2);
        this.taskManager.createSubTask(subTask2);

        SubTask subTask3 = new SubTask("Подзадача 3", "Описание подзадачи 3", epic1);
        epic1.addSubTask(subTask3);
        this.taskManager.createSubTask(subTask3);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        this.taskManager.createEpic(epic2);

        SubTask subTask4 = new SubTask("Подзадача 4", "Описание подзадачи 4", epic2);
        epic2.addSubTask(subTask4);
        this.taskManager.createSubTask(subTask4);

        SubTask subTask5 = new SubTask("Подзадача 5", "Описание подзадачи 5", epic2);
        epic2.addSubTask(subTask5);
        this.taskManager.createSubTask(subTask5);

        this.taskManager.removeAllSubTasks();

        Assertions.assertEquals(0, this.taskManager.getAllSubTasks().size());
        Assertions.assertEquals(0, this.taskManager.getEpicById(epic1.getId()).getAllSubTasks().size());
        Assertions.assertEquals(0, this.taskManager.getEpicById(epic2.getId()).getAllSubTasks().size());
    }

    @Test
    public void createEpicTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        Assertions.assertEquals(1, this.taskManager.getAllEpics().size());
    }

    @Test
    public void createNullInsteadOfEpicTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.taskManager.createEpic(null));
    }

    @Test
    public void createEpicTwiceTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createEpic(epic));
    }

    @Test
    public void createEpicWithInProgressStatusTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        epic.setStatus(TaskStatus.IN_PROGRESS);

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createEpic(epic));
    }

    @Test
    public void createEpicWithDoneStatusTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        epic.setStatus(TaskStatus.DONE);

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createEpic(epic));
    }

    @Test
    public void getEpicByIdTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        Assertions.assertEquals(epic, this.taskManager.getEpicById(epic.getId()));
    }

    @Test
    public void getUnknownEpicTest() {
        Assertions.assertNull(this.taskManager.getEpicById(5));
    }

    @Test
    public void getAllEpicsTest() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        this.taskManager.createEpic(epic1);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        this.taskManager.createEpic(epic2);

        Epic epic3 = new Epic("Эпик 3", "Описание эпика 3");
        this.taskManager.createEpic(epic3);

        Assertions.assertEquals(3, this.taskManager.getAllEpics().size());
    }

    @Test
    public void updateEpicTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        Epic epicClone = epic.clone();
        this.taskManager.updateEpic(epicClone);
    }

    @Test
    public void updateNullInsteadOfEpicTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.taskManager.updateEpic(null));
    }

    @Test
    public void updateUnknownEpicTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.updateEpic(epic));
    }

    @Test
    public void removeEpicByIdTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);
        epic.addSubTask(subTask);

        this.taskManager.createSubTask(subTask);

        this.taskManager.removeEpicById(epic.getId());

        Assertions.assertEquals(0, this.taskManager.getAllSubTasks().size());
        Assertions.assertEquals(0, this.taskManager.getAllEpics().size());
    }

    @Test
    public void removeUnknownEpicTest() {
        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.removeEpicById(5));
    }

    @Test
    public void removeAllEpicsTest() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        this.taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", epic1);
        epic1.addSubTask(subTask1);
        this.taskManager.createSubTask(subTask1);

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", epic1);
        epic1.addSubTask(subTask2);
        this.taskManager.createSubTask(subTask2);

        SubTask subTask3 = new SubTask("Подзадача 3", "Описание подзадачи 3", epic1);
        epic1.addSubTask(subTask3);
        this.taskManager.createSubTask(subTask3);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        this.taskManager.createEpic(epic2);

        SubTask subTask4 = new SubTask("Подзадача 4", "Описание подзадачи 4", epic2);
        epic2.addSubTask(subTask4);
        this.taskManager.createSubTask(subTask4);

        SubTask subTask5 = new SubTask("Подзадача 5", "Описание подзадачи 5", epic2);
        epic2.addSubTask(subTask5);
        this.taskManager.createSubTask(subTask5);

        this.taskManager.removeAllEpics();

        Assertions.assertEquals(0, this.taskManager.getAllSubTasks().size());
        Assertions.assertEquals(0, this.taskManager.getAllEpics().size());
    }
}