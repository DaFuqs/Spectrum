package de.dafuqs.spectrum.blocks.shooting_star;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class ShootingStarBlockEntity extends BlockEntity {
	
	protected int remainingHits;
	
	public ShootingStarBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.SHOOTING_STAR, pos, state);
		this.remainingHits = 1;
	}
	
	public int getRemainingHits() {
		return remainingHits;
	}
	
	public void setRemainingHits(int remainingHits) {
		this.remainingHits = remainingHits;
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.remainingHits = nbt.getInt("remaining_hits");
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putInt("remaining_hits", this.remainingHits);
	}
	
	@Override
	public void setStackNbt(ItemStack stack) {
		super.setStackNbt(stack);
	}
}
