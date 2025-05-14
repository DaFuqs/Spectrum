package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

import java.util.*;

public class BlockFlooderItem extends Item {
	
	public BlockFlooderItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack itemStack = user.getItemInHand(hand);
		world.playSound(null, user.getX(), user.getY(), user.getZ(), SpectrumSoundEvents.ENTITY_BLOCK_FLOODER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
		if (!world.isClientSide) {
			BlockFlooderProjectile blockFlooderProjectile = new BlockFlooderProjectile(world, user);
			blockFlooderProjectile.setItem(itemStack);
			blockFlooderProjectile.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 1.5F, 1.0F);
			world.addFreshEntity(blockFlooderProjectile);
		}
		
		user.awardStat(Stats.ITEM_USED.get(this));
		if (!user.getAbilities().instabuild) {
			itemStack.shrink(1);
		}
		
		return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.block_flooder.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.block_flooder.tooltip2").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.block_flooder.tooltip3").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.block_flooder.tooltip4").withStyle(ChatFormatting.GRAY));
	}
	
}
