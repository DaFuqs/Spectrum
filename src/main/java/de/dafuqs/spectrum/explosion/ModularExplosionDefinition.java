package de.dafuqs.spectrum.explosion;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.codec.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * A Set of ExplosionModifiers
 * - serializable as SpectrumDataComponentTypes.MODULAR_EXPLOSION
 * - implements the actual explosion logic
 *
 * TODO: rework the whole system to use enchantments instead
 */
public class ModularExplosionDefinition {
	
	protected ExplosionArchetype archetype = ExplosionArchetype.COSMETIC;
	protected List<ExplosionModifier> modifiers;
	
	public ModularExplosionDefinition() {
		this.modifiers = new ArrayList<>();
	}
	
	// Calls the explosion logic
	public static void explode(@NotNull ServerLevel world, BlockPos pos, @Nullable Player owner, ItemStack stack) {
		if (stack.getItem() instanceof ModularExplosionProvider provider) {
			ModularExplosionDefinition definition = new ModularExplosionDefinition();
			ModularExplosion.explode(world, pos, owner, provider.getBaseExplosionBlastRadius(), provider.getBaseExplosionDamage(), definition.archetype, definition.modifiers);
		}
	}
	
	public static void explode(@NotNull ServerLevel world, BlockPos pos, Direction direction, @Nullable Player owner, ItemStack stack) {
		if (stack.getItem() instanceof ModularExplosionProvider provider) {
			ModularExplosionDefinition definition = new ModularExplosionDefinition();
			BlockPos finalPos = pos.relative(direction, (int) provider.getBaseExplosionBlastRadius() - 2); // TODO: Add distance added via blast range modification
			ModularExplosion.explode(world, finalPos, owner, provider.getBaseExplosionBlastRadius(), provider.getBaseExplosionDamage(), definition.archetype, definition.modifiers);
		}
	}
	
	public ExplosionArchetype getArchetype() {
		return archetype;
	}
	
	public void setArchetype(ExplosionArchetype archetype) {
		this.archetype = archetype;
	}
	
	// Calls the explosion logic
	public void explode(@NotNull ServerLevel world, BlockPos pos, @Nullable Player owner, double baseBlastRadius, float baseDamage) {
		ModularExplosion.explode(world, pos, owner, baseBlastRadius, baseDamage, this.archetype, this.modifiers);
	}
	
}
