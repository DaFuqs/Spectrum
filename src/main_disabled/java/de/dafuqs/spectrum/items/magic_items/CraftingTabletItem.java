package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.items.tooltip.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.inventory.tooltip.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;

import java.util.*;

public class CraftingTabletItem extends Item implements LoomPatternProvider {
	
	private static final Component TITLE = Component.translatable("item.spectrum.crafting_tablet");
	
	public CraftingTabletItem(Properties settings) {
		super(settings);
	}
	
	public static void setStoredRecipe(ItemStack craftingTabletItemStack, RecipeHolder<?> recipe) {
		craftingTabletItemStack.set(SpectrumDataComponentTypes.STORED_RECIPE, recipe.id());
	}
	
	public static void clearStoredRecipe(ItemStack craftingTabletItemStack) {
		craftingTabletItemStack.remove(SpectrumDataComponentTypes.STORED_RECIPE);
	}
	
	public static RecipeHolder<?> getStoredRecipe(Level world, ItemStack itemStack) {
		if (world != null) {
			var id = itemStack.get(SpectrumDataComponentTypes.STORED_RECIPE);
			if (id != null)
				return world.getRecipeManager().byKey(id).orElse(null);
		}
		return null;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack itemStack = user.getItemInHand(hand);
		
		var storedRecipe = getStoredRecipe(world, itemStack);
		if (storedRecipe == null || user.isShiftKeyDown()) {
			if (world.isClientSide) {
				return InteractionResultHolder.success(user.getItemInHand(hand));
			} else {
				user.openMenu(createScreenHandlerFactory(world, (ServerPlayer) user, itemStack));
				return InteractionResultHolder.consume(user.getItemInHand(hand));
			}
		} else {
			if (storedRecipe.value() instanceof PedestalRecipe) {
				return InteractionResultHolder.pass(user.getItemInHand(hand));
			} else {
				if (tryCraftRecipe(user, storedRecipe.value(), world)) {
					if (world.isClientSide) {
						return InteractionResultHolder.success(user.getItemInHand(hand));
					} else {
						return InteractionResultHolder.consume(user.getItemInHand(hand));
					}
				}
				user.playSound(SpectrumSoundEvents.USE_FAIL, 1.0F, 1.0F);
				return InteractionResultHolder.fail(user.getItemInHand(hand));
			}
		}
	}
	
	public MenuProvider createScreenHandlerFactory(Level world, ServerPlayer serverPlayerEntity, ItemStack itemStack) {
		return new SimpleMenuProvider((syncId, inventory, player) -> new CraftingTabletScreenHandler(syncId, inventory, ContainerLevelAccess.create(world, serverPlayerEntity.blockPosition()), itemStack), TITLE);
	}
	
	public static boolean tryCraftRecipe(Player serverPlayerEntity, Recipe<?> recipe, Level world) {
		NonNullList<Ingredient> ingredients = recipe.getIngredients();
		
		Container playerInventory = serverPlayerEntity.getInventory();
		boolean hasInInventory = InventoryHelper.hasInInventory(ingredients, playerInventory);
		if (world.isClientSide) {
			return hasInInventory;
		}
		
		if (InventoryHelper.hasInInventory(ingredients, playerInventory)) {
			List<ItemStack> remainders = InventoryHelper.removeFromInventoryWithRemainders(ingredients, playerInventory);
			
			ItemStack craftingResult = recipe.getResultItem(serverPlayerEntity.level().registryAccess()).copy();
			serverPlayerEntity.getInventory().placeItemBackInInventory(craftingResult);
			
			for (ItemStack remainder : remainders) {
				serverPlayerEntity.getInventory().placeItemBackInInventory(remainder);
			}
			return true;
		}
		return false;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		var recipe = getStoredRecipe(Minecraft.getInstance().level, stack);
		if (recipe == null) {
			tooltip.add(Component.translatable("item.spectrum.crafting_tablet.tooltip.no_recipe").withStyle(ChatFormatting.GRAY));
		} else {
			if (recipe.value() instanceof PedestalRecipe) {
				tooltip.add(Component.translatable("item.spectrum.crafting_tablet.tooltip.pedestal_recipe").withStyle(ChatFormatting.GRAY));
			} else {
				tooltip.add(Component.translatable("item.spectrum.crafting_tablet.tooltip.crafting_recipe").withStyle(ChatFormatting.GRAY));
			}
			tooltip.add(Component.translatable("item.spectrum.crafting_tablet.tooltip.shift_to_view_gui").withStyle(ChatFormatting.GRAY));
		}
		
		addBannerPatternProviderTooltip(tooltip);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		Minecraft client = Minecraft.getInstance();
		var storedRecipe = CraftingTabletItem.getStoredRecipe(client.level, stack);
		if (storedRecipe != null) {
			return Optional.of(new CraftingTabletTooltipData(storedRecipe.value(), client.level));
		} else {
			return Optional.empty();
		}
	}
	
	@Override
	public ResourceKey<BannerPattern> getPattern() {
		return SpectrumBannerPatterns.CRAFTING_TABLET;
	}
	
}
