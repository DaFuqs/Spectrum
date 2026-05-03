package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;

import java.util.*;

public class RadiancePinItem extends SpectrumTrinketItem {
	
	public static final int CHECK_EVERY_X_TICKS = 20;
	public static final int MAX_LIGHT_LEVEL = 7;
	public static final BlockState LIGHT_BLOCK_STATE = SpectrumBlocks.TRANSIENT_LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, 15);
	public static final BlockState LIGHT_BLOCK_STATE_WATER = SpectrumBlocks.TRANSIENT_LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, 15).setValue(LightBlock.WATERLOGGED, true);
	
	public RadiancePinItem(Properties settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/radiance_pin"));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.radiance_pin.tooltip").withStyle(ChatFormatting.GRAY));
	}
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.tick(stack, slot, entity);
		Level world = entity.level();
		if (!world.isClientSide() && world.getGameTime() % CHECK_EVERY_X_TICKS == 0) {
			if (entity instanceof Player playerEntity && playerEntity.isSpectator()) {
				return;
			}
			BlockPos pos = entity.blockPosition();
			if (!GenericClaimModsCompat.canPlaceBlock(world, pos, entity)) {
				return;
			}
			
			if (!world.isOutsideBuildHeight(pos) && world.getMaxLocalRawBrightness(pos) <= MAX_LIGHT_LEVEL) {
				BlockState currentState = world.getBlockState(pos);
				boolean placed = false;
				if (currentState.isAir()) {
					world.setBlock(pos, LIGHT_BLOCK_STATE, 3);
					placed = true;
				} else if (currentState.equals(Blocks.WATER.defaultBlockState())) {
					world.setBlock(pos, LIGHT_BLOCK_STATE_WATER, 3);
					placed = true;
				} else if (currentState.is(SpectrumBlocks.TRANSIENT_LIGHT)) {
					if (currentState.getValue(LightBlock.WATERLOGGED)) {
						world.setBlock(pos, LIGHT_BLOCK_STATE_WATER, 3);
					} else {
						world.setBlock(pos, LIGHT_BLOCK_STATE, 3);
                    }
                    placed = true;
                }
                if (placed) {
					sendSmallLightCreatedParticle((ServerLevel) world, pos);
					world.playSound(null, entity.getX() + 0.5, entity.getY() + 0.5, entity.getZ() + 0.5, SpectrumSoundEvents.RADIANCE_STAFF_PLACE, SoundSource.PLAYERS, 0.08F, 0.9F + world.random.nextFloat() * 0.2F);
                }
            }
        }
    }
	
	public static void sendSmallLightCreatedParticle(ServerLevel world, BlockPos blockPos) {
		PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity(world, Vec3.atCenterOf(blockPos),
                SpectrumParticleTypes.SHIMMERSTONE_SPARKLE,
                4,
				Vec3.ZERO,
				new Vec3(0.1, 0.1, 0.1));
    }

}
