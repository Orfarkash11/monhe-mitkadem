package ecosystem.entities;

import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.interfaces.Actable;

public class LivingEntity extends AbstractEntity implements Actable {
    private int age=0;
    private int energy;
    private int maxEnergy;

    public LivingEntity(Position position, Environment environment) {
        super();
    }

    protected boolean setEnergy(int energy)     {}
}
