package de.dafuqs.pigment.blocks.conditional;

import de.dafuqs.pigment.PigmentBlockCloaker;
import de.dafuqs.pigment.PigmentBlocks;
import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.Support;
import de.dafuqs.pigment.interfaces.Cloakable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

import java.util.List;

public class ColoredSaplingBlock extends SaplingBlock implements Cloakable {

    public ColoredSaplingBlock(SaplingGenerator generator, Settings settings) {
        super(generator, settings);
        setupCloak();
    }

    @Override
    public Identifier getCloakAdvancementIdentifier() {
        return new Identifier(PigmentCommon.MOD_ID, "craft_colored_sapling");
    }

    public void setCloaked() {
        // Colored Logs => Oak logs
        BlockState cloakDefaultState = Blocks.OAK_SAPLING.getDefaultState();
        for(DyeColor dyeColor : DyeColor.values()) {
            BlockState defaultState = PigmentBlocks.getColoredSaplingBlock(dyeColor).getDefaultState();
            PigmentBlockCloaker.swapModel(defaultState, cloakDefaultState); // block
            PigmentBlockCloaker.swapModel(PigmentBlocks.getColoredSaplingItem(dyeColor), Items.OAK_SAPLING); // item
        }
    }

    public void setUncloaked() {
        for(DyeColor dyeColor : DyeColor.values()) {
            Block block = PigmentBlocks.getColoredSaplingBlock(dyeColor);
            PigmentBlockCloaker.unswapAllBlockStates(block);
            PigmentBlockCloaker.unswapModel(PigmentBlocks.getColoredSaplingItem(dyeColor));
        }
    }

    @Deprecated
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return getCloakedDroppedStacks(state, builder);
    }

}
