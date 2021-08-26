package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.blocks.pedestal.PedestalBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleSpawnerBlock extends Block implements RedstonePoweredBlock {

    protected static final VoxelShape SHAPE = Block.createCuboidShape(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D);
    public static final EnumProperty<PedestalBlock.RedstonePowerState> STATE = EnumProperty.of("state", RedstonePowerState.class);

    ParticleEffect particleEffect;
    float particlesPerTick; // >1 = every xth tick
    double sourcePositionOffsetX;
    double sourcePositionOffsetY;
    double sourcePositionOffsetZ;
    double randomPositionOffsetX;
    double randomPositionOffsetY;
    double randomPositionOffsetZ;
    double sourceVelocityX;
    double sourceVelocityY;
    double sourceVelocityZ;
    double randomVelocityX;
    double randomVelocityY;
    double randomVelocityZ;

    public ParticleSpawnerBlock(FabricBlockSettings of) {
        super(of);
        setDefaultState(getStateManager().getDefaultState().with(STATE, RedstonePowerState.UNPOWERED));

        List<ParticleEffect> availableParticleEffects = new ArrayList<>();
        availableParticleEffects.add(ParticleTypes.FLAME);
        availableParticleEffects.add(ParticleTypes.BUBBLE);

        this.particleEffect = ParticleTypes.FLAME;
        this.particlesPerTick = 1;
        this.sourcePositionOffsetX = 0;
        this.sourcePositionOffsetY = 3;
        this.sourcePositionOffsetZ = 0;
        this.randomPositionOffsetX = 1;
        this.randomPositionOffsetY = 0;
        this.randomPositionOffsetZ = 1;
        this.sourceVelocityX = 0;
        this.sourceVelocityY = 0.1;
        this.sourceVelocityZ = 0;
        this.randomVelocityX = 0.2;
        this.randomVelocityY = 0.1;
        this.randomVelocityZ = 0;
    }

    @Deprecated
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(STATE);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (!world.isClient) {
            if(this.isGettingPowered(world, pos)) {
                this.power(world, pos);
            } else {
                this.unPower(world, pos);
            }
        }
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState placementState = this.getDefaultState();

        if(ctx.getWorld().getReceivedRedstonePower(ctx.getBlockPos()) > 0) {
            placementState = placementState.with(STATE, RedstonePowerState.POWERED);
        }

        return placementState;
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if(state.get(ParticleSpawnerBlock.STATE).equals(PedestalBlock.RedstonePowerState.POWERED)) {
            float particlesToSpawn = this.particlesPerTick;
            while (particlesToSpawn > 1 || random.nextFloat() < particlesToSpawn) {
                spawnParticle(world, pos, random);
                particlesToSpawn--;
            }
        }
    }

    private void spawnParticle(World world, BlockPos pos, Random random) {
            double randomOffsetX = randomPositionOffsetX == 0 ? 0 : randomPositionOffsetX - random.nextDouble() * randomPositionOffsetX * 2.0D;
            double randomOffsetY = randomPositionOffsetY == 0 ? 0 : randomPositionOffsetY - random.nextDouble() * randomPositionOffsetY * 2.0D;
            double randomOffsetZ = randomPositionOffsetZ == 0 ? 0 : randomPositionOffsetZ - random.nextDouble() * randomPositionOffsetZ * 2.0D;

            double randomVelocityX = this.randomVelocityX == 0 ? 0 : this.randomVelocityX * random.nextDouble();
            double randomVelocityY = this.randomVelocityY == 0 ? 0 : this.randomVelocityY * random.nextDouble();
            double randomVelocityZ = this.randomVelocityZ == 0 ? 0 : this.randomVelocityZ * random.nextDouble();

            world.addParticle(particleEffect,
                    (double) pos.getX() + sourcePositionOffsetX + randomOffsetX,
                    (double) pos.getY() + sourcePositionOffsetY + randomOffsetY,
                    (double) pos.getZ() + sourcePositionOffsetZ + randomOffsetZ,
                    sourceVelocityX + randomVelocityX,
                    sourceVelocityY + randomVelocityY,
                    sourceVelocityZ + randomVelocityZ);
    }


}
