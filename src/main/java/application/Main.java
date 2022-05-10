package application;

import java.util.*;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
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
    BoardSettings boardSettings = new BoardSettings();
    CellActions cellActions = new CellActions();
    static List<Cell> cells = new ArrayList<>();
    //    static List<Corner> foods = new ArrayList<>();
    static List<Square> squares = new ArrayList<>();
    Map<String, Integer> mapSquareCoordinatesToIndex = new HashMap<>();
    static Map<Map<Integer, Integer>, Integer> foodsMap = new HashMap<>();
    static Dir direction = Dir.left;
    static boolean gameOver = false;
    static boolean gameStoped = false;
    static Random rand = new Random();
    List<Cell> cellsToDelete = new ArrayList<>();
    List<Cell> cellsToAdding = new ArrayList<>();
    public boolean isFoodAdding = true;

    private int getRandPos(int number) {
        return new Random(number).nextInt();
    }

    public enum Dir {
        left, right, up, down
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

                    if (now - lastTick > 1) {
                        lastTick = now;
                        tick(graphicsContext);
                    }
                }

            }.start();

            Scene scene = new Scene(root, boardSettings.getWidth() * boardSettings.getSquareSize(), boardSettings.getHeight() * boardSettings.getSquareSize());

            // control
            scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
                if (key.getCode() == KeyCode.SPACE) {
                    gameStoped = !gameStoped;
                }
                if(key.getCode() == KeyCode.UP){
                    cellAdding();
                }
                if(key.getCode() == KeyCode.LEFT){
                    isFoodAdding = !isFoodAdding;
                }
                if (key.getCode() == KeyCode.W) {
                    direction = Dir.up;
                }
                if (key.getCode() == KeyCode.A) {
                    direction = Dir.left;
                }
                if (key.getCode() == KeyCode.S) {
                    direction = Dir.down;
                }
                if (key.getCode() == KeyCode.D) {
                    direction = Dir.right;
                }

            });
            // initialization playing field
            squareAdding();


//            cellAdding();


            //food adding
            //If you do not want to use css style, you can just delete the next line.
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("SNAKE GAME");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // tick
    public void tick(GraphicsContext graphicsContext) {
        if (gameOver) {
            graphicsContext.setFill(Color.RED);
            graphicsContext.setFont(new Font("", 50));
            graphicsContext.fillText("GAME OVER", 100, 250);
            return;
        }

        if (!gameStoped) {
            // fill
            // background
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect(0, 0, boardSettings.getWidth() * boardSettings.getSquareSize(), boardSettings.getHeight() * boardSettings.getSquareSize());

            // score
            graphicsContext.setFill(Color.WHITE);
            graphicsContext.setFont(new Font("", 30));
            graphicsContext.fillText("Score: ", 10, 30);

            if(isFoodAdding) {
                freeFoodAdding();
                closeFoodAdding();
            }

            for (Square square : squares) { //отрисовка квадратиков
                graphicsContext.setFill(Color.color(square.color.getRed(), square.color.getGreen(), square.color.getBlue()));
                graphicsContext.fillRect(square.coordinates.x, square.coordinates.y, boardSettings.getSquareSize(), boardSettings.getSquareSize());
            }
            for (Cell cell : cells) {
                boolean isDeath = cell.checkIfDeath();
                Square currentSquare = getCurrentSquare(cell);
                if(cell.energy > 300){
                    cellsToAdding.add(cell.generateChild(boardSettings.getSquareSize()));
                }
                if(!isDeath){
                    CellActions.CellActionsNames nextAction = cell.getNextAction();
                    switch (nextAction) {
                        case DO_NOTHING -> {cellActions.onDoNothing();}
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
                    graphicsContext.fillOval(cell.coordinates.x, cell.coordinates.y, boardSettings.getSquareSize() - 1, boardSettings.getSquareSize() - 1);
                    cell.energy--;
                }
                else{
                    cellsToDelete.add(cell);
                    currentSquare.freeFood += 100;
                    currentSquare.calculateColor(true);
                }
            }
            if(!cellsToDelete.isEmpty()){
                cellsToDelete.forEach(e -> cells.remove(e));
                cellsToDelete = new ArrayList<>();
            }
            if(!cellsToAdding.isEmpty()){
                cells.addAll(cellsToAdding);
                cellsToAdding = new ArrayList<>();
            }
            for(Square square : squares){
                if(!square.items.isEmpty()){
                    square.calculateEating(cells);
                }
            }
        }
    }

    public void squareAdding() { //начальная инициация всех клеток игрового поля
        int index = 0;
        for (int i = 0; i < boardSettings.getWidth(); i++) {
            for (int j = 0; j < boardSettings.getHeight(); j++) {
                squares.add(new Square(new Coordinates(i * boardSettings.getSquareSize(), j * boardSettings.getSquareSize()), 0, 0, new ArrayList<>()));
                mapSquareCoordinatesToIndex.put((String.valueOf(i) + "|" + String.valueOf(j)), index);
                index++;
            }
        }
    }

    public void cellAdding() {
        cells.add(new Cell("first", 1, 500, new Coordinates(100, 100), new DNA("o", 0), Color.RED));
//        cells.add(new Cell("second", 500, new Coordinates(400, 100), new DNA("aabbccdd", 0), Color.AQUA));
        cells.add(new Cell("third", 1, 500, new Coordinates(800, 100), new DNA("o", 0), Color.BLACK));
        cells.add(new Cell("fourth", 1, 500, new Coordinates(100, 800), new DNA("o", 0), Color.GREEN));
        cells.add(new Cell("fifth", 1, 500, new Coordinates(800, 800), new DNA("o", 0), Color.BLUE));
    }

    public void freeFoodAdding() {
        int randomSquareIndex = rand.nextInt(squares.size() - 1);
        Square randomSquare = squares.get(randomSquareIndex);
        randomSquare.freeFoodOnLastFrame = randomSquare.freeFood;
        randomSquare.freeFood += 100;
        randomSquare.calculateColor(false);
    }

    public void closeFoodAdding() {
        int randomSquareIndex = rand.nextInt(squares.size() - 1);
        Square randomSquare = squares.get(randomSquareIndex);
        randomSquare.closeFoodOnLastFrame = randomSquare.closeFood;
        randomSquare.closeFood += 100;
        randomSquare.calculateColor(false);
    }

    public Square getCurrentSquare(Cell cell){
        if(this.mapSquareCoordinatesToIndex.get((String.valueOf(cell.coordinates.x/this.boardSettings.getSquareSize())
                + "|" + String.valueOf(cell.coordinates.y/this.boardSettings.getSquareSize()))) == null){
            int wow = 0;
            cellActions.teleportCell(cell);
        }
        if((String.valueOf(cell.coordinates.x/this.boardSettings.getSquareSize())
                + "|" + String.valueOf(cell.coordinates.y/this.boardSettings.getSquareSize())) != null){
            return squares.get(this.mapSquareCoordinatesToIndex.get((String.valueOf(cell.coordinates.x/this.boardSettings.getSquareSize())
                    + "|" + String.valueOf(cell.coordinates.y/this.boardSettings.getSquareSize()))));
        }
        else {
            return squares.get(this.mapSquareCoordinatesToIndex.get("100" + "|" + "100"));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}