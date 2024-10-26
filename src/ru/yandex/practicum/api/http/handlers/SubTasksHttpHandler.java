package ru.yandex.practicum.api.http.handlers;

// region imports

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.api.dto.SubTaskDto;
import ru.yandex.practicum.constants.HttpMethod;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.SubTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

// endregion

public final class SubTasksHttpHandler extends BaseHttpHandler {
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
        if (!path.startsWith("/subtasks")) {
            this.sendNotFound(httpExchange);
            return;
        }

        if (path.equals("/subtasks")) {
            try {
                this.sendText(httpExchange, this.gson.toJson(this.taskManager.getAllSubTasks()));
                return;
            } catch (Throwable ex) {
                this.sendInternalServerError(httpExchange);
                return;
            }
        }

        String[] pathParts = path.split("/");
        if (pathParts.length != 3) {
            this.sendNotFound(httpExchange);
            return;
        }

        int subTaskId;
        try {
            subTaskId = Integer.parseInt(pathParts[2]);
        } catch (Exception ex) {
            this.sendBadRequest(httpExchange);
            return;
        }

        Optional<SubTask> subTask = this.taskManager.getSubTaskById(subTaskId);
        if (subTask.isEmpty()) {
            this.sendNotFound(httpExchange);
            return;
        }

        this.sendText(httpExchange, this.gson.toJson(subTask.get()));
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        if (!path.equals("/subtasks")) {
            this.sendNotFound(httpExchange);
            return;
        }

        String body = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        try {
            SubTaskDto subTaskDto = this.gson.fromJson(body, SubTaskDto.class);
            if (subTaskDto.epicId == null) {
                this.sendBadRequest(httpExchange, "Property 'epicId' can't be null");
                return;
            }

            Optional<Epic> epic = this.taskManager.getEpicById(subTaskDto.epicId);
            if (epic.isEmpty()) {
                this.sendBadRequest(httpExchange, "Epic with id '" + subTaskDto.epicId + "' not found");
                return;
            }

            if (subTaskDto.id == null) {
                SubTask subTask = new SubTask(subTaskDto.name, subTaskDto.description, subTaskDto.startTime, subTaskDto.duration, subTaskDto.epicId);
                epic.get().addSubTask(subTask);

                this.taskManager.createSubTask(subTask);
            } else {
                this.taskManager.updateSubTask(new SubTask(subTaskDto.id, subTaskDto.name, subTaskDto.description, subTaskDto.status, subTaskDto.startTime, subTaskDto.duration, subTaskDto.epicId));
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
        if (!path.startsWith("/subtasks")) {
            this.sendNotFound(httpExchange);
            return;
        }

        String[] pathParts = path.split("/");
        if (pathParts.length != 3) {
            this.sendNotFound(httpExchange);
            return;
        }

        int subTaskId;
        try {
            subTaskId = Integer.parseInt(pathParts[2]);
        } catch (Exception ex) {
            this.sendBadRequest(httpExchange);
            return;
        }

        try {
            this.taskManager.removeSubTaskById(subTaskId);
        } catch (IllegalStateException ex) {
            this.sendNotFound(httpExchange);
        }

        this.sendOk(httpExchange);
    }
}
