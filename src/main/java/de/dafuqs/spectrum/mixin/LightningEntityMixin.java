package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.conditional.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(LightningBolt.class)
public abstract class LightningEntityMixin {
	
	@Shadow
	protected abstract BlockPos getStrikePosition();
	
	@Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getDifficulty()Lnet/minecraft/world/Difficulty;"))
	private void spectrum$trySpawnStormStoneOnImpact(CallbackInfo ci) {
		Level world = ((LightningBolt) (Object) this).level();
		
		// do not spawn storm stones when using other forms of spawning thunder,
		// like magic, ... in clear weather. Only when it is actually thundering
		if (world.isThundering() && SpectrumConfig.CONFIG.StormStoneDimensions.get().contains(world.dimension().location().toString())) {
			spectrum$spawnStormStone(world, this.getStrikePosition());
		}
	}
	
	@Unique
	private void spectrum$spawnStormStone(@NotNull Level world, BlockPos affectedBlockPos) {
		BlockState blockState = world.getBlockState(affectedBlockPos);
		BlockPos aboveGroundBlockPos;
		
		if (blockState.is(SpectrumBlockTags.C_LIGHTNING_RODS)) {
			// if struck a lightning rod: check around the base of the rod instead
			// always spawn a stone
			BlockPos blockPos2 = affectedBlockPos.relative((blockState.getValue(LightningRodBlock.FACING)).getOpposite());
			aboveGroundBlockPos = blockPos2.relative(Direction.from2DDataValue(world.getRandom().nextInt(6))).above();
		} else {
			// there is chance involved
			if (world.random.nextFloat() < SpectrumConfig.CONFIG.StormStoneSpawnChance.get()) {
				return;
			}
			aboveGroundBlockPos = affectedBlockPos.above();
		}
		
		if (world.isEmptyBlock(aboveGroundBlockPos)) {
			Direction randomDirection = Direction.from2DDataValue(world.random.nextInt(4));
			BlockState placementBlockState = SpectrumBlocks.STUCK_STORM_STONE.get().defaultBlockState().setValue(StuckStormStoneBlock.FACING, randomDirection);
			if (placementBlockState.canSurvive(world, aboveGroundBlockPos)) {
				world.setBlockAndUpdate(aboveGroundBlockPos, placementBlockState);
			}
		}
	}
	
}
