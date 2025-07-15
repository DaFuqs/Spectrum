package de.dafuqs.spectrum.blocks.deeper_down;

import de.dafuqs.spectrum.events.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

public class HummingstoneBlockEntity extends BlockEntity implements HummingstoneEventQueue.Callback<HummingstoneEventQueue.EventEntry> {
	
	private static final int RANGE = 8;
	protected final HummingstoneEventQueue listener;
	
	public HummingstoneBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.HUMMINGSTONE, pos, state);
		this.listener = new HummingstoneEventQueue(new BlockPositionSource(this.worldPosition), RANGE, this);
	}
	
	@SuppressWarnings("unused")
	public static void serverTick(@NotNull Level world, BlockPos pos, BlockState state, @NotNull HummingstoneBlockEntity blockEntity) {
		blockEntity.listener.tick(world);
	}
	
	@Override
	public boolean canAcceptEvent(Level world, GameEventListener listener, GameEvent.ListenerInfo message, Vec3 sourcePos) {
		return !this.isRemoved() && (message.gameEvent() == SpectrumGameEvents.HUMMINGSTONE_HYMN || message.gameEvent() == SpectrumGameEvents.HUMMINGSTONE_HUMMING);
	}
	
	@Override
	public void triggerEvent(Level world, GameEventListener listener, HummingstoneEventQueue.EventEntry entry) {
		GameEvent.ListenerInfo message = entry.message();
		
		if (message.gameEvent() == SpectrumGameEvents.HUMMINGSTONE_HUMMING) {
			HummingstoneBlock.startHumming(world, this.worldPosition, world.getBlockState(this.worldPosition), message.context().sourceEntity(), true);
		} else if (message.gameEvent() == SpectrumGameEvents.HUMMINGSTONE_HYMN) {
			HummingstoneBlock.onHymn(world, this.worldPosition, message.context().sourceEntity());
		}
	}
	
}
