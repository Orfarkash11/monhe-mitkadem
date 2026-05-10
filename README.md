# Ecosystem Simulation — Advanced OOP Assignment 2

Java project that simulates a simple ecosystem on a 2D map. This project focuses on inheritance, abstract classes, interfaces, polymorphism, and encapsulation within a discrete simulation environment.

---

## 1. Quick Start

### Windows CMD
```cmd
dir /s /b src\*.java > sources.txt
javac -encoding UTF-8 -d out @sources.txt
java -cp out ecosystem.core.SimulationEngine
```

### Git Bash / Linux / macOS
```bash
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d out @sources.txt
java -cp out ecosystem.core.SimulationEngine
```

---

## 2. Project Goal

The project simulates an ecosystem on a 2D map containing animals, plants, and resources. The simulation advances in discrete steps (ticks) where each entity interacts with its environment. The implementation demonstrates key OOP principles, including a delegation-based behavior system for animal movement and feeding strategies.

---

## 3. Current Implementation Status

- [x] Required package structure
- [x] Core classes
- [x] Interfaces
- [x] Entity hierarchy
- [x] Animals
- [x] Plants
- [x] Resources
- [x] Behaviors/delegators
- [x] Environment
- [x] SimulationEngine
- [x] Plain Java only
- [x] No external libraries

---

## 4. How the Simulation Works

1. **SimulationEngine** creates or receives an **Environment** instance.
2. **Environment** manages all entities and spatial logic.
3. Each **tick** creates a snapshot of the current entities to ensure deterministic behavior.
4. Each alive **Actable** entity runs its `act(environment)` logic.
5. Animals may sense, move, eat, and reproduce based on their state and strategies.
6. Plants grow and attempt to reproduce in adjacent free cells.
7. Dead entities (energy = 0) are removed from the environment at the end of the tick.
8. The current map state and a population summary are printed to the console.

---

## 5. Package Guide

| Package | Responsibility | Main files |
| :--- | :--- | :--- |
| `ecosystem.core` | Core simulation logic and spatial management | `Position`, `Environment`, `SimulationEngine` |
| `ecosystem.interfaces` | Behavioral contracts and capability markers | `Actable`, `Consumable`, `Movable`, `Eater` |
| `ecosystem.entities` | Base entity hierarchy and state management | `AbstractEntity`, `LivingEntity`, `StaticEntity` |
| `ecosystem.entities.animals` | Animal implementations | `Animal`, `Lion`, `Deer`, `Rabbit` |
| `ecosystem.entities.plants` | Plant implementations | `Plant`, `OakTree`, `Flower` |
| `ecosystem.entities.resources` | Static resource implementations | `Resource`, `Rock`, `Water` |
| `ecosystem.behaviors` | Delegated strategy implementations | `MovementStrategy`, `FeedingBehavior` |

---

## 6. Core Classes

- **Position**: Represents row and column coordinates. Handles Manhattan distance calculations and validates that coordinates are non-negative.
- **Environment**: Manages map dimensions and entity storage. Handles spatial queries (finding nearby entities, checking occupancy) and renders the map as a text grid.
  - **Note**: The map is **not** stored as a 2D array. Instead, entities store their own `Position`, and the `Environment` maintains a `List<AbstractEntity>`. The grid is generated from this list during rendering.
- **SimulationEngine**: Orchestrates the tick loop. Handles snapshot iteration, triggers entity actions, manages dead entity cleanup, and prints outputs.

---

## 7. Entity Hierarchy

```text
AbstractEntity
├── LivingEntity (manages age, energy, and life state)
│   ├── Animal (delegates logic to movement/feeding strategies)
│   │   ├── Lion
│   │   ├── Deer
│   │   └── Rabbit
│   └── Plant (handles growth and reproduction)
│       ├── OakTree
│       └── Flower
└── StaticEntity
    └── Resource (non-acting, static environment elements)
        ├── Rock
        └── Water
```

---

## 8. Interfaces

