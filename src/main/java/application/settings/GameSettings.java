package application.settings;

public class GameSettings {

    public BoardSettings boardSettings;
    public EnergyCostSettings energyCostSettings;
    public FoodAddingSettings foodAddingSettings;
    public CellGenerationSettings cellGenerationSettings;
    public RenderSettings renderSettings;
    public ApplicationSettings applicationSettings;

    public GameSettings() {
        this.boardSettings = new BoardSettings();
        this.energyCostSettings = new EnergyCostSettings();
        this.foodAddingSettings = new FoodAddingSettings();
        this.cellGenerationSettings = new CellGenerationSettings();
        this.renderSettings = new RenderSettings();
        this.applicationSettings = new ApplicationSettings();
    }
}
