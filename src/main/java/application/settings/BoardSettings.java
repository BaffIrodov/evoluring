package application.settings;

public class BoardSettings {
    private int width = 150; //считается не в чистых координатах, а в квадратиках
    private int height = 100; //считается не в чистых координатах, а в квадратиках
    private int squareSize = 6;

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
