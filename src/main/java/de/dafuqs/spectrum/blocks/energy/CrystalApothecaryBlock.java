package de.dafuqs.spectrum.blocks.energy;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class CrystalApothecaryBlock extends BaseEntityBlock {
	
	public static final MapCodec<CrystalApothecaryBlock> CODEC = simpleCodec(CrystalApothecaryBlock::new);
	
	public CrystalApothecaryBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends CrystalApothecaryBlock> codec() {
		return CODEC;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CrystalApothecaryBlockEntity(pos, state);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.crystal_apothecary.tooltip").withStyle(ChatFormatting.GRAY));
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof CrystalApothecaryBlockEntity crystalApothecaryBlockEntity) {
			if (placer instanceof ServerPlayer serverPlayerEntity) {
				crystalApothecaryBlockEntity.setOwner(serverPlayerEntity);
			}
			crystalApothecaryBlockEntity.harvestExistingClusters();
		}
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CrystalApothecaryBlockEntity crystalApothecaryBlockEntity) {
                player.openMenu(crystalApothecaryBlockEntity);
            }
            return InteractionResult.CONSUME;
        }
	}
	
	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}
	
	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
	}
	
	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		Containers.dropContentsOnDestroy(state, newState, world, pos);
		super.onRemove(state, world, pos, newState, moved);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}

	@Override
	public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, SpectrumBlockEntities.CRYSTAL_APOTHECARY.get(), CrystalApothecaryBlockEntity::tick);
	}

	@Override
	public <T extends BlockEntity> @Nullable GameEventListener getListener(ServerLevel world, T blockEntity) {
		return blockEntity instanceof CrystalApothecaryBlockEntity crystalApothecaryBlockEntity ? crystalApothecaryBlockEntity.getEventListener() : null;
	}
	
}
