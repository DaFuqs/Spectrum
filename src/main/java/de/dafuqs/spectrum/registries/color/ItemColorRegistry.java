package de.dafuqs.spectrum.registries.color;

import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.Blocks;
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
		
		put(SpectrumBlocks.BLACK_SAPLING.asItem(), DyeColor.BLACK);
		put(SpectrumBlocks.BLUE_SAPLING.asItem(), DyeColor.BLUE);
		put(SpectrumBlocks.BROWN_SAPLING.asItem(), DyeColor.BROWN);
		put(SpectrumBlocks.CYAN_SAPLING.asItem(), DyeColor.CYAN);
		put(SpectrumBlocks.GRAY_SAPLING.asItem(), DyeColor.GRAY);
		put(SpectrumBlocks.GREEN_SAPLING.asItem(), DyeColor.GREEN);
		put(SpectrumBlocks.LIGHT_BLUE_SAPLING.asItem(), DyeColor.LIGHT_BLUE);
		put(SpectrumBlocks.LIGHT_GRAY_SAPLING.asItem(), DyeColor.LIGHT_GRAY);
		put(SpectrumBlocks.LIME_SAPLING.asItem(), DyeColor.LIME);
		put(SpectrumBlocks.MAGENTA_SAPLING.asItem(), DyeColor.MAGENTA);
		put(SpectrumBlocks.ORANGE_SAPLING.asItem(), DyeColor.ORANGE);
		put(SpectrumBlocks.PINK_SAPLING.asItem(), DyeColor.PINK);
		put(SpectrumBlocks.PURPLE_SAPLING.asItem(), DyeColor.PURPLE);
		put(SpectrumBlocks.RED_SAPLING.asItem(), DyeColor.RED);
		put(SpectrumBlocks.WHITE_SAPLING.asItem(), DyeColor.WHITE);
		put(SpectrumBlocks.YELLOW_SAPLING.asItem(), DyeColor.YELLOW);
		
		put(SpectrumItems.SPARKLESTONE_GEM, DyeColor.YELLOW);
		put(SpectrumItems.RAW_AZURITE, DyeColor.BLUE);
		put(SpectrumItems.REFINED_AZURITE, DyeColor.BLUE);
		put(SpectrumItems.SCARLET_FRAGMENTS, DyeColor.RED);
		put(SpectrumItems.SCARLET_GEM, DyeColor.RED);
		put(SpectrumItems.PALETUR_FRAGMENTS, DyeColor.LIGHT_BLUE);
		put(SpectrumItems.PALETUR_GEM, DyeColor.LIGHT_BLUE);
		put(SpectrumItems.QUITOXIC_POWDER, DyeColor.PURPLE);
		put(SpectrumItems.MERMAIDS_GEM, DyeColor.LIGHT_BLUE);
		put(SpectrumItems.SHOOTING_STAR, DyeColor.PURPLE);
		put(SpectrumItems.LIGHTNING_STONE, DyeColor.YELLOW);
		put(SpectrumItems.VEGETAL, DyeColor.GREEN);
		put(SpectrumItems.NEOLITH, DyeColor.PURPLE);
		put(SpectrumItems.BEDROCK_DUST, DyeColor.LIGHT_GRAY);
		put(SpectrumItems.SPECTRAL_SHARD.asItem(), DyeColor.WHITE);
		put(SpectrumItems.BOTTLE_OF_DECAY_AWAY, DyeColor.LIGHT_GRAY);
		put(SpectrumItems.EXCHANGE_STAFF.asItem(), DyeColor.LIGHT_GRAY);
		put(SpectrumItems.MIDNIGHT_ABERRATION.asItem(), DyeColor.GRAY);
		put(SpectrumItems.ASHEN_CIRCLET, DyeColor.ORANGE);
		put(SpectrumItems.AZURE_DIKE_BELT, DyeColor.BLUE);
		put(SpectrumItems.AZURE_DIKE_RING, DyeColor.BLUE);
		put(SpectrumItems.FANCIFUL_BELT, DyeColor.BROWN);
		put(SpectrumItems.FANCIFUL_CIRCLET, DyeColor.YELLOW);
		put(SpectrumItems.FANCIFUL_PENDANT, DyeColor.YELLOW);
		put(SpectrumItems.FANCIFUL_STONE_RING, DyeColor.BROWN);
		put(SpectrumItems.GLEAMING_PIN, DyeColor.YELLOW);
		put(SpectrumItems.JEOPARDANT, DyeColor.RED);
		put(SpectrumItems.LESSER_POTION_PENDANT, DyeColor.PINK);
		put(SpectrumItems.GREATER_POTION_PENDANT, DyeColor.PINK);
		put(SpectrumItems.NEAT_RING, DyeColor.WHITE);
		put(SpectrumItems.PUFF_CIRCLET, DyeColor.WHITE);
		put(SpectrumItems.RADIANCE_PIN, DyeColor.BLUE);
		put(SpectrumItems.SEVEN_LEAGUE_BOOTS, DyeColor.PURPLE);
		put(SpectrumItems.STARDUST, DyeColor.PURPLE);
		put(SpectrumItems.TAKE_OFF_BELT, DyeColor.YELLOW);
		put(SpectrumItems.TIDAL_CIRCLET, DyeColor.LIGHT_BLUE);
		put(SpectrumItems.TOTEM_PENDANT, DyeColor.BLUE);
		put(SpectrumItems.WHISPY_CIRCLET, DyeColor.BROWN);
		put(SpectrumItems.GILDED_BOOK, DyeColor.YELLOW);
		put(SpectrumItems.PIGMENT_PALETTE, DyeColor.WHITE);
		put(SpectrumItems.ARTISTS_PALETTE, DyeColor.WHITE);
		put(SpectrumItems.MOONSTRUCK_NECTAR, DyeColor.LIME);

		put(SpectrumBlocks.SPARKLESTONE_BLOCK.asItem(), DyeColor.YELLOW);
		put(SpectrumBlocks.ENDER_TREASURE.asItem(), DyeColor.PURPLE);
		put(SpectrumBlocks.CLOVER.asItem(), DyeColor.LIME);
		put(SpectrumBlocks.FOUR_LEAF_CLOVER.asItem(), DyeColor.LIME);
		put(SpectrumBlocks.OMINOUS_SAPLING.asItem(), DyeColor.BROWN);
		put(SpectrumBlocks.MOONSTONE_CLUSTER.asItem(), DyeColor.WHITE);
		put(SpectrumBlocks.POLISHED_CALCITE.asItem(), DyeColor.WHITE);
		put(SpectrumBlocks.RESONANT_LILY.asItem(), DyeColor.WHITE);
		
		// Vanilla
		put(Blocks.BLACK_WOOL.asItem(), DyeColor.BLACK);
		put(Blocks.BLUE_WOOL.asItem(), DyeColor.BLUE);
		put(Blocks.BROWN_WOOL.asItem(), DyeColor.BROWN);
		put(Blocks.CYAN_WOOL.asItem(), DyeColor.CYAN);
		put(Blocks.GRAY_WOOL.asItem(), DyeColor.GRAY);
		put(Blocks.GREEN_WOOL.asItem(), DyeColor.GREEN);
		put(Blocks.LIGHT_BLUE_WOOL.asItem(), DyeColor.LIGHT_BLUE);
		put(Blocks.LIGHT_GRAY_WOOL.asItem(), DyeColor.LIGHT_GRAY);
		put(Blocks.LIME_WOOL.asItem(), DyeColor.LIME);
		put(Blocks.MAGENTA_WOOL.asItem(), DyeColor.MAGENTA);
		put(Blocks.ORANGE_WOOL.asItem(), DyeColor.ORANGE);
		put(Blocks.PINK_WOOL.asItem(), DyeColor.PINK);
		put(Blocks.PURPLE_WOOL.asItem(), DyeColor.PURPLE);
		put(Blocks.RED_WOOL.asItem(), DyeColor.RED);
		put(Blocks.WHITE_WOOL.asItem(), DyeColor.WHITE);
		put(Blocks.YELLOW_WOOL.asItem(), DyeColor.YELLOW);
		
		put(Items.DIAMOND_HORSE_ARMOR, DyeColor.LIGHT_BLUE);
		put(Items.GOLDEN_HORSE_ARMOR, DyeColor.YELLOW);
		put(Items.LEATHER_HORSE_ARMOR, DyeColor.BROWN);
		put(Items.LEATHER_CHESTPLATE, DyeColor.BROWN);
		
		put(Items.BLACKSTONE, DyeColor.BLACK);
		put(Items.GILDED_BLACKSTONE, DyeColor.BLACK);
		put(Items.DIAMOND_BLOCK, DyeColor.LIGHT_BLUE);
		put(Items.COPPER_INGOT, DyeColor.BROWN);
		put(Items.LEATHER, DyeColor.BROWN);
		put(Items.BOOK, DyeColor.PURPLE);
		put(Items.ENCHANTED_BOOK, DyeColor.PURPLE);
		put(Items.END_STONE, DyeColor.LIGHT_GRAY);
		put(Items.ENDER_PEARL, DyeColor.PURPLE);
		put(Items.EXPERIENCE_BOTTLE, DyeColor.PURPLE);
		put(Items.FLINT, DyeColor.LIGHT_GRAY);
		put(Items.GHAST_TEAR, DyeColor.GRAY);
		put(Items.GLOWSTONE, DyeColor.YELLOW);
		put(Items.GLOWSTONE_DUST, DyeColor.YELLOW);
		put(Items.GOLD_INGOT, DyeColor.YELLOW);
		put(Items.IRON_INGOT, DyeColor.BROWN);
		put(Items.LAVA_BUCKET, DyeColor.ORANGE);
		put(Items.REDSTONE, DyeColor.RED);
		put(Items.STRING, DyeColor.GRAY);
		put(Items.GOLDEN_APPLE, DyeColor.YELLOW);
		put(Items.GOLD_BLOCK, DyeColor.YELLOW);
		put(Items.GLASS_BOTTLE, DyeColor.LIGHT_GRAY);
		put(Items.EMERALD, DyeColor.LIME);
		put(Items.DIAMOND, DyeColor.LIGHT_BLUE);
		put(Items.NETHERITE_SCRAP, DyeColor.BROWN);
		put(Items.NETHERITE_INGOT, DyeColor.BROWN);
		put(Items.ENCHANTED_GOLDEN_APPLE, DyeColor.YELLOW);
		put(Items.ARROW, DyeColor.RED);
		put(Items.BLAZE_POWDER, DyeColor.ORANGE);
		put(Items.BLAZE_ROD, DyeColor.ORANGE);
		put(Items.FIRE_CHARGE, DyeColor.ORANGE);
		put(Items.GUNPOWDER, DyeColor.GRAY);
		put(Items.MAGMA_CREAM, DyeColor.ORANGE);
		put(Items.ROTTEN_FLESH, DyeColor.GRAY);
		put(Items.SCUTE, DyeColor.BLUE);
		put(Items.SUGAR, DyeColor.YELLOW);
		put(Items.WATER_BUCKET, DyeColor.LIGHT_BLUE);
		put(Items.SPONGE, DyeColor.LIGHT_BLUE);
		put(Items.WET_SPONGE, DyeColor.LIGHT_BLUE);
		put(Items.PUFFERFISH, DyeColor.LIGHT_BLUE);
		put(Items.FISHING_ROD, DyeColor.BROWN);
		put(Items.HONEYCOMB, DyeColor.BROWN);
		put(Items.NAUTILUS_SHELL, DyeColor.PINK);
		put(Items.SOUL_SAND, DyeColor.BROWN);
		put(Items.SOUL_SOIL, DyeColor.BROWN);
		put(Items.FEATHER, DyeColor.WHITE);
		put(Items.SUNFLOWER, DyeColor.YELLOW);
		put(Items.CHORUS_FLOWER, DyeColor.MAGENTA);
		put(Items.CHORUS_FRUIT, DyeColor.MAGENTA);
		put(Items.CHORUS_PLANT, DyeColor.MAGENTA);
		put(Items.POPPED_CHORUS_FRUIT, DyeColor.MAGENTA);
		put(Items.GLASS, DyeColor.WHITE);
		put(Items.HEART_OF_THE_SEA, DyeColor.LIGHT_BLUE);
		put(Items.LAPIS_LAZULI, DyeColor.BLUE);
		put(Items.LEATHER_BOOTS, DyeColor.BROWN);
		put(Items.PHANTOM_MEMBRANE, DyeColor.LIGHT_GRAY);
		put(Items.PRISMARINE_SHARD, DyeColor.LIGHT_BLUE);
		put(Items.RABBIT_FOOT, DyeColor.BROWN);
		put(Items.TOTEM_OF_UNDYING, DyeColor.PINK);
		
		put(Blocks.ICE.asItem(), DyeColor.LIGHT_BLUE);
		put(Blocks.PACKED_ICE.asItem(), DyeColor.LIGHT_BLUE);
		put(Blocks.BLUE_ICE.asItem(), DyeColor.LIGHT_BLUE);
		
		put(Items.COAL, DyeColor.BLACK);
		put(Blocks.COAL_BLOCK.asItem(), DyeColor.BLACK);
		put(Blocks.COAL_ORE.asItem(), DyeColor.BLACK);
		put(Blocks.DEEPSLATE_COAL_ORE.asItem(), DyeColor.BLACK);
		
		put(Blocks.OAK_SAPLING.asItem(), DyeColor.GREEN);
		put(Blocks.BIRCH_SAPLING.asItem(), DyeColor.GREEN);
		put(Blocks.SPRUCE_SAPLING.asItem(), DyeColor.GREEN);
		put(Blocks.JUNGLE_SAPLING.asItem(), DyeColor.GREEN);
		put(Blocks.ACACIA_SAPLING.asItem(), DyeColor.GREEN);
		put(Blocks.DARK_OAK_SAPLING.asItem(), DyeColor.GREEN);
		put(Blocks.AZALEA.asItem(), DyeColor.GREEN);
		put(Blocks.FLOWERING_AZALEA.asItem(), DyeColor.GREEN);
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
	
	public Optional<DyeColor> getMapping(Item item, DyeColor defaultColor) {
		return Optional.of(COLORS.getOrDefault(item, defaultColor));
	}
	
}
