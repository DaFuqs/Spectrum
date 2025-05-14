package de.dafuqs.spectrum.explosion;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.logging.*;

/**
 * Modifies an explosion in some way
 * This is still a mess. I like more how potion reagents manage that
 */
public abstract class ExplosionModifier {
	
	public final ExplosionModifierType type;
	public final int displayColor;
	private String translationKey;
	
	protected ExplosionModifier(ExplosionModifierType type, int displayColor) {
		this.type = type;
		this.displayColor = displayColor;
	}
	
	public ExplosionModifierType getType() {
		return type;
	}
	
	@ApiStatus.OverrideOnly
	public void applyToEntity(@NotNull Entity entity, double distance) {
	}
	
	@ApiStatus.OverrideOnly
	public void applyToBlocks(@NotNull ServerLevel world, @NotNull Iterable<BlockPos> blocks) {
	}
	
	@ApiStatus.OverrideOnly
	public float getBlastRadiusModifier() {
		return 1F;
	}
	
	@ApiStatus.OverrideOnly
	public float getDamageModifier() {
		return 1F;
	}
	
	@ApiStatus.OverrideOnly
	public float getKillZoneRadius() {
		return 0F;
	}
	
	@ApiStatus.OverrideOnly
	public float getKillZoneDamageModifier() {
		return 1F;
	}
	
	@ApiStatus.OverrideOnly
	public Optional<DamageSource> getDamageSource(@Nullable LivingEntity owner) {
		return Optional.empty();
	}
	
	@ApiStatus.OverrideOnly
	public Optional<ParticleOptions> getParticleEffects() {
		return Optional.empty();
	}
	
	@ApiStatus.OverrideOnly
	public void addEnchantments(ServerLevel world, ItemStack stack) {
	}
	
	@ApiStatus.OverrideOnly
	public Optional<ExplosionShape> getShape() {
		return Optional.empty();
	}
	
	public ResourceLocation getId() {
		return SpectrumRegistries.EXPLOSION_MODIFIER.getKey(this);
	}
	
	protected String loadTranslationKey() {
		if (this.translationKey == null) {
			this.translationKey = Util.makeDescriptionId("explosion_modifier", SpectrumRegistries.EXPLOSION_MODIFIER.getKey(this));
		}
		return this.translationKey;
	}
	
	public Component getName() {
		return Component.translatable(loadTranslationKey()).withStyle(style -> style.withColor(displayColor).withItalic(true));
	}
	
}
