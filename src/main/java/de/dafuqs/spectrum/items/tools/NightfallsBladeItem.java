package de.dafuqs.spectrum.items.tools;

import com.google.common.collect.*;
import com.jamieswhiteshirt.reachentityattributes.*;
import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.potion.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class NightfallsBladeItem extends ToolItem implements Vanishable, InkPoweredPotionFillable, SlotBackgroundEffectProvider {
	
	private static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("unlocks/equipment/nightfalls_blade");
	protected static final UUID REACH_MODIFIER_ID = UUID.fromString("8e2e05ef-a48a-4e2d-9633-388edcb21ea3");

	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
	
	public NightfallsBladeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
		super(material, settings);

		var damage = (float) attackDamage + material.getAttackDamage();
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", damage, EntityAttributeModifier.Operation.ADDITION));
		builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
		builder.put(ReachEntityAttributes.ATTACK_RANGE, new EntityAttributeModifier(REACH_MODIFIER_ID, "Weapon modifier", -1.5F, EntityAttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? attributeModifiers : super.getAttributeModifiers(slot);
	}
	
	@Override
	public int maxEffectCount() {
		return 1;
	}
	
	@Override
	public int maxEffectAmplifier() {
		return 2;
	}

	@Override
	public boolean isWeapon() {
		return true;
	}
	
	@Override
	public long adjustFinalCostFor(@NotNull InkPoweredStatusEffectInstance instance) {
		var mod = SpectrumStatusEffects.isStrongSleepEffect(instance) ? 1 : 0;
		return Math.round(Math.pow(instance.getInkCost().getCost(), 1.75 + instance.getStatusEffectInstance().getAmplifier() + mod));
	}
	
	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if(target.isAlive() && attacker instanceof PlayerEntity player) {
			if (AdvancementHelper.hasAdvancement(player, UNLOCK_IDENTIFIER)) {
				List<InkPoweredStatusEffectInstance> effects = InkPoweredPotionFillable.getEffects(stack);
				for(InkPoweredStatusEffectInstance instance : effects) {
					if(InkPowered.tryDrainEnergy(player, instance.getInkCost().getColor(), instance.getInkCost().getCost())) {
						World world = attacker.getWorld();
						if (world.isClient) {
							world.addParticle(new DynamicParticleEffect(ParticleTypes.EFFECT, 0.1F, ColorHelper.colorIntToVec(instance.getStatusEffectInstance().getEffectType().getColor()), 0.5F, 120, true, true),
									target.getParticleX(0.5D), target.getBodyY(0.5D), target.getParticleZ(0.5D),
									world.random.nextFloat() - 0.5, world.random.nextFloat() - 0.5, world.random.nextFloat() - 0.5
							);
						} else {
							target.addStatusEffect(instance.getStatusEffectInstance(), attacker);
						}
					}
				}
			}
		}
		return super.postHit(stack, target, attacker);
	}
	
	@Override
	public boolean hasGlint(ItemStack stack) {
		return super.hasGlint(stack) || !PotionUtil.getCustomPotionEffects(stack).isEmpty();
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		appendPotionFillableTooltip(stack, tooltip, Text.translatable("item.spectrum.nightfalls_blade.when_struck"), true);
	}
	
	@Override
	public SlotBackgroundEffectProvider.SlotEffect backgroundType(@Nullable PlayerEntity player, ItemStack stack) {
		if (InkPoweredPotionFillable.getEffects(stack).isEmpty()) {
			return SlotBackgroundEffectProvider.SlotEffect.NONE;
		}
		
		var effect = InkPoweredPotionFillable.getEffects(stack).get(0);
		var usable = InkPowered.hasAvailableInk(player, new InkCost(effect.getInkCost().getColor(), adjustFinalCostFor(effect)));
		return usable ? SlotBackgroundEffectProvider.SlotEffect.BORDER_FADE : SlotEffect.BORDER;
	}
	
	@Override
	public int getBackgroundColor(@Nullable PlayerEntity player, ItemStack stack, float tickDelta) {
		List<InkPoweredStatusEffectInstance> effects = InkPoweredPotionFillable.getEffects(stack);
		if (effects.isEmpty())
			return 0x000000;
		
		return effects.get(0).getColor();
	}
	
	@Override
	public float getEffectOpacity(@Nullable PlayerEntity player, ItemStack stack, float tickDelta) {
		List<InkPoweredStatusEffectInstance> effects = InkPoweredPotionFillable.getEffects(stack);
		if (effects.isEmpty())
			return 0F;
		
		var effect = effects.get(0);
		if (InkPowered.hasAvailableInk(player, new InkCost(effect.getInkCost().getColor(), adjustFinalCostFor(effect))))
			return 1F;
		
		if (player == null)
			return 0F;
		
		var time = player.getWorld().getTime();
		return (float) (Math.sin((time + tickDelta) / 30F) * 0.3F + 0.3);
	}
}