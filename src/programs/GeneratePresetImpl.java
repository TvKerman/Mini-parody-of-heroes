package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {
    private static final int MAX_UNIT_OF_ONE_TYPE = 11;
    private static final int WIDTH = 21;
    private static final int DEPTH = 3;
    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        List<Unit> sortedList = new ArrayList<>(unitList);
        sortedList.sort((firstUnit, secondUnit) -> {
            double attackCostRatioFirstUnit = (double)firstUnit.getBaseAttack() / firstUnit.getCost();
            double attackCostRatioSecondUnit = (double)secondUnit.getBaseAttack() / secondUnit.getCost();
            int compareResult = Double.compare(attackCostRatioSecondUnit, attackCostRatioFirstUnit);
            if (compareResult != 0) {
                return compareResult;
            } else {
                double healthCostRatioFirstUnit = (double)firstUnit.getHealth() / firstUnit.getCost();
                double healthCostRatioSecondUnit = (double) secondUnit.getHealth() / secondUnit.getCost();
                return Double.compare(healthCostRatioSecondUnit, healthCostRatioFirstUnit);
            }
        });

        int remainingPoints = maxPoints;
        List<Unit> newArmy = new ArrayList<>(MAX_UNIT_OF_ONE_TYPE * unitList.size());
        int[] positions = generateRandomPermutation(WIDTH * DEPTH, new Random());
        int indexPosition = 0;
        for (Unit candidate: sortedList) {
            int maxUnitsByCost = remainingPoints / candidate.getCost();
            int unitsToAdd = Math.min(MAX_UNIT_OF_ONE_TYPE, maxUnitsByCost);
            for (int i = 0; i < unitsToAdd; i++) {
                newArmy.add(new Unit(candidate.getName() + " " + (i + 1),
                        candidate.getUnitType(),
                        candidate.getHealth(),
                        candidate.getBaseAttack(),
                        candidate.getCost(),
                        candidate.getAttackType(),
                        candidate.getAttackBonuses(),
                        candidate.getDefenceBonuses(),
                        positions[indexPosition] / WIDTH,
                        positions[indexPosition] % WIDTH));
                indexPosition++;
            }
            remainingPoints -= candidate.getCost() * unitsToAdd;
        }
        Army army = new Army();
        army.setUnits(newArmy);
        army.setPoints(maxPoints - remainingPoints);
        return army;
    }

    private static int[] generateRandomPermutation(int n, Random random) {
        int[] permutation = new int[n];

        for (int i = 0; i < n; i++) {
            permutation[i] = i;
        }

        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);

            int temp = permutation[i];
            permutation[i] = permutation[j];
            permutation[j] = temp;
        }

        return permutation;
    }
}