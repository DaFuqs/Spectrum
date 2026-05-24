package de.dafuqs.spectrum.api.ink.capability;

import de.dafuqs.spectrum.api.ink.storage.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;

import javax.annotation.*;

public class BlockEntityInkCapability implements InkCapability {
	protected final InkStorageBlockEntity<?> inkStorageBlockEntity;
	protected final InkStorage storage;
	
	public static @Nullable BlockEntityInkCapability of(Level level, BlockPos pos) {
		return of(level.getBlockEntity(pos));
	}
	
	public static @Nullable BlockEntityInkCapability of(BlockEntity blockEntity) {
		if (blockEntity instanceof InkStorageBlockEntity<?> inkBlockEntity) {
			return new BlockEntityInkCapability(inkBlockEntity);
		}
		return null;
	}
	
	private BlockEntityInkCapability(InkStorageBlockEntity<?> inkBlockEntity) {
		this.inkStorageBlockEntity = inkBlockEntity;
		this.storage = inkBlockEntity.getInkStorage();
	}
	
	public InkStorage getStorage() {
		return storage;
	}
	
	public void markDirty() {
		inkStorageBlockEntity.setInkDirty();
	}
	
}
