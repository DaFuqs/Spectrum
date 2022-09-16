package de.dafuqs.spectrum.blocks.present;

import de.dafuqs.spectrum.items.tooltip.PresentTooltipData;
import net.minecraft.block.Block;
import net.minecraft.client.item.BundleTooltipData;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class PresentItem extends BlockItem {
	
	private static final String ITEMS_KEY = "Items";
	public static final int MAX_STORAGE_STACKS = 5;
	private static final int ITEM_BAR_COLOR = MathHelper.packRgb(0.4F, 0.4F, 1.0F);
	
	public PresentItem(Block block, Settings settings) {
		super(block, settings);
	}
	
	public static boolean isWrapped(ItemStack itemStack) {
		NbtCompound compound = itemStack.getNbt();
		return compound != null && compound.getBoolean("Wrapped");
	}
	
	public boolean onStackClicked(ItemStack present, Slot slot, ClickType clickType, PlayerEntity player) {
		if (clickType != ClickType.RIGHT) {
			return false;
		} else {
			ItemStack itemStack = slot.getStack();
			if (itemStack.isEmpty()) {
				this.playRemoveOneSound(player);
				removeFirstStack(present).ifPresent((removedStack) -> {
					addToPresent(present, slot.insertStack(removedStack));
				});
			} else if (itemStack.getItem().canBeNested()) {
				ItemStack slotStack = slot.takeStackRange(itemStack.getCount(), 64, player);
				int acceptedStacks = addToPresent(present, slotStack);
				slotStack.decrement(acceptedStacks);
				if(!slotStack.isEmpty()) {
					slot.setStack(slotStack);
				}
				if (acceptedStacks > 0) {
					this.playInsertSound(player);
				}
			}
			
			return true;
		}
	}
	
	public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
		if (clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
			if (otherStack.isEmpty()) {
				removeFirstStack(stack).ifPresent((itemStack) -> {
					this.playRemoveOneSound(player);
					cursorStackReference.set(itemStack);
				});
			} else {
				int i = addToPresent(stack, otherStack);
				if (i > 0) {
					this.playInsertSound(player);
					otherStack.decrement(i);
				}
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		if(isWrapped(itemStack)) {
			super.use(world, user, hand);
		}
		return TypedActionResult.pass(itemStack);
	}
	
	public boolean isItemBarVisible(ItemStack stack) {
		return !isWrapped(stack) && getBundledStacks(stack).findAny().isPresent();
	}
	
	public int getItemBarStep(ItemStack stack) {
		return Math.min(1 + 12 * (((int) getBundledStacks(stack).count()) / MAX_STORAGE_STACKS), 13);
	}
	
	public int getItemBarColor(ItemStack stack) {
		return ITEM_BAR_COLOR;
	}
	
	private static int addToPresent(ItemStack bundle, ItemStack stackToBundle) {
		if (!stackToBundle.isEmpty() && stackToBundle.getItem().canBeNested()) {
			NbtCompound bundleCompound = bundle.getOrCreateNbt();
			if (!bundleCompound.contains(ITEMS_KEY)) {
				bundleCompound.put(ITEMS_KEY, new NbtList());
			}
			NbtList nbtList = bundleCompound.getList(ITEMS_KEY, 10);
			
			int originalCount = stackToBundle.getCount();
			for(int i = 0; i < MAX_STORAGE_STACKS; i++) {
				ItemStack storedStack = ItemStack.fromNbt(nbtList.getCompound(i));
				if(storedStack.isEmpty()) {
					NbtCompound leftoverCompound = new NbtCompound();
					stackToBundle.writeNbt(leftoverCompound);
					nbtList.add(leftoverCompound);
					bundle.setNbt(bundleCompound);
					return originalCount;
				}
				if(ItemStack.canCombine(stackToBundle, storedStack)) {
					int additionalAmount = Math.min(stackToBundle.getCount(), storedStack.getMaxCount() - storedStack.getCount());
					if(additionalAmount > 0) {
						stackToBundle.decrement(additionalAmount);
						storedStack.increment(additionalAmount);
						
						NbtCompound newCompound = new NbtCompound();
						storedStack.writeNbt(newCompound);
						nbtList.set(i, newCompound);
						if (stackToBundle.isEmpty()) {
							bundle.setNbt(bundleCompound);
							return originalCount;
						}
					}
				}
			}
			
			return originalCount - stackToBundle.getCount();
		}
		return 0;
	}
	
	private static Optional<ItemStack> removeFirstStack(ItemStack stack) {
		NbtCompound nbtCompound = stack.getOrCreateNbt();
		if (!nbtCompound.contains(ITEMS_KEY)) {
			return Optional.empty();
		} else {
			NbtList nbtList = nbtCompound.getList(ITEMS_KEY, 10);
			if (nbtList.isEmpty()) {
				return Optional.empty();
			} else {
				int i = 0;
				NbtCompound nbtCompound2 = nbtList.getCompound(i);
				ItemStack itemStack = ItemStack.fromNbt(nbtCompound2);
				nbtList.remove(i);
				if (nbtList.isEmpty()) {
					stack.removeSubNbt(ITEMS_KEY);
				}
				
				return Optional.of(itemStack);
			}
		}
	}
	
	private static Stream<ItemStack> getBundledStacks(ItemStack stack) {
		NbtCompound nbtCompound = stack.getNbt();
		if (nbtCompound == null) {
			return Stream.empty();
		} else {
			NbtList nbtList = nbtCompound.getList(ITEMS_KEY, 10);
			Stream stream = nbtList.stream();
			Objects.requireNonNull(NbtCompound.class);
			return stream.map(NbtCompound.class::cast).map(c -> ItemStack.fromNbt((NbtCompound) c));
		}
	}
	
	public Optional<TooltipData> getTooltipData(ItemStack stack) {
		List<ItemStack> list = new ArrayList<>(MAX_STORAGE_STACKS);
		getBundledStacks(stack).forEachOrdered(s -> list.add(s));
		while(list.size() < MAX_STORAGE_STACKS) {
			list.add(ItemStack.EMPTY);
		}
		return Optional.of(new PresentTooltipData(list));
	}
	
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		DefaultedList<ItemStack> defaultedList = DefaultedList.of();
		Stream<ItemStack> bundledStacks = getBundledStacks(stack);
		bundledStacks.forEach(defaultedList::add);
		tooltip.add((new TranslatableText("item.minecraft.bundle.fullness", defaultedList.size(), MAX_STORAGE_STACKS)).formatted(Formatting.GRAY));
	}
	
	public void onItemEntityDestroyed(ItemEntity entity) {
		ItemUsage.spawnItemContents(entity, getBundledStacks(entity.getStack()));
	}
	
	private void playRemoveOneSound(Entity entity) {
		entity.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
	}
	
	private void playInsertSound(Entity entity) {
		entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
	}
	
}
