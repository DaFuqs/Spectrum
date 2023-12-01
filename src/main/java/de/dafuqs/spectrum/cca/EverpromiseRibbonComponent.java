package de.dafuqs.spectrum.cca;

import de.dafuqs.spectrum.*;
import dev.onyxstudios.cca.api.v3.component.*;
import net.minecraft.entity.*;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.*;

public class EverpromiseRibbonComponent implements Component {
	
	public static final ComponentKey<EverpromiseRibbonComponent> EVERPROMISE_RIBBON_COMPONENT = ComponentRegistry.getOrCreate(SpectrumCommon.locate("everpromise_ribbon"), EverpromiseRibbonComponent.class);
	
	private boolean hasRibbon = false;
	
	// this is not optional
	// removing this empty constructor will make the world not load
	public EverpromiseRibbonComponent() {
	
	}
	
	public EverpromiseRibbonComponent(LivingEntity entity) {
	
	}
	
	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		if (this.hasRibbon) {
			tag.putBoolean("has_everpromise_ribbon", true);
		}
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		this.hasRibbon = tag.getBoolean("has_everpromise_ribbon");
	}
	
	public static void attachRibbon(LivingEntity livingEntity) {
		EverpromiseRibbonComponent component = EVERPROMISE_RIBBON_COMPONENT.get(livingEntity);
		component.hasRibbon = true;
	}
	
	public static boolean hasRibbon(LivingEntity livingEntity) {
		EverpromiseRibbonComponent component = EVERPROMISE_RIBBON_COMPONENT.get(livingEntity);
		return component.hasRibbon;
	}
	
}
