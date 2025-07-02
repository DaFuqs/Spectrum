package de.dafuqs.spectrum.render.animation;

import org.jetbrains.annotations.*;

import java.util.*;

/**
 * FlowData objects contain all the dynamic animation data of a field.
 * They are in practice disposable instances of {@link DataSignature}, and the two are closely intertwined.
 * <p><p>
 * <i>There is no need to keep track of this throughout construction or initialization of {@link FlowAnimator}.
 * As long as the reference string is correct, the corresponding field will be populated via reflection.</i>
 * @see de.dafuqs.spectrum.render.animation.FlowAnimator.Builder#handle(String, FlowHandler) 
 */
public final class FlowData<N extends Number> {
	
	private final DataSignature<N> signature;
	private final boolean valueTarget;
	private final HashMap<FlowState, KeyFrame<N>> stateListeners = new HashMap<>();
	private N value;
	private KeyFrame<N> nextKeyFrame, pastKeyFrame;
	
	private FlowData(DataSignature<N> signature, boolean valueTarget) {
		this.signature = signature;
		this.valueTarget = valueTarget;
		setAll(signature.initialValue);
	}
	
	public static <N extends Number> FlowData<N> create(DataSignature<N> signature, boolean valueTarget) {
		return new FlowData<>(signature, valueTarget);
	}
	
	public static <N extends Number> FlowData<N> create(DataSignature<N> signature) {
		return create(signature, true);
	}
	
	public N get() {
		return value;
	}
	
	public N last(float delta, long time) {
		return pastKeyFrame.at(delta, time);
	}
	
	public N target(float delta, long time) {
		return nextKeyFrame.at(delta, time);
	}
	
	public void setAll(N newValue) {
		this.value = newValue;
		var keyFrame = KeyFrame.simple(newValue);
		this.pastKeyFrame = keyFrame;
		this.nextKeyFrame = keyFrame;
	}
	
	public void setNextKeyFrame(KeyFrame<N> keyFrame) {
		if (valueTarget) {
			this.pastKeyFrame = KeyFrame.simple(value);
			this.nextKeyFrame = keyFrame;
		} else {
			this.pastKeyFrame = nextKeyFrame;
			this.nextKeyFrame = keyFrame;
		}
	}
	
	public void clear(boolean hard) {
		if (hard) {
			value = signature.initialValue;
			pastKeyFrame = signature.defaultKeyFrame;
			nextKeyFrame = signature.defaultKeyFrame;
		} else {
			setNextKeyFrame(signature.defaultKeyFrame);
		}
	}
	
	public void update(float delta, long time) {
		value = signature.handler.interpolate(signature.interpolation, pastKeyFrame.at(delta, time), nextKeyFrame.at(delta, time), delta, time);
	}
	
	public FlowHandler<N> getHandler() {
		return signature.handler;
	}
	
	public void addStateListener(@NotNull FlowState state, KeyFrame<N> target) {
		stateListeners.put(state, target);
	}
	
	public void notifyStateChange(@NotNull FlowState state, boolean clearOnMiss) {
		if (clearOnMiss) {
			setNextKeyFrame(stateListeners.getOrDefault(state, signature.defaultKeyFrame));
			return;
		}
		
		if (stateListeners.containsKey(state))
			setNextKeyFrame(stateListeners.get(state));
	}
	
	public static <N extends Number> FlowData<N> NULL() {
		return new FlowData<N>(DataSignature.dummy(), true);
	}
}
