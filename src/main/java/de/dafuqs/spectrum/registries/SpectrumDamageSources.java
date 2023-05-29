package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.spells.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.item.Item;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.*;
import net.minecraft.text.Text;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import static de.dafuqs.spectrum.SpectrumCommon.locate;

// TODO - Test refactor
// General idea of the new damage type refactor:
// Damage Types handles the logic of how the damage behaves, determined via tags
// Damage Sources decide how death messages are handled
// Make a custom damage source if you want a custom message, otherwise just return a damage source with the type you want
public class SpectrumDamageSources {
	public static final TagKey<DamageType> ITEM_IMMUNITY = TagKey.of(RegistryKeys.DAMAGE_TYPE, locate("item_immunity"));
	public static final TagKey<Item> FIRE_IMMUNE_ITEMS = TagKey.of(RegistryKeys.ITEM, locate("fire_immune_items"));
	public static boolean recursiveDamage = false;
	// TODO - Move into common?
	public static final TagKey<DamageType> FAKE_PLAYER_DAMAGE = TagKey.of(RegistryKeys.DAMAGE_TYPE, locate("fake_player_damage"));
	public static final RegistryKey<DamageType> DECAY = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("decay"));
	public static final RegistryKey<DamageType> FLOATBLOCK = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("floatblock"));
	public static final RegistryKey<DamageType> SHOOTING_STAR = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("shooting_star"));
	public static final RegistryKey<DamageType> MIDNIGHT_SOLUTION = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("midnight_solution"));
	public static final RegistryKey<DamageType> DRAGONROT = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("dragonrot"));
	public static final RegistryKey<DamageType> DIKE_GATE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("dike_gate"));
	public static final RegistryKey<DamageType> INK_PROJECTILE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("dike_gate"));
	public static final RegistryKey<DamageType> DEADLY_POISON = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("deadly_poison"));
	public static final RegistryKey<DamageType> INCANDESCENCE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("incandescence"));
	public static final RegistryKey<DamageType> MOONSTONE_STRIKE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("incandescence"));
	public static final RegistryKey<DamageType> BRISTLE_SPROUTS = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("bristle_sprouts"));
	public static final RegistryKey<DamageType> SAWTOOTH = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("sawtooth"));
	public static final RegistryKey<DamageType> SET_HEALTH_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("set_health_damage"));
	public static final RegistryKey<DamageType> IRRADIANCE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, locate("irradiance"));

	public static DamageSource sawtooth(World world) {
		return new DamageSource(world.getDamageSources().registry.getEntry(SAWTOOTH).orElseThrow());
	}
	public static DamageSource dragonrot(World world) {
		return new DamageSource(world.getDamageSources().registry.getEntry(DRAGONROT).orElseThrow());
	}
	public static DamageSource inkProjectile(InkProjectileEntity projectile, @Nullable Entity attacker) {
		return new DamageSource(projectile.getDamageSources().registry.getEntry(INK_PROJECTILE).orElseThrow(), projectile, attacker);
	}
	
	public static DamageSource moonstoneBlast(World world, @Nullable MoonstoneStrike moonstoneStrike) {
		return moonstoneBlast(world, moonstoneStrike != null ? moonstoneStrike.getCausingEntity() : null);
	}
	
	public static DamageSource moonstoneBlast(World world, @Nullable LivingEntity attacker) {
		return new MoonstoneStrikeDamageSource(world, attacker);
	}

	public static DamageSource irradiance(World world, @Nullable LivingEntity attacker) {
		return new IrradianceDamageSource(world, attacker);
	}

	public static DamageSource setHealth(World world, LivingEntity attacker) {
		return new SetHealthDamageSource(world, attacker);
	}

	public static DamageSource floatblock(World world) {
		return new DamageSource(world.getDamageSources().registry.getEntry(FLOATBLOCK).orElseThrow());
	}

	public static DamageSource shootingStar(World world) {
		return new DamageSource(world.getDamageSources().registry.getEntry(SHOOTING_STAR).orElseThrow());
	}

	public static DamageSource incandescence(World world) {
		return new DamageSource(world.getDamageSources().registry.getEntry(INCANDESCENCE).orElseThrow());
	}

	public static DamageSource midnightSolution(World world) {
		return new DamageSource(world.getDamageSources().registry.getEntry(MIDNIGHT_SOLUTION).orElseThrow());
	}

	public static DamageSource decay(World world) {
		return new DamageSource(world.getDamageSources().registry.getEntry(DECAY).orElseThrow());
	}

	public static DamageSource deadlyPoison(World world) {
		return new DamageSource(world.getDamageSources().registry.getEntry(DEADLY_POISON).orElseThrow());
	}

	public static DamageSource dike(World world) {
		return new DamageSource(world.getDamageSources().registry.getEntry(DIKE_GATE).orElseThrow());
	}

	public static DamageSource bristeSprouts(World world) {
		return new DamageSource(world.getDamageSources().registry.getEntry(BRISTLE_SPROUTS).orElseThrow());
	}

	public static class SetHealthDamageSource extends DamageSource {

		public SetHealthDamageSource(World world, LivingEntity attacker) {
			super(world.getDamageSources().registry.getEntry(SET_HEALTH_DAMAGE).orElseThrow(), attacker);
		}
	}

	public static class MoonstoneStrikeDamageSource extends DamageSource {

		public MoonstoneStrikeDamageSource(World world, LivingEntity attacker) {
			super(world.getDamageSources().registry.getEntry(MOONSTONE_STRIKE).orElseThrow(), attacker);
		}

		public MoonstoneStrikeDamageSource(MoonstoneStrike moonstoneStrike) {
			super(moonstoneStrike.getDamageSource().getTypeRegistryEntry(), moonstoneStrike.getCausingEntity());
		}

		// TODO - Handle this message accordingly, since it might be more hardcoded than before
		@Override
		public Text getDeathMessage(LivingEntity killed) {
			return super.getDeathMessage(killed);
		}
	}

	public static class IrradianceDamageSource extends DamageSource {

		public IrradianceDamageSource(World world, @Nullable LivingEntity attacker) {
			super(world.getDamageSources().registry.getEntry(IRRADIANCE).orElseThrow(), attacker);
		}
	}

}
