package application.cell;

import application.Square;

import static application.Main.*;

public class CellActions {
    public enum CellActionsNames {
        DO_NOTHING,
        MOVE_LEFT,
        MOVE_RIGHT,
        MOVE_UP,
        MOVE_DOWN,
        REPLICATE,
        EAT,
        EAT_CLOSE_FOOD,
        DNACHANGE,
        ATTACK,
        DEFENCE,
        GENERATE_CHILD,
        // ИИ задатки
        WATCH, //не ясно, надо ли
        THINK // принять решение, куда двигаться
    }

    public Cell onGenerateChild(Cell cell) {
        return cell.generateChild(boardSettings.getSquareSize());
    }

    public void onDoNothing(Cell cell) {
        if (energyCostSettings.isConsiderActiveCost) {
            cell.energy -= energyCostSettings.doNothingActiveCost;
        }
    }

    public void onMoveLeft(Cell cell) {
        cell.coordinates.x = cell.coordinates.x - boardSettings.getSquareSize();
        if (energyCostSettings.isConsiderActiveCost) {
            cell.energy -= energyCostSettings.moveLeftActive;
        }
        teleportCell(cell);
    }

    public void onMoveRight(Cell cell) {
        cell.coordinates.x += boardSettings.getSquareSize();
        if (energyCostSettings.isConsiderActiveCost) {
            cell.energy -= energyCostSettings.moveRightActive;
        }
        teleportCell(cell);
    }

    public void onMoveUp(Cell cell) {
        cell.coordinates.y -= boardSettings.getSquareSize();
        if (energyCostSettings.isConsiderActiveCost) {
            cell.energy -= energyCostSettings.moveUpActive;
        }
        teleportCell(cell);
    }

    public void onMoveDown(Cell cell) {
        cell.coordinates.y += boardSettings.getSquareSize();
        if (energyCostSettings.isConsiderActiveCost) {
            cell.energy -= energyCostSettings.moveDownActive;
        }
        teleportCell(cell);
    }

    public void onEatCloseFood(Cell cell, Square square) {
        cell.energy += square.closeFood;
        if (energyCostSettings.isConsiderActiveCost) {
            cell.energy -= energyCostSettings.eatCloseFoodActive;
        }
        square.closeFood = 0;
        square.calculateColor(true);
    }

    public void teleportCell(Cell cell) {
        if (cell.coordinates.x < 0) {
            cell.coordinates.x = (boardSettings.getWidth() - 1) * boardSettings.getSquareSize();
        }
        if (cell.coordinates.x >= boardSettings.getWidth() * boardSettings.getSquareSize()) {
            cell.coordinates.x = 0;
        }
        if (cell.coordinates.y < 0) {
            cell.coordinates.y = (boardSettings.getHeight() - 1) * boardSettings.getSquareSize();
        }
        if (cell.coordinates.y >= boardSettings.getHeight() * boardSettings.getSquareSize()) {
            cell.coordinates.y = 0;
        }
    }

    public void actionMapGenerate() {
        actionMap.put("o", CellActions.CellActionsNames.DO_NOTHING);
        actionMap.put("a", CellActions.CellActionsNames.MOVE_LEFT);
        actionMap.put("b", CellActions.CellActionsNames.MOVE_UP);
        actionMap.put("c", CellActions.CellActionsNames.MOVE_RIGHT);
        actionMap.put("d", CellActions.CellActionsNames.MOVE_DOWN);
        actionMap.put("e", CellActions.CellActionsNames.EAT_CLOSE_FOOD);
        actionMap.put("f", CellActions.CellActionsNames.ATTACK);
        actionMap.put("g", CellActions.CellActionsNames.DEFENCE);
        actionMap.put("h", CellActions.CellActionsNames.GENERATE_CHILD);
    }
}
