package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.ink.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import top.theillusivec4.curios.api.*;

import java.util.*;

public class GlowVisionGogglesItem extends SpectrumCurioItem implements InkPowered {
	
	public static final InkAmount INK_COST = new InkAmount(InkColors.LIGHT_BLUE, 20);
	public static final Ingredient ITEM_COST = Ingredient.of(SpectrumItemTags.GLOW_VISION_GOGGLES_CONSUMABLE);
	
	public GlowVisionGogglesItem(Properties settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/glow_vision_goggles"));
	}
	
	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		super.curioTick(slotContext, stack);
		
		Level level = slotContext.entity().level();
		if (!level.isClientSide() && level.getGameTime() % 20 == 0) {
			if (slotContext.entity() instanceof ServerPlayer serverPlayerEntity) {
				giveEffect(level, stack, serverPlayerEntity);
			}
		}
	}
	
	@Override
	public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
		super.onEquip(slotContext, prevStack, stack);
		
		Level level = slotContext.entity().level();
		if (!level.isClientSide() && slotContext.entity() instanceof ServerPlayer serverPlayerEntity) {
			giveEffect(level, stack, serverPlayerEntity);
		}
	}
	
	private void giveEffect(Level world, ItemStack gogglesStack, ServerPlayer serverPlayer) {
		int lightLevelAtPlayerPos = world.getMaxLocalRawBrightness(serverPlayer.blockPosition());
		
		if (lightLevelAtPlayerPos < 7) {
			MobEffectInstance nightVisionInstance = serverPlayer.getEffect(MobEffects.NIGHT_VISION);
			if (nightVisionInstance == null || nightVisionInstance.getDuration() < 220) { // prevent "night vision running out" flashing
				// no / short night vision => search for glow ink sac and add night vision if found
				
				if (payForUse(serverPlayer, gogglesStack, INK_COST, ITEM_COST)) {
					MobEffectInstance newNightVisionInstance = new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * SpectrumConfig.CONFIG.GlowVisionGogglesDuration.get(), 0, true, true);
					serverPlayer.addEffect(newNightVisionInstance);
					world.playSound(null, serverPlayer, SpectrumSoundEvents.ITEM_ARMOR_EQUIP_GLOW_VISION, SoundSource.PLAYERS, 0.2F, 1.0F);
				}
			}
		}
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		if (InkPowered.canUseClient()) {
			tooltip.add(Component.translatable("item.spectrum.glow_vision_goggles.tooltip_with_ink", INK_COST.color().getColoredInkName()));
		} else {
			tooltip.add(Component.translatable("item.spectrum.glow_vision_goggles.tooltip"));
		}
	}
	
	@Override
	public List<InkColor> getUsedColors() {
		return List.of(INK_COST.color());
	}
}
