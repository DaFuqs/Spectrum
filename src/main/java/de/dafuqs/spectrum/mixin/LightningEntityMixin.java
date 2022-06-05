package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.entity.LightningEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningEntity.class)
public abstract class LightningEntityMixin {
	
	@Shadow
	protected abstract BlockPos getAffectedBlockPos();
	
	@Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LightningEntity;cleanOxidation(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"))
	private void spawnLightningStoneAtImpact(CallbackInfo ci) {
		World world = ((LightningEntity) (Object) this).world;
		
		// do not spawn storm stones when using other forms of
		// spawning thunder, like magic, ... in clear weather. Only when it is actually thundering
		if (world.isThundering() && SpectrumCommon.CONFIG.LightningStonesWorlds.contains(world.getRegistryKey().getValue().toString())) {
			spawnLightningStone(world, this.getAffectedBlockPos());
		}
	}
	
	private void spawnLightningStone(@NotNull World world, BlockPos affectedBlockPos) {
		BlockState blockState = world.getBlockState(affectedBlockPos);
		BlockPos aboveGroundBlockPos;
		
		if (blockState.isOf(Blocks.LIGHTNING_ROD)) {
			// if struck a lightning rod: check around the base of the rod instead
			// always spawn a stone
			BlockPos blockPos2 = affectedBlockPos.offset((blockState.get(LightningRodBlock.FACING)).getOpposite());
			aboveGroundBlockPos = blockPos2.offset(Direction.fromHorizontal(world.getRandom().nextInt(6))).up();
		} else {
			// there is chance involved
			if (world.random.nextFloat() > SpectrumCommon.CONFIG.LightningStonesChance) {
				return;
			}
			aboveGroundBlockPos = affectedBlockPos.up();
		}
		
		if (world.isAir(aboveGroundBlockPos)) {
			BlockState placementBlockState = SpectrumBlocks.STUCK_LIGHTNING_STONE.getDefaultState();
			if (placementBlockState.canPlaceAt(world, aboveGroundBlockPos)) {
				world.setBlockState(aboveGroundBlockPos, placementBlockState);
			}
		}
		
	}
	
}
