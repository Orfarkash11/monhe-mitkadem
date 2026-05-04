package ecosystem.behaviors;
import ecosystem.entities.animals.Animal;
import ecosystem.core.Environment;
public interface MovementStrategy {
   public boolean Move(Animal entity, Environment env) ;
}
