/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.lib.transmission;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import net.endofinternet.raymoon.lib.logging.Logging;

/**
 * transfers all data from the input to the output
 * 
 * @author raymoon
 */
public class Transmitter {

    private static class NullTransmissionsObserver implements TransmissionObserver {

        public void dataTransmitted(long byteCount) {
            // ignore, null object pattern
        }

        public void transmissionFinished() {
            // ignore, null object pattern
        }
    }

    public static void transmit(InputStream is, OutputStream os) throws TransmissionException {
        transmit(is, os, new NullTransmissionsObserver());
    }

    public static void transmit(InputStream is, OutputStream os, TransmissionObserver observer) throws TransmissionException {
        byte[] buffer = new byte[1024];
        int numberOfBytesRead = 0;

        try {
            while (numberOfBytesRead >= 0) {
                numberOfBytesRead = is.read(buffer);
                if (numberOfBytesRead > 0) {
                    os.write(buffer, 0, numberOfBytesRead);
                    observer.dataTransmitted(numberOfBytesRead);
                }
            }
        } catch (IOException ex) {
            throw new TransmissionException("error for transmitting all data from input to output", ex);
        } finally {
            observer.transmissionFinished();
            try {
                is.close();
            } catch (IOException ex) {
                Logging.warn(Transmitter.class, "failed to close input stream", ex);
            }
            try {
                os.close();
            } catch (IOException ex) {
                Logging.warn(Transmitter.class, "failed to close output stream", ex);
            }
        }
    }
}
