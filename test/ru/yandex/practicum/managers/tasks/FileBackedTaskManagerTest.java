package ru.yandex.practicum.managers.tasks;

// region imports

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.abstractions.TaskManager;
import ru.yandex.practicum.constants.TaskStatus;
import ru.yandex.practicum.constants.TaskType;
import ru.yandex.practicum.exceptions.ManagerLoadException;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

// endregion

public final class FileBackedTaskManagerTest {
    private File storage;
    private TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        this.storage = new File("./storage.csv");
        this.taskManager = new FileBackedTaskManager(this.storage);
    }

    @AfterEach
    public void afterEach() throws IOException {
        Files.deleteIfExists(storage.toPath());
    }

    @Test
    public void createFileBackedTaskManagerWithNullTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new FileBackedTaskManager(null));
    }

    @Test
    public void loadFromUnknownFileTest() {
        Assertions.assertThrows(ManagerLoadException.class, () -> FileBackedTaskManager.loadFromFile(new FileBackedTaskManager(new File("./storage.csv")), new File("./unknown.csv")));
    }

    @Test
    public void loadFromFileTest() throws IOException {
        Task task1 = new Task("Задача №1", "Описание задачи №1", LocalDateTime.now(), Duration.ofHours(1));
        taskManager.createTask(task1);

        Task task2 = new Task("Задача №2", "Описание задачи №2", LocalDateTime.now().plusHours(1), Duration.ofHours(1));
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Эпик №1", "Описание эпика № 1");
        taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Подзадача №1", "Описание подзадачи №1", LocalDateTime.now().plusHours(2), Duration.ofHours(1), epic1.getId());
        epic1.addSubTask(subTask1);
        taskManager.createSubTask(subTask1);

        SubTask subTask2 = new SubTask("Подзадача №2", "Описание подзадачи №2", LocalDateTime.now().plusHours(3), Duration.ofHours(1), epic1.getId());
        epic1.addSubTask(subTask2);
        taskManager.createSubTask(subTask2);

        Epic epic2 = new Epic("Эпик №2", "Описание эпика № 2");
        taskManager.createEpic(epic2);

        SubTask subTask3 = new SubTask("Подзадача №3", "Описание подзадачи №3", LocalDateTime.now().plusHours(4), Duration.ofHours(1), epic2.getId());
        epic2.addSubTask(subTask3);
        taskManager.createSubTask(subTask3);

        File otherStorage = new File("./otherStorage.csv");
        FileBackedTaskManager otherTaskManager = new FileBackedTaskManager(otherStorage);

        FileBackedTaskManager.loadFromFile(otherTaskManager, this.storage);
        Assertions.assertEquals(2, otherTaskManager.getAllEpics().size());
        Assertions.assertEquals(3, otherTaskManager.getAllSubTasks().size());
        Assertions.assertEquals(2, otherTaskManager.getAllTasks().size());

        Assertions.assertIterableEquals(Files.readAllLines(this.storage.toPath()), Files.readAllLines(otherStorage.toPath()));
        Files.deleteIfExists(otherStorage.toPath());
    }

    @Test
    public void createTaskTest() throws IOException {
        Task task = new Task("Задача", "Описание задачи", LocalDateTime.now(), Duration.ofHours(8));
        this.taskManager.createTask(task);

        List<String> lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertEquals(1, this.taskManager.getAllTasks().size());
        Assertions.assertEquals(String.join(",", String.valueOf(task.getId()), TaskType.TASK.toString(), task.getName(), task.getStatus().toString(), task.getDescription(), task.getStartTime().toString(), task.getDuration().toString()), lines.getFirst());
    }

    @Test
    public void createCrossedTaskTest() {
        LocalDateTime startTime = LocalDateTime.now();

        Task task1 = new Task("Задача 1", "Описание задачи 1", startTime, Duration.ofHours(8));
        this.taskManager.createTask(task1);

        Task task2 = new Task("Задача 2", "Описание задачи 2", startTime, Duration.ofHours(8));
        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createTask(task2));

        Task task3 = new Task("Задача 3", "Описание задачи 3", startTime.minusHours(4), Duration.ofHours(8));
        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createTask(task3));

        Task task4 = new Task("Задача 4", "Описание задачи 4", startTime.plusHours(4), Duration.ofHours(8));
        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createTask(task4));
    }

    @Test
    public void createNullInsteadOfTaskTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.taskManager.createTask(null));
    }

    @Test
    public void createTaskTwiceTest() {
        Task task = new Task("Задача", "Описание задачи", LocalDateTime.now(), Duration.ofHours(8));
        this.taskManager.createTask(task);

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createTask(task));
    }

    @Test
    public void createTaskWithInProgressStatusTest() {
        Task task = new Task("Задача", "Описание задачи", LocalDateTime.now(), Duration.ofHours(8));
        task.setStatus(TaskStatus.IN_PROGRESS);

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createTask(task));
    }

    @Test
    public void createTaskWithDoneStatusTest() {
        Task task = new Task("Задача", "Описание задачи", LocalDateTime.now(), Duration.ofHours(8));
        task.setStatus(TaskStatus.DONE);

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createTask(task));
    }

    @Test
    public void getTaskByIdTest() {
        Task task = new Task("Задача", "Описание задачи", LocalDateTime.now(), Duration.ofHours(8));
        this.taskManager.createTask(task);

        Assertions.assertTrue(this.taskManager.getTaskById(task.getId()).isPresent());
        Assertions.assertEquals(task, this.taskManager.getTaskById(task.getId()).get());
    }

    @Test
    public void getUnknownTaskTest() {
        Assertions.assertTrue(this.taskManager.getTaskById(5).isEmpty());
    }

    @Test
    public void getAllTasksTest() throws IOException {
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDateTime.now(), Duration.ofHours(1));
        this.taskManager.createTask(task1);

        Task task2 = new Task("Задача 2", "Описание задачи 2", LocalDateTime.now().plusHours(1), Duration.ofHours(1));
        this.taskManager.createTask(task2);

        Task task3 = new Task("Задача 3", "Описание задачи 3", LocalDateTime.now().plusHours(2), Duration.ofHours(2));
        this.taskManager.createTask(task3);

        Assertions.assertEquals(3, this.taskManager.getAllTasks().size());

        List<String> lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertEquals(String.join(",", String.valueOf(task1.getId()), TaskType.TASK.toString(), task1.getName(), task1.getStatus().toString(), task1.getDescription(), task1.getStartTime().toString(), task1.getDuration().toString()), lines.getFirst());
        Assertions.assertEquals(String.join(",", String.valueOf(task2.getId()), TaskType.TASK.toString(), task2.getName(), task2.getStatus().toString(), task2.getDescription(), task2.getStartTime().toString(), task2.getDuration().toString()), lines.get(1));
        Assertions.assertEquals(String.join(",", String.valueOf(task3.getId()), TaskType.TASK.toString(), task3.getName(), task3.getStatus().toString(), task3.getDescription(), task3.getStartTime().toString(), task3.getDuration().toString()), lines.get(2));
    }

    @Test
    public void updateTaskTest() throws IOException {
        Task task = new Task("Задача", "Описание задачи", LocalDateTime.now(), Duration.ofHours(8));
        this.taskManager.createTask(task);

        Task taskClone = Task.clone(task);
        taskClone.setStatus(TaskStatus.IN_PROGRESS);

        this.taskManager.updateTask(taskClone);

        List<String> lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertTrue(this.taskManager.getTaskById(task.getId()).isPresent());
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, this.taskManager.getTaskById(task.getId()).get().getStatus());
        Assertions.assertEquals(String.join(",", String.valueOf(taskClone.getId()), TaskType.TASK.toString(), taskClone.getName(), taskClone.getStatus().toString(), taskClone.getDescription(), taskClone.getStartTime().toString(), taskClone.getDuration().toString()), lines.getFirst());
    }

    @Test
    public void updateNullInsteadOfTaskTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.taskManager.updateTask(null));
    }

    @Test
    public void updateUnknownTaskTest() {
        Task task = new Task("Задача", "Описание задачи", LocalDateTime.now(), Duration.ofHours(8));

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.updateTask(task));
    }

    @Test
    public void removeTaskByIdTest() throws IOException {
        Task task = new Task("Задача", "Описание задачи", LocalDateTime.now(), Duration.ofHours(8));
        this.taskManager.createTask(task);

        Assertions.assertEquals(1, this.taskManager.getAllTasks().size());

        this.taskManager.removeTaskById(task.getId());

        List<String> lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertEquals(0, this.taskManager.getAllTasks().size());
        Assertions.assertEquals(0, lines.size());
    }

    @Test
    public void removeUnknownTaskTest() {
        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.removeTaskById(5));
    }

    @Test
    public void removeAllTasksTest() throws IOException {
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDateTime.now(), Duration.ofHours(1));
        this.taskManager.createTask(task1);

        Task task2 = new Task("Задача 2", "Описание задачи 2", LocalDateTime.now().plusHours(1), Duration.ofHours(1));
        this.taskManager.createTask(task2);

        Task task3 = new Task("Задача 3", "Описание задачи 3", LocalDateTime.now().plusHours(2), Duration.ofHours(1));
        this.taskManager.createTask(task3);

        Assertions.assertEquals(3, this.taskManager.getAllTasks().size());

        this.taskManager.removeAllTasks();

        List<String> lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertEquals(0, this.taskManager.getAllTasks().size());
        Assertions.assertEquals(0, lines.size());
    }

    @Test
    public void createSubTaskTest() throws IOException {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic.getId());
        epic.addSubTask(subTask);

        this.taskManager.createSubTask(subTask);

        List<String> lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertEquals(1, this.taskManager.getAllSubTasks().size());
        Assertions.assertTrue(epic.getStartTime().isPresent());
        Assertions.assertTrue(epic.getDuration().isPresent());
        Assertions.assertEquals(String.join(",", String.valueOf(epic.getId()), TaskType.EPIC.toString(), epic.getName(), epic.getStatus().toString(), epic.getDescription(), epic.getStartTime().get().toString(), epic.getDuration().get().toString()), lines.getFirst());
        Assertions.assertEquals(String.join(",", String.valueOf(subTask.getId()), TaskType.SUBTASK.toString(), subTask.getName(), subTask.getStatus().toString(), subTask.getDescription(), subTask.getStartTime().toString(), subTask.getDuration().toString(), String.valueOf(epic.getId())), lines.get(1));
    }

    @Test
    public void createNullInsteadOfSubTaskTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.taskManager.createSubTask(null));
    }

    @Test
    public void createSubTaskTwiceTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic.getId());
        epic.addSubTask(subTask);

        this.taskManager.createSubTask(subTask);

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createSubTask(subTask));
    }

    @Test
    public void createSubTaskWithInProgressStatusTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic.getId());
        subTask.setStatus(TaskStatus.IN_PROGRESS);

        epic.addSubTask(subTask);

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createSubTask(subTask));
    }

    @Test
    public void createSubTaskWithDoneStatusTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic.getId());
        subTask.setStatus(TaskStatus.DONE);

        epic.addSubTask(subTask);

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createSubTask(subTask));
    }

    @Test
    public void createSubTaskWithoutCreatingEpicTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic.getId());
        epic.addSubTask(subTask);

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createSubTask(subTask));
    }

    @Test
    public void createSubTaskWithoutAddingToEpicTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic.getId());

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.createSubTask(subTask));
    }

    @Test
    public void getSubTaskByIdTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic.getId());
        epic.addSubTask(subTask);

        this.taskManager.createSubTask(subTask);

        Assertions.assertTrue(this.taskManager.getSubTaskById(subTask.getId()).isPresent());
        Assertions.assertEquals(subTask, this.taskManager.getSubTaskById(subTask.getId()).get());
    }

    @Test
    public void getUnknownSubTaskTest() {
        Assertions.assertTrue(this.taskManager.getSubTaskById(5).isEmpty());
    }

    @Test
    public void getSubtasksByEpicTest() throws IOException {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        this.taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", LocalDateTime.now(), Duration.ofHours(1), epic1.getId());
        epic1.addSubTask(subTask1);
        this.taskManager.createSubTask(subTask1);

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", LocalDateTime.now().plusHours(1), Duration.ofHours(1), epic1.getId());
        epic1.addSubTask(subTask2);
        this.taskManager.createSubTask(subTask2);

        SubTask subTask3 = new SubTask("Подзадача 3", "Описание подзадачи 3", LocalDateTime.now().plusHours(2), Duration.ofHours(1), epic1.getId());
        epic1.addSubTask(subTask3);
        this.taskManager.createSubTask(subTask3);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        this.taskManager.createEpic(epic2);

        SubTask subTask4 = new SubTask("Подзадача 4", "Описание подзадачи 4", LocalDateTime.now().plusHours(3), Duration.ofHours(1), epic2.getId());
        epic2.addSubTask(subTask4);
        this.taskManager.createSubTask(subTask4);

        SubTask subTask5 = new SubTask("Подзадача 5", "Описание подзадачи 5", LocalDateTime.now().plusHours(4), Duration.ofHours(1), epic2.getId());
        epic2.addSubTask(subTask5);
        this.taskManager.createSubTask(subTask5);

        List<String> lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertArrayEquals(Arrays.asList(subTask1, subTask2, subTask3).toArray(), this.taskManager.getSubTasksByEpic(epic1).toArray());
        Assertions.assertTrue(epic1.getStartTime().isPresent());
        Assertions.assertTrue(epic1.getDuration().isPresent());
        Assertions.assertEquals(String.join(",", String.valueOf(epic1.getId()), TaskType.EPIC.toString(), epic1.getName(), epic1.getStatus().toString(), epic1.getDescription(), epic1.getStartTime().get().toString(), epic1.getDuration().get().toString()), lines.getFirst());
        Assertions.assertTrue(epic2.getStartTime().isPresent());
        Assertions.assertTrue(epic2.getDuration().isPresent());
        Assertions.assertEquals(String.join(",", String.valueOf(epic2.getId()), TaskType.EPIC.toString(), epic2.getName(), epic2.getStatus().toString(), epic2.getDescription(), epic2.getStartTime().get().toString(), epic2.getDuration().get().toString()), lines.get(1));
        Assertions.assertEquals(String.join(",", String.valueOf(subTask1.getId()), TaskType.SUBTASK.toString(), subTask1.getName(), subTask1.getStatus().toString(), subTask1.getDescription(), subTask1.getStartTime().toString(), subTask1.getDuration().toString(), String.valueOf(epic1.getId())), lines.get(2));
        Assertions.assertEquals(String.join(",", String.valueOf(subTask2.getId()), TaskType.SUBTASK.toString(), subTask2.getName(), subTask2.getStatus().toString(), subTask2.getDescription(), subTask2.getStartTime().toString(), subTask2.getDuration().toString(), String.valueOf(epic1.getId())), lines.get(3));
        Assertions.assertEquals(String.join(",", String.valueOf(subTask3.getId()), TaskType.SUBTASK.toString(), subTask3.getName(), subTask3.getStatus().toString(), subTask3.getDescription(), subTask3.getStartTime().toString(), subTask3.getDuration().toString(), String.valueOf(epic1.getId())), lines.get(4));
        Assertions.assertEquals(String.join(",", String.valueOf(subTask4.getId()), TaskType.SUBTASK.toString(), subTask4.getName(), subTask4.getStatus().toString(), subTask4.getDescription(), subTask4.getStartTime().toString(), subTask4.getDuration().toString(), String.valueOf(epic2.getId())), lines.get(5));
        Assertions.assertEquals(String.join(",", String.valueOf(subTask5.getId()), TaskType.SUBTASK.toString(), subTask5.getName(), subTask5.getStatus().toString(), subTask5.getDescription(), subTask5.getStartTime().toString(), subTask5.getDuration().toString(), String.valueOf(epic2.getId())), lines.get(6));
    }

    @Test
    public void getAllSubTasksTest() throws IOException {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        this.taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", LocalDateTime.now(), Duration.ofHours(1), epic1.getId());
        epic1.addSubTask(subTask1);
        this.taskManager.createSubTask(subTask1);

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", LocalDateTime.now().plusHours(1), Duration.ofHours(1), epic1.getId());
        epic1.addSubTask(subTask2);
        this.taskManager.createSubTask(subTask2);

        SubTask subTask3 = new SubTask("Подзадача 3", "Описание подзадачи 3", LocalDateTime.now().plusHours(2), Duration.ofHours(1), epic1.getId());
        epic1.addSubTask(subTask3);
        this.taskManager.createSubTask(subTask3);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        this.taskManager.createEpic(epic2);

        SubTask subTask4 = new SubTask("Подзадача 4", "Описание подзадачи 4", LocalDateTime.now().plusHours(3), Duration.ofHours(1), epic2.getId());
        epic2.addSubTask(subTask4);
        this.taskManager.createSubTask(subTask4);

        SubTask subTask5 = new SubTask("Подзадача 5", "Описание подзадачи 5", LocalDateTime.now().plusHours(4), Duration.ofHours(1), epic2.getId());
        epic2.addSubTask(subTask5);
        this.taskManager.createSubTask(subTask5);

        List<String> lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertArrayEquals(Arrays.asList(subTask1, subTask2, subTask3, subTask4, subTask5).toArray(), this.taskManager.getAllSubTasks().toArray());
        Assertions.assertTrue(epic1.getStartTime().isPresent());
        Assertions.assertTrue(epic1.getDuration().isPresent());
        Assertions.assertEquals(String.join(",", String.valueOf(epic1.getId()), TaskType.EPIC.toString(), epic1.getName(), epic1.getStatus().toString(), epic1.getDescription(), epic1.getStartTime().get().toString(), epic1.getDuration().get().toString()), lines.getFirst());
        Assertions.assertTrue(epic2.getStartTime().isPresent());
        Assertions.assertTrue(epic2.getDuration().isPresent());
        Assertions.assertEquals(String.join(",", String.valueOf(epic2.getId()), TaskType.EPIC.toString(), epic2.getName(), epic2.getStatus().toString(), epic2.getDescription(), epic2.getStartTime().get().toString(), epic2.getDuration().get().toString()), lines.get(1));
        Assertions.assertEquals(String.join(",", String.valueOf(subTask1.getId()), TaskType.SUBTASK.toString(), subTask1.getName(), subTask1.getStatus().toString(), subTask1.getDescription(), subTask1.getStartTime().toString(), subTask1.getDuration().toString(), String.valueOf(epic1.getId())), lines.get(2));
        Assertions.assertEquals(String.join(",", String.valueOf(subTask2.getId()), TaskType.SUBTASK.toString(), subTask2.getName(), subTask2.getStatus().toString(), subTask2.getDescription(), subTask2.getStartTime().toString(), subTask2.getDuration().toString(), String.valueOf(epic1.getId())), lines.get(3));
        Assertions.assertEquals(String.join(",", String.valueOf(subTask3.getId()), TaskType.SUBTASK.toString(), subTask3.getName(), subTask3.getStatus().toString(), subTask3.getDescription(), subTask3.getStartTime().toString(), subTask3.getDuration().toString(), String.valueOf(epic1.getId())), lines.get(4));
        Assertions.assertEquals(String.join(",", String.valueOf(subTask4.getId()), TaskType.SUBTASK.toString(), subTask4.getName(), subTask4.getStatus().toString(), subTask4.getDescription(), subTask4.getStartTime().toString(), subTask4.getDuration().toString(), String.valueOf(epic2.getId())), lines.get(5));
        Assertions.assertEquals(String.join(",", String.valueOf(subTask5.getId()), TaskType.SUBTASK.toString(), subTask5.getName(), subTask5.getStatus().toString(), subTask5.getDescription(), subTask5.getStartTime().toString(), subTask5.getDuration().toString(), String.valueOf(epic2.getId())), lines.get(6));
    }

    @Test
    public void updateSubTaskTest() throws IOException {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic.getId());
        epic.addSubTask(subTask);

        this.taskManager.createSubTask(subTask);

        SubTask subTaskClone = SubTask.clone(subTask);
        subTaskClone.setStatus(TaskStatus.IN_PROGRESS);

        this.taskManager.updateSubTask(subTaskClone);

        List<String> lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertTrue(this.taskManager.getSubTaskById(subTask.getId()).isPresent());
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, this.taskManager.getSubTaskById(subTask.getId()).get().getStatus());

        Assertions.assertTrue(this.taskManager.getEpicById(epic.getId()).isPresent());
        Assertions.assertTrue(this.taskManager.getEpicById(epic.getId()).get().getSubTaskById(subTask.getId()).isPresent());
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, this.taskManager.getEpicById(epic.getId()).get().getSubTaskById(subTask.getId()).get().getStatus());

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, this.taskManager.getEpicById(epic.getId()).get().getStatus());

        Assertions.assertTrue(epic.getStartTime().isPresent());
        Assertions.assertTrue(epic.getDuration().isPresent());
        Assertions.assertEquals(String.join(",", String.valueOf(epic.getId()), TaskType.EPIC.toString(), epic.getName(), epic.getStatus().toString(), epic.getDescription(), epic.getStartTime().get().toString(), epic.getDuration().get().toString()), lines.getFirst());
        Assertions.assertEquals(String.join(",", String.valueOf(subTaskClone.getId()), TaskType.SUBTASK.toString(), subTaskClone.getName(), subTaskClone.getStatus().toString(), subTaskClone.getDescription(), subTaskClone.getStartTime().toString(), subTaskClone.getDuration().toString(), String.valueOf(epic.getId())), lines.get(1));
    }

    @Test
    public void updateNullInsteadOfSubTaskTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.taskManager.updateSubTask(null));
    }

    @Test
    public void updateUnknownSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic.getId());

        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.updateSubTask(subTask));
    }

    @Test
    public void removeSubTaskByIdTest() throws IOException {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic.getId());
        epic.addSubTask(subTask);

        this.taskManager.createSubTask(subTask);
        this.taskManager.removeSubTaskById(subTask.getId());

        List<String> lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertEquals(0, this.taskManager.getAllSubTasks().size());

        Assertions.assertTrue(this.taskManager.getEpicById(epic.getId()).isPresent());
        Assertions.assertEquals(0, this.taskManager.getEpicById(epic.getId()).get().getAllSubTasks().size());

        Assertions.assertEquals(String.join(",", String.valueOf(epic.getId()), TaskType.EPIC.toString(), epic.getName(), epic.getStatus().toString(), epic.getDescription(), null, null), lines.getFirst());
    }

    @Test
    public void removeUnknownSubTaskTest() {
        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.removeSubTaskById(5));
    }

    @Test
    public void removeAllSubTasksTest() throws IOException {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        this.taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", LocalDateTime.now(), Duration.ofHours(1), epic1.getId());
        epic1.addSubTask(subTask1);
        this.taskManager.createSubTask(subTask1);

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", LocalDateTime.now().plusHours(1), Duration.ofHours(1), epic1.getId());
        epic1.addSubTask(subTask2);
        this.taskManager.createSubTask(subTask2);

        SubTask subTask3 = new SubTask("Подзадача 3", "Описание подзадачи 3", LocalDateTime.now().plusHours(2), Duration.ofHours(1), epic1.getId());
        epic1.addSubTask(subTask3);
        this.taskManager.createSubTask(subTask3);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        this.taskManager.createEpic(epic2);

        SubTask subTask4 = new SubTask("Подзадача 4", "Описание подзадачи 4", LocalDateTime.now().plusHours(3), Duration.ofHours(1), epic2.getId());
        epic2.addSubTask(subTask4);
        this.taskManager.createSubTask(subTask4);

        SubTask subTask5 = new SubTask("Подзадача 5", "Описание подзадачи 5", LocalDateTime.now().plusHours(4), Duration.ofHours(1), epic2.getId());
        epic2.addSubTask(subTask5);
        this.taskManager.createSubTask(subTask5);

        this.taskManager.removeAllSubTasks();

        List<String> lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertEquals(0, this.taskManager.getAllSubTasks().size());

        Assertions.assertTrue(this.taskManager.getEpicById(epic1.getId()).isPresent());
        Assertions.assertEquals(0, this.taskManager.getEpicById(epic1.getId()).get().getAllSubTasks().size());

        Assertions.assertTrue(this.taskManager.getEpicById(epic2.getId()).isPresent());
        Assertions.assertEquals(0, this.taskManager.getEpicById(epic2.getId()).get().getAllSubTasks().size());

        Assertions.assertEquals(String.join(",", String.valueOf(epic1.getId()), TaskType.EPIC.toString(), epic1.getName(), epic1.getStatus().toString(), epic1.getDescription(), null, null), lines.get(0));
        Assertions.assertEquals(String.join(",", String.valueOf(epic2.getId()), TaskType.EPIC.toString(), epic2.getName(), epic2.getStatus().toString(), epic2.getDescription(), null, null), lines.get(1));
    }

    @Test
    public void createEpicTest() throws IOException {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        List<String> lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertEquals(1, this.taskManager.getAllEpics().size());
        Assertions.assertEquals(String.join(",", String.valueOf(epic.getId()), TaskType.EPIC.toString(), epic.getName(), epic.getStatus().toString(), epic.getDescription(), null, null), lines.getFirst());
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
    public void getEpicByIdTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        Assertions.assertTrue(this.taskManager.getEpicById(epic.getId()).isPresent());
        Assertions.assertEquals(epic, this.taskManager.getEpicById(epic.getId()).get());
    }

    @Test
    public void getUnknownEpicTest() {
        Assertions.assertTrue(this.taskManager.getEpicById(5).isEmpty());
    }

    @Test
    public void getAllEpicsTest() throws IOException {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        this.taskManager.createEpic(epic1);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        this.taskManager.createEpic(epic2);

        Epic epic3 = new Epic("Эпик 3", "Описание эпика 3");
        this.taskManager.createEpic(epic3);

        List<String> lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertEquals(3, this.taskManager.getAllEpics().size());
        Assertions.assertEquals(String.join(",", String.valueOf(epic1.getId()), TaskType.EPIC.toString(), epic1.getName(), epic1.getStatus().toString(), epic1.getDescription(), null, null), lines.get(0));
        Assertions.assertEquals(String.join(",", String.valueOf(epic2.getId()), TaskType.EPIC.toString(), epic2.getName(), epic2.getStatus().toString(), epic2.getDescription(), null, null), lines.get(1));
        Assertions.assertEquals(String.join(",", String.valueOf(epic3.getId()), TaskType.EPIC.toString(), epic3.getName(), epic3.getStatus().toString(), epic3.getDescription(), null, null), lines.get(2));
    }

    @Test
    public void updateEpicTest() throws IOException {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        Epic epicClone = Epic.clone(epic);
        this.taskManager.updateEpic(epicClone);

        List<String> lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);
        Assertions.assertEquals(String.join(",", String.valueOf(epic.getId()), TaskType.EPIC.toString(), epic.getName(), epic.getStatus().toString(), epic.getDescription(), null, null), lines.getFirst());
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
    public void removeEpicByIdTest() throws IOException {
        Epic epic = new Epic("Эпик", "Описание эпика");
        this.taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", LocalDateTime.now(), Duration.ofHours(8), epic.getId());
        epic.addSubTask(subTask);

        this.taskManager.createSubTask(subTask);

        this.taskManager.removeEpicById(epic.getId());

        List<String> lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertEquals(0, this.taskManager.getAllSubTasks().size());
        Assertions.assertEquals(0, this.taskManager.getAllEpics().size());
        Assertions.assertEquals(0, lines.size());
    }

    @Test
    public void removeUnknownEpicTest() {
        Assertions.assertThrows(IllegalStateException.class, () -> this.taskManager.removeEpicById(5));
    }

    @Test
    public void removeAllEpicsTest() throws IOException {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        this.taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", LocalDateTime.now(), Duration.ofHours(1), epic1.getId());
        epic1.addSubTask(subTask1);
        this.taskManager.createSubTask(subTask1);

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", LocalDateTime.now().plusHours(1), Duration.ofHours(1), epic1.getId());
        epic1.addSubTask(subTask2);
        this.taskManager.createSubTask(subTask2);

        SubTask subTask3 = new SubTask("Подзадача 3", "Описание подзадачи 3", LocalDateTime.now().plusHours(2), Duration.ofHours(1), epic1.getId());
        epic1.addSubTask(subTask3);
        this.taskManager.createSubTask(subTask3);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        this.taskManager.createEpic(epic2);

        SubTask subTask4 = new SubTask("Подзадача 4", "Описание подзадачи 4", LocalDateTime.now().plusHours(3), Duration.ofHours(1), epic2.getId());
        epic2.addSubTask(subTask4);
        this.taskManager.createSubTask(subTask4);

        SubTask subTask5 = new SubTask("Подзадача 5", "Описание подзадачи 5", LocalDateTime.now().plusHours(4), Duration.ofHours(1), epic2.getId());
        epic2.addSubTask(subTask5);
        this.taskManager.createSubTask(subTask5);

        this.taskManager.removeAllEpics();

        List<String> lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertEquals(0, this.taskManager.getAllSubTasks().size());
        Assertions.assertEquals(0, this.taskManager.getAllEpics().size());
        Assertions.assertEquals(0, lines.size());
    }

    @Test
    public void getHistoryTest() throws IOException {
        Task task1 = new Task("Задача №1", "Описание задачи №1", LocalDateTime.now(), Duration.ofHours(1));
        taskManager.createTask(task1);
        taskManager.getTaskById(task1.getId());

        List<String> lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertEquals(1, this.taskManager.getHistory().size());
        Assertions.assertArrayEquals(List.of(task1).toArray(), this.taskManager.getHistory().toArray());
        Assertions.assertEquals(String.join(",", String.valueOf(task1.getId()), TaskType.TASK.toString(), task1.getName(), task1.getStatus().toString(), task1.getDescription(), task1.getStartTime().toString(), task1.getDuration().toString()), lines.getFirst());

        Task task2 = new Task("Задача №2", "Описание задачи №2", LocalDateTime.now().plusHours(1), Duration.ofHours(1));
        taskManager.createTask(task2);
        taskManager.getTaskById(task2.getId());

        lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertEquals(2, this.taskManager.getHistory().size());
        Assertions.assertArrayEquals(Arrays.asList(task1, task2).toArray(), this.taskManager.getHistory().toArray());
        Assertions.assertEquals(String.join(",", String.valueOf(task1.getId()), TaskType.TASK.toString(), task1.getName(), task1.getStatus().toString(), task1.getDescription(), task1.getStartTime().toString(), task1.getDuration().toString()), lines.get(0));
        Assertions.assertEquals(String.join(",", String.valueOf(task2.getId()), TaskType.TASK.toString(), task2.getName(), task2.getStatus().toString(), task2.getDescription(), task2.getStartTime().toString(), task2.getDuration().toString()), lines.get(1));

        taskManager.getTaskById(task1.getId());

        Assertions.assertEquals(2, this.taskManager.getHistory().size());
        Assertions.assertArrayEquals(Arrays.asList(task2, task1).toArray(), this.taskManager.getHistory().toArray());

        taskManager.removeTaskById(task2.getId());

        lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertEquals(1, this.taskManager.getHistory().size());
        Assertions.assertArrayEquals(List.of(task1).toArray(), this.taskManager.getHistory().toArray());
        Assertions.assertEquals(String.join(",", String.valueOf(task1.getId()), TaskType.TASK.toString(), task1.getName(), task1.getStatus().toString(), task1.getDescription(), task1.getStartTime().toString(), task1.getDuration().toString()), lines.getFirst());

        Epic epic1 = new Epic("Эпик №1", "Описание эпика № 1");
        Assertions.assertTrue(epic1.getStartTime().isEmpty());
        Assertions.assertTrue(epic1.getDuration().isEmpty());

        taskManager.createEpic(epic1);
        taskManager.getEpicById(epic1.getId());

        lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertEquals(2, this.taskManager.getHistory().size());
        Assertions.assertArrayEquals(Arrays.asList(task1, epic1).toArray(), this.taskManager.getHistory().toArray());
        Assertions.assertEquals(String.join(",", String.valueOf(epic1.getId()), TaskType.EPIC.toString(), epic1.getName(), epic1.getStatus().toString(), epic1.getDescription(), null, null), lines.get(0));
        Assertions.assertEquals(String.join(",", String.valueOf(task1.getId()), TaskType.TASK.toString(), task1.getName(), task1.getStatus().toString(), task1.getDescription(), task1.getStartTime().toString(), task1.getDuration().toString()), lines.get(1));

        SubTask subTask1 = new SubTask("Подзадача №1", "Описание подзадачи №1", LocalDateTime.now().plusHours(2), Duration.ofHours(1), epic1.getId());
        epic1.addSubTask(subTask1);
        Assertions.assertTrue(epic1.getStartTime().isPresent());
        Assertions.assertTrue(epic1.getDuration().isPresent());

        taskManager.createSubTask(subTask1);
        taskManager.getSubTaskById(subTask1.getId());

        lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertEquals(3, this.taskManager.getHistory().size());
        Assertions.assertArrayEquals(Arrays.asList(task1, epic1, subTask1).toArray(), this.taskManager.getHistory().toArray());
        Assertions.assertEquals(String.join(",", String.valueOf(epic1.getId()), TaskType.EPIC.toString(), epic1.getName(), epic1.getStatus().toString(), epic1.getDescription(), epic1.getStartTime().get().toString(), epic1.getDuration().get().toString()), lines.get(0));
        Assertions.assertEquals(String.join(",", String.valueOf(subTask1.getId()), TaskType.SUBTASK.toString(), subTask1.getName(), subTask1.getStatus().toString(), subTask1.getDescription(), subTask1.getStartTime().toString(), subTask1.getDuration().toString(), String.valueOf(epic1.getId())), lines.get(1));
        Assertions.assertEquals(String.join(",", String.valueOf(task1.getId()), TaskType.TASK.toString(), task1.getName(), task1.getStatus().toString(), task1.getDescription(), task1.getStartTime().toString(), task1.getDuration().toString()), lines.get(2));

        taskManager.getEpicById(epic1.getId());

        Assertions.assertEquals(3, this.taskManager.getHistory().size());
        Assertions.assertArrayEquals(Arrays.asList(task1, subTask1, epic1).toArray(), this.taskManager.getHistory().toArray());

        SubTask subTask2 = new SubTask("Подзадача №2", "Описание подзадачи №2", LocalDateTime.now().plusHours(3), Duration.ofHours(1), epic1.getId());
        epic1.addSubTask(subTask2);
        Assertions.assertTrue(epic1.getStartTime().isPresent());
        Assertions.assertTrue(epic1.getDuration().isPresent());

        taskManager.createSubTask(subTask2);
        taskManager.getSubTaskById(subTask2.getId());

        lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertEquals(4, this.taskManager.getHistory().size());
        Assertions.assertArrayEquals(Arrays.asList(task1, subTask1, epic1, subTask2).toArray(), this.taskManager.getHistory().toArray());
        Assertions.assertEquals(String.join(",", String.valueOf(epic1.getId()), TaskType.EPIC.toString(), epic1.getName(), epic1.getStatus().toString(), epic1.getDescription(), epic1.getStartTime().get().toString(), epic1.getDuration().get().toString()), lines.get(0));
        Assertions.assertEquals(String.join(",", String.valueOf(subTask1.getId()), TaskType.SUBTASK.toString(), subTask1.getName(), subTask1.getStatus().toString(), subTask1.getDescription(), subTask1.getStartTime().toString(), subTask1.getDuration().toString(), String.valueOf(epic1.getId())), lines.get(1));
        Assertions.assertEquals(String.join(",", String.valueOf(subTask2.getId()), TaskType.SUBTASK.toString(), subTask2.getName(), subTask2.getStatus().toString(), subTask2.getDescription(), subTask2.getStartTime().toString(), subTask2.getDuration().toString(), String.valueOf(epic1.getId())), lines.get(2));
        Assertions.assertEquals(String.join(",", String.valueOf(task1.getId()), TaskType.TASK.toString(), task1.getName(), task1.getStatus().toString(), task1.getDescription(), task1.getStartTime().toString(), task1.getDuration().toString()), lines.get(3));

        Epic epic2 = new Epic("Эпик №2", "Описание эпика № 2");
        Assertions.assertTrue(epic2.getStartTime().isEmpty());
        Assertions.assertTrue(epic2.getDuration().isEmpty());

        taskManager.createEpic(epic2);

        SubTask subTask3 = new SubTask("Подзадача №3", "Описание подзадачи №3", LocalDateTime.now().plusHours(4), Duration.ofHours(1), epic2.getId());
        epic2.addSubTask(subTask3);
        Assertions.assertTrue(epic2.getStartTime().isPresent());
        Assertions.assertTrue(epic2.getDuration().isPresent());

        taskManager.createSubTask(subTask3);
        taskManager.getSubTaskById(subTask3.getId());

        lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertEquals(5, this.taskManager.getHistory().size());
        Assertions.assertArrayEquals(Arrays.asList(task1, subTask1, epic1, subTask2, subTask3).toArray(), this.taskManager.getHistory().toArray());
        Assertions.assertEquals(String.join(",", String.valueOf(epic1.getId()), TaskType.EPIC.toString(), epic1.getName(), epic1.getStatus().toString(), epic1.getDescription(), epic1.getStartTime().get().toString(), epic1.getDuration().get().toString()), lines.get(0));
        Assertions.assertEquals(String.join(",", String.valueOf(epic2.getId()), TaskType.EPIC.toString(), epic2.getName(), epic2.getStatus().toString(), epic2.getDescription(), epic2.getStartTime().get().toString(), epic2.getDuration().get().toString()), lines.get(1));
        Assertions.assertEquals(String.join(",", String.valueOf(subTask1.getId()), TaskType.SUBTASK.toString(), subTask1.getName(), subTask1.getStatus().toString(), subTask1.getDescription(), subTask1.getStartTime().toString(), subTask1.getDuration().toString(), String.valueOf(epic1.getId())), lines.get(2));
        Assertions.assertEquals(String.join(",", String.valueOf(subTask2.getId()), TaskType.SUBTASK.toString(), subTask2.getName(), subTask2.getStatus().toString(), subTask2.getDescription(), subTask2.getStartTime().toString(), subTask2.getDuration().toString(), String.valueOf(epic1.getId())), lines.get(3));
        Assertions.assertEquals(String.join(",", String.valueOf(subTask3.getId()), TaskType.SUBTASK.toString(), subTask3.getName(), subTask3.getStatus().toString(), subTask3.getDescription(), subTask3.getStartTime().toString(), subTask3.getDuration().toString(), String.valueOf(epic2.getId())), lines.get(4));
        Assertions.assertEquals(String.join(",", String.valueOf(task1.getId()), TaskType.TASK.toString(), task1.getName(), task1.getStatus().toString(), task1.getDescription(), task1.getStartTime().toString(), task1.getDuration().toString()), lines.get(5));

        taskManager.removeEpicById(epic1.getId());

        lines = Files.readAllLines(this.storage.toPath(), StandardCharsets.UTF_8);

        Assertions.assertEquals(2, this.taskManager.getHistory().size());
        Assertions.assertArrayEquals(Arrays.asList(task1, subTask3).toArray(), this.taskManager.getHistory().toArray());
        Assertions.assertEquals(String.join(",", String.valueOf(epic2.getId()), TaskType.EPIC.toString(), epic2.getName(), epic2.getStatus().toString(), epic2.getDescription(), epic2.getStartTime().get().toString(), epic2.getDuration().get().toString()), lines.get(0));
        Assertions.assertEquals(String.join(",", String.valueOf(subTask3.getId()), TaskType.SUBTASK.toString(), subTask3.getName(), subTask3.getStatus().toString(), subTask3.getDescription(), subTask3.getStartTime().toString(), subTask3.getDuration().toString(), String.valueOf(epic2.getId())), lines.get(1));
        Assertions.assertEquals(String.join(",", String.valueOf(task1.getId()), TaskType.TASK.toString(), task1.getName(), task1.getStatus().toString(), task1.getDescription(), task1.getStartTime().toString(), task1.getDuration().toString()), lines.get(2));
    }
}
