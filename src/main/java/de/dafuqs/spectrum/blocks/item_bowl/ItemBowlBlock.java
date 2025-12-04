package de.dafuqs.spectrum.blocks.item_bowl;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.enchanter.*;
import de.dafuqs.spectrum.blocks.spirit_instiller.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemBowlBlock extends InWorldInteractionBlock {
	
	public static final MapCodec<ItemBowlBlock> CODEC = simpleCodec(ItemBowlBlock::new);
	
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 11.0D, 16.0D);

	// Positions to check on place / destroy to upgrade those blocks upgrade counts
	private final List<Vec3i> possibleEnchanterOffsets = new ArrayList<>() {{
		add(new Vec3i(5, 0, 3));
		add(new Vec3i(-5, 0, -3));
		add(new Vec3i(-3, 0, 5));
		add(new Vec3i(-3, 0, -5));
		add(new Vec3i(3, 0, 5));
		add(new Vec3i(3, 0, -5));
		add(new Vec3i(5, 0, 3));
		add(new Vec3i(5, 0, -3));
	}};

	// Positions to check on place / destroy to upgrade those blocks upgrade counts
	private final List<Vec3i> possibleSpiritInstillerOffsets = new ArrayList<>() {{
		add(new Vec3i(0, -1, 2));
		add(new Vec3i(0, -1, -2));
		add(new Vec3i(2, -1, 0));
		add(new Vec3i(-2, -1, 0));
	}};
	
	public ItemBowlBlock(Properties settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends ItemBowlBlock> codec() {
		return CODEC;
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ItemBowlBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return world.isClientSide ? createTickerHelper(type, SpectrumBlockEntities.ITEM_BOWL, ItemBowlBlockEntity::clientTick) : null;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		Containers.dropContentsOnDestroy(state, newState, world, pos);
		super.onRemove(state, world, pos, newState, moved);
		updateConnectedMultiBlocks(world, pos);
	}
	
	/**
	 * When placed or removed the item bowl searches for a valid block entity and triggers it to update its current recipe
	 */
	private void updateConnectedMultiBlocks(@NotNull Level world, @NotNull BlockPos pos) {
		for (Vec3i possibleUpgradeBlockOffset : possibleEnchanterOffsets) {
			BlockPos currentPos = pos.offset(possibleUpgradeBlockOffset);
			BlockEntity blockEntity = world.getBlockEntity(currentPos);
			if (blockEntity instanceof EnchanterBlockEntity enchanterBlockEntity) {
				enchanterBlockEntity.inventoryChanged();
				break;
			}
		}
		
		for (Vec3i possibleUpgradeBlockOffset : possibleSpiritInstillerOffsets) {
			BlockPos currentPos = pos.offset(possibleUpgradeBlockOffset);
			BlockEntity blockEntity = world.getBlockEntity(currentPos);
			if (blockEntity instanceof SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
				spiritInstillerBlockEntity.inventoryChanged();
				break;
			}
		}
	}
	
	@Override
	public ItemInteractionResult useItemOn(ItemStack handStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide) {
			return ItemInteractionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
				if (exchangeStack(world, pos, player, hand, handStack, itemBowlBlockEntity)) {
					updateConnectedMultiBlocks(world, pos);
				}
			}
			return ItemInteractionResult.CONSUME;
		}
	}
	
}
