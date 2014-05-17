/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.transmission;

import net.endofinternet.raymoon.lib.transmission.TransmissionException;
import net.endofinternet.raymoon.lib.transmission.Transmitter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author raymoon
 */
public class TransmitterTest {

    @Test
    public void correctlyTransmittedData() throws TransmissionException {
        byte[] data = new byte[10000];

        Random r = new Random();
        r.nextBytes(data);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);

        Transmitter.transmit(bis, bos);

        byte[] received = bos.toByteArray();
        Assert.assertTrue(data.length == received.length);
        for (int i = 0; i < data.length; i++) {
            Assert.assertTrue(received[i] == data[i]);
        }
    }
}
