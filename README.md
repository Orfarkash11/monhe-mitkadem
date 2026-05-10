# Ecosystem Simulation — Advanced OOP Assignment 2

פרויקט ג'אווה המדמה מערכת אקולוגית פשוטה על מפה דו-ממדית. הפרויקט נבנה במסגרת קורס תכנות מונחה עצמים מתקדם (Advanced OOP).

---

## 1. Quick Start / התחלה מהירה

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

## 2. Project Goal / מטרת הפרויקט

הפרויקט מדמה עולם המכיל חיות, צמחים ומשאבים על גבי רשת.
כל "פעימה" (Tick) מקדמת את הסימולציה ומאפשרת ליצורים לפעול.
המימוש מתמקד בעקרונות OOP מתקדמים: ירושה, ממשקים, פולימורפיזם, אנקפסולציה ושימוש בתבנית ה-Delegation עבור התנהגויות תנועה ותזונה.

---

## 3. Current Implementation Status / מצב המימוש

- [x] Required package structure: **done**
- [x] Core classes: **done**
- [x] Interfaces: **done**
- [x] Entity hierarchy: **done**
- [x] Animals: **done**
- [x] Plants: **done**
- [x] Resources: **done**
- [x] Behaviors/delegators: **done**
- [x] Environment: **done**
- [x] SimulationEngine: **done**
- [x] Plain Java only: **done**
- [x] No external libraries: **done**

---

## 4. How the Simulation Works / איך הסימולציה עובדת

1. **SimulationEngine** מאתחל או מקבל אובייקט **Environment**.
2. ה-**Environment** מנהל את כל היצורים (Entities) בעולם.
3. בכל פעימה (Tick) נוצר "צילום מצב" (Snapshot) של היצורים הקיימים.
4. כל יצור חי שמממש את ממשק ה-**Actable** מבצע את פעולת ה-`act(environment)`.
5. חיות יכולות לנוע, לאכול או להתרבות. צמחים יכולים לגדול ולהתרבות.
6. יצורים שמתים (energy=0) מוסרים מהעולם.
7. בסיום הפעימה מודפסת המפה המעודכנת וסיכום של אוכלוסיית העולם.

---

## 5. Package Guide / מדריך חבילות

| Package | Responsibility | Main files |
| :--- | :--- | :--- |
| `ecosystem.core` | ליבת המערכת וניהול העולם | `Position`, `Environment`, `SimulationEngine` |
| `ecosystem.interfaces` | הגדרת חוזים והתנהגויות | `Actable`, `Consumable`, `Movable`, `Eater` |
| `ecosystem.entities` | היררכיית הישויות הבסיסית | `AbstractEntity`, `LivingEntity`, `StaticEntity` |
| `ecosystem.entities.animals` | מימושי חיות | `Animal`, `Lion`, `Deer`, `Rabbit` |
| `ecosystem.entities.plants` | מימושי צמחים | `Plant`, `OakTree`, `Flower` |
| `ecosystem.entities.resources` | מימושי משאבים סטטיים | `Resource`, `Rock`, `Water` |
| `ecosystem.behaviors` | אסטרטגיות תנועה ותזונה | `MovementStrategy`, `FeedingBehavior` |

---

## 6. Core Classes / מחלקות הליבה

- **Position**: שומרת קואורדינטות (row/col). מחשבת מרחק מנהטן (Manhattan Distance) ומוודאת שערכים שליליים לא מוזנים בבנאי.
- **Environment**: מנהלת את גודל העולם ואת רשימת היצורים. בודקת מיקומים פנויים, מוסיפה/מסירה יצורים ומבצעת רינדור של המפה לטקסט.
  - **חשוב**: המפה אינה נשמרת ככפל דו-ממדי (2D Array). במקום זאת, לכל יצור יש מיקום, וה-Environment מחזיק רשימה פשוטה של יצורים. המפה נוצרת בזמן אמת מהרשימה.
- **SimulationEngine**: מנהל את הלופ המרכזי. דואג לסדר הפעולות בכל Tick, ניקוי יצורים מתים והדפסת פלט למשתמש.

---

## 7. Entity Hierarchy / היררכיית הישויות

```text
AbstractEntity
├── LivingEntity (has age/energy/maxEnergy)
│   ├── Animal (acts using delegators)
│   │   ├── Lion
│   │   ├── Deer
│   │   └── Rabbit
│   └── Plant (grows and reproduces)
│       ├── OakTree
│       └── Flower
└── StaticEntity
    └── Resource (static, no act)
        ├── Rock
        └── Water
```

---

## 8. Interfaces / ממשקים

