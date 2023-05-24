package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.items.tooltip.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.recipe.*;
import net.minecraft.screen.*;
import net.minecraft.server.network.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.registry.entry.*;
import net.minecraft.world.*;

import java.util.*;

public class CraftingTabletItem extends Item implements LoomPatternProvider {
	
	private static final Text TITLE = Text.translatable("item.spectrum.crafting_tablet");
	
	public CraftingTabletItem(Settings settings) {
		super(settings);
	}
	
	public static void setStoredRecipe(ItemStack craftingTabletItemStack, Recipe recipe) {
		NbtCompound nbtCompound = craftingTabletItemStack.getOrCreateNbt();
		nbtCompound.putString("recipe", recipe.getId().toString());
		craftingTabletItemStack.setNbt(nbtCompound);
	}
	
	public static void clearStoredRecipe(ItemStack craftingTabletItemStack) {
		NbtCompound nbtCompound = craftingTabletItemStack.getOrCreateNbt();
		if (nbtCompound.contains("recipe")) {
			nbtCompound.remove("recipe");
			craftingTabletItemStack.setNbt(nbtCompound);
		}
	}
	
	public static Recipe getStoredRecipe(World world, ItemStack itemStack) {
		if (world != null) {
			NbtCompound nbtCompound = itemStack.getNbt();
			
			if (nbtCompound != null && nbtCompound.contains("recipe")) {
				String recipeString = nbtCompound.getString("recipe");
				Identifier recipeIdentifier = new Identifier(recipeString);
				
				Optional<? extends Recipe> optional = world.getRecipeManager().get(recipeIdentifier);
				if (optional.isPresent()) {
					return optional.get();
				}
			}
		}
		return null;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		
		if (!world.isClient) {
			Recipe storedRecipe = getStoredRecipe(world, itemStack);
			if (storedRecipe == null || user.isSneaking()) {
				user.openHandledScreen(createScreenHandlerFactory(world, (ServerPlayerEntity) user, itemStack));
			} else {
				if (storedRecipe instanceof PedestalCraftingRecipe) {
					return TypedActionResult.pass(user.getStackInHand(hand));
				} else {
					tryCraftRecipe((ServerPlayerEntity) user, storedRecipe);
				}
			}
			return TypedActionResult.success(user.getStackInHand(hand));
		} else {
			return TypedActionResult.consume(user.getStackInHand(hand));
		}
	}
	
	public NamedScreenHandlerFactory createScreenHandlerFactory(World world, ServerPlayerEntity serverPlayerEntity, ItemStack itemStack) {
		return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new CraftingTabletScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, serverPlayerEntity.getBlockPos()), itemStack), TITLE);
	}
	
	private void tryCraftRecipe(ServerPlayerEntity serverPlayerEntity, Recipe recipe) {
		DefaultedList<Ingredient> ingredients = recipe.getIngredients();
		
		Inventory playerInventory = serverPlayerEntity.getInventory();
		if (InventoryHelper.hasInInventory(ingredients, playerInventory)) {
			List<ItemStack> remainders = InventoryHelper.removeFromInventoryWithRemainders(ingredients, playerInventory);
			
			ItemStack craftingResult = recipe.getOutput(serverPlayerEntity.getWorld().getRegistryManager()).copy();
			serverPlayerEntity.getInventory().offerOrDrop(craftingResult);
			
			for (ItemStack remainder : remainders) {
				serverPlayerEntity.getInventory().offerOrDrop(remainder);
			}
		}
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		Recipe recipe = getStoredRecipe(world, itemStack);
		if (recipe == null) {
			tooltip.add(Text.translatable("item.spectrum.crafting_tablet.tooltip.no_recipe").formatted(Formatting.GRAY));
		} else {
			if (recipe instanceof PedestalCraftingRecipe) {
				tooltip.add(Text.translatable("item.spectrum.crafting_tablet.tooltip.pedestal_recipe").formatted(Formatting.GRAY));
			} else {
				tooltip.add(Text.translatable("item.spectrum.crafting_tablet.tooltip.crafting_recipe").formatted(Formatting.GRAY));
			}
			tooltip.add(Text.translatable("item.spectrum.crafting_tablet.tooltip.shift_to_view_gui").formatted(Formatting.GRAY));
		}
		
		SpectrumBannerPatternItem.addBannerPatternProviderTooltip(tooltip);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public Optional<TooltipData> getTooltipData(ItemStack stack) {
		Recipe storedRecipe = CraftingTabletItem.getStoredRecipe(MinecraftClient.getInstance().world, stack);
		if (storedRecipe != null) {
			return Optional.of(new CraftingTabletTooltipData(storedRecipe));
		} else {
			return Optional.empty();
		}
	}
	
	@Override
	public RegistryEntry<BannerPattern> getPattern() {
		return SpectrumBannerPatterns.CRAFTING_TABLET;
	}
	
}
