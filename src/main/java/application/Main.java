package application;

import java.util.*;

import application.frontground.FrontGroundController;
import application.keyController.Key;
import application.keyController.KeyTitles;
import application.renders.PauseRender;
import application.settings.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
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

    GameSettings gameSettings = new GameSettings();
    BoardSettings boardSettings = gameSettings.getBoardSettings();
    EnergyCostSettings energyCostSettings = gameSettings.getCostSetting();
    FoodAddingSettings foodAddingSettings = gameSettings.getFoodAddingSettings();
    CellGenerationSettings cellGenerationSettings = gameSettings.getCellGenerationSettings();
    RenderSettings renderSettings = gameSettings.getRenderSettings();
    FrontGroundController frontGroundController = new FrontGroundController();
    CellActions cellActions = new CellActions();
    KeyTitles keyTitles = new KeyTitles();
    PauseRender pauseRender = new PauseRender();
    static List<Cell> cells = new ArrayList<>();
    static List<Square> squares = new ArrayList<>();
    static Map<String, Integer> mapSquareCoordinatesToIndex = new HashMap<>();
    static Map<Map<Integer, Integer>, Integer> foodsMap = new HashMap<>();
    static boolean gameOver = false;
    static boolean gameStoped = false;
    static Random rand = new Random();
    List<Cell> cellsToDelete = new ArrayList<>();
    List<Cell> cellsToAdding = new ArrayList<>();
    public boolean isFoodAdding = false;
    public boolean isOnlyCloseAdding = false;
    public static int currentTick = 0;
    public long startGameMills = System.currentTimeMillis();
    private int realFrameCount = 1;

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
                Square square = squares.get(index);
                if (square.frontGroundColor != Color.BLACK) {
                    square.frontGroundColor = Color.BLACK;
                } else {
                    square.frontGroundColor = null;
                }
                int i = 0;
            });
            scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
                if (keyTitles.mapKeyByKeyCodes.containsKey(key.getCode())) {
                    if (key.getCode() == KeyCode.SPACE) {
                        gameStoped = !gameStoped;
                        if (gameStoped) {
                            if (renderSettings.pauseKeySettingsEnable || renderSettings.pauseGameConditionEnable) {
                                if (renderSettings.pauseBackgroundEnable) {
                                    graphicsContext.setFill(Color.color(0.9, 0.9, 0.9, 0.6));
                                    graphicsContext.fillRect(0, 0, 600, 500);
                                }
                                graphicsContext.setFill(Color.BLACK);
                                graphicsContext.setFont(new Font("", 16));
                                String splitter = "\n----------------\n";
                                String keyDescription = renderSettings.pauseKeySettingsEnable ?
                                        pauseRender.getKeyDescriptions(keyTitles) : "";
                                String gameCondition = renderSettings.pauseGameConditionEnable ?
                                        pauseRender.getGameCondition(cells) : "";
                                graphicsContext.fillText(keyDescription + splitter + gameCondition,
                                        30, 30);
                            }
                        }
                    }
                    if (key.getCode() == KeyCode.UP) {
                        cellAdding();
                    }
                    if (key.getCode() == KeyCode.LEFT) {
                        isFoodAdding = !isFoodAdding;
                    }
                    if (key.getCode() == KeyCode.DOWN) {
                        isOnlyCloseAdding = !isOnlyCloseAdding;
                    }
                    if (key.getCode() == KeyCode.RIGHT) {
                        testFreeFoodInDistrict();
                    }
                    if (key.getCode() == KeyCode.DIGIT1) {
                        this.realFrameCount = 1;
                    }
                    if (key.getCode() == KeyCode.DIGIT2) {
                        this.realFrameCount = 2;
                    }
                    if (key.getCode() == KeyCode.DIGIT3) {
                        this.realFrameCount = 10;
                    }
                }
            });
            // initialization playing field
            squareAdding();


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
        for (int i = 0; i < realFrameCount; i++) {
            currentTick++;
//        if (currentTick % 100 == 0) {
////            testFreeFoodInDistrict();
////            testCloseFoodInDistrict();
//        }
            if (currentTick % 100 == 0) {
                System.out.println("time: " + (System.currentTimeMillis() - startGameMills)
                        + " size: " + cells.size() + " fps: " + (100000 / (System.currentTimeMillis() - startGameMills)));
                startGameMills = System.currentTimeMillis();
            }

            if (!gameStoped) {
                Long now = System.currentTimeMillis();

                if (isFoodAdding) {
                    freeFoodAdding();
                    closeFoodAdding();
                }

                if (isOnlyCloseAdding) {
                    closeFoodAdding();
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
                                if (cellGenerationSettings.generateByGene && cell.energy > cellGenerationSettings.costOfGenerationByFood) {
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
                        square.calculateEating(cells, gameSettings.getRenderSettings());
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

    public void cellAdding() {
        cells.add(new Cell("red", 1, 500, new Coordinates(150, 150), new DNA("ach", 0), Color.RED));
        cells.add(new Cell("black", 1, 500, new Coordinates(450, 150), new DNA("bdh", 0), Color.BLACK));
        cells.add(new Cell("green", 1, 500, new Coordinates(150, 450), new DNA("cah", 0), Color.GREEN));
        cells.add(new Cell("blue", 1, 500, new Coordinates(450, 450), new DNA("dbh", 0), Color.BLUE));
    }

    public void testFreeFoodInDistrict() {
        for (Square square : squares) {
            if (square.coordinates.x >= 100 && square.coordinates.x <= 200 && square.coordinates.y >= 100 && square.coordinates.y <= 200) {
                square.freeFood += 100;
                square.calculateColor(true, gameSettings.getRenderSettings());
            }
            if (square.coordinates.x >= 200 && square.coordinates.x <= 400 && square.coordinates.y >= 140 && square.coordinates.y <= 160) {
                square.freeFood += 100;
                square.calculateColor(true, gameSettings.getRenderSettings());
            }
            if (square.coordinates.x >= 400 && square.coordinates.x <= 500 && square.coordinates.y >= 100 && square.coordinates.y <= 200) {
                square.freeFood += 100;
                square.calculateColor(true, gameSettings.getRenderSettings());
            }
            if (square.coordinates.x >= 200 && square.coordinates.x <= 400 && square.coordinates.y >= 440 && square.coordinates.y <= 460) {
                square.freeFood += 100;
                square.calculateColor(true, gameSettings.getRenderSettings());
            }
            if (square.coordinates.x >= 100 && square.coordinates.x <= 200 && square.coordinates.y >= 400 && square.coordinates.y <= 500) {
                square.freeFood += 100;
                square.calculateColor(true, gameSettings.getRenderSettings());
            }
            if (square.coordinates.x >= 440 && square.coordinates.x <= 460 && square.coordinates.y >= 200 && square.coordinates.y <= 400) {
                square.freeFood += 100;
                square.calculateColor(true, gameSettings.getRenderSettings());
            }
            if (square.coordinates.x >= 400 && square.coordinates.x <= 500 && square.coordinates.y >= 400 && square.coordinates.y <= 500) {
                square.freeFood += 100;
                square.calculateColor(true, gameSettings.getRenderSettings());
            }
            if (square.coordinates.x >= 140 && square.coordinates.x <= 160 && square.coordinates.y >= 200 && square.coordinates.y <= 400) {
                square.freeFood += 100;
                square.calculateColor(true, gameSettings.getRenderSettings());
            }
        }

    }

    public void testCloseFoodInDistrict() {
        for (Square square : squares) {
            if (square.coordinates.x >= 700 && square.coordinates.x <= 800) {
                square.closeFood += 100;
                square.calculateColor(true, gameSettings.getRenderSettings());
            }
        }

    }

    public void freeFoodAdding() {
        for (int i = 0; i < foodAddingSettings.freeFoodAddingRate; i++) {
            int randomSquareIndex = rand.nextInt(squares.size() - 1);
            Square randomSquare = squares.get(randomSquareIndex);
            randomSquare.freeFoodOnLastFrame = randomSquare.freeFood;
            randomSquare.freeFood += foodAddingSettings.freeEatAddingByEveryTick;
            randomSquare.calculateColor(false, gameSettings.getRenderSettings());
        }
    }

    public void closeFoodAdding() {
        for (int i = 0; i < foodAddingSettings.closeFoodAddingRate; i++) {
            int randomSquareIndex = rand.nextInt(squares.size() - 1);
            Square randomSquare = squares.get(randomSquareIndex);
            randomSquare.closeFoodOnLastFrame = randomSquare.closeFood;
            randomSquare.closeFood += foodAddingSettings.closeEatAddingByEveryTick;
            randomSquare.calculateColor(false, gameSettings.getRenderSettings());
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