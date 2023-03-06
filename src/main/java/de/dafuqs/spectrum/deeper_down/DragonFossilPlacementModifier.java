package de.dafuqs.spectrum.deeper_down;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.fluid.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.stream.*;

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
			
			if (state.isOf(SpectrumBlocks.ROTTEN_GROUND) || fstate.isIn(SpectrumFluidTags.DRAGONROT)) {
				// The Fossil feature places at Y - 15 - rand[0..10], so we add 15 to make up for that.
				// We should probably make a custom Feature for that, but it might not be worth it.
				mutable.move(new Vec3i(0, 15, 0));
				if (structureWorldAccess.isOutOfHeightLimit(mutable)) {
					return Stream.of(new BlockPos[0]);
				}
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
