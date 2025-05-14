package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class DraconicTwinswordItem extends SwordItem implements SplittableItem, SlotReservingItem, Preenchanted, ExtendedItemBarProvider, SlotBackgroundEffectProvider {

	public static final float MAX_CHARGE_TIME = 60;
	private final ItemAttributeModifiers modifiers;
	
	public DraconicTwinswordItem(Tier toolMaterial, int attackDamage, float attackSpeed, Properties settings) {
		super(toolMaterial, settings);
		this.modifiers = createAttributes(toolMaterial, attackDamage, attackSpeed);
	}
	
	@Override
	public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
		if (SlotReservingItem.isReservingSlot(stack) || remainingUseTicks == 0) {
			return;
		}
		
		var strength = Math.min(getUseDuration(stack, user) - remainingUseTicks, MAX_CHARGE_TIME) / MAX_CHARGE_TIME;
		var twinsword = initiateTwinswordEntity(stack, world, user, strength);
		
		world.addFreshEntity(twinsword);
		
		world.playSound(null, twinsword, SoundEvents.TRIDENT_THROW.value(), SoundSource.PLAYERS, 0.5F + strength / 2, 1.0F);
		SlotReservingItem.reserve(stack, twinsword.getUUID());
		
		if (!world.isClientSide())
			stack.hurtAndBreak(1, user, LivingEntity.getSlotForHand(user.getUsedItemHand()));
		
		super.releaseUsing(stack, world, user, remainingUseTicks);
	}
	
	@NotNull
	private static DraconicTwinswordEntity initiateTwinswordEntity(ItemStack stack, Level world, LivingEntity user, float strength) {
		var twinsword = new DraconicTwinswordEntity(world);
		twinsword.setOwner(user);
		twinsword.setPickupItemStack(stack);
		
		var yaw = user.getYRot();
		var pitch = user.getXRot();
		
		float f = -Mth.sin(yaw * (float) (Math.PI / 180.0)) * Mth.cos(pitch * (float) (Math.PI / 180.0));
		float h = Mth.cos(yaw * (float) (Math.PI / 180.0)) * Mth.cos(pitch * (float) (Math.PI / 180.0));
		
		twinsword.absMoveTo(user.getX() + f * 1.334, user.getEyeY() - 0.2, user.getZ() + h * 1.334);
		twinsword.setDeltaMovement(0, strength, 0);
		twinsword.setMaxPierce(SpectrumEnchantmentHelper.getLevel(world.registryAccess(), Enchantments.PIERCING, stack));
		twinsword.hasImpulse = true;
		twinsword.hurtMarked = true;
		twinsword.pickup = AbstractArrow.Pickup.DISALLOWED;
		return twinsword;
	}
	
	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return 200;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		user.startUsingItem(hand);
		return InteractionResultHolder.consume(user.getItemInHand(hand));
	}
	
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BLOCK;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		tooltip.add(Component.translatable("item.spectrum.draconic_twinsword.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.draconic_twinsword.tooltip2").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.draconic_twinsword.tooltip3").withStyle(ChatFormatting.GRAY));
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
	public boolean isFoil(ItemStack stack) {
		return super.isFoil(stack) && !SlotReservingItem.isReservingSlot(stack);
	}
	
	@Override
	public ItemStack getSplitResult(ServerPlayer player, ItemStack parent) {
		var result = new ItemStack(SpectrumItems.DRAGON_TALON);
		var durability = parent.getDamageValue();
		
		if (SlotReservingItem.isReservingSlot(parent)) {
			durability += player.getAbilities().instabuild ? 0 : 500;
			player.getCooldowns().addCooldown(result.getItem(), 400);
		}
		
		result.applyComponents(parent.getComponents());
		result.remove(DataComponents.ATTRIBUTE_MODIFIERS);
		SlotReservingItem.free(result);
		
		result.setDamageValue(Math.min(durability, parent.getMaxDamage() - 1));
		sign(player, result);
		return result;
	}
	
	@Override
	public boolean canSplit(ServerPlayer player, InteractionHand occupiedHand, ItemStack stack) {
		if (player.getCooldowns().isOnCooldown(stack.getItem()))
			return false;
		
		return switch (occupiedHand) {
			case MAIN_HAND -> player.getItemInHand(InteractionHand.OFF_HAND).isEmpty();
			case OFF_HAND -> player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty();
		};
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
	public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.SWEEPING_EDGE, 5);
	}
	
	@Override
	public int barCount(ItemStack stack) {
		return 1;
	}
	
	@Override
	public boolean allowVanillaDurabilityBarRendering(@Nullable Player player, ItemStack stack) {
		if (player == null || SlotReservingItem.isReservingSlot(stack) || player.getItemInHand(player.getUsedItemHand()) != stack)
			return true;
		
		return !player.isUsingItem();
	}
	
	@Override
	public BarSignature getSignature(@Nullable Player player, @NotNull ItemStack stack, int index) {
		if (player == null || SlotReservingItem.isReservingSlot(stack) || !player.isUsingItem())
			return ExtendedItemBarProvider.PASS;
		
		var activeStack = player.getItemInHand(player.getUsedItemHand());
		if (activeStack != stack)
			return ExtendedItemBarProvider.PASS;
		
		var progress = Math.round(Mth.clampedLerp(0, 13, ((float) player.getTicksUsingItem() / MAX_CHARGE_TIME)));
		return new BarSignature(2, 13, 13, progress, 1, InkColors.YELLOW_COLOR, 2, ExtendedItemBarProvider.DEFAULT_BACKGROUND_COLOR);
	}
	
	@Override
	public SlotEffect backgroundType(@Nullable Player player, ItemStack stack) {
		return SlotEffect.BORDER_FADE;
	}
	
	@Override
	public int getBackgroundColor(@Nullable Player player, ItemStack stack, float tickDelta) {
		return InkColors.YELLOW_COLOR;
	}
	
	@Override
	public boolean canBeEnchantedWith(ItemStack stack, Holder<Enchantment> enchantment, EnchantingContext context) {
		return super.canBeEnchantedWith(stack, enchantment, context) || enchantment.is(Enchantments.CHANNELING) || enchantment.is(Enchantments.PIERCING) || enchantment.is(SpectrumEnchantments.INERTIA);
	}
}
