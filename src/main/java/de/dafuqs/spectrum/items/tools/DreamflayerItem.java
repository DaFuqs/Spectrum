package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
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
	
	private final float baseAttackDamage;
	private final float baseAttackSpeed;
	
	public DreamflayerItem(Tier toolMaterial, int attackDamage, float attackSpeed, Properties settings) {
		super(toolMaterial, settings.attributes(SwordItem.createAttributes(toolMaterial, attackDamage, attackSpeed)));
		this.baseAttackDamage = attackDamage;
		this.baseAttackSpeed = attackSpeed;
	}
	
	public static float getDamageAfterModifier(float amount, LivingEntity attacker, LivingEntity target) {
		float damageMultiplier = (target.getArmorValue() + DreamflayerItem.ARMOR_DIFFERENCE_DAMAGE_MULTIPLIER) / (attacker.getArmorValue() + DreamflayerItem.ARMOR_DIFFERENCE_DAMAGE_MULTIPLIER);
		return amount * damageMultiplier;
	}
	
	@Override
	public void onUseTick(Level world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		super.onUseTick(world, user, stack, remainingUseTicks);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack stack = user.getItemInHand(hand);
		if (hand == InteractionHand.MAIN_HAND && user.isShiftKeyDown()) {
			boolean isActivated = ActivatableItem.isActivated(stack);
			if (isActivated) {
				setActivated(stack, false);
				if (!world.isClientSide) {
					world.playSound(null, user.getX(), user.getY(), user.getZ(), SpectrumSoundEvents.DREAMFLAYER_DEACTIVATE, SoundSource.PLAYERS, 1.0F, 1F);
				}
			} else {
				if (InkPowered.tryDrainEnergy(user, USED_COLOR, INK_COST_FOR_ACTIVATION)) {
					setActivated(stack, true);
					if (!world.isClientSide) {
						world.playSound(null, user.getX(), user.getY(), user.getZ(), SpectrumSoundEvents.DREAMFLAYER_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1F);
					}
				} else if (!world.isClientSide) {
					world.playSound(null, user.getX(), user.getY(), user.getZ(), SpectrumSoundEvents.DREAMFLAYER_DEACTIVATE, SoundSource.PLAYERS, 1.0F, 1F);
				}
			}
			
			return InteractionResultHolder.pass(stack);
		}
		
		return InteractionResultHolder.success(stack);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
		
		if (world.isClientSide) {
			if (ActivatableItem.isActivated(stack)) {
				Vec3 pos = entity.position();
				world.addParticle(ColoredCraftingParticleEffect.RED,
						entity.getRandomX(1.0), pos.y() + 1.05D, entity.getRandomZ(1.0),
						0.0D, 0.1D, 0.0D);
			}
		} else {
			if (world.getGameTime() % 20 == 0 && ActivatableItem.isActivated(stack)) {
				if (entity instanceof ServerPlayer player && !InkPowered.tryDrainEnergy(player, USED_COLOR, INK_COST_PER_SECOND)) {
					setActivated(stack, false);
					world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SpectrumSoundEvents.DREAMFLAYER_DEACTIVATE, SoundSource.PLAYERS, 0.8F, 1F);
				}
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.dreamflayer.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.dreamflayer.tooltip2").withStyle(ChatFormatting.GRAY));
		if (ActivatableItem.isActivated(stack)) {
			tooltip.add(Component.translatable("item.spectrum.dreamflayer.tooltip.activated").withStyle(ChatFormatting.GRAY));
		} else {
			tooltip.add(Component.translatable("item.spectrum.dreamflayer.tooltip.deactivated", USED_COLOR.getColoredInkName()).withStyle(ChatFormatting.GRAY));
		}
	}
	
	@Override
	public boolean allowComponentsUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
		return reequipAnimation(oldStack, newStack);
	}
	
	private boolean reequipAnimation(ItemStack before, ItemStack after) {
		return !after.is(this) || ActivatableItem.isActivated(before) != ActivatableItem.isActivated(after);
	}
	
	public void setActivated(ItemStack stack, boolean active) {
		if (ActivatableItem.isActivated(stack) != active) {
			float damage = baseAttackDamage * (active ? 1.5f : 1f);
			float speed = baseAttackSpeed * (active ? 0.75f : 1f);
			stack.update(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY, comp -> {
				var builder = ItemAttributeModifiers.builder();
				for (var entry : comp.modifiers()) {
					if (entry.modifier().is(BASE_ATTACK_DAMAGE_ID))
						builder.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, damage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
					if (entry.modifier().is(BASE_ATTACK_SPEED_ID))
						builder.add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, speed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
					else
						builder.add(entry.attribute(), entry.modifier(), entry.slot());
				}
				return builder.build();
			});
			ActivatableItem.setActivated(stack, active);
		}
	}

	@Override
	public List<InkColor> getUsedColors() {
		return List.of(USED_COLOR);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void addInkPoweredTooltip(List<Component> tooltip) {
		InkPowered.super.addInkPoweredTooltip(tooltip);
	}

	@Override
	public DamageComposition getDamageComposition(LivingEntity attacker, LivingEntity target, ItemStack stack, float damage) {
		float newDamage = getDamageAfterModifier(damage, attacker, target);

		DamageComposition composition = new DamageComposition();
		if (ActivatableItem.isActivated(stack)) {
			composition.addPlayerOrEntity(attacker, newDamage * 0.5F);
			composition.add(attacker.damageSources().magic(), newDamage * 0.25F);
			composition.add(SpectrumDamageTypes.setHealth(attacker.level(), attacker), newDamage * 0.25F);
		} else {
			composition.addPlayerOrEntity(attacker, newDamage * 0.75F);
			composition.add(attacker.damageSources().magic(), newDamage * 0.25F);
		}
		return composition;
	}
	
	@Override
	public SlotEffect backgroundType(@Nullable Player player, ItemStack stack) {
		if (ActivatableItem.isActivated(stack))
			return SlotEffect.FULL_PACKAGE;
		
		var usable = InkPowered.hasAvailableInk(player, new InkCost(InkColors.RED, INK_COST_FOR_ACTIVATION));
		return usable ? SlotEffect.BORDER_FADE : SlotEffect.NONE;
	}
	
	@Override
	public int getBackgroundColor(@Nullable Player player, ItemStack stack, float tickDelta) {
		return InkColors.RED_COLOR;
	}
}
