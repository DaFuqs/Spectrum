package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.blocks.altar.AltarBlockEntity;
import de.dafuqs.spectrum.inventories.slots.ReadOnlySlot;
import de.dafuqs.spectrum.inventories.slots.StackFilterSlot;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class AltarScreenHandler extends AbstractRecipeScreenHandler<Inventory> {

    private final ScreenHandlerContext context;
    private final PlayerEntity player;

    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    protected final World world;
    private final RecipeBookCategory category;
    private final CraftingResultInventory craftingResultInventory;

    public AltarScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(SpectrumScreenHandlerTypes.ALTAR, ScreenHandlerContext.EMPTY, RecipeBookCategory.CRAFTING, syncId, playerInventory);
    }

    protected AltarScreenHandler(ScreenHandlerType<?> type, ScreenHandlerContext context, RecipeBookCategory recipeBookCategory, int i, PlayerInventory playerInventory) {
        this(type, context, recipeBookCategory, i, playerInventory, new SimpleInventory(AltarBlockEntity.INVENTORY_SIZE), new ArrayPropertyDelegate(2));
    }

    public AltarScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        this(SpectrumScreenHandlerTypes.ALTAR, ScreenHandlerContext.EMPTY, RecipeBookCategory.CRAFTING, syncId, playerInventory, inventory, propertyDelegate);
    }

    protected AltarScreenHandler(ScreenHandlerType<?> type, ScreenHandlerContext context, RecipeBookCategory recipeBookCategory, int i, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(type, i);
        this.context = context;
        this.player = playerInventory.player;
        this.category = recipeBookCategory;
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        this.world = playerInventory.player.world;
        this.craftingResultInventory = new CraftingResultInventory();

        checkSize(inventory, AltarBlockEntity.INVENTORY_SIZE);
        checkDataCount(propertyDelegate, 2);
        inventory.onOpen(playerInventory.player);

        // crafting slots
        int m;
        int n;
        for(m = 0; m < 3; ++m) {
            for(n = 0; n < 3; ++n) {
                this.addSlot(new Slot(inventory, n + m * 3, 30 + n * 18, 19 + m * 18));
            }
        }

        // spectrum slots
        this.addSlot(new StackFilterSlot(inventory, 9, 44, 77, SpectrumItems.TOPAZ_POWDER));
        this.addSlot(new StackFilterSlot(inventory, 10, 44 + 18, 77, SpectrumItems.AMETHYST_POWDER));
        this.addSlot(new StackFilterSlot(inventory, 11, 44 + 2 * 18, 77, SpectrumItems.CITRINE_POWDER));
        this.addSlot(new StackFilterSlot(inventory, 12, 44 + 3 * 18, 77, SpectrumItems.ONYX_POWDER));
        this.addSlot(new StackFilterSlot(inventory, 13, 44 + 4 * 18, 77, SpectrumItems.MOONSTONE_POWDER));

        // crafting tablet slot
        this.addSlot(new StackFilterSlot(inventory, AltarBlockEntity.CRAFTING_TABLET_SLOT_ID, 93, 19, SpectrumItems.CRAFTING_TABLET));

        // preview slot
        this.addSlot(new ReadOnlySlot(craftingResultInventory, 0, 127, 37));

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

    @Override
    public void sendContentUpdates() {
        super.sendContentUpdates();

        // serverside only: if the recipe output has changed send update to the client
        if(!world.isClient) {
            ItemStack outputItemStack = ((AltarBlockEntity) inventory).getCraftingOutput();

            ItemStack displayedItemStack;
            if(outputItemStack.isEmpty()) {
                displayedItemStack = inventory.getStack(15);

                // if there is no block in the output slot
                // show the result of the current recipe
                if(displayedItemStack == ItemStack.EMPTY) {
                    Recipe recipe = (AltarBlockEntity.calculateRecipe(world, (AltarBlockEntity) inventory));
                    if(recipe != null) {
                        ((ServerPlayerEntity) player).networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, this.nextRevision(), 15, displayedItemStack));
                    }
                }
            } else {
                displayedItemStack = outputItemStack.copy();
                ItemStack remainingItemStack = inventory.getStack(15);

                if(!remainingItemStack.isEmpty()
                        && displayedItemStack.isItemEqual(remainingItemStack)
                        && displayedItemStack.getCount() + remainingItemStack.getCount() <= displayedItemStack.getMaxCount()) {

                    displayedItemStack.increment(remainingItemStack.getCount());
                }
            }

            if(!craftingResultInventory.getStack(0).equals(displayedItemStack)) {
                craftingResultInventory.setStack(0, displayedItemStack);
                ((ServerPlayerEntity) player).networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, this.nextRevision(), 15, displayedItemStack));
            }
        }
    }

    public void populateRecipeFinder(RecipeMatcher recipeMatcher) {
        if (this.inventory != null) {
            ((RecipeInputProvider)this.inventory).provideRecipeInputs(recipeMatcher);
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
        return 16;
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

    @Override
    public boolean canInsertIntoSlot(int index) {
        return index != 1;
    }

    // Shift-Clicking
    // 0-8: crafting slots
    // 9-13: spectrum slots
    // 14: crafting tablet
    // 15: preview slot
    // 16: hidden output slot
    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack clickedStackCopy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack clickedStack = slot.getStack();
            clickedStackCopy = clickedStack.copy();

            if(index < 15) {
                // altar => player inv
                if (!this.insertItem(clickedStack, 16, 51, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(clickedStackCopy.isOf(SpectrumItems.TOPAZ_POWDER)) {
                if(!this.insertItem(clickedStack, 9, 10, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(clickedStackCopy.isOf(SpectrumItems.AMETHYST_POWDER)) {
                if(!this.insertItem(clickedStack, 10, 11, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(clickedStackCopy.isOf(SpectrumItems.CITRINE_POWDER)) {
                if(!this.insertItem(clickedStack, 11, 12, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(clickedStackCopy.isOf(SpectrumItems.ONYX_POWDER)) {
                if(!this.insertItem(clickedStack, 12, 13, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(clickedStackCopy.isOf(SpectrumItems.MOONSTONE_POWDER)) {
                if(!this.insertItem(clickedStack, 13, 14, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(clickedStackCopy.isOf(SpectrumItems.CRAFTING_TABLET)) {
                if(!this.insertItem(clickedStack, AltarBlockEntity.CRAFTING_TABLET_SLOT_ID, AltarBlockEntity.CRAFTING_TABLET_SLOT_ID+1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // crafting grid
            if (!this.insertItem(clickedStack, 0, 8, false)) {
                return ItemStack.EMPTY;
            }

            if (clickedStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (clickedStack.getCount() == clickedStackCopy.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, clickedStack);
        }

        return clickedStackCopy;
    }


}
