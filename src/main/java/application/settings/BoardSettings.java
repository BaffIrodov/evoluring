package application.settings;

public class BoardSettings {
    private int width = 300; //считается не в чистых координатах, а в квадратиках
    private int height = 200; //считается не в чистых координатах, а в квадратиках
    private int squareSize = 3;

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
