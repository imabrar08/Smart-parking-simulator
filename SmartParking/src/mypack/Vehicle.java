package mypack;

/**
 * A vehicle registered by a Student. The vehicle's department zone is taken
 * from the student's branch using the Department enum (Experiment 9).
 */
public class Vehicle {

    public static final int NOT_PARKED = -1;

    private final Student owner;
    private final String number;
    private final VehicleType type;
    private final Department department;
    private int slotId = NOT_PARKED;

    public Vehicle(Student owner, String number, VehicleType type) {
        this.owner = owner;
        this.number = number;
        this.type = type;
        this.department = Department.valueOf(owner.getBranch());
    }

    public Student getOwner() {
        return owner;
    }

    public String getNumber() {
        return number;
    }

    public VehicleType getType() {
        return type;
    }

    public Department getDepartment() {
        return department;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public boolean isParked() {
        return slotId != NOT_PARKED;
    }

    @Override
    public String toString() {
        return number + " (" + type.getLabel() + ", " + owner.getName() + ", " + department + ")";
    }
}
