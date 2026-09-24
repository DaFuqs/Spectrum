package de.dafuqs.spectrum.blocks.deeper_down.groundcover;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.placement.*;
import net.neoforged.neoforge.common.*;
import org.jspecify.annotations.*;

import java.util.*;

public interface OvergrownBlock extends BonemealableBlock {
	
	@Override
	default Type getType() {
		return Type.NEIGHBOR_SPREADER;
	}
	
	@Override
	default boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
		return level.getBlockState(pos.above()).isAir() && !level.getBiome(pos).value().getGenerationSettings().getFlowerFeatures().isEmpty();
	}
	
	@Override
	default boolean isBonemealSuccess(Level level, RandomSource p_221276_, BlockPos pos, BlockState state) {
		return true;
	}
	
	@Override
	default void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
		BlockPos blockpos = pos.above();

		outer:
		for(int i = 0; i < 128; ++i) {
			BlockPos offsetPos = blockpos;
			
			for(int j = 0; j < i / 16; ++j) {
				offsetPos = offsetPos.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
				if (!(level.getBlockState(offsetPos.below()).getBlock() instanceof OvergrownBlock) || level.getBlockState(offsetPos).isCollisionShapeFullBlock(level, offsetPos)) {
					continue outer;
				}
			}
			
			BlockState offsetState = level.getBlockState(offsetPos);
			if (offsetState.isAir()) {
				Holder<PlacedFeature> holder;
				List<ConfiguredFeature<?, ?>> list = level.getBiome(offsetPos).value().getGenerationSettings().getFlowerFeatures();
				if (list.isEmpty()) {
					continue;
				}
				holder = ((RandomPatchConfiguration) list.get(random.nextInt(list.size())).config()).feature();
				holder.value().place(level, level.getChunkSource().getGenerator(), random, offsetPos);
			}
		}
	}
	
}
