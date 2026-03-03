package de.dafuqs.spectrum.registries.client;

import de.dafuqs.spectrum.render.biome_rendering.EnvironmentalData;
import de.dafuqs.spectrum.render.biome_rendering.EnvironmentalDataOverride;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Predicate;

public class SpectrumEnvironmentalDataOverrides {

    public static void register() {
		EnvironmentalDataOverride.register(new EnvironmentalDataOverride(e -> e instanceof LivingEntity l && l.hasEffect(SpectrumStatusEffects.FRENZY) && (l.hasEffect(SpectrumStatusEffects.ETERNAL_SLUMBER) || l.hasEffect(SpectrumStatusEffects.FATAL_SLUMBER)),
				new EnvironmentalDataOverride.ColorData(0xdf2449, 0.55F),
				new EnvironmentalData(0.25F, 0.25F, 0.334F, 0.5F),
				5)
		);
		
		EnvironmentalDataOverride.register(new EnvironmentalDataOverride(hasMobEffect(SpectrumStatusEffects.FATAL_SLUMBER),
				new EnvironmentalDataOverride.ColorData(0x8136c2, 0.65F),
				new EnvironmentalData(0.1F, 0.6F, 0.05F, 0.1F),
				10)
		);
		
		EnvironmentalDataOverride.register(new EnvironmentalDataOverride(hasMobEffect(SpectrumStatusEffects.ETERNAL_SLUMBER),
				new EnvironmentalDataOverride.ColorData(SpectrumStatusEffects.ETERNAL_SLUMBER_COLOR, 0.65F),
				new EnvironmentalData(0.1F, 0.5F, 0.05F, 0.2F),
				15)
		);
		
		EnvironmentalDataOverride.register(new EnvironmentalDataOverride(hasMobEffect(SpectrumStatusEffects.FRENZY),
				new EnvironmentalDataOverride.ColorData(0xb9080e, 0.3F),
				new EnvironmentalData(1.0F, 1.0F, 0.334F, 0.5F),
				20)
		);
		
		EnvironmentalDataOverride.register(new EnvironmentalDataOverride(hasMobEffect(SpectrumStatusEffects.SOMNOLENCE),
                        new EnvironmentalDataOverride.ColorData(SpectrumStatusEffects.ETERNAL_SLUMBER_COLOR, 0.575F),
                        new EnvironmentalData(0.5F, 0.5F, 0.125F, 0.25F),
                        25)
        );
    }

    public static Predicate<Entity> hasMobEffect(Holder<MobEffect> effect) {
        return entity -> entity instanceof LivingEntity l && l.hasEffect(effect);
    }
	
}