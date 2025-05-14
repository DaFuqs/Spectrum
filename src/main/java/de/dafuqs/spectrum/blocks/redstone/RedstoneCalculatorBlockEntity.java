package de.dafuqs.spectrum.blocks.redstone;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

public class RedstoneCalculatorBlockEntity extends BlockEntity {
	
	private int outputSignal;
	
	public RedstoneCalculatorBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.REDSTONE_CALCULATOR, pos, state);
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		nbt.putInt("output_signal", this.outputSignal);
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		this.outputSignal = nbt.getInt("output_signal");
	}
	
	public int getOutputSignal() {
		return this.outputSignal;
	}
	
	public void setOutputSignal(int outputSignal) {
		this.outputSignal = outputSignal;
	}
	
}
