package de.dafuqs.spectrum.blocks.bottomless_bundle;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.enchanter.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.items.tooltip.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.*;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.*;
import net.minecraft.screen.slot.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.stat.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import org.jetbrains.annotations.*;

import java.util.*;

import de.dafuqs.spectrum.helpers.CustomItemRender.Stack.Extra.RenderRecursionGuard;

public class BottomlessBundleItem extends BundleItem implements InventoryInsertionAcceptor, ExtendedEnchantable {
	
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
	
	public static boolean isLocked(ItemStack itemStack) {
		NbtCompound compound = itemStack.getNbt();
		if (compound != null) {
			return compound.getBoolean("Locked");
		}
		return false;
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
		ItemStack itemStack = new ItemStack(Registries.ITEM.get(new Identifier(storedItemCompound.getString("ID"))));
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
	
	private static void bundleStack(ItemStack voidBundleStack, ItemStack stackToBundle) {
		bundleStack(voidBundleStack, stackToBundle, stackToBundle.getCount());
	}
	
	private static int bundleStack(ItemStack voidBundleStack, ItemStack stackToBundle, int amount) {
		NbtCompound voidBundleCompound = voidBundleStack.getOrCreateNbt();
		NbtCompound storedItemCompound = new NbtCompound();
		
		boolean hasVoiding = EnchantmentHelper.getLevel(SpectrumEnchantments.VOIDING, voidBundleStack) > 0;
		int maxStoredAmount = getMaxStoredAmount(voidBundleStack);
		int newAmount = Math.min(maxStoredAmount, storedItemCompound.getInt("Count") + amount);
		int overflowAmount = hasVoiding ? 0 : Math.max(0, amount - maxStoredAmount);

		Identifier identifier = Registries.ITEM.getId(stackToBundle.getItem());
		storedItemCompound.putString("ID", identifier.toString());
		storedItemCompound.putInt("Count", newAmount);
		if (stackToBundle.getNbt() != null) {
			storedItemCompound.put("Tag", stackToBundle.getNbt().copy());
		}

		voidBundleCompound.put("StoredStack", storedItemCompound);
		voidBundleStack.setNbt(voidBundleCompound);

		return overflowAmount;
	}
	
	protected static void setBundledStack(ItemStack voidBundleStack, ItemStack stackToBundle, int amount) {
		if (stackToBundle.isEmpty() || amount <= 0) {
			voidBundleStack.removeSubNbt("StoredStack");
		} else {
			NbtCompound voidBundleCompound = voidBundleStack.getOrCreateNbt();
			NbtCompound storedItemCompound = new NbtCompound();
			int maxStoredAmount = getMaxStoredAmount(voidBundleStack);
			int newAmount = Math.min(maxStoredAmount, amount);

			Identifier identifier = Registries.ITEM.getId(stackToBundle.getItem());
			storedItemCompound.putString("ID", identifier.toString());
			storedItemCompound.putInt("Count", newAmount);
			if (stackToBundle.getNbt() != null) {
				storedItemCompound.put("Tag", stackToBundle.getNbt().copy());
			}

			voidBundleCompound.put("StoredStack", storedItemCompound);
			voidBundleStack.setNbt(voidBundleCompound);
			
		}
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
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		if (user.isSneaking()) {
			ItemStack handStack = user.getStackInHand(hand);
			NbtCompound compound = handStack.getOrCreateNbt();
			if (compound.contains("Locked")) {
				compound.remove("Locked");
				if (world.isClient) {
					playZipSound(user, 0.8F);
				}
			} else {
				compound.putBoolean("Locked", true);
				if (world.isClient) {
					playZipSound(user, 1.0F);
				}
			}
			return TypedActionResult.success(itemStack, world.isClient());
		} else if (dropOneBundledStack(itemStack, user)) {
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
	
	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return getStoredAmount(stack) > 0;
	}
	
	@Override
	public int getItemBarStep(ItemStack stack) {
		return Math.min(1 + (int) Math.round(12 * ((double) getStoredAmount(stack) / getMaxStoredAmount(stack))), 13);
	}
	
	@Override
	public int getItemBarColor(ItemStack stack) {
		return super.getItemBarColor(stack);
	}
	
	@Override
	public boolean canBeNested() {
		return false;
	}
	
	@Override
	public Optional<TooltipData> getTooltipData(ItemStack voidBundleStack) {
		ItemStack itemStack = getFirstBundledStack(voidBundleStack);
		int storedAmount = getStoredAmount(voidBundleStack);
		
		return Optional.of(new BottomlessBundleTooltipData(itemStack, storedAmount));
	}
	
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		boolean locked = isLocked(stack);
		int storedAmount = getStoredAmount(stack);
		if (storedAmount == 0) {
			tooltip.add(Text.translatable("item.spectrum.bottomless_bundle.tooltip.empty").formatted(Formatting.GRAY));
			if (locked) {
				tooltip.add(Text.translatable("item.spectrum.bottomless_bundle.tooltip.locked").formatted(Formatting.GRAY));
			}
		} else {
			ItemStack firstStack = getFirstBundledStack(stack);
			String totalStacks = Support.getShortenedNumberString(storedAmount / (float) firstStack.getMaxCount());
			tooltip.add(Text.translatable("item.spectrum.bottomless_bundle.tooltip.count", storedAmount, getMaxStoredAmount(stack), totalStacks).formatted(Formatting.GRAY));
			if (locked) {
				tooltip.add(Text.translatable("item.spectrum.bottomless_bundle.tooltip.locked").formatted(Formatting.GRAY));
			} else {
				tooltip.add(Text.translatable("item.spectrum.bottomless_bundle.tooltip.enter_inventory", firstStack.getName().getString()).formatted(Formatting.GRAY));
			}
		}
		if (EnchantmentHelper.getLevel(SpectrumEnchantments.VOIDING, stack) > 0) {
			tooltip.add(Text.translatable("item.spectrum.bottomless_bundle.tooltip.voiding"));
		}
	}
	
	@Override
	public void onItemEntityDestroyed(ItemEntity entity) {
		World world = entity.getWorld();
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
	@Override
	public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
		if (clickType != ClickType.RIGHT) {
			return false;
		} else {
			ItemStack itemStack = slot.getStack();
			if (itemStack.isEmpty()) {
				playRemoveOneSound(player);
				removeFirstBundledStack(stack).ifPresent((removedStack) -> addToBundle(stack, slot.insertStack(removedStack)));
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
		if (isLocked(inventoryInsertionAcceptorStack)) {
			return itemStackToAccept.getCount();
		}
		
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
	
	private void playZipSound(Entity entity, float basePitch) {
		entity.playSound(SpectrumSoundEvents.BOTTOMLESS_BUNDLE_ZIP, 0.8F, basePitch + entity.getWorld().getRandom().nextFloat() * 0.4F);
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return stack.getCount() == 1;
	}
	
	@Override
	public boolean acceptsEnchantment(Enchantment enchantment) {
		return enchantment == Enchantments.POWER || enchantment == SpectrumEnchantments.VOIDING;
	}
	
	@Override
	public int getEnchantability() {
		return 5;
	}

	// impl of CustomItemRender
	@Override
	public boolean shouldRender(ItemStack stack, ModelTransformationMode mode) {
		return super.shouldRender(stack, mode)
				&& mode == ModelTransformationMode.GUI
				&& getStoredAmount(stack) > 0;
	}
	@Override
	public void render(ItemRenderer instance, ItemStack stack, ModelTransformationMode mode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model) {
		try (RenderRecursionGuard ignored = new RenderRecursionGuard(stack)) {
			instance.renderItem(stack, mode, leftHanded, matrices, vertexConsumers, light, overlay, model);

			ItemStack bundledStack = BottomlessBundleItem.getFirstBundledStack(stack);
			MinecraftClient client = MinecraftClient.getInstance();
			bundledStack.setCurrentlyRendering(true); // prevent potential recursion; could use another guard(that is, if the guard becomes the stable part of Extra)
			BakedModel bundledModel = instance.getModel(bundledStack, client.world, client.player, 0);

			matrices.push();
			matrices.scale(0.5F, 0.5F, 0.5F);
			matrices.translate(0.5F, 0.5F, 0.5F);
			instance.renderItem(bundledStack, mode, leftHanded, matrices, vertexConsumers, light, overlay, bundledModel);
			matrices.pop();
		}
	}

	public static class BottomlessBundlePlacementDispenserBehavior extends FallibleItemDispenserBehavior {
		
		@Override
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
