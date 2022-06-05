package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumItems;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LightBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
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
		return context.isHolding(SpectrumItems.RADIANCE_STAFF) ? VoxelShapes.fullCube() : VoxelShapes.empty();
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		if (world.isClient && MinecraftClient.getInstance().player.getMainHandStack().isOf(SpectrumItems.RADIANCE_STAFF)) {
			world.addParticle(SpectrumParticleTypes.SPARKLESTONE_SPARKLE_SMALL, (double) pos.getX() + 0.2 + random.nextFloat() * 0.6, (double) pos.getY() + 0.1 + random.nextFloat() * 0.6, (double) pos.getZ() + 0.2 + random.nextFloat() * 0.6, 0.0D, 0.03D, 0.0D);
		}
	}
	
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(SpectrumItems.RADIANCE_STAFF);
	}
	
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			BlockState newState = state.cycle(LEVEL_15);
			if (newState.get(LEVEL_15) == 0) { // lights with a level of 0 are absolutely
				newState = newState.cycle(LEVEL_15);
			}
			world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SpectrumSoundEvents.LIGHT_STAFF_PLACE, SoundCategory.PLAYERS, 1.0F, (float) (0.75 + 0.05 * newState.get(LEVEL_15)));
			world.setBlockState(pos, newState, Block.NOTIFY_LISTENERS);
			return ActionResult.SUCCESS;
		} else {
			return ActionResult.CONSUME;
		}
	}
	
}
