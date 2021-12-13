package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class SparklestoneOreBlock extends CloakedOreBlock {

	public SparklestoneOreBlock(Settings settings, UniformIntProvider uniformIntProvider, boolean deepSlateOre) {
		super(settings, uniformIntProvider, deepSlateOre);
		registerCloak();
	}

	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return new Identifier(SpectrumCommon.MOD_ID, "milestones/reveal_sparklestone");
	}

}
