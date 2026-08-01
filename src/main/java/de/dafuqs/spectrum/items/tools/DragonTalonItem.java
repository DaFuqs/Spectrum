package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.*;

public class DragonTalonItem extends MalachiteBidentItem implements MergeableItem, SlotReservingItem, SlotBackgroundEffectProvider {
	
	private final ItemAttributeModifiers modifiers;
	
	public DragonTalonItem(Tier toolMaterial, double damage, double extraReach, Item.Properties settings) {
		super(settings, 0, 0, 0, 0, false);
		this.modifiers = ItemAttributeModifiers.builder()
				.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, damage + toolMaterial.getAttackDamageBonus(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(SpectrumEntityAttributes.REACH_MODIFIER_ID, extraReach, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.build();
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		tooltip.add(Component.translatable("item.spectrum.dragon_talon.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.dragon_talon.tooltip2").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.dragon_talon.tooltip3").withStyle(ChatFormatting.GRAY));
	}
	
	@Override
	public float getThrowSpeed(ItemStack stack) {
		return 3.5F;
	}
	
	@Override
	protected void throwBident(ItemStack stack, ServerLevel world, Player playerEntity) {
		DragonTalonEntity dragonTalon = new DragonTalonEntity(world);
		dragonTalon.setPickupItemStack(stack);
		dragonTalon.setOwner(playerEntity);
		dragonTalon.absMoveTo(playerEntity.getX(), playerEntity.getEyeY() - 0.1, playerEntity.getZ());
		dragonTalon.shootFromRotation(playerEntity, playerEntity.getXRot(), playerEntity.getYRot(), 0.0F, getThrowSpeed(stack), 1.0F);
		dragonTalon.hasImpulse = true;
		dragonTalon.hurtMarked = true;
		dragonTalon.pickup = AbstractArrow.Pickup.ALLOWED;
		
		world.addFreshEntity(dragonTalon);
		SoundEvent soundEvent = SoundEvents.TRIDENT_THROW.value();
		
		world.playSound(null, dragonTalon, soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);
		SlotReservingItem.reserve(stack, dragonTalon.getUUID());
	}
	
	@Override
	public ItemStack getMergeResult(ServerPlayer player, ItemStack firstHalf, ItemStack secondHalf) {
		var durability = Math.max(firstHalf.getDamageValue(), secondHalf.getDamageValue());
		var result = new ItemStack(SpectrumItems.DRACONIC_TWINSWORD.get());
		result.applyComponents(firstHalf.getComponents());
		
		result.remove(SpectrumDataComponentTypes.PAIRED_ITEM);
		result.remove(DataComponents.ATTRIBUTE_MODIFIERS);
		SlotReservingItem.free(result);
		
		if (SlotReservingItem.isReservingSlot(firstHalf) || SlotReservingItem.isReservingSlot(secondHalf)) {
			durability += player.getAbilities().instabuild ? 0 : 500;
			player.getCooldowns().addCooldown(result.getItem(), 400);
		}
		if (result.isDamageableItem()) {
			result.setDamageValue(Math.min(durability, firstHalf.getMaxDamage() - 1));
		}
		
		return result;
	}
	
	@Override
	public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
		var hand = user.getUsedItemHand();
		if (hand == InteractionHand.MAIN_HAND)
			return;
		
		if (!SlotReservingItem.isReservingSlot(stack)) {
			super.releaseUsing(user.getItemInHand(InteractionHand.OFF_HAND), world, user, remainingUseTicks);
			return;
		}
		
		UUID reserver = SlotReservingItem.getReserver(stack);
		if (world instanceof ServerLevel serverWorld && reserver != null) {
			if (serverWorld.getEntity(reserver) instanceof DragonTalonEntity dragonTalonEntity) {
				dragonTalonEntity.recall();
			}
		}
	}
	
	@Override
	public boolean isFoil(ItemStack stack) {
		return super.isFoil(stack) && !SlotReservingItem.isReservingSlot(stack);
	}
	
	@Override
	public boolean canMerge(ServerPlayer player, ItemStack parent, ItemStack other) {
		if (player.getCooldowns().isOnCooldown(parent.getItem()))
			return false;
		return (parent.getItem() == other.getItem() && verify(parent, other));
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		if (hand == InteractionHand.MAIN_HAND)
			return InteractionResultHolder.fail(user.getItemInHand(hand));
		return super.use(world, user, hand);
	}
	
	@Override
	public void playSound(ServerPlayer player) {
		player.playNotifySound(SpectrumSoundEvents.METALLIC_UNSHEATHE, SoundSource.PLAYERS, 0.5F, 0.8F + player.getRandom().nextFloat() * 0.4F);
	}
	
	public static ItemStack findThrownStack(Player player, UUID id) {
		var inventory = player.getInventory();
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			var stack = inventory.getItem(i);
			if (SlotReservingItem.isReserver(stack, id)) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		if (entity instanceof Player player) {
			if (player.getCooldowns().isOnCooldown(stack.getItem()) || SlotReservingItem.isReservingSlot(stack)) {
				stack.remove(DataComponents.ATTRIBUTE_MODIFIERS);
			} else {
				stack.set(DataComponents.ATTRIBUTE_MODIFIERS, modifiers);
			}
		}
	}
	
	@Override
	public DamageComposition getDamageComposition(LivingEntity attacker, LivingEntity target, ItemStack stack, float damage) {
		var composition = new DamageComposition();
		composition.add(SpectrumDamageTypes.evisceration(attacker.level(), attacker), damage);
		return composition;
	}
	
	@Override
	public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
		return Map.of();
	}
	
	@Override
	public float getDefenseMultiplier(LivingEntity target, ItemStack stack) {
		return 1F;
	}
	
	@Override
	public SlotBackgroundEffectProvider.SlotEffect backgroundType(@Nullable Player player, ItemStack stack) {
		return SlotBackgroundEffectProvider.SlotEffect.BORDER_FADE;
	}
	
	@Override
	public int getBackgroundColor(@Nullable Player player, ItemStack stack, float tickDelta) {
		return InkColors.YELLOW_COLOR;
	}
	
	@Override
	public void expandTooltip(ItemStack stack, @Nullable Player player, Consumer<Component> tooltip, TooltipContext context) {
	}
	
	@Override
	public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
		return super.supportsEnchantment(stack, enchantment) || enchantment.is(Enchantments.CHANNELING) || enchantment.is(Enchantments.PIERCING);
	}
}
