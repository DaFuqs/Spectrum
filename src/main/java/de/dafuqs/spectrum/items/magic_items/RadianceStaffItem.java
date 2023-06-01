package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

import static net.minecraft.state.property.Properties.*;

public class RadianceStaffItem extends Item implements InkPowered {
	
	public static final int USE_DURATION = 12;
	public static final int REACH_STEP_DISTANCE = 4;
	public static final int MAX_REACH_STEPS = 8;
	public static final int MIN_LIGHT_LEVEL = 10;
	
	public static final ItemStack COST = new ItemStack(SpectrumItems.SHIMMERSTONE_GEM, 1);
	
	public RadianceStaffItem(Settings settings) {
		super(settings);
	}
	
	public static boolean placeLight(World world, BlockPos targetPos, ServerPlayerEntity playerEntity) {
		if (GenericClaimModsCompat.isProtected(world, targetPos, playerEntity)) {
			return false;
		}
		
		BlockState targetBlockState = world.getBlockState(targetPos);
		if (targetBlockState.isAir()) {
			if (playerEntity.isCreative() || InkPowered.tryDrainEnergy(playerEntity, InkColors.YELLOW, 10L) || InventoryHelper.removeFromInventoryWithRemainders(playerEntity, COST)) {
				world.setBlockState(targetPos, SpectrumBlocks.WAND_LIGHT_BLOCK.getDefaultState(), 3);
				return true;
			}
		} else if (targetBlockState.isOf(Blocks.WATER)) {
			if (playerEntity.isCreative() || InkPowered.tryDrainEnergy(playerEntity, InkColors.YELLOW, 10L) || InventoryHelper.removeFromInventoryWithRemainders(playerEntity, COST)) {
				world.setBlockState(targetPos, SpectrumBlocks.WAND_LIGHT_BLOCK.getDefaultState().with(WATERLOGGED, true), 3);
				return true;
			}
		}
		return false;
	}
	
	public static void playSoundAndParticles(World world, BlockPos targetPos, ServerPlayerEntity playerEntity, int useTimes, int iteration) {
        float pitch;
        if (useTimes % 2 == 0) { // high ding <=> deep ding
            pitch = Math.min(1.35F, 0.7F + 0.1F * useTimes);
        } else {
            pitch = Math.min(1.5F, 0.7F + 0.1F * useTimes);
        }
        SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) world, Vec3d.ofCenter(targetPos),
                SpectrumParticleTypes.SHIMMERSTONE_SPARKLE, 20, Vec3d.ZERO, new Vec3d(0.3, 0.3, 0.3));

        world.playSound(null, playerEntity.getX() + 0.5, playerEntity.getY() + 0.5, playerEntity.getZ() + 0.5, SpectrumSoundEvents.RADIANCE_STAFF_PLACE, SoundCategory.PLAYERS, (float) Math.max(0.25, 1.0F - (float) iteration * 0.1F), pitch);
    }
	
	public static void playDenySound(World world, PlayerEntity playerEntity) {
		world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SpectrumSoundEvents.USE_FAIL, SoundCategory.PLAYERS, 1.0F, 0.8F + playerEntity.getRandom().nextFloat() * 0.4F);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		if (InkPowered.canUseClient()) {
			tooltip.add(Text.translatable("item.spectrum.radiance_staff.tooltip.ink"));
		} else {
			tooltip.add(Text.translatable("item.spectrum.radiance_staff.tooltip"));
		}
		tooltip.add(Text.translatable("item.spectrum.radiance_staff.tooltip2"));
		tooltip.add(Text.translatable("item.spectrum.radiance_staff.tooltip3"));
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient) {
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SpectrumSoundEvents.RADIANCE_STAFF_CHARGING, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
		return ItemUsage.consumeHeldItem(world, user, hand);
	}
	
	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		// trigger the items' usage action every x ticks
		if (user instanceof ServerPlayerEntity serverPlayerEntity && user.getItemUseTime() > USE_DURATION && user.getItemUseTime() % USE_DURATION == 0) {
			usage(world, stack, serverPlayerEntity);
		}
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		if (world.isClient) {
			return ActionResult.SUCCESS;
		}
		
		PlayerEntity player = context.getPlayer();
		if (player == null) {
			return ActionResult.PASS;
		}
		
		BlockPos pos = context.getBlockPos();
		Direction direction = context.getSide();
		
		if (!world.getBlockState(pos).isOf(SpectrumBlocks.WAND_LIGHT_BLOCK)) { // those get destroyed instead
			BlockPos targetPos = pos.offset(direction);
			if (placeLight(world, targetPos, (ServerPlayerEntity) player)) {
				RadianceStaffItem.playSoundAndParticles(world, targetPos, (ServerPlayerEntity) player, world.random.nextInt(5), world.random.nextInt(5));
			} else {
				RadianceStaffItem.playDenySound(world, player);
			}
			return ActionResult.CONSUME;
		}
		
		return ActionResult.PASS;
	}
	
	public void usage(World world, ItemStack stack, ServerPlayerEntity user) {
		int useTimes = (user.getItemUseTime() / USE_DURATION);
		int maxCheckDistance = Math.min(MAX_REACH_STEPS, useTimes);
		
		BlockPos sourcePos = user.getBlockPos();
		Vec3d cameraVec = user.getRotationVec(0);
		
		for (int iteration = 1; iteration < maxCheckDistance; iteration++) {
			BlockPos targetPos = sourcePos.add(cameraVec.x * (double) iteration * REACH_STEP_DISTANCE, cameraVec.y * (double) iteration * REACH_STEP_DISTANCE, cameraVec.z * (double) iteration * 4);
			targetPos = targetPos.add(iteration - world.getRandom().nextInt(2 * iteration), iteration - world.getRandom().nextInt(2 * iteration), iteration - world.getRandom().nextInt(2 * iteration));
			
			if (world.getLightLevel(LightType.BLOCK, targetPos) < MIN_LIGHT_LEVEL) {
				if (placeLight(world, targetPos, user)) {
					playSoundAndParticles(world, targetPos, user, useTimes, iteration);
				} else {
					playDenySound(world, user);
				}
				break;
			}
		}
	}
	
	@Override
	public int getEnchantability() {
		return 8;
	}
	
	@Override
	public List<InkColor> getUsedColors() {
		return List.of(InkColors.YELLOW);
	}
}
