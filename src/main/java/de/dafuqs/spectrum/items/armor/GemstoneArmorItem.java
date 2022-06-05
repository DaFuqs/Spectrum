package de.dafuqs.spectrum.items.armor;

import de.dafuqs.spectrum.interfaces.ArmorWithHitEffect;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GemstoneArmorItem extends ArmorItem implements ArmorWithHitEffect {
	
	private final EquipmentSlot equipmentSlot;
	private final int armorSlotID;
	
	public GemstoneArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
		super(material, slot, settings);
		this.equipmentSlot = slot;
		switch (slot) {
			case HEAD -> {
				this.armorSlotID = 0;
			}
			case CHEST -> {
				this.armorSlotID = 1;
			}
			case LEGS -> {
				this.armorSlotID = 2;
			}
			default -> {
				this.armorSlotID = 3;
			}
		}
	}
	
	@Override
	public void onHit(ItemStack itemStack, DamageSource source, LivingEntity targetEntity, float amount) {
		// While mostly useful against mobs, being able to trigger this effect for all kinds of damage
		// like fall damage seems like an awesome mechanic
		process(equipmentSlot, source, targetEntity);
		targetEntity.world.playSound(null, targetEntity.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_HIT, SoundCategory.PLAYERS, 1.0F, 1.0F);
		targetEntity.world.playSound(null, targetEntity.getBlockPos(), SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);
		
		itemStack.damage(2, targetEntity, (e) -> {
			e.sendEquipmentBreakStatus(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, this.armorSlotID));
		});
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		addTooltip(tooltip, equipmentSlot);
	}
	
	private void process(@NotNull EquipmentSlot equipmentSlot, DamageSource source, LivingEntity targetEntity) {
		switch (equipmentSlot) {
			case HEAD -> {
				if (source.getAttacker() instanceof LivingEntity) {
					StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.WEAKNESS, 5 * 20, 2);
					((LivingEntity) source.getAttacker()).addStatusEffect(statusEffectInstance);
					statusEffectInstance = new StatusEffectInstance(StatusEffects.SLOWNESS, 5 * 20, 2);
					((LivingEntity) source.getAttacker()).addStatusEffect(statusEffectInstance);
				}
			}
			case CHEST -> {
				StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.ABSORPTION, 5 * 20, 1);
				targetEntity.addStatusEffect(statusEffectInstance);
				statusEffectInstance = new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 1);
				targetEntity.addStatusEffect(statusEffectInstance);
			}
			case FEET -> {
				StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.RESISTANCE, 5 * 20, 2);
				targetEntity.addStatusEffect(statusEffectInstance);
				statusEffectInstance = new StatusEffectInstance(StatusEffects.REGENERATION, 5 * 20, 1);
				targetEntity.addStatusEffect(statusEffectInstance);
			}
			case LEGS -> {
				StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.SPEED, 5 * 20, 2);
				targetEntity.addStatusEffect(statusEffectInstance);
				statusEffectInstance = new StatusEffectInstance(StatusEffects.INVISIBILITY, 5 * 20, 2);
				targetEntity.addStatusEffect(statusEffectInstance);
			}
		}
	}
	
	public void addTooltip(List<Text> tooltip, @NotNull EquipmentSlot equipmentSlot) {
		switch (equipmentSlot) {
			case HEAD -> {
				tooltip.add(new TranslatableText("item.spectrum.emergency_helmet.tooltip").formatted(Formatting.GRAY));
			}
			case CHEST -> {
				tooltip.add(new TranslatableText("item.spectrum.emergency_chestplate.tooltip").formatted(Formatting.GRAY));
			}
			case LEGS -> {
				tooltip.add(new TranslatableText("item.spectrum.emergency_leggings.tooltip").formatted(Formatting.GRAY));
			}
			case FEET -> {
				tooltip.add(new TranslatableText("item.spectrum.emergency_boots.tooltip").formatted(Formatting.GRAY));
			}
		}
	}
	
}
