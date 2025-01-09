package eecs1021;

import edu.princeton.cs.introcs.StdDraw;
import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;

import java.util.HashMap;
import java.util.TimerTask;

public class MoistureSensor extends TimerTask {

    private final SSD1306 OLEDDISPLAY;
    private final Pin GroveSensor;
    private final Pin ArduinoPump;
    private final Pin buttonPin;
    private int counter = 1;
    private boolean isPumpOffDueToButton = false;

    public MoistureSensor(SSD1306 OLEDDISPLAY, Pin GroveSensor, Pin ArduinoPump, Pin buttonPin) {
        this.OLEDDISPLAY = OLEDDISPLAY;
        this.GroveSensor = GroveSensor;
        this.ArduinoPump = ArduinoPump;
        this.buttonPin = buttonPin;
    }

    @Override
    public void run() {
        try {
            long buttonState = buttonPin.getValue(); //  button state
            long moistureValue = GroveSensor.getValue(); // Get the moisture sensor value

            System.out.println("Button State in MoistureSensor: " + buttonState); // Debugging line
            System.out.println("Moisture Value: " + moistureValue); // Debugging line

            // Check if the button is pressed
            if (buttonState == 1) { // Button is pressed equals turn pump off
                System.out.println("Button is pressed, pump will not turn on.");
                ArduinoPump.setValue(0); //  pump is off
                isPumpOffDueToButton = true; //
            } else {
                // Button is not pressed, handle moisture levels
                if (isPumpOffDueToButton) {
                    // If the pump was turned off due to button, keep it off
                    ArduinoPump.setValue(0);
                } else if (moistureValue >= 700) {  // Moisture level is too low, turn on the pump
                    String currentMoisture = String.valueOf(moistureValue);
                    OLEDDISPLAY.getCanvas().clear();
                    OLEDDISPLAY.getCanvas().setTextsize(1);
                    OLEDDISPLAY.getCanvas().drawString(0, 0, currentMoisture + " Moisture is too low");
                    OLEDDISPLAY.display();
                    ArduinoPump.setValue(1); // Turn ON the pump
                } else { // Moisture level is sufficient
                    String currentMoisture = String.valueOf(moistureValue);
                    OLEDDISPLAY.getCanvas().clear();
                    OLEDDISPLAY.getCanvas().setTextsize(1);
                    OLEDDISPLAY.getCanvas().drawString(0, 0, currentMoisture + " Moisture level is good");
                    OLEDDISPLAY.display();
                    ArduinoPump.setValue(0); // Turn pump off
                }
                isPumpOffDueToButton = false;
            }

            // Graph plotting
            HashMap<Integer, Double> finalGraph = new HashMap<>();
            finalGraph.put(counter, (double) moistureValue * 5 / 1023);

            StdDraw.setXscale(0, 100);
            StdDraw.setYscale(0, 5);

            StdDraw.setPenRadius(0.009);
            StdDraw.setPenColor(StdDraw.RED);

            StdDraw.line(0, 0, 0, 5);
            StdDraw.line(0, 0, 80, 0);
            StdDraw.text(-1.55, 4.5, "[V]");
            StdDraw.text(30, -0.20, "Time[sec]");
            StdDraw.text(30, 5, "Moisture Vs. Time ");
            StdDraw.text(0, -0.20, "0");
            StdDraw.text(-1.55, 0, "0");
            StdDraw.text(60, -0.20, "100");
            StdDraw.text(-1.55, 5, "5");
            finalGraph.forEach((xValue, yValue) -> StdDraw.text(xValue, yValue, "*"));
            counter++;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}