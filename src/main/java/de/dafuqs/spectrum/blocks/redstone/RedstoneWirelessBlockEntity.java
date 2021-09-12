package de.dafuqs.spectrum.blocks.redstone;

import de.dafuqs.spectrum.events.SpectrumGameEvents;
import de.dafuqs.spectrum.events.WirelessRedstoneSignalListener;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class RedstoneWirelessBlockEntity extends BlockEntity implements WirelessRedstoneSignalListener.Callback {

    private static final int RANGE = 16;

    public boolean isSender;  // true = send false = receive
    public DyeColor channel;
    private int cachedSignal;
    private int outputSignal;
    private WirelessRedstoneSignalListener listener;

    public RedstoneWirelessBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpectrumBlockEntityRegistry.REDSTONE_WIRELESS, blockPos, blockState);
        this.isSender = true;
        this.channel = DyeColor.RED;
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.putBoolean("sender", this.isSender);
        tag.putInt("outputSignal", this.outputSignal);
        tag.putInt("channel", this.channel.ordinal());
        return tag;
    }

    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        this.isSender = tag.getBoolean("sender");
        this.outputSignal = tag.getInt("outputSignal");
        this.channel = DyeColor.byId(tag.getInt("channel"));
        if(!this.isSender) {
            this.listener = new WirelessRedstoneSignalListener(new BlockPositionSource(this.pos), RANGE, this);
        }
    }

    public static void serverTick(@NotNull World world, BlockPos pos, BlockState state, @NotNull RedstoneWirelessBlockEntity blockEntity) {
        if(blockEntity.listener != null) {
            blockEntity.listener.tick(world);
        } else {
            if(blockEntity.outputSignal != blockEntity.cachedSignal) {
                blockEntity.outputSignal = blockEntity.cachedSignal;
                blockEntity.world.emitGameEvent(SpectrumGameEvents.WIRELESS_REDSTONE_SIGNALS.get(blockEntity.outputSignal), blockEntity.getPos());
            }
        }
    }

    public @Nullable WirelessRedstoneSignalListener getEventListener() {
        return this.listener;
    }

    public int getRange() {
        return RANGE;
    }

    public void toggleSendingMode() {
        this.isSender = !this.isSender;
        if(this.isSender) {
            this.listener = null;
        } else {
            this.listener = new WirelessRedstoneSignalListener(new BlockPositionSource(this.pos), RANGE, this);
        }
    }

    // TODO when clicking with dye
    public void setChannel(DyeColor dyeColor) {
        this.channel = dyeColor;
    }

    @Override
    public boolean accepts(World world, GameEventListener listener, BlockPos pos, GameEvent event, Entity entity) {
        return !this.isSender && SpectrumGameEvents.WIRELESS_REDSTONE_SIGNALS.contains(event);
    }

    @Override
    public void accept(World world, GameEventListener listener, GameEvent event, int distance) {
        if(listener instanceof WirelessRedstoneSignalListener && !this.isSender) {
            int receivedSignal = SpectrumGameEvents.WIRELESS_REDSTONE_SIGNALS.indexOf(event);
            this.outputSignal = receivedSignal;
            // trigger a block update in all cases, even when powered does not change. That way connected blocks
            // can react on the strength change of the block, since we store the power in the block entity, not the block state
            if (receivedSignal == 0) {
                world.setBlockState(pos, world.getBlockState(pos).with(RedstoneWirelessBlock.POWERED, false), Block.NOTIFY_LISTENERS);
            } else {
                world.setBlockState(pos, world.getBlockState(pos).with(RedstoneWirelessBlock.POWERED, true), Block.NOTIFY_LISTENERS);
            }
        }
    }

    // since redstone is weird we have to cache a new signal or so
    // if we would start a game event right here it could be triggered
    // multiple times a tick (because neighboring redstone updates > 1/tick)
    // and therefore receivers receiving a wrong (because old) signal
    public void setSignalStrength(int newSignal) {
        if(this.isSender) {
            this.cachedSignal = newSignal;
        } else {
            this.outputSignal = newSignal;
        }
    }

    public boolean isSending() {
        return this.isSender;
    }

    public int getOutputSignal() {
        if(this.isSender) {
            return 0;
        }
        return this.outputSignal;
    }

    public int getCurrentSignalStrength() {
        return this.outputSignal;
    }

}
