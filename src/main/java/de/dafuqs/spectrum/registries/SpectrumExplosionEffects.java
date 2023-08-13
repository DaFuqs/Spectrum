package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.explosion.ExplosionEffectFamily;
import de.dafuqs.spectrum.explosion.ExplosionEffectModifier;
import de.dafuqs.spectrum.explosion.modifier.AmalgamModifier;
import de.dafuqs.spectrum.explosion.modifier.DamageChangingModifier;
import de.dafuqs.spectrum.explosion.modifier.ParticleAddingModifier;
import de.dafuqs.spectrum.explosion.modifier.PrimordialFireModifier;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.registry.Registry;

public class SpectrumExplosionEffects {

    public static final ExplosionEffectFamily AMALGAM_FAMILY = registerFamily("amalgam", 3);
    public static final ExplosionEffectFamily DAMAGE_CHANGING = registerFamily("damage_source", 1);
    public static final ExplosionEffectFamily COSMETIC = registerFamily("cosmetic", Integer.MAX_VALUE);

    //Boosters
    public static final AmalgamModifier AMALGAM_MODIFIER = registerEffect(new AmalgamModifier(SpectrumCommon.locate("amalgam"), AMALGAM_FAMILY, 0xffbf40, SpectrumBlocks.INCANDESCENT_AMALGAM.asItem()));

    //Damage source changers
    public static final DamageChangingModifier LIGHTNING = registerEffect(new DamageChangingModifier(SpectrumCommon.locate("lightning_damage"), DAMAGE_CHANGING, DamageSource.LIGHTNING_BOLT, SpectrumParticleTypes.WHITE_EXPLOSION, 0xaff3eb, SpectrumItems.STORM_STONE));
    public static final DamageChangingModifier MAGIC = registerEffect(new DamageChangingModifier(SpectrumCommon.locate("magic_damage"), DAMAGE_CHANGING, DamageSource.MAGIC, SpectrumParticleTypes.PURPLE_CRAFTING, 0xff59ff, SpectrumItems.NEOLITH));
    public static final DamageChangingModifier LOOTING = registerEffect(new DamageChangingModifier(SpectrumCommon.locate("looting_damage"), DAMAGE_CHANGING, SpectrumDamageSources.MIDNIGHT_SOLUTION, ParticleTypes.ENCHANT, 0x5433a5, SpectrumItems.MIDNIGHT_CHIP, SpectrumItems.MIDNIGHT_ABERRATION));
    public static final DamageChangingModifier PRIMORDIAL_FIRE = registerEffect(new PrimordialFireModifier(SpectrumCommon.locate("primordial_fire_damage"), DAMAGE_CHANGING, SpectrumDamageSources.PRIMORDIAL_FIRE, SpectrumParticleTypes.PRIMORDIAL_FLAME_SMALL, 0xff2664, SpectrumItems.REFINED_BLOODSTONE));

    //Cosmetic
    public static final ParticleAddingModifier STARRY_MODIFIER = registerEffect(new ParticleAddingModifier(SpectrumCommon.locate("starry"), COSMETIC, ParticleTypes.END_ROD, 0xc3c8d4, Items.CHORUS_FRUIT.asItem()));

    public static void register() {}

    private static <T extends ExplosionEffectModifier> T registerEffect(T modifier) {
        return Registry.register(SpectrumRegistries.EXPLOSION_EFFECT_MODIFIERS, modifier.id, modifier);
    }

    private static ExplosionEffectFamily registerFamily(String name, int maxMods) {
        var id = SpectrumCommon.locate(name);
        return Registry.register(SpectrumRegistries.EXPLOSION_EFFECT_FAMILIES, id, new ExplosionEffectFamily(id, maxMods));
    }
}
