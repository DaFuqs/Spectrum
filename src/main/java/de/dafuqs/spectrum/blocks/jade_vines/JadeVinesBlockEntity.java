package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

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
	
	public boolean isLaterNight(@NotNull World world) {
		long time = world.getTime();
		if(TimeHelper.getTimeOfDay(time) == TimeHelper.TimeOfDay.NIGHT) { // timeOfDay % 24000 >= 13000 && timeOfDay % 24000 < 23000
			return TimeHelper.getDay(time + 1000) != TimeHelper.getDay(lastGrowthTick + 1000);
		}
		return false;
	}
	
	public void setLastGrownTime(long time) {
		this.lastGrowthTick = time;
		this.markDirty();
	}
	
}
