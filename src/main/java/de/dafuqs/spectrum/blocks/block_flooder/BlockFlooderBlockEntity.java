package de.dafuqs.spectrum.blocks.block_flooder;

import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class BlockFlooderBlockEntity extends BlockEntity {
	
	private Entity owner;
	private UUID ownerUUID;
	
	private BlockPos sourcePos;
	private BlockState targetBlockState = Blocks.AIR.getDefaultState();
	
	public BlockFlooderBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.BLOCK_FLOODER, pos, state);
	}
	
	public UUID getOwnerUUID() {
		return ownerUUID;
	}
	
	public void setOwnerUUID(UUID ownerUUID) {
		this.ownerUUID = ownerUUID;
	}
	
	public Entity getOwner() {
		if (this.owner == null) {
			this.owner = PlayerOwned.getPlayerEntityIfOnline(world, this.ownerUUID);
		}
		return this.owner;
	}
	
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		if (nbt.contains("OwnerUUID")) {
			this.ownerUUID = nbt.getUuid("OwnerUUID");
		} else {
			this.ownerUUID = null;
		}
		if (nbt.contains("SourcePositionX") && nbt.contains("SourcePositionY") && nbt.contains("SourcePositionZ")) {
			this.sourcePos = new BlockPos(nbt.getInt("SourcePositionX"), nbt.getInt("SourcePositionY"), nbt.getInt("SourcePositionZ"));
		}
	}
	
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (this.ownerUUID != null) {
			nbt.putUuid("OwnerUUID", this.ownerUUID);
		}
		if (this.sourcePos != null) {
			nbt.putInt("SourcePositionX", this.sourcePos.getX());
			nbt.putInt("SourcePositionY", this.sourcePos.getY());
			nbt.putInt("SourcePositionZ", this.sourcePos.getZ());
		}
	}
	
	
	public BlockPos getSourcePos() {
		if (this.sourcePos == null) {
			this.sourcePos = this.pos;
		}
		return this.sourcePos;
	}
	
	public void setSourcePos(BlockPos sourcePos) {
		this.sourcePos = sourcePos;
	}
	
	public BlockState getTargetBlockState() {
		return targetBlockState;
	}
	
	public void setTargetBlockState(BlockState targetBlockState) {
		this.targetBlockState = targetBlockState;
	}
	
}
