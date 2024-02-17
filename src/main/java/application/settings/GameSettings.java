package application.settings;

public class GameSettings {

    //todo надо поменять - не делать new, но сделать как поля

    public BoardSettings getBoardSettings() {
        return new BoardSettings();
    }

    public EnergyCostSettings getCostSetting() {
        return new EnergyCostSettings();
    }

    public FoodAddingSettings getFoodAddingSettings() {
        return new FoodAddingSettings();
    }

    public CellGenerationSettings getCellGenerationSettings() {
        return new CellGenerationSettings();
    }
    public RenderSettings getRenderSettings() { return new RenderSettings(); }
    public ApplicationSettings getApplicationSettings() { return new ApplicationSettings(); }
}
