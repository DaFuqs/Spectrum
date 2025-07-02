package de.dafuqs.spectrum.injectors;

public interface MapDecorationInjector {
	
	default void spectrum$setScale(byte scale) {
	}
	
	default byte spectrum$getScale() {
		return 0;
	}
	
}
