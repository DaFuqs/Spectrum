package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.explosion.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AmalgamModifier extends ExplosionModifier {
    
    public AmalgamModifier(Identifier id, ExplosionModifierType type, int displayColor) {
        super(id, type, displayColor);
    }
    
    @Override
    public void applyToEntities(@NotNull List<Entity> entity) {
    }
    
    @Override
    public void applyToBlocks(@NotNull World world, @NotNull List<BlockPos> blocks) {
    }
    
    @Override
    public void applyToWorld(@NotNull World world, @NotNull Vec3d center) {
    }
    
    /**
     * This number exists exclusively so that the total amount of damage dealt by a maxed out conflux is 6k flat<p>
     * 2500 * (1.3389 * 3) = 6000.45
     * <p> <p>
     * Autism gaming
     */
    @Override
    public float getDamageModifier() {
        return 1.6778F;
    }
    
    @Override
    public float getBlastRadiusModifier() {
        return 1.5F;
    }
    
}
