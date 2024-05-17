package application.cellaction;

import application.Square;
import application.cell.Cell;

public class CellActionContext {

    public void execute(CellAction cellAction, Cell cell, Square currentSquare) {
        cellAction.execute(cell, currentSquare);
    }

}
