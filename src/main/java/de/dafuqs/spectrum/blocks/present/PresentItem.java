package de.dafuqs.spectrum.blocks.present;

import de.dafuqs.spectrum.items.tooltip.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.screen.slot.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;
import java.util.stream.*;

public class PresentItem extends BlockItem {
	
	private static final String ITEMS_KEY = "Items";
	public static final int MAX_STORAGE_STACKS = 5;
	private static final int ITEM_BAR_COLOR = MathHelper.packRgb(0.4F, 0.4F, 1.0F);
	
	public PresentItem(Block block, Settings settings) {
		super(block, settings);
	}
	
	
	@Override
	protected boolean canPlace(ItemPlacementContext context, BlockState state) {
		return isWrapped(context.getStack()) && super.canPlace(context, state);
	}
	
	public static boolean isWrapped(ItemStack itemStack) {
		return isWrapped(itemStack.getNbt());
	}
	
	public static boolean isWrapped(NbtCompound compound) {
		return compound != null && compound.getBoolean("Wrapped");
	}
	
	public static void setWrapper(ItemStack itemStack, PlayerEntity giver) {
		setWrapper(itemStack, giver.getUuid(), giver.getName().getString());
	}
	
	public static void setWrapper(ItemStack itemStack, UUID uuid, String name) {
		NbtCompound compound = itemStack.getOrCreateNbt();
		compound.putUuid("GiverUUID", uuid);
		compound.putString("Giver", name);
		itemStack.setNbt(compound);
	}
	
	public static Optional<Pair<UUID, String>> getWrapper(ItemStack itemStack) {
		return getWrapper(itemStack.getNbt());
	}
	
	public static Optional<Pair<UUID, String>> getWrapper(NbtCompound compound) {
		if (compound != null && compound.contains("GiverUUID") && compound.contains("Giver", NbtElement.STRING_TYPE)) {
			return Optional.of(new Pair<>(compound.getUuid("GiverUUID"), compound.getString("Giver")));
		}
		return Optional.empty();
	}
	
	public static Map<DyeColor, Integer> getColors(ItemStack itemStack) {
		return getColors(itemStack.getNbt());
	}
	
	public static Map<DyeColor, Integer> getColors(NbtCompound compound) {
		Map<DyeColor, Integer> colors = new HashMap<>();
		if (compound != null && compound.contains("Colors", NbtElement.LIST_TYPE)) {
			for (NbtElement e : compound.getList("Colors", NbtElement.COMPOUND_TYPE)) {
				NbtCompound c = (NbtCompound) e;
				colors.put(DyeColor.valueOf(c.getString("Color").toUpperCase(Locale.ROOT)), c.getInt("Amount"));
			}
		}
		return colors;
	}
	
	public static void wrap(ItemStack itemStack, PresentBlock.WrappingPaper wrappingPaper, Map<DyeColor, Integer> colors) {
		NbtCompound compound = itemStack.getOrCreateNbt();
		setWrapped(compound);
		setVariant(compound, wrappingPaper);
		setColors(compound, colors);
		itemStack.setNbt(compound);
	}
	
	public static void setWrapped(NbtCompound compound) {
		compound.putBoolean("Wrapped", true);
	}
	
	public static void setColors(NbtCompound compound, Map<DyeColor, Integer> colors) {
		if (!colors.isEmpty()) {
			NbtList colorList = new NbtList();
			for (Map.Entry<DyeColor, Integer> colorEntry : colors.entrySet()) {
				NbtCompound colorCompound = new NbtCompound();
				colorCompound.putString("Color", colorEntry.getKey().getName());
				colorCompound.putInt("Amount", colorEntry.getValue());
				colorList.add(colorCompound);
			}
			compound.put("Colors", colorList);
		}
	}
	
	public static void setVariant(NbtCompound compound, PresentBlock.WrappingPaper wrappingPaper) {
		compound.putString("Variant", wrappingPaper.asString());
	}
	
	public static PresentBlock.WrappingPaper getVariant(NbtCompound compound) {
		if (compound != null && compound.contains("Variant", NbtElement.STRING_TYPE)) {
			return PresentBlock.WrappingPaper.valueOf(compound.getString("Variant").toUpperCase(Locale.ROOT));
		}
		return PresentBlock.WrappingPaper.RED;
	}
	
	@Override
	public boolean onStackClicked(ItemStack present, Slot slot, ClickType clickType, PlayerEntity player) {
		if (clickType != ClickType.RIGHT) {
			return false;
		} else {
			ItemStack itemStack = slot.getStack();
			if (itemStack.isEmpty()) {
				this.playRemoveOneSound(player);
				removeFirstStack(present).ifPresent((removedStack) -> addToPresent(present, slot.insertStack(removedStack)));
			} else if (itemStack.getItem().canBeNested()) {
				ItemStack slotStack = slot.takeStackRange(itemStack.getCount(), 64, player);
				int acceptedStacks = addToPresent(present, slotStack);
				slotStack.decrement(acceptedStacks);
				if (!slotStack.isEmpty()) {
					slot.setStack(slotStack);
				}
				if (acceptedStacks > 0) {
					this.playInsertSound(player);
				}
			}
			
			return true;
		}
	}
	
	@Override
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
	
