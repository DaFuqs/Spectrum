package de.dafuqs.spectrum.blocks.fluid;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.core.cauldron.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

import java.util.function.*;

public class SpectrumCauldronBlock extends AbstractCauldronBlock {

	protected final Supplier<SpectrumFluid> fluidType;
	
    public SpectrumCauldronBlock(Supplier<SpectrumFluid> fluidType, CauldronInteraction.InteractionMap interactions, BlockBehaviour.Properties properties) {
        super(properties, interactions);
		this.fluidType = fluidType;
    }
	
	@Override
	protected double getContentHeight(BlockState p_153500_) {
		return 0.9375;
	}
	
	@Override
	public boolean isFull(BlockState p_153511_) {
		return true;
	}
	
	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (this.isEntityInsideContent(state, pos, entity)) {
			this.getFluidType().onEntityCollision(state, level, pos, entity);
		}
	}
	
	protected SpectrumFluid getFluidType() {
		return fluidType.get();
	}
	
	@Override
	protected MapCodec<? extends AbstractCauldronBlock> codec() {
		return null;
	}
	
}