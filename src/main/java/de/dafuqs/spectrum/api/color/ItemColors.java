package de.dafuqs.spectrum.api.color;

import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

import java.util.*;

public class ItemColors extends ColorRegistry<Item> {
	
	private static final HashMap<Item, DyeColor> COLORS = new HashMap<>() {{
		// Vanilla
		put(Items.AMETHYST_SHARD, DyeColor.MAGENTA);
		
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
		
		put(Items.BEEF, DyeColor.PINK);
		put(Items.CHICKEN, DyeColor.PINK);
		put(Items.MUTTON, DyeColor.PINK);
		put(Items.PORKCHOP, DyeColor.PINK);
		put(Items.RABBIT, DyeColor.PINK);
		put(Items.COOKED_BEEF, DyeColor.PINK);
		put(Items.COOKED_CHICKEN, DyeColor.PINK);
		put(Items.COOKED_MUTTON, DyeColor.PINK);
		put(Items.COOKED_PORKCHOP, DyeColor.PINK);
		put(Items.COOKED_RABBIT, DyeColor.PINK);
		
		put(Items.ANVIL, DyeColor.BROWN);
		put(Items.BLACKSTONE, DyeColor.BLACK);
		put(Items.GILDED_BLACKSTONE, DyeColor.BLACK);
		put(Items.DIAMOND_BLOCK, DyeColor.LIGHT_BLUE);
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
		put(Items.NETHER_STAR, DyeColor.BLACK);
		put(Items.COPPER_INGOT, DyeColor.BROWN);
		put(Items.RAW_COPPER, DyeColor.BROWN);
		put(Items.COPPER_ORE, DyeColor.BROWN);
		put(Items.COPPER_BLOCK, DyeColor.BROWN);
		put(Items.DEEPSLATE_COPPER_ORE, DyeColor.BROWN);
		
		put(Items.NETHERITE_SCRAP, DyeColor.BROWN);
		put(Items.NETHERITE_INGOT, DyeColor.BROWN);
		put(Items.NETHERITE_AXE, DyeColor.BROWN);
		put(Items.NETHERITE_BLOCK, DyeColor.BROWN);
		put(Items.NETHERITE_BOOTS, DyeColor.BROWN);
		put(Items.NETHERITE_CHESTPLATE, DyeColor.BROWN);
		put(Items.NETHERITE_HELMET, DyeColor.BROWN);
		put(Items.NETHERITE_LEGGINGS, DyeColor.BROWN);
		put(Items.NETHERITE_SWORD, DyeColor.BROWN);
		put(Items.NETHERITE_PICKAXE, DyeColor.BROWN);
		put(Items.NETHERITE_HOE, DyeColor.BROWN);
		put(Items.NETHERITE_SHOVEL, DyeColor.BROWN);
		
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
		put(Items.PHANTOM_MEMBRANE, DyeColor.LIGHT_GRAY);
		put(Items.PRISMARINE_SHARD, DyeColor.LIGHT_BLUE);
		put(Items.RABBIT_FOOT, DyeColor.BROWN);
		put(Items.RABBIT_HIDE, DyeColor.BROWN);
		put(Items.TOTEM_OF_UNDYING, DyeColor.PINK);
		put(Items.APPLE, DyeColor.PINK);
		put(Items.CARROT, DyeColor.PINK);
		put(Items.BAKED_POTATO, DyeColor.PINK);
		put(Items.CACTUS, DyeColor.LIME);
		put(Items.TORCHFLOWER_SEEDS, DyeColor.LIME);
		put(Items.QUARTZ, DyeColor.LIGHT_GRAY);
		put(Items.QUARTZ_BLOCK, DyeColor.LIGHT_GRAY);

		put(Items.SCULK, DyeColor.BLACK);
		put(Items.SCULK_CATALYST, DyeColor.BLACK);
		put(Items.SCULK_SENSOR, DyeColor.BLACK);
		put(Items.SCULK_VEIN, DyeColor.BLACK);
		put(Items.SCULK_SHRIEKER, DyeColor.BLACK);
		
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

		put(Blocks.POPPY.asItem(), DyeColor.PURPLE);

		
		put(Items.ACACIA_PLANKS, DyeColor.LIME);
		put(Items.BIRCH_PLANKS, DyeColor.LIME);
		put(Items.BOW, DyeColor.RED);
		put(Items.CRIMSON_PLANKS, DyeColor.ORANGE);
		put(Items.CROSSBOW, DyeColor.RED);
		put(Items.DARK_OAK_PLANKS, DyeColor.LIME);
		put(Items.JUNGLE_PLANKS, DyeColor.LIME);
		put(Items.OAK_PLANKS, DyeColor.LIME);
		put(Items.ROSE_BUSH, DyeColor.RED);
		put(Items.SHEARS, DyeColor.LIGHT_BLUE);
		put(Items.SPRUCE_PLANKS, DyeColor.LIME);
		put(Items.WARPED_PLANKS, DyeColor.LIME);
		put(Items.MANGROVE_PLANKS, DyeColor.LIME);
		put(Items.BAMBOO_PLANKS, DyeColor.LIME);
		put(Items.CHERRY_PLANKS, DyeColor.LIME);
		
		put(Items.GOLDEN_HELMET, DyeColor.YELLOW);
		put(Items.GOLDEN_CHESTPLATE, DyeColor.YELLOW);
		put(Items.GOLDEN_LEGGINGS, DyeColor.YELLOW);
		put(Items.GOLDEN_BOOTS, DyeColor.YELLOW);
		put(Items.GOLDEN_AXE, DyeColor.YELLOW);
		put(Items.GOLDEN_PICKAXE, DyeColor.YELLOW);
		put(Items.GOLDEN_SWORD, DyeColor.YELLOW);
		put(Items.GOLDEN_SHOVEL, DyeColor.YELLOW);
		put(Items.GOLDEN_HOE, DyeColor.YELLOW);
		
		put(Items.DIAMOND_HELMET, DyeColor.LIGHT_BLUE);
		put(Items.DIAMOND_CHESTPLATE, DyeColor.LIGHT_BLUE);
		put(Items.DIAMOND_LEGGINGS, DyeColor.LIGHT_BLUE);
		put(Items.DIAMOND_BOOTS, DyeColor.LIGHT_BLUE);
		put(Items.DIAMOND_AXE, DyeColor.LIGHT_BLUE);
		put(Items.DIAMOND_PICKAXE, DyeColor.LIGHT_BLUE);
		put(Items.DIAMOND_SWORD, DyeColor.LIGHT_BLUE);
		put(Items.DIAMOND_SHOVEL, DyeColor.LIGHT_BLUE);
		put(Items.DIAMOND_HOE, DyeColor.LIGHT_BLUE);
		
		put(Items.IRON_HELMET, DyeColor.BROWN);
		put(Items.IRON_CHESTPLATE, DyeColor.BROWN);
		put(Items.IRON_LEGGINGS, DyeColor.BROWN);
		put(Items.IRON_BOOTS, DyeColor.BROWN);
		put(Items.IRON_AXE, DyeColor.BROWN);
		put(Items.IRON_PICKAXE, DyeColor.BROWN);
		put(Items.IRON_SWORD, DyeColor.BROWN);
		put(Items.IRON_SHOVEL, DyeColor.BROWN);
		put(Items.IRON_HOE, DyeColor.BROWN);
		
		put(Items.LEATHER_HELMET, DyeColor.BROWN);
		put(Items.LEATHER_CHESTPLATE, DyeColor.BROWN);
		put(Items.LEATHER_LEGGINGS, DyeColor.BROWN);
		put(Items.LEATHER_BOOTS, DyeColor.BROWN);
		
		put(Items.STONE_AXE, DyeColor.BROWN);
		put(Items.STONE_PICKAXE, DyeColor.BROWN);
		put(Items.STONE_SWORD, DyeColor.BROWN);
		put(Items.STONE_SHOVEL, DyeColor.BROWN);
		put(Items.STONE_HOE, DyeColor.BROWN);
		
		put(Items.WOODEN_AXE, DyeColor.LIME);
		put(Items.WOODEN_PICKAXE, DyeColor.LIME);
		put(Items.WOODEN_SWORD, DyeColor.LIME);
		put(Items.WOODEN_SHOVEL, DyeColor.LIME);
		put(Items.WOODEN_HOE, DyeColor.LIME);
		
		put(Items.TRIDENT, DyeColor.LIGHT_BLUE);
		put(Items.ECHO_SHARD, DyeColor.GRAY);
		
		put(Items.WHITE_BANNER, DyeColor.WHITE);
		put(Items.ORANGE_BANNER, DyeColor.ORANGE);
		put(Items.MAGENTA_BANNER, DyeColor.MAGENTA);
		put(Items.LIGHT_BLUE_BANNER, DyeColor.LIGHT_BLUE);
		put(Items.YELLOW_BANNER, DyeColor.YELLOW);
		put(Items.LIME_BANNER, DyeColor.LIME);
		put(Items.PINK_BANNER, DyeColor.PINK);
		put(Items.GRAY_BANNER, DyeColor.GRAY);
		put(Items.LIGHT_GRAY_BANNER, DyeColor.LIGHT_GRAY);
		put(Items.CYAN_BANNER, DyeColor.CYAN);
		put(Items.PURPLE_BANNER, DyeColor.PURPLE);
		put(Items.BLUE_BANNER, DyeColor.BLUE);
		put(Items.BROWN_BANNER, DyeColor.BROWN);
		put(Items.GREEN_BANNER, DyeColor.GREEN);
		put(Items.RED_BANNER, DyeColor.RED);
		put(Items.BLACK_BANNER, DyeColor.BLACK);
		put(Items.WHEAT_SEEDS, DyeColor.LIME);
		put(Items.SLIME_BALL, DyeColor.GRAY);
		put(Items.SPIDER_EYE, DyeColor.GRAY);
		put(Items.PRISMARINE, DyeColor.LIGHT_BLUE);
		put(Items.WHEAT, DyeColor.BROWN);
		put(Items.SPAWNER, DyeColor.LIGHT_GRAY);
		put(Items.HAY_BLOCK, DyeColor.BROWN);
		put(Items.BROWN_MUSHROOM, DyeColor.BROWN);
		put(Items.SNOWBALL, DyeColor.PINK);
		put(Items.BAMBOO, DyeColor.GREEN);
		put(Items.RED_MUSHROOM, DyeColor.BROWN);
		put(Items.MANGROVE_PROPAGULE, DyeColor.GREEN);
		put(Items.CHERRY_SAPLING, DyeColor.GREEN);
		put(Items.WARPED_FUNGUS, DyeColor.BROWN);
		put(Items.SEAGRASS, DyeColor.GREEN);
		put(Items.STONE_BRICKS, DyeColor.BROWN);
		put(Items.IRON_BLOCK, DyeColor.BROWN);
		put(Items.DRAGON_BREATH, DyeColor.BLACK);
		put(Items.WITHER_ROSE, DyeColor.BLACK);
		put(Items.CRIMSON_FUNGUS, DyeColor.BROWN);
		put(Items.COD, DyeColor.LIGHT_BLUE);
		put(Items.COOKED_COD, DyeColor.LIGHT_BLUE);
		put(Items.SALMON, DyeColor.LIGHT_BLUE);
		put(Items.COOKED_SALMON, DyeColor.LIGHT_BLUE);
		put(Items.TROPICAL_FISH, DyeColor.LIGHT_BLUE);
		
		put(Items.GLOW_BERRIES, DyeColor.YELLOW);
		put(Items.SWEET_BERRIES, DyeColor.PINK);
		put(Items.BONE, DyeColor.GRAY);
		put(Items.DRAGON_HEAD, DyeColor.GRAY);
		put(Items.ZOMBIE_HEAD, DyeColor.GRAY);
		put(Items.SKELETON_SKULL, DyeColor.GRAY);
		put(Items.WITHER_SKELETON_SKULL, DyeColor.GRAY);
		put(Items.CREEPER_HEAD, DyeColor.GRAY);
		put(Items.PLAYER_HEAD, DyeColor.GRAY);
		put(Items.PIGLIN_HEAD, DyeColor.GRAY);
	}};

	@Override
	public void registerColorMapping(Item item, DyeColor dyeColor) {
		COLORS.put(item, dyeColor);
	}
	
	@Override
	public Optional<DyeColor> getMapping(Item item) {
		if (COLORS.containsKey(item)) {
			return Optional.of(COLORS.get(item));
		} else {
			return Optional.empty();
		}
	}
	
	public Optional<DyeColor> getMapping(Item item, DyeColor defaultColor) {
		return Optional.of(COLORS.getOrDefault(item, defaultColor));
	}
	
}
