package ecosystem.core;

import ecosystem.commands.Command;
import ecosystem.entities.LivingEntity;
import ecosystem.gui.observer.SimulationObserver;
import ecosystem.gui.observer.SimulationEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Or Farkash 314920984
 * Oleg Magit 312544752
 * * Orchestrates the ecosystem simulation using a multithreaded architecture.
 * Manages a thread-safe command queue (Producer-Consumer pattern) to handle
 * entity actions without race conditions.
 */

public class SimulationEngine {
    private final List<SimulationObserver> observers = Collections.synchronizedList(new ArrayList<>());
    private final BlockingQueue<Command> commandQueue = new LinkedBlockingQueue<>();
    public final Object resourceLock = new Object();

    private int tickCount = 0;
    private Environment environment;
    private volatile boolean running = false;
    private Thread engineThread;

    public SimulationEngine() {
        this(new Environment(10, 10));
    }

    public SimulationEngine(Environment environment) {
        this.environment = (environment != null) ? environment : new Environment(10, 10);
    }

    public void submitCommand(Command cmd) {
        if (cmd != null) {
            commandQueue.offer(cmd);
        }
    }

    public void startSimulation() {
        if (running) return;
        running = true;
        engineThread = new Thread(() -> {
            while (running) {
                try {
                    Command cmd = commandQueue.take();
                    cmd.execute(environment);

                    synchronized (resourceLock) {
                        resourceLock.notifyAll();
                    }

                    tickCount++;
                    notifyObservers();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.err.println("Engine error: " + e.getMessage());
                }
            }
        }, "SimulationEngineThread");
        engineThread.start();
    }

    public void stopSimulation() {
        running = false;
        if (engineThread != null) {
            engineThread.interrupt();
            try {
                engineThread.join(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        commandQueue.clear();
    }

    public Environment getEnvironment() {
        return environment;
    }

    public int getTickCount() {
        return tickCount;
    }

    public boolean isRunning() {
        return running;
    }

    public void addObserver(SimulationObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    public void removeObserver(SimulationObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        SimulationEvent event = new SimulationEvent(tickCount, environment);
        List<SimulationObserver> snapshot;
        synchronized (observers) {
            snapshot = new ArrayList<>(observers);
        }
        for (SimulationObserver obs : snapshot) {
            obs.onSimulationUpdated(event);
        }
    }

    public void reset() {
        stopSimulation();
        tickCount = 0;
        if (environment != null) {
            environment.clear();
        }
        notifyObservers();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimulationEngine that = (SimulationEngine) o;
        return environment != null ? environment.equals(that.environment) : that.environment == null;
    }

    @Override
    public String toString() {
        return "SimulationEngine environment=" + environment;
    }

    public void replaceEntity(LivingEntity oldEntity, LivingEntity newEntity) {

        oldEntity.stopSimulation();
        environment.removeEntity(oldEntity);
        newEntity.setEngine(this);
        environment.addEntity(newEntity);

        if (newEntity.isAlive()) {
            newEntity.resumeSimulation();
            Thread t = new Thread(newEntity);
            t.start();
        }
    }
}