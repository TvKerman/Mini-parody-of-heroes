package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {
    private static final int MAX_UNIT_IN_ROW = 21;
    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();
        Unit[] zBuffer = new Unit[MAX_UNIT_IN_ROW];
        for (var row: unitsByRow) {
            for (var unit: row) {
                int index = unit.getyCoordinate();
                if ((isLeftArmyTarget || zBuffer[index] == null) && unit.isAlive()) {
                    zBuffer[index] = unit;
                }
            }
        }

        for (var unit: zBuffer) {
            if (unit != null) {
                suitableUnits.add(unit);
            }
        }

        return suitableUnits;
    }
}
