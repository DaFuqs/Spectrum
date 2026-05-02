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

public class TintingStationBlock extends BaseInkTransferBlock {
	
	public static final MapCodec<TintingStationBlock> CODEC = simpleCodec(TintingStationBlock::new);
	
	public TintingStationBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends TintingStationBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.tinting_station.tooltip").withStyle(ChatFormatting.GRAY));
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TintingStationBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return world.isClientSide ? null : Support.checkType(type, SpectrumBlockEntities.TINTING_STATION.get(), TintingStationBlockEntity::serverTick);
	}
	
	@Override
	protected void openScreen(Level world, BlockPos pos, Player player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof TintingStationBlockEntity tintingStationBlockEntity) {
			tintingStationBlockEntity.setOwner(player);
			player.openMenu(tintingStationBlockEntity);
		}
	}
	
}
