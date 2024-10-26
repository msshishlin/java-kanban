package ru.yandex.practicum;

// region imports

import ru.yandex.practicum.managers.tasks.InMemoryTaskManager;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;
import ru.yandex.practicum.models.Task;

import java.time.Duration;
import java.time.LocalDateTime;

// endregion

public class Main {

    private static final InMemoryTaskManager taskManager = new InMemoryTaskManager();

    public static void main(String[] args) {
        Task task1 = new Task("Задача №1", "Описание задачи №1", LocalDateTime.now(), Duration.ofHours(8));
        Task task2 = new Task("Задача №2", "Описание задачи №2", LocalDateTime.now(), Duration.ofHours(8));
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Эпик №1", "Эпик с 3 подзадачами");
        taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Подзадача №1", "Описание подзадачи №1", LocalDateTime.now(), Duration.ofHours(8), epic1.getId());
        epic1.addSubTask(subTask1);
        taskManager.createSubTask(subTask1);

        SubTask subTask2 = new SubTask("Подзадача №2", "Описание подзадачи №2", LocalDateTime.now(), Duration.ofHours(8), epic1.getId());
        epic1.addSubTask(subTask2);
        taskManager.createSubTask(subTask2);

        SubTask subTask3 = new SubTask("Подзадача №3", "Описание подзадачи №3", LocalDateTime.now(), Duration.ofHours(8), epic1.getId());
        epic1.addSubTask(subTask3);
        taskManager.createSubTask(subTask3);

        Epic epic2 = new Epic("Эпик №2", "Эпик без подзадач");
        taskManager.createEpic(epic2);

        System.out.println("Выводим список задач в порядке создания:");
        System.out.println(taskManager.getTaskById(task1.getId()));
        System.out.println(taskManager.getTaskById(task2.getId()));
        System.out.println(taskManager.getEpicById(epic1.getId()));
        System.out.println(taskManager.getSubTaskById(subTask1.getId()));
        System.out.println(taskManager.getSubTaskById(subTask2.getId()));
        System.out.println(taskManager.getSubTaskById(subTask3.getId()));
        System.out.println(taskManager.getEpicById(epic2.getId()));

        System.out.println("Выводим подзадачи первого эпика:");
        System.out.println(taskManager.getSubTaskById(subTask1.getId()));
        System.out.println(taskManager.getSubTaskById(subTask2.getId()));
        System.out.println(taskManager.getSubTaskById(subTask3.getId()));

        System.out.println("Выводим эпики:");
        System.out.println(taskManager.getEpicById(epic1.getId()));
        System.out.println(taskManager.getEpicById(epic2.getId()));

        System.out.println("Выводим задачи");
        System.out.println(taskManager.getTaskById(task1.getId()));
        System.out.println(taskManager.getTaskById(task2.getId()));

        System.out.println("Проверяем историю просмотров:");
        System.out.println(taskManager.getHistory());

        System.out.println("Удаляем задачу №1");
        taskManager.removeTaskById(task1.getId());

        System.out.println("Проверяем историю просмотров:");
        System.out.println(taskManager.getHistory());

        System.out.println("Удаляем эпик №1");
        taskManager.removeEpicById(epic1.getId());

        System.out.println("Проверяем историю просмотров:");
        System.out.println(taskManager.getHistory());
    }
}
