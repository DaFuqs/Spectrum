package de.dafuqs.spectrum.blocks;

import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.neoforged.neoforge.common.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class StrippableRotatedPillarBlock extends RotatedPillarBlock {

    protected final Supplier<? extends RotatedPillarBlock> strippedBlock;

    public StrippableRotatedPillarBlock(Properties properties, Supplier<? extends RotatedPillarBlock> strippedBlock) {
        super(properties);
        this.strippedBlock = strippedBlock;
    }

    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
        ItemStack handStack = context.getItemInHand();
        if (itemAbility == ItemAbilities.AXE_STRIP && handStack.canPerformAction(itemAbility)) {
            return strippedBlock.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
        }
        return null;
    }
}