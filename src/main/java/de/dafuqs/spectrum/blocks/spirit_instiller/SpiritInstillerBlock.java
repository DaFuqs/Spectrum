package de.dafuqs.spectrum.blocks.spirit_instiller;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.InWorldInteractionBlock;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import de.dafuqs.spectrum.registries.SpectrumMultiblocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

public class SpiritInstillerBlock extends InWorldInteractionBlock {
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("midgame/build_spirit_instiller_structure");
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);
	
	public SpiritInstillerBlock(Settings settings) {
		super(settings);
	}
	
	public static void clearCurrentlyRenderedMultiBlock(World world) {
		if (world.isClient) {
			IMultiblock currentlyRenderedMultiBlock = PatchouliAPI.get().getCurrentMultiblock();
			if (currentlyRenderedMultiBlock != null && currentlyRenderedMultiBlock.getID().equals(SpectrumMultiblocks.SPIRIT_INSTILLER_IDENTIFIER)) {
				PatchouliAPI.get().clearMultiblock();
			}
		}
	}
	
	public static boolean verifyStructure(World world, @NotNull BlockPos blockPos, @Nullable ServerPlayerEntity serverPlayerEntity, @NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
		IMultiblock multiblock = SpectrumMultiblocks.MULTIBLOCKS.get(SpectrumMultiblocks.SPIRIT_INSTILLER_IDENTIFIER);
		
		BlockRotation lastBlockRotation = spiritInstillerBlockEntity.getMultiblockRotation();
		boolean valid = false;
		
		// try all 4 rotations
		BlockRotation checkRotation = lastBlockRotation;
		for (int i = 0; i < BlockRotation.values().length; i++) {
			valid = multiblock.validate(world, blockPos.down(1).offset(Support.directionFromRotation(checkRotation), 2), checkRotation);
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
				IMultiblock currentMultiBlock = PatchouliAPI.get().getCurrentMultiblock();
				if (currentMultiBlock == multiblock) {
					lastBlockRotation = BlockRotation.values()[(lastBlockRotation.ordinal() + 1) % BlockRotation.values().length]; // cycle rotation
					spiritInstillerBlockEntity.setMultiblockRotation(lastBlockRotation);
				} else {
					lastBlockRotation = BlockRotation.NONE;
				}
				PatchouliAPI.get().showMultiblock(multiblock, new TranslatableText("multiblock.spectrum.spirit_instiller.structure"), blockPos.down(2).offset(Support.directionFromRotation(lastBlockRotation), 2), lastBlockRotation);
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
					if(exchangeSingle(world, pos, player, hand, handStack, spiritInstillerBlockEntity)) {
						spiritInstillerBlockEntity.setOwner(player);
					}
				}
			}
			return ActionResult.CONSUME;
		}
	}
	
}
