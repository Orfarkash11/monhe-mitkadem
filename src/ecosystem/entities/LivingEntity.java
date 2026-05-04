package ecosystem.entities;

import ecosystem.interfaces.Actable;

public class LivingEntity extends AbstractEntity implements Actable {
    private int age=0;
    private int energy;
    private int maxEnergy;
}
