package de.dafuqs.pigment.blocks.mob_head;

import de.dafuqs.pigment.registries.PigmentBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

import java.util.List;

public class PigmentSkullBlockItem extends WallStandingBlockItem {

    private String artistCached;

    public PigmentSkullBlockItem(Block standingBlock, Block wallBlock, Settings settings) {
        super(standingBlock, wallBlock, settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);

        if(tooltipContext.isAdvanced()) {
            if (artistCached == null) {
                artistCached = getHeadArtist(PigmentBlocks.getSkullType(this.getBlock()));
            }
            if (!artistCached.equals("")) {
                tooltip.add(new TranslatableText("item.pigment.mob_head.tooltip.designer", artistCached));
            }
        }
    }


    // MANY thanks to the people at https://minecraft-heads.com/ !
    private String getHeadArtist(PigmentSkullBlock.Type type) {
        switch (type) {
            case FOX_ARCTIC:
            case BEE:
            case CAT:
            case CLOWNFISH:
            case FOX:
            case PANDA:
            case RAVAGER:
            case SALMON:
            case WITHER:
            case PUFFERFISH:
                return "Pandaclod";
            case GHAST:
            case CAVE_SPIDER:
            case CHICKEN:
            case COW:
            case ENDERMAN:
            case IRON_GOLEM:
            case BLAZE:
            case MAGMA_CUBE:
            case MOOSHROOM:
            case OCELOT:
            case PIG:
            case SLIME:
            case SPIDER:
            case SQUID:
            case VILLAGER:
            case ZOMBIFIED_PIGLIN:
                return "Mojang";
            case AXOLOTL_BLUE:
            case AXOLOTL_CYAN:
            case AXOLOTL_GOLD:
            case AXOLOTL_LEUCISTIC:
            case AXOLOTL_BROWN:
            case HOGLIN:
                return "ML_Monster";
            case SHULKER_BLACK:
            case SHULKER_BLUE:
            case SHULKER_BROWN:
            case SHULKER_CYAN:
            case SHULKER_PURPLE:
            case SHULKER_GRAY:
            case SHULKER_GREEN:
            case SHULKER_LIGHT_BLUE:
            case SHULKER_LIGHT_GRAY:
            case SHULKER_LIME:
            case SHULKER_MAGENTA:
            case SHULKER_ORANGE:
            case SHULKER_PINK:
            case SHULKER_RED:
            case SHULKER_WHITE:
            case SHULKER_YELLOW:
                return "ChimpD";
            case ZOMBIE_VILLAGER:
                return "Kiaria";
            case TRADER_LLAMA:
                return "miner_william_05";
            case ILLUSIONER:
            case DONKEY:
                return "titigillette";
            case PIGLIN:
                return "pianoboy913";
            case WANDERING_TRADER:
                return "BBS_01";
            case ZOGLIN:
                return "GreenRumble4454";
            case STRIDER:
                return "Deadly_Golem";
            default:
                return "";
        }
    }

}
