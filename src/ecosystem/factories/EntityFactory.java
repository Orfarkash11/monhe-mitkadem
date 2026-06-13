package ecosystem.factories;

import ecosystem.core.Position;
import ecosystem.entities.AbstractEntity;
import ecosystem.entities.animals.Deer;
import ecosystem.entities.animals.Lion;
import ecosystem.entities.animals.Rabbit;
import ecosystem.entities.plants.Flower;
import ecosystem.entities.plants.OakTree;
import ecosystem.entities.resources.Rock;
import ecosystem.entities.resources.Water;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * * Pattern: Factory Method
 * Factory class responsible for creating all ecosystem entities,
 * decoupling the GUI from concrete implementations.
 */
public class EntityFactory {

    public static AbstractEntity createEntity(String type, Position pos, int initialEnergy) {
        // Note: Currently using default constructors that set their own default energies.
        // The signature includes initialEnergy as requested by the assignment requirements.
        switch (type) {
            case "Lion":
                return new Lion(pos);
            case "Deer":
                return new Deer(pos);
            case "Rabbit":
                return new Rabbit(pos);
            case "OakTree":
                return new OakTree(pos);
            case "Flower":
                return new Flower(pos);
            case "Rock":
                return new Rock(pos);
            case "Water":
                return new Water(pos);
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}