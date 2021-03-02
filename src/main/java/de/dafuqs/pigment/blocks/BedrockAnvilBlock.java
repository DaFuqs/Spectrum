package de.dafuqs.pigment.blocks;

import net.minecraft.block.AnvilBlock;
import net.minecraft.entity.FallingBlockEntity;

public class BedrockAnvilBlock extends AnvilBlock {

    public BedrockAnvilBlock(Settings settings) {
        super(settings);
    }

    // Heavier => More damage
    protected void configureFallingBlockEntity(FallingBlockEntity entity) {
        entity.setHurtEntities(3.0F, 64);
    }

}
