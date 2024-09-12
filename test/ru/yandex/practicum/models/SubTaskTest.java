package ru.yandex.practicum.models;

// region imports

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.constants.TaskStatus;

// endregion

public class SubTaskTest {
    @Test
    public void createSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);

        Assertions.assertEquals("Подзадача", subTask.getName());
        Assertions.assertEquals("Описание подзадачи", subTask.getDescription());
        Assertions.assertEquals(TaskStatus.NEW, subTask.getStatus());
        Assertions.assertEquals(epic, subTask.getEpic());
    }

    @Test
    public void createSubTaskWithoutEpicTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new SubTask("Подзадача", "Описание подзадачи", null));
    }

    @Test
    public void toStringTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);
        epic.addSubTask(subTask);

        String expected = subTask.getClass().getSimpleName() + "{" + "id: " + subTask.getId() + ", " + "name: "
                + subTask.getName() + ", " + "description: " + subTask.getDescription() + ", " + "status: "
                + subTask.getStatus().name() + ", " + "epic: " + subTask.getEpic() + "}";

        Assertions.assertEquals(expected, subTask.toString());
    }

    @Test
    public void cloneSubTaskTest() {
        Epic epic = new Epic("Эпик", "Описание эпика");

        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", epic);
        SubTask subTaskClone = subTask.clone();

        Assertions.assertEquals(subTask.getId(), subTaskClone.getId());
        Assertions.assertEquals(subTask.getName(), subTaskClone.getName());
        Assertions.assertEquals(subTask.getDescription(), subTaskClone.getDescription());
        Assertions.assertEquals(subTask.getStatus(), subTaskClone.getStatus());
        Assertions.assertEquals(subTask.getEpic(), subTaskClone.getEpic());
    }
}