package application.cellaction.decrement;

import application.cell.Cell;
import application.cellaction.increment.ActionIncrementCellProperty;

public class ActionDecrementAttack extends ActionDecrementCellProperty {
    private final int minCellAttack = 0;

    @Override
    public void decrement(Cell cell, int decrementSize) {
        cell.attack -= decrementSize;
        if (cell.attack < minCellAttack) {
            cell.attack = minCellAttack;
        }
    }
}