| Interface | Used by | Purpose |
| :--- | :--- | :--- |
| `Actable` | `LivingEntity` | מאפשר ליצור לבצע פעולה בכל Tick |
| `Movable` | `Animal` | מאפשר ליצור לנוע במפה |
| `Consumable` | Resources, Animals, Plants | מאפשר ליצור להיאכל/להיצרך |
| `Eater` | `Animal` | מאפשר ליצור לצרוך `Consumable` |
| `Reproducible` | Plants, Rabbit | מאפשר ליצור להתרבות |
| `Sensory` | `Animal` | מאפשר ליצור לזהות יצורים סמוכים |
| `EdibleByCarnivore` | Prey / Carnivores | מסמן יצור כאכיל עבור טורפים |
| `EdibleByHerbivore` | Plants | מסמן יצור כאכיל עבור אוכלי עשב |

---

## 9. Behavior Delegators / האצלת סמכויות התנהגות

החיות לא מכילות את כל לוגיקת התנועה והאכילה בתוכן. במקום זאת, הן משתמשות באובייקטי אסטרטגיה:

### MovementStrategy
| Class | Behavior |
| :--- | :--- |
| `RandomMovement` | תנועה אקראית לתא פנוי סמוך |
| `ChaseMovement` | רדיפה אחרי מזון מתאים (טורפים) |
| `EscapeMovement` | בריחה מיצור מאיים סמוך |

### FeedingBehavior
| Class | Behavior |
| :--- | :--- |
| `CarnivoreBehavior` | אכילת יצורים מסוג `EdibleByCarnivore` |
| `HerbivoreBehavior` | אכילת יצורים מסוג `EdibleByHerbivore` |

---

## 10. Entity Symbols / סמלי היצורים

| Entity | Symbol | Entity | Symbol |
| :--- | :--- | :--- | :--- |
| Lion | **L** | Deer | **D** |
| Rabbit | **R** | OakTree | **T** |
| Flower | **F** | Rock | **X** |
| Water | **W** | Empty | **.** |

---

## 11. Where to Change What / מדריך לשינויים בקוד

| אם ברצונך לשנות... | ערוך את הקובץ/חבילה הבאים |
| :--- | :--- |
| גודל מפה / אחסון יצורים | `Environment` |
| לוגיקת הסימולציה וסיכומי ה-Tick | `SimulationEngine` |
| התנהגות בסיסית של חיות | `Animal` |
| התנהגות אריה (תנועה/אכילה) | `Lion` + `ChaseMovement` + `CarnivoreBehavior` |
| התנהגות צבי / ארנב | `Deer` / `Rabbit` + אסטרטגיות מתאימות |
| התרבות ארנבים | `Rabbit.reproduce()` |
| קצב גדילה של צמחים | `Plant` |
| התרבות עץ אלון / פרח | `OakTree` / `Flower` |
| חוקי תנועה כלליים | חבילת `ecosystem.behaviors` (מחלקות התנועה) |
| חוקי אכילה כלליים | חבילת `ecosystem.behaviors` (מחלקות האכילה) |
| סמלי היצורים | בנאים (Constructors) של הישויות הספציפיות |

---

## 12. Validation Commands / פקודות אימות

לפני הגשה, וודא שהקוד תקין בעזרת הפקודות הבאות:

### Static Checks (PowerShell)
בדוק שאין שדות `public` או `protected` ושהשימוש ב-`instanceof` מוצדק:
```powershell
Get-ChildItem -Recurse src -Filter *.java | Select-String -Pattern "public\s+.*\s*;"
Get-ChildItem -Recurse src -Filter *.java | Select-String -Pattern "protected\s+.*\s*;"
Get-ChildItem -Recurse src -Filter *.java | Select-String -Pattern "instanceof"
Get-ChildItem -Recurse src -Filter *.java | Select-String -Pattern "void\s+"
```

---

## 13. Design Decisions / החלטות עיצוב

- **List-based map storage**: העולם מנוהל כרשימה (`List`) ולא כמערך דו-ממדי כדי לפשט את הניהול הדינמי של היצורים ולמנוע בעיות סנכרון בין הרשימה למטריצה.
- **Plant vs Planet**: נעשה שימוש ב-`Plant` כפי שנדרש במבנה החבילות, למרות טעויות כתיב אפשריות בהוראות המטלה.
- **Randomness**: התנועה וההתרבות משתמשות ב-`Random`, לכן הפלט עשוי להשתנות בין ריצות שונות.

---

## 14. Do Not Commit / קבצים שאין להוסיף ל-Git

הקבצים הבאים הם קבצים זמניים או תוצרי קומפילציה ואין להעלותם למאגר המרכזי:
- `sources.txt`
- `out/` (תיקיית הפלט)
- `*.class`
- `antigravity_assignment2_split_pack/` (תיקיית ההוראות)

---

## 15. Recommended Reading Order / סדר קריאה מומלץ

כדי להבין את הפרויקט במהירות, מומלץ לעבור על הקבצים בסדר הבא:
1. `Position` -> `Environment`
2. `AbstractEntity` -> `LivingEntity` -> `StaticEntity`
3. `Resource` / `Rock` / `Water`
4. `Plant` / `OakTree` / `Flower`
5. `Animal` / `Lion` / `Deer` / `Rabbit`
6. `MovementStrategy` / `FeedingBehavior` -> מימושים קונקרטיים
7. `SimulationEngine`
