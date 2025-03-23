package de.dafuqs.spectrum.items.tools;

import com.google.common.collect.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class DreamflayerItem extends SwordItem implements InkPowered, ActivatableItem, SplitDamageItem, SlotBackgroundEffectProvider {
	
	public static final InkColor USED_COLOR = InkColors.RED;
	public static final long INK_COST_FOR_ACTIVATION = 200L;
	public static final long INK_COST_PER_SECOND = 20L;
	
	/**
	 * The less armor the attacker with this sword has and the more
	 * the one that gets attacked, the higher the damage will be
	 * <p>
	 * See LivingEntityMixin spectrum$applyDreamflayerDamage
	 */
	public static final float ARMOR_DIFFERENCE_DAMAGE_MULTIPLIER = 2.5F;
	
	public final float attackDamage;
	public final float attackSpeed;
	
	public DreamflayerItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
		super(toolMaterial, attackDamage, attackSpeed, settings);
		this.attackDamage = attackDamage;
		this.attackSpeed = attackSpeed;
	}
	
	public static float getDamageAfterModifier(float amount, LivingEntity attacker, LivingEntity target) {
		float damageMultiplier = (target.getArmor() + DreamflayerItem.ARMOR_DIFFERENCE_DAMAGE_MULTIPLIER) / (attacker.getArmor() + DreamflayerItem.ARMOR_DIFFERENCE_DAMAGE_MULTIPLIER);
		return amount * damageMultiplier;
	}
	
	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		super.usageTick(world, user, stack, remainingUseTicks);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (hand == Hand.MAIN_HAND && user.isSneaking()) {
			boolean isActivated = ActivatableItem.isActivated(stack);
			if (isActivated) {
				ActivatableItem.setActivated(stack, false);
				if (!world.isClient) {
					world.playSound(null, user.getX(), user.getY(), user.getZ(), SpectrumSoundEvents.DREAMFLAYER_DEACTIVATE, SoundCategory.PLAYERS, 1.0F, 1F);
				}
			} else {
				if (InkPowered.tryDrainEnergy(user, USED_COLOR, INK_COST_FOR_ACTIVATION)) {
					ActivatableItem.setActivated(stack, true);
					if (!world.isClient) {
						world.playSound(null, user.getX(), user.getY(), user.getZ(), SpectrumSoundEvents.DREAMFLAYER_ACTIVATE, SoundCategory.PLAYERS, 1.0F, 1F);
					}
				} else if (!world.isClient) {
					world.playSound(null, user.getX(), user.getY(), user.getZ(), SpectrumSoundEvents.DREAMFLAYER_DEACTIVATE, SoundCategory.PLAYERS, 1.0F, 1F);
				}
			}
			
			return TypedActionResult.pass(stack);
		}
		
		return TypedActionResult.success(stack);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
		
		if (world.isClient) {
			if (ActivatableItem.isActivated(stack)) {
				Vec3d pos = entity.getPos();
				world.addParticle(SpectrumParticleTypes.RED_CRAFTING,
						entity.getParticleX(1.0), pos.getY() + 1.05D, entity.getParticleZ(1.0),
						0.0D, 0.1D, 0.0D);
			}
		} else {
			if (world.getTime() % 20 == 0 && ActivatableItem.isActivated(stack)) {
				if (entity instanceof ServerPlayerEntity player && !InkPowered.tryDrainEnergy(player, USED_COLOR, INK_COST_PER_SECOND)) {
					ActivatableItem.setActivated(stack, false);
					world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SpectrumSoundEvents.DREAMFLAYER_DEACTIVATE, SoundCategory.PLAYERS, 0.8F, 1F);
				}
			}
		}
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.dreamflayer.tooltip").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.dreamflayer.tooltip2").formatted(Formatting.GRAY));
		if (ActivatableItem.isActivated(stack)) {
			tooltip.add(Text.translatable("item.spectrum.dreamflayer.tooltip.activated").formatted(Formatting.GRAY));
		} else {
			tooltip.add(Text.translatable("item.spectrum.dreamflayer.tooltip.deactivated", USED_COLOR.getColoredInkName()).formatted(Formatting.GRAY));
		}
	}
	
	@Override
	public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
		return reequipAnimation(oldStack, newStack);
	}
	
	private boolean reequipAnimation(ItemStack before, ItemStack after) {
		return !after.isOf(this) || ActivatableItem.isActivated(before) != ActivatableItem.isActivated(after);
	}
	
	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		if (slot == EquipmentSlot.MAINHAND) {
			if (ActivatableItem.isActivated(stack)) {
				builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.attackDamage * 1.5, EntityAttributeModifier.Operation.ADDITION));
				builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", this.attackSpeed * 0.75, EntityAttributeModifier.Operation.ADDITION));
			} else {
				builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.attackDamage, EntityAttributeModifier.Operation.ADDITION));
				builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", this.attackSpeed, EntityAttributeModifier.Operation.ADDITION));
			}
		}
		return builder.build();
	}

	@Override
	public List<InkColor> getUsedColors() {
		return List.of(USED_COLOR);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void addInkPoweredTooltip(List<Text> tooltip) {
		InkPowered.super.addInkPoweredTooltip(tooltip);
	}

	@Override
	public DamageComposition getDamageComposition(LivingEntity attacker, LivingEntity target, ItemStack stack, float damage) {
		float newDamage = getDamageAfterModifier(damage, attacker, target);

		DamageComposition composition = new DamageComposition();
		if (ActivatableItem.isActivated(stack)) {
			composition.addPlayerOrEntity(attacker, newDamage * 0.5F);
			composition.add(attacker.getDamageSources().magic(), newDamage * 0.25F);
			composition.add(SpectrumDamageTypes.setHealth(attacker.getWorld(), attacker), newDamage * 0.25F);
		} else {
			composition.addPlayerOrEntity(attacker, newDamage * 0.75F);
			composition.add(attacker.getDamageSources().magic(), newDamage * 0.25F);
		}
		return composition;
	}
	
	@Override
	public SlotEffect backgroundType(@Nullable PlayerEntity player, ItemStack stack) {
		if (ActivatableItem.isActivated(stack))
			return SlotEffect.FULL_PACKAGE;
		
		var usable = InkPowered.hasAvailableInk(player, new InkCost(InkColors.RED, INK_COST_FOR_ACTIVATION));
		return usable ? SlotEffect.BORDER_FADE : SlotEffect.NONE;
	}
	
	@Override
	public int getBackgroundColor(@Nullable PlayerEntity player, ItemStack stack, float tickDelta) {
		return InkColors.RED_COLOR;
	}
}
