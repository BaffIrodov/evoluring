package application.cellaction.increment;

import application.cell.Cell;

public class ActionIncrementAttack extends ActionIncrementCellProperty {
    private final int maxCellAttack = 10;

    @Override
    public void increment(Cell cell, int incrementSize) {
        cell.attack += incrementSize;
        if (cell.attack > maxCellAttack) {
            cell.attack = maxCellAttack;
        }
    }
}
