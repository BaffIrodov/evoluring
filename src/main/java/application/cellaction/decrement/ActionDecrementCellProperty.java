package application.cellaction.decrement;

import application.Square;
import application.cell.Cell;
import application.cellaction.CellAction;

public abstract class ActionDecrementCellProperty extends CellAction {

    {
        this.activeAction = false;
    }

    @Override
    public void execute(Cell cell, Square currentSquare) {
        decrement(cell, 1);
    }

    public abstract void decrement(Cell cell, int decrementSize);
}
