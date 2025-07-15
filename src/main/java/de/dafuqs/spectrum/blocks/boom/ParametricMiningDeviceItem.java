package de.dafuqs.spectrum.blocks.boom;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;

import java.util.*;

public class ParametricMiningDeviceItem extends ModularExplosionBlockItem {
	
	public ParametricMiningDeviceItem(Block block, Item.Properties properties) {
		super(block, 5, 0, 3, properties);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		tooltip.add(Component.translatable("block.spectrum.parametric_mining_device.tooltip").withStyle(ChatFormatting.GRAY));
		super.appendHoverText(stack, context, tooltip, type);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		var stack = user.getItemInHand(hand);
		if (stack.is(this)) {
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SpectrumSoundEvents.BLOCK_PARAMETRIC_MINING_DEVICE_THROWN, SoundSource.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
			if (!world.isClientSide()) {
				ParametricMiningDeviceEntity entity = new ParametricMiningDeviceEntity(world, user);
				entity.setItem(stack);
				entity.shootFromRotation(user, user.getXRot(), user.getYRot(), 0, 1.5F, 0F);
				world.addFreshEntity(entity);
			}
			if (!user.getAbilities().instabuild) {
				stack.shrink(1);
			}
		}
		return InteractionResultHolder.success(stack);
	}
	
}
