package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.enums.GemstoneColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class GemstoneOreBlock extends CloakedOreBlock {

	private final Identifier cloakAdvancementIdentifier;
	private final GemstoneColor gemstoneColor;

	public GemstoneOreBlock(Settings settings, UniformIntProvider experienceDropped, GemstoneColor gemstoneColor, boolean deepSlateOre, Identifier cloakAdvancementIdentifier) {
		super(settings, experienceDropped, deepSlateOre);
		this.gemstoneColor = gemstoneColor;
		this.cloakAdvancementIdentifier = cloakAdvancementIdentifier;
		registerCloak();
	}

	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return cloakAdvancementIdentifier;
	}

}
