package de.dafuqs.spectrum.blocks.redstone;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class RedstoneCalculatorBlockEntity extends BlockEntity {
	
	private int outputSignal;
	
	public RedstoneCalculatorBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.REDSTONE_CALCULATOR, pos, state);
	}
	
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putInt("output_signal", this.outputSignal);
	}
	
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
