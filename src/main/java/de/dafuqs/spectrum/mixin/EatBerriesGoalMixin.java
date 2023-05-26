package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.blocks.dd_deco.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(FoxEntity.EatBerriesGoal.class)
public abstract class EatBerriesGoalMixin extends MoveToTargetPosGoal {
	
	@SuppressWarnings("target")
	@Shadow(aliases = {"this$0", "field_17975"}, remap = false) // risky mixin of the day award goes to this
	private FoxEntity foxEntity;
	
	public EatBerriesGoalMixin(PathAwareEntity mob, double speed, int range) {
		super(mob, speed, range);
	}
	
	@Inject(method = "isTargetPos(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Z", at = @At("HEAD"), cancellable = true)
	private void spectrum$isTargetPos(WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		BlockState blockState = world.getBlockState(pos);
		if (blockState.isOf(SpectrumBlocks.SAWBLADE_HOLLY_BUSH) && blockState.get(SawbladeHollyBushBlock.AGE) == SawbladeHollyBushBlock.MAX_AGE) {
			cir.setReturnValue(true);
		}
	}
	
	@Inject(method = "eatBerries()V", at = @At("HEAD"), cancellable = true)
	private void spectrum$eatBerries(CallbackInfo ci) {
		if (foxEntity.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
			BlockState blockState = foxEntity.world.getBlockState(this.targetPos);
			if (blockState.isOf(SpectrumBlocks.SAWBLADE_HOLLY_BUSH)) {
				spectrum$pickSawbladeHollyBerries(blockState);
				ci.cancel();
			}
		}
	}
	
	private void spectrum$pickSawbladeHollyBerries(BlockState state) {
		int age = state.get(SawbladeHollyBushBlock.AGE);
		int berriesPlucked = 1 + foxEntity.world.random.nextInt(2) + (age == SawbladeHollyBushBlock.MAX_AGE ? 1 : 0);
		ItemStack itemStack = foxEntity.getEquippedStack(EquipmentSlot.MAINHAND);
		if (itemStack.isEmpty()) {
			foxEntity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(SpectrumItems.SAWBLADE_HOLLY_BERRY));
			--berriesPlucked;
		}
		
		if (berriesPlucked > 0) {
			Block.dropStack(foxEntity.world, this.targetPos, new ItemStack(SpectrumItems.SAWBLADE_HOLLY_BERRY, berriesPlucked));
		}
		
		foxEntity.playSound(SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
		foxEntity.world.setBlockState(this.targetPos, state.with(SawbladeHollyBushBlock.AGE, 1), Block.NOTIFY_LISTENERS);
	}
	
	
}
