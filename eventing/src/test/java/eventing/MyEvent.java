package eventing;

public class MyEvent implements EventType {
	private final long timestamp;
	private final long value;

	public MyEvent(long value) {
		super();
		
		this.timestamp = System.currentTimeMillis();
		this.value = value;
	}

	
	
	public long getTimestamp() {
		return timestamp;
	}



	boolean hasStuff() {
		return true;
	}
}
