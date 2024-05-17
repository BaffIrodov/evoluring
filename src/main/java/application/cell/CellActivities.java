package application.cell;

import application.Square;
import application.cellaction.CellAction;
import application.cellaction.CellActionContext;
import javafx.scene.canvas.GraphicsContext;

import static application.Main.*;

public class CellActivities {

    public void cellTick(Cell cell, GraphicsContext graphicsContext, CellActionContext cellActionContext) {
        boolean isDeath = cell.checkIfDeath();
        if (!isDeath) {
            Square currentSquare = cell.getCurrentSquare();
            if (cellReplicationSettings.replicateByFood) {
                if (cell.energy > cellReplicationSettings.costOfReplicationByFood) {
                    cellsToAdding.add(cell.replicate(boardSettings.getSquareSize()));
                }
            }
            CellAction newCellAction = cell.dna.getNextAction();
            newCellAction.execute(cell, currentSquare);
            graphicsContext.setFill(cell.color);
            graphicsContext.fillRect(cell.coordinates.x, cell.coordinates.y, boardSettings.getSquareSize() /*- 1*/, boardSettings.getSquareSize() /*- 1*/);
            if (energyCostSettings.isConsiderPassiveCost) {
                cell.energy -= cell.energyCost;
            }
            cell.energy --;
        } else {
            cellsToDelete.add(cell);
        }
    }

}
