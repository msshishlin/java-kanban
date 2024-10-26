package ru.yandex.practicum;

// region imports

import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.api.http.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

// endregion

public class HttpTaskServer {
    public static void main(String[] args) {
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
            httpServer.createContext("/epics", new EpicsHttpHandler());
            httpServer.createContext("/history", new HistoryHttpHandler());
            httpServer.createContext("/prioritized", new PrioritizedTasksHttpHandler());
            httpServer.createContext("/subtasks", new SubTasksHttpHandler());
            httpServer.createContext("/tasks", new TasksHttpHandler());
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
