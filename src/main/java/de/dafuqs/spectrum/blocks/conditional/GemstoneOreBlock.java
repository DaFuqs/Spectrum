package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.enums.GemstoneColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class GemstoneOreBlock extends CloakedOreBlock {

	private final GemstoneColor gemstoneColor;

	public GemstoneOreBlock(Settings settings, UniformIntProvider experienceDropped, GemstoneColor gemstoneColor, boolean deepSlateOre) {
		super(settings, experienceDropped, deepSlateOre);
		this.gemstoneColor = gemstoneColor;
		registerCloak();
	}

	@Override
	public Identifier getCloakAdvancementIdentifier() {
		switch (this.gemstoneColor) {
			case CYAN -> {
				return new Identifier(SpectrumCommon.MOD_ID, "hidden/collect_shards/collect_topaz_shard");
			}
			case MAGENTA -> {
				return new Identifier(SpectrumCommon.MOD_ID, "hidden/collect_shards/collect_amethyst_shard");
			}
			case YELLOW -> {
				return new Identifier(SpectrumCommon.MOD_ID, "hidden/collect_shards/collect_citrine_shard");
			}
			case BLACK -> {
				return new Identifier(SpectrumCommon.MOD_ID, "hidden/collect_shards/collect_onyx_shard");
			}
			default -> {
				return new Identifier(SpectrumCommon.MOD_ID, "hidden/collect_shards/collect_moonstone_shard");
			}
		}
	}

}
