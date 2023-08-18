package de.dafuqs.spectrum.explosion;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * A Set of ExplosionModifiers
 * - serializable / deserializable via nbt
 * - implements the actual explosion logic
 */
public class ExplosionModifierSet {
	
	public static final double BASE_EXPLOSION_RADIUS = 5;
	public static final double BASE_KILL_ZONE_RADIUS = 0.5;
	public static final float BASE_EXPLOSION_DAMAGE = 20;
	public static final float BASE_KILL_ZONE_DAMAGE = 2500;
	
	protected Set<ExplosionModifier> modifiers;
	
	public ExplosionModifierSet() {
		this.modifiers = new HashSet<>();
	}
	
	public ExplosionModifierSet(Set<ExplosionModifier> modifiers) {
		this.modifiers = modifiers;
	}
	
	public void addModifier(ExplosionModifier modifier) {
		this.modifiers.add(modifier);
	}
	
	public boolean canAcceptModifier(ExplosionModifier modifier, ExplosionArchetypeProvider provider) {
		if (this.modifiers.size() >= provider.getMaxModifierCount()) {
			return false;
		}
		
		int occurrences = 0;
		for (ExplosionModifier explosionModifier : modifiers) {
			if (explosionModifier.type == modifier.type) {
				occurrences++;
			} else if (!modifier.type.isCompatibleWith(explosionModifier.type)) {
				return false;
			}
		}
		return occurrences < modifier.type.maxModifiersForType();
	}
	
	protected static String NBT_KEY = "explosion_modifiers";
	protected static String NBT_MODIFIER_LIST_KEY = "l";
	
	// Serialization
	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();
		
		NbtList modifierList = new NbtList();
		for (ExplosionModifier modifier : this.modifiers) {
			modifierList.add(NbtString.of(modifier.getId().toString()));
		}
		nbt.put(NBT_MODIFIER_LIST_KEY, modifierList);
		
