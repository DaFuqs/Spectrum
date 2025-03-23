package de.dafuqs.spectrum.blocks.spirit_instiller;

import com.klikli_dev.modonomicon.api.multiblock.*;
import com.klikli_dev.modonomicon.client.render.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class SpiritInstillerBlock extends InWorldInteractionBlock {
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);
	
	public SpiritInstillerBlock(Settings settings) {
		super(settings);
	}
	
	public static void clearCurrentlyRenderedMultiBlock(World world) {
		if (world.isClient) {
			ModonomiconHelper.clearRenderedMultiblock(SpectrumMultiblocks.get(SpectrumMultiblocks.SPIRIT_INSTILLER));
		}
	}
	
	public static boolean verifyStructure(World world, @NotNull BlockPos blockPos, @Nullable ServerPlayerEntity serverPlayerEntity, @NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
		Multiblock multiblock = SpectrumMultiblocks.get(SpectrumMultiblocks.SPIRIT_INSTILLER);
		
		BlockRotation lastBlockRotation = spiritInstillerBlockEntity.getMultiblockRotation();
		boolean valid = false;
		
		// try all 4 rotations
		int offset = -4;
		BlockRotation checkRotation = lastBlockRotation;
		for (int i = 0; i < BlockRotation.values().length; i++) {
			valid = multiblock.validate(world, blockPos.down(1).offset(Support.directionFromRotation(checkRotation), offset), checkRotation);
			if (valid) {
				if (i != 0) {
					spiritInstillerBlockEntity.setMultiblockRotation(checkRotation);
				}
				break;
			} else {
				checkRotation = BlockRotation.values()[(checkRotation.ordinal() + 1) % BlockRotation.values().length];
			}
		}
		
		if (valid) {
			if (serverPlayerEntity != null) {
				SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger(serverPlayerEntity, multiblock);
			}
		} else {
			if (world.isClient) {
				Multiblock currentMultiBlock = MultiblockPreviewRenderer.getMultiblock();
				if (currentMultiBlock == multiblock) {
					lastBlockRotation = BlockRotation.values()[(MultiblockPreviewRenderer.getFacingRotation().ordinal() + 1) % BlockRotation.values().length]; // cycle rotation
					spiritInstillerBlockEntity.setMultiblockRotation(lastBlockRotation);
				}
				ModonomiconHelper.renderMultiblock(SpectrumMultiblocks.get(SpectrumMultiblocks.SPIRIT_INSTILLER), SpectrumMultiblocks.SPIRIT_INSTILLER_TEXT, blockPos.down(2).offset(Support.directionFromRotation(lastBlockRotation), offset), lastBlockRotation);
			} else {
				scatterContents(world, blockPos);
			}
		}
		
		return valid;
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SpiritInstillerBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (world.isClient) {
			return checkType(type, SpectrumBlockEntities.SPIRIT_INSTILLER, SpiritInstillerBlockEntity::clientTick);
		} else {
			return checkType(type, SpectrumBlockEntities.SPIRIT_INSTILLER, SpiritInstillerBlockEntity::serverTick);
		}
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		if (world.isClient()) {
			clearCurrentlyRenderedMultiBlock((World) world);
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (world.isClient) {
			if (blockEntity instanceof SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
				verifyStructure(world, pos, null, spiritInstillerBlockEntity);
			}
			return ActionResult.SUCCESS;
		} else {
			if (blockEntity instanceof SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
				if (verifyStructure(world, pos, (ServerPlayerEntity) player, spiritInstillerBlockEntity)) {
					ItemStack handStack = player.getStackInHand(hand);
					if (exchangeStack(world, pos, player, hand, handStack, spiritInstillerBlockEntity)) {
						spiritInstillerBlockEntity.setOwner(player);
						spiritInstillerBlockEntity.inventoryChanged();
					}
				}
			}
			return ActionResult.CONSUME;
		}
	}
	
}
