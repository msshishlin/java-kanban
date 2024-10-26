package ru.yandex.practicum.api.http.handlers;

// region imports

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.api.dto.TaskDto;
import ru.yandex.practicum.constants.HttpMethod;
import ru.yandex.practicum.models.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

// endregion

public final class TasksHttpHandler extends BaseHttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        switch (httpExchange.getRequestMethod()) {
            case HttpMethod.GET:
                this.handleGet(httpExchange);
                break;
            case HttpMethod.POST:
                this.handlePost(httpExchange);
                break;
            case HttpMethod.DELETE:
                this.handleDelete(httpExchange);
                break;
            default:
                this.sendNotFound(httpExchange);
                break;
        }
    }

    private void handleGet(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        if (!path.startsWith("/tasks")) {
            this.sendNotFound(httpExchange);
            return;
        }

        if (path.equals("/tasks")) {
            this.sendText(httpExchange, this.gson.toJson(this.taskManager.getAllTasks()));
            return;
        }

        String[] pathParts = path.split("/");
        if (pathParts.length != 3) {
            this.sendNotFound(httpExchange);
            return;
        }

        int taskId;
        try {
            taskId = Integer.parseInt(pathParts[2]);
        } catch (Exception ex) {
            this.sendBadRequest(httpExchange);
            return;
        }

        Optional<Task> task = this.taskManager.getTaskById(taskId);
        if (task.isEmpty()) {
            this.sendNotFound(httpExchange);
            return;
        }

        this.sendText(httpExchange, this.gson.toJson(task.get()));
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        if (!path.equals("/tasks")) {
            this.sendNotFound(httpExchange);
            return;
        }

        String body = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        try {
            TaskDto taskDto = this.gson.fromJson(body, TaskDto.class);
            if (taskDto.id == null) {
                this.taskManager.createTask(new Task(taskDto.name, taskDto.description, taskDto.startTime, taskDto.duration));
            } else {
                this.taskManager.updateTask(new Task(taskDto.id, taskDto.name, taskDto.description, taskDto.status, taskDto.startTime, taskDto.duration));
            }
            this.sendCreated(httpExchange);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            this.sendBadRequest(httpExchange, ex.getMessage());
        } catch (Exception ex) {
            this.sendInternalServerError(httpExchange);
        }
    }

    private void handleDelete(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        if (!path.startsWith("/tasks")) {
            this.sendNotFound(httpExchange);
            return;
        }

        String[] pathParts = path.split("/");
        if (pathParts.length != 3) {
            this.sendNotFound(httpExchange);
            return;
        }

        int taskId;
        try {
            taskId = Integer.parseInt(pathParts[2]);
        } catch (Exception ex) {
            this.sendBadRequest(httpExchange);
            return;
        }

        try {
            this.taskManager.removeTaskById(taskId);
        } catch (IllegalStateException ex) {
            this.sendNotFound(httpExchange);
        }

        this.sendOk(httpExchange);
    }
}
