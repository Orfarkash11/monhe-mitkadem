package ecosystem.entities.plants;

import ecosystem.core.Environment;
import ecosystem.core.Position;
import ecosystem.entities.LivingEntity;
import ecosystem.interfaces.Consumable;
import ecosystem.interfaces.EdibleByHerbivore;
import ecosystem.interfaces.Reproducible;

public abstract class Plant extends LivingEntity implements Reproducible, EdibleByHerbivore {
    private int growthRate;
    private double reproductionChance;

    public Plant(Position position, char symbol, int energy, int maxEnergy,
          int growthRate, double reproductionChance)

    public int getNutritionValue(){

    }
    public boolean onConsumed(){

    }
    public boolean reproduce(Environment env){

    }
    public boolean act(Environment env){
        
    }
}
