package de.dafuqs.spectrum.compat.starry_skies;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.compat.starry_skies.decorators.*;
import de.dafuqs.starryskies.registries.*;
import de.dafuqs.starryskies.worldgen.*;
import net.minecraft.core.*;

public class StarrySkiesCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	public static SphereDecorator<SphereDecoratorConfig.DefaultSphereDecoratorConfig> QUITOXIC_REEDS_POND_DECORATOR = registerDecorator("quitoxic_reeds_pond", new QuitoxicReedsPondDecorator(SphereDecoratorConfig.DefaultSphereDecoratorConfig.CODEC));
	
	@Override
	public void register() {
	
	}
	
	@Override
	public void registerClient() {
	
	}
	
	private static <C extends SphereDecoratorConfig, F extends SphereDecorator<C>> F registerDecorator(String name, F feature) {
		return Registry.register(StarryRegistries.SPHERE_DECORATOR, SpectrumCommon.locate(name), feature);
	}
	
}