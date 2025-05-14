package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.deeper_down.flora.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Fox.FoxEatBerriesGoal.class)
public abstract class EatBerriesGoalMixin extends MoveToBlockGoal {

	@Unique
	private final Fox foxEntity = (Fox) mob;
	
	public EatBerriesGoalMixin(PathfinderMob mob, double speed, int range) {
		super(mob, speed, range);
	}
	
	@Inject(method = "isValidTarget", at = @At("HEAD"), cancellable = true)
	private void spectrum$isTargetPos(LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		BlockState blockState = world.getBlockState(pos);
		if (blockState.is(SpectrumBlocks.SAWBLADE_HOLLY_BUSH) && blockState.getValue(SawbladeHollyBushBlock.AGE) == SawbladeHollyBushBlock.MAX_AGE) {
			cir.setReturnValue(true);
		}
	}
	
	@Inject(method = "onReachedTarget", at = @At("HEAD"), cancellable = true)
	private void spectrum$eatBerries(CallbackInfo ci) {
		if (foxEntity.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
			BlockState blockState = foxEntity.level().getBlockState(this.blockPos);
			if (blockState.is(SpectrumBlocks.SAWBLADE_HOLLY_BUSH)) {
				spectrum$pickSawbladeHollyBerries(blockState);
				ci.cancel();
			}
		}
	}
	
	@Unique
	private void spectrum$pickSawbladeHollyBerries(BlockState state) {
		Level world = foxEntity.level();
		int age = state.getValue(SawbladeHollyBushBlock.AGE);
		int berriesPlucked = 1 + world.random.nextInt(2) + (age == SawbladeHollyBushBlock.MAX_AGE ? 1 : 0);
		ItemStack itemStack = foxEntity.getItemBySlot(EquipmentSlot.MAINHAND);
		if (itemStack.isEmpty()) {
			foxEntity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(SpectrumItems.SAWBLADE_HOLLY_BERRY));
			--berriesPlucked;
		}
		
		if (berriesPlucked > 0) {
			Block.popResource(world, this.blockPos, new ItemStack(SpectrumItems.SAWBLADE_HOLLY_BERRY, berriesPlucked));
		}
		
		foxEntity.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
		world.setBlock(this.blockPos, state.setValue(SawbladeHollyBushBlock.AGE, 1), Block.UPDATE_CLIENTS);
	}
	
	
}
