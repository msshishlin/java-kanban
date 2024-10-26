package ru.yandex.practicum.api.http.handlers;

// region imports

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.constants.HttpMethod;

import java.io.IOException;

// endregion

public final class HistoryHttpHandler extends BaseHttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if (!httpExchange.getRequestMethod().equals(HttpMethod.GET) || !httpExchange.getRequestURI().getPath().equals("/history")) {
            this.sendNotFound(httpExchange);
            return;
        }

        this.sendText(httpExchange, this.gson.toJson(this.taskManager.getHistory()));
    }
}
