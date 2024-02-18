package de.dafuqs.spectrum.explosion;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * A Set of ExplosionModifiers
 * - serializable / deserializable via nbt
 * - implements the actual explosion logic
 */
public class ModularExplosionDefinition {
	
	protected ExplosionArchetype archetype = ExplosionArchetype.COSMETIC;
	protected List<ExplosionModifier> modifiers;
	
	public ModularExplosionDefinition() {
		this.modifiers = new ArrayList<>();
	}
	
	public ModularExplosionDefinition(ArrayList<ExplosionModifier> modifiers) {
		this.modifiers = modifiers;
	}
	
	public void addModifier(ExplosionModifier modifier) {
		this.modifiers.add(modifier);
	}
	
	public void addModifiers(List<ExplosionModifier> modifiers) {
		this.modifiers.addAll(modifiers);
	}
	
	public void setArchetype(ExplosionArchetype archetype) {
		this.archetype = archetype;
	}
	
	public ExplosionArchetype getArchetype() {
		return archetype;
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
	
	protected static String NBT_ROOT_KEY = "explosion_data";
	protected static String NBT_ARCHETYPE_KEY = "archetype";
	protected static String NBT_MODIFIER_LIST_KEY = "mods";
	
	// Serialization
	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();
		
		nbt.putString(NBT_ARCHETYPE_KEY, this.archetype.toString());
		NbtList modifierList = new NbtList();
		for (ExplosionModifier modifier : this.modifiers) {
			modifierList.add(NbtString.of(modifier.getId().toString()));
		}
		nbt.put(NBT_MODIFIER_LIST_KEY, modifierList);
		
		return nbt;
	}
	
	public static ModularExplosionDefinition fromNbt(NbtCompound nbt) {
		ModularExplosionDefinition set = new ModularExplosionDefinition();
		if (nbt == null) {
			return set;
		}
		
		if (nbt.contains(NBT_ARCHETYPE_KEY, NbtElement.STRING_TYPE)) {
			set.archetype = ExplosionArchetype.tryParse(nbt.getString(NBT_ARCHETYPE_KEY));
		}
		NbtList modifierList = nbt.getList(NBT_MODIFIER_LIST_KEY, NbtElement.STRING_TYPE);
		for (NbtElement e : modifierList) {
			ExplosionModifier mod = SpectrumRegistries.EXPLOSION_MODIFIERS.get(Identifier.tryParse(e.asString()));
			if (mod != null) {
				set.modifiers.add(mod);
			}
		}
		
		return set;
	}
	
	public static ModularExplosionDefinition getFromStack(ItemStack stack) {
		NbtCompound nbt = stack.getNbt();
		if (nbt != null && nbt.contains(NBT_ROOT_KEY, NbtElement.COMPOUND_TYPE)) {
			return fromNbt(nbt.getCompound(NBT_ROOT_KEY));
		}
		return new ModularExplosionDefinition();
	}
	
	public void attachToStack(ItemStack stack) {
		stack.setSubNbt(NBT_ROOT_KEY, toNbt());
	}
	
	public static void removeFromStack(ItemStack stack) {
		stack.removeSubNbt(NBT_ROOT_KEY);
	}
	
	// Tooltips
	public void appendTooltip(List<Text> tooltip, ModularExplosionProvider provider) {
		int modifierCount = this.modifiers.size();
		int maxModifierCount = provider.getMaxExplosionModifiers();
		
		tooltip.add(archetype.getName());
		tooltip.add(Text.translatable("item.spectrum.tooltip.explosives.remaining_slots", modifierCount, maxModifierCount).formatted(Formatting.GRAY));
		
		if (modifierCount == 0) {
			tooltip.add(Text.translatable("item.spectrum.tooltip.explosives.modifiers").formatted(Formatting.GRAY));
		} else {
			for (ExplosionModifier explosionModifier : modifiers) {
				tooltip.add(explosionModifier.getName());
			}
		}
	}
	
	// Calls the explosion logic
	public void explode(@NotNull ServerWorld world, BlockPos pos, @Nullable PlayerEntity owner, double baseBlastRadius, float baseDamage) {
		ModularExplosion.explode(world, pos, owner, baseBlastRadius, baseDamage, this.archetype, this.modifiers);
	}
	
	// Calls the explosion logic
	public static void explode(@NotNull ServerWorld world, BlockPos pos, @Nullable PlayerEntity owner, ItemStack stack) {
		if (stack.getItem() instanceof ModularExplosionProvider provider) {
			ModularExplosionDefinition definition = getFromStack(stack);
			ModularExplosion.explode(world, pos, owner, provider.getBaseExplosionBlastRadius(), provider.getBaseExplosionDamage(), definition.archetype, definition.modifiers);
		}
	}
	
	public static void explode(@NotNull ServerWorld world, BlockPos pos, Direction direction, @Nullable PlayerEntity owner, ItemStack stack) {
		if (stack.getItem() instanceof ModularExplosionProvider provider) {
			ModularExplosionDefinition definition = getFromStack(stack);
			BlockPos finalPos = pos.offset(direction, (int) provider.getBaseExplosionBlastRadius() - 2); // TODO: Add distance added via blast range modification
			ModularExplosion.explode(world, finalPos, owner, provider.getBaseExplosionBlastRadius(), provider.getBaseExplosionDamage(), definition.archetype, definition.modifiers);
		}
	}
	
}
