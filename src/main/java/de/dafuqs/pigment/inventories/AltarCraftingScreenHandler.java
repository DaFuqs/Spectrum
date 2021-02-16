package de.dafuqs.pigment.inventories;

import de.dafuqs.pigment.inventories.slots.ReadOnlySlot;
import de.dafuqs.pigment.inventories.slots.StackFilterSlot;
import de.dafuqs.pigment.items.PigmentItems;
import de.dafuqs.pigment.recipe.altar.AltarCraftingRecipe;
import de.dafuqs.pigment.recipe.PigmentRecipeTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

public class AltarCraftingScreenHandler extends AbstractRecipeScreenHandler<Inventory> {

    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    protected final World world;
    private final RecipeBookCategory category;

    public AltarCraftingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(PigmentScreenHandlerTypes.ALTAR, PigmentRecipeTypes.ALTAR, RecipeBookCategory.CRAFTING, syncId, playerInventory);
    }

    protected AltarCraftingScreenHandler(ScreenHandlerType<?> type, RecipeType<? extends AltarCraftingRecipe> recipeType, RecipeBookCategory recipeBookCategory, int i, PlayerInventory playerInventory) {
        this(type, recipeType, recipeBookCategory, i, playerInventory, new SimpleInventory(15), new ArrayPropertyDelegate(2));
    }

    public AltarCraftingScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        this(PigmentScreenHandlerTypes.ALTAR, PigmentRecipeTypes.ALTAR, RecipeBookCategory.CRAFTING, syncId, playerInventory, inventory, propertyDelegate);
    }

    protected AltarCraftingScreenHandler(ScreenHandlerType<?> type, RecipeType<? extends AltarCraftingRecipe> recipeType, RecipeBookCategory recipeBookCategory, int i, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(type, i);
        this.category = recipeBookCategory;
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        this.world = playerInventory.player.world;

        checkSize(inventory, 9+5);
        checkDataCount(propertyDelegate, 2);

        // crafting slots
        int m;
        int n;
        for(m = 0; m < 3; ++m) {
            for(n = 0; n < 3; ++n) {
                this.addSlot(new Slot(inventory, n + m * 3, 30 + n * 18, 19 + m * 18));
            }
        }

        // gem slots
        this.addSlot(new StackFilterSlot(inventory, 9,  44 + 0 * 18, 77, Items.AMETHYST_SHARD));
        this.addSlot(new StackFilterSlot(inventory, 10, 44 + 1 * 18, 77, PigmentItems.CITRINE_SHARD_ITEM));
        this.addSlot(new StackFilterSlot(inventory, 11, 44 + 2 * 18, 77, PigmentItems.TOPAZ_SHARD_ITEM));
        this.addSlot(new StackFilterSlot(inventory, 12, 44 + 3 * 18, 77, PigmentItems.ONYX_SHARD_ITEM));
        this.addSlot(new StackFilterSlot(inventory, 13, 44 + 4 * 18, 77, PigmentItems.MOONSTONE_SHARD_ITEM));

        // preview slot
        this.addSlot(new ReadOnlySlot(inventory, 14, 127, 37));

        // player inventory
        int l;
        for(l = 0; l < 3; ++l) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + l * 9 + 9, 8 + k * 18, 112 + l * 18));
            }
        }

        // player hotbar
        for(l = 0; l < 9; ++l) {
            this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 170));
        }

        this.addProperties(propertyDelegate);
    }

    public void populateRecipeFinder(RecipeFinder finder) {
        if (this.inventory instanceof RecipeInputProvider) {
            ((RecipeInputProvider)this.inventory).provideRecipeInputs(finder);
        }
    }

    public void clearCraftingSlots() {
        for(int i = 0; i < 9; i++) {
            this.getSlot(i).setStack(ItemStack.EMPTY);
        }
    }

    public boolean matches(Recipe<? super Inventory> recipe) {
        return recipe.matches(this.inventory, this.world);
    }

    public int getCraftingResultSlotIndex() {
        return 15;
    }

    public int getCraftingWidth() {
        return 3;
    }

    public int getCraftingHeight() {
        return 3;
    }

    public int getCraftingSlotCount() {
        return 9;
    }

    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Environment(EnvType.CLIENT)
    public int getCraftingProgress() {
        int craftingTime = this.propertyDelegate.get(0); // craftingTime
        int craftingTimeTotal = this.propertyDelegate.get(1); // craftingTimeTotal
        return craftingTimeTotal != 0 && craftingTime != 0 ? craftingTime * 24 / craftingTimeTotal : 0;
    }

    public boolean isCrafting() {
        return this.propertyDelegate.get(0) > 0; // craftingTime
    }

    @Environment(EnvType.CLIENT)
    public RecipeBookCategory getCategory() {
        return this.category;
    }

    public boolean method_32339(int i) {
        return i != 1;
    }

}
