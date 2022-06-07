package de.dafuqs.spectrum.blocks.mob_head;

import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class SpectrumSkullBlockItem extends WallStandingBlockItem {
	
	protected EntityType entityType;
	protected String artistCached;
	
	public SpectrumSkullBlockItem(Block standingBlock, Block wallBlock, Settings settings, EntityType entityType) {
		super(standingBlock, wallBlock, settings);
		this.entityType = entityType;
	}
	
	public static Optional<EntityType> getEntityTypeOfSkullStack(ItemStack itemStack) {
		Item item = itemStack.getItem();
		if (item instanceof SpectrumSkullBlockItem spectrumSkullBlockItem) {
			return Optional.of(spectrumSkullBlockItem.entityType);
		}
		if (Items.CREEPER_HEAD.equals(item)) {
			return Optional.of(EntityType.CREEPER);
		} else if (Items.DRAGON_HEAD.equals(item)) {
			return Optional.of(EntityType.ENDER_DRAGON);
		} else if (Items.ZOMBIE_HEAD.equals(item)) {
			return Optional.of(EntityType.ZOMBIE);
		} else if (Items.SKELETON_SKULL.equals(item)) {
			return Optional.of(EntityType.SKELETON);
		} else if (Items.WITHER_SKELETON_SKULL.equals(item)) {
			return Optional.of(EntityType.WITHER_SKELETON);
		}
		return Optional.empty();
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		
		if (tooltipContext.isAdvanced()) {
			if (artistCached == null) {
				artistCached = getHeadArtist(SpectrumBlocks.getSkullType(this.getBlock()));
			}
			if (!artistCached.equals("")) {
				tooltip.add(new TranslatableText("item.spectrum.mob_head.tooltip.designer", artistCached));
			}
		}
	}
	
	// MANY thanks to the people at https://minecraft-heads.com/ !
	private String getHeadArtist(SpectrumSkullBlock.SpectrumSkullBlockType type) {
		return switch (type) {
			case FOX_ARCTIC, BEE, CAT, CLOWNFISH, FOX, PANDA, RAVAGER, SALMON, WITHER, PUFFERFISH -> "Pandaclod";
			case GHAST, CAVE_SPIDER, CHICKEN, COW, ENDERMAN, IRON_GOLEM, BLAZE, MAGMA_CUBE, MOOSHROOM_RED, MOOSHROOM_BROWN, OCELOT, PIG, SLIME, SPIDER, SQUID, VILLAGER, WITCH, ZOMBIFIED_PIGLIN -> "Mojang";
			case AXOLOTL_BLUE, AXOLOTL_CYAN, AXOLOTL_GOLD, AXOLOTL_LEUCISTIC, AXOLOTL_BROWN, HOGLIN -> "ML_Monster";
			case SHULKER_BLACK, SHULKER_BLUE, SHULKER_BROWN, SHULKER_CYAN, SHULKER_PURPLE, SHULKER_GRAY, SHULKER_GREEN, SHULKER_LIGHT_BLUE, SHULKER_LIGHT_GRAY, SHULKER_LIME, SHULKER_MAGENTA, SHULKER_ORANGE, SHULKER_PINK, SHULKER_RED, SHULKER_WHITE, SHULKER_YELLOW -> "ChimpD";
			case ZOMBIE_VILLAGER -> "Kiaria";
			case TRADER_LLAMA -> "miner_william_05";
			case ILLUSIONER, DONKEY -> "titigillette";
			case PIGLIN -> "pianoboy913";
			case WANDERING_TRADER -> "BBS_01";
			case ZOGLIN -> "GreenRumble4454";
			case STRIDER -> "Deadly_Golem";
			default -> "";
		};
	}
	
}
