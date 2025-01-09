package eecs1021;

import org.firmata4j.IODevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;

import java.io.IOException;
public class OledDisplay {public static void main(String[] args)
        throws InterruptedException, IOException {
    String IyinsPort = "/dev/cu.usbserial-0001";
    IODevice myGroveBoard = new FirmataDevice(IyinsPort);

    try {
        myGroveBoard.start();
        myGroveBoard.ensureInitializationIsDone();

        var GroveSensor = myGroveBoard.getPin(15);
        GroveSensor.setMode(Pin.Mode.ANALOG);

        var myPump = myGroveBoard.getPin(7);
        myPump.setMode(Pin.Mode.OUTPUT);

        if (GroveSensor.getValue() >= 400) {
            try {
                myPump.setValue(1);
            } catch (Exception ex) {

            }
        } else {
            System.out.println(GroveSensor.getValue());
        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }
}
}
