package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class GlowVisionGogglesItem extends SpectrumTrinketItem {
	
	public static ItemStack COST = new ItemStack(Items.GLOW_INK_SAC, 1);
	
	public GlowVisionGogglesItem(Settings settings) {
		super(settings, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_glow_vision_helmet"));
	}
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.tick(stack, slot, entity);
		
		World world = entity.getWorld();
		if (world != null && world.getTimeOfDay() % 20 == 0) {
			if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
				int lightLevelAtPlayerPos = world.getLightLevel(serverPlayerEntity.getBlockPos());
				
				if (lightLevelAtPlayerPos < 7) {
					StatusEffectInstance nightVisionInstance = serverPlayerEntity.getStatusEffect(StatusEffects.NIGHT_VISION);
					if (nightVisionInstance == null || nightVisionInstance.getDuration() < 10 * 20) { // prevent "night vision running out" flashing
						// no / short night vision => search for glow ink sac and add night vision if found
						
						if (InventoryHelper.removeFromInventory(serverPlayerEntity, COST)) {
							StatusEffectInstance newNightVisionInstance = new StatusEffectInstance(StatusEffects.NIGHT_VISION, 20 * SpectrumCommon.CONFIG.GlowVisionGogglesDuration);
							serverPlayerEntity.addStatusEffect(newNightVisionInstance);
							world.playSoundFromEntity(null, serverPlayerEntity, SpectrumSoundEvents.ITEM_ARMOR_EQUIP_GLOW_VISION, SoundCategory.PLAYERS, 0.2F, 1.0F);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		tooltip.add(new TranslatableText("item.spectrum.glow_vision_helmet.tooltip"));
	}
	
}
