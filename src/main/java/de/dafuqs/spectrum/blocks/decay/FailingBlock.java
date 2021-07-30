package de.dafuqs.spectrum.blocks.decay;

import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.tag.Tag;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class FailingBlock extends DecayBlock {

    private static final EnumProperty<DecayConversion> DECAY_STATE = EnumProperty.of("decay_state", DecayConversion.class);

    public enum DecayConversion implements StringIdentifiable {
        DEFAULT("default"),
        OBSIDIAN("obsidian"),
        CRYING_OBSIDIAN("crying_obsidian");

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

    public FailingBlock(Settings settings, Tag<Block> whiteListBlockTag, Tag<Block> blackListBlockTag, int tier, float damageOnTouching) {
        super(settings, whiteListBlockTag, blackListBlockTag, tier, damageOnTouching);
        setDefaultState(getStateManager().getDefaultState().with(DECAY_STATE, DecayConversion.DEFAULT));

        BlockState destinationBlockState = this.getDefaultState().with(DECAY_STATE, DecayConversion.OBSIDIAN);
        addDecayConversion(SpectrumBlockTags.DECAY_OBSIDIAN_CONVERSIONS, destinationBlockState);

        BlockState destinationBlockState2 = this.getDefaultState().with(DECAY_STATE, DecayConversion.CRYING_OBSIDIAN);
        addDecayConversion(SpectrumBlockTags.DECAY_CRYING_OBSIDIAN_CONVERSIONS, destinationBlockState2);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(DECAY_STATE);
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(FailingBlock.DECAY_STATE).equals(DecayConversion.CRYING_OBSIDIAN)) {
            float xOffset = random.nextFloat();
            float zOffset = random.nextFloat();
            world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, state), pos.getX() + xOffset, pos.getY() + 1, pos.getZ() + zOffset, 0.0D, 0.0D, 0.0D);
        }
    }

}
