package application.cellaction;

import application.Square;
import application.cell.Cell;

import static application.Main.energyCostSettings;

public class ActionDoNothing extends CellAction {

    {
        this.activeAction = false;
    }

    @Override
    public void execute(Cell cell, Square currentSquare) {
        if (energyCostSettings.isConsiderActiveCost) {
            cell.energy -= energyCostSettings.doNothingActiveCost;
        }
    }
}
