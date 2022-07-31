package application;

import application.settingsDtos.CostSettings;
import application.settingsDtos.FoodAddingSettings;

public class BoardSettings {
    private int width = 300; //считается не в чистых координатах, а в квадратиках
    private int height = 200; //считается не в чистых координатах, а в квадратиках
    private int squareSize = 3;

    // energy cost settings
    // passive - в этом случае учитывается днк-код, и клетка платит за каждый фрагмент в коде
    // active - в этом случае учитывается только последствие применения способности
    private boolean isConsiderActiveCost = false; // учитывать ли стоимость действий
    private boolean isConsiderPassiveCost = true; // учитывать ли стоимость действий
    private int attackPassiveCost = 4;
    private int attackActiveCost = 0;
    private int defencePassiveCost = 4;
    private int defenceActiveCost = 0;
    private int doNothingPassiveCost = 1;
    private int doNothingActiveCost = 0;
    private int moveLeftPassive = 2;
    private int moveLeftActive = 3;
    private int moveRightPassive = 2;
    private int moveRightActive = 3;
    private int moveUpPassive = 2;
    private int moveUpActive = 3;
    private int moveDownPassive = 2;
    private int moveDownActive = 3;
    private int eatCloseFoodPassive = 3;
    private int eatCloseFoodActive = 5;

    // food adding settings
    // добавление энергии на клетку в течении хода, добавление "трупа" после смерти, частота
    private int freeFoodAddingRate = 5; // в тик на карте появляются столько квадратов free-еды
    private int closeFoodAddingRate = 5; // в тик на карте появляются столько квадратов close-еды
    private int freeEatAddingByEveryTick = 300; // количество добавляемой free-энергии за тик
    private int closeEatAddingByEveryTick = 300; // количество добавляемой close-энергии за тик
    private int freeEatAddingByDeath = 100; // количество free-энергии от "трупа" клетки

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getSquareSize() {
        return squareSize;
    }

    public CostSettings getCostSetting() {
        CostSettings settings = new CostSettings();
        settings.isConsiderActiveCost = this.isConsiderActiveCost;
        settings.isConsiderPassiveCost = this.isConsiderPassiveCost;
        settings.attackPassiveCost = this.attackPassiveCost;
        settings.attackActiveCost = this.attackActiveCost;
        settings.defencePassiveCost = this.defencePassiveCost;
        settings.defenceActiveCost = this.defenceActiveCost;
        settings.doNothingPassiveCost = this.doNothingPassiveCost;
        settings.doNothingActiveCost = this.doNothingActiveCost;
        settings.moveLeftPassive = this.moveLeftPassive;
        settings.moveLeftActive = this.moveLeftActive;
        settings.moveRightPassive = this.moveRightPassive;
        settings.moveRightActive = this.moveRightActive;
        settings.moveUpPassive = this.moveUpPassive;
        settings.moveUpActive = this.moveUpActive;
        settings.moveDownPassive = this.moveDownPassive;
        settings.moveDownActive = this.moveDownActive;
        settings.eatCloseFoodPassive = this.eatCloseFoodPassive;
        settings.eatCloseFoodActive = this.eatCloseFoodActive;
        return settings;
    }

    public FoodAddingSettings getFoodAddingSettings() {
        FoodAddingSettings foodAddingSettings = new FoodAddingSettings();
        foodAddingSettings.closeFoodAddingRate = this.closeFoodAddingRate;
        foodAddingSettings.freeFoodAddingRate = this.freeFoodAddingRate;
        foodAddingSettings.closeEatAddingByEveryTick = this.closeEatAddingByEveryTick;
        foodAddingSettings.freeEatAddingByEveryTick = this.freeEatAddingByEveryTick;
        foodAddingSettings.freeEatAddingByDeath = this.freeEatAddingByDeath;
        return foodAddingSettings;
    }
}
