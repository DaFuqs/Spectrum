package de.dafuqs.spectrum.blocks.present;

import com.mojang.authlib.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.inventory.tooltip.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.stream.*;

public class PresentBlockItem extends BlockItem {
	
	public PresentBlockItem(Block block, Properties settings) {
		super(block, settings);
	}
	
	@Override
	protected boolean canPlace(BlockPlaceContext context, BlockState state) {
		return isWrapped(context.getItemInHand()) && super.canPlace(context, state);
	}
	
	public static void setOwner(ItemStack itemStack, Player giver) {
		var profile = new GameProfile(giver.getUUID(), giver.getName().getString());
		itemStack.set(DataComponents.PROFILE, new ResolvableProfile(profile));
	}
	
	public static Optional<ResolvableProfile> getOwner(ItemStack itemStack) {
		return Optional.ofNullable(itemStack.get(DataComponents.PROFILE));
	}
	
	public static boolean isEmpty(ItemStack itemStack) {
		return itemStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY).isEmpty();
	}
	
	public static boolean isWrapped(ItemStack itemStack) {
		return getWrapData(itemStack) != null;
	}
	
	public static @Nullable WrappedPresentComponent getWrapData(ItemStack itemStack) {
		return itemStack.get(SpectrumDataComponentTypes.WRAPPED_PRESENT);
	}
	
	public static void wrap(ItemStack itemStack, PresentBlock.WrappingPaper wrappingPaper, Map<Integer, Integer> colors) {
		itemStack.set(SpectrumDataComponentTypes.WRAPPED_PRESENT, new WrappedPresentComponent(wrappingPaper, colors));
	}
	
	@Override
	public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
		if (isWrapped(stack)) {
			return false;
		}
		
		if (action != ClickAction.SECONDARY) {
			return false;
		}
		
		BundleContents bundleContents = stack.get(DataComponents.BUNDLE_CONTENTS);
		if (bundleContents == null) {
			return false;
		}
		
		ItemStack itemStack = slot.getItem();
		BundleContents.Mutable mutable = new BundleContents.Mutable(bundleContents);
		if (itemStack.isEmpty()) {
			this.playRemoveOneSound(player);
			ItemStack itemStack2 = mutable.removeOne();
			if (itemStack2 != null) {
				ItemStack itemStack3 = slot.safeInsert(itemStack2);
				mutable.tryInsert(itemStack3);
			}
		} else if (itemStack.getItem().canFitInsideContainerItems()) {
			int i = mutable.tryTransfer(slot, player);
			if (i > 0) {
				this.playInsertSound(player);
			}
		}
		
		stack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
		return true;
	}
	
	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
		if (isWrapped(stack)) {
			return false;
		}
		if (isCraftingInventory(slot)) {
			return false;
		}
		
		if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
			BundleContents bundleContents = stack.get(DataComponents.BUNDLE_CONTENTS);
			if (bundleContents == null) {
				return false;
			}
			
			BundleContents.Mutable mutable = new BundleContents.Mutable(bundleContents);
			if (other.isEmpty()) {
				ItemStack itemStack = mutable.removeOne();
				if (itemStack != null) {
					this.playRemoveOneSound(player);
					access.set(itemStack);
				}
			} else {
				int i = mutable.tryInsert(other);
				if (i > 0) {
					this.playInsertSound(player);
				}
			}
			
			stack.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());
			return true;
		} else {
			return false;
		}
	}
	
	private void playRemoveOneSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}
	
	private void playInsertSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack itemStack = user.getItemInHand(hand);
		if (isWrapped(itemStack)) {
			super.use(world, user, hand);
		}
		return InteractionResultHolder.pass(itemStack);
	}
	
	// CraftingInventory does not recalculate the recipe after inputting / retrieving stacks from the present.
	// The recipes output will still hold the original present data from when it was put into the crafting grid
	// If the player then puts / receives items from the present they are able to duplicate items
	private boolean isCraftingInventory(Slot slot) {
		return slot.container instanceof TransientCraftingContainer;
	}
	
	@Override
	public void onCraftedBy(ItemStack stack, Level world, Player player) {
		super.onCraftedBy(stack, world, player);
		setOwner(stack, player);
	}
	
	@Override
	public boolean isBarVisible(ItemStack stack) {
		return !isWrapped(stack) && super.isBarVisible(stack);
	}
	
	public static Stream<ItemStack> getBundledStacks(ItemStack stack) {
		return stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY).itemCopyStream();
	}
	
	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		return !stack.has(DataComponents.HIDE_TOOLTIP) && !stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP)
				? Optional.ofNullable(stack.get(DataComponents.BUNDLE_CONTENTS)).map(BundleTooltip::new)
				: Optional.empty();
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		boolean wrapped = isWrapped(stack);
		if (wrapped) {
			var gifter = getOwner(stack);
			if (gifter.isPresent()) {
				gifter.get().name().ifPresent(name -> tooltip.add((Component.translatable("block.spectrum.present.tooltip.wrapped.giver", name).withStyle(ChatFormatting.GRAY))));
				if (type.isAdvanced()) {
					gifter.get().id().ifPresent(id -> tooltip.add((Component.literal("UUID: " + id).withStyle(ChatFormatting.GRAY))));
				}
			} else {
				tooltip.add((Component.translatable("block.spectrum.present.tooltip.wrapped").withStyle(ChatFormatting.GRAY)));
			}
		} else {
			tooltip.add((Component.translatable("block.spectrum.present.tooltip.description").withStyle(ChatFormatting.GRAY)));
			tooltip.add((Component.translatable("block.spectrum.present.tooltip.description2").withStyle(ChatFormatting.GRAY)));
			tooltip.add((Component.translatable("item.minecraft.bundle.fullness", getBundledStacks(stack).count(), 64)).withStyle(ChatFormatting.GRAY));
		}
	}
	
	@Override
	public boolean canFitInsideContainerItems() {
		return false;
	}
	
}
