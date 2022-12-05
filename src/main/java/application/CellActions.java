package application;

import application.settingsDtos.CostSettings;

public class CellActions {
    BoardSettings boardSettings = new BoardSettings();
    CostSettings costSettings = boardSettings.getCostSetting();
    public enum CellActionsNames {
        DO_NOTHING,
        MOVE_LEFT,
        MOVE_RIGHT,
        MOVE_UP,
        MOVE_DOWN,
        WATCH,
        REPLICATE,
        EAT,
        EAT_CLOSE_FOOD,
        DNACHANGE,
        ATTACK,
        DEFENCE,
        GENERATE_CHILD
    }

    public Cell onGenerateChild(Cell cell) {
        return cell.generateChild(this.boardSettings.getSquareSize());
    }

    public void onDoNothing(Cell cell){
        if(costSettings.isConsiderActiveCost) {
            cell.energy -= costSettings.doNothingActiveCost;
        }
    }

    public void onMoveLeft(Cell cell){
        cell.coordinates.x = cell.coordinates.x - boardSettings.getSquareSize();
        if(costSettings.isConsiderActiveCost) {
            cell.energy -= costSettings.moveLeftActive;
        }
        teleportCell(cell);
    }

    public void onMoveRight(Cell cell) {
        cell.coordinates.x += boardSettings.getSquareSize();
        if(costSettings.isConsiderActiveCost) {
            cell.energy -= costSettings.moveRightActive;
        }
        teleportCell(cell);
    }

    public void onMoveUp(Cell cell){
        cell.coordinates.y -= boardSettings.getSquareSize();
        if(costSettings.isConsiderActiveCost) {
            cell.energy -= costSettings.moveUpActive;
        }
        teleportCell(cell);
    }

    public void onMoveDown(Cell cell){
        cell.coordinates.y += boardSettings.getSquareSize();
        if(costSettings.isConsiderActiveCost) {
            cell.energy -= costSettings.moveDownActive;
        }
        teleportCell(cell);
    }

    public void onEatCloseFood(Cell cell, Square square){
        cell.energy += square.closeFood;
        if(costSettings.isConsiderActiveCost) {
            cell.energy -= costSettings.eatCloseFoodActive;
        }
        square.closeFood = 0;
        square.calculateColor(true);
    }

    public void teleportCell(Cell cell){
        if(cell.coordinates.x < 0){
            cell.coordinates.x = (boardSettings.getWidth()-1)*boardSettings.getSquareSize();
        }
        if(cell.coordinates.x >= boardSettings.getWidth()*boardSettings.getSquareSize()){
            cell.coordinates.x = 0;
        }
        if(cell.coordinates.y < 0){
            cell.coordinates.y = (boardSettings.getHeight()-1)*boardSettings.getSquareSize();
        }
        if(cell.coordinates.y >= boardSettings.getHeight()*boardSettings.getSquareSize()){
            cell.coordinates.y = 0;
        }
    }
}
