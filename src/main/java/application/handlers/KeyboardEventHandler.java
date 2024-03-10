package application.handlers;

import application.board.BoardActivities;
import application.json.JsonSaveService;
import application.keyController.KeyTitles;
import application.renders.PauseRender;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static application.Main.*;

public class KeyboardEventHandler {

    private final BoardActivities boardActivities = new BoardActivities();
    private final JsonSaveService jsonSaveService = new JsonSaveService();

    public void handle(Scene scene, KeyTitles keyTitles,
                       GraphicsContext graphicsContext, PauseRender pauseRender) {
        if (applicationSettings.keyboardEventHandlerEnable) {
            scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
                if (keyTitles.mapKeyByKeyCodes.containsKey(key.getCode())) {
                    if (key.getCode() == KeyCode.SPACE) {
                        gameStopped = !gameStopped;
                        if (gameStopped) {
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
                        boardActivities.cellAdding();
                    }
                    if (key.getCode() == KeyCode.LEFT) {
                        isFoodAdding = !isFoodAdding;
                    }
                    if (key.getCode() == KeyCode.DOWN) {
                        isOnlyCloseAdding = !isOnlyCloseAdding;
                    }
                    if (key.getCode() == KeyCode.RIGHT) {
                        boardActivities.addFreeFoodInDistrict();
                    }
                    if (key.getCode() == KeyCode.DIGIT1) {
                        realFrameCount = 1;
                    }
                    if (key.getCode() == KeyCode.DIGIT2) {
                        realFrameCount = 2;
                    }
                    if (key.getCode() == KeyCode.DIGIT3) {
                        realFrameCount = 10;
                    }
                    if (key.getCode() == KeyCode.DIGIT5) {
                        jsonSaveService.saveAllObjectsToJson();
                    }
                }
            });
        }
    }

}
