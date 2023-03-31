package de.dafuqs.spectrum.blocks.dd_deco;

import de.dafuqs.spectrum.events.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.particle.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import net.minecraft.world.event.listener.*;
import org.jetbrains.annotations.*;

public class HummingstoneBlock extends BlockWithEntity {

    public static final float CHANCE_TO_ECHO_HUM_EVENT = 0.08F;
    public static final BooleanProperty HUMMING = BooleanProperty.of("humming");

    public HummingstoneBlock(Settings settings) {
        super(settings);
        setDefaultState(this.stateManager.getDefaultState().with(HUMMING, false));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        float r = random.nextFloat();
        if (state.get(HUMMING) && r < 0.3F || r < 0.01) {
            Direction direction = Direction.random(random);
            if (direction != Direction.DOWN) {
                BlockPos blockPos = pos.offset(direction);
                BlockState blockState = world.getBlockState(blockPos);
                if (!state.isOpaque() || !blockState.isSideSolidFullSquare(world, blockPos, direction.getOpposite())) {
                    double d = direction.getOffsetX() == 0 ? random.nextDouble() : 0.5D + (double) direction.getOffsetX() * 0.6D;
                    double e = direction.getOffsetY() == 0 ? random.nextDouble() : 0.5D + (double) direction.getOffsetY() * 0.6D;
                    double f = direction.getOffsetZ() == 0 ? random.nextDouble() : 0.5D + (double) direction.getOffsetZ() * 0.6D;
                    world.addParticle(ParticleTypes.NOTE, (double) pos.getX() + d, (double) pos.getY() + e, (double) pos.getZ() + f, 0.0D, 0.05D, 0.0D);
                }
                float pitch = 0.4F + 0.4F * pos.getX() % 8 + 0.4F * pos.getY() % 8 + 0.4F * pos.getZ() % 8;
                world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SpectrumSoundEvents.HUMMINGSTONE_HUM, SoundCategory.BLOCKS, 0.4F + random.nextFloat() * 0.1F, pitch, false);
            }
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HUMMING);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!state.get(HUMMING)) {
            startHumming(world, pos, state, player, false);
            return ActionResult.success(world.isClient);
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        if (state.get(HUMMING)) {
            stopHumming(world, pos, state);
        }
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        startHumming(world, pos, state, entity, false);
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.onLandedUpon(world, state, pos, entity, fallDistance);
        startHumming(world, pos, state, entity, false);
    }

    @Override
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        if (!world.isClient) {
            startHumming(world, hit.getBlockPos(), state, projectile.getOwner(), false);
        }
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (!world.isClient) {
            return checkType(type, SpectrumBlockEntities.HUMMINGSTONE, HummingstoneBlockEntity::serverTick);
        }
        return null;
    }

    public static void startHumming(World world, BlockPos pos, BlockState state, @Nullable Entity entity, boolean causedByOtherHum) {
        if (!(state.getBlock() instanceof HummingstoneBlock)) {
            return;
        }
    
        world.playSound(null, pos, SpectrumSoundEvents.HUMMINGSTONE_HUM, SoundCategory.BLOCKS, 0.75F, 1.0F);
        if (!state.get(HUMMING)) {
            world.setBlockState(pos, state.with(HUMMING, true));
        }
        if (!causedByOtherHum || world.random.nextFloat() < CHANCE_TO_ECHO_HUM_EVENT) {
            world.emitGameEvent(entity, SpectrumGameEvents.HUMMINGSTONE_HUMMING, pos);
        }
    }

    public static void stopHumming(World world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state.with(HUMMING, false));
        world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 0.5F, 0.5F + world.random.nextFloat() * 1.2F);
    }

    public static void onHymn(World world, BlockPos pos, @Nullable Entity entity) {
        if (!(world.getBlockState(pos).getBlock() instanceof HummingstoneBlock)) {
            return;
        }

        world.emitGameEvent(entity, SpectrumGameEvents.HUMMINGSTONE_HYMN, pos);
        world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.BLOCKS, 1.0F, 0.5F + world.random.nextFloat() * 1.2F);
        world.breakBlock(pos, false);
        dropStack(world, pos, SpectrumItems.RESONANCE_SHARD.getDefaultStack());

        if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
            SpectrumAdvancementCriteria.CREATE_HUMMINGSTONE_HYMN.trigger(serverPlayerEntity, (ServerWorld) world, pos);
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new HummingstoneBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> GameEventListener getGameEventListener(ServerWorld world, T blockEntity) {
        if (blockEntity instanceof HummingstoneBlockEntity hummingstoneBlockEntity) {
            return hummingstoneBlockEntity.listener;
        }
        return null;
    }

}
