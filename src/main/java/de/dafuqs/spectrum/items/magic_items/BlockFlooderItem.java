package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.stat.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BlockFlooderItem extends Item {
	
	public BlockFlooderItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
		if (!world.isClient) {
			BlockFlooderProjectile blockFlooderProjectile = new BlockFlooderProjectile(world, user);
			blockFlooderProjectile.setItem(itemStack);
			blockFlooderProjectile.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
			world.spawnEntity(blockFlooderProjectile);
		}
		
		user.incrementStat(Stats.USED.getOrCreateStat(this));
		if (!user.getAbilities().creativeMode) {
			itemStack.decrement(1);
		}
		
		return TypedActionResult.success(itemStack, world.isClient());
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.block_flooder.tooltip").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.block_flooder.tooltip2").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.block_flooder.tooltip3").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.block_flooder.tooltip4").formatted(Formatting.GRAY));
	}
	
}