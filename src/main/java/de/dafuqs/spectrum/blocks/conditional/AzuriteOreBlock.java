package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class AzuriteOreBlock extends CloakedOreBlock {

    public AzuriteOreBlock(Settings settings, UniformIntProvider uniformIntProvider, boolean deepSlateOre) {
        super(settings, uniformIntProvider, deepSlateOre);
        registerCloak();
    }

    @Override
    public Identifier getCloakAdvancementIdentifier() {
        return new Identifier(SpectrumCommon.MOD_ID, "craft_colored_sapling");
    }

}
