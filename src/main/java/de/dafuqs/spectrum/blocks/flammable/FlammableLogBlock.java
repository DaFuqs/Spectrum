package de.dafuqs.spectrum.blocks.flammable;

import net.minecraft.core.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.neoforged.neoforge.common.*;
import javax.annotation.*;

import java.util.function.*;

public class FlammableLogBlock extends RotatedPillarBlock {

    protected final Supplier<? extends RotatedPillarBlock> strippedBlock;

    public FlammableLogBlock(Properties properties, Supplier<? extends RotatedPillarBlock> strippedBlock) {
        super(properties.ignitedByLava());
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
	
	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return 5;
	}
	
	@Override
	public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return 5;
	}
	
}