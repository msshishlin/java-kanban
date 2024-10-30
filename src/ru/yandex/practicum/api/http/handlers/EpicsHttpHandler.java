package ru.yandex.practicum.api.http.handlers;

// region imports

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.api.dto.EpicDto;
import ru.yandex.practicum.constants.HttpMethod;
import ru.yandex.practicum.models.Epic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

// endregion

public final class EpicsHttpHandler extends BaseHttpHandler {
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
        if (!path.startsWith("/epics")) {
            this.sendNotFound(httpExchange);
            return;
        }

        if (path.equals("/epics")) {
            try {
                this.sendText(httpExchange, this.gson.toJson(this.taskManager.getAllEpics()));
                return;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                this.sendInternalServerError(httpExchange);
                return;
            }
        }

        String[] pathParts = path.split("/");
        if (pathParts.length != 3) {
            this.sendNotFound(httpExchange);
            return;
        }

        int epicId;
        try {
            epicId = Integer.parseInt(pathParts[2]);
        } catch (Exception ex) {
            this.sendBadRequest(httpExchange);
            return;
        }

        Optional<Epic> epic = this.taskManager.getEpicById(epicId);
        if (epic.isEmpty()) {
            this.sendNotFound(httpExchange);
            return;
        }

        this.sendText(httpExchange, this.gson.toJson(epic.get()));
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        if (!path.equals("/epics")) {
            this.sendNotFound(httpExchange);
            return;
        }

        String body = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        try {
            EpicDto epicDto = this.gson.fromJson(body, EpicDto.class);
            if (epicDto.id == null) {
                this.taskManager.createEpic(new Epic(epicDto.name, epicDto.description));
            } else {
                Optional<Epic> epic = this.taskManager.getEpicById(epicDto.id);
                if (epic.isEmpty()) {
                    this.sendBadRequest(httpExchange, "Epic with id '" + epicDto.id + "' not found");
                    return;
                }

                this.taskManager.updateEpic(new Epic(epicDto.id, epicDto.name, epicDto.description, epic.get().getSubTasks()));
            }
            this.sendCreated(httpExchange);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            System.out.println(ex.getMessage());
            this.sendBadRequest(httpExchange, ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            this.sendInternalServerError(httpExchange);
        }
    }

    private void handleDelete(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        if (!path.startsWith("/epics")) {
            this.sendNotFound(httpExchange);
            return;
        }

        String[] pathParts = path.split("/");
        if (pathParts.length != 3) {
            this.sendNotFound(httpExchange);
            return;
        }

        int epicId;
        try {
            epicId = Integer.parseInt(pathParts[2]);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            this.sendBadRequest(httpExchange);
            return;
        }

        try {
            this.taskManager.removeEpicById(epicId);
        } catch (IllegalStateException ex) {
            System.out.println(ex.getMessage());
            this.sendNotFound(httpExchange);
            return;
        }

        this.sendOk(httpExchange);
    }
}
