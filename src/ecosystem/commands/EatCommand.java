package ecosystem.commands;

import ecosystem.core.Environment;
import ecosystem.entities.animals.Animal;
import ecosystem.interfaces.Consumable;
import ecosystem.entities.AbstractEntity;

public class EatCommand implements Command {
    private final Animal eater;
    private final Consumable target;

    public EatCommand(Animal eater, Consumable target) {
        this.eater = eater;
        this.target = target;
    }

    @Override
    public void execute(Environment env) {
        if (eater.isAlive() && target instanceof AbstractEntity) {
            AbstractEntity targetEntity = (AbstractEntity) target;
            if (targetEntity.isAlive()) {
                eater.eat(target);
            }
        }
    }
}