package application.board;

import application.*;
import application.settings.*;
import javafx.scene.paint.Color;

import static application.Main.*;

public class BoardActivities {

    public void freeFoodAdding() {
        for (int i = 0; i < gameSettings.foodAddingSettings.freeFoodAddingRate; i++) {
            int randomSquareIndex = rand.nextInt(squares.size() - 1);
            Square randomSquare = squares.get(randomSquareIndex);
            randomSquare.freeFoodOnLastFrame = randomSquare.freeFood;
            randomSquare.freeFood += gameSettings.foodAddingSettings.freeEatAddingByEveryTick;
            randomSquare.calculateColor(false, gameSettings.renderSettings);
        }
    }

    public void closeFoodAdding() {
        for (int i = 0; i < gameSettings.foodAddingSettings.closeFoodAddingRate; i++) {
            int randomSquareIndex = rand.nextInt(squares.size() - 1);
            Square randomSquare = squares.get(randomSquareIndex);
            randomSquare.closeFoodOnLastFrame = randomSquare.closeFood;
            randomSquare.closeFood += gameSettings.foodAddingSettings.closeEatAddingByEveryTick;
            randomSquare.calculateColor(false, gameSettings.renderSettings);
        }
    }

    public void cellAdding() {
        cells.add(new Cell("red", 1, 500, new Coordinates(150, 150), new DNA("ah", 0), Color.RED, boardSettings, energyCostSettings));
        cells.add(new Cell("black", 1, 500, new Coordinates(450, 150), new DNA("bh", 0), Color.BLACK, boardSettings, energyCostSettings));
        cells.add(new Cell("green", 1, 500, new Coordinates(150, 450), new DNA("ch", 0), Color.GREEN, boardSettings, energyCostSettings));
        cells.add(new Cell("blue", 1, 500, new Coordinates(450, 450), new DNA("dh", 0), Color.BLUE, boardSettings, energyCostSettings));
    }

    public void addFreeFoodInDistrict() {
        for (Square square : squares) {
            if (square.coordinates.x >= 100 && square.coordinates.x <= 200 && square.coordinates.y >= 100 && square.coordinates.y <= 200) {
                square.freeFood += 100;
                square.calculateColor(true, renderSettings);
            }
            if (square.coordinates.x >= 200 && square.coordinates.x <= 400 && square.coordinates.y >= 140 && square.coordinates.y <= 160) {
                square.freeFood += 100;
                square.calculateColor(true, renderSettings);
            }
            if (square.coordinates.x >= 400 && square.coordinates.x <= 500 && square.coordinates.y >= 100 && square.coordinates.y <= 200) {
                square.freeFood += 100;
                square.calculateColor(true, renderSettings);
            }
            if (square.coordinates.x >= 200 && square.coordinates.x <= 400 && square.coordinates.y >= 440 && square.coordinates.y <= 460) {
                square.freeFood += 100;
                square.calculateColor(true, renderSettings);
            }
            if (square.coordinates.x >= 100 && square.coordinates.x <= 200 && square.coordinates.y >= 400 && square.coordinates.y <= 500) {
                square.freeFood += 100;
                square.calculateColor(true, renderSettings);
            }
            if (square.coordinates.x >= 440 && square.coordinates.x <= 460 && square.coordinates.y >= 200 && square.coordinates.y <= 400) {
                square.freeFood += 100;
                square.calculateColor(true, renderSettings);
            }
            if (square.coordinates.x >= 400 && square.coordinates.x <= 500 && square.coordinates.y >= 400 && square.coordinates.y <= 500) {
                square.freeFood += 100;
                square.calculateColor(true, renderSettings);
            }
            if (square.coordinates.x >= 140 && square.coordinates.x <= 160 && square.coordinates.y >= 200 && square.coordinates.y <= 400) {
                square.freeFood += 100;
                square.calculateColor(true, renderSettings);
            }
        }
    }

    public void addCloseFoodInDistrict() {
        for (Square square : squares) {
            if (square.coordinates.x >= 700 && square.coordinates.x <= 800) {
                square.closeFood += 100;
                square.calculateColor(true, renderSettings);
            }
        }
    }

}