		return nbt;
	}
	
	public static ExplosionModifierSet fromNbt(NbtCompound nbt) {
		ExplosionModifierSet set = new ExplosionModifierSet();
		if (nbt == null) {
			return set;
		}
		
		NbtList modifierList = nbt.getList(NBT_MODIFIER_LIST_KEY, NbtElement.STRING_TYPE);
		for (NbtElement e : modifierList) {
			ExplosionModifier mod = SpectrumRegistries.EXPLOSION_MODIFIERS.get(Identifier.tryParse(e.asString()));
			set.modifiers.add(mod);
		}
		
		return set;
	}
	
	public static ExplosionModifierSet getFromStack(ItemStack stack) {
		return fromNbt(stack.getSubNbt(NBT_KEY));
	}
	
	public void attachToStack(ItemStack stack) {
		NbtCompound nbt = toNbt();
		stack.setSubNbt(NBT_KEY, nbt);
	}
	
	// Tooltips
	public void appendTooltip(List<Text> tooltip, ExplosionArchetypeProvider provider) {
		int modifierCount = this.modifiers.size();
		int maxModifierCount = provider.getMaxModifierCount();
		tooltip.add(Text.translatable("item.spectrum.tooltip.explosives.remaining_slots", modifierCount, maxModifierCount).formatted(Formatting.GRAY));
		
		if (modifierCount == 0) {
			tooltip.add(Text.translatable("item.spectrum.tooltip.explosives.modifiers").formatted(Formatting.GRAY));
		} else {
			for (ExplosionModifier explosionModifier : modifiers) {
				tooltip.add(explosionModifier.getName());
			}
		}
	}
	
	// Explosion logic
	public void explode(@NotNull ServerWorld world, BlockPos pos) {
		float killDamage = BASE_KILL_ZONE_DAMAGE;
		double killRadius = BASE_KILL_ZONE_RADIUS;
		double blastRadius = BASE_EXPLOSION_RADIUS;
		float blastDamage = BASE_EXPLOSION_DAMAGE;
		var damageSource = SpectrumDamageSources.INCANDESCENCE;
		
		for (ExplosionModifier explosionEffect : modifiers) {
			float radiusMod = explosionEffect.getBlastRadiusModifier();
			float damageMod = explosionEffect.getDamageModifier();
			
			blastRadius *= radiusMod;
			blastDamage *= damageMod;
			killRadius *= Math.max((radiusMod - 1) / 3 + 1, 1);
			killDamage *= (damageMod - 1) / 2 + 1;
			var effectDamage = explosionEffect.getDamageSource();
			if (effectDamage.isPresent())
				damageSource = effectDamage.get();
		}
		
		var center = Vec3d.ofCenter(pos);
		world.playSound(null, center.getX(), center.getY(), center.getZ(), SpectrumSoundEvents.BLOCK_THREAT_CONFLUX_EXPLODE, SoundCategory.BLOCKS, 1.0F, 0.8F + world.getRandom().nextFloat() * 0.3F);
		
		// I feel like this should be called for each Modifier separately, similar to applyToWorld(), applyToBlocks() & applyToEntities()
		// or even moved to use a modifier that uses applyToEntities(Map<affectedEntities, distance>);
		spawnParticles(world, center, modifiers, blastRadius);
		
		var blastBox = Box.of(center, blastRadius * 2, blastRadius * 2, blastRadius * 2);
		var affectedBlocks = BlockPos.stream(blastBox).toList();
		
		double finalBlastRadius = blastRadius;
		var affectedEntities = world.getOtherEntities(null, blastBox).stream().filter(entity -> entity.getPos().distanceTo(center) < finalBlastRadius).toList();
		
		for (Entity entity : affectedEntities) {
			double distance = Math.max(entity.getPos().distanceTo(center) - entity.getWidth() / 2, 0);
			if (distance <= killRadius) {
				entity.damage(damageSource, entity.getType().isIn(ConventionalEntityTypeTags.BOSSES) ? killDamage / 25F : killDamage);
			} else {
				var finalDamage = MathHelper.lerp(distance / blastRadius, blastDamage, blastDamage / 2);
				entity.damage(damageSource, (float) finalDamage);
			}
		}
		
		for (ExplosionModifier explosionEffect : modifiers) {
			explosionEffect.applyToWorld(world, center);
			explosionEffect.applyToBlocks(world, affectedBlocks);
			explosionEffect.applyToEntities(affectedEntities);
		}
	}
	
	// the client does not know about the block entities data
	// we have to send it from server => client
	private void spawnParticles(ServerWorld world, Vec3d center, Set<ExplosionModifier> effectModifiers, double blastRadius) {
		Random random = world.getRandom();
		ArrayList<net.minecraft.particle.ParticleEffect> types = new ArrayList<>(effectModifiers.stream().map(ExplosionModifier::getParticleEffects).filter(Optional::isPresent).map(Optional::get).toList());
		types.add(SpectrumParticleTypes.PRIMORDIAL_SMOKE);
		
		world.spawnParticles(SpectrumParticleTypes.PRIMORDIAL_FLAME, center.getX(), center.getY(), center.getZ(), 30, random.nextFloat() * 0.5 - 0.25, random.nextFloat() * 0.5 - 0.25, random.nextFloat() * 0.5 - 0.25, 0.0);
		
		double particleCount = blastRadius * blastRadius + random.nextInt((int) (blastRadius * 2)) * (types.size() / 2F + 0.5);
		for (int i = 0; i < particleCount; i++) {
			var r = random.nextDouble() * blastRadius;
			var orientation = Orientation.create(random.nextDouble() * Math.PI * 2, random.nextDouble() * Math.PI * 2);
			var particle = orientation.toVector(r).add(center);
			Collections.shuffle(types);
			
			world.spawnParticles(types.get(0), particle.getX(), particle.getY(), particle.getZ(), 1, 0, 0, 0, 0);
		}
	}
	
}
