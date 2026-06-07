package ecosystem.commands;

import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.entities.animals.Animal;

public class MoveCommand implements Command {
    private final Animal animal;
    private final Position targetPosition;

    public MoveCommand(Animal animal, Position targetPosition) {
        this.animal = animal;
        this.targetPosition = targetPosition;
    }

    @Override
    public void execute(Environment env) {
        if (animal.isAlive() && env.isInsideBounds(targetPosition) && env.isPositionFree(targetPosition)) {
            env.moveEntity(animal, targetPosition);
        }
    }
}