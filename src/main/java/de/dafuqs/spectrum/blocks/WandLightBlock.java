package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.LightBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class WandLightBlock extends LightBlock {

    public WandLightBlock(Settings settings) {
        super(settings);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return context.isHolding(SpectrumItems.LIGHT_STAFF) ? VoxelShapes.fullCube() : VoxelShapes.empty();
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        if(world.isClient && MinecraftClient.getInstance().player.getMainHandStack().isOf(SpectrumItems.LIGHT_STAFF) && random.nextFloat() > 0.3F) {
            world.addParticle(ParticleTypes.WAX_OFF, (double) pos.getX() + 0.2 + random.nextFloat() * 0.6, (double) pos.getY() + 0.2 + random.nextFloat() * 0.6, (double) pos.getZ() + 0.2 + random.nextFloat() * 0.6, 0.0D, 0.1D, 0.0D);

        }
    }

}
