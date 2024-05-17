package application.cellaction;

import application.Square;
import application.cell.Cell;
import application.cellaction.decrement.ActionDecrementAttack;
import application.cellaction.decrement.ActionDecrementDefence;
import application.cellaction.increment.ActionIncrementAttack;
import application.cellaction.increment.ActionIncrementDefence;

import java.util.List;
import static application.Main.rand;

public class CellActionContext {

    static List<CellAction> cellActionList = List.of(
            new ActionDoNothing(),
            new ActionEatFood(),
            new ActionMoveDown(),
            new ActionMoveLeft(),
            new ActionMoveRight(),
            new ActionMoveUp(),
            new ActionReplicate(),
            new ActionIncrementAttack(),
            new ActionDecrementAttack(),
            new ActionIncrementDefence(),
            new ActionDecrementDefence()
    );

    public static CellAction getRandomCellAction() {
        return cellActionList.get(rand.nextInt(0, cellActionList.size()));
    }

//    public void execute(CellAction cellAction, Cell cell, Square currentSquare) {
//        cellAction.execute(cell, currentSquare);
//    }

}
