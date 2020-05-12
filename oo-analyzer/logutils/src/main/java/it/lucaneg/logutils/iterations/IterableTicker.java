package it.lucaneg.logutils.iterations;

import java.util.Iterator;

final class IterableTicker<E> implements ITicker, Iterable<E> {

	/**
	 * The iterator over whose elements this ticker iterates.
	 */

	private final Iterator<E> iterator;

	/**
	 * The ticker that gets decorated.
	 */

	private final Ticker backingTicker;

	/**
	 * Builds an iterator ticker from another ticker. It iterates
	 * over the elements of the given iterator.
	 *
	 * @param iterator the iterator
	 * @param backingTicker the original ticker
	 */

	IterableTicker(Iterator<E> iterator, Ticker backingTicker) {
		this.iterator = new Iterator<E>() {

			@Override
			public boolean hasNext() {
				if (iterator.hasNext())
					return true;
				else {
					off();
					return false;
				}
			}

			@Override
			public E next() {
				tick();

				return iterator.next();
			}

			@Override
			public void remove() {
				iterator.remove();
			}
		};

		this.backingTicker = backingTicker;

		on();
	}

	@Override
	public int getTicks() {
		return backingTicker.getTicks();
	}

	@Override
	public void on() {
		backingTicker.on();
	}

	@Override
	public void tick() {
		backingTicker.tick();
	}

	@Override
	public void off() {
		backingTicker.off();
	}

	@Override
	public Iterator<E> iterator() {
		return iterator;
	}
}
