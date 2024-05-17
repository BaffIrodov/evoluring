package application.cellaction;

import application.Square;
import application.cell.Cell;

import static application.Main.cellActions;

public class ActionMoveLeft extends CellAction {

    {
        this.activeAction = true;
    }

    @Override
    public void execute(Cell cell, Square currentSquare) {
        currentSquare.removeObjectFromSquareItems(cell);
        cellActions.onMoveLeft(cell);
        currentSquare = cell.getCurrentSquare();
        currentSquare.addObjectToSquareItems(cell);
    }
}
