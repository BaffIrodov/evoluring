package application.handlers;

import application.Main;
import application.board.BoardActivities;
import application.keyController.KeyTitles;
import application.renders.PauseRender;
import application.settings.*;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class KeyboardEventHandler {

    private final BoardActivities boardActivities = new BoardActivities();

    public void handle(ApplicationSettings applicationSettings, BoardSettings boardSettings,
                       RenderSettings renderSettings, EnergyCostSettings energyCostSettings,
                       Scene scene, KeyTitles keyTitles,
                       GraphicsContext graphicsContext, PauseRender pauseRender) {
        if (applicationSettings.keyboardEventHandlerEnable) {
            scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
                if (keyTitles.mapKeyByKeyCodes.containsKey(key.getCode())) {
                    if (key.getCode() == KeyCode.SPACE) {
                        Main.gameStopped = !Main.gameStopped;
                        if (Main.gameStopped) {
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
                                        pauseRender.getGameCondition(Main.cells) : "";
                                graphicsContext.fillText(keyDescription + splitter + gameCondition,
                                        30, 30);
                            }
                        }
                    }
                    if (key.getCode() == KeyCode.UP) {
                        boardActivities.cellAdding();
                    }
                    if (key.getCode() == KeyCode.LEFT) {
                        Main.isFoodAdding = !Main.isFoodAdding;
                    }
                    if (key.getCode() == KeyCode.DOWN) {
                        Main.isOnlyCloseAdding = !Main.isOnlyCloseAdding;
                    }
                    if (key.getCode() == KeyCode.RIGHT) {
                        boardActivities.addFreeFoodInDistrict();
                    }
                    if (key.getCode() == KeyCode.DIGIT1) {
                        Main.realFrameCount = 1;
                    }
                    if (key.getCode() == KeyCode.DIGIT2) {
                        Main.realFrameCount = 2;
                    }
                    if (key.getCode() == KeyCode.DIGIT3) {
                        Main.realFrameCount = 10;
                    }
                }
            });
        }
    }

}
