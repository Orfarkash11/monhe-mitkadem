package ecosystem.interfaces;
import ecosystem.core.Environment;
import ecosystem.entities.AbstractEntity;

import java.util.List;

public interface Sensory {
    List<AbstractEntity> sense (Environment env);
}
