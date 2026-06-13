package ecosystem.factories;

import ecosystem.core.Position;
import ecosystem.entities.AbstractEntity;
import ecosystem.entities.LivingEntity;
import ecosystem.entities.animals.Deer;
import ecosystem.entities.animals.Lion;
import ecosystem.entities.animals.Rabbit;
import ecosystem.entities.plants.Flower;
import ecosystem.entities.plants.OakTree;
import ecosystem.entities.resources.Rock;
import ecosystem.entities.resources.Water;

import java.lang.reflect.Method;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * Pattern: Factory Method
 */
public class EntityFactory {

    public static final String[] SUPPORTED_TYPES = {
            "Lion", "Deer", "Rabbit", "OakTree", "Flower", "Rock", "Water"
    };

    public static AbstractEntity createEntity(String type, Position pos, int initialEnergy) {
        AbstractEntity entity;

        switch (type) {
            case "Lion":
                entity = new Lion(pos);
                break;
            case "Deer":
                entity = new Deer(pos);
                break;
            case "Rabbit":
                entity = new Rabbit(pos);
                break;
            case "OakTree":
                entity = new OakTree(pos);
                break;
            case "Flower":
                entity = new Flower(pos);
                break;
            case "Rock":
                return new Rock(pos);
            case "Water":
                return new Water(pos);
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }

        if (entity instanceof LivingEntity) {
            try {
                Method setEnergyMethod = LivingEntity.class.getDeclaredMethod("setEnergy", int.class);
                setEnergyMethod.setAccessible(true);
                setEnergyMethod.invoke(entity, initialEnergy);
            } catch (Exception ignored) {
            }
        }

        return entity;
    }
}