package de.dafuqs.spectrum.blocks.bottomless_bundle;

import com.mojang.blaze3d.vertex.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.tooltip.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.minecraft.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.dispenser.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.codec.*;
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
import org.jetbrains.annotations.*;

import java.util.*;

public class BottomlessBundleItem extends BlockItem implements InventoryInsertionAcceptor {
	
	private static final long MAX_STORED_AMOUNT_BASE = 20000;
	
	public BottomlessBundleItem(Block block, Item.Properties settings) {
		super(block, settings.component(SpectrumDataComponentTypes.BOTTOMLESS_STACK, BottomlessStack.DEFAULT));
	}
	
	public static long getMaxStoredAmount(int powerLevel) {
		return MAX_STORED_AMOUNT_BASE * (int) Math.pow(10, Math.min(5, powerLevel)); // to not exceed int max
	}
	
	private static boolean dropOneBundledStack(ItemStack stack, Player player) {
		var builder = BottomlessStack.Builder.of(player.level(), stack);
		var dropped = builder.removeFirstStack();
		if (dropped.isEmpty())
			return false;
		
		player.drop(dropped, true);
		builder.buildAndSet(stack);
		return true;
	}
	
	public static boolean isLocked(ItemStack itemStack) {
		return itemStack.has(DataComponents.LOCK);
	}
	
	public static ItemVariant getTemplateVariant(ItemStack stack) {
		return stack.getOrDefault(SpectrumDataComponentTypes.BOTTOMLESS_STACK, BottomlessStack.DEFAULT).variant();
	}
	
	public static long getStoredAmount(ItemStack voidBundleStack) {
		return voidBundleStack
				.getOrDefault(SpectrumDataComponentTypes.BOTTOMLESS_STACK, BottomlessStack.DEFAULT)
				.count();
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack itemStack = user.getItemInHand(hand);
		if (user.isShiftKeyDown()) {
			ItemStack handStack = user.getItemInHand(hand);
			if (handStack.has(DataComponents.LOCK)) {
				handStack.remove(DataComponents.LOCK);
				if (world.isClientSide) {
					playZipSound(user, 0.8F);
				}
			} else {
				handStack.set(DataComponents.LOCK, LockCode.NO_LOCK);
				if (world.isClientSide) {
					playZipSound(user, 1.0F);
				}
			}
			return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
		} else if (dropOneBundledStack(itemStack, user)) {
			this.playDropContentsSound(user);
			user.awardStat(Stats.ITEM_USED.get(this));
			return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
		} else {
			return InteractionResultHolder.fail(itemStack);
		}
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (context.getPlayer().isShiftKeyDown())
			return super.useOn(context);
		return InteractionResult.PASS;
	}
	
