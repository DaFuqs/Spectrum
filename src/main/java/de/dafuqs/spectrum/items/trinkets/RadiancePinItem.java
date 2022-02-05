package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.Identifier;

public class RadiancePinItem extends SpectrumTrinketItem {
	
	public static final int RANGE = 16;
	public static final int EFFECT_DURATION = 240;
	public static final long COOLDOWN_TICKS = 160;
	
    private final Identifier UNLOCK_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "midgame/spectrum_midgame");

	public RadiancePinItem(Settings settings) {
		super(settings);
	}
	
	@Override
    protected Identifier getUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
 
}