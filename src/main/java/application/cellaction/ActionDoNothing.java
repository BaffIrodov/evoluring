package application.cellaction;

import application.Square;
import application.cell.Cell;

public class ActionDoNothing extends CellAction {

    {
        this.activeAction = false;
    }

    @Override
    public void execute(Cell cell, Square currentSquare) {

    }
}
