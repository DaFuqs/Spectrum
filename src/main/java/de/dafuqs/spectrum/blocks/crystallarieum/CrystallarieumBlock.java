package de.dafuqs.spectrum.blocks.crystallarieum;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.enchanter.*;
import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CrystallarieumBlock extends InWorldInteractionBlock {
	
	public static final EnumProperty<NullableDyeColor> COLOR = EnumProperty.of("color", NullableDyeColor.class);
	
	public CrystallarieumBlock(Settings settings) {
		super(settings);
		this.setDefaultState((this.stateManager.getDefaultState()).with(COLOR, NullableDyeColor.NONE));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(COLOR);
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CrystallarieumBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(type, SpectrumBlockEntities.CRYSTALLARIEUM, world.isClient ? CrystallarieumBlockEntity::clientTick : CrystallarieumBlockEntity::serverTick);
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!world.isClient() && direction == Direction.UP) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof CrystallarieumBlockEntity crystallarieumBlockEntity) {
				crystallarieumBlockEntity.onTopBlockChange(neighborState, null);
			}
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		if (!world.isClient && entity instanceof ItemEntity itemEntity) {
			if (itemEntity.getPos().x % 0.5 != 0 && itemEntity.getPos().z % 0.5 != 0) {
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity instanceof CrystallarieumBlockEntity crystallarieumBlockEntity) {
					ItemStack stack = itemEntity.getStack();
					crystallarieumBlockEntity.acceptStack(stack, false);
				}
			}
		} else {
			super.onLandedUpon(world, state, pos, entity, fallDistance);
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			// if the structure is valid the player can put / retrieve blocks into the shrine
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof CrystallarieumBlockEntity crystallarieumBlockEntity) {
				
				ItemStack handStack = player.getStackInHand(hand);
				if (player.isSneaking() || handStack.isEmpty()) {
					// sneaking or empty hand: remove items
					for (int i = 0; i < EnchanterBlockEntity.INVENTORY_SIZE; i++) {
						if (retrieveStack(world, pos, player, hand, handStack, crystallarieumBlockEntity, i)) {
							crystallarieumBlockEntity.setOwner(player);
							break;
						}
					}
					return ActionResult.CONSUME;
				} else {
					// hand is full and inventory is empty: add
					// hand is full and inventory already contains item: exchange them
					if (handStack.getItem() instanceof InkStorageItem<?> inkStorageItem) {
						if (inkStorageItem.getDrainability().canDrain(false) && exchangeStack(world, pos, player, hand, handStack, crystallarieumBlockEntity, CrystallarieumBlockEntity.INK_STORAGE_STACK_SLOT_ID)) {
							crystallarieumBlockEntity.setOwner(player);
						}
					} else {
						if (exchangeStack(world, pos, player, hand, handStack, crystallarieumBlockEntity, CrystallarieumBlockEntity.CATALYST_SLOT_ID)) {
							crystallarieumBlockEntity.setOwner(player);
						}
					}
				}
			}
			return ActionResult.CONSUME;
		}
	}
	
	public ItemStack asStackWithColor(NullableDyeColor color) {
		ItemStack stack = asItem().getDefaultStack();
		NullableDyeColor.set(stack, color);
		return stack;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		NullableDyeColor.addTooltip(stack, world, tooltip, options);
	}
	
}
