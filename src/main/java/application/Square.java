package application;

import application.settings.RenderSettings;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Square {
    public Coordinates coordinates;
    public int freeFood; //количество энергии на клетке, которую можно сразу съесть
    public int closeFood; //количество энергии на клетке по типу энергии света, которую не каждый может достать
    public int freeFoodOnLastFrame; //количество энергии на клетке в прошлом кадре (если для клетки не менялась еда, то её и не надо перекрашивать
    public int closeFoodOnLastFrame; //закрытая энергия в прошлом
    public List<Object> items; //на клетке может находиться несколько клеток

    public Color color; //будем устанавливать цвет самой клетки внутри класса
    public Color frontGroundColor = null; //этот цвет перезатирает "свой" цвет квадрата

    public Square(Coordinates coordinates, int freeFood, int closeFood, List<Object> items) {
        this.coordinates = coordinates;
        this.freeFood = freeFood;
        this.closeFood = closeFood;
        this.items = items;
        this.color = Color.color(1, 1, 1);
    }

    public void calculateColor(Boolean forceCalculate, RenderSettings renderSettings) {
        if (renderSettings.isCalculatingColor) {
            if (freeFood != freeFoodOnLastFrame || forceCalculate) {
                float freeFood = (float) this.freeFood;
                float green = 1000 - freeFood;
                if (green < 0) {
                    green = 0;
                }
                float blue = 1000 - freeFood;
                if (blue < 0) {
                    blue = 0;
                }
                this.color = Color.color(this.color.getRed(), (green / 1000), (blue / 1000));
            }
            if (closeFood != closeFoodOnLastFrame || forceCalculate) {
                float closeFood = (float) this.closeFood;
                float red = 1000 - closeFood;
                if (red < 0) {
                    red = 0;
                }
                float blue = 1000 - closeFood;
                if (blue < 0) {
                    blue = 0;
                }
                this.color = Color.color((red / 1000), this.color.getGreen(), (blue / 1000));
            }
        }
    }

    public void calculateEating(List<Cell> cells, RenderSettings renderSettings) { //тут сразу все методы - свободная еда, закрытая, сражения
        if (this.items.size() == 1) { //обсчитываем свободную или закрытую еду и всё
            Cell cell = (Cell) this.items.get(0);
            cell.energy += this.freeFood;
            this.freeFood = 0;
            clearItems();
            calculateColor(true, renderSettings);
        }
        if (this.items.size() == 2) {
            Cell firstCell = (Cell) this.items.get(0);
            Cell secondCell = (Cell) this.items.get(1);
            if(!Objects.equals(firstCell.name, secondCell.name)) {
                if (Optional.ofNullable(firstCell.attack).orElse(0) > Optional.ofNullable(secondCell.defence).orElse(0)) {
                    firstCell.energy += secondCell.energy;
                    this.removeObjectFromSquareItems(secondCell);
                    cells.remove(secondCell);
                }
                if (Optional.ofNullable(secondCell.attack).orElse(0) > Optional.ofNullable(firstCell.defence).orElse(0)) {
                    secondCell.energy += firstCell.energy;
                    this.removeObjectFromSquareItems(firstCell);
                    cells.remove(firstCell);
                }
            }
        }
    }

    public void addObjectToSquareItems(Object object) {
        this.items.add(object);
    }

    public void removeObjectFromSquareItems(Object object) {
        List<Object> notMatchedObjects = this.items.stream().filter(e -> !e.equals(object)).toList();
        clearItems();
        this.items.addAll(notMatchedObjects);
    }

    public void clearItems() {
        this.items = new ArrayList<>();
    }
}
