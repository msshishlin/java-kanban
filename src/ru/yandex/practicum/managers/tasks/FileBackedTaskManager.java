package ru.yandex.practicum.managers.tasks;

// region imports

import ru.yandex.practicum.constants.TaskStatus;
import ru.yandex.practicum.constants.TaskType;
import ru.yandex.practicum.exceptions.ManagerLoadException;
import ru.yandex.practicum.exceptions.ManagerSaveException;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

// endregion

public final class FileBackedTaskManager extends InMemoryTaskManager {
    /**
     * Файл, представляющий собой хранилище данных.
     */
    private final File storage;

    /**
     * Конструктор.
     *
     * @param storage файл, представляющий собой хранилище данных.
     */
    public FileBackedTaskManager(File storage) {
        super();

        if (storage == null) {
            throw new IllegalArgumentException("Parameter 'storage' can't be null");
        }

        this.storage = storage;
    }

    /**
     * Загрузить данные из файла хранилища данных.
     *
     * @param taskManager менеджер задач.
     * @param storage     файл, представляющий собой хранилище данных.
     */
    public static void loadFromFile(FileBackedTaskManager taskManager, File storage) throws ManagerLoadException {
        try {
            List<String> lines = Files.readAllLines(storage.toPath(), StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] parts = line.split(",");

                TaskType taskType = TaskType.valueOf(parts[1]);
                switch (taskType) {
                    case EPIC: {
                        int id = Integer.parseInt(parts[0]);
                        String name = parts[2];
                        String description = parts[4];
                        TaskStatus status = TaskStatus.valueOf(parts[3]);

                        Epic epic = new Epic(id, name, description, status, new HashMap<>());

                        taskManager.createEpic(epic);
                        break;
                    }
                    case SUBTASK: {
                        int id = Integer.parseInt(parts[0]);
                        String name = parts[2];
                        String description = parts[4];
                        TaskStatus status = TaskStatus.valueOf(parts[3]);
                        int epicId = Integer.parseInt(parts[5]);

                        Epic epic = taskManager.getEpicById(epicId);

                        SubTask subTask = new SubTask(id, name, description, status, epic);
                        epic.addSubTask(subTask);

                        taskManager.createSubTask(subTask);
                        break;
                    }
                    case TASK: {
                        int id = Integer.parseInt(parts[0]);
                        String name = parts[2];
                        String description = parts[4];
                        TaskStatus status = TaskStatus.valueOf(parts[3]);

                        Task task = new Task(id, name, description, status);

                        taskManager.createTask(task);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка при загрузке данных из файла: " + storage.getName(), e);
        }
    }

    /**
     * Сохранить текущее состояние в файл, представляющий собой хранилище данных.
     */
    private void save() throws ManagerSaveException {
        try (FileWriter fileWriter = new FileWriter(this.storage, StandardCharsets.UTF_8, false)) {
            for (Epic epic : this.epics.values()) {
                fileWriter.write(String.format("%s%n", epic.toCsvString()));
            }

            for (SubTask subTask : this.subTasks.values()) {
                fileWriter.write(String.format("%s%n", subTask.toCsvString()));
            }

            for (Task task : this.tasks.values()) {
                fileWriter.write(String.format("%s%n", task.toCsvString()));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных в файл: " + this.storage.getName(), e);
        }
    }

    // region Overrides of InMemoryTaskManager

    // region Задачи

    /**
     * Создать задачу.
     *
     * @param task задача.
     */
    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    /**
     * Обновить задачу.
     *
     * @param task задача.
     */
    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    /**
     * Удалить задачу по её идентификатору.
     *
     * @param taskId идентификатор задачи.
     */
    @Override
    public void removeTaskById(int taskId) {
        super.removeTaskById(taskId);
        save();
    }

    /**
     * Удалить все задачи.
     */
    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    // endregion

    // region Подзадачи

    /**
     * Создать подзадачу.
     *
     * @param subTask подзадача.
     */
    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    /**
     * Обновить подзадачу.
     *
     * @param subTask подзадача.
     */
    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    /**
     * Удалить подзадачу по её идентификаторку.
     *
     * @param subTaskId идентификатор подзадачи
     */
    @Override
    public void removeSubTaskById(int subTaskId) {
        super.removeSubTaskById(subTaskId);
        save();
    }

    /**
     * Удалить все подзадачи.
     */
    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        save();
    }

    // endregion

    // region Эпики

    /**
     * Создать эпик.
     *
     * @param epic эпик.
     */
    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    /**
     * Обновить эпик.
     *
     * @param epic эпик.
     */
    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    /**
     * Удалить эпик по его идентификатору.
     *
     * @param epicId идентификатор эпика.
     */
    @Override
    public void removeEpicById(int epicId) {
        super.removeEpicById(epicId);
        save();
    }

    /**
     * Удалить все эпики.
     */
    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    // endregion

    // endregion
}
