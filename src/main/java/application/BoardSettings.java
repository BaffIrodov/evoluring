package application;

public class BoardSettings {
    private int width = 90; //считается не в чистых координатах, а в квадратиках
    private int height = 90; //считается не в чистых координатах, а в квадратиках
    private int squareSize = 10;

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
