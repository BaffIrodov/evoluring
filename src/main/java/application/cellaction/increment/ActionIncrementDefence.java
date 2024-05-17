package application.cellaction.increment;

import application.cell.Cell;

public class ActionIncrementDefence extends ActionIncrementCellProperty {
    private final int maxCellDefence = 10;

    @Override
    public void increment(Cell cell, int incrementSize) {
        cell.defence += incrementSize;
        if (cell.defence > maxCellDefence) {
            cell.defence = maxCellDefence;
        }
    }
}
