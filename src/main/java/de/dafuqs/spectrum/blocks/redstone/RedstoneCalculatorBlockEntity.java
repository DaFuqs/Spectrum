package de.dafuqs.spectrum.blocks.redstone;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.nbt.*;
import net.minecraft.util.math.*;

public class RedstoneCalculatorBlockEntity extends BlockEntity {
	
	private int outputSignal;
	
	public RedstoneCalculatorBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.REDSTONE_CALCULATOR, pos, state);
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putInt("output_signal", this.outputSignal);
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.outputSignal = nbt.getInt("output_signal");
	}
	
	public int getOutputSignal() {
		return this.outputSignal;
	}
	
	public void setOutputSignal(int outputSignal) {
		this.outputSignal = outputSignal;
	}
	
}
