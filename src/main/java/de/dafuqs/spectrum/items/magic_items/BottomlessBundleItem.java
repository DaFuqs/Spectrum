package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.interfaces.InventoryInsertionAcceptor;
import de.dafuqs.spectrum.items.tooltip.VoidBundleTooltipData;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BottomlessBundleItem extends BundleItem implements InventoryInsertionAcceptor {

	private static final int MAX_STORED_AMOUNT = 2000000000;

	public BottomlessBundleItem(Item.Settings settings) {
		super(settings);
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		if (dropOneBundledStack(itemStack, user)) {
			user.incrementStat(Stats.USED.getOrCreateStat(this));
			return TypedActionResult.success(itemStack, world.isClient());
		} else {
			return TypedActionResult.fail(itemStack);
		}
	}

	public boolean isItemBarVisible(ItemStack stack) {
		return getStoredAmount(stack) > 0;
	}

	public int getItemBarStep(ItemStack stack) {
		return Math.min(1 + (int) Math.round(12 * ((double) getStoredAmount(stack) / MAX_STORED_AMOUNT)), 13);
	}

	public int getItemBarColor(ItemStack stack) {
		return super.getItemBarColor(stack);
	}

	@Override
	public boolean canBeNested() {
		return false;
	}

	public Optional<TooltipData> getTooltipData(ItemStack voidBundleStack) {
		ItemStack itemStack = getFirstBundledStack(voidBundleStack);
		int storedAmount = getStoredAmount(voidBundleStack);
		
		return Optional.of(new VoidBundleTooltipData(itemStack, storedAmount));
	}

	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		int storedAmount = getStoredAmount(stack);
		if(storedAmount == 0) {
			tooltip.add(new TranslatableText("item.spectrum.void_bundle.tooltip.empty").formatted(Formatting.GRAY));
		} else {
			ItemStack firstStack = getFirstBundledStack(stack);
			String totalStacks = Support.getShortenedNumberString(storedAmount / (float) firstStack.getMaxCount());
			tooltip.add(new TranslatableText("item.spectrum.void_bundle.tooltip.count", storedAmount, MAX_STORED_AMOUNT, totalStacks).formatted(Formatting.GRAY));
			tooltip.add(new TranslatableText("item.spectrum.void_bundle.tooltip.enter_inventory", firstStack.getName().getString()).formatted(Formatting.GRAY));
		}
	}

	public void onItemEntityDestroyed(ItemEntity entity) {
		World world = entity.world;
		if (!world.isClient) {
			ItemStack voidBundleItemStack = entity.getStack();
			int currentAmount = getStoredAmount(voidBundleItemStack);
			if(currentAmount > 0) {
				ItemStack storedStack = getFirstBundledStack(voidBundleItemStack);
				while (currentAmount > 0) {
					int stackCount = Math.min(currentAmount, storedStack.getMaxCount());

					ItemStack entityStack = storedStack.copy();
					entityStack.setCount(stackCount);
					world.spawnEntity(new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), entityStack));
					
					currentAmount -= stackCount;
				}
			}
		}
	}

	/**
	 * When the bundle is clicked onto another stack
	 */
	public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
		if (clickType != ClickType.RIGHT) {
			return false;
		} else {
			ItemStack itemStack = slot.getStack();
			if (itemStack.isEmpty()) {
				removeFirstBundledStack(stack).ifPresent((removedStack) -> {
					addToBundle(stack, slot.insertStack(removedStack), player);
				});
			} else if (itemStack.getItem().canBeNested()) {
				ItemStack firstStack = getFirstBundledStack(stack);
				if(firstStack.isEmpty() || ItemStack.canCombine(firstStack, itemStack)) {
					int amountAbleToStore = Math.min(itemStack.getCount(), (MAX_STORED_AMOUNT - getStoredAmount(stack)));
					if(amountAbleToStore > 0) {
						addToBundle(stack, slot.takeStackRange(itemStack.getCount(), amountAbleToStore, player), player);
					}
				}
			}

			return true;
		}
	}

	/**
	 * When an itemStack is right clicked onto the bundle
	 */
	@Override
	public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
		if (clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
			if (otherStack.isEmpty()) {
				Optional<ItemStack> removedItemStack = removeFirstBundledStack(stack);
				Objects.requireNonNull(cursorStackReference);
				removedItemStack.ifPresent(cursorStackReference::set);
			} else {
				otherStack.decrement(addToBundle(stack, otherStack, player));
			}

			return true;
		} else {
			return false;
		}
	}

	private static int addToBundle(ItemStack bundle, ItemStack stackToBundle, PlayerEntity playerEntity) {
		if (!stackToBundle.isEmpty() && stackToBundle.getItem().canBeNested()) {
			int storedAmount = getStoredAmount(bundle);
			int amountAbleToStore = Math.min(stackToBundle.getCount(), (MAX_STORED_AMOUNT - getStoredAmount(bundle)));
			if (amountAbleToStore > 0) {
				ItemStack stackInBundle = getFirstBundledStack(bundle);
				if (stackInBundle.isEmpty()) {
					stackInBundle = stackToBundle.copy();
					stackInBundle.setCount(amountAbleToStore);
					bundleStack(bundle, stackInBundle, playerEntity);
					return amountAbleToStore;
				} else if (ItemStack.canCombine(stackInBundle, stackToBundle)) {
					stackInBundle.increment(amountAbleToStore);
					bundleStack(bundle, stackInBundle, storedAmount+amountAbleToStore, playerEntity);
					return amountAbleToStore;
				}
			}
		}
		return 0;
	}

	private static Optional<ItemStack> removeFirstBundledStack(ItemStack voidBundleStack) {
		ItemStack removedStack = getFirstBundledStack(voidBundleStack);
		if (removedStack.isEmpty()) {
			return Optional.empty();
		} else {
			removeBundledStackAmount(voidBundleStack, removedStack.getCount());
			return Optional.of(removedStack);
		}
	}

	private static boolean dropOneBundledStack(ItemStack voidBundleStack, PlayerEntity player) {
		ItemStack bundledStack = getFirstBundledStack(voidBundleStack);
		int storedAmount = getStoredAmount(voidBundleStack);
		
		int droppedAmount = Math.min(bundledStack.getMaxCount(), storedAmount);
		if(droppedAmount > 0) {
			player.dropItem(new ItemStack(bundledStack.getItem(), droppedAmount), true);
			removeBundledStackAmount(voidBundleStack, droppedAmount);
			return true;
		} else {
			return false;
		}
	}

	private static ItemStack getFirstBundledStack(ItemStack voidBundleStack) {
		NbtCompound nbtCompound = voidBundleStack.getNbt();
		if (nbtCompound == null) {
			return ItemStack.EMPTY;
		} else {
			return getFirstBundledStack(nbtCompound);
		}
	}
	
	private static ItemStack getFirstBundledStack(NbtCompound nbtCompound) {
		NbtCompound storedItemCompound = nbtCompound.getCompound("StoredStack");

		int storedAmount = storedItemCompound.getInt("Count");
		ItemStack itemStack = new ItemStack(Registry.ITEM.get(new Identifier(storedItemCompound.getString("ID"))));
		int stackAmount = Math.min(storedAmount, itemStack.getMaxCount());
		itemStack.setCount(stackAmount);

		if (storedItemCompound.contains("Tag", 10)) {
			itemStack.setNbt(storedItemCompound.getCompound("Tag"));
		}
		if (itemStack.getItem().isDamageable()) {
			itemStack.setDamage(itemStack.getDamage());
		}
		return itemStack;
	}

	private static int bundleStack(ItemStack voidBundleStack, ItemStack storedItemStack, PlayerEntity playerEntity) {
		return bundleStack(voidBundleStack, storedItemStack, storedItemStack.getCount(), playerEntity);
	}

	private static int bundleStack(ItemStack voidBundleStack, ItemStack storedItemStack, int amount, PlayerEntity playerEntity) {
		NbtCompound voidBundleCompound = voidBundleStack.getOrCreateNbt();
		NbtCompound storedItemCompound = new NbtCompound();

		Identifier identifier = Registry.ITEM.getId(storedItemStack.getItem());
		storedItemCompound.putString("ID", identifier.toString());
		storedItemCompound.putInt("Count", amount);
		if (storedItemStack.getNbt() != null) {
			storedItemCompound.put("Tag", storedItemStack.getNbt().copy());
		}

		voidBundleCompound.put("StoredStack", storedItemCompound);
		voidBundleStack.setNbt(voidBundleCompound);

		return Math.max(0, amount - MAX_STORED_AMOUNT);
	}

	private static int getStoredAmount(ItemStack voidBundleStack) {
		NbtCompound voidBundleCompound = voidBundleStack.getOrCreateNbt();
		if(voidBundleCompound.contains("StoredStack")) {
			NbtCompound storedStackCompound = voidBundleCompound.getCompound("StoredStack");
			if(storedStackCompound.contains("Count")) {
				return storedStackCompound.getInt("Count");
			}
		}
		return 0;
	}

	private static void removeBundledStackAmount(ItemStack voidBundleStack, int amount) {
		int storedAmount = getStoredAmount(voidBundleStack);

		NbtCompound voidBundleCompound = voidBundleStack.getOrCreateNbt();
		if (voidBundleCompound.contains("StoredStack")) {
			int remainingCount = storedAmount - amount;
			if (remainingCount > 0) {
				NbtCompound storedStackCompound = voidBundleCompound.getCompound("StoredStack");
				storedStackCompound.putInt("Count", remainingCount);
				voidBundleCompound.put("StoredStack", storedStackCompound);
			} else {
				voidBundleCompound.remove("StoredStack");
			}
			voidBundleStack.setNbt(voidBundleCompound);
		}
	}


	@Override
	public boolean acceptsItemStack(ItemStack inventoryInsertionAcceptorStack, ItemStack itemStackToAccept) {
		ItemStack storedStack = getFirstBundledStack(inventoryInsertionAcceptorStack);
		if(storedStack.isEmpty()) {
			return false;
		} else {
			return ItemStack.canCombine(storedStack, itemStackToAccept);
		}
	}

	@Override
	public int acceptItemStack(ItemStack inventoryInsertionAcceptorStack, ItemStack itemStackToAccept, PlayerEntity playerEntity) {
		int storedAmount = getStoredAmount(inventoryInsertionAcceptorStack);
		return bundleStack(inventoryInsertionAcceptorStack, itemStackToAccept, itemStackToAccept.getCount() + storedAmount, playerEntity);
	}

}