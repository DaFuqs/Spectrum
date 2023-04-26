package de.dafuqs.spectrum.cca;

import de.dafuqs.spectrum.*;
import dev.onyxstudios.cca.api.v3.component.*;
import dev.onyxstudios.cca.api.v3.entity.*;
import net.minecraft.entity.*;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.*;

public class BondingRibbonComponent implements Component, EntityComponentInitializer {
	
	public static final ComponentKey<BondingRibbonComponent> BONDING_RIBBON_COMPONENT = ComponentRegistry.getOrCreate(SpectrumCommon.locate("bonding_ribbon"), BondingRibbonComponent.class);
	
	private boolean hasBondingRibbon = false;
	
	// this is not optional
	// removing this empty constructor will make the world not load
	public BondingRibbonComponent() {
	
	}
	
	public BondingRibbonComponent(LivingEntity entity) {
	
	}
	
	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(LivingEntity.class, BONDING_RIBBON_COMPONENT, BondingRibbonComponent::new);
	}
	
	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		if (this.hasBondingRibbon) {
			tag.putBoolean("has_bonding_ribbon", true);
		}
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		this.hasBondingRibbon = tag.getBoolean("has_bonding_ribbon");
	}
	
	public static void attachBondingRibbon(LivingEntity livingEntity) {
		BondingRibbonComponent component = BONDING_RIBBON_COMPONENT.get(livingEntity);
		component.hasBondingRibbon = true;
	}
	
	public static boolean hasBondingRibbon(LivingEntity livingEntity) {
		BondingRibbonComponent component = BONDING_RIBBON_COMPONENT.get(livingEntity);
		return component.hasBondingRibbon;
	}
	
}
