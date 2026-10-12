package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

public class PreservationSensorBlockEntity extends BlockEntity {
	
	protected Vec3i controllerOffset = Vec3i.ZERO;
	
	public PreservationSensorBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.PRESERVATION_SENSOR.get(), pos, state);
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		this.controllerOffset = null;
		if (nbt.contains("ControllerOffset", Tag.TAG_INT_ARRAY)) {
			int[] offset = nbt.getIntArray("ControllerOffset");
			this.controllerOffset = new Vec3i(offset[0], offset[1], offset[2]);
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		if (this.controllerOffset != null) {
			nbt.putIntArray("ControllerOffset", new int[]{this.controllerOffset.getX(), this.controllerOffset.getY(), this.controllerOffset.getZ()});
		}
	}
	
	@Override
	public boolean onlyOpCanSetNbt() {
		return true;
	}
	
	public void activateController() {
		if (level instanceof ServerLevel && controllerOffset != null) {
			BlockEntity blockEntity = level.getBlockEntity(Support.directionalOffset(this.worldPosition, this.controllerOffset, level.getBlockState(this.worldPosition).getValue(PreservationSensorBlock.FACING)));
			if (blockEntity instanceof PreservationControllerBlockEntity controller) {
				controller.openExit();
			}
		}
	}
	
}
