package de.dafuqs.pigment.blocks.private_chest;

import de.dafuqs.pigment.interfaces.PlayerOwned;
import de.dafuqs.pigment.registries.PigmentBlockEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.api.EnvironmentInterfaces;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.*;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@EnvironmentInterfaces({@EnvironmentInterface(
        value = EnvType.CLIENT,
        itf = ChestAnimationProgress.class
)})
public class PrivateChestBlockEntity extends LootableContainerBlockEntity implements ChestAnimationProgress, SidedInventory, PlayerOwned {

    private UUID playerUUID;
    private DefaultedList<ItemStack> inventory;
    public final ChestStateManager stateManager;
    private final ChestLidAnimator lidAnimator;
    long lastNonOwnerOpenedTick;

    public PrivateChestBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(PigmentBlockEntityTypes.PRIVATE_CHEST, blockPos, blockState);
        this.inventory = DefaultedList.ofSize(size(), ItemStack.EMPTY);
        this.lastNonOwnerOpenedTick = -1;

        this.stateManager = new ChestStateManager() {
            protected void onChestOpened(World world, BlockPos pos, BlockState state) {
                PrivateChestBlockEntity.playSound(world, pos, state, SoundEvents.BLOCK_CHEST_OPEN);
            }

            protected void onChestClosed(World world, BlockPos pos, BlockState state) {
                PrivateChestBlockEntity.playSound(world, pos, state, SoundEvents.BLOCK_CHEST_CLOSE);
            }

            protected void onInteracted(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
                PrivateChestBlockEntity.this.onInvOpenOrClose(world, pos, state, oldViewerCount, newViewerCount);
            }

            protected boolean isPlayerViewing(PlayerEntity player) {
                if (!(player.currentScreenHandler instanceof GenericContainerScreenHandler)) {
                    return false;
                } else {
                    Inventory inventory = ((GenericContainerScreenHandler)player.currentScreenHandler).getInventory();
                    return inventory == PrivateChestBlockEntity.this;
                }
            }
        };
        this.lidAnimator = new ChestLidAnimator();
    }

    private static void playSound(World world, BlockPos pos, BlockState state, SoundEvent soundEvent) {
        world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, soundEvent, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
    }

    protected void onInvOpenOrClose(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
        Block block = state.getBlock();

        if (oldViewerCount != newViewerCount) {
            updateRedstone(pos, state);
        }

        world.addSyncedBlockEvent(pos, block, 1, newViewerCount);
    }

    public void updateRedstone(BlockPos pos, BlockState state) {
        world.updateNeighborsAlways(pos, state.getBlock());
        world.updateNeighborsAlways(pos.down(), state.getBlock());

        if(wasRecentlyTriedToOpenByNonOwner()) {
            world.getBlockTickScheduler().schedule(pos, state.getBlock(), 10);
        }
    }

    public static int getPlayersLookingInChestCount(BlockView world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        if (blockState.hasBlockEntity()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof PrivateChestBlockEntity) {
                return ((PrivateChestBlockEntity)blockEntity).stateManager.getViewerCount();
            }
        }
        return 0;
    }

    protected Text getContainerName() {
        return new TranslatableText("block.pigment.private_chest.title");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return GenericContainerScreenHandler.createGeneric9x6(syncId, playerInventory, this);
    }

    @Override
    public int size() {
        return 54;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        super.setStack(slot, stack);
    }

    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return this.inventory;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }

    public CompoundTag writeNbt(CompoundTag tag) {
        super.writeNbt(tag);

        if (!this.serializeLootTable(tag)) {
            Inventories.writeNbt(tag, this.inventory);
        }

        if(this.playerUUID !=  null) {
            tag.putUuid("UUID", this.playerUUID);
        }

        tag.putLong("LastNonOwnerOpenedTick", this.lastNonOwnerOpenedTick);

        return tag;
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, PrivateChestBlockEntity blockEntity) {
        blockEntity.lidAnimator.step();
    }

    public boolean onSyncedBlockEvent(int type, int data) {
        if (type == 1) {
            this.lidAnimator.setOpen(data > 0);
            return true;
        } else {
            return super.onSyncedBlockEvent(type, data);
        }
    }

    public void onOpen(PlayerEntity player) {
        if (!player.isSpectator()) {
            this.stateManager.openChest(player, this.getWorld(), this.getPos(), this.getCachedState());
        }

    }

    public void onClose(PlayerEntity player) {
        if (!player.isSpectator()) {
            this.stateManager.closeChest(player, this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    @Environment(EnvType.CLIENT)
    public float getAnimationProgress(float tickDelta) {
        return this.lidAnimator.getProgress(tickDelta);
    }

    public void onScheduledTick() {
        this.stateManager.updateViewerCount(this.getWorld(), this.getPos(), this.getCachedState());
        this.updateRedstone(this.getPos(), this.world.getBlockState(pos));
    }

    public void readNbt(CompoundTag tag) {
        super.readNbt(tag);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.deserializeLootTable(tag)) {
            Inventories.readNbt(tag, this.inventory);
        }

        if(tag.contains("UUID")) {
            this.playerUUID = tag.getUuid("UUID");
        } else {
            this.playerUUID = null;
        }

        if(tag.contains("LastNonOwnerOpenedTick")) {
            this.lastNonOwnerOpenedTick = tag.getLong("LastNonOwnerOpenedTick");
        } else {
            this.lastNonOwnerOpenedTick = -1;
        }
    }

    @Override
    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    @Override
    public UUID getPlayerUUID() {
        return this.playerUUID;
    }

    public void setPlayerData(UUID uuid, Text name) {
        this.setPlayerUUID(uuid);
        setCustomName(new TranslatableText("block.pigment.private_chest.title_with_owner", name.asString()));
    }

    @Override
    public boolean checkUnlocked(PlayerEntity player) {
        boolean isOwner = this.getPlayerUUID().equals(player.getUuid());

        if(!isOwner && this.world != null) {
            this.lastNonOwnerOpenedTick = this.world.getTime();
            updateRedstone(this.pos, this.world.getBlockState(pos));
        }

        return isOwner;
    }

    public boolean wasRecentlyTriedToOpenByNonOwner() {
        if(this.world != null) {
            return this.lastNonOwnerOpenedTick > 0 && this.lastNonOwnerOpenedTick + 20 > this.world.getTime();
        }
        return false;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return new int[0];
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return false;
    }

    public boolean canBreak(UUID uuid) {
        if(this.playerUUID == null) {
            return true;
        } else {
            return this.playerUUID.equals(uuid);
        }
    }
}
