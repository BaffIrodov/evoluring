package application;

import javafx.scene.paint.Color;

import java.util.*;
import java.util.stream.Collectors;

public class Cell {
    public String name;
    public Integer generationNumber;
    public Integer energy;
    public DNA dna;
    public Integer attack;
    public Integer defence;
    public Coordinates coordinates;
    public Cell parentCell;
    public Cell childCell;
    public Color color;
    public Map<String, CellActions.CellActionsNames> actionMap = new HashMap<>();

    public Cell(String name, Integer generationNumber, Integer energy, Coordinates coordinates, DNA dna, Color color) {
        this.name = name;
        this.generationNumber = generationNumber;
        this.energy = energy;
        this.coordinates = coordinates;
        this.dna = dna;
        this.color = color;
        getAttackAndDefenceLikeVariables();
        actionMapGenerate();
    }

    public void actionMapGenerate() {
        this.actionMap.put("o", CellActions.CellActionsNames.DO_NOTHING);
        this.actionMap.put("a", CellActions.CellActionsNames.MOVE_LEFT);
        this.actionMap.put("b", CellActions.CellActionsNames.MOVE_UP);
        this.actionMap.put("c", CellActions.CellActionsNames.MOVE_RIGHT);
        this.actionMap.put("d", CellActions.CellActionsNames.MOVE_DOWN);
        this.actionMap.put("e", CellActions.CellActionsNames.EAT_CLOSE_FOOD);
        this.actionMap.put("f", CellActions.CellActionsNames.ATTACK);
        this.actionMap.put("g", CellActions.CellActionsNames.DEFENCE);
    }

    public CellActions.CellActionsNames getNextAction() {
        char[] dnaCommands = dna.dnaCode.toCharArray();

        int commandOverflow = dnaCommands.length;
        if (dna.dnaCursor + 1 == commandOverflow) {
            dna.dnaCursor = -1;
        }
        char nextActionInDNA = dnaCommands[dna.dnaCursor + 1];
        dna.dnaCursor++;
        return this.actionMap.get(String.valueOf(nextActionInDNA));
    }

    public void pickDnaToAction(CellActions.CellActionsNames cellActionName, String dnaFragment) {
        this.actionMap.put(dnaFragment, cellActionName);
    }

    public Cell generateChild(int squareSize) {
        Random rand = new Random();
        int rangeX = rand.nextInt(1, 3);
        int rangeY = rand.nextInt(1, 3);
        int signForRangeX = rand.nextInt(1, 2);
        int signForRangeY = rand.nextInt(1, 2);
        if (signForRangeX == 2) {
            rangeX = -rangeX;
        }
        if (signForRangeY == 2) {
            rangeY = -rangeY;
        }
        this.energy -= 100;
        Cell newCell = new Cell(this.name, this.generationNumber++, 100, new Coordinates(this.coordinates.x + rangeX * squareSize, this.coordinates.y + rangeY * squareSize), new DNA(this.dnaGeneration(this.dna.dnaCode), 0), this.color);
        newCell.parentCell = this;
        this.childCell = newCell;
        return newCell;
    }

    public boolean checkIfDeath() {
        return this.energy < 0;
    }

    public String dnaGeneration(String dnaCode) {
        int countOfGenesToDeleting = 1;
        List<String> geneList = actionMap.keySet().stream().toList();
        String genesToAdding = "";
        Random rand = new Random();
        int countOfGenesToAdding = rand.nextInt(1, 5);
        if (countOfGenesToAdding > 1) {
            countOfGenesToDeleting = rand.nextInt(1, countOfGenesToAdding);
        }
        for (int i = 0; i < countOfGenesToAdding; i++) {
            int nextGene = rand.nextInt(0, geneList.size());
            genesToAdding += geneList.get(nextGene);
        }
        String genesAfterAdding = dnaCode + genesToAdding;
        String result = "";
        for (int j = 0; j < countOfGenesToDeleting; j++) {
            result = "";
            int index = rand.nextInt(genesAfterAdding.length() - 1);
            char[] genesAfterAddingArray = genesAfterAdding.toCharArray();
            for (int i = 0; i < genesAfterAddingArray.length; i++) {
                if (i != index) {
                    result += genesAfterAddingArray[i];
                }
            }
            genesAfterAdding = result;
        }
        return result;
    }

    public void getAttackAndDefenceLikeVariables() {
        this.attack = this.dna.dnaCode.length() - this.dna.dnaCode.replace("f", "").length();
        this.defence = this.dna.dnaCode.length() - this.dna.dnaCode.replace("g", "").length();
    }
}
