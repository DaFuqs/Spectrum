package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class WandLightBlock extends LightBlock {
	
	public WandLightBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if(context instanceof EntityShapeContext entityShapeContext && entityShapeContext.getEntity() != null && holdsRadianceStaff(entityShapeContext.getEntity())) {
			return VoxelShapes.fullCube();
		}
		return VoxelShapes.empty();
	}
	
	private boolean holdsRadianceStaff(@NotNull Entity entity) {
		// context.isHolding() only checks the main hand, so we use our own implementation
		for(ItemStack stack : entity.getHandItems()) {
			if(stack.getItem() instanceof RadianceStaffItem) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		if (world.isClient && holdsRadianceStaff(MinecraftClient.getInstance().player)) {
			world.addImportantParticle(SpectrumParticleTypes.SHIMMERSTONE_SPARKLE_SMALL, (double) pos.getX() + 0.2 + random.nextFloat() * 0.6, (double) pos.getY() + 0.1 + random.nextFloat() * 0.6, (double) pos.getZ() + 0.2 + random.nextFloat() * 0.6, 0.0D, 0.03D, 0.0D);
		}
	}
	
	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(SpectrumItems.RADIANCE_STAFF);
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			BlockState newState = state.cycle(LEVEL_15);
			if (newState.get(LEVEL_15) == 0) { // lights with a level of 0 are absolutely
				newState = newState.cycle(LEVEL_15);
			}
			world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SpectrumSoundEvents.RADIANCE_STAFF_PLACE, SoundCategory.PLAYERS, 1.0F, (float) (0.75 + 0.05 * newState.get(LEVEL_15)));
			world.setBlockState(pos, newState, Block.NOTIFY_LISTENERS);
			return ActionResult.SUCCESS;
		} else {
			return ActionResult.CONSUME;
		}
	}
	
}
