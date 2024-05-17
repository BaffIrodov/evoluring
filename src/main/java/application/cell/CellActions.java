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

    public Cell onReplicate(Cell cell) {
        return cell.replicate(boardSettings.getSquareSize());
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

    public void generateActionMap() {
        actionMap.put("o", CellActionsNames.DO_NOTHING);
        actionMap.put("a", CellActionsNames.MOVE_LEFT);
        actionMap.put("b", CellActionsNames.MOVE_UP);
        actionMap.put("c", CellActionsNames.MOVE_RIGHT);
        actionMap.put("d", CellActionsNames.MOVE_DOWN);
        actionMap.put("e", CellActionsNames.EAT_CLOSE_FOOD);
        actionMap.put("f", CellActionsNames.ATTACK);
        actionMap.put("g", CellActionsNames.DEFENCE);
        actionMap.put("h", CellActionsNames.GENERATE_CHILD);
        actionMap.put("t", CellActionsNames.THINK);
    }
}
