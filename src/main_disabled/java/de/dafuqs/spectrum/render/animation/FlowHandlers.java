package de.dafuqs.spectrum.render.animation;

public class FlowHandlers {
	public static final FlowHandler<Integer> INT = new FlowHandler<>() {
		
		@Override
		public FlowData<Integer> createData(DataSignature<Integer> signature) {
			return FlowData.create(signature);
		}
		
		@Override
		public Integer interpolate(Interpolation interpolation, Integer start, Integer end, float delta, long time) {
			return (int) Math.round(interpolation.apply(start, end, delta));
		}
		
	};
	
	public static final FlowHandler<Float> FLOAT = new FlowHandler<>() {
		
		@Override
		public FlowData<Float> createData(DataSignature<Float> signature) {
			return FlowData.create(signature);
		}
		
		@Override
		public Float interpolate(Interpolation interpolation, Float start, Float end, float delta, long time) {
			return (float) interpolation.apply(start, end, delta);
		}
		
	};
}
