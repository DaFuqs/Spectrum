package de.dafuqs.spectrum.items.misc;

import de.dafuqs.spectrum.InventoryHelper;
import de.dafuqs.spectrum.inventories.CraftingTabletScreenHandler;
import de.dafuqs.spectrum.items.tooltip.CraftingTabletTooltipData;
import de.dafuqs.spectrum.recipe.pedestal.PedestalCraftingRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class CraftingTabletItem extends Item {

    private static final Text TITLE = new TranslatableText("item.spectrum.crafting_tablet");

    public CraftingTabletItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            Recipe storedRecipe = getStoredRecipe(world, itemStack);
            if(storedRecipe == null || user.isSneaking()) {
                user.openHandledScreen(createScreenHandlerFactory(world, (ServerPlayerEntity) user, itemStack));
            } else {
                if(!(storedRecipe instanceof PedestalCraftingRecipe)) {
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

    public static void setStoredRecipe(ItemStack craftingTabletItemStack, Recipe recipe) {
        NbtCompound nbtCompound = craftingTabletItemStack.getOrCreateTag();
        nbtCompound.putString("recipe", recipe.getId().toString());
        craftingTabletItemStack.setTag(nbtCompound);
    }

    public static void clearStoredRecipe(ItemStack craftingTabletItemStack) {
        NbtCompound nbtCompound = craftingTabletItemStack.getOrCreateTag();
        if (nbtCompound.contains("recipe")) {
            nbtCompound.remove("recipe");
            craftingTabletItemStack.setTag(nbtCompound);
        }
    }

    public static Recipe getStoredRecipe(World world, ItemStack itemStack) {
        NbtCompound nbtCompound = itemStack.getTag();

        if(nbtCompound != null && nbtCompound.contains("recipe")) {
            String recipeString = nbtCompound.getString("recipe");
            Identifier recipeIdentifier = new Identifier(recipeString);

            Optional<? extends Recipe> optional = world.getRecipeManager().get(recipeIdentifier);
            if(optional.isPresent()) {
                return optional.get();
            }
        }
        return null;
    }

    private void tryCraftRecipe(ServerPlayerEntity serverPlayerEntity, Recipe recipe) {
        DefaultedList<Ingredient> ingredients = recipe.getIngredients();
        Inventory playerInventory = serverPlayerEntity.getInventory();
        if (InventoryHelper.removeFromInventory(ingredients, playerInventory, true)) {
            InventoryHelper.removeFromInventory(ingredients, playerInventory, false);

            ItemStack craftingResult = recipe.getOutput().copy();
            boolean insertInventorySuccess = serverPlayerEntity.getInventory().insertStack(craftingResult);
            ItemEntity itemEntity;
            if (insertInventorySuccess && craftingResult.isEmpty()) {
                craftingResult.setCount(1);
                itemEntity = serverPlayerEntity.dropItem(craftingResult, false);
                if (itemEntity != null) {
                    itemEntity.setDespawnImmediately();
                }

                serverPlayerEntity.world.playSound(null, serverPlayerEntity.getX(), serverPlayerEntity.getY(), serverPlayerEntity.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((serverPlayerEntity.getRandom().nextFloat() - serverPlayerEntity.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                serverPlayerEntity.currentScreenHandler.sendContentUpdates();
            } else {
                itemEntity = serverPlayerEntity.dropItem(craftingResult, false);
                if (itemEntity != null) {
                    itemEntity.resetPickupDelay();
                    itemEntity.setOwner(serverPlayerEntity.getUuid());
                }
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);
        Recipe recipe = getStoredRecipe(world, itemStack);
        if (recipe == null) {
            tooltip.add(new TranslatableText("item.spectrum.crafting_tablet.tooltip.no_recipe").formatted(Formatting.GRAY));
        } else {
            if(recipe instanceof PedestalCraftingRecipe) {
                tooltip.add(new TranslatableText("item.spectrum.crafting_tablet.tooltip.pedestal_recipe").formatted(Formatting.GRAY));
            } else {
                tooltip.add(new TranslatableText("item.spectrum.crafting_tablet.tooltip.crafting_recipe").formatted(Formatting.GRAY));
            }
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        Recipe storedRecipe = CraftingTabletItem.getStoredRecipe(MinecraftClient.getInstance().world, stack);
        if(storedRecipe != null) {
            return Optional.of(new CraftingTabletTooltipData(storedRecipe));
        } else {
            return Optional.empty();
        }
    }

}
