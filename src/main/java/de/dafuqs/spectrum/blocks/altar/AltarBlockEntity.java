package de.dafuqs.spectrum.blocks.altar;

import de.dafuqs.spectrum.blocks.SpectrumBlockEntityType;
import de.dafuqs.spectrum.sounds.SpectrumSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ChestStateManager;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AltarBlockEntity extends LootableContainerBlockEntity {

    private DefaultedList<ItemStack> inventory;
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
        if (!this.serializeLootTable(tag)) {
            Inventories.toTag(tag, this.inventory);
        }
        return tag;
    }

    public void fromTag(CompoundTag compoundTag) {
        super.fromTag(compoundTag);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.deserializeLootTable(compoundTag)) {
            Inventories.fromTag(compoundTag, this.inventory);
        }
    }

    public int size() {
        return 14; // 0-8: crafting grid, 9-13: gems
    }

    protected DefaultedList<ItemStack> getInvStackList() {
        return this.inventory;
    }

    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }

    protected Text getContainerName() {
        return new TranslatableText("spectrum.container.altar");
    }

    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new AltarScreenHandler(syncId, playerInventory, this);
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

}
