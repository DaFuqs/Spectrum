package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.enums.SpectrumColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class GemstoneOreBlock extends CloakedOreBlock {

    private final SpectrumColor spectrumColor;

    public GemstoneOreBlock(Settings settings, UniformIntProvider experienceDropped, SpectrumColor spectrumColor, boolean deepSlateOre) {
        super(settings, experienceDropped, deepSlateOre);
        this.spectrumColor = spectrumColor;

        setupCloak();
    }

    @Override
    public Identifier getCloakAdvancementIdentifier() {
        switch (this.spectrumColor) {
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
