package de.dafuqs.spectrum.registries;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class DeferredRegistrar {
	
	private final ArrayList<Runnable> deferred = new ArrayList<>();
	
	public void flush() {
		deferred.forEach(Runnable::run);
		deferred.clear();
		deferred.trimToSize();
	}
	
	public <T> T defer(T value, Consumer<T> callback) {
		deferred.add(() -> callback.accept(value));
		return value;
	}
	
	public void defer(Runnable callback) {
		deferred.add(callback);
	}
	
	public static <T> Chain<T> chain(T value) {
		return new Chain<T>(value);
	}
	
	public record Chain<T>(T value) {
		
		public void defer(DeferredRegistrar registrar, Consumer<T> callback) {
			registrar.defer(() -> callback.accept(value));
		}
		
		public <D> void defer(DeferredRegistrar.Contextual<D> registrar, BiConsumer<D, T> callback) {
			registrar.defer(ctx -> callback.accept(ctx, value));
		}
		
		public <D> void defer(DeferredRegistrar.KeyedContextual<T, D> registrar, BiConsumer<T, D> callback) {
			registrar.defer(value, callback);
		}
		
	}
	
	public static class Contextual<D> {
		
		private ArrayList<Consumer<D>> deferred;
		
		public Contextual() {
			this(true);
		}
		
		public Contextual(boolean active) {
			this.deferred = active ? new ArrayList<>() : null;
		}
		
		public void flush(D data) {
			if (this.deferred != null) {
				deferred.forEach(c -> c.accept(data));
				deferred = new ArrayList<>();
			}
		}
		
		public void defer(Consumer<D> callback) {
			if (deferred != null) {
				deferred.add(callback);
			}
		}
		
	}
	
	public static class KeyedContextual<T, D> {
		
		private HashMap<T, BiConsumer<T, D>> deferred;
		
		public KeyedContextual() {
			this(true);
		}
		
		public KeyedContextual(boolean active) {
			this.deferred = active ? new HashMap<>() : null;
		}
		
		public void flush(D data) {
			if (this.deferred != null) {
				deferred.forEach((value, consumer) -> consumer.accept(value, data));
				deferred = new HashMap<>();
			}
		}
		
		public Stream<T> streamKeys() {
			return deferred == null ? Stream.empty() : deferred.keySet().stream();
		}
		
		public T defer(T value, BiConsumer<T, D> callback) {
			if (deferred != null) {
				deferred.put(value, callback);
			}
			return value;
		}
		
	}
	
}
