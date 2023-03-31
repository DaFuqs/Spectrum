package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MysteriousLocketItem extends Item {
	
	public MysteriousLocketItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient) {
			ItemStack handStack = user.getStackInHand(hand);
			if (isSocketed(handStack)) {
				ItemUsage.exchangeStack(handStack, user, SpectrumItems.MYSTERIOUS_COMPASS.getDefaultStack());
				world.playSound(null, user.getX(), user.getY(), user.getZ(), SpectrumSoundEvents.UNLOCK, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			}
		}
		return super.use(world, user, hand);
	}
	
	
	public static boolean isSocketed(ItemStack compassStack) {
		NbtCompound nbt = compassStack.getNbt();
		return nbt != null && nbt.getBoolean("socketed");
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.mysterious_locket.tooltip").formatted(Formatting.GRAY));
		if (isSocketed(stack)) {
			tooltip.add(Text.translatable("item.spectrum.mysterious_locket.tooltip_socketed").formatted(Formatting.GRAY));
		} else {
			tooltip.add(Text.translatable("item.spectrum.mysterious_locket.tooltip_empty").formatted(Formatting.GRAY));
		}
	}
	
}
