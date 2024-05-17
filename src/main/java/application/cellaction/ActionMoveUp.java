package application.cellaction;

import application.Square;
import application.cell.Cell;

import static application.Main.cellActions;

public class ActionMoveUp extends CellAction {

    {
        this.activeAction = true;
    }

    @Override
    public void execute(Cell cell, Square currentSquare) {
        currentSquare.removeObjectFromSquareItems(cell);
        cellActions.onMoveUp(cell);
        currentSquare = cell.getCurrentSquare();
        currentSquare.addObjectToSquareItems(cell);
    }
}
