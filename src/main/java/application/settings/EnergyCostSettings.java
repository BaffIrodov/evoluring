package application.settings;

public class EnergyCostSettings {
    public boolean isConsiderActiveCost = true; // учитывать ли стоимость действий
    public boolean isConsiderPassiveCost = false; // учитывать ли стоимость действий
    public int attackPassiveCost = 4;
    public int attackActiveCost = 0;
    public int defencePassiveCost = 4;
    public int defenceActiveCost = 0;
    public int doNothingPassiveCost = 1;
    public int doNothingActiveCost = 0;
    public int moveLeftPassive = 2;
    public int moveLeftActive = 3;
    public int moveRightPassive = 2;
    public int moveRightActive = 3;
    public int moveUpPassive = 2;
    public int moveUpActive = 3;
    public int moveDownPassive = 2;
    public int moveDownActive = 3;
    public int eatCloseFoodPassive = 3;
    public int eatCloseFoodActive = 5;
}
