package de.dafuqs.spectrum.blocks.bottomless_bundle;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import org.jspecify.annotations.Nullable;

public record BottomlessComponent(BottomlessItemHandler handler) {
	
	private static final long MAX_STORED_AMOUNT_BASE = 20000;
	
	public static final BottomlessComponent DEFAULT = new BottomlessComponent(new BottomlessItemHandler(getMaxStoredAmount(0), false, false, ItemStack.EMPTY, 0));
	
	public static Codec<BottomlessComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.LONG.fieldOf("capacity").forGetter(component -> component.handler.capacity()),
			Codec.BOOL.fieldOf("deletesOverflow").forGetter(component -> component.handler.deletesOverflow()),
			Codec.BOOL.fieldOf("locked").forGetter(component -> component.handler.locked()),
			ItemStack.OPTIONAL_CODEC.fieldOf("variant").forGetter(component -> component.handler.variant()),
			Codec.LONG.fieldOf("count").forGetter(component -> component.handler.count())
	).apply(instance, BottomlessComponent::new));
	
	
	public static StreamCodec<RegistryFriendlyByteBuf, BottomlessComponent> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_LONG, component -> component.handler.capacity(),
			ByteBufCodecs.BOOL, component -> component.handler.deletesOverflow(),
			ByteBufCodecs.BOOL, component -> component.handler.locked(),
			ItemStack.OPTIONAL_STREAM_CODEC, component -> component.handler.variant(),
			ByteBufCodecs.VAR_LONG, component -> component.handler.count(),
			BottomlessComponent::new
	);
	
	/**
	 * Get the Component - recalculates its values if enchantments changed. Always use this one serverside!
	 * @param bottomlessBundle the Bundle stack
	 * @param registryLookup The registryLookup (since EMI passes a HolderLookup without world that value is nullable)
	 * @param recalculateEnchantmentDependentValuesAndSet set the component if enchantment changed (always true serverside)
	 * @return the component
	 */
	public static BottomlessComponent get(ItemStack bottomlessBundle, HolderLookup.@Nullable Provider registryLookup, boolean recalculateEnchantmentDependentValuesAndSet) {
		@Nullable BottomlessComponent existing = bottomlessBundle.get(SpectrumDataComponentTypes.BOTTOMLESS_STACK);
		
		ItemStack storedStack = ItemStack.EMPTY;
		long storedCount = 0;
		long maxStoredCount = DEFAULT.handler().capacity();
		boolean deletesOverflow = false;
		if(registryLookup != null) {
			maxStoredCount = getMaxStoredAmount(bottomlessBundle.getEnchantmentLevel(registryLookup.lookup(Registries.ENCHANTMENT).flatMap(impl -> impl.get(Enchantments.POWER)).orElse(null)));
			deletesOverflow = EnchantmentHelper.hasTag(bottomlessBundle, SpectrumEnchantmentTags.DELETES_OVERFLOW);
		}
		boolean locked = false;
		if(existing != null) {
			storedStack = existing.handler().variant();
			storedCount = existing.handler().count();
			locked = existing.handler().locked();
		}
		
		BottomlessComponent result = new BottomlessComponent(maxStoredCount, deletesOverflow, locked, storedStack, storedCount);
		
		if(recalculateEnchantmentDependentValuesAndSet) {
			if(existing == null) {
				bottomlessBundle.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, result);
			} else if(maxStoredCount != existing.handler().capacity() || deletesOverflow != existing.handler().deletesOverflow()) {
				bottomlessBundle.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, result);
			}
		}
		
		return result;
	}
	
	// only use for constructing new ones or changing values.
	// use get() for lookups
	@Deprecated
	public BottomlessComponent(long capacity, boolean deletesOverflow, boolean locked, ItemStack variant, long count) {
		this(new BottomlessItemHandler(capacity, deletesOverflow, locked, variant, count));
	}
	
	public static long getMaxStoredAmount(int powerLevel) {
		return MAX_STORED_AMOUNT_BASE * (int) Math.pow(10, Math.min(5, powerLevel)); // to not exceed int max
	}
	
}
