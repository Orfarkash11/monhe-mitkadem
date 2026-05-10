# Ecosystem Simulation — Advanced OOP Assignment 2

Java project that simulates a simple ecosystem on a 2D map.

The project was built for an Advanced Object-Oriented Programming course and focuses on:

- inheritance and abstract classes
- interfaces and polymorphism
- encapsulation with private fields
- boolean action methods
- delegation / strategy pattern for movement and feeding
- simple Java implementation without external libraries

---

## Project Structure

```text
src/
└── ecosystem/
    ├── core/
    │   ├── Position.java
    │   ├── Environment.java
    │   └── SimulationEngine.java
    │
    ├── interfaces/
    │   ├── Actable.java
    │   ├── Movable.java
    │   ├── Consumable.java
    │   ├── Eater.java
    │   ├── Reproducible.java
    │   ├── Sensory.java
    │   ├── EdibleByCarnivore.java
    │   └── EdibleByHerbivore.java
    │
    ├── entities/
    │   ├── AbstractEntity.java
    │   ├── LivingEntity.java
    │   ├── StaticEntity.java
    │   ├── animals/
    │   │   ├── Animal.java
    │   │   ├── Lion.java
    │   │   ├── Deer.java
    │   │   └── Rabbit.java
    │   ├── plants/
    │   │   ├── Plant.java
    │   │   ├── OakTree.java
    │   │   └── Flower.java
    │   └── resources/
    │       ├── Resource.java
    │       ├── Rock.java
    │       └── Water.java
    │
    └── behaviors/
        ├── MovementStrategy.java
        ├── RandomMovement.java
        ├── ChaseMovement.java
        ├── EscapeMovement.java
        ├── FeedingBehavior.java
        ├── CarnivoreBehavior.java
        └── HerbivoreBehavior.java
```

### Main Components

**Position**

Represents a position on the map using row and column.
Main responsibilities:
- store row and column
- validate coordinates
- calculate Manhattan distance
- support equals and toString

**Environment**

Manages the ecosystem world.
Responsibilities:
- stores map size
- stores all entities in a list
- checks whether a position is free
- adds and removes entities
- moves entities
- finds nearby entities
- renders the map as text

The map is managed using a list of entities:
`private List<AbstractEntity> entities;`
Each entity has its own Position.

**SimulationEngine**

Runs the simulation in ticks.
Each tick:
- creates a snapshot of the current entities
- calls act(environment) on entities that implement Actable
- removes dead entities
- prints the map
- prints population summary

---

## Entity Symbols

| Entity | Symbol |
| :--- | :--- |
| Lion | L |
| Deer | D |
| Rabbit | R |
| OakTree | T |
| Flower | F |
| Rock | X |
| Water | W |
| Empty cell | . |

---

## Animals

| Class | Initial Energy | Movement | Feeding |
| :--- | :--- | :--- | :--- |
| Lion | 100 | ChaseMovement | CarnivoreBehavior |
| Deer | 70 | EscapeMovement | HerbivoreBehavior |
| Rabbit | 50 | RandomMovement | HerbivoreBehavior |

Rabbit also implements Reproducible.

---

## Plants

| Class | Symbol | Initial Energy | Max Energy | Growth Rate |
| :--- | :--- | :--- | :--- | :--- |
| OakTree | T | 80 | 120 | 2 |
| Flower | F | 30 | 70 | 5 |

Plants do not lose 2 energy per tick. Instead, they grow according to growthRate and may reproduce.

---

## Resources

| Class | Symbol | Description |
| :--- | :--- | :--- |
| Rock | X | Static resource that blocks occupation of its cell |
| Water | W | Consumable resource that does not disappear when consumed |

---

## Delegation / Strategy

Animal movement and feeding logic is delegated to behavior classes.
Each Animal has:
- `private MovementStrategy movementStrategy;`
- `private FeedingBehavior feedingBehavior;`

**Movement strategies:**
- RandomMovement
- ChaseMovement
- EscapeMovement

**Feeding behaviors:**
- CarnivoreBehavior
- HerbivoreBehavior

This keeps animal classes simple and avoids duplicated logic.

---

## Compile

**Windows CMD**
```cmd
dir /s /b src\*.java > sources.txt
javac -encoding UTF-8 -d out @sources.txt
```

**Git Bash / Linux / macOS**
```bash
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d out @sources.txt
```

---

## Run

```bash
java -cp out ecosystem.core.SimulationEngine
```

The demo initializes a small ecosystem and runs several simulation ticks.

---

## Do Not Commit Generated Files

These files/folders should not be committed:
- sources.txt
- out/
- *.class
- antigravity_assignment2_split_pack/

Only source files under src/ and this README should be committed.

---

## Implementation Notes

### Plant vs Planet
The assignment text may mention Planet, but the required package structure uses Plant. This project uses Plant.

### Consumable vs Consumer
The project uses Consumable according to the required interface structure.

### Current Status
The project has been verified with:
- javac compilation
- SimulationEngine runtime demo
- private field checks
- no external dependencies
- no added test framework
