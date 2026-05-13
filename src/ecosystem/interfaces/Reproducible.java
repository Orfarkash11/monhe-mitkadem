package ecosystem.interfaces;
import ecosystem.core.Environment;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
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
