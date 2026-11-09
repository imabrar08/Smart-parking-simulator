import mypack.*;   // Experiment 6 - the package 'mypack' is imported here

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Campus Smart Parking Simulator - entry point.
 *
 *   java Main            -> interactive menu
 *   java Main --demo     -> runs all 10 experiments + micro project without pauses
 */
public class Main {

    public static void main(String[] args) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        if (args.length > 0 && args[0].equalsIgnoreCase("--demo")) {
            new ExperimentDemo(in, false).runAll();
            return;
        }
        new ParkingApp(in).run();
    }
}
