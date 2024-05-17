package application.cellaction;

import application.Square;
import application.cell.Cell;

public abstract class CellAction {

    public boolean activeAction;

    public abstract void execute(Cell cell, Square currentSquare);

}
