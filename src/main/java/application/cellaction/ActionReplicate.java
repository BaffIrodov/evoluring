package application.cellaction;

import application.Square;
import application.cell.Cell;

import static application.Main.*;

public class ActionReplicate extends CellAction {

    {
        this.activeAction = false;
    }

    @Override
    public void execute(Cell cell, Square currentSquare) {
        //todo тут что-то не то
        if (cellReplicationSettings.replicateByGene && cell.energy > cellReplicationSettings.costOfReplicationByGene) {
            Cell newCell = cell.replicate(boardSettings.getSquareSize());
            cellsToAdding.add(newCell);
            newCell.getCurrentSquare().addObjectToSquareItems(newCell);
        }
    }
}
