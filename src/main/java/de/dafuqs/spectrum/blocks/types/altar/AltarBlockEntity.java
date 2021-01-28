package de.dafuqs.spectrum.blocks.types.altar;

import de.dafuqs.spectrum.blocks.SpectrumBlockEntityType;
import de.dafuqs.spectrum.inventories.AltarCraftingScreenHandler;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.AltarCraftingRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class AltarBlockEntity extends LockableContainerBlockEntity implements RecipeUnlocker, RecipeInputProvider {

    private Text customName;
    protected DefaultedList<ItemStack> inventory;

    private int craftingTime;
    private int craftingTimeTotal;

    protected final PropertyDelegate propertyDelegate;
    private static final RecipeType<? extends AltarCraftingRecipe> recipeType = SpectrumRecipeTypes.ALTAR;

    public AltarBlockEntity(BlockPos blockPos, BlockState blockState) {

        super(SpectrumBlockEntityType.ALTAR_BLOCK_ENTITY_TYPE, blockPos, blockState);

        this.inventory = DefaultedList.ofSize(9+5, ItemStack.EMPTY);

        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch(index) {
                    case 0:
                        return AltarBlockEntity.this.craftingTime;
                    case 1:
                        return AltarBlockEntity.this.craftingTimeTotal;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch(index) {
                    case 0:
                        AltarBlockEntity.this.craftingTime = value;
                        break;
                    case 1:
                        AltarBlockEntity.this.craftingTimeTotal = value;
                }

            }

            public int size() {
                return 2;
            }
        };
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("block.container.altar");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new AltarCraftingScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        Iterator<ItemStack> var1 = this.inventory.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = var1.next();
        } while(itemStack.isEmpty());

        return false;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        ItemStack itemStack = this.inventory.get(slot);
        boolean bl = !stack.isEmpty() && stack.isItemEqualIgnoreDamage(itemStack) && ItemStack.areTagsEqual(stack, itemStack);
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }

        if (slot == 0 && !bl) {
            //this.craftingTimeTotal = getCraftTime(this.world, recipeType, this);
            this.craftingTime = 0;
            this.markDirty();
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return player.squaredDistanceTo((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void provideRecipeInputs(RecipeFinder finder) {
        for (ItemStack itemStack : this.inventory) {
            finder.addItem(itemStack);
        }
    }

    @Override
    public void setLastRecipe(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            Identifier identifier = recipe.getId();
        }
    }

    @Nullable
    @Override
    public Recipe<?> getLastRecipe() {
        return null;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }




    /*


    private boolean isCrafting() {
        return this.craftingTime > 0;
    }

    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        this.inventory = new AltarInventory();
        //Inventories.fromTag(tag, this.inventory);
        this.craftingTime = tag.getShort("CookTime");
        this.craftingTimeTotal = tag.getShort("CookTimeTotal");
        if (tag.contains("CustomName", 8)) {
            this.customName = Text.Serializer.fromJson(tag.getString("CustomName"));
        }
    }

    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putShort("CookTime", (short)this.craftingTime);
        tag.putShort("CookTimeTotal", (short)this.craftingTimeTotal);
        //Inventories.toTag(tag, this.inventory.getItems());
        if (this.customName != null) {
            tag.putString("CustomName", Text.Serializer.toJson(this.customName));
        }
        return tag;
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, AbstractFurnaceBlockEntity abstractFurnaceBlockEntity) {

    }

    @Override
    public Text getName() {
        return this.customName != null ? this.customName : this.getContainerName();
    }

    @Override
    public Text getDisplayName() {
        return this.getName();
    }

    @Override
    public void provideRecipeInputs(RecipeFinder finder) {
        this.inventory.provideRecipeInputs(finder);
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new AltarScreenHandler(syncId, playerInventory, player);
    }

    /*
    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        Iterator<ItemStack> var1 = this.inventory.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }
            itemStack = var1.next();
        } while(itemStack.isEmpty());

        return false;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        ItemStack itemStack = this.inventory.get(slot);
        boolean bl = !stack.isEmpty() && stack.isItemEqualIgnoreDamage(itemStack) && ItemStack.areTagsEqual(stack, itemStack);
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }

        if (!bl && this.world != null) {
            this.craftingTimeTotal = getCraftingTime(world, this);
            this.craftingTime = 0;
            this.markDirty();
        }
    }

    private static int getCraftingTime(World world, Inventory inventory) {
        return world.getRecipeManager().getFirstMatch(AltarBlockEntity.recipeType, inventory, world).map(AltarCraftingRecipe::getCraftingTime).orElse(defaultCraftingTime);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        assert this.world != null;
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return player.squaredDistanceTo((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void provideRecipeInputs(RecipeFinder finder) {
        for (ItemStack itemStack : this.inventory) {
            finder.addItem(itemStack);
        }
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }



    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return null;
    }


    /*private final DefaultedList<ItemStack> inventory;
    private final ChestStateManager stateManager;*/



    /*




    public AltarBlockEntity(BlockPos pos, BlockState state) {

        super(SpectrumBlockEntityType.ALTAR_BLOCK_ENTITY_TYPE, pos, state);

        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);

        this.stateManager = new ChestStateManager() {
            protected void onChestOpened(World world, BlockPos pos, BlockState state) {
                playSound(state, SpectrumSoundEvents.ALTAR_USE);
            }

            protected void onChestClosed(World world, BlockPos pos, BlockState state) {
                playSound(state, SpectrumSoundEvents.ALTAR_USE);
            }

            protected void onInteracted(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
            }

            protected boolean isPlayerViewing(PlayerEntity player) {
                if (player.currentScreenHandler instanceof GenericContainerScreenHandler) {
                    Inventory inventory = ((GenericContainerScreenHandler)player.currentScreenHandler).getInventory();
                    return inventory == AltarBlockEntity.this;
                } else {
                    return false;
                }
            }
        };
    }

    protected Text getContainerName() {
        return new TranslatableText("container.furnace");
    }

    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new FurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        Inventories.toTag(tag, this.inventory);
        return tag;
    }

    public void fromTag(CompoundTag compoundTag) {
        super.fromTag(compoundTag);
        Inventories.fromTag(compoundTag, this.inventory);
    }

    public int size() {
        return 14; // 0-8: crafting grid, 9-13: gems
    }

    public void playSound(BlockState state, SoundEvent soundEvent) {
        double d = (double)this.pos.getX() + 0.5D;
        double e = (double)this.pos.getY() + 0.5D;
        double f = (double)this.pos.getZ() + 0.5D;
        this.world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
    }

    /*@Override
    public boolean isValid(int slot, ItemStack stack) {
        if(slot < 9) {
            return true;
        }

        return slot == 9 && stack.getItem().equals(Items.AMETHYST_SHARD)
                || slot == 10 && stack.getItem().equals(SpectrumItems.CITRINE_SHARD_ITEM)
                || slot == 11 && stack.getItem().equals(SpectrumItems.TOPAZ_SHARD_ITEM)
                || slot == 12 && stack.getItem().equals(SpectrumItems.MOONSTONE_SHARD_ITEM)
                || slot == 13 && stack.getItem().equals(SpectrumItems.ONYX_SHARD_ITEM);
    }*/

    /*@Override
    public Text getDisplayName() {
        return new TranslatableText("Altar display name");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        //We provide *this* to the screenHandler as our class Implements Inventory
        //Only the Server has the Inventory at the start, this will be synced to the client in the ScreenHandler
        return new AltarScreenHandler(syncId, playerInventory, this);
    }*/

    /*@Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        //We provide *this* to the screenHandler as our class Implements Inventory
        //Only the Server has the Inventory at the start, this will be synced to the client in the ScreenHandler
        return new AltarScreenHandler(syncId, playerInventory, this);
    }*/

    /*public int getTier() {
        return 1;
    }*/

    /*public static void tick(World world, BlockPos blockPos, BlockState blockState, AltarBlockEntity altarBlockEntity) {

        // only craft stuff if redstone powered
        if(world.getBlockState(blockPos) == SpectrumBlocks.ALTAR.getDefaultState().with(AltarBlock.STATE, AltarBlock.AltarState.REDSTONE)) {
            altarBlockEntity.stateManager.updateViewerCount(altarBlockEntity.getWorld(), altarBlockEntity.getPos(), altarBlockEntity.getCachedState());

            RecipeType recipeType = SpectrumRecipeTypes.ALTAR_RECIPE_TYPE;
            Recipe<?> recipe = (Recipe) world.getRecipeManager().getFirstMatch(recipeType, altarBlockEntity, world).orElse(null);

            if(recipe != null) {
                altarBlockEntity.markDirty();
            }
        }
    }*/
}
