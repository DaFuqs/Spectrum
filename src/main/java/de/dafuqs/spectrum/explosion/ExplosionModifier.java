package de.dafuqs.spectrum.explosion;

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
    
    public final Identifier id;
    public final ExplosionModifierType type;
    public final String translationKey;
    public final int displayColor;
    
    protected ExplosionModifier(Identifier id, ExplosionModifierType type, int displayColor) {
        this.id = id;
        this.type = type;
        this.translationKey = Util.createTranslationKey("explosion_effect_modifier", id);
        this.displayColor = displayColor;
    }
    
    @ApiStatus.OverrideOnly
    public boolean isCompatibleWithArchetype(ExplosionArchetype archetype) {
        return type.acceptsArchetype(archetype);
    }
    
    @ApiStatus.OverrideOnly
    public abstract void applyToEntities(@NotNull List<Entity> entity);
    
    @ApiStatus.OverrideOnly
    public abstract void applyToBlocks(@NotNull World world, @NotNull List<BlockPos> blocks);
    
    @ApiStatus.OverrideOnly
    public abstract void applyToWorld(@NotNull World world, @NotNull Vec3d center);
    
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
    
    public Text getName() {
        return Text.translatable(translationKey).styled(style -> style.withColor(displayColor).withItalic(true));
    }
    
    @Override
    public int hashCode() {
        return 31 * id.hashCode() + type.hashCode();
    }
}
