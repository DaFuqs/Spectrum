package de.dafuqs.spectrum.explosion;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

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
	public void applyToBlocks(@NotNull World world, @NotNull Iterable<BlockPos> blocks) {
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
	public Optional<DamageSource> getDamageSource(@Nullable Entity owner) {
		return Optional.empty();
	}
	
	@ApiStatus.OverrideOnly
	public Optional<ParticleEffect> getParticleEffects() {
		return Optional.empty();
	}
	
	@ApiStatus.OverrideOnly
	public void addEnchantments(ItemStack stack) {
	}
	
	@ApiStatus.OverrideOnly
	public Optional<ExplosionShape> getShape() {
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
