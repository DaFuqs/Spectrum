package de.dafuqs.spectrum.blocks.energy;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PaintingStationBlock extends BaseInkTransferBlock {
	
	public static final MapCodec<PaintingStationBlock> CODEC = simpleCodec(PaintingStationBlock::new);
	
	public PaintingStationBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends PaintingStationBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.painting_station.tooltip").withStyle(ChatFormatting.GRAY));
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PaintingStationBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return world.isClientSide ? null : Support.checkType(type, SpectrumBlockEntities.PAINTING_STATION.get(), PaintingStationBlockEntity::serverTick);
	}
	
	@Override
	protected void openScreen(Level world, BlockPos pos, Player player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof PaintingStationBlockEntity paintingStationBlockEntity) {
			paintingStationBlockEntity.setOwner(player);
			player.openMenu(paintingStationBlockEntity);
		}
	}
	
}
