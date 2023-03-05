package de.dafuqs.spectrum.deeper_down;

import java.util.stream.Stream;

import com.mojang.serialization.Codec;

import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumFluids;
import de.dafuqs.spectrum.registries.SpectrumPlacementModifiers;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.FeaturePlacementContext;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

public class DragonFossilPlacementModifier extends PlacementModifier {
	private static final DragonFossilPlacementModifier INSTANCE = new DragonFossilPlacementModifier();
    public static final Codec<DragonFossilPlacementModifier> MODIFIER_CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Stream<BlockPos> getPositions(FeaturePlacementContext context, Random random, BlockPos pos) {
		BlockPos.Mutable mutable = pos.mutableCopy();
		StructureWorldAccess structureWorldAccess = context.getWorld();
		
		while (!structureWorldAccess.isOutOfHeightLimit(mutable)) {
			BlockState state = context.getBlockState(mutable);
			FluidState fstate = state.getFluidState();
			
			if (state.isOf(SpectrumBlocks.ROTTEN_GROUND) || fstate.isOf(SpectrumFluids.DRAGONROT) || fstate.isOf(SpectrumFluids.FLOWING_DRAGONROT)) {
				// The Fossil feature places at Y - 15 - rand[0..10], so we add 15 to make up for that.
				// We should probably make a custom Feature for that, but it might not be worth it.
				mutable.move(new Vec3i(0, 15, 0));
				return Stream.of(mutable);
			}
			
			mutable.move(Direction.UP);
		}
		
        return Stream.of(new BlockPos[0]);
    }

    @Override
    public PlacementModifierType<?> getType() {
        return SpectrumPlacementModifiers.DRAGON_FOSSIL;
    }
}
