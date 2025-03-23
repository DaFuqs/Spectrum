package de.dafuqs.spectrum.api.interaction;

import net.minecraft.item.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemProviderRegistry {
	
	private static final Map<Item, ItemProvider> PROVIDERS = new HashMap<>();
	
	public static void register(Item item, ItemProvider provider) {
		PROVIDERS.put(item, provider);
	}
	
	public static @Nullable ItemProvider getProvider(ItemStack stack) {
		return PROVIDERS.get(stack.getItem());
	}
	
}
