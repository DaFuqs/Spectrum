package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.*;
import top.theillusivec4.curios.api.*;

import java.util.*;

public class RadiancePinItem extends SpectrumCurioItem {
	
	public static final int CHECK_EVERY_X_TICKS = 20;
	public static final int MAX_LIGHT_LEVEL = 7;
	
	public RadiancePinItem(Properties settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/radiance_pin"));
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		
		if(context.registries() != null && SpectrumEnchantmentHelper.hasEnchantment(context.registries(), Enchantments.INFINITY, stack)) {
			tooltip.add(Component.translatable("item.spectrum.radiance_pin.tooltip.persistent").withStyle(ChatFormatting.GRAY));
		} else {
			tooltip.add(Component.translatable("item.spectrum.radiance_pin.tooltip").withStyle(ChatFormatting.GRAY));
		}
		
	}
	
	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		super.curioTick(slotContext, stack);
		
		LivingEntity entity = slotContext.entity();
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
				boolean persistent = SpectrumEnchantmentHelper.hasEnchantment(world.registryAccess(), Enchantments.INFINITY, stack);

				if (currentState.isAir() || currentState.is(SpectrumBlocks.TRANSIENT_LIGHT)) {
					BlockState placementState = persistent ? SpectrumBlocks.PERSISTENT_LIGHT.get().defaultBlockState() : SpectrumBlocks.TRANSIENT_LIGHT.get().defaultBlockState();
					world.setBlockAndUpdate(pos, placementState);
					sendSmallLightCreatedParticle((ServerLevel) world, pos);
					world.playSound(null, entity.getX() + 0.5, entity.getY() + 0.5, entity.getZ() + 0.5, SpectrumSoundEvents.RADIANCE_STAFF_PLACE, SoundSource.PLAYERS, 0.08F, 0.9F + world.getRandom().nextFloat() * 0.2F);
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
	
	@Override
	public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
		return super.supportsEnchantment(stack, enchantment) || enchantment.is(Enchantments.INFINITY);
	}
	
}
