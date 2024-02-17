package application.handlers;

import application.Cell;
import application.Square;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static application.Main.*;

public class MouseEventHandler {

    public void handle(Scene scene) {
        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            double realX = mouseEvent.getX() / boardSettings.getSquareSize();
            double realY = mouseEvent.getY() / boardSettings.getSquareSize();
            int index = mapSquareCoordinatesToIndex.get((int) realX + "|" + (int) realY);
            Square currentSquare = squares.get(index);
            // нажатие лкм
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                paintFrontGround(currentSquare, 10, Color.BLACK);
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                makeExplosion(mouseEvent, 200);
            } else if (mouseEvent.getButton() == MouseButton.MIDDLE) {

            }
        });
    }

    private void paintFrontGround(Square currentSquare, int paintRange, Color color) {
        if (paintRange == 1) {
            if (currentSquare.frontGroundColor != color) {
                currentSquare.frontGroundColor = color;
            } else {
                currentSquare.frontGroundColor = null;
            }
        } else if (paintRange < 1) {
            return;
        } else {
            for (Square square : squares) {
                if (square.coordinates.x > (currentSquare.coordinates.x - paintRange * boardSettings.getSquareSize())
                        && square.coordinates.x < (currentSquare.coordinates.x + paintRange * boardSettings.getSquareSize())
                        && square.coordinates.y > (currentSquare.coordinates.y - paintRange * boardSettings.getSquareSize())
                        && square.coordinates.y < (currentSquare.coordinates.y + paintRange * boardSettings.getSquareSize())) {
                    if (square.frontGroundColor != color) {
                        square.frontGroundColor = color;
                    } else {
                        square.frontGroundColor = null;
                    }
                }
            }
        }
    }

    private void makeExplosion(MouseEvent mouseEvent, int explosionRange) {
        for (Square square : squares) {
            if (square.coordinates.x > (mouseEvent.getX() - explosionRange)
                    && square.coordinates.x < (mouseEvent.getX() + explosionRange)
                    && square.coordinates.y > (mouseEvent.getY() - explosionRange)
                    && square.coordinates.y < (mouseEvent.getY() + explosionRange)) {
                square.items = new ArrayList<>();
                square.freeFood = 0;
                square.closeFood = 0;
                square.calculateColor(true);
            }
        }
        List<Cell> cellsToDelete = new ArrayList<>();
        for (Cell cell : cells) {
            if (cell.coordinates.x > (mouseEvent.getX() - explosionRange)
                    && cell.coordinates.x < (mouseEvent.getX() + explosionRange)
                    && cell.coordinates.y > (mouseEvent.getY() - explosionRange)
                    && cell.coordinates.y < (mouseEvent.getY() + explosionRange)) {
                cellsToDelete.add(cell);
            }
        }
        cells.removeAll(cellsToDelete);
    }

}
