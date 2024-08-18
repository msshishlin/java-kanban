package ru.yandex.practicum;

// region imports

import ru.yandex.practicum.constants.TaskStatus;
import ru.yandex.practicum.managers.tasks.InMemoryTaskManager;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

// endregion

public class Main {

    private static final InMemoryTaskManager taskManager = new InMemoryTaskManager();

    public static void main(String[] args) {
        Task task1 = new Task("Задача №1", "Описание задачи №1");
        Task task2 = new Task("Задача №2", "Описание задачи №2");
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Эпик №1", "Описание эпика №1");
        taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Подзадача №1", "Описание подзадачи №1", epic1);
        epic1.addSubTask(subTask1);
        taskManager.createSubTask(subTask1);

        SubTask subTask2 = new SubTask("Подзадача №2", "Описание подзадачи №2", epic1);
        epic1.addSubTask(subTask2);
        taskManager.createSubTask(subTask2);

        Epic epic2 = new Epic("Эпик №2", "Описание эпика №2");
        taskManager.createEpic(epic2);

        SubTask subTask3 = new SubTask("Подзадача №3", "Описание подзадачи №3", epic2);
        epic2.addSubTask(subTask3);
        taskManager.createSubTask(subTask3);

        System.out.println("========== Списки до изменений ========== ");
        System.out.println();

        printAll();

        Task task1Copy = task1.clone();
        task1Copy.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task1Copy);

        SubTask subTask1Copy = subTask1.clone();
        subTask1Copy.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(subTask1Copy);

        SubTask subTask3Copy = subTask3.clone();
        subTask3Copy.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTask3Copy);

        System.out.println("========== Списки после изменений ========== ");
        System.out.println();

        printAll();

        taskManager.removeTaskById(task2.getId());
        taskManager.removeEpicById(epic2.getId());

        System.out.println("========== Списки после удалений ========== ");
        System.out.println();

        printAll();

        taskManager.removeAllTasks();
        taskManager.removeAllSubTasks();
        taskManager.removeAllEpics();

        System.out.println("========== Списки после зачистки ========== ");
        System.out.println();

        printAll();
    }

    private static void printAll() {
        printAllTasks();
        printAllSubTasks();
        printAllEpics();
    }

    private static void printAllTasks() {
        System.out.println("Список задач:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println();
    }

    private static void printAllSubTasks() {
        System.out.println("Список подзадач:");
        for (SubTask subTask : taskManager.getAllSubTasks()) {
            System.out.println(subTask);
        }
        System.out.println();
    }

    private static void printAllEpics() {
        System.out.println("Список эпиков:");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic);
        }
        System.out.println();
    }
}
