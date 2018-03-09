package eventing.stateless;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ForkJoinPool;

import eventing.EventType;

public class StatelessEventing {
	public static interface Builder<T extends EventType> {
		void perform(Handler<T> handler);
	}

	public static interface Handler<T extends EventType> {
		void handle(T event);
	}

	public <T extends EventType> Builder<T> on(Class<T> type) {
		return new Builder<T>() {

			public void perform(Handler<T> handler) {
				synchronized (handlers) {
					if (!handlers.containsKey(type)) {
						handlers.put(type, new LinkedList<>());
					}
					
					handlers.get(type).add((Handler<EventType>) handler);
				}
			}

		};
	}

	public <T extends EventType> void emit(T event) {
		synchronized (handlers) {
			for(Handler<EventType> handler : handlers.get(event.getClass())){
				Runnable runnable = () -> handler.handle(event);
				ForkJoinPool.commonPool().submit(runnable);
			}
		}
	}

	private final Map<Class<? extends EventType>, List<Handler<EventType>>> handlers = new HashMap<>();
}
