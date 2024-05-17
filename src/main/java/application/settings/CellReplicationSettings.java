package application.settings;

public class CellReplicationSettings {
    public boolean replicateByFood = true; // рождение новых клеток по количеству еды
    public boolean replicateByGene = false; // рождение новых клеток по гену
    public int costOfReplicationByFood = 300; // стоимость рождения по еде в её количестве
    public int costOfReplicationByGene = 50; // стоимость рождения по гену по еде в её количестве
}
