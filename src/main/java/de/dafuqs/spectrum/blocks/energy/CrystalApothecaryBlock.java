package de.dafuqs.spectrum.blocks.energy;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrystalApothecaryBlock extends BlockWithEntity {
	
	public CrystalApothecaryBlock(Settings settings) {
		super(settings);
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CrystalApothecaryBlockEntity(pos, state);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText("block.spectrum.crystal_apothecary.tooltip").formatted(Formatting.GRAY));
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof CrystalApothecaryBlockEntity crystalApothecaryBlockEntity) {
			if (placer instanceof ServerPlayerEntity serverPlayerEntity) {
				crystalApothecaryBlockEntity.setOwner(serverPlayerEntity);
			}
			if (itemStack.hasCustomName()) {
				crystalApothecaryBlockEntity.setCustomName(itemStack.getName());
			}
			crystalApothecaryBlockEntity.harvestExistingClusters();
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof CrystalApothecaryBlockEntity crystalApothecaryBlockEntity) {
				player.openHandledScreen(crystalApothecaryBlockEntity);
			}
			return ActionResult.CONSUME;
		}
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof CrystalApothecaryBlockEntity crystalApothecaryBlockEntity) {
				ItemScatterer.spawn(world, pos, crystalApothecaryBlockEntity);
				world.updateComparators(pos, this);
			}
			
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(type, SpectrumBlockEntityRegistry.CRYSTAL_APOTHECARY, CrystalApothecaryBlockEntity::tick);
	}
	
	@Nullable
	public <T extends BlockEntity> GameEventListener getGameEventListener(World world, T blockEntity) {
		return blockEntity instanceof CrystalApothecaryBlockEntity crystalApothecaryBlockEntity ? crystalApothecaryBlockEntity.getEventListener() : null;
	}
	
}
