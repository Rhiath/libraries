package eventing;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import eventing.stateless.StatelessEventing;

public class TEST {
	@Test
	public void testEventIsProvidedOnce() throws InterruptedException {
		StatelessEventing e = new StatelessEventing();

		AtomicInteger counter = new AtomicInteger(0);

		MyEvent event = new MyEvent(5);
		e.on(MyEvent.class).perform((t) -> {
			if (t == event)
				counter.incrementAndGet();

			long timeDelta = System.currentTimeMillis() - t.getTimestamp();
			System.out.println("received: " + new Gson().toJson(t) + ", time since event was created: "
					+ (timeDelta + " msec"));
		});

		e.emit(event);

		TimeUnit.SECONDS.sleep(1);

		Assert.assertTrue(counter.get() == 1);
	}

	@Test
	public void testThrougput() throws InterruptedException {
		StatelessEventing e = new StatelessEventing();

		AtomicInteger counter = new AtomicInteger(0);

		e.on(MyEvent.class).perform((t) -> {
			counter.incrementAndGet();
		});

		int numberOfEvents = 1_000_000;
		for (int i = 0; i < numberOfEvents; i++) {
			e.emit(new MyEvent(i));
		}

		TimeUnit.SECONDS.sleep(1);
		
		System.out.println(counter.get()+" events received");

		Assert.assertTrue(counter.get() == numberOfEvents);
	}
	

	@Test
	public void testDelay() throws InterruptedException {
		StatelessEventing e = new StatelessEventing();

		AtomicLong sumOfAllDelays = new AtomicLong(0);

		e.on(MyEvent.class).perform((t) -> {
			sumOfAllDelays.addAndGet(System.currentTimeMillis() - t.getTimestamp());
		});

		int numberOfEvents = 1_000_000;
		for (int i = 0; i < numberOfEvents; i++) {
			e.emit(new MyEvent(i));
		}

		TimeUnit.SECONDS.sleep(1);
		
		double delayPerEvent = (double) sumOfAllDelays.get() / (double) numberOfEvents;
		
		System.out.println(delayPerEvent+" msec delay on average");

		Assert.assertTrue(delayPerEvent < 5);
	}
}
