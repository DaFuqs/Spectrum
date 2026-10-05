package de.dafuqs.spectrum.render.animation;

public interface FlowHandler<N extends Number> {
	
	FlowData<N> createData(DataSignature<N> signature);
	
	N interpolate(Interpolation interpolation, N start, N end, float delta, long time);
}
