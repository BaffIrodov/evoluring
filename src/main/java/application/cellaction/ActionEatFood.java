package application.cellaction;

import application.Square;
import application.cell.Cell;

import static application.Main.cellActions;
import static application.Main.energyCostSettings;

public class ActionEatFood extends CellAction {

    {
        this.activeAction = true;
    }

    @Override
    public void execute(Cell cell, Square currentSquare) {
        currentSquare = cell.getCurrentSquare();
        cell.energy += currentSquare.closeFood;
        if (energyCostSettings.isConsiderActiveCost) {
            cell.energy -= energyCostSettings.eatCloseFoodActive;
        }
        currentSquare.closeFood = 0;
        currentSquare.calculateColor(true);
    }
}
