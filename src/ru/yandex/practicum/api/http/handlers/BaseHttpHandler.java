package ru.yandex.practicum.api.http.handlers;

// region imports

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.Managers;
import ru.yandex.practicum.abstractions.TaskManager;
import ru.yandex.practicum.utils.json.DurationTypeAdapter;
import ru.yandex.practicum.utils.json.LocalDateTimeTypeAdapter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

// endregion

public abstract class BaseHttpHandler implements HttpHandler {
    protected final TaskManager taskManager;
    protected final Gson gson;

    protected BaseHttpHandler() {
        this.taskManager = Managers.getDefault();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
    }

    protected void sendText(HttpExchange httpExchange, String text) throws IOException {
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(200, 0);
        httpExchange.getResponseBody().write(text.getBytes(StandardCharsets.UTF_8));
        httpExchange.close();
    }

    protected void sendOk(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(200, 0);
        httpExchange.close();
    }

    protected void sendCreated(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(201, 0);
        httpExchange.close();
    }

    protected void sendBadRequest(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(400, 0);
        httpExchange.close();
    }

    protected void sendBadRequest(HttpExchange httpExchange, String text) throws IOException {
        httpExchange.sendResponseHeaders(400, 0);
        httpExchange.getResponseBody().write(text.getBytes(StandardCharsets.UTF_8));
        httpExchange.close();
    }

    protected void sendNotFound(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(404, 0);
        httpExchange.close();
    }

    protected void sendInternalServerError(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(500, 0);
        httpExchange.close();
    }
}
