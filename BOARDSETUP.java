package eecs1021;

import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class BOARDSETUP {

    // Pin definitions
    static final int D2 = 2;  // Pump
    static final int A0 = 14; // Moisture Sensor
    static final byte I2C0 = 0x3C; // OLED Display address
    static final int D6 = 6;  // Button

    public static void main(String[] args) throws InterruptedException, IOException {
        // Initialize Firmata device
        FirmataDevice device = new FirmataDevice("/dev/cu.usbserial-0001"); // Change to your serial port
        device.start();
        device.ensureInitializationIsDone();

        // Initialize pins
        Pin pumpPin = device.getPin(D2);
        pumpPin.setMode(Pin.Mode.OUTPUT);

        Pin sensorPin = device.getPin(A0);
        sensorPin.setMode(Pin.Mode.ANALOG);

        Pin buttonPin = device.getPin(D6);
        buttonPin.setMode(Pin.Mode.INPUT);

        // Set up the OLED display
        SSD1306 oledDisplay = new SSD1306(device.getI2CDevice(I2C0), SSD1306.Size.SSD1306_128_64);
        oledDisplay.init();

        // Create Timer and schedule tasks
        Timer timer = new Timer();

        // Schedule MoistureSensor task
        TimerTask moistureSensorTask = new MoistureSensor(oledDisplay, sensorPin, pumpPin, buttonPin);
        timer.scheduleAtFixedRate(moistureSensorTask, 0, 1000); // Every second

        // Schedule ButtonControlTask
        TimerTask buttonControlTask = new ButtonControlTask(buttonPin, pumpPin);
        timer.scheduleAtFixedRate(buttonControlTask, 0, 100); // Every 100 milliseconds
    }
}