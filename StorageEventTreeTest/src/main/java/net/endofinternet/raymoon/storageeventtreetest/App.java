package net.endofinternet.raymoon.storageeventtreetest;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        ServiceConsumer consumer = new ServiceConsumer();
        ServiceProvider provider = new ServiceProvider(consumer);
        consumer.setProvider(new ServiceProviderLogger(provider));

        int count = 10000;
        for (int i = 0; i < count; i++) {
            provider.put("" + i);
        }


        for (int i = 0; i < count; i++) {
            provider.remove("" + i);
        }
    }
}
