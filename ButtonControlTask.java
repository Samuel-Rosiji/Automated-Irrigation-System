package eecs1021;

import org.firmata4j.Pin;
import java.util.TimerTask;

public class ButtonControlTask extends TimerTask {
    private final Pin buttonPin;
    private final Pin ArduinoPump;

    public ButtonControlTask(Pin buttonPin, Pin pumpPin) {
        this.buttonPin = buttonPin;
        this.ArduinoPump = pumpPin;
    }

    @Override
    public void run() {
        try {
            long buttonState = buttonPin.getValue(); // Get the button state
            System.out.println("Button State: " + buttonState); // Debugging line

            if (buttonState == 1) {
                ArduinoPump.setValue(0); // Turn off the pump when the button is pressed
                System.out.println("Pump turned off"); // Debugging line
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}