package de.dafuqs.spectrum.blocks.boom;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;

import java.util.*;

public class ParametricMiningDeviceItem extends BlockItem implements Preenchanted {
	
	public ParametricMiningDeviceItem(Block block, Item.Properties properties) {
		super(block, properties);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		tooltip.add(Component.translatable("block.spectrum.parametric_mining_device.tooltip").withStyle(ChatFormatting.GRAY));
		super.appendHoverText(stack, context, tooltip, type);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
		var stack = user.getItemInHand(hand);
		if (stack.is(this)) {
			level.playSound(null, user.getX(), user.getY(), user.getZ(), SpectrumSoundEvents.BLOCK_PARAMETRIC_MINING_DEVICE_THROWN, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
			if (!level.isClientSide()) {
				ParametricMiningDeviceEntity entity = new ParametricMiningDeviceEntity(level, user);
				entity.setItem(stack);
				entity.shootFromRotation(user, user.getXRot(), user.getYRot(), 0, 1.5F, 0F);
				level.addFreshEntity(entity);
			}
			if (!user.getAbilities().instabuild) {
				stack.shrink(1);
			}
		}
		return InteractionResultHolder.success(stack);
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}
	
	@Override
	public int getEnchantmentValue(ItemStack stack) {
		return 12;
	}
	
	@Override
	public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
		return super.supportsEnchantment(stack, enchantment) || enchantment.is(SpectrumEnchantmentTags.ON_ARCANE_CHARGES);
	}
	
	@Override
	public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.POWER, 1);
	}
	
}
