package ecosystem.commands;

import ecosystem.core.Environment;
import ecosystem.entities.AbstractEntity;
import ecosystem.entities.LivingEntity;

public class ReproduceCommand implements Command {
    private final AbstractEntity child;

    public ReproduceCommand(AbstractEntity child) {
        this.child = child;
    }

    @Override
    public void execute(Environment env) {
        if (child != null && child.getPosition() != null) {
            if (env.isInsideBounds(child.getPosition()) && env.isPositionFree(child.getPosition())) {
                env.addEntity(child);
                if (child instanceof LivingEntity) {
                    ((LivingEntity) child).startThread();
                }
            }
        }
    }
}