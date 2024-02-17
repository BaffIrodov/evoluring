package application;

import java.util.*;

import application.board.BoardActivities;
import application.frontground.FrontGroundController;
import application.handlers.KeyboardEventHandler;
import application.keyController.Key;
import application.keyController.KeyTitles;
import application.renders.PauseRender;
import application.settings.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Main extends Application {

    public static GameSettings gameSettings = new GameSettings();
    public static BoardSettings boardSettings = gameSettings.boardSettings;
    public static EnergyCostSettings energyCostSettings = gameSettings.energyCostSettings;
    public static FoodAddingSettings foodAddingSettings = gameSettings.foodAddingSettings;
    public static CellGenerationSettings cellGenerationSettings = gameSettings.cellGenerationSettings;
    public static RenderSettings renderSettings = gameSettings.renderSettings;
    public static ApplicationSettings applicationSettings = gameSettings.applicationSettings;
    FrontGroundController frontGroundController = new FrontGroundController();
    CellActions cellActions = new CellActions();
    KeyTitles keyTitles = new KeyTitles();
    PauseRender pauseRender = new PauseRender();
    public static List<Cell> cells = new ArrayList<>();
    public static List<Square> squares = new ArrayList<>();
    static Map<String, Integer> mapSquareCoordinatesToIndex = new HashMap<>();
    static Map<Map<Integer, Integer>, Integer> foodsMap = new HashMap<>();
    public static Map<String, CellActions.CellActionsNames> actionMap = new HashMap<>();
    static boolean gameOver = false;
    public static boolean gameStopped = false;
    public static Random rand = new Random();
    List<Cell> cellsToDelete = new ArrayList<>();
    List<Cell> cellsToAdding = new ArrayList<>();
    public static boolean isFoodAdding = false;
    public static boolean isOnlyCloseAdding = false;
    public static int currentTick = 0;
    public long startGameMills = System.currentTimeMillis();
    public static int realFrameCount = 1;
    KeyboardEventHandler keyboardEventHandler = new KeyboardEventHandler();
    BoardActivities boardActivities = new BoardActivities();

    private int getRandPos(int number) {
        return new Random(number).nextInt();
    }

    public void start(Stage primaryStage) {
        try {
            VBox root = new VBox();
            Canvas c = new Canvas(boardSettings.getWidth() * boardSettings.getSquareSize(), boardSettings.getHeight() * boardSettings.getSquareSize());
            GraphicsContext graphicsContext = c.getGraphicsContext2D();
            root.getChildren().add(c);

            new AnimationTimer() {
                long lastTick = 0;
                int index = 0;

                public void handle(long now) {
                    if (lastTick == 0) {
                        lastTick = now;
                        tick(graphicsContext);
                        return;
                    }

                    if (now - lastTick > 0) {
                        lastTick = now;
                        tick(graphicsContext);
                    }
                }

            }.start();

            Scene scene = new Scene(root, boardSettings.getWidth() * boardSettings.getSquareSize(), boardSettings.getHeight() * boardSettings.getSquareSize());

            // control
            scene.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                double realX = mouseEvent.getX() / boardSettings.getSquareSize();
                double realY = mouseEvent.getY() / boardSettings.getSquareSize();
                int index = mapSquareCoordinatesToIndex.get((int) realX + "|" + (int) realY);
                Square currentSquare = squares.get(index);
                // нажатие лкм
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    if (currentSquare.frontGroundColor != Color.BLACK) {
                        currentSquare.frontGroundColor = Color.BLACK;
                    } else {
                        currentSquare.frontGroundColor = null;
                    }
                } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    int explosionRange = 200;
                    for (Square square : squares) {
                        if (square.coordinates.x > (mouseEvent.getX() - explosionRange)
                                && square.coordinates.x < (mouseEvent.getX() + explosionRange)
                                && square.coordinates.y > (mouseEvent.getY() - explosionRange)
                                && square.coordinates.y < (mouseEvent.getY() + explosionRange)) {
                            square.items = new ArrayList<>();
                            square.freeFood = 0;
                            square.closeFood = 0;
                            square.calculateColor(true, renderSettings);
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
                } else if (mouseEvent.getButton() == MouseButton.MIDDLE) {

                }
            });
            keyboardEventHandler.handle(applicationSettings, boardSettings, renderSettings, energyCostSettings,
                    scene, keyTitles, graphicsContext, pauseRender);
            // initialization playing field
            squareAdding();
            cellActions.actionMapGenerate();


//            cellAdding();


            //food adding
            //If you do not want to use css style, you can just delete the next line.
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("EVOLURING");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // tick
    public void tick(GraphicsContext graphicsContext) {
        if (currentTick == 1100) {
            System.out.println("------- 1100 frames done ------");
        }
        if (currentTick < 50) {
            isFoodAdding = true;
            boardActivities.cellAdding();
        }
        for (int i = 0; i < realFrameCount; i++) {
            currentTick++;
            if (currentTick % 100 == 0) {
                // вернуть, как было
//                System.out.println("time: " + (System.currentTimeMillis() - startGameMills)
//                        + " size: " + cells.size() + " fps: " + (100000 / (System.currentTimeMillis() - startGameMills)));
                System.out.println("time: " + (System.currentTimeMillis() - startGameMills)
                        + " size: " + cells.size());
                //startGameMills = System.currentTimeMillis(); вернуть как было
            }

            if (!gameStopped) {
                Long now = System.currentTimeMillis();

                if (isFoodAdding) {
                    boardActivities.freeFoodAdding();
                    boardActivities.closeFoodAdding();
                }

                if (isOnlyCloseAdding) {
                    boardActivities.closeFoodAdding();
                }

                for (Square square : squares) { //отрисовка квадратиков
                    graphicsContext.setFill(square.color);
                    graphicsContext.fillRect(square.coordinates.x, square.coordinates.y, boardSettings.getSquareSize(), boardSettings.getSquareSize());
                }
                for (Cell cell : cells) {
                    boolean isDeath = cell.checkIfDeath();
                    if (!isDeath) {
                        Square currentSquare = getCurrentSquare(cell);
                        if (cellGenerationSettings.generateByFood) {
                            if (cell.energy > cellGenerationSettings.costOfGenerationByFood) {
                                cellsToAdding.add(cell.generateChild(boardSettings.getSquareSize()));
                            }
                        }
                        CellActions.CellActionsNames nextAction = cell.getNextAction();
                        switch (nextAction) {
                            case GENERATE_CHILD -> {
                                if (cellGenerationSettings.generateByGene && cell.energy > cellGenerationSettings.costOfGenerationByGene) {
                                    Cell newCell = cellActions.onGenerateChild(cell);
                                    cellsToAdding.add(newCell);
                                    getCurrentSquare(newCell).addObjectToSquareItems(newCell);
                                }
                            }
                            case DO_NOTHING -> {
                                cellActions.onDoNothing(cell);
                            }
                            case MOVE_LEFT -> {
                                currentSquare.removeObjectFromSquareItems(cell);
                                cellActions.onMoveLeft(cell);
                                currentSquare = getCurrentSquare(cell);
                                currentSquare.addObjectToSquareItems(cell);
                            }
                            case MOVE_RIGHT -> {
                                currentSquare.removeObjectFromSquareItems(cell);
                                cellActions.onMoveRight(cell);
                                currentSquare = getCurrentSquare(cell);
                                currentSquare.addObjectToSquareItems(cell);
                            }
                            case MOVE_DOWN -> {
                                currentSquare.removeObjectFromSquareItems(cell);
                                cellActions.onMoveDown(cell);
                                currentSquare = getCurrentSquare(cell);
                                currentSquare.addObjectToSquareItems(cell);
                            }
                            case MOVE_UP -> {
                                currentSquare.removeObjectFromSquareItems(cell);
                                cellActions.onMoveUp(cell);
                                currentSquare = getCurrentSquare(cell);
                                currentSquare.addObjectToSquareItems(cell);
                            }
                            case EAT_CLOSE_FOOD -> {
                                currentSquare = getCurrentSquare(cell);
                                cellActions.onEatCloseFood(cell, currentSquare);
                            }
                        }
                        graphicsContext.setFill(cell.color);
                        graphicsContext.fillRect(cell.coordinates.x, cell.coordinates.y, boardSettings.getSquareSize() /*- 1*/, boardSettings.getSquareSize() /*- 1*/);
                        if (energyCostSettings.isConsiderPassiveCost) {
                            cell.energy -= cell.energyCost;
                        }
                        cell.energy--;
                    } else {
                        cellsToDelete.add(cell);
                    }
                }
                for (Square square : squares) { //отрисовка квадратиков
                    if (square.frontGroundColor != null) {
                        graphicsContext.setFill(square.frontGroundColor);
                        graphicsContext.fillRect(square.coordinates.x, square.coordinates.y, boardSettings.getSquareSize(), boardSettings.getSquareSize());
                    }
                }
//                frontGroundController.renderQRCode(cells);
                if (!cellsToDelete.isEmpty()) {
                    cellsToDelete.forEach(e -> {
                        getCurrentSquare(e).removeObjectFromSquareItems(e);
                        cells.remove(e);
                    });
                    cellsToDelete = new ArrayList<>();
                }
                if (!cellsToAdding.isEmpty()) {
                    cellsToAdding.forEach(e -> {
                        cells.add(e);
                        getCurrentSquare(e).addObjectToSquareItems(e);
                    });
                    cellsToAdding = new ArrayList<>();
                }
                for (Square square : squares) {
                    if (!square.items.isEmpty()) {
                        square.calculateEating(cells, renderSettings);
                    }
                }
            }
        }
    }

    public void squareAdding() { //начальная инициация всех клеток игрового поля
        int index = 0;
        for (int i = 0; i < boardSettings.getWidth(); i++) {
            for (int j = 0; j < boardSettings.getHeight(); j++) {
                squares.add(new Square(new Coordinates(i * boardSettings.getSquareSize(), j * boardSettings.getSquareSize()), 0, 0, new ArrayList<>()));
                mapSquareCoordinatesToIndex.put((i + "|" + j), index);
                index++;
            }
        }
    }

    public Square getCurrentSquare(Cell cell) {
        int realXCoordinate = cell.coordinates.x / this.boardSettings.getSquareSize();
        int realYCoordinate = cell.coordinates.y / this.boardSettings.getSquareSize();
        if (mapSquareCoordinatesToIndex.get(realXCoordinate
                + "|" + realYCoordinate) == null) {
            cellActions.teleportCell(cell);
            realXCoordinate = cell.coordinates.x / this.boardSettings.getSquareSize();
            realYCoordinate = cell.coordinates.y / this.boardSettings.getSquareSize();
        }
        return squares.get(mapSquareCoordinatesToIndex.get(realXCoordinate
                + "|" + realYCoordinate));
    }

    public static void main(String[] args) {
        launch(args);
    }
}