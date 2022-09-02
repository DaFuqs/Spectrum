package de.dafuqs.spectrum.items.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import de.dafuqs.spectrum.energy.InkPowered;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.color.InkColors;
import de.dafuqs.spectrum.items.ActivatableItem;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DreamflayerItem extends SwordItem implements FabricItem, InkPowered, ActivatableItem {
	
	public static final InkColor USED_COLOR = InkColors.RED;
	public static final long INK_COST_FOR_ACTIVATION = 200L;
	public static final long INK_COST_PER_SECOND = 20L;
	
	/**
	 * The less armor the attacker with this sword has and the more
	 * the one that gets attacked, the higher the damage will be
	 * <p>
	 * See LivingEntityMixin spectrum$applyDreamflayerDamage
	 */
	public static float ARMOR_DIFFERENCE_DAMAGE_MULTIPLIER = 2.5F;
	
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
		
		if(world.isClient) {
			if(ActivatableItem.isActivated(stack)) {
				Vec3d pos = entity.getPos();
				world.addParticle(SpectrumParticleTypes.RED_CRAFTING,
						pos.getX() + world.random.nextDouble(), pos.getY() + 1.05D, pos.getZ() + world.random.nextDouble(),
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
		if(ActivatableItem.isActivated(stack)) {
			tooltip.add(Text.translatable("item.spectrum.dreamflayer.tooltip.activated").formatted(Formatting.GRAY));
		} else {
			tooltip.add(Text.translatable("item.spectrum.dreamflayer.tooltip.deactivated").formatted(Formatting.GRAY));
		}
	}
	
	public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
		return reequipAnimation(oldStack, newStack);
	}
	
	private boolean reequipAnimation(ItemStack before, ItemStack after) {
		return !after.isOf(this) || ActivatableItem.isActivated(before) != ActivatableItem.isActivated(after);
	}
	
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
}
