package application.cellaction;

import application.Square;
import application.cell.Cell;

import static application.Main.cellActions;

public class ActionMoveRight extends CellAction {

    {
        this.activeAction = true;
    }

    @Override
    public void execute(Cell cell, Square currentSquare) {
        currentSquare.removeObjectFromSquareItems(cell);
        cellActions.onMoveRight(cell);
        currentSquare = cell.getCurrentSquare();
        currentSquare.addObjectToSquareItems(cell);
    }
}
