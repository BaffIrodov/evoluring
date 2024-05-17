package application.cellaction.decrement;

import application.cell.Cell;

public class ActionDecrementDefence extends ActionDecrementCellProperty {
    private final int minCellDefence = 0;

    @Override
    public void decrement(Cell cell, int decrementSize) {
        cell.defence -= decrementSize;
        if (cell.defence < minCellDefence) {
            cell.defence = minCellDefence;
        }
    }
}
