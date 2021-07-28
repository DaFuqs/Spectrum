package de.dafuqs.pigment.blocks.conditional;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.enums.PigmentColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class GemstoneOreBlock extends ConditionallyVisibleOreBlock {

    private final PigmentColor pigmentColor;

    public GemstoneOreBlock(Settings settings, UniformIntProvider experienceDropped, PigmentColor pigmentColor, boolean deepSlateOre) {
        super(settings, experienceDropped, deepSlateOre);
        this.pigmentColor = pigmentColor;

        setupCloak();
    }

    @Override
    public Identifier getCloakAdvancementIdentifier() {
        switch (this.pigmentColor) {
            case CYAN -> {
                return new Identifier(PigmentCommon.MOD_ID, "hidden/collect_shards/collect_topaz_shard");
            }
            case MAGENTA -> {
                return new Identifier(PigmentCommon.MOD_ID, "hidden/collect_shards/collect_amethyst_shard");
            }
            case YELLOW -> {
                return new Identifier(PigmentCommon.MOD_ID, "hidden/collect_shards/collect_citrine_shard");
            }
            case BLACK -> {
                return new Identifier(PigmentCommon.MOD_ID, "hidden/collect_shards/collect_onyx_shard");
            }
            default -> {
                return new Identifier(PigmentCommon.MOD_ID, "hidden/collect_shards/collect_moonstone_shard");
            }
        }
    }
}
