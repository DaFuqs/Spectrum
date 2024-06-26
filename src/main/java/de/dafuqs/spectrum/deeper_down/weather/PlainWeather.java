package de.dafuqs.spectrum.deeper_down.weather;

import de.dafuqs.spectrum.*;

public class PlainWeather extends NoParticleState {
	
	public static final PlainWeather INSTANCE = new PlainWeather();
	
	private PlainWeather() {
		super(SpectrumCommon.locate("plain_weather"));
	}
	
	@Override
	public float getThirst() {
		return 0F;
	}
}
