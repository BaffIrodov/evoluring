package application;

import javafx.scene.paint.Color;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static application.Main.*;

public class Square {
    public Coordinates coordinates;
    public int freeFood; //количество энергии на клетке, которую можно сразу съесть
    public int closeFood; //количество энергии на клетке по типу энергии света, которую не каждый может достать
    public int freeFoodOnLastFrame; //количество энергии на клетке в прошлом кадре (если для клетки не менялась еда, то её и не надо перекрашивать
    public int closeFoodOnLastFrame; //закрытая энергия в прошлом
    public List<Object> items; //на клетке может находиться несколько клеток, так же закладываюсь на то, что это не только клетки

    public Color color; //будем устанавливать цвет самой клетки внутри класса
    public Color frontGroundColor = null; //этот цвет перезатирает "свой" цвет квадрата

    public Square(Coordinates coordinates, int freeFood, int closeFood, List<Object> items) {
        this.coordinates = coordinates;
        this.freeFood = freeFood;
        this.closeFood = closeFood;
        this.items = items;
        this.color = Color.color(1, 1, 1);
    }

    public void calculateColor(Boolean forceCalculate) {
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

    public void calculateEating(List<Cell> cells) { //тут сразу все методы - свободная еда, закрытая, сражения
        if (this.items.size() == 1) { //обсчитываем свободную или закрытую еду и всё
            Cell cell = (Cell) this.items.get(0);
            cell.energy += this.freeFood;
            this.freeFood = 0;
        }
        if (this.items.size() > 1) {
            Map<String, Integer> attackPlusDefenceByCellName = new HashMap<>();
            Map<String, Integer> energyByCellName = new HashMap<>();
            Map<String, Integer> cellCountByCellName = new HashMap<>();
            this.items.forEach(item -> {
                Cell cell = (Cell) item;
                attackPlusDefenceByCellName.merge(cell.name, cell.attack + cell.defence, Integer::sum);
                energyByCellName.merge(cell.name, cell.energy, Integer::sum);
                cellCountByCellName.merge(cell.name, 1, Integer::sum);
            });
            if (attackPlusDefenceByCellName.size() > 1) {
                AtomicReference<String> nameOfStrongerCell = new AtomicReference<>("");
                final Integer[] maxStrongOfCell = {-1};
                attackPlusDefenceByCellName.forEach((k, v) -> {
                    if (maxStrongOfCell[0] < v) {
                        maxStrongOfCell[0] = v;
                        nameOfStrongerCell.set(k);
                    }
                });
                AtomicReference<Integer> energySumOfLosers = new AtomicReference<>(0);
                energyByCellName.forEach((k, v) -> {
                    if (!Objects.equals(k, nameOfStrongerCell.get())) {
                        energySumOfLosers.updateAndGet(v1 -> v1 + v);
                    }
                });
                Integer cellIncrementAfterBattle = energySumOfLosers.get() / cellCountByCellName.get(nameOfStrongerCell.get());
                Integer cellIncrementByFreeFoodAfterBattle = this.freeFood / cellCountByCellName.get(nameOfStrongerCell.get());
                for (Object item : this.items) {
                    Cell cell = (Cell) item;
                    if (!Objects.equals(cell.name, nameOfStrongerCell.get())) {
                        this.removeObjectFromSquareItems(cell);
                        cells.remove(cell);
                    } else {
                        cell.energy += cellIncrementAfterBattle + cellIncrementByFreeFoodAfterBattle;
                    }
                }
                this.freeFood = 0;
            } else {
                AtomicReference<Integer> cellIncrementByFreeFoodAfterBattle = new AtomicReference<>(0);
                cellCountByCellName.forEach((k, v) -> {
                    cellIncrementByFreeFoodAfterBattle.set(this.freeFood / v);
                });
                for (Object item : this.items) {
                    Cell cell = (Cell) item;
                    cell.energy += cellIncrementByFreeFoodAfterBattle.get();
                }
                this.freeFood = 0;
            }
        }
        calculateColor(true);
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
