package de.dafuqs.pigment.blocks.compressor;

import de.dafuqs.pigment.PigmentBlockEntityType;
import de.dafuqs.pigment.inventories.AutoCraftingInventory;
import de.dafuqs.pigment.inventories.InventoryHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompressorBlockEntity extends LootableContainerBlockEntity {

    private DefaultedList<ItemStack> inventory;

    ItemStack lastCraftingItemStack;// cache
    CraftingRecipe lastCraftingRecipe; // cache
    boolean hasToCraft;

    public CompressorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(PigmentBlockEntityType.COMPRESSOR_BLOCK_ENTITY_TYPE, blockPos, blockState);
        this.inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
        this.lastCraftingItemStack = ItemStack.EMPTY;
        this.lastCraftingRecipe = null;
        this.hasToCraft = false;
    }

    protected Text getContainerName() {
        return new TranslatableText("container.compressor");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this);
    }

    public static void tick(World world, BlockPos pos, BlockState state, CompressorBlockEntity compressorBlockEntity) {
        if(compressorBlockEntity.hasToCraft) {
            boolean couldCraft = compressorBlockEntity.tryCraftOnce();
            if(!couldCraft) {
                compressorBlockEntity.hasToCraft = false;
            }
        }
    }

    @Override
    public int size() {
        return 27;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        super.setStack(slot, stack);
        this.hasToCraft = true;
    }

    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return this.inventory;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }

    private boolean tryCraftOnce() {
        Optional<CraftingRecipe> optionalCraftingRecipe = Optional.empty();
        List<ItemStack> craftingStacks = null;
        DefaultedList<ItemStack> inventory = this.getInvStackList();

        if(lastCraftingRecipe != null) {
            // try craft
            Pair<Integer, List<ItemStack>> stackPair = InventoryHelper.getStackCountInInventory(lastCraftingItemStack, inventory);
            if(stackPair.getLeft() >= 9) {
                craftingStacks = stackPair.getRight();
                optionalCraftingRecipe = Optional.ofNullable(lastCraftingRecipe);
            } else {
                lastCraftingRecipe = null;
                lastCraftingItemStack = ItemStack.EMPTY;
            }
        }

        if(!optionalCraftingRecipe.isPresent()) {
            for(ItemStack itemStack : inventory) {
                if(itemStack.isEmpty()) {
                    continue;
                }
                Pair<Integer, List<ItemStack>> stackPair = InventoryHelper.getStackCountInInventory(itemStack, inventory);
                if(stackPair.getLeft() >= 9) {
                    craftingStacks = stackPair.getRight();
                }

                if(craftingStacks != null) {
                    AutoCraftingInventory autoCraftingInventory = new AutoCraftingInventory();
                    autoCraftingInventory.setCompacting(AutoCraftingInventory.AutoCraftingMode.ThreeXTree, craftingStacks.get(0).copy());
                    optionalCraftingRecipe = world.getServer().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, autoCraftingInventory, world);
                    if(!optionalCraftingRecipe.isPresent() || optionalCraftingRecipe.get().getOutput().isEmpty()) {
                        optionalCraftingRecipe = Optional.empty();
                    } else {
                        break;
                    }
                }
            }
        }

        if(optionalCraftingRecipe.isPresent()) {
            ItemStack craftingInput = craftingStacks.get(0).copy();
            craftingInput.setCount(9);
            ItemStack craftingOutput = optionalCraftingRecipe.get().getOutput().copy();

            if(tryCraftInInventory(inventory, craftingInput, craftingOutput)) {
                this.lastCraftingRecipe = optionalCraftingRecipe.get();
                this.lastCraftingItemStack = craftingInput;
                return true;
            }
        }
        return false;
    }

    public boolean tryCraftInInventory(DefaultedList<ItemStack> inventory, ItemStack removalItemStack, ItemStack additionItemStack) {
        InventoryHelper.removeFromInventory(removalItemStack, inventory);

        boolean spaceInInventory;

        List<ItemStack> additionItemStacks = new ArrayList<>();
        additionItemStacks.add(additionItemStack);

        // room for output?
        ItemStack remainderStack;
        Item recipeRemainderItem = removalItemStack.getItem().getRecipeRemainder();
        if(recipeRemainderItem != null) {
            remainderStack = recipeRemainderItem.getDefaultStack();
            remainderStack.setCount(9);
            additionItemStacks.add(remainderStack);
        }

        spaceInInventory = InventoryHelper.addToInventory(additionItemStacks, inventory, true);

        if(spaceInInventory) {
            // craft
            InventoryHelper.addToInventory(additionItemStacks, inventory, false);
            this.setInvStackList(inventory);

            // cache
            return true;
        }
        return false;
    }

    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        if (!this.serializeLootTable(tag)) {
            Inventories.toTag(tag, this.inventory);
        }

        return tag;
    }

    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.deserializeLootTable(tag)) {
            Inventories.fromTag(tag, this.inventory);
        }

    }

}
