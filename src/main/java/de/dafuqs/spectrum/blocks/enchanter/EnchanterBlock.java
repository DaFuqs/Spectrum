package de.dafuqs.spectrum.blocks.enchanter;

import com.klikli_dev.modonomicon.api.multiblock.*;
import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import javax.annotation.*;

import java.util.*;

public class EnchanterBlock extends InWorldInteractionBlock {
	
	public static final MapCodec<EnchanterBlock> CODEC = simpleCodec(EnchanterBlock::new);
	
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("midgame/build_enchanting_structure");
	
	public static final List<Vec3i> UPGRADE_BLOCK_OFFSETS = List.of(
			new Vec3i(3, 0, 3),
			new Vec3i(-3, 0, 3),
			new Vec3i(3, 0, -3),
			new Vec3i(-3, 0, -3)
	);
	
	public EnchanterBlock(Properties settings) {
		super(settings);
		
		Upgradeable.registerUpgradePosOffsets(UPGRADE_BLOCK_OFFSETS);
	}
	
	@Override
	public MapCodec<? extends EnchanterBlock> codec() {
		return CODEC;
	}
	
	public static void clearCurrentlyRenderedMultiBlock(Level world) {
		if (world.isClientSide) {
			ModonomiconHelper.clearRenderedMultiblock(SpectrumMultiblocks.get(SpectrumMultiblocks.ENCHANTER));
		}
	}
	
	public static boolean verifyStructure(Level world, BlockPos blockPos, @Nullable ServerPlayer serverPlayerEntity) {
		Multiblock multiblock = SpectrumMultiblocks.get(SpectrumMultiblocks.ENCHANTER);
		boolean valid = multiblock.validate(world, blockPos.below(3), Rotation.NONE);
		
		if (valid) {
			if (serverPlayerEntity != null) {
				SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger(serverPlayerEntity, multiblock);
			}
		} else if (world.isClientSide) {
			ModonomiconHelper.renderMultiblock(SpectrumMultiblocks.get(SpectrumMultiblocks.ENCHANTER), SpectrumMultiblocks.ENCHANTER_TEXT, blockPos.below(4), Rotation.NONE);
		}
		
		return valid;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EnchanterBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, SpectrumBlockEntities.ENCHANTER.get(), world.isClientSide ? EnchanterBlockEntity::clientTick : EnchanterBlockEntity::serverTick);
	}
	
	@Override
	public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
		if (world.isClientSide()) {
			clearCurrentlyRenderedMultiBlock((Level) world);
		}
	}
	
	@Override
	public ItemInteractionResult useItemOn(ItemStack handStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide) {
			verifyStructure(world, pos, null);
			return ItemInteractionResult.SUCCESS;
		} else {
			if (verifyStructure(world, pos, (ServerPlayer) player)) {
				
				// if the structure is valid the player can put / retrieve blocks into the shrine
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity instanceof EnchanterBlockEntity enchanterBlockEntity) {
					
					if (player.isShiftKeyDown() || handStack.isEmpty()) {
						// sneaking or empty hand: remove items
						for (int i = 0; i < EnchanterBlockEntity.INVENTORY_SIZE; i++) {
							if (retrieveStack(world, pos, player, hand, handStack, enchanterBlockEntity, i)) {
								enchanterBlockEntity.setItemFacingDirection(player.getDirection());
								enchanterBlockEntity.setOwner(player);
								break;
							}
						}
						return ItemInteractionResult.CONSUME;
					} else {
						// hand is full and inventory is empty: add
						// hand is full and inventory already contains item: exchange them
						int inputInventorySlotIndex = handStack.getItem() instanceof ExperienceStorageItem ? enchanterBlockEntity.getItem(1).isEmpty() ? 1 : 0 : 0;
						if (exchangeStack(world, pos, player, hand, handStack, enchanterBlockEntity, inputInventorySlotIndex)) {
							enchanterBlockEntity.setItemFacingDirection(player.getDirection());
							enchanterBlockEntity.setOwner(player);
						}
					}
				}
			}
			return ItemInteractionResult.CONSUME;
		}
	}
	
}
