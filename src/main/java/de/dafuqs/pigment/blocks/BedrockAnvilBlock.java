package de.dafuqs.pigment.blocks;

import net.minecraft.block.AnvilBlock;
import net.minecraft.entity.FallingBlockEntity;

public class BedrockAnvilBlock extends AnvilBlock {

    public BedrockAnvilBlock(Settings settings) {
        super(settings);
    }

    // Does not get damaged by falling or using it
    // => Mixin into AnvilBlock
    /*@Nullable
    public static BlockState getLandingState(BlockState fallingState) {
        return PigmentBlocks.BEDROCK_ANVIL.getDefaultState().with(FACING, fallingState.get(FACING));
    }*/

    // Heavier => More damage
    protected void configureFallingBlockEntity(FallingBlockEntity entity) {
        entity.setHurtEntities(3.0F, 64);
    }

}
