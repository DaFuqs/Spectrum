package de.dafuqs.spectrum.blocks.altar;

import de.dafuqs.spectrum.blocks.SpectrumBlockEntityType;
import de.dafuqs.spectrum.items.SpectrumItems;
import de.dafuqs.spectrum.sounds.SpectrumSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestStateManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AltarBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, AltarBlockInventory {

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

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public int size() {
        return 14; // 0-8: crafting grid, 9-13: gems
    }

    public void tick() {
        this.stateManager.updateViewerCount(this.getWorld(), this.getPos(), this.getCachedState());
    }

    public void playSound(BlockState state, SoundEvent soundEvent) {
        double d = (double)this.pos.getX() + 0.5D;
        double e = (double)this.pos.getY() + 0.5D;
        double f = (double)this.pos.getZ() + 0.5D;
        this.world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
    }

    /**
     * Used for hoppers and other stuff
     * @param slot The slot to place item in
     * @param stack The item stack to place
     * @return If the item can be placed in the slot
     */
    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if(slot < 9) {
            return true;
        }

        if(slot == 9 && stack.getItem().equals(Items.AMETHYST_SHARD)
                || slot == 10 && stack.getItem().equals(SpectrumItems.CITRINE_SHARD_ITEM)
                || slot == 11 && stack.getItem().equals(SpectrumItems.TOPAZ_SHARD_ITEM)
                || slot == 12 && stack.getItem().equals(SpectrumItems.MOONSTONE_SHARD_ITEM)
                || slot == 13 && stack.getItem().equals(SpectrumItems.ONYX_SHARD_ITEM)) {
            return true;
        }

        return false;
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("Altar display name");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        //We provide *this* to the screenHandler as our class Implements Inventory
        //Only the Server has the Inventory at the start, this will be synced to the client in the ScreenHandler
        return new AltarScreenHandler(syncId, playerInventory, this);
    }
}
