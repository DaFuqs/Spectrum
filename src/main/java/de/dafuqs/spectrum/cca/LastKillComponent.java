package de.dafuqs.spectrum.cca;

import de.dafuqs.spectrum.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.*;
import org.jetbrains.annotations.*;
import org.ladysnake.cca.api.v3.component.*;

public class LastKillComponent implements Component {
	
	public static final ComponentKey<LastKillComponent> LAST_KILL_COMPONENT = ComponentRegistry.getOrCreate(SpectrumCommon.locate("last_kill"), LastKillComponent.class);
	
	private long lastKillTick = -1;
	
	// this is not optional
	// removing this empty constructor will make the world not load
	public LastKillComponent() {
	
	}
	
	public LastKillComponent(LivingEntity entity) {
	
	}
	
	@Override
	public void writeToNbt(@NotNull CompoundTag tag, HolderLookup.Provider wrapperLookup) {
		if (this.lastKillTick >= 0) {
			tag.putLong("last_kill_tick", this.lastKillTick);
		}
	}
	
	@Override
	public void readFromNbt(CompoundTag tag, HolderLookup.Provider wrapperLookup) {
		if (tag.contains("last_kill_tick", Tag.TAG_LONG)) {
			this.lastKillTick = tag.getLong("last_kill_tick");
		}
	}
	
	public static void rememberKillTick(LivingEntity livingEntity, long tick) {
		LastKillComponent component = LAST_KILL_COMPONENT.get(livingEntity);
		component.lastKillTick = tick;
	}
	
	public static long getLastKillTick(LivingEntity livingEntity) {
		LastKillComponent component = LAST_KILL_COMPONENT.get(livingEntity);
		return component.lastKillTick;
	}
	
}
