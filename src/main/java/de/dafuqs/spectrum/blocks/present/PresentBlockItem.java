package de.dafuqs.spectrum.blocks.present;

import com.mojang.authlib.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.items.bundles.*;
import de.dafuqs.spectrum.items.tooltip.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
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

import java.util.*;
import java.util.stream.*;

public class PresentBlockItem extends PlaceableBundleBlockItem {
	
	public static final int MAX_STORAGE_STACKS = 5;
	
	public PresentBlockItem(Block block, Properties settings) {
		super(new ExtendedBundleComponent(MAX_STORAGE_STACKS), block, settings);
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
		return getWrapData(itemStack).wrapped();
	}
	
	public static WrappedPresentComponent getWrapData(ItemStack itemStack) {
		return itemStack.getOrDefault(SpectrumDataComponentTypes.WRAPPED_PRESENT, WrappedPresentComponent.DEFAULT);
	}
	
	public static void wrap(ItemStack itemStack, PresentBlock.WrappingPaper wrappingPaper, Map<Integer, Integer> colors) {
		itemStack.set(SpectrumDataComponentTypes.WRAPPED_PRESENT, new WrappedPresentComponent(true, wrappingPaper, colors));
	}
	
	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack otherStack, Slot slot, ClickAction clickType, Player player, SlotAccess cursorStackReference) {
		return !isCraftingInventory(slot) && super.overrideOtherStackedOnMe(stack, otherStack, slot, clickType, player, cursorStackReference);
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
		if (player != null) {
			setOwner(stack, player);
		}
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
		if (isWrapped(stack)) {
			return Optional.empty();
		}
		
		// TODO: Use BundleTooltipComponent and such instead
		var list = NonNullList.withSize(MAX_STORAGE_STACKS, ItemStack.EMPTY);
		var stacks = getBundledStacks(stack).toList();
		for (int i = 0; i < stacks.size(); i++)
			list.set(i, stacks.get(i));
		return Optional.of(new PresentTooltipData(list));
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
			tooltip.add((Component.translatable("item.minecraft.bundle.fullness", getBundledStacks(stack).count(), MAX_STORAGE_STACKS)).withStyle(ChatFormatting.GRAY));
		}
	}
	
	@Override
	public boolean canFitInsideContainerItems() {
		return false;
	}
	
}
