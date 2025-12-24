package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.Iterator;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog;

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        playerArmy.getUnits().sort((u1, u2) -> Integer.compare(u2.getBaseAttack(), u1.getBaseAttack()));
        computerArmy.getUnits().sort((u1, u2) -> Integer.compare(u2.getBaseAttack(), u1.getBaseAttack()));
        while (armyHasAlive(playerArmy) && armyHasAlive(computerArmy)) {
            Iterator<Unit> player = playerArmy.getUnits().iterator();
            Iterator<Unit> computer = computerArmy.getUnits().iterator();
            while (player.hasNext() || computer.hasNext()) {
                step(player);
                step(computer);
            }
            playerArmy.getUnits().removeIf(unit -> !unit.isAlive());
            computerArmy.getUnits().removeIf(unit -> !unit.isAlive());
        }

        if (!armyHasAlive(playerArmy) && !armyHasAlive(computerArmy)) {
            System.out.println("Ничья");
        } else if (armyHasAlive(playerArmy)) {
            System.out.println("Победила армия игрока");
        } else {
            System.out.println("Победила армия компьютера");
        }
    }

    private void step(Iterator<Unit> player) throws InterruptedException {
        if (player.hasNext()) {
            Unit currentUnit = player.next();
            if (currentUnit.isAlive()) {
                Unit unit = currentUnit.getProgram().attack();
                printBattleLog.printBattleLog(currentUnit, unit);
            }
        }
    }

    private boolean armyHasAlive(Army army) {
        return army.getUnits().stream().anyMatch(Unit::isAlive);
    }
}