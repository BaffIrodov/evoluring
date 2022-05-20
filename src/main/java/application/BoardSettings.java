package application;

public class BoardSettings {
    private int width = 200; //считается не в чистых координатах, а в квадратиках
    private int height = 200; //считается не в чистых координатах, а в квадратиках
    private int squareSize = 5;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getSquareSize() {
        return squareSize;
    }
}
