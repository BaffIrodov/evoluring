package application.cell;

import application.Square;
import application.cellaction.CellActionContext;
import javafx.scene.canvas.GraphicsContext;

import static application.Main.*;

public class CellActivities {

    public void cellTick(Cell cell, GraphicsContext graphicsContext, CellActionContext cellActionContext) {
        boolean isDeath = cell.checkIfDeath();
        if (!isDeath) {
            Square currentSquare = cell.getCurrentSquare();
            if (cellGenerationSettings.generateByFood) {
                if (cell.energy > cellGenerationSettings.costOfGenerationByFood) {
                    cellsToAdding.add(cell.generateChild(boardSettings.getSquareSize()));
                }
            }
            CellActions.CellActionsNames nextAction = cell.getNextAction();
            switch (nextAction) {
                case GENERATE_CHILD -> {
                    if (cellGenerationSettings.generateByGene && cell.energy > cellGenerationSettings.costOfGenerationByGene) {
                        Cell newCell = cellActions.onGenerateChild(cell);
                        cellsToAdding.add(newCell);
                        newCell.getCurrentSquare().addObjectToSquareItems(newCell);
                    }
                }
                case DO_NOTHING -> {
                    cellActions.onDoNothing(cell);
                }
                case MOVE_LEFT -> {
                    currentSquare.removeObjectFromSquareItems(cell);
                    cellActions.onMoveLeft(cell);
                    currentSquare = cell.getCurrentSquare();
                    currentSquare.addObjectToSquareItems(cell);
                }
                case MOVE_RIGHT -> {
                    currentSquare.removeObjectFromSquareItems(cell);
                    cellActions.onMoveRight(cell);
                    currentSquare = cell.getCurrentSquare();
                    currentSquare.addObjectToSquareItems(cell);
                }
                case MOVE_DOWN -> {
                    currentSquare.removeObjectFromSquareItems(cell);
                    cellActions.onMoveDown(cell);
                    currentSquare = cell.getCurrentSquare();
                    currentSquare.addObjectToSquareItems(cell);
                }
                case MOVE_UP -> {
                    currentSquare.removeObjectFromSquareItems(cell);
                    cellActions.onMoveUp(cell);
                    currentSquare = cell.getCurrentSquare();
                    currentSquare.addObjectToSquareItems(cell);
                }
                case EAT_CLOSE_FOOD -> {
                    currentSquare = cell.getCurrentSquare();
                    cellActions.onEatCloseFood(cell, currentSquare);
                }
            }
            graphicsContext.setFill(cell.color);
            graphicsContext.fillRect(cell.coordinates.x, cell.coordinates.y, boardSettings.getSquareSize() /*- 1*/, boardSettings.getSquareSize() /*- 1*/);
            if (energyCostSettings.isConsiderPassiveCost) {
                cell.energy -= cell.energyCost;
            }
            cell.energy--;
        } else {
            cellsToDelete.add(cell);
        }
    }

}