	@Override
	public void onCraft(ItemStack stack, World world, PlayerEntity player) {
		super.onCraft(stack, world, player);
		if (player != null) {
			setWrapper(stack, player);
		}
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		if (isWrapped(itemStack)) {
			super.use(world, user, hand);
		}
		return TypedActionResult.pass(itemStack);
	}
	
	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return !isWrapped(stack) && getBundledStacks(stack).findAny().isPresent();
	}
	
	@Override
	public int getItemBarStep(ItemStack stack) {
		return Math.min(1 + (int) (12 * (getBundledStacks(stack).count() / (float) MAX_STORAGE_STACKS)), 13);
	}
	
	@Override
	public int getItemBarColor(ItemStack stack) {
		return ITEM_BAR_COLOR;
	}

	public static int addToPresent(ItemStack present, ItemStack stackToAdd) {
		if (!stackToAdd.isEmpty() && stackToAdd.getItem().canBeNested()) {
			NbtCompound nbt = present.getOrCreateNbt();
			if (!nbt.contains(ITEMS_KEY)) {
				nbt.put(ITEMS_KEY, new NbtList());
			}
			
			NbtList nbtList = nbt.getList(ITEMS_KEY, 10);
			
			int originalCount = stackToAdd.getCount();
			for (int i = 0; i < MAX_STORAGE_STACKS; i++) {
				ItemStack storedStack = ItemStack.fromNbt(nbtList.getCompound(i));
				if (storedStack.isEmpty()) {
					NbtCompound leftoverCompound = new NbtCompound();
					stackToAdd.writeNbt(leftoverCompound);
					nbtList.add(leftoverCompound);
					present.setNbt(nbt);
					return originalCount;
				}
				if (ItemStack.canCombine(stackToAdd, storedStack)) {
					int additionalAmount = Math.min(stackToAdd.getCount(), storedStack.getMaxCount() - storedStack.getCount());
					if (additionalAmount > 0) {
						stackToAdd.decrement(additionalAmount);
						storedStack.increment(additionalAmount);
						
						NbtCompound newCompound = new NbtCompound();
						storedStack.writeNbt(newCompound);
						nbtList.set(i, newCompound);
						if (stackToAdd.isEmpty()) {
							present.setNbt(nbt);
							return originalCount;
						}
					}
				}
			}
			
			return originalCount - stackToAdd.getCount();
		}
		return 0;
	}
	
	private static Optional<ItemStack> removeFirstStack(ItemStack stack) {
		NbtCompound nbt = stack.getOrCreateNbt();
		if (!nbt.contains(ITEMS_KEY)) {
			return Optional.empty();
		} else {
			NbtList nbtList = nbt.getList(ITEMS_KEY, 10);
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
	
	public static Stream<ItemStack> getBundledStacks(ItemStack stack) {
		return getBundledStacks(stack.getNbt());
	}
	
	public static Stream<ItemStack> getBundledStacks(NbtCompound nbtCompound) {
		if (nbtCompound == null) {
			return Stream.empty();
		} else {
			NbtList nbtList = nbtCompound.getList(ITEMS_KEY, 10);
			Stream<NbtElement> stream = nbtList.stream();
			Objects.requireNonNull(NbtCompound.class);
			return stream.map(NbtCompound.class::cast).map(ItemStack::fromNbt);
		}
	}
	
	@Override
	public Optional<TooltipData> getTooltipData(ItemStack stack) {
		boolean wrapped = isWrapped(stack);
		if (wrapped) {
			return Optional.empty();
		}
		
		List<ItemStack> list = new ArrayList<>(MAX_STORAGE_STACKS);
		getBundledStacks(stack).forEachOrdered(list::add);
		while (list.size() < MAX_STORAGE_STACKS) {
			list.add(ItemStack.EMPTY);
		}
		return Optional.of(new PresentTooltipData(list));
	}
	
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		boolean wrapped = isWrapped(stack);
		if (wrapped) {
			Optional<Pair<UUID, String>> giver = getWrapper(stack);
			if (giver.isPresent()) {
				tooltip.add((Text.translatable("block.spectrum.present.tooltip.wrapped.giver", giver.get().getRight()).formatted(Formatting.GRAY)));
				if (context.isAdvanced()) {
					tooltip.add((Text.literal("UUID: " + giver.get().getLeft().toString()).formatted(Formatting.GRAY)));
				}
			} else {
				tooltip.add((Text.translatable("block.spectrum.present.tooltip.wrapped").formatted(Formatting.GRAY)));
			}
		} else {
			tooltip.add((Text.translatable("block.spectrum.present.tooltip.description").formatted(Formatting.GRAY)));
			tooltip.add((Text.translatable("block.spectrum.present.tooltip.description2").formatted(Formatting.GRAY)));
			
			DefaultedList<ItemStack> defaultedList = DefaultedList.of();
			Stream<ItemStack> bundledStacks = getBundledStacks(stack);
			bundledStacks.forEach(defaultedList::add);
			tooltip.add((Text.translatable("item.minecraft.bundle.fullness", defaultedList.size(), MAX_STORAGE_STACKS)).formatted(Formatting.GRAY));
		}
	}
	
	@Override
	public boolean canBeNested() {
		return false;
	}
	
	@Override
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
