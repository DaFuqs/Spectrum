package de.dafuqs.spectrum.blocks.ink.gen;

import de.dafuqs.spectrum.blocks.ink.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import org.jetbrains.annotations.*;

public abstract class InkGeneratorBlock extends BaseInkBlock {
	
	public InkGeneratorBlock(Properties settings) {
		super(settings);
	}
	
	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createInkGeneratorTicker(Level level, BlockEntityType<T> serverType, BlockEntityType<? extends InkGeneratorBlockEntity> clientType) {
		return level.isClientSide ? null : Support.checkType(serverType, clientType, InkGeneratorBlockEntity::serverTick);
	}
	
}
