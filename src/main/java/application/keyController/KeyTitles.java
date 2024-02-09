package application.keyController;

import javafx.scene.input.KeyCode;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class KeyTitles {

    public List<Key> keyList = List.of(
            new Key("Пробел", "Поставить игру на паузу", KeyCode.SPACE),
            new Key("Стрелка наверх", "Создать клетки", KeyCode.UP),
            new Key("Стрелка влево", "Включить/отключить добавление еды", KeyCode.LEFT),
            new Key("Стрелка вниз", "Включить/отключить добавление только close-еды", KeyCode.DOWN),
            new Key("Стрелка вправо", "Добавить еду в определенном участке", KeyCode.RIGHT),
            new Key("Кнопка 1", "Поставить скорость игры х1", KeyCode.DIGIT1),
            new Key("Кнопка 2", "Поставить скорость игры х2", KeyCode.DIGIT2),
            new Key("Кнопка 3", "Поставить скорость игры х10", KeyCode.DIGIT3)
    );
    public Map<KeyCode, Key> mapKeyByKeyCodes;

    public KeyTitles() {
        this.mapKeyByKeyCodes = keyList.stream().collect(Collectors.toMap(Key::getKeyCode, Function.identity()));
    }
}

