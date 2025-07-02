package de.dafuqs.spectrum.render.animation;

import org.jetbrains.annotations.*;

@ApiStatus.OverrideOnly
public interface FlowHandler<N extends Number> {
	
	FlowData<N> createData(DataSignature <N> signature);
	
	N interpolate(Interpolation interpolation, N start, N end, float delta, long time);
}
