package application.renders;

import application.Cell;
import application.keyController.Key;
import application.keyController.KeyTitles;

import java.util.ArrayList;
import java.util.List;

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
        String gameCondition = "";
        List<Cell> redCells = cellList.stream().filter(e -> e.name.equals("red")).toList();
        List<Cell> blackCells = cellList.stream().filter(e -> e.name.equals("black")).toList();
        List<Cell> greenCells = cellList.stream().filter(e -> e.name.equals("green")).toList();
        List<Cell> blueCells = cellList.stream().filter(e -> e.name.equals("blue")).toList();
        gameCondition = String.format("Красных клеток: %s\n" +
                "Черных клеток: %s\n" +
                "Синих клеток: %s\n" +
                "Зеленых клеток: %s\n",
                redCells.size(), blackCells.size(), blueCells.size(), greenCells.size());
        return gameCondition;
    }

    // Показывает статистику игры, сколько всего было клеток
    public String getGameStatistics() {
        return "";
    }

    // Показывает настройки игры
    public String getGameSettings() {
        return "";
    }

}
