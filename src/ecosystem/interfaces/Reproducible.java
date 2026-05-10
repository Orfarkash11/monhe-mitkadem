package ecosystem.interfaces;
import ecosystem.core.Environment;

/**
 * Interface for entities that can reproduce.
 */
public interface Reproducible {
    /**
     * Attempts to reproduce within the environment.
     * @param env the environment.
     * @return true if reproduction was successful, false otherwise.
     */
    boolean reproduce(Environment env);
}