	@Override
	public boolean canFitInsideContainerItems() {
		return false;
	}
	
	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack bundleStack) {
		ItemVariant variant = getTemplateVariant(bundleStack);
		var storedAmount = getStoredAmount(bundleStack);
		
		return Optional.of(new BottomlessBundleTooltipData(variant, storedAmount));
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		boolean locked = isLocked(stack);
		long storedAmount = getStoredAmount(stack);
		if (storedAmount == 0) {
			tooltip.add(Component.translatable("item.spectrum.bottomless_bundle.tooltip.empty").withStyle(ChatFormatting.GRAY));
			if (locked) {
				tooltip.add(
						Component.translatable("item.spectrum.bottomless_bundle.tooltip.locked").withStyle(ChatFormatting.GRAY));
			}
		} else {
			ItemVariant variant = getTemplateVariant(stack);
			var powerLevel = context.registries()
					.lookup(Registries.ENCHANTMENT)
					.flatMap(impl -> impl.get(Enchantments.POWER))
					.map(ench -> EnchantmentHelper.getItemEnchantmentLevel(ench, stack))
					.orElse(0);
			String totalStacks = Support.getShortenedNumberString(storedAmount / (float) variant.getItem().getDefaultMaxStackSize());
			tooltip.add(Component.translatable("item.spectrum.bottomless_bundle.tooltip.count", storedAmount,
					getMaxStoredAmount(powerLevel), totalStacks).withStyle(ChatFormatting.GRAY));
			if (locked) {
				tooltip.add(Component.translatable("item.spectrum.bottomless_bundle.tooltip.locked").withStyle(ChatFormatting.GRAY));
			} else {
				tooltip.add(Component.translatable("item.spectrum.bottomless_bundle.tooltip.enter_inventory",
						variant.getItem().getDescription().getString()).withStyle(ChatFormatting.GRAY));
			}
		}
		if (EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.DELETES_OVERFLOW)) {
			tooltip.add(Component.translatable("item.spectrum.bottomless_bundle.tooltip.voiding"));
		}
	}
	
	@Override
	public void onDestroyed(ItemEntity entity) {
		var bottomlessStack = entity.getItem().get(SpectrumDataComponentTypes.BOTTOMLESS_STACK);
		if (bottomlessStack != null) {
			entity.getItem().set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, BottomlessStack.DEFAULT);
			ItemUtils.onContainerDestroyed(entity, bottomlessStack.iterateCopy());
		}
	}
	
	/**
	 * When the bundle is clicked onto another stack
	 */
	@Override
	public boolean overrideStackedOnOther(ItemStack bundleStack, Slot slot, ClickAction clickType, Player player) {
		if (bundleStack.getCount() != 1 || clickType != ClickAction.SECONDARY) {
			return false;
		}
		
		ItemStack slotStack = slot.getItem();
		var builder = BottomlessStack.Builder.of(player.level(), bundleStack);
		if (slotStack.isEmpty()) {
			var removed = builder.removeFirstStack();
			if (!removed.isEmpty()) {
				this.playRemoveOneSound(player);
				var remainder = slot.safeInsert(removed);
				builder.add(remainder);
			}
		} else if (slotStack.getItem().canFitInsideContainerItems()) {
			var added = builder.add(slot, player);
			if (added > 0) {
				this.playInsertSound(player);
			}
		}
		
		builder.buildAndSet(bundleStack);
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
		
		var builder = BottomlessStack.Builder.of(player.level(), bundleStack);
		if (otherStack.isEmpty()) {
			var removed = builder.removeFirstStack();
			if (!removed.isEmpty()) {
				this.playRemoveOneSound(player);
				cursorStackReference.set(removed);
			}
		} else {
			var added = builder.add(otherStack);
			if (added > 0) {
				otherStack.shrink(added);
				this.playInsertSound(player);
			}
		}
		
		builder.buildAndSet(bundleStack);
		return true;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		// We unbundle, tick and then rebundle the stack, in case inventory tick would modify components, count or other properties
		// The slot isn't technically correct, since it's the slot of the bundle, not that of the bundled stack
		BottomlessStack.Builder builder = BottomlessStack.Builder.of(world, stack);
		ItemVariant bundledVariant = builder.getVariant();
		ItemStack bundledStack = builder.getVariant().toStack((int) Math.min(Integer.MAX_VALUE, builder.count));
		long count = bundledStack.getCount();
		bundledStack.inventoryTick(world, entity, slot, selected);
		if (!bundledVariant.matches(bundledStack) || bundledStack.getCount() != count) {
			builder.set(bundledStack, Math.min(builder.getMaxAllowed(bundledStack), builder.count + bundledStack.getCount() - count));
			builder.buildAndSet(stack);
		}
	}
	
	
	@Override
	public boolean acceptsItemStack(ItemStack inventoryInsertionAcceptorStack, ItemStack itemStackToAccept) {
		ItemVariant variant = getTemplateVariant(inventoryInsertionAcceptorStack);
		return !variant.isBlank() && variant.matches(itemStackToAccept);
	}
	
	@Override
	public int acceptItemStack(ItemStack inventoryInsertionAcceptorStack, ItemStack itemStackToAccept, Player playerEntity) {
		if (isLocked(inventoryInsertionAcceptorStack)) {
			return itemStackToAccept.getCount();
		}
		
		var builder = BottomlessStack.Builder.of(playerEntity.level(), inventoryInsertionAcceptorStack);
		var added = builder.add(itemStackToAccept);
		builder.buildAndSet(inventoryInsertionAcceptorStack);
		return itemStackToAccept.getCount() - added;
	}
	
	private void playRemoveOneSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F,
				0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}
	
	private void playInsertSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}
	
	private void playDropContentsSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F,
				0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}
	
	private void playZipSound(Entity entity, float basePitch) {
		entity.playSound(SpectrumSoundEvents.BOTTOMLESS_BUNDLE_ZIP, 0.8F,
				basePitch + entity.level().getRandom().nextFloat() * 0.4F);
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return stack.getCount() == 1;
	}
	
	@Override
	public int getEnchantmentValue() {
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
	
	@Environment(EnvType.CLIENT)
	public static class Renderer implements DynamicItemRenderer {
		public Renderer() {
		}
		
		@Override
		public void render(ItemRenderer renderer, ItemStack stack, ItemDisplayContext mode, boolean leftHanded,
						   PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay,
						   BakedModel model) {
			renderer.render(stack, mode, leftHanded, matrices, vertexConsumers, light, overlay, model);
			if (mode != ItemDisplayContext.GUI
					|| getStoredAmount(stack) <= 0)
				return;
			ItemStack bundledStack = BottomlessBundleItem.getTemplateVariant(stack).toStack();
			Minecraft client = Minecraft.getInstance();
			BakedModel bundledModel = renderer.getModel(bundledStack, client.level, client.player, 0);
			
			matrices.pushPose();
			matrices.scale(0.5F, 0.5F, 0.5F);
			matrices.translate(0.5F, 0.5F, 0.5F);
			renderer.render(bundledStack, mode, leftHanded, matrices, vertexConsumers, light, overlay, bundledModel);
			matrices.popPose();
		}
	}
	
	public record BottomlessStack(ItemVariant variant, long count, boolean locked) {
		
		public static BottomlessStack DEFAULT = new BottomlessStack(ItemStack.EMPTY, 0, false);
		
		public static Codec<BottomlessStack> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ItemVariant.CODEC.fieldOf("variant").forGetter(BottomlessStack::variant),
				Codec.LONG.fieldOf("count").forGetter(BottomlessStack::count),
				Codec.BOOL.fieldOf("locked").forGetter(BottomlessStack::locked)
		).apply(instance, BottomlessStack::new));
		
		public static StreamCodec<RegistryFriendlyByteBuf, BottomlessStack> PACKET_CODEC = StreamCodec.composite(
				ItemVariant.PACKET_CODEC, BottomlessStack::variant,
				ByteBufCodecs.VAR_LONG, BottomlessStack::count,
				ByteBufCodecs.BOOL, BottomlessStack::locked,
				BottomlessStack::new
		);
		
		public BottomlessStack(ItemStack stack, long count, boolean locked) {
			this(ItemVariant.of(stack), count, locked);
		}
		
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
			private ItemVariant variant;
			
			public static Builder of(Level world, ItemStack stack) {
				var prev = stack.getOrDefault(SpectrumDataComponentTypes.BOTTOMLESS_STACK, BottomlessStack.DEFAULT);
				var max = BottomlessBundleItem.getMaxStoredAmount(SpectrumEnchantmentHelper.getLevel(world.registryAccess(), Enchantments.POWER, stack));
				var voiding = EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.DELETES_OVERFLOW_IN_INVENTORY);
				var locked = stack.has(DataComponents.LOCK);
				return new Builder(prev, max, voiding, locked);
			}
			
			public Builder(BottomlessStack prev, long max, boolean voiding, boolean locked) {
				this.variant = prev.variant();
				this.max = max;
				this.count = prev.count();
				this.voiding = voiding;
				this.locked = locked;
			}
			
			public Builder clear() {
				this.variant = ItemVariant.blank();
				this.count = 0;
				return this;
			}
			
			public int getMaxAllowed(ItemStack stack) {
				return (int) Math.min(getMaxAllowed(ItemVariant.of(stack), stack.getCount()), Integer.MAX_VALUE);
			}
			
			public long getMaxAllowed(ItemVariant variant, long amount) {
				if (isEmpty()) {
					return this.max;
				}
				if (variant.isBlank() || amount <= 0 || !variant.getItem().canFitInsideContainerItems())
					return 0;
				return voiding ? Long.MAX_VALUE : this.max - this.count;
			}
			
			public int add(ItemStack stack) {
				int toAdd = Math.min(stack.getCount(), this.getMaxAllowed(stack));
				if (toAdd == 0)
					return 0;
				
				if (this.count == 0)
					this.variant = ItemVariant.of(stack);
				
				this.count += Math.min(this.max - this.count, toAdd);
				return toAdd;
			}
			
			public void setStack(ItemStack stack) {
				this.variant = ItemVariant.of(stack);
			}
			
			public void set(SingleVariantStorage<ItemVariant> storage) {
				this.variant = storage.variant;
				this.count = storage.amount;
			}
			
			public void set(ItemStack stack, long count) {
				if (stack.isEmpty() || count == 0) {
					this.variant = ItemVariant.blank();
					this.count = 0;
				} else {
					this.variant = ItemVariant.of(stack);
					this.count = count;
				}
			}
			
			public long add(Slot slot, Player player) {
				var maxAllowed = this.getMaxAllowed(slot.getItem());
				return this.add(slot.safeTake(slot.getItem().getCount(), maxAllowed, player));
			}
			
			public ItemStack remove(int amount) {
				if (isEmpty())
					return ItemStack.EMPTY;
				
				var toRemove = Math.min((int) this.count, amount);
				var removed = this.variant.toStack(toRemove);
				this.count -= toRemove;
				if (this.count == 0)
					this.variant = ItemVariant.blank();
				
				return removed;
			}
			
			public ItemStack removeFirstStack() {
				return remove(variant.getItem().getDefaultMaxStackSize());
			}
			
			public long getCount() {
				return count;
			}
			
			public ItemVariant getVariant() {
				return variant;
			}
			
			public boolean isEmpty() {
				return count == 0 || variant.isBlank();
			}
			
			public void buildAndSet(ItemStack bottomlessBundleStack) {
				if (this.isEmpty()) {
					bottomlessBundleStack.remove(SpectrumDataComponentTypes.BOTTOMLESS_STACK);
				} else {
					bottomlessBundleStack.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, new BottomlessStack(variant, count, locked));
				}
			}
		}
		
	}
	
	@Override
	public boolean canBeEnchantedWith(ItemStack stack, Holder<Enchantment> enchantment, EnchantingContext context) {
		return super.canBeEnchantedWith(stack, enchantment, context) || enchantment.is(Enchantments.POWER) || enchantment.is(SpectrumEnchantments.VOIDING);
	}
	
}
