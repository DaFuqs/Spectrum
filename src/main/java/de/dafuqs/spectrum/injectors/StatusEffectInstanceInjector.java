package de.dafuqs.spectrum.injectors;

public interface StatusEffectInstanceInjector {
	
	default boolean spectrum$isSevere() {
		return false;
	}
	
	default void spectrum$setSevere(boolean severe) {
	}
	
	default void spectrum$setDuration(int newDuration) {
	}
	
	default void spectrum$setAmplifier(int newAmplifier) {
	}
	
}
