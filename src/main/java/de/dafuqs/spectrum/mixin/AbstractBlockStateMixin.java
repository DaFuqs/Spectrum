package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.shapes.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class AbstractBlockStateMixin {
	
	@ModifyVariable(method = "spawnAfterBreak", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	public boolean spectrum$preventXPDropsWhenUsingResonance(boolean dropExperience, ServerLevel world, BlockPos pos, ItemStack stack) {
		if (ResonanceProcessor.preventNextXPDrop && EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.RESONANT_BLOCK_DROPS)) {
			ResonanceProcessor.preventNextXPDrop = false;
			return false;
		}
		return dropExperience;
	}
	
	// https://github.com/apace100/water-walking-fix
	@ModifyReturnValue(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("RETURN"))
	public VoxelShape getFluidloggedCollisionShape(VoxelShape original, BlockGetter level, BlockPos pos, CollisionContext context) {
		FluidState fluidState = spectrum$getFluidStateHelper(level, pos);
		if(fluidState == null || fluidState.isEmpty()) {
			return original;
		}
		int fluidLevel = fluidState.getAmount();
		if (fluidLevel == 0) return original;
		VoxelShape fluidShape = FLUID_LEVEL_SHAPES[fluidLevel];
		
		if (context.isAbove(fluidShape, pos, true) && context.canStandOnFluid(spectrum$getFluidStateHelper(level, pos.above()), fluidState)) {
			return Shapes.or(original, fluidShape);
		}
		return original;
	}
	
	// Thank you, sable
	@Unique
	private static FluidState spectrum$getFluidStateHelper(BlockGetter level, BlockPos pos) {
		if (level.isOutsideBuildHeight(pos)) {
			return Fluids.EMPTY.defaultFluidState();
		} else {
			if (level instanceof LevelAccessor levelAccessor) {
				ChunkAccess chunk = levelAccessor.getChunk(pos);
				return chunk.getFluidState(pos);
			}
			return level.getFluidState(pos);
		}
	}
	
	@Unique
	private static final VoxelShape[] FLUID_LEVEL_SHAPES;
	static {
		FLUID_LEVEL_SHAPES = new VoxelShape[16];
		for (int i = 0; i <= 8; i++) {
			FLUID_LEVEL_SHAPES[i] = Block.box(0.0, 0.0, 0.0, 16.0, i, 16.0);
		}
	}
}
