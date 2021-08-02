package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.InventoryHelper;
import de.dafuqs.spectrum.enums.SpectrumTier;
import de.dafuqs.spectrum.inventories.AutoCompactingInventory;
import de.dafuqs.spectrum.inventories.GenericSpectrumContainerScreenHandler;
import de.dafuqs.spectrum.inventories.SpectrumScreenHandlerTypes;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

public class CompactingChestBlockEntity extends SpectrumChestBlockEntity {

    AutoCompactingInventory autoCompactingInventory = new AutoCompactingInventory();
    ItemStack lastCraftingItemStack;// cache
    CraftingRecipe lastCraftingRecipe; // cache
    boolean hasToCraft;

    public CompactingChestBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpectrumBlockEntityRegistry.COMPACTING_CHEST, blockPos, blockState);
        this.lastCraftingItemStack = ItemStack.EMPTY;
        this.lastCraftingRecipe = null;
        this.hasToCraft = false;
    }

    protected Text getContainerName() {
        return new TranslatableText("block.spectrum.compacting_chest");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return GenericSpectrumContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this, SpectrumTier.TIER1);
    }

    public static void tick(World world, BlockPos pos, BlockState state, CompactingChestBlockEntity compactingChestBlockEntity) {
        if(world.isClient) {
            compactingChestBlockEntity.lidAnimator.step();
        } else {
            if (compactingChestBlockEntity.hasToCraft) {
                boolean couldCraft = compactingChestBlockEntity.tryCraftOnce();
                if (!couldCraft) {
                    compactingChestBlockEntity.hasToCraft = false;
                }
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
                    autoCompactingInventory.setCompacting(AutoCompactingInventory.AutoCraftingMode.ThreeXTree, craftingStacks.get(0).copy());
                    optionalCraftingRecipe = world.getServer().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, autoCompactingInventory, world);
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

}
