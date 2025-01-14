package de.dafuqs.spectrum.blocks.redstone;

import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlayerDetectorBlock extends DetectorBlock implements BlockEntityProvider {
	
	public PlayerDetectorBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient && placer instanceof PlayerEntity) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof PlayerDetectorBlockEntity) {
				((PlayerDetectorBlockEntity) blockEntity).setOwner((PlayerEntity) placer);
			}
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			if (player.isSneaking()) {
				
				String ownerName = getOwnerName(world, pos);
				if (ownerName != null && !ownerName.isBlank()) {
					player.sendMessage(Text.translatable("block.spectrum.player_detector.owner", ownerName), true);
				}
				return ActionResult.CONSUME;
			} else {
				return super.onUse(state, world, pos, player, hand, hit);
			}
		}
	}
	
	@Override
	protected void updateState(BlockState state, World world, BlockPos pos) {
		List<PlayerEntity> players = world.getEntitiesByType(EntityType.PLAYER, getDetectionBox(pos), player -> player.isAlive() && !player.isSpectator());
		
		int power = 0;
		
		if (!players.isEmpty()) {
			power = 8;
			UUID ownerUUID = getOwnerUUID(world, pos);
			if (ownerUUID != null) {
				for (PlayerEntity playerEntity : players) {
					if (playerEntity.getUuid().equals(ownerUUID)) {
						power = 15;
						break;
					}
				}
			}
		}
		
		power = state.get(INVERTED) ? 15 - power : power;
		if (state.get(POWER) != power) {
			world.setBlockState(pos, state.with(POWER, power), 3);
		}
	}
	
	@Override
	int getUpdateFrequencyTicks() {
		return 20;
	}
	
	private UUID getOwnerUUID(World world, BlockPos blockPos) {
		BlockEntity blockEntity = world.getBlockEntity(blockPos);
		if (blockEntity instanceof PlayerDetectorBlockEntity) {
			return ((PlayerDetectorBlockEntity) blockEntity).getOwnerUUID();
		}
		return null;
	}
	
	private String getOwnerName(World world, BlockPos blockPos) {
		BlockEntity blockEntity = world.getBlockEntity(blockPos);
		if (blockEntity instanceof PlayerDetectorBlockEntity) {
			return ((PlayerDetectorBlockEntity) blockEntity).getOwnerName();
		}
		return null;
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PlayerDetectorBlockEntity(pos, state);
	}
}
