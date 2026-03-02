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
import org.jetbrains.annotations.*;

public record BottomlessComponent(@NotNull BottomlessItemHandler handler) {
	
	public static final BottomlessComponent DEFAULT = new BottomlessComponent(new BottomlessItemHandler(BottomlessBundleItem.getMaxStoredAmount(0), false, false, ItemStack.EMPTY, 0));
	
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
	public @NotNull static BottomlessComponent get(ItemStack bottomlessBundle, @Nullable HolderLookup.Provider registryLookup, boolean recalculateEnchantmentDependentValuesAndSet) {
		@Nullable BottomlessComponent existing = bottomlessBundle.get(SpectrumDataComponentTypes.BOTTOMLESS_STACK);
		
		ItemStack stack = ItemStack.EMPTY;
		long count = 0;
		long maxStoredAmount = DEFAULT.handler().capacity();
		boolean deletesOverflow = false;
		if(registryLookup != null) {
			maxStoredAmount = BottomlessBundleItem.getMaxStoredAmount(bottomlessBundle.getEnchantmentLevel(registryLookup.lookup(Registries.ENCHANTMENT).flatMap(impl -> impl.get(Enchantments.POWER)).orElse(null)));
			deletesOverflow = EnchantmentHelper.hasTag(bottomlessBundle, SpectrumEnchantmentTags.DELETES_OVERFLOW);
		}
		boolean locked = false;
		if(existing != null) {
			stack = existing.handler().variant();
			count = existing.handler().count();
			locked = existing.handler().locked();
		}
		
		BottomlessComponent result = new BottomlessComponent(maxStoredAmount, deletesOverflow, locked, stack, count);
		
		if(recalculateEnchantmentDependentValuesAndSet) {
			if(existing == null) {
				stack.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, result);
			} else if(maxStoredAmount != existing.handler().capacity() || deletesOverflow != existing.handler().deletesOverflow()) {
				stack.set(SpectrumDataComponentTypes.BOTTOMLESS_STACK, result);
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
	
}
