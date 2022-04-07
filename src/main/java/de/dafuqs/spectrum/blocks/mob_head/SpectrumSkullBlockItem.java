package de.dafuqs.spectrum.blocks.mob_head;

import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

import java.util.List;

public class SpectrumSkullBlockItem extends WallStandingBlockItem {

	private String artistCached;

	public SpectrumSkullBlockItem(Block standingBlock, Block wallBlock, Settings settings) {
		super(standingBlock, wallBlock, settings);
	}

	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);

		if(tooltipContext.isAdvanced()) {
			if (artistCached == null) {
				artistCached = getHeadArtist(SpectrumBlocks.getSkullType(this.getBlock()));
			}
			if (!artistCached.equals("")) {
				tooltip.add(new TranslatableText("item.spectrum.mob_head.tooltip.designer", artistCached));
			}
		}
	}
	
	// MANY thanks to the people at https://minecraft-heads.com/ !
	private String getHeadArtist(SpectrumSkullBlock.Type type) {
		return switch (type) {
			case FOX_ARCTIC, BEE, CAT, CLOWNFISH, FOX, PANDA, RAVAGER, SALMON, WITHER, PUFFERFISH -> "Pandaclod";
			case GHAST, CAVE_SPIDER, CHICKEN, COW, ENDERMAN, IRON_GOLEM, BLAZE, MAGMA_CUBE, MOOSHROOM_RED, MOOSHROOM_BROWN, OCELOT, PIG, SLIME, SPIDER, SQUID, VILLAGER, ZOMBIFIED_PIGLIN -> "Mojang";
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
