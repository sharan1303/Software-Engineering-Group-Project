package uk.ac.soton.git.comp2211g17.viewmodel.util;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.List;

/**
 * An entry in a {@link VirtualObservableList}
 * This is used because we can't implement ListChangeListener on VirtualObservableList without something to keep track
 * of what the removed data was - so we store the data in VirtualListEntry objects and don't replace them.
 * As these objects are held only by WeakReference, it is safe to recreate them in a ListChangeListener.wasRemoved call, as we know they can't
 * be compared with objects that don't exist.
 */
public class VirtualListEntry<T> implements ObservableValue<T> {
	private final int index;
	private final VirtualListCacher<T> backingList;
	private T cachedValue;

	private List<ChangeListener<? super T>> changeListeners;
	private List<InvalidationListener> invalidationListeners;

	protected VirtualListEntry(int index, VirtualListCacher<T> backingList) {
		this.index = index;
		this.backingList = backingList;
	}

	@Override
	public T getValue() {
		if (cachedValue == null) {
			cachedValue = backingList.get(index);
		}
		return cachedValue;
	}

	protected void invalidate() {
		T oldValue = this.cachedValue;
		this.cachedValue = null;

		if (changeListeners != null) {
			for (ChangeListener<? super T> listener : changeListeners) {
				listener.changed(this, oldValue, getValue());
			}
		}
		if (invalidationListeners != null) {
			for (InvalidationListener listener : invalidationListeners) {
				listener.invalidated(this);
			}
		}
	}

	@Override
	public void addListener(ChangeListener<? super T> listener) {
		if (changeListeners == null) {
			changeListeners = new ArrayList<>();
		}
		changeListeners.add(listener);
	}

	@Override
	public void removeListener(ChangeListener<? super T> listener) {
		if (changeListeners != null) {
			changeListeners.remove(listener);
		}
	}

	@Override
	public void addListener(InvalidationListener listener) {
		if (invalidationListeners == null) {
			invalidationListeners = new ArrayList<>();
		}
		invalidationListeners.add(listener);
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		if (invalidationListeners != null) {
			invalidationListeners.remove(listener);
		}
	}
}
