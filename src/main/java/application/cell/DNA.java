package application.cell;

import application.cellaction.CellAction;
import application.cellaction.CellActionContext;

import java.util.*;

import static application.Main.*;
import static application.Main.energyCostSettings;

public class DNA {

    public LinkedList<DNASection> dnaSections = new LinkedList<>();
    public String dnaCode;
    public int dnaCursor;

    public DNA() {
        for (int i = 0; i < 3; i++) {
            this.dnaSections.add(new DNASection(CellActionContext.getRandomCellAction()));
        }
    }

    public DNA(LinkedList<DNASection> dnaSections) {
        this.dnaSections.addAll(dnaSections);
    }

    public DNA(DNA parentCellDNA, int generationNumber) {
        //todo пока затычка, изменить
        int countOfGenesToAdding = rand.nextInt(1, 4);
        int countOfGenesToDeleting = rand.nextInt(0, countOfGenesToAdding+1);
        int maxDnaSize = 50;

        if (parentCellDNA.dnaSections.size() > 0) {
            this.dnaSections.addAll(parentCellDNA.dnaSections);
        }
        // добавление
        if (this.dnaSections.size() + countOfGenesToAdding < maxDnaSize) {
            for (int i = 0; i < countOfGenesToAdding; i++) {
                this.dnaSections.add(new DNASection(CellActionContext.getRandomCellAction()));
            }
        }
        // удаление
        for (int i = 0; i < countOfGenesToDeleting; i++) {
            this.dnaSections.remove(rand.nextInt(0, this.dnaSections.size()));
        }
    }

    public CellAction getNextAction() {
        this.dnaCursor++;
        if (this.dnaCursor >= this.dnaSections.size()) {
            this.dnaCursor = 0;
        }
        return this.dnaSections.get(this.dnaCursor).cellAction;
    }

//    public void getCountDnaGenesByTypeAndEnergyCost() { //плата за сложность днк
//        char[] chars = this.dna.dnaCode.toCharArray();
//        Map<String, Integer> dnaCodeCountByName = new HashMap<>();
//        for (char aChar : chars) {
//            dnaCodeCountByName.merge(String.valueOf(aChar), 1, Integer::sum);
//        }
//        this.attack = Optional.ofNullable(dnaCodeCountByName.get("f")).orElse(0);
//        this.defence = Optional.ofNullable(dnaCodeCountByName.get("g")).orElse(0);
//        int doNothingLength = Optional.ofNullable(dnaCodeCountByName.get("o")).orElse(0);
//        int moveLeftLength = Optional.ofNullable(dnaCodeCountByName.get("a")).orElse(0);
//        int moveRightLength = Optional.ofNullable(dnaCodeCountByName.get("b")).orElse(0);
//        int moveUpLength = Optional.ofNullable(dnaCodeCountByName.get("c")).orElse(0);
//        int moveDownLength = Optional.ofNullable(dnaCodeCountByName.get("d")).orElse(0);
//        int eatCloseLength = Optional.ofNullable(dnaCodeCountByName.get("e")).orElse(0);
//        this.energyCost = this.attack * energyCostSettings.attackPassiveCost
//                + this.defence * energyCostSettings.defencePassiveCost
//                + doNothingLength * energyCostSettings.doNothingPassiveCost
//                + moveLeftLength * energyCostSettings.moveLeftPassive
//                + moveRightLength * energyCostSettings.moveRightPassive
//                + moveUpLength * energyCostSettings.moveUpPassive
//                + moveDownLength * energyCostSettings.moveDownPassive
//                + eatCloseLength * energyCostSettings.eatCloseFoodPassive;
//    }

    public void addDnaCode(String addingElement) {
        this.dnaCode += addingElement;
    }
}
