package ru.yandex.practicum.exceptions;

public class ManagerSaveException extends RuntimeException {
    /**
     * Конструктор.
     */
    public ManagerSaveException() {
        super();
    }

    /**
     * Конструктор.
     * @param message сообщение, содержащее детали исключения.
     */
    public ManagerSaveException(String message) {
        super(message);
    }

    /**
     * Конструктор.
     * @param message сообщение, содержащее детали исключения.
     * @param cause изначальное исключение.
     */
    public ManagerSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Конструктор.
     * @param cause изначальное исключение.
     */
    public ManagerSaveException(Throwable cause) {
        super(cause);
    }
}
