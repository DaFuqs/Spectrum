package de.dafuqs.spectrum.blocks.ink;

import de.dafuqs.spectrum.helpers.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

public abstract class InkGeneratorBlock extends BaseInkTransferBlock {
	
	public InkGeneratorBlock(Properties settings) {
		super(settings);
	}
	
	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createInkGeneratorTicker(Level level, BlockEntityType<T> serverType, BlockEntityType<? extends InkGeneratorBlockEntity> clientType) {
		return level.isClientSide ? null : Support.checkType(serverType, clientType, InkGeneratorBlockEntity::serverTick);
	}
	
	@Override
	protected void openScreen(Level world, BlockPos pos, Player player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof InkGeneratorBlockEntity inkGeneratorBlockEntity) {
			inkGeneratorBlockEntity.setOwner(player);
			player.openMenu(inkGeneratorBlockEntity);
		}
	}
	
}
