package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import net.neoforged.api.distmarker.*;
import org.jspecify.annotations.*;
import top.theillusivec4.curios.api.*;

import java.util.*;

public class JeopardantItem extends SpectrumCurioItem {
	
	public JeopardantItem(Properties settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/jeopardant"));
	}
	
	public static double getAttackModifierForWearer(@Nullable LivingEntity entity) {
		if (entity == null) {
			return 0;
		} else {
			double mod = entity.getMaxHealth() / (entity.getHealth() * entity.getHealth() + 1); // starting with 1 % damage at 14 health up to 300 % damage at 1/20 health
			return Math.max(0, 1 + Math.log10(mod));
		}
	}
	
	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
		super.onUnequip(slotContext, newStack, stack);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		Minecraft client = Minecraft.getInstance();
		long mod = Math.round(getAttackModifierForWearer(client.player) * 100);
		if (mod == 0) {
			tooltip.add(Component.translatable("item.spectrum.jeopardant.tooltip.damage_zero"));
		} else {
			tooltip.add(Component.translatable("item.spectrum.jeopardant.tooltip.damage", mod));
		}
	}
	
}
