package ru.yandex.practicum.exceptions;

public class ManagerLoadException extends RuntimeException {
    /**
     * Конструктор.
     */
    public ManagerLoadException() {
        super();
    }

    /**
     * Конструктор.
     * @param message сообщение, содержащее детали исключения.
     */
    public ManagerLoadException(String message) {
        super(message);
    }

    /**
     * Конструктор.
     * @param message сообщение, содержащее детали исключения.
     * @param cause изначальное исключение.
     */
    public ManagerLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Конструктор.
     * @param cause изначальное исключение.
     */
    public ManagerLoadException(Throwable cause) {
        super(cause);
    }
}
