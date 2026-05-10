package ecosystem.entities;

import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.interfaces.Actable;

public abstract class LivingEntity extends AbstractEntity implements Actable {
    private int age = 0;
    private int energy;
    private int maxEnergy;

    public LivingEntity(Position position, char symbol, int energy, int maxEnergy, Environment environment) {

        super(position, symbol);
    }

    public int getAge()
    public int getMaxEnergy()
    public int getEnergy() {
        return this.energy;
    }
    protected boolean setEnergy(int energy) {
        this.energy = energy;
        return true;
    }
    protected boolean setMaxEnergy(int maxEnergy){}

    public boolean addEnergy(int amount){

    }
    public boolean reduceEnergy(int amount){
        
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
