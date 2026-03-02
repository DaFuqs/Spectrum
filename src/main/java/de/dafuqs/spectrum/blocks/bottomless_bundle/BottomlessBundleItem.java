package de.dafuqs.spectrum.blocks.bottomless_bundle;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.tooltip.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.*;
import net.minecraft.core.dispenser.*;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.inventory.tooltip.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.neoforged.api.distmarker.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BottomlessBundleItem extends BlockItem {
	
	private static final long MAX_STORED_AMOUNT_BASE = 20000;
	
	public BottomlessBundleItem(Block block, Item.Properties settings) {
		super(block, settings);
	}
	
	public static long getMaxStoredAmount(int powerLevel) {
		return MAX_STORED_AMOUNT_BASE * (int) Math.pow(10, Math.min(5, powerLevel)); // to not exceed int max
	}
	
	private static boolean dropOneBundledStack(ItemStack stack, Player player) {
		BottomlessItemHandlerComponent bottomlessComponent = BottomlessItemHandlerComponent.get(stack, player.level().registryAccess(), true);
		BottomlessItemHandler storage = bottomlessComponent.handler();
		
		if (!storage.variant.isEmpty() && storage.count > 0) {
			int extractCount = Math.min(storage.variant.getItem().getDefaultMaxStackSize(), (int) Math.min(Integer.MAX_VALUE, storage.count));
			ItemStack removed = storage.variant.copyWithCount(extractCount);
			storage.count -= extractCount;
			player.drop(removed, true);
			
			stack.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, new BottomlessItemHandlerComponent(storage));
			return true;
		}
		return false;
	}
	
	@Override
	public void verifyComponentsAfterLoad(ItemStack stack) {
		super.verifyComponentsAfterLoad(stack);
	}
	
	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player user, @NotNull InteractionHand hand) {
		ItemStack handStack = user.getItemInHand(hand);
		if (user.isShiftKeyDown()) {
			
			BottomlessItemHandlerComponent bottomlessComponent = BottomlessItemHandlerComponent.get(handStack, user.level().registryAccess(), true);
			BottomlessItemHandler storage = bottomlessComponent.handler();
			
			if(bottomlessComponent.handler().locked()) {
				handStack.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, new BottomlessItemHandlerComponent(
						storage.capacity(),
						storage.deletesOverflow(),
						false,
						storage.variant(),
						storage.count())
				);
				if (level.isClientSide) {
					playZipSound(user, 0.8F);
				}
			} else {
				handStack.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, new BottomlessItemHandlerComponent(
						storage.capacity(),
						storage.deletesOverflow(),
						true,
						storage.variant(),
						storage.count())
				);
				if (level.isClientSide) {
					playZipSound(user, 1.0F);
				}
				
				handStack.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, bottomlessComponent);
			}
			
			return InteractionResultHolder.sidedSuccess(handStack, level.isClientSide());
		} else if (dropOneBundledStack(handStack, user)) {
			this.playDropContentsSound(user);
			user.awardStat(Stats.ITEM_USED.get(this));
			return InteractionResultHolder.sidedSuccess(handStack, level.isClientSide());
		} else {
			return InteractionResultHolder.fail(handStack);
		}
	}
	
	@Override
	public boolean canFitInsideContainerItems() {
		return false;
	}
	
	@Override
	public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack bundleStack) {
		BottomlessItemHandlerComponent itemHandler = bundleStack.get(SpectrumDataComponentTypes.BOTTOMLESS_STACK);
		if(itemHandler == null) {
			return Optional.empty();
		}
		return Optional.of(new BottomlessBundleTooltipData(itemHandler.handler()));
	}
	
	@Override
	public void appendHoverText(@NotNull ItemStack bundleStack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
		BottomlessItemHandlerComponent component = BottomlessItemHandlerComponent.get(bundleStack, context.level() == null ? null : context.level().registryAccess(), false);
		BottomlessItemHandler itemHandler = component.handler();
		boolean locked = itemHandler.locked();
		long storedAmount = itemHandler.count();
		if (storedAmount == 0) {
			tooltip.add(Component.translatable("item.spectrum.bottomless_bundle.tooltip.empty").withStyle(ChatFormatting.GRAY));
			if (locked) {
				tooltip.add(Component.translatable("item.spectrum.bottomless_bundle.tooltip.locked").withStyle(ChatFormatting.GRAY));
			}
		} else {
			ItemStack variant = itemHandler.variant();
			String totalStacks = Support.getShortenedNumberString(storedAmount / (float) variant.getItem().getDefaultMaxStackSize());
			tooltip.add(Component.translatable("item.spectrum.bottomless_bundle.tooltip.count", storedAmount, itemHandler.capacity(), totalStacks).withStyle(ChatFormatting.GRAY));
			if (locked) {
				tooltip.add(Component.translatable("item.spectrum.bottomless_bundle.tooltip.locked").withStyle(ChatFormatting.GRAY));
			} else {
				tooltip.add(Component.translatable("item.spectrum.bottomless_bundle.tooltip.enter_inventory", variant.getItem().getDescription().getString()).withStyle(ChatFormatting.GRAY));
			}
		}
		if (itemHandler.deletesOverflow()) {
			tooltip.add(Component.translatable("item.spectrum.bottomless_bundle.tooltip.voiding"));
		}
	}
	
	@Override
	public void onDestroyed(ItemEntity entity) {
		BottomlessItemHandlerComponent component = entity.getItem().get(SpectrumDataComponentTypes.BOTTOMLESS_STACK);
		if(component != null) {
			ItemUtils.onContainerDestroyed(entity, component.handler());
		}
	}
	
	/**
	 * When the bundle is clicked onto another stack
	 */
	@Override
	public boolean overrideStackedOnOther(ItemStack bundleStack, @NotNull Slot slot, @NotNull ClickAction clickType, @NotNull Player player) {
		if (bundleStack.getCount() != 1 || clickType != ClickAction.SECONDARY) {
			return false;
		}
		
		ItemStack slotStack = slot.getItem();
		BottomlessItemHandlerComponent component = bundleStack.getOrDefault(SpectrumDataComponentTypes.BOTTOMLESS_STACK, BottomlessItemHandlerComponent.DEFAULT);
		BottomlessItemHandler itemHandler = component.handler();
		
		if (slotStack.isEmpty()) {
			ItemStack removed = itemHandler.extractSingleStack();
			if (!removed.isEmpty()) {
				this.playRemoveOneSound(player);
				var remainder = slot.safeInsert(removed);
				itemHandler.insertItem(0, remainder, false);
			}
		} else if (slotStack.canFitInsideContainerItems()) {
			ItemStack remainder = itemHandler.insertItem(0, slotStack, false);
			if (slotStack.getCount() != remainder.getCount()) {
				this.playInsertSound(player);
			}
		}
		
		return true;
	}
	
	/**
	 * When a stack is right-clicked onto the bundle
	 */
	@Override
	public boolean overrideOtherStackedOnMe(ItemStack bundleStack, ItemStack otherStack, Slot slot, ClickAction clickType, Player player, SlotAccess cursorStackReference) {
		if (bundleStack.getCount() != 1 || clickType != ClickAction.SECONDARY || !slot.allowModification(player)) {
			return false;
		}
		
		BottomlessItemHandlerComponent component = bundleStack.getOrDefault(SpectrumDataComponentTypes.BOTTOMLESS_STACK, BottomlessItemHandlerComponent.DEFAULT);
		BottomlessItemHandler itemHandler = component.handler();
		
		if (otherStack.isEmpty()) {
			var removed = itemHandler.extractSingleStack();
			if (!removed.isEmpty()) {
				this.playRemoveOneSound(player);
				cursorStackReference.set(removed);
				bundleStack.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, new BottomlessItemHandlerComponent(itemHandler));
			}
		} else {
			ItemStack remainder = itemHandler.insertItem(0, otherStack, false);
			if (otherStack.getCount() != remainder.getCount()) {
				this.playInsertSound(player);
				bundleStack.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, new BottomlessItemHandlerComponent(itemHandler));
			}
		}
		
		return true;
	}
	
	@Override
	public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int slot, boolean selected) {
		// We unbundle, tick and then rebundle the stack, in case inventory tick would modify components, count or other properties
		// The slot isn't technically correct, since it's the slot of the bundle, not that of the bundled stack
		
		// TODO
		/*BottomlessStack.Builder builder = BottomlessStack.Builder.get(world, stack);
		ItemStack bundledVariant = builder.getVariant();
		ItemStack bundledStack = builder.getVariant().copyWithCount((int) Math.min(Integer.MAX_VALUE, builder.count));
		long count = bundledStack.getCount();
		bundledStack.inventoryTick(world, entity, slot, selected);
		if (!ItemStack.isSameItemSameComponents(bundledVariant, bundledStack) || bundledStack.getCount() != count) {
			builder.set(bundledStack, Math.min(builder.getMaxAllowed(bundledStack), builder.count + bundledStack.getCount() - count));
			builder.buildAndSet(stack);
		}*/
	}
	
	private void playRemoveOneSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}
	
	private void playInsertSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}
	
	private void playDropContentsSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}
	
	private void playZipSound(Entity entity, float basePitch) {
		entity.playSound(SpectrumSoundEvents.BOTTOMLESS_BUNDLE_ZIP, 0.8F, basePitch + entity.level().getRandom().nextFloat() * 0.4F);
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return stack.getCount() == 1;
	}
	
	@Override
	public int getEnchantmentValue(@NotNull ItemStack stack) {
		return 5;
	}
	
	public static class BottomlessBundlePlacementDispenserBehavior extends OptionalDispenseItemBehavior {
		
		@Override
		@SuppressWarnings("resource")
		protected ItemStack execute(BlockSource pointer, ItemStack stack) {
			this.setSuccess(false);
			if (stack.getItem() instanceof BottomlessBundleItem bottomlessBundleItem) {
				Direction direction = pointer.state().getValue(DispenserBlock.FACING);
				BlockPos blockPos = pointer.pos().relative(direction);
				Direction direction2 = pointer.level().isEmptyBlock(blockPos.below()) ? direction : Direction.UP;
				
				try {
					this.setSuccess(bottomlessBundleItem.place(new DirectionalPlaceContext(pointer.level(), blockPos, direction, stack, direction2)).consumesAction());
				} catch (Exception e) {
					SpectrumCommon.logError("Error trying to place bottomless bundle at " + blockPos + " : " + e);
				}
			}
			return stack;
		}
		
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class Renderer implements DynamicItemRenderer {
		public Renderer() {
		}
		
		@Override
		public void render(ItemRenderer renderer, ItemStack stack, ItemDisplayContext mode, boolean leftHanded, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, BakedModel model) {
			// TODO
			/*renderer.render(stack, mode, leftHanded, matrices, vertexConsumers, light, overlay, model);
			if (mode != ItemDisplayContext.GUI || getStoredAmount(stack) <= 0)
				return;
			ItemStack bundledStack = BottomlessBundleItem.getTemplateVariant(stack);
			Minecraft client = Minecraft.getInstance();
			BakedModel bundledModel = renderer.getModel(bundledStack, client.level, client.player, 0);
			
			matrices.pushPose();
			matrices.scale(0.5F, 0.5F, 0.5F);
			matrices.translate(0.5F, 0.5F, 0.5F);
			renderer.render(bundledStack, mode, leftHanded, matrices, vertexConsumers, light, overlay, bundledModel);
			matrices.popPose();*/
		}
	}
	
	/*
	public record BottomlessStack(ItemStack variant, long count, boolean locked) {
		
		public static BottomlessStack DEFAULT = new BottomlessStack(ItemStack.EMPTY, 0, false);
		
		public static Codec<BottomlessStack> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ItemStack.OPTIONAL_CODEC.fieldOf("variant").forGetter(BottomlessStack::variant),
				Codec.LONG.fieldOf("count").forGetter(BottomlessStack::count),
				Codec.BOOL.fieldOf("locked").forGetter(BottomlessStack::locked)
		).apply(instance, BottomlessStack::new));
		
		public static StreamCodec<RegistryFriendlyByteBuf, BottomlessStack> PACKET_CODEC = StreamCodec.composite(
				ItemStack.STREAM_CODEC, BottomlessStack::variant,
				ByteBufCodecs.VAR_LONG, BottomlessStack::count,
				ByteBufCodecs.BOOL, BottomlessStack::locked,
				BottomlessStack::new
		);
		
		public Iterable<ItemStack> iterateCopy() {
			return new Iterable<>() {
				
				@Override
				public @NotNull Iterator<ItemStack> iterator() {
					return new Iterator<>() {
						
						private final Builder builder = new Builder(BottomlessStack.this, Integer.MAX_VALUE, false, false);
						
						@Override
						public boolean hasNext() {
							return !builder.isEmpty();
						}
						
						@Override
						public ItemStack next() {
							return builder.removeFirstStack();
						}
						
					};
				}
				
			};
		}
		
		public static class Builder {
			
			private final boolean voiding, locked;
			private final long max;
			private long count;
			private ItemStack variant;
			
			public static @Nullable Builder get(Level world, ItemStack bottomlessBundle) {
				var prev = bottomlessBundle.getOrDefault(SpectrumDataComponentTypes.BOTTOMLESS_STACK, BottomlessStack.DEFAULT);
				var max = BottomlessBundleItem.getMaxStoredAmount(SpectrumEnchantmentHelper.getLevel(world.registryAccess(), Enchantments.POWER, bottomlessBundle));
				var voiding = EnchantmentHelper.hasTag(bottomlessBundle, SpectrumEnchantmentTags.DELETES_OVERFLOW_IN_INVENTORY);
				var locked = bottomlessBundle.has(DataComponents.LOCK);
				return new Builder(prev, max, voiding, locked);
			}
			
			public Builder(BottomlessStack prev, long max, boolean voiding, boolean locked) {
				this.variant = prev.variant();
				this.max = max;
				this.count = prev.count();
				this.voiding = voiding;
				this.locked = locked;
			}
			
			public int getMaxAllowed(ItemStack stack) {
				if (this.count > 0 && !ItemStack.isSameItemSameComponents(this.variant, stack)) {
					return 0;
				}
				
				long result;
				if (isEmpty()) {
					result = this.max;
				} else if (stack.isEmpty() || !stack.getItem().canFitInsideContainerItems(stack)) {
					result = 0;
				} else {
					result = voiding ? Long.MAX_VALUE : this.max - this.count;
				}
				return (int) Math.min(result, Integer.MAX_VALUE);
			}
			// returns the count that got added to the bundle
			public int add(ItemStack stack) {
				int toAdd = Math.min(stack.getCount(), this.getMaxAllowed(stack));
				if (toAdd == 0)
					return 0;
				
				if (this.count == 0)
					this.variant = stack.copyWithCount(1);
				
				this.count += Math.min(this.max - this.count, toAdd);
				return toAdd;
			}
			
			public void setStack(ItemStack stack) {
				this.variant = stack.copyWithCount(1);
			}
			
			public void set(ItemStack stack, long count) {
				if (stack.isEmpty() || count == 0) {
					this.variant = ItemStack.EMPTY;
					this.count = 0;
				} else {
					this.variant = stack.copyWithCount(1);
					this.count = count;
				}
			}
			// returns the count that got added to the bundle
			public long add(Slot slot, Player player) {
				var maxAllowed = this.getMaxAllowed(slot.getItem());
				return this.add(slot.safeTake(slot.getItem().getCount(), maxAllowed, player));
			}
			
			public ItemStack remove(int amount) {
				if (isEmpty())
					return ItemStack.EMPTY;
				
				var toRemove = Math.min((int) this.count, amount);
				var removed = this.variant.copyWithCount(toRemove);
				this.count -= toRemove;
				if (this.count == 0)
					this.variant = ItemStack.EMPTY;
				
				return removed;
			}
			
			public ItemStack removeFirstStack() {
				return remove(variant.getItem().getDefaultMaxStackSize());
			}
			
			public long getCount() {
				return count;
			}
			
			public ItemStack getVariant() {
				return variant;
			}
			
			public boolean isEmpty() {
				return count == 0 || variant.isEmpty();
			}
			
			public void buildAndSet(ItemStack bottomlessBundleStack) {
				if (this.isEmpty()) {
					bottomlessBundleStack.remove(SpectrumDataComponentTypes.BOTTOMLESS_STACK);
				} else {
					bottomlessBundleStack.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, new BottomlessStack(variant, count, locked));
				}
			}
		}
		
	}*/
	
	@Override
	public boolean supportsEnchantment(@NotNull ItemStack stack, @NotNull Holder<Enchantment> enchantment) {
		return super.supportsEnchantment(stack, enchantment) || enchantment.is(Enchantments.POWER);
	}
	
}