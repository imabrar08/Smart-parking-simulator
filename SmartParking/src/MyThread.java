/**
 * Experiment 8 - class MyThread with a class constructor that calls the base
 * class constructor using super, and starts the thread. The run() method
 * starts after that. Each arriving vehicle is its own child thread, so the
 * main thread (gate monitor) and the child threads execute concurrently.
 */
public class MyThread extends Thread {

    private final ParkingLot lot;
    private final mypack.Vehicle vehicle;
    private final int delayMs;

    public MyThread(String threadName, ParkingLot lot, mypack.Vehicle vehicle, int delayMs) {
        super(threadName);          // call the base class (Thread) constructor using super
        this.lot = lot;
        this.vehicle = vehicle;
        this.delayMs = delayMs;
        start();                    // start the thread -> run() begins concurrently
    }

    @Override
    public void run() {
        System.out.println("   [" + getName() + "] arrived at the gate, driving to the zone...");
        try {
            Thread.sleep(delayMs);  // time taken to reach the slot
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        int slot = lot.park(vehicle);
        if (slot == ParkingLot.ZONE_FULL) {
            System.out.println("   [" + getName() + "] NO FREE SLOT in " + vehicle.getDepartment() + " zone");
        } else if (slot == ParkingLot.ALREADY_PARKED) {
            System.out.println("   [" + getName() + "] is already parked");
        } else {
            System.out.println("   [" + getName() + "] PARKED at " + lot.slotLabel(slot) + "  " + lot.shapeAt(slot).describe());
        }
    }
}
