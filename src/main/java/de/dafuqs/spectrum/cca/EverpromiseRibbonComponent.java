package de.dafuqs.spectrum.cca;

import de.dafuqs.spectrum.*;
import net.minecraft.nbt.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import org.jetbrains.annotations.*;
import org.ladysnake.cca.api.v3.component.*;

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
	public void writeToNbt(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider wrapperLookup) {
		if (this.hasRibbon) {
			tag.putBoolean("has_everpromise_ribbon", true);
		}
	}
	
	@Override
	public void readFromNbt(CompoundTag tag, HolderLookup.@NotNull Provider wrapperLookup) {
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
