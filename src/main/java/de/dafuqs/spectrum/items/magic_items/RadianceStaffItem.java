package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;

import java.util.*;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

public class RadianceStaffItem extends Item implements InkPowered {
	
	public static final int USE_DURATION = 12;
	public static final int REACH_STEP_DISTANCE = 4;
	public static final int MAX_REACH_STEPS = 8;
	public static final int PLACEMENT_TRIES_PER_STEP = 4;
	public static final int MIN_LIGHT_LEVEL = 10;
	
	public static final InkCost INK_COST = new InkCost(InkColors.YELLOW, 10);
	public static final ItemStack COST = new ItemStack(SpectrumItems.SHIMMERSTONE_GEM.get(), 1);
	
	public RadianceStaffItem(Properties settings) {
		super(settings);
	}
	
	public static boolean placeLight(Level world, BlockPos targetPos, ServerPlayer playerEntity) {
		if (!GenericClaimModsCompat.canPlaceBlock(world, targetPos, playerEntity)) {
			return false;
		}
		
		BlockState targetBlockState = world.getBlockState(targetPos);
		if (targetBlockState.isAir()) {
			if (playerEntity.isCreative() || InkPowered.tryDrainEnergy(playerEntity, INK_COST) || InventoryHelper.removeFromInventoryWithRemainders(playerEntity, COST)) {
				world.setBlock(targetPos, SpectrumBlocks.PERSISTENT_LIGHT.get().defaultBlockState(), 3);
				return true;
			}
		} else if (targetBlockState.is(Blocks.WATER)) {
			if (playerEntity.isCreative() || InkPowered.tryDrainEnergy(playerEntity, INK_COST) || InventoryHelper.removeFromInventoryWithRemainders(playerEntity, COST)) {
				world.setBlock(targetPos, SpectrumBlocks.PERSISTENT_LIGHT.get().defaultBlockState().setValue(WATERLOGGED, true), 3);
				return true;
			}
		}
		return false;
	}
	
	public static void playSoundAndParticles(Level world, BlockPos targetPos, ServerPlayer playerEntity, int useTimes, int iteration) {
		float pitch;
		if (useTimes % 2 == 0) { // high ding <=> deep ding
			pitch = Math.min(1.35F, 0.7F + 0.1F * useTimes);
		} else {
			pitch = Math.min(1.5F, 0.7F + 0.1F * useTimes);
		}
		PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) world, Vec3.atCenterOf(targetPos), SpectrumParticleTypes.SHIMMERSTONE_SPARKLE, 20, Vec3.ZERO, new Vec3(0.3, 0.3, 0.3));
		world.playSound(null, playerEntity.getX() + 0.5, playerEntity.getY() + 0.5, playerEntity.getZ() + 0.5, SpectrumSoundEvents.RADIANCE_STAFF_PLACE, SoundSource.PLAYERS, (float) Math.max(0.25, 1.0F - (float) iteration * 0.1F), pitch);
	}
	
	public static void playDenySound(Level world, Player playerEntity) {
		world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SpectrumSoundEvents.USE_FAIL, SoundSource.PLAYERS, 1.0F, 0.8F + playerEntity.getRandom().nextFloat() * 0.4F);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		if (InkPowered.canUseClient()) {
			tooltip.add(Component.translatable("item.spectrum.radiance_staff.tooltip.ink", INK_COST.color().getColoredInkName()));
		} else {
			tooltip.add(Component.translatable("item.spectrum.radiance_staff.tooltip"));
		}
		tooltip.add(Component.translatable("item.spectrum.radiance_staff.tooltip2"));
		tooltip.add(Component.translatable("item.spectrum.radiance_staff.tooltip3"));
	}
	
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		if (!world.isClientSide) {
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SpectrumSoundEvents.RADIANCE_STAFF_CHARGING, SoundSource.PLAYERS, 1.0F, 1.0F);
		}
		return ItemUtils.startUsingInstantly(world, user, hand);
	}
	
	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return 72000;
	}
	
	@Override
	public void onUseTick(Level world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		// trigger the items' usage action every x ticks
		if (user instanceof ServerPlayer serverPlayerEntity && user.getTicksUsingItem() > USE_DURATION && user.getTicksUsingItem() % USE_DURATION == 0) {
			usage(world, serverPlayerEntity);
		}
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		}
		
		Player player = context.getPlayer();
		if (player == null) {
			return InteractionResult.PASS;
		}
		
		BlockPos pos = context.getClickedPos();
		Direction direction = context.getClickedFace();
		
		if (!world.getBlockState(pos).is(SpectrumBlocks.PERSISTENT_LIGHT)) { // those get destroyed instead
			BlockPos targetPos = pos.relative(direction);
			if (placeLight(world, targetPos, (ServerPlayer) player)) {
				RadianceStaffItem.playSoundAndParticles(world, targetPos, (ServerPlayer) player, world.random.nextInt(5), world.random.nextInt(5));
			} else {
				RadianceStaffItem.playDenySound(world, player);
			}
			return InteractionResult.CONSUME;
		}
		
		return InteractionResult.PASS;
	}
	
	public void usage(Level world, ServerPlayer user) {
		int useTimes = (user.getTicksUsingItem() / USE_DURATION);
		int maxCheckDistance = Math.min(MAX_REACH_STEPS, useTimes);
		
		BlockPos sourcePos = user.blockPosition();
		Vec3 cameraVec = user.getViewVector(0);
		
		for (int iteration = 1; iteration < maxCheckDistance; iteration++) {
			BlockPos targetPos = sourcePos.offset(
					Mth.floor(cameraVec.x * iteration * REACH_STEP_DISTANCE),
					Mth.floor(cameraVec.y * iteration * REACH_STEP_DISTANCE),
					Mth.floor(cameraVec.z * iteration * REACH_STEP_DISTANCE)
			);
			
			boolean success = false;
			for (int tries = 0; tries < PLACEMENT_TRIES_PER_STEP; tries++) {
				targetPos = targetPos.offset(
						iteration - world.getRandom().nextInt(2 * iteration),
						iteration - world.getRandom().nextInt(2 * iteration),
						iteration - world.getRandom().nextInt(2 * iteration)
				);
				
				if (world.getBrightness(LightLayer.BLOCK, targetPos) < MIN_LIGHT_LEVEL) {
					if (placeLight(world, targetPos, user)) {
						success = true;
						playSoundAndParticles(world, targetPos, user, useTimes, iteration);
						break;
					}
				}
			}
			
			if (!success) {
				playDenySound(world, user);
			}
		}
	}
	
	@Override
	public int getEnchantmentValue() {
		return 8;
	}
	
	@Override
	public List<InkColor> getUsedColors() {
		return List.of(INK_COST.color());
	}
}
