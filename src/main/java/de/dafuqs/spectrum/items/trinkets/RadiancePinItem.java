package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class RadiancePinItem extends SpectrumTrinketItem {
	
	public static final int CHECK_EVERY_X_TICKS = 20;
	public static final int MAX_LIGHT_LEVEL = 7;
	public static final BlockState LIGHT_BLOCK_STATE = SpectrumBlocks.DECAYING_LIGHT_BLOCK.getDefaultState().with(LightBlock.LEVEL_15, 15);
	public static final BlockState LIGHT_BLOCK_STATE_WATER = SpectrumBlocks.DECAYING_LIGHT_BLOCK.getDefaultState().with(LightBlock.LEVEL_15, 15).with(LightBlock.WATERLOGGED, true);
	
	public RadiancePinItem(Settings settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/radiance_pin"));
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.radiance_pin.tooltip").formatted(Formatting.GRAY));
	}
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.tick(stack, slot, entity);
		World world = entity.getWorld();
		if (!world.isClient && world.getTime() % CHECK_EVERY_X_TICKS == 0) {
			if (entity instanceof PlayerEntity playerEntity && playerEntity.isSpectator()) {
				return;
			}
			
			if (world.getLightLevel(entity.getBlockPos()) <= MAX_LIGHT_LEVEL) {
				BlockState currentState = world.getBlockState(entity.getBlockPos());
				boolean placed = false;
				if (currentState.isAir()) {
					world.setBlockState(entity.getBlockPos(), LIGHT_BLOCK_STATE, 3);
					placed = true;
				} else if (currentState.equals(Blocks.WATER.getDefaultState())) {
					world.setBlockState(entity.getBlockPos(), LIGHT_BLOCK_STATE_WATER, 3);
					placed = true;
				} else if (currentState.isOf(SpectrumBlocks.DECAYING_LIGHT_BLOCK)) {
					if (currentState.get(LightBlock.WATERLOGGED)) {
						world.setBlockState(entity.getBlockPos(), LIGHT_BLOCK_STATE_WATER, 3);
					} else {
						world.setBlockState(entity.getBlockPos(), LIGHT_BLOCK_STATE, 3);
                    }
                    placed = true;
                }
                if (placed) {
                    sendSmallLightCreatedParticle((ServerWorld) world, entity.getBlockPos());
                    world.playSound(null, entity.getX() + 0.5, entity.getY() + 0.5, entity.getZ() + 0.5, SpectrumSoundEvents.RADIANCE_STAFF_PLACE, SoundCategory.PLAYERS, 0.08F, 0.9F + world.random.nextFloat() * 0.2F);
                }
            }
        }
    }

    public static void sendSmallLightCreatedParticle(ServerWorld world, BlockPos blockPos) {
        SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity(world, Vec3d.ofCenter(blockPos),
                SpectrumParticleTypes.SHIMMERSTONE_SPARKLE,
                4,
                Vec3d.ZERO,
                new Vec3d(0.1, 0.1, 0.1));
    }

}