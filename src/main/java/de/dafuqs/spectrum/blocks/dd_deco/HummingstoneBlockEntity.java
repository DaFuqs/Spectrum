package de.dafuqs.spectrum.blocks.dd_deco;

import de.dafuqs.spectrum.events.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import net.minecraft.world.event.listener.*;
import org.jetbrains.annotations.*;

public class HummingstoneBlockEntity extends BlockEntity implements HummingstoneEventQueue.Callback<HummingstoneEventQueue.EventEntry> {
    
    private static final int RANGE = 8;
    protected final HummingstoneEventQueue listener;
    
    public HummingstoneBlockEntity(BlockPos pos, BlockState state) {
        super(SpectrumBlockEntities.HUMMINGSTONE, pos, state);
        this.listener = new HummingstoneEventQueue(new BlockPositionSource(this.pos), RANGE, this);
    }
    
    public static void serverTick(@NotNull World world, BlockPos pos, BlockState state, @NotNull HummingstoneBlockEntity blockEntity) {
        blockEntity.listener.tick(world);
    }

    @Override
    public boolean canAcceptEvent(World world, GameEventListener listener, GameEvent.Message message, Vec3d sourcePos) {
        return !this.isRemoved() && (message.getEvent() == SpectrumGameEvents.HUMMINGSTONE_HYMN || message.getEvent() == SpectrumGameEvents.HUMMINGSTONE_HUMMING);
    }
    
    @Override
    public void triggerEvent(World world, GameEventListener listener, HummingstoneEventQueue.EventEntry entry) {
        GameEvent.Message message = entry.message;
        GameEvent.Emitter emitter = message.getEmitter();
        
        if (message.getEvent() == SpectrumGameEvents.HUMMINGSTONE_HUMMING) {
            HummingstoneBlock.startHumming(world, this.pos, world.getBlockState(this.pos), emitter.sourceEntity(), true);
        } else if (message.getEvent() == SpectrumGameEvents.HUMMINGSTONE_HYMN) {
            HummingstoneBlock.onHymn(world, this.pos, emitter.sourceEntity());
        }
    }

}
