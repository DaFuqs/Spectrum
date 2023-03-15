package de.dafuqs.spectrum.blocks.shooting_star;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.nbt.*;
import net.minecraft.util.math.*;

public class ShootingStarBlockEntity extends BlockEntity {
	
	protected int remainingHits;
	protected boolean hardened;
	
	public ShootingStarBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.SHOOTING_STAR, pos, state);
		this.remainingHits = 1;
		this.hardened = false;
	}
	
	public void setData(int remainingHits, boolean hardened) {
		this.remainingHits = remainingHits;
		this.hardened = hardened;
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.remainingHits = nbt.getInt("remaining_hits");
		this.hardened = nbt.getBoolean("hardened");
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putInt("remaining_hits", this.remainingHits);
		nbt.putBoolean("hardened", this.hardened);
	}
	
}
