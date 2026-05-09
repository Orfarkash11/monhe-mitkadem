package ecosystem.entities;

import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.interfaces.Actable;

public class LivingEntity extends AbstractEntity implements Actable {
    private int age = 0;
    private int energy;
    private int maxEnergy;

    public LivingEntity(Position position, Environment environment) {
        super();
    }

    public int getEnergy() {
        return this.energy;
    }

    protected boolean setEnergy(int energy) {
        this.energy = energy;
        return true;
    }

    @Override
    public boolean act(Environment env) {
        this.age++;
        this.energy -= 2;
        if (this.energy <= 0) {
            return false;
        }
        return true;
    }
}
