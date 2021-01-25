package de.dafuqs.spectrum.blocks.altar;

import de.dafuqs.spectrum.blocks.SpectrumBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class AltarScreenHandler extends AbstractRecipeScreenHandler<AltarBlockInventory> {

    private final AltarBlockInventory input;
    private final CraftingResultInventory result;
    private final ScreenHandlerContext context;
    private final PlayerEntity player;

    //This constructor gets called on the client when the server wants it to open the screenHandler,
    //The client will call the other constructor with an empty Inventory and the screenHandler will automatically
    //sync this empty inventory with the inventory on the server.
    public AltarScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public AltarScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ScreenHandlerType.CRAFTING, syncId);
        this.input = new AltarBlockInventory(this, 3, 3);
        this.result = new CraftingResultInventory();
        this.context = context;
        this.player = playerInventory.player;
        //this.addSlot(new CraftingResultSlot(playerInventory.player, this.input, this.result, 0, 124, 35));

        int m;
        int l;
        for(m = 0; m < 3; ++m) {
            for(l = 0; l < 3; ++l) {
                this.addSlot(new Slot(this.input, l + m * 3, 30 + l * 18, 17 + m * 18));
            }
        }

        for(m = 0; m < 3; ++m) {
            for(l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }

        for(m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }

    }

    @Override
    public void populateRecipeFinder(RecipeFinder finder) {
        this.input.provideRecipeInputs(finder);
    }

    @Override
    public void clearCraftingSlots() {
        this.input.clear();
        this.result.clear();
    }

    @Override
    public boolean matches(Recipe<? super AltarBlockInventory> recipe) {
        return recipe.matches(this.input, this.player.world);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return 13; // TODO: Spawn item in world instead
    }

    @Override
    public int getCraftingWidth() {
        return this.input.getWidth();
    }

    @Override
    public int getCraftingHeight() {
        return this.input.getHeight();
    }

    @Override
    public int getCraftingSlotCount() {
        return 9 + 5; // crafting + gems
    }

    @Override
    public RecipeBookCategory getCategory() {
        return null;
    }

    @Override
    public boolean method_32339(int i) {
        return i != this.getCraftingResultSlotIndex();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, SpectrumBlocks.ALTAR);
    }


    /*
    //This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    //and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    public AltarScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(SpectrumScreenHandlerTypes.ALTAR_SCREEN_HANDLER, syncId); // Since we didn't create a ScreenHandlerType, we will place null here.

        checkSize(inventory, craftingRows * craftingColumns + gemColumns);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);

        int i = (craftingRows - 4) * 18;
        int n;
        int m;
        for(n = 0; n < craftingRows; ++n) {
            for(m = 0; m < craftingColumns; ++m) {
                this.addSlot(new Slot(inventory, m + n * craftingRows, 62 + m * 18, 13 + n * 18));
            }
        }

        for(m = 0; m < gemColumns; ++m) {
            int finalM = m;
            this.addSlot(new Slot(inventory, 9 + finalM, 44 + finalM * 18, 21 + n * 18) {
                public boolean canInsert(ItemStack stack) {
                    return isItemStackValidForSlot(finalM, stack);
                }
            });
        }

        for(n = 0; n < 3; ++n) {
            for(m = 0; m < 9; ++m) {
                this.addSlot(new Slot(playerInventory, m + n * 9 + 9, 8 + m * 18, 119 + n * 18 + i));
            }
        }

        for(n = 0; n < 9; ++n) {
            this.addSlot(new Slot(playerInventory, n, 8 + n * 18, 177 + i));
        }
    }

    private boolean isItemStackValidForSlot(int gemSlotId, ItemStack itemStack) {
        switch (gemSlotId) {
            case 0:
                return itemStack.isOf(Items.AMETHYST_SHARD);
            case 1:
                return itemStack.isOf(SpectrumItems.CITRINE_SHARD_ITEM);
            case 2:
                return itemStack.isOf(SpectrumItems.TOPAZ_SHARD_ITEM);
            case 3:
                return itemStack.isOf(SpectrumItems.MOONSTONE_SHARD_ITEM);
            case 4:
                return itemStack.isOf(SpectrumItems.ONYX_SHARD_ITEM);
        }
        return false;
    }

    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    // Shift + Player Inv Slot
    public ItemStack transferSlot(PlayerEntity player, int slotIndex) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (slotIndex < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    public void close(PlayerEntity player) {
        super.close(player);
        this.inventory.onClose(player);
    }
*/
}
