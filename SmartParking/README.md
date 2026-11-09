# Campus Smart Parking Simulator (Java OOP Micro Project)

A software-only parking simulator for a college campus. Students register their vehicles, vehicles park in
their department's zone on a 2-floor grid, and the app bills them when they leave. No hardware is needed.
**Every one of the 10 lab experiments is used in a real feature of the project.**

## How to run

### VS Code
1. Install **JDK 17 or newer** and the **Extension Pack for Java** (Microsoft).
2. `File > Open Folder...` and choose the `SmartParking` folder.
3. Open `src/Main.java` and click **Run** above `main`, or press **F5** and choose
   *Smart Parking (menu)* or *Smart Parking (full demo)*.

### Terminal (Windows / Linux / macOS)
```
run.bat            (Windows)         ./run.sh           (Linux / macOS)
run.bat --demo                       ./run.sh --demo
```
or manually:
```
javac -d bin src/mypack/*.java src/*.java
java -cp bin Main            # interactive menu
java -cp bin Main --demo     # all 10 experiments + micro project, no pauses
```

Fastest way to show the examiner everything: run the menu and choose **13 (Run FULL DEMO)**.

## Folder structure
```
SmartParking/
  src/
    mypack/    Department, DivisionByZeroException, IntStack, MatrixUtil, SafeMath,
               Student, Vehicle, VehicleType                     (Experiment 6 package)
    Main, ParkingApp, ExperimentDemo, ParkingLot, SampleData,
    Shape, Circle, Square, Triangle, Resizable, Rectangle, ParkingZone,
    MyThread, FeeCalculator
  .vscode/     settings.json, launch.json
  run.bat, run.sh
```

## Experiment to feature mapping (put this table in your report)

| # | Experiment | Class / method | Used in the project for |
|---|-----------|----------------|--------------------------|
| 1 | Add two matrices of order N | `mypack.MatrixUtil.addMatrices()`, `ParkingLot.combinedOccupancy()` | Floor 1 + Floor 2 occupancy matrices (N = 4) give the combined occupancy |
| 2 | Stack of max 10 integers | `mypack.IntStack` (`push`, `pop`, `peek`, `display`) | Remembers the last 10 parked slot numbers; menu option 6 undoes the last parking |
| 3 | Student class, N objects, headings | `mypack.Student` (`printHeader`, `display`) | Vehicle owners; option 1 registers N students |
| 4 | Shape + Circle / Triangle / Square, polymorphism | `Shape`, `Circle`, `Square`, `Triangle` (`draw`, `erase`) | Bike = circle, car = square, bus/truck = triangle, drawn on the map when parking and erased on exit |
| 5 | Resizable interface + Rectangle | `Resizable`, `Rectangle`, `ParkingZone extends Rectangle` | Option 10 resizes a department's zone (width = slots, height = floors) |
| 6 | Package `mypack` imported and used | `import mypack.*;` in `Main` | All model, stack and utility classes live in `mypack` |
| 7 | Custom exception DivisionByZero (try, catch, throw, finally) | `mypack.DivisionByZeroException`, `SafeMath.divide()`, `calculateAverage()` | A zone with 0 slots, or no bills yet, raises the exception in analytics (option 11) |
| 8 | MyThread, `super(...)`, `start()`, `run()` | `MyThread` | Each arriving vehicle is a thread; the main thread monitors the gate at the same time (option 9) |
| 9 | enum Department + `valueOf()` | `mypack.Department`, `Department.valueOf(text)` | The typed department selects the parking zone |
| 10 | Strings to wrapper objects, autoboxing / unboxing | `FeeCalculator` (`Integer.valueOf`, `Integer`/`Double` arithmetic) | Hours typed as text are converted and used to calculate fees, totals and averages (options 5 and 12) |
| 11 | Micro project | the whole application | Campus Smart Parking Simulator |

## Menu
```
 1 Register N students      2 Load sample data     3 Show students
 4 Park a vehicle           5 Exit and bill       6 Undo last parking
 7 Show map + matrix        8 Show stack          9 Concurrent arrivals (threads)
10 Resize a zone           11 Analytics           12 Bill three vehicles
13 FULL DEMO of all experiments                    0 Exit
```
Billing rates: Bike Rs.10/hour, Car Rs.30/hour, Bus/Truck Rs.60/hour (`VehicleType`).

## Likely viva questions
- **Why `synchronized` in `ParkingLot`?** Several vehicle threads can park at the same time; synchronized methods stop two threads from taking the same slot.
- **Interface vs abstract class?** `Resizable` is an interface (a capability that any class can add); `Shape` is an abstract class (shared data and partial behaviour).
- **Where is polymorphism?** `Shape s = new Circle(); s.draw(canvas);` - the same call runs a different `draw()` per sub class.
- **Why is `DivisionByZeroException` a checked exception?** It extends `Exception`, so callers must catch or declare it. This makes the error handling explicit.
- **What does `finally` do?** It always runs (here: a closing message), whether or not the exception happened.
- **Autoboxing vs unboxing?** `Integer total = h1 + h2;` unboxes h1 and h2 to `int`, adds them, then boxes the result into an `Integer`.
- **Why `Thread` created with `super(name)`?** The constructor of the base class `Thread` sets the thread's name; `start()` then calls `run()` in a new thread.
- **Why does `Department.valueOf("xyz")` fail?** It throws `IllegalArgumentException` when no constant matches; the program catches it and asks again.

## Report outline (about 6 to 8 pages)
1. Title, team members, USNs, guide  2. Problem statement and objectives  3. Software requirements (JDK, VS Code)
4. System design - class diagram and the mapping table above  5. Implementation - key code snippets per experiment
6. Output screenshots (menu, map, bill, invalid inputs, exception)  7. Conclusion and future scope (GUI, database, QR tickets)
