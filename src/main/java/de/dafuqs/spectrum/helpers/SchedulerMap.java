package de.dafuqs.spectrum.helpers;

import com.mojang.datafixers.util.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;

import java.util.*;
import java.util.stream.*;

// TODO: migrate to net.minecraft.world.tick ?
public class SchedulerMap<K> implements Iterable<Map.Entry<K, Integer>> {
	
    private final Map<K, Integer> map;

    public SchedulerMap() {
        this(new HashMap<>());
    }

    public SchedulerMap(Map<K, Integer> map) {
        this.map = map;
    }

    public void put(K val, int ticks) {
        this.map.put(val, ticks);
    }

    public void clear() {
        map.clear();
    }

    public boolean containsKey(K val) {
        return map.containsKey(val);
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public int get(K val) {
        return map.get(val);
    }

    public final String toString() {
        return map.toString();
    }

    public final int size() {
        return map.size();
    }

    public void tick() {
        if (!map.isEmpty()) {
            Iterator<Map.Entry<K, Integer>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<K, Integer> next = iterator.next();
                K key = next.getKey();
                if (key instanceof Freezable freezableTimer && freezableTimer.isFrozen()) {
                    continue;
                }

                if (next.getValue() >= 1) {
                    next.setValue(next.getValue() - 1);
                } else {
                    if (key instanceof SchedulerMap.Callback) {
                        ((Callback) key).trigger();
                    }
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public Iterator<Map.Entry<K, Integer>> iterator() {
        return map.entrySet().iterator();
    }

    public static <K> Codec<SchedulerMap<K>> getCodec(Codec<Pair<K, Integer>> entryCodec) {
		return Codec.list(entryCodec).xmap(list -> {
			var map = new HashMap<K, Integer>();
			list.forEach(p -> map.put(p.getFirst(), p.getSecond()));
			return new SchedulerMap<>(map);
		}, m -> m.map.entrySet().stream().map(e -> new Pair<>(e.getKey(), e.getValue())).toList());
    }
	
	public Map<K, Integer> getMap() {
		return map;
	}
	
	public interface Callback {
        void trigger();
    }

    public interface Freezable extends Callback {
        boolean isFrozen();
    }
}