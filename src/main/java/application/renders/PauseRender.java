package application.renders;

import application.Cell;
import application.Main;
import application.keyController.Key;
import application.keyController.KeyTitles;

import java.util.*;

// класс, который определяет, что выдавать на экран при нажатии на паузу
public class PauseRender {

    // Показывает, какие настройки кнопок есть в игре
    public String getKeyDescriptions(KeyTitles keyTitles) {
        String keyDescription = "";
        for (Key currentKey : keyTitles.keyList) {
            keyDescription += currentKey.getName() + " - "
                    + currentKey.getDescription() + "\n";
        }
        return keyDescription;
    }

    // Показывает, какое текущее состояние игры (сколько клеток, какого цвета)
    public String getGameCondition(List<Cell> cellList) {
        Map<String, Integer> cellCountByColor = new HashMap<>();
        cellList.forEach(cell -> {
            cellCountByColor.merge(cell.name, 1, Integer::sum);
        });
        StringJoiner gameConditionJoiner = new StringJoiner("\n");
        gameConditionJoiner.add("Текущее состояние игры");
        gameConditionJoiner.add(String.format("Кадров отрисовано: %s", Main.currentTick));
        cellCountByColor.forEach((color, count) -> {
            gameConditionJoiner.add(String.format("Клеток цвета %s: %s", color, count));
        });
        return gameConditionJoiner.toString();
    }

    // Показывает настройки игры
    public String getGameSettings() {
        return "";
    }

}
