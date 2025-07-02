package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;

public class CushionBlock extends Block {
	
	public static final MapCodec<CushionBlock> CODEC = simpleCodec(CushionBlock::new);
	
	private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 7, 16);
    public static final double SITTING_OFFSET = 5 / 16.0;
	
	public CushionBlock(Properties settings) {
        super(settings);
    }

	@Override
	public MapCodec<? extends CushionBlock> codec() {
		return CODEC;
	}
	
	public void updateEntityAfterFallOn(BlockGetter world, Entity entity) {
		if (entity.isSuppressingBounce()) {
			super.updateEntityAfterFallOn(world, entity);
        } else {
            this.bounce(entity);
        }
    }

    private void bounce(Entity entity) {
		Vec3 vec3d = entity.getDeltaMovement();
        if (vec3d.y < 0.0) {
			entity.setDeltaMovement(vec3d.x, -vec3d.y, vec3d.z);
        }
    }

    @Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        var seat = getOrCreateSeatEntity(world, pos, state);

        if (seat.getFirstPassenger() == null) {
            player.startRiding(seat, true);
			return InteractionResult.sidedSuccess(world.isClientSide());
        }
		
		return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
	public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		super.playerWillDestroy(world, pos, state, player);

        var seat = getOrCreateSeatEntity(world, pos, state);
        seat.remove(Entity.RemovalReason.DISCARDED);
        return state;
    }
	
	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		super.onRemove(state, world, pos, newState, moved);
		
		if (!moved && !state.is(newState.getBlock())) {
            var seat = getOrCreateSeatEntity(world, pos, newState);
            seat.remove(Entity.RemovalReason.DISCARDED);
        }
    }
	
	public SeatEntity getOrCreateSeatEntity(Level world, BlockPos pos, BlockState state) {
		var seats = world.getEntitiesOfClass(SeatEntity.class, new AABB(pos), seatEntity -> true);
        SeatEntity seat;

        if (seats.isEmpty()) {
            seat = new SeatEntity(world, SITTING_OFFSET);
			var seatPos = Vec3.atLowerCornerOf(pos).add(0.5, SITTING_OFFSET, 0.5);
			seat.setPos(seatPos);
            seat.setCushion(state);
			world.addFreshEntity(seat);
        }
        else {
            seat = seats.getFirst();
        }

        return seat;
    }
}
