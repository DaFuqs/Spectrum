package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.decay.RuinBlock;
import de.dafuqs.spectrum.blocks.decay.TerrorBlock;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(GlassBottleItem.class)
public abstract class GlassBottleItemMixin {
	
	@Shadow
	protected abstract ItemStack fill(ItemStack stack, PlayerEntity player, ItemStack outputStack);
	
	@Inject(method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/tag/TagKey;)Z"),
			cancellable = true,
			locals = LocalCapture.CAPTURE_FAILHARD)
	public void onUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult> cir, List list, ItemStack handStack, HitResult areaEffectCloudEntity, BlockPos blockPos) {
		BlockState blockState = world.getBlockState(blockPos);
		if (blockState.isOf(SpectrumBlocks.FADING) && Support.hasAdvancement(user, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_bottle_of_fading"))) {
			world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			cir.setReturnValue(TypedActionResult.success(this.fill(handStack, user, SpectrumItems.BOTTLE_OF_FADING.getDefaultStack()), world.isClient()));
		} else if (blockState.isOf(SpectrumBlocks.FAILING) && Support.hasAdvancement(user, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_bottle_of_failing"))) {
			world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			cir.setReturnValue(TypedActionResult.success(this.fill(handStack, user, SpectrumItems.BOTTLE_OF_FAILING.getDefaultStack()), world.isClient()));
		} else if (blockState.isOf(SpectrumBlocks.RUIN) && Support.hasAdvancement(user, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_bottle_of_ruin"))) {
			if (blockState.get(RuinBlock.DECAY_STATE) == RuinBlock.DecayConversion.BEDROCK) {
				world.setBlockState(blockPos, Blocks.BEDROCK.getDefaultState());
			} else {
				world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
			}
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			cir.setReturnValue(TypedActionResult.success(this.fill(handStack, user, SpectrumItems.BOTTLE_OF_RUIN.getDefaultStack()), world.isClient()));
		} else if (blockState.isOf(SpectrumBlocks.TERROR) && Support.hasAdvancement(user, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_bottle_of_terror"))) {
			if (blockState.get(TerrorBlock.DECAY_STATE) == TerrorBlock.DecayConversion.BEDROCK) {
				world.setBlockState(blockPos, Blocks.BEDROCK.getDefaultState());
			} else {
				world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
			}
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			cir.setReturnValue(TypedActionResult.success(this.fill(handStack, user, SpectrumItems.BOTTLE_OF_TERROR.getDefaultStack()), world.isClient()));
		}
	}
	
}
