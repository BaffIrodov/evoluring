package application;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentState {

    Map<RelativeCoordinates, SquareEnvironmentState> SESbyRelativeCoordinates = new HashMap<>();

    public enum SquareEnvironmentState {
        // основные состояния
        SQUARE_EMPTY,
        SQUARE_WITH_ENEMY,
        SQUARE_WITH_FRIEND,
        SQUARE_WITH_BATTLE,
        // состояния по типу еды
        SQUARE_WITH_FREE_FOOD,
        SQUARE_WITH_CLOSE_FOOD
    }
}
