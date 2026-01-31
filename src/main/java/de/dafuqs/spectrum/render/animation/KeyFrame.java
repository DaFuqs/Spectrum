package de.dafuqs.spectrum.render.animation;

@FunctionalInterface
public interface KeyFrame<N extends Number> {
	N at(float tickDelta, long time);
	
	static <N extends Number> KeyFrame<N> simple(N number) {
		return ((tickDelta, time) -> number);
	}
	
	static KeyFrame<Float> sine(float frequency, float offset) {
		return sine(frequency, 1, offset);
	}
	
	static KeyFrame<Float> sine(float frequency, float amplitude, float offset) {
		return sine(frequency, amplitude, offset, 0);
	}
	
	static KeyFrame<Float> sine(float frequency, float amplitude, float offset, float delay) {
		return ((tickDelta, time) -> (float) (Math.sin((time + tickDelta + delay) * frequency) * amplitude + offset));
	}
	
}
