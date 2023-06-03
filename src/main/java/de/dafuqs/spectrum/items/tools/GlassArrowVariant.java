package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.registry.*;

public class GlassArrowVariant {
	
	public static final GlassArrowVariant MALACHITE = register("malachite");
	public static final GlassArrowVariant TOPAZ = register("topaz");
	public static final GlassArrowVariant AMETHYST = register("amethyst");
	public static final GlassArrowVariant CITRINE = register("citrine");
	public static final GlassArrowVariant ONYX = register("onyx");
	public static final GlassArrowVariant MOONSTONE = register("moonstone");
	
	private static GlassArrowVariant register(String id) {
		return Registry.register(SpectrumRegistries.GLASS_ARROW_VARIANT, id, new GlassArrowVariant());
	}
	
	protected ArrowItem arrow;
	protected ParticleEffect particleEffect;
	
	public static void init() {
	}
	
	// bit of a load order hack because arrows are not registered at the time the variant registry is needed
	void setData(ArrowItem arrowItem, ParticleEffect particleEffect) {
		this.arrow = arrowItem;
		this.particleEffect = particleEffect;
	}
	
	public ArrowItem getArrow() {
		return arrow;
	}
	
	public ParticleEffect getParticleEffect() {
		return particleEffect;
	}
	
}
