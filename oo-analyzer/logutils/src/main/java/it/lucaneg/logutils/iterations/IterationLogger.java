package it.lucaneg.logutils.iterations;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class IterationLogger {

	private final String message, objects;
	private final Logger logger;

	public IterationLogger(Logger logger, String message, String objects) {
		this.logger = logger;
		this.message = message;
		this.objects = objects;
	}
	
	public <E> Iterable<E> iterate(E[] array) {
		return iterate(Level.INFO, new IterableFromArray<>(array).iterator(), array.length);
	}

	public <E> Iterable<E> iterate(Level level, E[] array) {
		return iterate(level, new IterableFromArray<>(array).iterator(), array.length);
	}

	public <E> Iterable<E> iterate(Collection<E> collection) {
		return iterate(Level.INFO, collection.iterator(), collection.size());
	}

	public <E> Iterable<E> iterate(Level level, Collection<E> collection) {
		return iterate(level, collection.iterator(), collection.size());
	}
	
	public <E> Iterable<E> iterate(Iterable<E> iterable) {
		return iterate(Level.INFO, iterable);
	}

	public <E> Iterable<E> iterate(Level level, Iterable<E> iterable) {
		int size = 0;
		for (@SuppressWarnings("unused") E e: iterable)
			size++;
		
		return iterate(level, iterable.iterator(), size);
	}
	
	public <E> Iterable<E> iterate(Stream<E> stream) {
		return iterate(Level.INFO, stream);
	}
	
	public <E> Iterable<E> iterate(Level level, Stream<E> stream) {
		List<E> list = stream.collect(Collectors.toList());
		return iterate(level, list.iterator(), list.size());
	}
	
	private <E> Iterable<E> iterate(Level level, Iterator<E> it, int size){
		return new IterableTicker<>(it, getTicker(level, size));
	}
	
	public <E> Stream<E> stream(E[] array) {
		return stream(Level.INFO, Arrays.stream(array), array.length);
	}

	public <E> Stream<E> stream(Level level, E[] array) {
		return stream(level, Arrays.stream(array), array.length);
	}
	
	public <E> Stream<E> stream(Collection<E> collection) {
		return stream(Level.INFO, collection.stream(), collection.size());
	}

	public <E> Stream<E> stream(Level level, Collection<E> collection) {
		return stream(level, collection.stream(), collection.size());
	}
	
	public <E> Stream<E> stream(Iterable<E> iterable) {
		return stream(Level.INFO, iterable);
	}

	public <E> Stream<E> stream(Level level, Iterable<E> iterable) {
		int size = 0;
		for (@SuppressWarnings("unused") E e: iterable)
			size++;
		
		return stream(level, StreamSupport.stream(iterable.spliterator(), false), size);
	}
	
	public <E> Stream<E> stream(Stream<E> stream) {
		return stream(Level.INFO, stream);
	}
	
	public <E> Stream<E> stream(Level level, Stream<E> stream) {
		List<E> list = stream.collect(Collectors.toList());
		return stream(level, stream, list.size());
	}

	private <E> Stream<E> stream(Level level, Stream<E> stream, int size) {
		Ticker ticker = getTicker(level, size);
		if (size > 0)
			ticker.on();

		AtomicInteger counter = new AtomicInteger();
		return stream.peek(e -> {
				ticker.tick();
				if (counter.incrementAndGet() == size)
					ticker.off();
			});
	}

	public final Ticker getManualTicker() {
		return getTicker(Level.INFO);
	}
	
	public final Ticker getManualTicker(Level level) {
		return getTicker(level);
	}
	
	private Ticker getTicker(Level level) {
		return new Ticker(logger, level, message, objects);
	}
	
	private Ticker getTicker(Level level, int size) {
		return getTicker(level, size, 0.1);
	}
	
	private Ticker getTicker(Level level, int size, double updatePercentage) {
		return new Ticker(logger, level, message, objects, size, updatePercentage);
	}
	
	private class IterableFromArray<E> implements Iterable<E> {

		private final E[] array;

		public IterableFromArray(E[] array) {
			this.array = array;
		}

		@Override
		public Iterator<E> iterator() {
			return new IteratorFromArray();
		}

		private class IteratorFromArray implements Iterator<E> {

			private int pos;

			private IteratorFromArray() {}

			@Override
			public boolean hasNext() {
				return pos < array.length;
			}

			@Override
			public E next() {
				return array[pos++];
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		}
	}
}
