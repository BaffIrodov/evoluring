package application.cellaction.increment;

import application.Square;
import application.cell.Cell;
import application.cellaction.CellAction;
import javafx.scene.effect.Reflection;

public abstract class ActionIncrementCellProperty extends CellAction {

    private int attack;

    {
        this.activeAction = false;
    }

    ActionIncrementCellProperty() {

    }

    @Override
    public void execute(Cell cell, Square currentSquare) {
        increment(cell, 1);
    }

    public abstract void increment(Cell cell, int incrementSize);
}
