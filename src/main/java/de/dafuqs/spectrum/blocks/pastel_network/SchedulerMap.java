package de.dafuqs.spectrum.blocks.pastel_network;

import java.util.*;

public class SchedulerMap<V> {

	private final Map<V, Integer> timer;

	public SchedulerMap() {
		this(new HashMap());
	}

	public SchedulerMap(Map<V, Integer> map) {
		timer = map;
	}

	public void put(V val, int time) {
		this.timer.put(val, time);
	}
	
	public void clear() {
		timer.clear();
	}

	public boolean containsKey(V val) {
		return timer.containsKey(val);
	}

	public boolean isEmpty() {
		return timer.isEmpty();
	}
	
	public int get(V val) {
		return timer.get(val);
	}
	
	public final String toString() {
		return timer.toString();
	}

	public final int size() {
		return timer.size();
	}
	
	public void tick() {
		if(!timer.isEmpty()) {
			Iterator<Map.Entry<V, Integer>> it = timer.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<V, Integer> e = it.next();
				V key = e.getKey();
				if (key instanceof Freezable freezableTimer && freezableTimer.isFrozen()) {
					continue;
				}
				
				if (e.getValue() >= 1) {
					e.setValue(e.getValue()-1);
				} else {
					if (key instanceof SchedulerMap.Callback) {
						((Callback)key).call();
					}
					it.remove();
				}
			}
		}
	}

	public interface Callback {
		void call();
	}

	public interface Freezable extends Callback {
		boolean isFrozen();
	}

}