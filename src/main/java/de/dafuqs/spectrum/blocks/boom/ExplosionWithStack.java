package de.dafuqs.spectrum.blocks.boom;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.event.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class ExplosionWithStack extends Explosion {
	
	private final ItemStack stack;
	
	public ExplosionWithStack(Level level, @Nullable Entity source, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator damageCalculator, double x, double y, double z, float radius, boolean fire, BlockInteraction blockInteraction, ParticleOptions smallExplosionParticles, ParticleOptions largeExplosionParticles, Holder<SoundEvent> explosionSound, ItemStack stack) {
		super(level, source, damageSource, damageCalculator, x, y, z, radius, fire, blockInteraction, smallExplosionParticles, largeExplosionParticles, explosionSound);
		this.stack = stack;
	}
	
	public ItemStack getStack() {
		return this.stack;
	}
	
	private static class EnhancedExplosionDamageCalculator extends SimpleExplosionDamageCalculator {
		
		private final ServerLevel level;
		private final DamageSource damageSource;
		private final ItemStack stack;
		
		public EnhancedExplosionDamageCalculator(ServerLevel level, DamageSource damageSource, ItemStack stack, boolean explodesBlocks, boolean damagesEntities, Optional<Float> knockbackMultiplier, Optional<HolderSet<Block>> immuneBlocks) {
			super(explodesBlocks, damagesEntities, knockbackMultiplier, immuneBlocks);
			this.level = level;
			this.damageSource = damageSource;
			this.stack = stack;
		}
		
		@Override
		public float getEntityDamageAmount(Explosion explosion, Entity entity) {
			float damage = super.getEntityDamageAmount(explosion, entity);
			damage = EnchantmentHelper.modifyDamage(level, stack, entity, damageSource, damage);
			return damage;
		}
		
	}
	
	public static void explode(ServerLevel level, @Nullable Entity source, @Nullable ItemStack stack, Vec3 pos) {
		boolean primodialFireDamage = false; // stack.getEnchantmentLevel(level.registryAccess().registry(Registries.ENCHANTMENT).get().getHolderOrThrow(SpectrumEnchantments.RESONANCE)) > 0;
		boolean preservesBlocks = stack.getEnchantmentLevel(level.registryAccess().registry(Registries.ENCHANTMENT).get().getHolderOrThrow(Enchantments.BLAST_PROTECTION)) > 0;
		boolean preserveAllDrops = stack.getEnchantmentLevel(level.registryAccess().registry(Registries.ENCHANTMENT).get().getHolderOrThrow(Enchantments.EFFICIENCY)) > 0;
		
		@Nullable DamageSource damageSource = primodialFireDamage ? SpectrumDamageTypes.incandescence(level, source) : Explosion.getDefaultDamageSource(level, source);
		@Nullable ExplosionDamageCalculator damageCalculator = new EnhancedExplosionDamageCalculator(level, damageSource, stack, true, true, Optional.empty(), Optional.empty());
		
		float radius = 4.0F;
		
		BlockInteraction blockinteraction = preservesBlocks ? BlockInteraction.KEEP : preserveAllDrops ? BlockInteraction.DESTROY : BlockInteraction.DESTROY_WITH_DECAY;
		ExplosionWithStack explosion = new ExplosionWithStack(
				level,
				source,
				damageSource,
				damageCalculator,
				pos.x,
				pos.y,
				pos.z,
				radius,
				false,
				blockinteraction,
				ParticleTypes.EXPLOSION,
				ParticleTypes.EXPLOSION_EMITTER,
				SoundEvents.GENERIC_EXPLODE,
				stack
		);
		
		if (EventHooks.onExplosionStart(level, explosion)) return;
		
		explosion.explode();
		
		if (!explosion.interactsWithBlocks()) {
			explosion.clearToBlow();
		}
		
		for (ServerPlayer serverplayer : level.getPlayers(serverPlayer -> serverPlayer.distanceToSqr(pos.x, pos.y, pos.z) < 4096.0)) {
			serverplayer.connection.send(new ClientboundExplodePacket(
							pos.x, pos.y, pos.z,
							radius,
							explosion.getToBlow(),
							explosion.getHitPlayers().get(serverplayer),
							explosion.getBlockInteraction(),
							explosion.getSmallExplosionParticles(),
							explosion.getLargeExplosionParticles(),
							explosion.getExplosionSound()
					)
			);
		}
		
		explosion.finalizeExplosion(true);
	}
	
}
