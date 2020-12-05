package de.dafuqs.spectrum.blocks.decay;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.StringIdentifiable;

public class DecayBlock1 extends DecayBlock {

    private static final EnumProperty<DecayConversion> DECAY_STATE = EnumProperty.of("decay_state", DecayConversion.class);

    public enum DecayConversion implements StringIdentifiable {
        DEFAULT("default"),
        LEAVES("leaves");

        private final String name;

        private DecayConversion(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String asString() {
            return this.name;
        }
    }

    public DecayBlock1(Settings settings, Tag<Block> whiteListBlockTag, Tag<Block> blackListBlockTag, int tier, float damageOnTouching) {
        super(settings, whiteListBlockTag, blackListBlockTag, tier, damageOnTouching);

        setDefaultState(getStateManager().getDefaultState().with(DECAY_STATE, DecayConversion.DEFAULT));

        BlockState destinationBlockState = this.getDefaultState().with(DECAY_STATE, DecayConversion.LEAVES);
        addDecayConversion(BlockTags.LEAVES, destinationBlockState);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(DECAY_STATE);
    }

}
