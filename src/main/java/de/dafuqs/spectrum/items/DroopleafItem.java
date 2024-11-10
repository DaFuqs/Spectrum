package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.blocks.redstone.DroopleafBlock;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DroopleafItem extends BlockItem {
    public DroopleafItem(Block block, Item.Settings settings) {
        super(block, settings);
    }
    @Nullable
    public ItemPlacementContext getPlacementContext(ItemPlacementContext context) {
        BlockPos blockPos = context.getBlockPos();
        World world = context.getWorld();
        BlockState blockState = world.getBlockState(blockPos);
        if (!blockState.isOf(SpectrumBlocks.DROOPLEAF) && !blockState.isOf(SpectrumBlocks.DROOPLEAF_STEM)) {
            return this.getBlock().canPlaceAt(blockState,world,blockPos) ? context : null;
        } else {
            BlockPos.Mutable mutable = blockPos.mutableCopy().move(Direction.UP);
            while(true) {
                if (!world.isClient && !world.isInBuildLimit(mutable)) {
                    PlayerEntity playerEntity = context.getPlayer();
                    int j = world.getTopY();
                    if (playerEntity instanceof ServerPlayerEntity && mutable.getY() >= j) {
                        ((ServerPlayerEntity)playerEntity).sendMessageToClient(Text.translatable("build.tooHigh", j - 1).formatted(Formatting.RED), true);
                    }
                    break;
                }
                blockState = world.getBlockState(mutable);
                if (!blockState.isOf(SpectrumBlocks.DROOPLEAF) && !blockState.isOf(SpectrumBlocks.DROOPLEAF_STEM)) {
                    if (blockState.canReplace(context)) {
                        return ItemPlacementContext.offset(context, mutable, Direction.UP);
                    }
                    break;
                }
                mutable.move(Direction.UP);
            }
            return null;
        }
    }

    protected boolean checkStatePlacement() {
        return false;
    }
}
