package de.dafuqs.spectrum.blocks.decay;

import de.dafuqs.spectrum.blocks.SpectrumBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.tag.Tag;
import net.minecraft.util.StringIdentifiable;

public class DecayBlock3 extends DecayBlock {

    private static final EnumProperty<DecayConversion> DECAY_STATE = EnumProperty.of("decay_state", DecayConversion.class);

    public enum DecayConversion implements StringIdentifiable {
        DEFAULT("default"),
        BEDROCK("bedrock");

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

    public DecayBlock3(Settings settings, Tag<Block> whiteListBlockTag, Tag<Block> blackListBlockTag, int tier, float damageOnTouching) {
        super(settings, whiteListBlockTag, blackListBlockTag, tier, damageOnTouching);

        setDefaultState(getStateManager().getDefaultState().with(DECAY_STATE, DecayConversion.DEFAULT));

        BlockState destinationBlockState = this.getDefaultState().with(DECAY_STATE, DecayConversion.BEDROCK);
        addDecayConversion(SpectrumBlockTags.DECAY_BEDROCK_CONVERSIONS, destinationBlockState);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(DECAY_STATE);
    }

}
