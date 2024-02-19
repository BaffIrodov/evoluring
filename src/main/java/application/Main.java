package application;

import java.util.*;

import application.board.BoardActivities;
import application.cell.Cell;
import application.cell.CellActions;
import application.cell.CellActivities;
import application.cell.CellActivitiesThread;
import application.frontground.FrontGroundController;
import application.handlers.KeyboardEventHandler;
import application.handlers.MouseEventHandler;
import application.keyController.KeyTitles;
import application.renders.PauseRender;
import application.settings.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;

public class Main extends Application {

    public static GameSettings gameSettings = new GameSettings();
    public static BoardSettings boardSettings = gameSettings.boardSettings;
    public static EnergyCostSettings energyCostSettings = gameSettings.energyCostSettings;
    public static FoodAddingSettings foodAddingSettings = gameSettings.foodAddingSettings;
    public static CellGenerationSettings cellGenerationSettings = gameSettings.cellGenerationSettings;
    public static RenderSettings renderSettings = gameSettings.renderSettings;
    public static ApplicationSettings applicationSettings = gameSettings.applicationSettings;
    FrontGroundController frontGroundController = new FrontGroundController();
    public static CellActions cellActions = new CellActions();
    KeyTitles keyTitles = new KeyTitles();
    PauseRender pauseRender = new PauseRender();
    public static List<Cell> cells = new ArrayList<>();
    public static List<Cell> cells1 = new ArrayList<>();
    public static List<Cell> cells2 = new ArrayList<>();
    public static List<Square> squares = new ArrayList<>();
    public static Map<String, Integer> mapSquareCoordinatesToIndex = new HashMap<>();
    static Map<Map<Integer, Integer>, Integer> foodsMap = new HashMap<>();
    public static Map<String, CellActions.CellActionsNames> actionMap = new HashMap<>();
    static boolean gameOver = false;
    public static boolean gameStopped = false;
    public static Random rand = new Random();
    public static List<Cell> cellsToDelete = new ArrayList<>();
    public static List<Cell> cellsToAdding = new ArrayList<>();
    public static boolean isFoodAdding = false;
    public static boolean isOnlyCloseAdding = false;
    public static int currentTick = 0;
    public long startGameMills = System.currentTimeMillis();
    public static int realFrameCount = 1;
    MouseEventHandler mouseEventHandler = new MouseEventHandler();
    KeyboardEventHandler keyboardEventHandler = new KeyboardEventHandler();
    BoardActivities boardActivities = new BoardActivities();
    CellActivities cellActivities = new CellActivities();

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
            mouseEventHandler.handle(scene);
            keyboardEventHandler.handle(scene, keyTitles, graphicsContext, pauseRender);
            // initialization playing field
            squareAdding();
            cellActions.actionMapGenerate();

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
                //startGameMills = System.currentTimeMillis(); вернуть как было
                System.out.println("time: " + (System.currentTimeMillis() - startGameMills)
                        + " size: " + cells.size());
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
                cells1 = cells.subList(0, (cells.size() / 2) - 1);
                cells2 = cells.subList(cells.size() / 2, cells.size());
                Thread firstThread = new CellActivitiesThread(cells1, graphicsContext, "first");
                Thread secondThread = new CellActivitiesThread(cells2, graphicsContext, "second");
                firstThread.start();
                secondThread.start();
                try{
                    firstThread.join();	//Подождать пока оппонент закончит высказываться.
                } catch(InterruptedException ignored){}
                try{
                    secondThread.join();	//Подождать пока оппонент закончит высказываться.
                } catch(InterruptedException ignored){}
                for (Cell cell : cells) {
                    graphicsContext.setFill(cell.color);
                    graphicsContext.fillRect(cell.coordinates.x, cell.coordinates.y, boardSettings.getSquareSize() /*- 1*/, boardSettings.getSquareSize() /*- 1*/);
                }
                int iiii = 0;
//                for (Cell cell : cells) {
//                    cellActivities.cellTick(cell, graphicsContext);
//                }
                for (Square square : squares) { //отрисовка квадратиков
                    if (square.frontGroundColor != null) {
                        graphicsContext.setFill(square.frontGroundColor);
                        graphicsContext.fillRect(square.coordinates.x, square.coordinates.y, boardSettings.getSquareSize(), boardSettings.getSquareSize());
                    }
                }
//                frontGroundController.renderQRCode(cells);
                if (!cellsToDelete.isEmpty()) {
                    cellsToDelete.forEach(e -> {
                        //todo надо мапу: удаленный - текущий квадратик
                        getCurrentSquare(e).removeObjectFromSquareItems(e);
                        cells.remove(e);
                    });
                    cellsToDelete = new ArrayList<>();
                }
                if (!cellsToAdding.isEmpty()) {
                    cellsToAdding.forEach(e -> {
                        cells.add(e);
                        //todo надо мапу: добавленный - текущий квадратик
                        getCurrentSquare(e).addObjectToSquareItems(e);
                    });
                    cellsToAdding = new ArrayList<>();
                }
                for (Square square : squares) {
                    if (!square.items.isEmpty()) {
                        square.calculateEating(cells);
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