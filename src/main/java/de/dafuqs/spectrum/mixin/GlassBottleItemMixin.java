package de.dafuqs.spectrum.mixin;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.decay.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

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
		
		if (blockState.isOf(SpectrumBlocks.FADING)
				&& SpectrumCommon.CONFIG.CanPickUpFading
				&& AdvancementHelper.hasAdvancement(user, SpectrumCommon.locate("unlocks/items/bottle_of_fading"))) {
			
			world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			cir.setReturnValue(TypedActionResult.success(this.fill(handStack, user, SpectrumItems.BOTTLE_OF_FADING.getDefaultStack()), world.isClient()));
			
		} else if (blockState.isOf(SpectrumBlocks.FAILING)
				&& SpectrumCommon.CONFIG.CanPickUpFailing
				&& AdvancementHelper.hasAdvancement(user, SpectrumCommon.locate("unlocks/items/bottle_of_failing"))) {
			
			world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			cir.setReturnValue(TypedActionResult.success(this.fill(handStack, user, SpectrumItems.BOTTLE_OF_FAILING.getDefaultStack()), world.isClient()));
			
		} else if (blockState.isOf(SpectrumBlocks.RUIN)
				&& SpectrumCommon.CONFIG.CanPickUpRuin
				&& AdvancementHelper.hasAdvancement(user, SpectrumCommon.locate("unlocks/items/bottle_of_ruin"))) {
			
			if (blockState.get(DecayBlock.CONVERSION) == DecayBlock.Conversion.DEFAULT) {
				world.setBlockState(blockPos, Blocks.BEDROCK.getDefaultState());
			} else {
				world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
			}
			
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			cir.setReturnValue(TypedActionResult.success(this.fill(handStack, user, SpectrumItems.BOTTLE_OF_RUIN.getDefaultStack()), world.isClient()));
			
		} else if (blockState.isOf(SpectrumBlocks.FORFEITURE)
				&& SpectrumCommon.CONFIG.CanPickUpForfeiture
				&& AdvancementHelper.hasAdvancement(user, SpectrumCommon.locate("unlocks/items/bottle_of_forfeiture"))) {
			
			if (blockState.get(DecayBlock.CONVERSION) == DecayBlock.Conversion.DEFAULT) {
				world.setBlockState(blockPos, Blocks.BEDROCK.getDefaultState());
			} else {
				world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
			}
			
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			cir.setReturnValue(TypedActionResult.success(this.fill(handStack, user, SpectrumItems.BOTTLE_OF_FORFEITURE.getDefaultStack()), world.isClient()));
		}
	}
	
}
