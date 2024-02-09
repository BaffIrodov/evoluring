package application.settings;

public class BoardSettings {
    private int width = 750; //считается не в чистых координатах, а в квадратиках
    private int height = 450; //считается не в чистых координатах, а в квадратиках
    private int squareSize = 2;

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
