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
		BottomlessComponent bottomlessComponent = BottomlessComponent.get(stack, player.level().registryAccess(), true);
		BottomlessItemHandler storage = bottomlessComponent.handler();
		
		if (!storage.variant.isEmpty() && storage.count > 0) {
			int extractCount = Math.min(storage.variant.getItem().getDefaultMaxStackSize(), (int) Math.min(Integer.MAX_VALUE, storage.count));
			ItemStack removed = storage.variant.copyWithCount(extractCount);
			storage.count -= extractCount;
			player.drop(removed, true);
			
			stack.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, new BottomlessComponent(storage));
			return true;
		}
		return false;
	}
	
	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player user, @NotNull InteractionHand hand) {
		ItemStack handStack = user.getItemInHand(hand);
		if (user.isShiftKeyDown()) {
			
			BottomlessComponent bottomlessComponent = BottomlessComponent.get(handStack, user.level().registryAccess(), true);
			BottomlessItemHandler storage = bottomlessComponent.handler();
			
			if(bottomlessComponent.handler().locked()) {
				handStack.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, new BottomlessComponent(
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
				handStack.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, new BottomlessComponent(
						storage.capacity(),
						storage.deletesOverflow(),
						true,
						storage.variant(),
						storage.count())
				);
				if (level.isClientSide) {
					playZipSound(user, 1.0F);
				}
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
		BottomlessComponent itemHandler = bundleStack.get(SpectrumDataComponentTypes.BOTTOMLESS_STACK);
		if(itemHandler == null) {
			return Optional.empty();
		}
		return Optional.of(new BottomlessBundleTooltipData(itemHandler.handler()));
	}
	
	@Override
	public void appendHoverText(@NotNull ItemStack bundleStack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
		BottomlessComponent component = BottomlessComponent.get(bundleStack, context.registries(), false);
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
		BottomlessComponent component = entity.getItem().get(SpectrumDataComponentTypes.BOTTOMLESS_STACK);
		if(component != null) {
			ItemUtils.onContainerDestroyed(entity, component.handler());
		}
	}
	
	/**
	 * When the bundle is clicked onto another stack
	 * Only fired clientside!
	 */
	@Override
	public boolean overrideStackedOnOther(ItemStack bundleStack, @NotNull Slot slot, @NotNull ClickAction clickType, @NotNull Player player) {
		if (bundleStack.getCount() != 1 || clickType != ClickAction.SECONDARY) {
			return false;
		}
		
		ItemStack slotStack = slot.getItem();
		BottomlessItemCapability itemHandler = BottomlessItemCapability.get(bundleStack, player.level().registryAccess());
		if (slotStack.isEmpty()) {
			ItemStack removed = itemHandler.extractSingleStack();
			if (!removed.isEmpty()) {
				this.playRemoveOneSound(player);
				ItemStack remainder = slot.safeInsert(removed);
				itemHandler.insertItem(0, remainder, false);
			}
		} else if (slotStack.canFitInsideContainerItems()) {
			ItemStack remainder = itemHandler.insertItem(0, slotStack, false);
			slot.set(remainder);
			if (slotStack.getCount() != remainder.getCount()) {
				this.playInsertSound(player);
			}
		}
		
		return true;
	}
	
	/**
	 * When a stack is right-clicked onto the bundle
	 * Only fired clientside!
	 */
	@Override
	public boolean overrideOtherStackedOnMe(ItemStack bundleStack, ItemStack otherStack, Slot slot, ClickAction clickType, Player player, SlotAccess cursorStackReference) {
		if (bundleStack.getCount() != 1 || clickType != ClickAction.SECONDARY || !slot.allowModification(player)) {
			return false;
		}
		
		BottomlessItemCapability itemHandler = BottomlessItemCapability.get(bundleStack, player.level().registryAccess());
		if (otherStack.isEmpty()) {
			var removed = itemHandler.extractSingleStack();
			if (!removed.isEmpty()) {
				this.playRemoveOneSound(player);
				cursorStackReference.set(removed);
			}
		} else {
			ItemStack remainder = itemHandler.insertItem(0, otherStack, false);
			if (otherStack.getCount() != remainder.getCount()) {
				cursorStackReference.set(remainder);
				this.playInsertSound(player);
			}
		}
		
		return true;
	}
	
	@Override
	public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int slot, boolean selected) {
		// We unbundle, tick and then rebundle the stack, in case inventory tick would modify components, count or other properties
		// The slot isn't technically correct, since it's the slot of the bundle, not that of the bundled stack
		
		BottomlessComponent component = BottomlessComponent.get(stack, world.registryAccess(), true);
		BottomlessItemHandler handler = component.handler();
		
		ItemStack bundledVariant = handler.variant();
		ItemStack bundledStack = bundledVariant.copyWithCount((int) Math.min(Integer.MAX_VALUE, handler.count()));
		bundledStack.inventoryTick(world, entity, slot, selected);
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
	
	@Override
	public boolean supportsEnchantment(@NotNull ItemStack stack, @NotNull Holder<Enchantment> enchantment) {
		return super.supportsEnchantment(stack, enchantment) || enchantment.is(Enchantments.POWER);
	}
	
}