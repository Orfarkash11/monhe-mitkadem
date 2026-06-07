package ecosystem.commands;
import ecosystem.core.Environment;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * * Defines a command interface for the simulation to execute actions asynchronously.
 * This pattern facilitates thread-safe interaction with the Environment.
 */

public interface Command {
    void execute(Environment env);
}