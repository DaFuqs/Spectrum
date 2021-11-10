package de.dafuqs.spectrum.registries.color;

import de.dafuqs.spectrum.registries.SpectrumBlocks;
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
		
		put(SpectrumItems.AMETHYST_POWDER, DyeColor.MAGENTA);
		put(SpectrumItems.TOPAZ_POWDER, DyeColor.CYAN);
		put(SpectrumItems.CITRINE_POWDER, DyeColor.YELLOW);
		put(SpectrumItems.ONYX_POWDER, DyeColor.BLACK);
		put(SpectrumItems.MOONSTONE_POWDER, DyeColor.WHITE);
		
		put(SpectrumItems.VIBRANT_MAGENTA_CATKIN, DyeColor.MAGENTA);
		put(SpectrumItems.VIBRANT_CYAN_CATKIN, DyeColor.CYAN);
		put(SpectrumItems.VIBRANT_YELLOW_CATKIN, DyeColor.YELLOW);
		put(SpectrumItems.VIBRANT_BLACK_CATKIN, DyeColor.BLACK);
		put(SpectrumItems.VIBRANT_WHITE_CATKIN, DyeColor.WHITE);
		put(SpectrumItems.LUCID_MAGENTA_CATKIN, DyeColor.MAGENTA);
		put(SpectrumItems.LUCID_CYAN_CATKIN, DyeColor.CYAN);
		put(SpectrumItems.LUCID_YELLOW_CATKIN, DyeColor.YELLOW);
		put(SpectrumItems.LUCID_BLACK_CATKIN, DyeColor.BLACK);
		put(SpectrumItems.LUCID_WHITE_CATKIN, DyeColor.WHITE);
		
		put(SpectrumItems.BLACK_PIGMENT, DyeColor.BLACK);
		put(SpectrumItems.BLUE_PIGMENT, DyeColor.BLUE);
		put(SpectrumItems.BROWN_PIGMENT, DyeColor.BROWN);
		put(SpectrumItems.CYAN_PIGMENT, DyeColor.CYAN);
		put(SpectrumItems.GRAY_PIGMENT, DyeColor.GRAY);
		put(SpectrumItems.GREEN_PIGMENT, DyeColor.GREEN);
		put(SpectrumItems.LIGHT_BLUE_PIGMENT, DyeColor.LIGHT_BLUE);
		put(SpectrumItems.LIGHT_GRAY_PIGMENT, DyeColor.LIGHT_GRAY);
		put(SpectrumItems.LIME_PIGMENT, DyeColor.LIME);
		put(SpectrumItems.MAGENTA_PIGMENT, DyeColor.MAGENTA);
		put(SpectrumItems.ORANGE_PIGMENT, DyeColor.ORANGE);
		put(SpectrumItems.PINK_PIGMENT, DyeColor.PINK);
		put(SpectrumItems.PURPLE_PIGMENT, DyeColor.PURPLE);
		put(SpectrumItems.RED_PIGMENT, DyeColor.RED);
		put(SpectrumItems.WHITE_PIGMENT, DyeColor.WHITE);
		put(SpectrumItems.YELLOW_PIGMENT, DyeColor.YELLOW);
		
		put(SpectrumItems.SPARKLESTONE_GEM, DyeColor.YELLOW);
		put(SpectrumItems.RAW_AZURITE, DyeColor.BLUE);
		put(SpectrumItems.SHAPED_AZURITE, DyeColor.BLUE);
		put(SpectrumItems.SCARLET_FRAGMENTS, DyeColor.RED);
		put(SpectrumItems.SCARLET_GEM, DyeColor.RED);
		put(SpectrumItems.PALETUR_FRAGMENTS, DyeColor.LIGHT_BLUE);
		put(SpectrumItems.PALETUR_GEM, DyeColor.LIGHT_BLUE);
		put(SpectrumItems.QUITOXIC_POWDER, DyeColor.PURPLE);
		put(SpectrumItems.MERMAIDS_GEM, DyeColor.LIGHT_BLUE);
		put(SpectrumItems.SHOOTING_STAR, DyeColor.PURPLE);
		put(SpectrumItems.LIGHTNING_STONE, DyeColor.YELLOW);
		put(SpectrumItems.VEGETAL, DyeColor.GREEN);
		put(SpectrumItems.CORRUPTED_OBSIDIAN_DUST, DyeColor.PURPLE);
		put(SpectrumItems.BEDROCK_DUST, DyeColor.LIGHT_GRAY);
		put(SpectrumBlocks.ENDER_TREASURE.asItem(), DyeColor.PURPLE);
		
		put(Items.GOLDEN_APPLE, DyeColor.YELLOW);
		put(Items.GOLD_BLOCK, DyeColor.YELLOW);
		put(Items.GLASS_BOTTLE, DyeColor.LIGHT_GRAY);
		put(SpectrumBlocks.SPARKLESTONE_BLOCK.asItem(), DyeColor.YELLOW);
		put(Items.ENCHANTED_GOLDEN_APPLE, DyeColor.YELLOW);
		put(SpectrumItems.BOTTLE_OF_DECAY_AWAY, DyeColor.LIGHT_GRAY);
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
