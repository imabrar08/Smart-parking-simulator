package mypack;

/** Vehicle categories with their parking rate (Rs. per hour). */
public enum VehicleType {
    BIKE("Bike", 10),
    CAR("Car", 30),
    BUS("Bus/Truck", 60);

    private final String label;
    private final int ratePerHour;

    VehicleType(String label, int ratePerHour) {
        this.label = label;
        this.ratePerHour = ratePerHour;
    }

    public String getLabel() {
        return label;
    }

    public int getRatePerHour() {
        return ratePerHour;
    }
}
