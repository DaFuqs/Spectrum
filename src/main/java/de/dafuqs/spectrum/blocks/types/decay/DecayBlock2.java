package de.dafuqs.spectrum.blocks.types.decay;

import de.dafuqs.spectrum.blocks.SpectrumBlockTags;
import de.dafuqs.spectrum.blocks.SpectrumBlocks;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class DecayBlock2 extends DecayBlock {

    private static final EnumProperty<DecayConversion> DECAY_STATE = EnumProperty.of("decay_state", DecayConversion.class);

    private static BlockState CRYING_OBSIDIAN_BLOCKSTATE;

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

    public DecayBlock2(Settings settings, Tag<Block> whiteListBlockTag, Tag<Block> blackListBlockTag, int tier, float damageOnTouching) {
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

        if(CRYING_OBSIDIAN_BLOCKSTATE == null) {
            CRYING_OBSIDIAN_BLOCKSTATE = SpectrumBlocks.DECAY2.getDefaultState().with(DecayBlock2.DECAY_STATE, DecayConversion.CRYING_OBSIDIAN);
        }

        if (world.getBlockState(pos).equals(CRYING_OBSIDIAN_BLOCKSTATE)) {
            Vec3d vec3d = new Vec3d(0.5, 0.5, 0.5);
            float xOffset = random.nextFloat();
            float zOffset = random.nextFloat();
            world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, CRYING_OBSIDIAN_BLOCKSTATE), pos.getX() + xOffset, pos.getY() + 1, pos.getZ() + zOffset, 0.0D, 0.0D, 0.0D);
        }
    }

}
