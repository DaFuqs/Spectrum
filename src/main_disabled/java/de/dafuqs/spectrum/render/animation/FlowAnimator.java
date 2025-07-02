package de.dafuqs.spectrum.render.animation;

import org.jetbrains.annotations.*;

import java.util.*;

/**
 * An FSM-based animation engine, for use primarily in procedural animation.
 * <p>
 * Especially oriented towards BERs, but generally useful.
 */
public class FlowAnimator {
	
	final Map<FlowState, StateInfo> trackedStates;
	final Set<FlowData<?>> liveData = new HashSet<>();
	private @NotNull StateInfo info;
	int interpProgress;
	
	private FlowAnimator(Map<FlowState, StateInfo> trackedStates, @NotNull FlowState initialState) {
		this.trackedStates = trackedStates;
		this.info = trackedStates.get(initialState);
		interpProgress = info.interpTime;
	}
	
	public void tick() {
		if (interpProgress > 0)
			interpProgress--;
	}
	
	/**
	 * Updates the animation data values of the state machine.
	 * @param time can be zeroed out if unnecessary.
	 */
	public void animate(float tickDelta, long time) {
		var delta = Math.clamp(1 - ((interpProgress - tickDelta) / info.interpTime), 0, 1);
		liveData.forEach(flowData -> flowData.update(delta, time));
	}
	
	/**
	 * Swaps to a new animation sate. Does nothing if already in the target state.
	 * <p>
	 * Notifies listeners of the change and updates State Info.
	 * @see FlowData#notifyStateChange(FlowState, boolean)
	 */
	public void swapState(@NotNull FlowState newState) {
		if (newState == info.state || !trackedStates.containsKey(newState))
			return;
		
		info = trackedStates.get(newState);
		liveData.forEach(flowData -> flowData.notifyStateChange(info.state, info.clearOnMiss));
		interpProgress = info.interpTime;
	}
	
	public static final class Builder<T> {
		private final List<DataSignature<?>> holder = new ArrayList<>();
		private final Map<FlowState, StateInfo> states = new HashMap<>();
		private final Class<T> clazz;
		
		public Builder(Class<T> clazz) {
			this.clazz = clazz;
			stateInfo(FlowStates.INIT, 0);
		}
		
		public Builder<T> stateInfo(FlowState state, int interpolationTime, boolean clearOnMiss) {
			states.put(state, new StateInfo(state, interpolationTime, clearOnMiss));
			return this;
		}
		
		public Builder<T> stateInfo(FlowState state, int interpolationTime) {
			return stateInfo(state, interpolationTime, false);
		}
		
		/**
		 * Commences creation of a {@link DataSignature} object.
		 * <p>
		 * <b>Exert care when inputting the handle reference. This string will be used to populate a matching field with a {@link FlowData} object
		 * upon animator instantiation.</b> The field in question must be of a FlowData with matching type, and its name must be the same as the reference prepended with an underscore.
		 * @param reference the matching field name, will be prepended with a "_" for population.
		 */
		public <N extends Number> DataBuilder<T, N> handle(String reference, FlowHandler<N> handler) {
			return new DataBuilder<>(this, reference, handler);
		}
		
		public Factory<T> build() {
			return new Factory<>(clazz, states, Collections.unmodifiableList(holder));
		}
		
		public static final class DataBuilder<T, N extends Number> {
			private final Builder<T> builder;
			private final Map<FlowState, KeyFrame<N>> stateHolder = new HashMap<>();
			private final String reference;
			private final FlowHandler<N> handler;
			private Interpolation interpolation = Interpolation.LINEAR;
			private KeyFrame<N> defaultKeyFrame;
			private N initialValue;
			
			public DataBuilder(Builder<T> builder, String reference, FlowHandler<N> handler) {
				this.builder = builder;
				this.reference = "_" + reference;
				this.handler = handler;
			}
			
			public DataBuilder<T, N> interpolate(Interpolation interpolation) {
				this.interpolation = interpolation;
				return this;
			}
			
			public DataBuilder<T, N> forStates(N keyFrame, FlowState... states) {
				return forStates(KeyFrame.simple(keyFrame), states);
			}
			
			public DataBuilder<T, N> loopback(FlowState... states) {
				if (defaultKeyFrame == null) {
					
					if (initialValue == null)
						throw new IllegalStateException("What the fuck. Like actually how.");
					
					defaultKeyFrame = KeyFrame.simple(initialValue);
				}
				return forStates(defaultKeyFrame, states);
			}
			
			public DataBuilder<T, N> forStates(KeyFrame<N> keyFrame, FlowState... states) {
				for (FlowState state : states) {
					stateHolder.put(state, keyFrame);
				}
				return this;
			}
			
			public DataBuilder<T, N> initial(N initialValue) {
				this.initialValue = initialValue;
				return this;
			}
			
			public DataBuilder<T, N> startingKeyFrame(KeyFrame<N> keyFrame) {
				this.defaultKeyFrame = keyFrame;
				return this;
			}
			
			public void push() {
				var clazz = builder.clazz;
				assert handler != null;
				assert interpolation != null;
				assert !stateHolder.isEmpty();
				
				if (defaultKeyFrame == null)
					defaultKeyFrame = KeyFrame.simple(initialValue);
				
				try {
					var field = clazz.getDeclaredField(reference);
					builder.holder.add(new DataSignature<>(field, handler, interpolation, initialValue, defaultKeyFrame, stateHolder));
				} catch (NoSuchFieldException e) {
					throw new NoSuchFieldError("Invalid animation target [" + reference + "] for " + clazz.getName());
				}
			}
		}
	}
	
	record StateInfo(FlowState state, int interpTime, boolean clearOnMiss) {}
	
	public static final class Factory<T> {
		
		private final Map<FlowState, StateInfo> stateRegistrar;
		private final List<DataSignature<?>> larvalData;
		private final Class<T> targetClazz;
		
		private Factory(Class<T> targetClazz, Map<FlowState, StateInfo> stateRegistrar, List<DataSignature<?>> larvalData) {
			this.stateRegistrar = Collections.unmodifiableMap(stateRegistrar);
			this.larvalData = larvalData;
			this.targetClazz = targetClazz;
		}
		
		public FlowAnimator create(@NotNull FlowState initialState, T instance) {
			if (!targetClazz.isInstance(instance))
				throw new IllegalStateException("Attempted to create an animator for an incompatible object");
			
			var animator = new FlowAnimator(stateRegistrar, initialState);
			
			for (DataSignature<?> signature : larvalData) {
				try {
					var data = signature.instantiate();
					signature.link(data, instance);
					animator.liveData.add(data);
					
				} catch (IllegalAccessException e) {
					throw new IllegalStateException("Failed to create flow data");
				}
			}
			
			return animator;
		}
	}
}