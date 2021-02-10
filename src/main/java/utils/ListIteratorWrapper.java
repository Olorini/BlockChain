package utils;

import java.util.ListIterator;

public class ListIteratorWrapper<T> {

	private final ListIterator<T> listIterator;

	public ListIteratorWrapper(ListIterator<T> listIterator) {
		this.listIterator = listIterator;
	}

	public boolean hasNext() {
		return listIterator.hasNext();
	}

	public T nextAfterPrevious() {
		listIterator.next();
		return listIterator.next();
	}

	public T previous() {
		return listIterator.previous();
	}
}