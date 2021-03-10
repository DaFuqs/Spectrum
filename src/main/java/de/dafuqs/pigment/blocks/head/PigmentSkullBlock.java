package de.dafuqs.pigment.blocks.head;

import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PigmentSkullBlock extends SkullBlock {

    public PigmentSkullBlock(SkullType skullType, Settings settings) {
        super(skullType, settings);
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PigmentSkullBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return null;
    }

    public static enum Type implements SkullBlock.SkullType {
        AXOLOTL_BLUE,
        AXOLOTL_BROWN,
        AXOLOTL_CYAN,
        AXOLOTL_GOLD,
        AXOLOTL_LEUCISTIC,
        BAT,
        BEE,
        BLAZE,
        CAT,
        CAVE_SPIDER,
        CHICKEN,
        CLOWNFISH,
        COW,
        DONKEY,
        DROWNED,
        ELDER_GUARDIAN,
        ENDERMAN,
        ENDERMITE,
        EVOKER,
        FOX,
        FOX_ARCTIC,
        GHAST,
        GUARDIAN,
        HOGLIN,
        HORSE,
        HUSK,
        ILLUSIONER,
        IRON_GOLEM,
        LLAMA,
        MAGMA_CUBE,
        MOOSHROOM,
        MULE,
        OCELOT,
        PANDA,
        PARROT_BLUE,
        PARROT_CYAN,
        PARROT_GRAY,
        PARROT_GREEN,
        PARROT_RED,
        PHANTOM,
        PIG,
        PIGLIN,
        POLAR_BEAR,
        PUFFERFISH,
        RABBIT,
        RAVAGER,
        SALMON,
        SHEEP_BLACK,
        SHEEP_BLUE,
        SHEEP_BROWN,
        SHEEP_CYAN,
        SHEEP_GRAY,
        SHEEP_GREEN,
        SHEEP_LIGHT_BLUE,
        SHEEP_LIGHT_GRAY,
        SHEEP_LIME,
        SHEEP_MAGENTA,
        SHEEP_ORANGE,
        SHEEP_PINK,
        SHEEP_PURPLE,
        SHEEP_RED,
        SHEEP_WHITE,
        SHEEP_YELLOW,
        SHULKER_BLACK,
        SHULKER_BLUE,
        SHULKER_BROWN,
        SHULKER_CYAN,
        SHULKER_GRAY,
        SHULKER_GREEN,
        SHULKER_LIGHT_BLUE,
        SHULKER_LIGHT_GRAY,
        SHULKER_LIME,
        SHULKER_MAGENTA,
        SHULKER_ORANGE,
        SHULKER_PINK,
        SHULKER_PURPLE,
        SHULKER_RED,
        SHULKER_WHITE,
        SHULKER_YELLOW,
        SILVERFISH,
        SLIME,
        SNOW_GOLEM,
        SPIDER,
        SQUID,
        STRAY,
        STRIDER,
        TRADER_LLAMA,
        TURTLE,
        VEX,
        VILLAGER,
        VINDICATOR,
        WANDERING_TRADER,
        WITHER,
        WOLF,
        ZOGLIN,
        ZOMBIE_VILLAGER,
        ZOMBIFIED_PIGLIN;

        private Type() {
        }
    }

}
