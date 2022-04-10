package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.command.TimeCommand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class JadeVinesBlockEntity extends BlockEntity {
	
	private long lastGrowthTick = -1;
	
	public JadeVinesBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.JADE_VINES, pos, state);
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		if(nbt.contains("LastGrowthTick", NbtElement.LONG_TYPE)) {
			this.lastGrowthTick = nbt.getLong("LastGrowthTick");
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putLong("LastGrowthTick", this.lastGrowthTick);
	}
	
	public boolean canGrow(World world) {
		
	}
	
	/*public long getDay(long time) {
		return time / 24000L % 2147483647L;
	}*/
	
}
