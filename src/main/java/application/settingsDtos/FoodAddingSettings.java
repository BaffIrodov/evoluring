package application.settingsDtos;

public class FoodAddingSettings {
    public int freeFoodAddingRate; // в тик на карте появляются столько квадратов free-еды
    public int closeFoodAddingRate; // в тик на карте появляются столько квадратов close-еды
    public int freeEatAddingByEveryTick; // количество добавляемой free-энергии за тик
    public int closeEatAddingByEveryTick; // количество добавляемой close-энергии за тик
    public int freeEatAddingByDeath; // количество free-энергии от "трупа" клетки
}
