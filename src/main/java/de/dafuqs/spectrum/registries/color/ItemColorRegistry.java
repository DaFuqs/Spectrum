package de.dafuqs.spectrum.registries.color;

import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Optional;

public class ItemColorRegistry extends ColorRegistry<Item> {
	
	private static final HashMap<Item, DyeColor> COLORS = new HashMap<>() {{
		put(Items.AMETHYST_SHARD, DyeColor.MAGENTA);
		put(SpectrumItems.TOPAZ_SHARD, DyeColor.CYAN);
		put(SpectrumItems.CITRINE_SHARD, DyeColor.YELLOW);
		put(SpectrumItems.ONYX_SHARD, DyeColor.BLACK);
		put(SpectrumItems.MOONSTONE_SHARD, DyeColor.WHITE);
		
		put(Items.ENCHANTED_GOLDEN_APPLE, DyeColor.YELLOW);
	}};
	
	@Override
	public void registerColorMapping(Identifier identifier, DyeColor dyeColor) {
		Item item = Registry.ITEM.get(identifier);
		if(item != Items.AIR) {
			COLORS.put(item, dyeColor);
		}
	}
	
	@Override
	public void registerColorMapping(Item item, DyeColor dyeColor) {
		COLORS.put(item, dyeColor);
	}
	
	@Override
	public Optional<DyeColor> getMapping(Item item) {
		if(COLORS.containsKey(item)) {
			return  Optional.of(COLORS.get(item));
		} else {
			return Optional.empty();
		}
	}
	
}