| Interface | Used by | Purpose |
| :--- | :--- | :--- |
| `Actable` | `LivingEntity` | Defines the entry point for per-tick logic |
| `Movable` | `Animal` | Allows an entity to change its position |
| `Consumable` | Resources, Entities | Allows an entity to be eaten/consumed for energy |
| `Eater` | `Animal` | Allows an entity to consume `Consumable` targets |
| `Reproducible` | Plants, Rabbit | Defines reproduction capabilities |
| `Sensory` | `Animal` | Allows an entity to detect nearby entities |
| `EdibleByCarnivore`| Prey / Carnivores | Marker for carnivore diet targets |
| `EdibleByHerbivore` | Plants | Marker for herbivore diet targets |

---

## 9. Behavior Delegators

Animals delegate their complex movement and feeding logic to strategy objects to ensure clean separation of concerns and avoid code duplication.

### MovementStrategy
| Class | Behavior |
| :--- | :--- |
| `RandomMovement` | Moves randomly to a nearby adjacent free cell |
| `ChaseMovement` | Targets and moves toward `EdibleByCarnivore` food |
| `EscapeMovement` | Moves away from the nearest animal threat |

### FeedingBehavior
| Class | Behavior |
| :--- | :--- |
| `CarnivoreBehavior` | Consumes `Consumable` + `EdibleByCarnivore` targets |
| `HerbivoreBehavior` | Consumes `Consumable` + `EdibleByHerbivore` targets |

**Note**: If no diet-specific food exists, behaviors may fall back to adjacent neutral `Consumable` resources such as `Water`.

---

## 10. Entity Symbols

| Entity | Symbol | Entity | Symbol |
| :--- | :--- | :--- | :--- |
| Lion | **L** | Deer | **D** |
| Rabbit | **R** | OakTree | **T** |
| Flower | **F** | Rock | **X** |
| Water | **W** | Empty | **.** |

---

## 11. Where to Change What

| If you want to change... | Edit this file/package |
| :--- | :--- |
| Map size or entity storage | `Environment` |
| Tick flow or output summary | `SimulationEngine` |
| Base animal behavior | `Animal` |
| Lion movement/eating | `Lion`, `ChaseMovement`, `CarnivoreBehavior` |
| Deer behavior | `Deer`, `EscapeMovement`, `HerbivoreBehavior` |
| Rabbit reproduction | `Rabbit` |
| Plant growth logic | `Plant` |
| OakTree reproduction | `OakTree` |
| Flower reproduction | `Flower` |
| Movement rules | `ecosystem.behaviors` movement classes |
| Feeding rules | `ecosystem.behaviors` feeding classes |
| Entity symbols | Concrete entity constructors |

---

## 12. Validation Commands

### Static Checks (PowerShell)
Run these commands to verify encapsulation and assignment compliance:
```powershell
# Check for public/protected fields (encapsulation)
Get-ChildItem -Recurse src -Filter *.java | Select-String -Pattern "public\s+.*\s*;"
Get-ChildItem -Recurse src -Filter *.java | Select-String -Pattern "protected\s+.*\s*;"

# Review type usage and polymorphism
Get-ChildItem -Recurse src -Filter *.java | Select-String -Pattern "instanceof"

# Review method return types
Get-ChildItem -Recurse src -Filter *.java | Select-String -Pattern "void\s+"
```

---

## 13. Design Decisions

- **List-based map storage**: `Environment` uses a `List<AbstractEntity>` instead of a 2D array. This simplifies dynamic management, movement logic, and avoids synchronization overhead between lists and matrices.
- **Plant vs Planet**: Used `Plant` as the canonical name (per package requirements), treating "Planet" in the assignment text as a typo.
- **Consumable vs Consumer**: Implemented the `Consumable` interface as per the required structural specifications.
- **Randomness**: Movement and reproduction utilize `java.util.Random`, so simulation outputs will naturally vary between runs.

---

## 14. Do Not Commit

The following files are local or generated artifacts and should not be tracked in the repository:
- `sources.txt`
- `out/`
- `*.class`
- `antigravity_assignment2_split_pack/`

---

## 15. Recommended Reading Order

To understand the project flow, read files in the following order:
1. `Position` -> `Environment`
2. `AbstractEntity` -> `LivingEntity` -> `StaticEntity`
3. `Resource`, `Rock`, `Water`
4. `Plant`, `OakTree`, `Flower`
5. `Animal`, `Lion`, `Deer`, `Rabbit`
6. `MovementStrategy`, `FeedingBehavior` and their concrete implementations
7. `SimulationEngine`
