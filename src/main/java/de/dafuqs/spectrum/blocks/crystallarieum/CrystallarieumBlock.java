package de.dafuqs.spectrum.blocks.crystallarieum;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CrystallarieumBlock extends InWorldInteractionBlock implements SlotBackgroundEffectProvider {
	
	public static final MapCodec<CrystallarieumBlock> CODEC = simpleCodec(CrystallarieumBlock::new);
	
	public CrystallarieumBlock(Properties settings) {
		super(settings);
		this.registerDefaultState((this.stateDefinition.any()));
	}
	
	@Override
	public MapCodec<? extends CrystallarieumBlock> codec() {
		return CODEC;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CrystallarieumBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, SpectrumBlockEntities.CRYSTALLARIEUM, world.isClientSide ? CrystallarieumBlockEntity::clientTick : CrystallarieumBlockEntity::serverTick);
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (!world.isClientSide() && direction == Direction.UP) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof CrystallarieumBlockEntity crystallarieumBlockEntity) {
				crystallarieumBlockEntity.onTopBlockChange(neighborState, null);
			}
		}
		return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		if (!world.isClientSide && entity instanceof ItemEntity itemEntity) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof CrystallarieumBlockEntity crystallarieumBlockEntity) {
				ItemStack stack = itemEntity.getItem();
				crystallarieumBlockEntity.acceptStack(stack, false, itemEntity.getOwner() != null ? itemEntity.getOwner().getUUID() : null);
			}
		} else {
			super.fallOn(world, state, pos, entity, fallDistance);
		}
	}
	
	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!world.isClientSide) {
			// if the structure is valid the player can put / retrieve blocks into the shrine
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof CrystallarieumBlockEntity crystallarieumBlockEntity) {
				
				if (player.isShiftKeyDown() || stack.isEmpty()) {
					// sneaking or empty hand: remove items
					if (retrieveStack(world, pos, player, hand, stack, crystallarieumBlockEntity, 1) || retrieveStack(world, pos, player, hand, stack, crystallarieumBlockEntity, 0)) {
						crystallarieumBlockEntity.inventoryChanged();
						crystallarieumBlockEntity.setOwner(player);
					}
					return ItemInteractionResult.CONSUME;
				} else {
					if (ContainerItemContext.forPlayerInteraction(player, hand).find(FluidStorage.ITEM) != null) {
						FluidStorageUtil.interactWithFluidStorage(crystallarieumBlockEntity.fluidStorage, player, hand);
					}
					
					// hand is full and inventory is empty: add
					// hand is full and inventory already contains item: exchange them
					else if (stack.getItem() instanceof InkStorageItem<?> inkStorageItem) {
						if (inkStorageItem.getDrainability().canDrain(false) && exchangeStack(world, pos, player, hand, stack, crystallarieumBlockEntity, CrystallarieumBlockEntity.INK_STORAGE_STACK_SLOT_ID)) {
							crystallarieumBlockEntity.inventoryChanged();
							crystallarieumBlockEntity.setOwner(player);
						}
					} else {
						if (exchangeStack(world, pos, player, hand, stack, crystallarieumBlockEntity, CrystallarieumBlockEntity.CATALYST_SLOT_ID)) {
							crystallarieumBlockEntity.inventoryChanged();
							crystallarieumBlockEntity.setOwner(player);
						}
					}
				}
			}
			return ItemInteractionResult.CONSUME;
		}
		return ItemInteractionResult.SUCCESS;
	}
	
	public ItemStack asStackWithColor(InkColor color) {
		ItemStack stack = asItem().getDefaultInstance();
		stack.set(SpectrumDataComponentTypes.INK_COLOR, color);
		return stack;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		var color = stack.get(SpectrumDataComponentTypes.INK_COLOR);
		if (color != null)
			tooltip.add(color.getColoredInkName());
	}
	
	@Override
	public SlotEffect backgroundType(@Nullable Player player, ItemStack stack) {
		var color = stack.get(SpectrumDataComponentTypes.INK_COLOR);
		return color != null ? SlotEffect.BORDER_FADE : SlotEffect.NONE;
	}
	
	@Override
	public int getBackgroundColor(@Nullable Player player, ItemStack stack, float tickDelta) {
		var color = stack.getOrDefault(SpectrumDataComponentTypes.INK_COLOR, InkColors.WHITE);
		return color.getColorInt();
	}
}
