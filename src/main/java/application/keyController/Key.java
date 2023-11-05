package application.keyController;

import javafx.scene.input.KeyCode;

import java.lang.reflect.Method;

public class Key {
    private String name;
    private String description;
    private KeyCode keyCode;

    Key(String name, String description, KeyCode keyCode) {
        this.name = name;
        this.description = description;
        this.keyCode = keyCode;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public KeyCode getKeyCode() {
        return this.keyCode;
    }

}
