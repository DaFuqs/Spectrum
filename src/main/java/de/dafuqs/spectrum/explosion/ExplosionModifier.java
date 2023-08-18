package de.dafuqs.spectrum.explosion;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.particle.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * Modifies an explosion in some way
 */
public abstract class ExplosionModifier {
	
	public final ExplosionModifierType type;
	public final int displayColor;
	private String translationKey;
	
	protected ExplosionModifier(ExplosionModifierType type, int displayColor) {
		this.type = type;
		this.displayColor = displayColor;
	}
	
	@ApiStatus.OverrideOnly
	public boolean isCompatibleWithArchetype(ExplosionArchetype archetype) {
		return type.acceptsArchetype(archetype);
	}
	
	@ApiStatus.OverrideOnly
	public void applyToEntities(@NotNull List<Entity> entities) {
	}
	
	@ApiStatus.OverrideOnly
	public void applyToBlocks(@NotNull World world, @NotNull List<BlockPos> blocks) {
	}
	
	@ApiStatus.OverrideOnly
	public void applyToWorld(@NotNull World world, @NotNull Vec3d center) {
	}
	
	@ApiStatus.OverrideOnly
	public float getBlastPowerModifier() {
		return 1F;
	}
	
	@ApiStatus.OverrideOnly
	public float getDropChanceModifier() {
		return 1F;
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
	public Optional<DamageSource> getDamageSource() {
		return Optional.empty();
	}
	
	@ApiStatus.OverrideOnly
	public Optional<ParticleEffect> getParticleEffects() {
		return Optional.empty();
	}
	
	public Identifier getId() {
		return SpectrumRegistries.EXPLOSION_MODIFIERS.getId(this);
	}
	
	protected String loadTranslationKey() {
		if (this.translationKey == null) {
			this.translationKey = Util.createTranslationKey("explosion_modifier", SpectrumRegistries.EXPLOSION_MODIFIERS.getId(this));
		}
		return this.translationKey;
	}
	
	public Text getName() {
		return Text.translatable(loadTranslationKey()).styled(style -> style.withColor(displayColor).withItalic(true));
	}
	
}
