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
import java.lang.*;

/**
 * A Set of ExplosionModifiers
 * - serializable as SpectrumDataComponentTypes.MODULAR_EXPLOSION
 * - implements the actual explosion logic
 */
public class ModularExplosionDefinition {
	
	public static final Codec<ModularExplosionDefinition> CODEC = RecordCodecBuilder.create(i -> i.group(
			StringRepresentable.fromEnum(ExplosionArchetype::values).fieldOf("archetype").forGetter(c -> c.archetype),
			SpectrumRegistries.EXPLOSION_MODIFIER.byNameCodec().listOf().optionalFieldOf("modifiers", List.of()).forGetter(c -> c.modifiers)
	).apply(i, ModularExplosionDefinition::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, ModularExplosionDefinition> PACKET_CODEC = PacketCodecHelper.tuple(
			ExplosionArchetype.PACKET_CODEC, c -> c.archetype,
			ByteBufCodecs.registry(SpectrumRegistryKeys.EXPLOSION_MODIFIER).apply(ByteBufCodecs.list()), c -> c.modifiers,
			ModularExplosionDefinition::new
	);
	
	protected ExplosionArchetype archetype = ExplosionArchetype.COSMETIC;
	protected List<ExplosionModifier> modifiers;
	
	public ModularExplosionDefinition() {
		this.modifiers = new ArrayList<>();
	}
	
	public ModularExplosionDefinition(ExplosionArchetype archetype, List<ExplosionModifier> modifiers) {
		this.archetype = archetype;
		this.modifiers = modifiers;
	}
	
	public static ModularExplosionDefinition getFromStack(ItemStack stack) {
		return stack.getOrDefault(SpectrumDataComponentTypes.MODULAR_EXPLOSION, new ModularExplosionDefinition());
	}

	public static ModularExplosionDefinition clone(ModularExplosionDefinition original) {
		return new ModularExplosionDefinition(original.archetype, new ArrayList<ExplosionModifier>(original.modifiers));
	}

	public static ModularExplosionDefinition cloneFromStack(ItemStack stack) {
		return clone(getFromStack(stack));
	}
	
	public static void removeFromStack(ItemStack stack) {
		stack.remove(SpectrumDataComponentTypes.MODULAR_EXPLOSION);
	}
	
	// Calls the explosion logic
	public static void explode(@NotNull ServerLevel world, BlockPos pos, @Nullable Player owner, ItemStack stack) {
		if (stack.getItem() instanceof ModularExplosionProvider provider) {
			ModularExplosionDefinition definition = getFromStack(stack);
			ModularExplosion.explode(world, pos, owner, provider.getBaseExplosionBlastRadius(), provider.getBaseExplosionDamage(), definition.archetype, definition.modifiers);
		}
	}
	
	public static void explode(@NotNull ServerLevel world, BlockPos pos, Direction direction, @Nullable Player owner, ItemStack stack) {
		if (stack.getItem() instanceof ModularExplosionProvider provider) {
			ModularExplosionDefinition definition = getFromStack(stack);
			BlockPos finalPos = pos.relative(direction, (int) provider.getBaseExplosionBlastRadius() - 2); // TODO: Add distance added via blast range modification
			ModularExplosion.explode(world, finalPos, owner, provider.getBaseExplosionBlastRadius(), provider.getBaseExplosionDamage(), definition.archetype, definition.modifiers);
		}
	}
	
	public void addModifiers(List<ExplosionModifier> modifiers) {
		try {
			this.modifiers.addAll(modifiers);
		} catch (java.lang.UnsupportedOperationException e) { // `modifiers` collection actually may be ImmutableCollection
			var newModifiers = new ArrayList<ExplosionModifier>();
			newModifiers.addAll(this.modifiers);
			newModifiers.addAll(modifiers);
			this.modifiers = newModifiers;
		}
	}
	
	public ExplosionArchetype getArchetype() {
		return archetype;
	}
	
	public void setArchetype(ExplosionArchetype archetype) {
		this.archetype = archetype;
	}
	
	public boolean isValid(ModularExplosionProvider provider) {
		if (this.modifiers.size() > provider.getMaxExplosionModifiers()) {
			return false;
		}
		
		Map<ExplosionModifierType, Integer> occurrences = new HashMap<>();
		for (ExplosionModifier modifier : modifiers) {
			if (!modifier.type.acceptsArchetype(archetype)) {
				return false;
			}
			ExplosionModifierType type = modifier.getType();
			int typeCount = occurrences.getOrDefault(type, 0);
			if (typeCount > type.getMaxModifiersForType()) {
				return false;
			}
			occurrences.put(type, typeCount + 1);
		}
		
		return true;
	}
	
	public int getModifierCount() {
		return this.modifiers.size();
	}
	
	public void attachToStack(ItemStack stack) {
		stack.set(SpectrumDataComponentTypes.MODULAR_EXPLOSION, this);
	}
	
	// Tooltips
	public void appendTooltip(List<Component> tooltip, ModularExplosionProvider provider) {
		int modifierCount = this.modifiers.size();
		int maxModifierCount = provider.getMaxExplosionModifiers();
		
		tooltip.add(archetype.getName());
		tooltip.add(Component.translatable("item.spectrum.tooltip.explosives.remaining_slots", modifierCount, maxModifierCount).withStyle(ChatFormatting.GRAY));
		
		if (modifierCount == 0) {
			tooltip.add(Component.translatable("item.spectrum.tooltip.explosives.modifiers").withStyle(ChatFormatting.GRAY));
		} else {
			for (ExplosionModifier explosionModifier : modifiers) {
				tooltip.add(explosionModifier.getName());
			}
		}
	}
	
	// Calls the explosion logic
	public void explode(@NotNull ServerLevel world, BlockPos pos, @Nullable Player owner, double baseBlastRadius, float baseDamage) {
		ModularExplosion.explode(world, pos, owner, baseBlastRadius, baseDamage, this.archetype, this.modifiers);
	}
	
}
