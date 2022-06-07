package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.enchanter.EnchanterEnchantable;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.interfaces.InventoryInsertionAcceptor;
import de.dafuqs.spectrum.items.tooltip.VoidBundleTooltipData;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class BottomlessBundleItem extends BundleItem implements InventoryInsertionAcceptor, EnchanterEnchantable {
	
	private static final int MAX_STORED_AMOUNT_BASE = 20000;
	
	public BottomlessBundleItem(Item.Settings settings) {
		super(settings);
	}
	
	public static ItemStack getWithBlockAndCount(ItemStack itemStack, int amount) {
		ItemStack bottomlessBundleStack = new ItemStack(SpectrumItems.BOTTOMLESS_BUNDLE);
		BottomlessBundleItem.bundleStack(bottomlessBundleStack, itemStack, amount);
		return bottomlessBundleStack;
	}
	
	public static int getMaxStoredAmount(ItemStack itemStack) {
		int powerLevel = EnchantmentHelper.getLevel(Enchantments.POWER, itemStack);
		return MAX_STORED_AMOUNT_BASE * (int) Math.pow(10, Math.min(5, powerLevel)); // to not exceed int max
	}
	
	/**
	 * @param voidBundleStack
	 * @param stackToBundle
	 * @return The amount of items put into the bundle
	 */
	public static int addToBundle(ItemStack voidBundleStack, ItemStack stackToBundle) {
		if (!stackToBundle.isEmpty() && stackToBundle.getItem().canBeNested()) {
			int storedAmount = getStoredAmount(voidBundleStack);
			int amountAbleToStore = Math.min(stackToBundle.getCount(), (getMaxStoredAmount(voidBundleStack) - getStoredAmount(voidBundleStack)));
			if (amountAbleToStore > 0) {
				ItemStack stackInBundle = getFirstBundledStack(voidBundleStack);
				if (stackInBundle.isEmpty()) {
					stackInBundle = stackToBundle.copy();
					stackInBundle.setCount(amountAbleToStore);
					bundleStack(voidBundleStack, stackInBundle);
					return amountAbleToStore;
				} else if (ItemStack.canCombine(stackInBundle, stackToBundle)) {
					stackInBundle.increment(amountAbleToStore);
					bundleStack(voidBundleStack, stackInBundle, storedAmount + amountAbleToStore);
					return amountAbleToStore;
				}
			}
		}
		return 0;
	}
	
	public static Optional<ItemStack> removeFirstBundledStack(ItemStack voidBundleStack) {
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
		if (droppedAmount > 0) {
			ItemStack stackToDrop = bundledStack.copy();
			stackToDrop.setCount(droppedAmount);
			player.dropItem(stackToDrop, true);
			removeBundledStackAmount(voidBundleStack, droppedAmount);
			return true;
		} else {
			return false;
		}
	}
	
	public static ItemStack getFirstBundledStack(ItemStack voidBundleStack) {
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
	
	private static int bundleStack(ItemStack voidBundleStack, ItemStack storedItemStack) {
		return bundleStack(voidBundleStack, storedItemStack, storedItemStack.getCount());
	}
	
	private static int bundleStack(ItemStack voidBundleStack, ItemStack storedItemStack, int amount) {
		NbtCompound voidBundleCompound = voidBundleStack.getOrCreateNbt();
		NbtCompound storedItemCompound = new NbtCompound();
		
		boolean hasVoiding = EnchantmentHelper.getLevel(SpectrumEnchantments.VOIDING, voidBundleStack) > 0;
		int maxStoredAmount = getMaxStoredAmount(voidBundleStack);
		int newAmount = Math.min(maxStoredAmount, storedItemCompound.getInt("Count") + amount);
		int overflowAmount = hasVoiding ? 0 : Math.max(0, amount - maxStoredAmount);
		
		Identifier identifier = Registry.ITEM.getId(storedItemStack.getItem());
		storedItemCompound.putString("ID", identifier.toString());
		storedItemCompound.putInt("Count", newAmount);
		if (storedItemStack.getNbt() != null) {
			storedItemCompound.put("Tag", storedItemStack.getNbt().copy());
		}
		
		voidBundleCompound.put("StoredStack", storedItemCompound);
		voidBundleStack.setNbt(voidBundleCompound);
		
		return overflowAmount;
	}
	
	public static int getStoredAmount(ItemStack voidBundleStack) {
		NbtCompound voidBundleCompound = voidBundleStack.getOrCreateNbt();
		if (voidBundleCompound.contains("StoredStack")) {
			NbtCompound storedStackCompound = voidBundleCompound.getCompound("StoredStack");
			if (storedStackCompound.contains("Count")) {
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
	
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		if (dropOneBundledStack(itemStack, user)) {
			this.playDropContentsSound(user);
			user.incrementStat(Stats.USED.getOrCreateStat(this));
			return TypedActionResult.success(itemStack, world.isClient());
		} else {
			return TypedActionResult.fail(itemStack);
		}
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (context.getPlayer().isSneaking()) {
			// place as block
			return this.place(new ItemPlacementContext(context));
		}
		return super.useOnBlock(context);
	}
	
	public ActionResult place(ItemPlacementContext itemPlacementContext) {
		if (!itemPlacementContext.canPlace()) {
			return ActionResult.FAIL;
		} else {
			BlockState blockState = this.getPlacementState(itemPlacementContext);
			if (blockState == null) {
				return ActionResult.FAIL;
			} else if (!this.place(itemPlacementContext, blockState)) {
				return ActionResult.FAIL;
			} else {
				BlockPos blockPos = itemPlacementContext.getBlockPos();
				World world = itemPlacementContext.getWorld();
				PlayerEntity playerEntity = itemPlacementContext.getPlayer();
				ItemStack itemStack = itemPlacementContext.getStack();
				BlockState blockState2 = world.getBlockState(blockPos);
				if (blockState2.isOf(blockState.getBlock())) {
					blockState2.getBlock().onPlaced(world, blockPos, blockState2, playerEntity, itemStack);
					if (playerEntity instanceof ServerPlayerEntity) {
						Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) playerEntity, blockPos, itemStack);
					}
				}
				
				BlockSoundGroup blockSoundGroup = blockState2.getSoundGroup();
				world.playSound(playerEntity, blockPos, this.getPlaceSound(blockState2), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);
				world.emitGameEvent(playerEntity, GameEvent.BLOCK_PLACE, blockPos);
				if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
					itemStack.decrement(1);
				}
				
				return ActionResult.success(world.isClient);
			}
		}
	}
	
	protected boolean place(ItemPlacementContext context, BlockState state) {
		return context.getWorld().setBlockState(context.getBlockPos(), state, 11);
	}
	
	protected SoundEvent getPlaceSound(BlockState state) {
		return state.getSoundGroup().getPlaceSound();
	}
	
	@Nullable
	protected BlockState getPlacementState(ItemPlacementContext context) {
		BlockState blockState = SpectrumBlocks.BOTTOMLESS_BUNDLE.getPlacementState(context);
		return blockState != null && this.canPlace(context, blockState) ? blockState : null;
	}
	
	protected boolean canPlace(ItemPlacementContext context, BlockState state) {
		PlayerEntity playerEntity = context.getPlayer();
		ShapeContext shapeContext = playerEntity == null ? ShapeContext.absent() : ShapeContext.of(playerEntity);
		return state.canPlaceAt(context.getWorld(), context.getBlockPos()) && context.getWorld().canPlace(state, context.getBlockPos(), shapeContext);
	}
	
	public boolean isItemBarVisible(ItemStack stack) {
		return getStoredAmount(stack) > 0;
	}
	
	public int getItemBarStep(ItemStack stack) {
		return Math.min(1 + (int) Math.round(12 * ((double) getStoredAmount(stack) / getMaxStoredAmount(stack))), 13);
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
		if (storedAmount == 0) {
			tooltip.add(new TranslatableText("item.spectrum.void_bundle.tooltip.empty").formatted(Formatting.GRAY));
		} else {
			ItemStack firstStack = getFirstBundledStack(stack);
			String totalStacks = Support.getShortenedNumberString(storedAmount / (float) firstStack.getMaxCount());
			tooltip.add(new TranslatableText("item.spectrum.void_bundle.tooltip.count", storedAmount, getMaxStoredAmount(stack), totalStacks).formatted(Formatting.GRAY));
			tooltip.add(new TranslatableText("item.spectrum.void_bundle.tooltip.enter_inventory", firstStack.getName().getString()).formatted(Formatting.GRAY));
		}
		if (EnchantmentHelper.getLevel(SpectrumEnchantments.VOIDING, stack) > 0) {
			tooltip.add(new TranslatableText("item.spectrum.void_bundle.tooltip.voiding"));
		}
	}
	
	public void onItemEntityDestroyed(ItemEntity entity) {
		World world = entity.world;
		if (!world.isClient) {
			ItemStack voidBundleItemStack = entity.getStack();
			int currentAmount = getStoredAmount(voidBundleItemStack);
			if (currentAmount > 0) {
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
				playRemoveOneSound(player);
				removeFirstBundledStack(stack).ifPresent((removedStack) -> {
					addToBundle(stack, slot.insertStack(removedStack));
				});
			} else if (itemStack.getItem().canBeNested()) {
				ItemStack firstStack = getFirstBundledStack(stack);
				if (firstStack.isEmpty() || ItemStack.canCombine(firstStack, itemStack)) {
					boolean hasVoiding = EnchantmentHelper.getLevel(SpectrumEnchantments.VOIDING, stack) > 0;
					int amountAbleToStore = hasVoiding ? itemStack.getCount() : Math.min(itemStack.getCount(), (getMaxStoredAmount(stack) - getStoredAmount(stack)));
					if (amountAbleToStore > 0) {
						addToBundle(stack, slot.takeStackRange(itemStack.getCount(), amountAbleToStore, player));
						this.playInsertSound(player);
					}
				}
			}
			
			return true;
		}
	}
	
	/**
	 * When an itemStack is right-clicked onto the bundle
	 */
	@Override
	public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
		if (clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
			if (otherStack.isEmpty()) {
				removeFirstBundledStack(stack).ifPresent((itemStack) -> {
					this.playRemoveOneSound(player);
					cursorStackReference.set(itemStack);
				});
			} else {
				int storedAmount = addToBundle(stack, otherStack);
				if (storedAmount > 0) {
					this.playInsertSound(player);
					otherStack.decrement(storedAmount);
				}
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
		
		// tick stack inside the bundle. I hope that kinda wrong slot reference does not break anything
		ItemStack bundledStack = BottomlessBundleItem.getFirstBundledStack(stack);
		if (!bundledStack.isEmpty()) {
			int amount = BottomlessBundleItem.getStoredAmount(stack);
			bundledStack.setCount(amount);
			bundledStack.getItem().inventoryTick(bundledStack, world, entity, slot, selected);
		}
	}
	
	@Override
	public boolean acceptsItemStack(ItemStack inventoryInsertionAcceptorStack, ItemStack itemStackToAccept) {
		ItemStack storedStack = getFirstBundledStack(inventoryInsertionAcceptorStack);
		if (storedStack.isEmpty()) {
			return false;
		} else {
			return ItemStack.canCombine(storedStack, itemStackToAccept);
		}
	}
	
	@Override
	public int acceptItemStack(ItemStack inventoryInsertionAcceptorStack, ItemStack itemStackToAccept, PlayerEntity playerEntity) {
		int storedAmount = getStoredAmount(inventoryInsertionAcceptorStack);
		return bundleStack(inventoryInsertionAcceptorStack, itemStackToAccept, itemStackToAccept.getCount() + storedAmount);
	}
	
	private void playRemoveOneSound(Entity entity) {
		entity.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
	}
	
	private void playInsertSound(Entity entity) {
		entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
	}
	
	private void playDropContentsSound(Entity entity) {
		entity.playSound(SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
	}
	
	@Override
	public boolean canAcceptEnchantment(Enchantment enchantment) {
		return enchantment == Enchantments.POWER || enchantment == SpectrumEnchantments.VOIDING;
	}
	
	@Override
	public int getEnchantability() {
		return 5;
	}
	
	public static class BottomlessBundlePlacementDispenserBehavior extends FallibleItemDispenserBehavior {
		
		protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
			this.setSuccess(false);
			Item item = stack.getItem();
			if (item instanceof BottomlessBundleItem bottomlessBundleItem) {
				Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
				BlockPos blockPos = pointer.getPos().offset(direction);
				Direction direction2 = pointer.getWorld().isAir(blockPos.down()) ? direction : Direction.UP;
				
				try {
					this.setSuccess(bottomlessBundleItem.place(new AutomaticItemPlacementContext(pointer.getWorld(), blockPos, direction, stack, direction2)).isAccepted());
				} catch (Exception e) {
					SpectrumCommon.logError("Error trying to place bottomless bundle at " + blockPos + " : " + e);
				}
			}
			
			return stack;
		}
	}
	
	
}