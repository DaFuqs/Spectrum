package de.dafuqs.spectrum.api.color;

import de.dafuqs.spectrum.api.energy.color.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

import java.util.*;

public class ItemColors extends ColorRegistry<Item> {
	
	private static final HashMap<Item, InkColor> COLORS = new HashMap<>() {{
		// Vanilla
		put(Items.AMETHYST_SHARD, InkColors.MAGENTA);
		
		put(Blocks.BLACK_WOOL.asItem(), InkColors.BLACK);
		put(Blocks.BLUE_WOOL.asItem(), InkColors.BLUE);
		put(Blocks.BROWN_WOOL.asItem(), InkColors.BROWN);
		put(Blocks.CYAN_WOOL.asItem(), InkColors.CYAN);
		put(Blocks.GRAY_WOOL.asItem(), InkColors.GRAY);
		put(Blocks.GREEN_WOOL.asItem(), InkColors.GREEN);
		put(Blocks.LIGHT_BLUE_WOOL.asItem(), InkColors.LIGHT_BLUE);
		put(Blocks.LIGHT_GRAY_WOOL.asItem(), InkColors.LIGHT_GRAY);
		put(Blocks.LIME_WOOL.asItem(), InkColors.LIME);
		put(Blocks.MAGENTA_WOOL.asItem(), InkColors.MAGENTA);
		put(Blocks.ORANGE_WOOL.asItem(), InkColors.ORANGE);
		put(Blocks.PINK_WOOL.asItem(), InkColors.PINK);
		put(Blocks.PURPLE_WOOL.asItem(), InkColors.PURPLE);
		put(Blocks.RED_WOOL.asItem(), InkColors.RED);
		put(Blocks.WHITE_WOOL.asItem(), InkColors.WHITE);
		put(Blocks.YELLOW_WOOL.asItem(), InkColors.YELLOW);
		
		put(Items.DIAMOND_HORSE_ARMOR, InkColors.LIGHT_BLUE);
		put(Items.GOLDEN_HORSE_ARMOR, InkColors.YELLOW);
		put(Items.LEATHER_HORSE_ARMOR, InkColors.BROWN);
		
		put(Items.BEEF, InkColors.PINK);
		put(Items.CHICKEN, InkColors.PINK);
		put(Items.MUTTON, InkColors.PINK);
		put(Items.PORKCHOP, InkColors.PINK);
		put(Items.RABBIT, InkColors.PINK);
		put(Items.COOKED_BEEF, InkColors.PINK);
		put(Items.COOKED_CHICKEN, InkColors.PINK);
		put(Items.COOKED_MUTTON, InkColors.PINK);
		put(Items.COOKED_PORKCHOP, InkColors.PINK);
		put(Items.COOKED_RABBIT, InkColors.PINK);
		
		put(Items.ANVIL, InkColors.BROWN);
		put(Items.BLACKSTONE, InkColors.BLACK);
		put(Items.GILDED_BLACKSTONE, InkColors.BLACK);
		put(Items.DIAMOND_BLOCK, InkColors.LIGHT_BLUE);
		put(Items.LEATHER, InkColors.BROWN);
		put(Items.BOOK, InkColors.PURPLE);
		put(Items.ENCHANTED_BOOK, InkColors.PURPLE);
		put(Items.END_STONE, InkColors.LIGHT_GRAY);
		put(Items.ENDER_PEARL, InkColors.PURPLE);
		put(Items.EXPERIENCE_BOTTLE, InkColors.PURPLE);
		put(Items.FLINT, InkColors.LIGHT_GRAY);
		put(Items.GHAST_TEAR, InkColors.GRAY);
		put(Items.GLOWSTONE, InkColors.YELLOW);
		put(Items.GLOWSTONE_DUST, InkColors.YELLOW);
		put(Items.GOLD_INGOT, InkColors.YELLOW);
		put(Items.IRON_INGOT, InkColors.BROWN);
		put(Items.LAVA_BUCKET, InkColors.ORANGE);
		put(Items.REDSTONE, InkColors.RED);
		put(Items.STRING, InkColors.GRAY);
		put(Items.GOLDEN_APPLE, InkColors.YELLOW);
		put(Items.GOLD_BLOCK, InkColors.YELLOW);
		put(Items.GLASS_BOTTLE, InkColors.LIGHT_GRAY);
		put(Items.EMERALD, InkColors.LIME);
		put(Items.DIAMOND, InkColors.LIGHT_BLUE);
		put(Items.NETHER_STAR, InkColors.BLACK);
		put(Items.COPPER_INGOT, InkColors.BROWN);
		put(Items.RAW_COPPER, InkColors.BROWN);
		put(Items.COPPER_ORE, InkColors.BROWN);
		put(Items.COPPER_BLOCK, InkColors.BROWN);
		put(Items.DEEPSLATE_COPPER_ORE, InkColors.BROWN);
		
		put(Items.NETHERITE_SCRAP, InkColors.BROWN);
		put(Items.NETHERITE_INGOT, InkColors.BROWN);
		put(Items.NETHERITE_AXE, InkColors.BROWN);
		put(Items.NETHERITE_BLOCK, InkColors.BROWN);
		put(Items.NETHERITE_BOOTS, InkColors.BROWN);
		put(Items.NETHERITE_CHESTPLATE, InkColors.BROWN);
		put(Items.NETHERITE_HELMET, InkColors.BROWN);
		put(Items.NETHERITE_LEGGINGS, InkColors.BROWN);
		put(Items.NETHERITE_SWORD, InkColors.BROWN);
		put(Items.NETHERITE_PICKAXE, InkColors.BROWN);
		put(Items.NETHERITE_HOE, InkColors.BROWN);
		put(Items.NETHERITE_SHOVEL, InkColors.BROWN);
		
		put(Items.ENCHANTED_GOLDEN_APPLE, InkColors.YELLOW);
		put(Items.ARROW, InkColors.RED);
		put(Items.BLAZE_POWDER, InkColors.ORANGE);
		put(Items.BLAZE_ROD, InkColors.ORANGE);
		put(Items.FIRE_CHARGE, InkColors.ORANGE);
		put(Items.GUNPOWDER, InkColors.GRAY);
		put(Items.MAGMA_CREAM, InkColors.ORANGE);
		put(Items.ROTTEN_FLESH, InkColors.GRAY);
		put(Items.TURTLE_SCUTE, InkColors.BLUE);
		put(Items.SUGAR, InkColors.YELLOW);
		put(Items.WATER_BUCKET, InkColors.LIGHT_BLUE);
		put(Items.SPONGE, InkColors.LIGHT_BLUE);
		put(Items.WET_SPONGE, InkColors.LIGHT_BLUE);
		put(Items.PUFFERFISH, InkColors.LIGHT_BLUE);
		put(Items.FISHING_ROD, InkColors.BROWN);
		put(Items.HONEYCOMB, InkColors.BROWN);
		put(Items.NAUTILUS_SHELL, InkColors.PINK);
		put(Items.SOUL_SAND, InkColors.BROWN);
		put(Items.SOUL_SOIL, InkColors.BROWN);
		put(Items.FEATHER, InkColors.WHITE);
		put(Items.SUNFLOWER, InkColors.YELLOW);
		put(Items.CHORUS_FLOWER, InkColors.MAGENTA);
		put(Items.CHORUS_FRUIT, InkColors.MAGENTA);
		put(Items.CHORUS_PLANT, InkColors.MAGENTA);
		put(Items.POPPED_CHORUS_FRUIT, InkColors.MAGENTA);
		put(Items.GLASS, InkColors.WHITE);
		put(Items.HEART_OF_THE_SEA, InkColors.LIGHT_BLUE);
		put(Items.LAPIS_LAZULI, InkColors.BLUE);
		put(Items.PHANTOM_MEMBRANE, InkColors.LIGHT_GRAY);
		put(Items.PRISMARINE_SHARD, InkColors.LIGHT_BLUE);
		put(Items.RABBIT_FOOT, InkColors.BROWN);
		put(Items.RABBIT_HIDE, InkColors.BROWN);
		put(Items.TOTEM_OF_UNDYING, InkColors.PINK);
		put(Items.APPLE, InkColors.PINK);
		put(Items.CARROT, InkColors.PINK);
		put(Items.BAKED_POTATO, InkColors.PINK);
		put(Items.CACTUS, InkColors.LIME);
		put(Items.TORCHFLOWER_SEEDS, InkColors.LIME);
		put(Items.QUARTZ, InkColors.LIGHT_GRAY);
		put(Items.QUARTZ_BLOCK, InkColors.LIGHT_GRAY);
		
		put(Items.SCULK, InkColors.BLACK);
		put(Items.SCULK_CATALYST, InkColors.BLACK);
		put(Items.SCULK_SENSOR, InkColors.BLACK);
		put(Items.SCULK_VEIN, InkColors.BLACK);
		put(Items.SCULK_SHRIEKER, InkColors.BLACK);
		
		put(Blocks.ICE.asItem(), InkColors.LIGHT_BLUE);
		put(Blocks.PACKED_ICE.asItem(), InkColors.LIGHT_BLUE);
		put(Blocks.BLUE_ICE.asItem(), InkColors.LIGHT_BLUE);
		
		put(Items.COAL, InkColors.BLACK);
		put(Blocks.COAL_BLOCK.asItem(), InkColors.BLACK);
		put(Blocks.COAL_ORE.asItem(), InkColors.BLACK);
		put(Blocks.DEEPSLATE_COAL_ORE.asItem(), InkColors.BLACK);
		
		put(Blocks.OAK_SAPLING.asItem(), InkColors.GREEN);
		put(Blocks.BIRCH_SAPLING.asItem(), InkColors.GREEN);
		put(Blocks.SPRUCE_SAPLING.asItem(), InkColors.GREEN);
		put(Blocks.JUNGLE_SAPLING.asItem(), InkColors.GREEN);
		put(Blocks.ACACIA_SAPLING.asItem(), InkColors.GREEN);
		put(Blocks.DARK_OAK_SAPLING.asItem(), InkColors.GREEN);
		put(Blocks.AZALEA.asItem(), InkColors.GREEN);
		put(Blocks.FLOWERING_AZALEA.asItem(), InkColors.GREEN);
		
		put(Blocks.POPPY.asItem(), InkColors.PURPLE);
		
		
		put(Items.ACACIA_PLANKS, InkColors.LIME);
		put(Items.BIRCH_PLANKS, InkColors.LIME);
		put(Items.BOW, InkColors.RED);
		put(Items.CRIMSON_PLANKS, InkColors.ORANGE);
		put(Items.CROSSBOW, InkColors.RED);
		put(Items.DARK_OAK_PLANKS, InkColors.LIME);
		put(Items.JUNGLE_PLANKS, InkColors.LIME);
		put(Items.OAK_PLANKS, InkColors.LIME);
		put(Items.ROSE_BUSH, InkColors.RED);
		put(Items.SHEARS, InkColors.LIGHT_BLUE);
		put(Items.SPRUCE_PLANKS, InkColors.LIME);
		put(Items.WARPED_PLANKS, InkColors.LIME);
		put(Items.MANGROVE_PLANKS, InkColors.LIME);
		put(Items.BAMBOO_PLANKS, InkColors.LIME);
		put(Items.CHERRY_PLANKS, InkColors.LIME);
		
		put(Items.GOLDEN_HELMET, InkColors.YELLOW);
		put(Items.GOLDEN_CHESTPLATE, InkColors.YELLOW);
		put(Items.GOLDEN_LEGGINGS, InkColors.YELLOW);
		put(Items.GOLDEN_BOOTS, InkColors.YELLOW);
		put(Items.GOLDEN_AXE, InkColors.YELLOW);
		put(Items.GOLDEN_PICKAXE, InkColors.YELLOW);
		put(Items.GOLDEN_SWORD, InkColors.YELLOW);
		put(Items.GOLDEN_SHOVEL, InkColors.YELLOW);
		put(Items.GOLDEN_HOE, InkColors.YELLOW);
		
		put(Items.DIAMOND_HELMET, InkColors.LIGHT_BLUE);
		put(Items.DIAMOND_CHESTPLATE, InkColors.LIGHT_BLUE);
		put(Items.DIAMOND_LEGGINGS, InkColors.LIGHT_BLUE);
		put(Items.DIAMOND_BOOTS, InkColors.LIGHT_BLUE);
		put(Items.DIAMOND_AXE, InkColors.LIGHT_BLUE);
		put(Items.DIAMOND_PICKAXE, InkColors.LIGHT_BLUE);
		put(Items.DIAMOND_SWORD, InkColors.LIGHT_BLUE);
		put(Items.DIAMOND_SHOVEL, InkColors.LIGHT_BLUE);
		put(Items.DIAMOND_HOE, InkColors.LIGHT_BLUE);
		
		put(Items.IRON_HELMET, InkColors.BROWN);
		put(Items.IRON_CHESTPLATE, InkColors.BROWN);
		put(Items.IRON_LEGGINGS, InkColors.BROWN);
		put(Items.IRON_BOOTS, InkColors.BROWN);
		put(Items.IRON_AXE, InkColors.BROWN);
		put(Items.IRON_PICKAXE, InkColors.BROWN);
		put(Items.IRON_SWORD, InkColors.BROWN);
		put(Items.IRON_SHOVEL, InkColors.BROWN);
		put(Items.IRON_HOE, InkColors.BROWN);
		
		put(Items.LEATHER_HELMET, InkColors.BROWN);
		put(Items.LEATHER_CHESTPLATE, InkColors.BROWN);
		put(Items.LEATHER_LEGGINGS, InkColors.BROWN);
		put(Items.LEATHER_BOOTS, InkColors.BROWN);
		
		put(Items.STONE_AXE, InkColors.BROWN);
		put(Items.STONE_PICKAXE, InkColors.BROWN);
		put(Items.STONE_SWORD, InkColors.BROWN);
		put(Items.STONE_SHOVEL, InkColors.BROWN);
		put(Items.STONE_HOE, InkColors.BROWN);
		
		put(Items.WOODEN_AXE, InkColors.LIME);
		put(Items.WOODEN_PICKAXE, InkColors.LIME);
		put(Items.WOODEN_SWORD, InkColors.LIME);
		put(Items.WOODEN_SHOVEL, InkColors.LIME);
		put(Items.WOODEN_HOE, InkColors.LIME);
		
		put(Items.TRIDENT, InkColors.LIGHT_BLUE);
		put(Items.ECHO_SHARD, InkColors.GRAY);
		
		put(Items.WHITE_BANNER, InkColors.WHITE);
		put(Items.ORANGE_BANNER, InkColors.ORANGE);
		put(Items.MAGENTA_BANNER, InkColors.MAGENTA);
		put(Items.LIGHT_BLUE_BANNER, InkColors.LIGHT_BLUE);
		put(Items.YELLOW_BANNER, InkColors.YELLOW);
		put(Items.LIME_BANNER, InkColors.LIME);
		put(Items.PINK_BANNER, InkColors.PINK);
		put(Items.GRAY_BANNER, InkColors.GRAY);
		put(Items.LIGHT_GRAY_BANNER, InkColors.LIGHT_GRAY);
		put(Items.CYAN_BANNER, InkColors.CYAN);
		put(Items.PURPLE_BANNER, InkColors.PURPLE);
		put(Items.BLUE_BANNER, InkColors.BLUE);
		put(Items.BROWN_BANNER, InkColors.BROWN);
		put(Items.GREEN_BANNER, InkColors.GREEN);
		put(Items.RED_BANNER, InkColors.RED);
		put(Items.BLACK_BANNER, InkColors.BLACK);
		put(Items.WHEAT_SEEDS, InkColors.LIME);
		put(Items.SLIME_BALL, InkColors.GRAY);
		put(Items.SPIDER_EYE, InkColors.GRAY);
		put(Items.PRISMARINE, InkColors.LIGHT_BLUE);
		put(Items.WHEAT, InkColors.BROWN);
		put(Items.SPAWNER, InkColors.LIGHT_GRAY);
		put(Items.HAY_BLOCK, InkColors.BROWN);
		put(Items.BROWN_MUSHROOM, InkColors.BROWN);
		put(Items.SNOWBALL, InkColors.PINK);
		put(Items.BAMBOO, InkColors.GREEN);
		put(Items.RED_MUSHROOM, InkColors.BROWN);
		put(Items.MANGROVE_PROPAGULE, InkColors.GREEN);
		put(Items.CHERRY_SAPLING, InkColors.GREEN);
		put(Items.WARPED_FUNGUS, InkColors.BROWN);
		put(Items.SEAGRASS, InkColors.GREEN);
		put(Items.STONE_BRICKS, InkColors.BROWN);
		put(Items.IRON_BLOCK, InkColors.BROWN);
		put(Items.DRAGON_BREATH, InkColors.BLACK);
		put(Items.WITHER_ROSE, InkColors.BLACK);
		put(Items.CRIMSON_FUNGUS, InkColors.BROWN);
		put(Items.COD, InkColors.LIGHT_BLUE);
		put(Items.COOKED_COD, InkColors.LIGHT_BLUE);
		put(Items.SALMON, InkColors.LIGHT_BLUE);
		put(Items.COOKED_SALMON, InkColors.LIGHT_BLUE);
		put(Items.TROPICAL_FISH, InkColors.LIGHT_BLUE);
		
		put(Items.GLOW_BERRIES, InkColors.YELLOW);
		put(Items.SWEET_BERRIES, InkColors.PINK);
		put(Items.BONE, InkColors.GRAY);
		put(Items.DRAGON_HEAD, InkColors.GRAY);
		put(Items.ZOMBIE_HEAD, InkColors.GRAY);
		put(Items.SKELETON_SKULL, InkColors.GRAY);
		put(Items.WITHER_SKELETON_SKULL, InkColors.GRAY);
		put(Items.CREEPER_HEAD, InkColors.GRAY);
		put(Items.PLAYER_HEAD, InkColors.GRAY);
		put(Items.PIGLIN_HEAD, InkColors.GRAY);
	}};

	@Override
	public void registerColorMapping(Item item, InkColor color) {
		COLORS.put(item, color);
	}
	
	@Override
	public Optional<InkColor> getMapping(Item item) {
		if (COLORS.containsKey(item)) {
			return Optional.of(COLORS.get(item));
		} else {
			return Optional.empty();
		}
	}
	
	public InkColor getMapping(Item item, InkColor fallback) {
		return COLORS.getOrDefault(item, fallback);
	}
	
}
