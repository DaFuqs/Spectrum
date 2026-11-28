package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.*;

import java.util.*;

public class WhispyTartItem extends Item {
	
	public WhispyTartItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
		ItemStack itemstack = super.finishUsingItem(stack, level, livingEntity);
		if (!level.isClientSide && livingEntity instanceof ServerPlayer serverPlayer) {
			WhispyCircletItem.preventPhantomSpawns(serverPlayer);
		}
		
		return itemstack;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.whispy_tart.tooltip").withStyle(ChatFormatting.GRAY));
	}
	
}