package de.dafuqs.spectrum.blocks.redstone;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class PlayerDetectorBlock extends DetectorBlock implements BlockEntityProvider {
	
	public PlayerDetectorBlock(Settings settings) {
		super(settings);
	}
	
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient && placer instanceof PlayerEntity) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof PlayerDetectorBlockEntity) {
				((PlayerDetectorBlockEntity) blockEntity).setOwner((PlayerEntity) placer);
			}
		}
	}
	
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			if (player.isSneaking()) {
				
				String ownerName = getOwnerName(world, pos);
				if (ownerName != null && !ownerName.equals("")) {
					player.sendMessage(new TranslatableText("block.spectrum.player_detector").append(new TranslatableText("container.spectrum.owned_by_player", ownerName)), false);
				}
				return ActionResult.CONSUME;
			} else {
				return super.onUse(state, world, pos, player, hand, hit);
			}
		}
	}
	
	protected void updateState(BlockState state, World world, BlockPos pos) {
		List<PlayerEntity> players = world.getEntitiesByType(EntityType.PLAYER, getBoxWithRadius(pos, 10), LivingEntity::isAlive);
		
		int power = 0;
		
		if (players.size() > 0) {
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
