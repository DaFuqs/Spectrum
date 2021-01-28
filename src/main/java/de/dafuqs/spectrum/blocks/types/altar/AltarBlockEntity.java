package de.dafuqs.spectrum.blocks.types.altar;

import de.dafuqs.spectrum.blocks.SpectrumBlockEntityType;
import de.dafuqs.spectrum.sounds.SpectrumSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestStateManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AltarBlockEntity extends BlockEntity {

    private final DefaultedList<ItemStack> inventory;
    private final ChestStateManager stateManager;

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
