package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.energy.InkPowered;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.color.InkColors;
import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.List;

import static net.minecraft.state.property.Properties.WATERLOGGED;

public class RadianceStaffItem extends Item implements InkPowered {
	
	public static int USE_DURATION = 12;
	public static int REACH_STEP_DISTANCE = 4;
	public static int MAX_REACH_STEPS = 8;
	public static int MIN_LIGHT_LEVEL = 10;
	
	public static ItemStack COST = new ItemStack(SpectrumItems.SPARKLESTONE_GEM, 1);
	
	public RadianceStaffItem(Settings settings) {
		super(settings);
	}
	
	public boolean placeLight(World world, BlockPos targetPos, ServerPlayerEntity playerEntity) {
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
		SpectrumS2CPacketSender.sendLightCreatedParticle(world, targetPos);
		world.playSound(null, playerEntity.getX() + 0.5, playerEntity.getY() + 0.5, playerEntity.getZ() + 0.5, SpectrumSoundEvents.LIGHT_STAFF_PLACE, SoundCategory.PLAYERS, (float) Math.max(0.25, 1.0F - (float) iteration * 0.1F), pitch);
	}
	
	public static void playDenySound(World world, PlayerEntity playerEntity) {
		world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SpectrumSoundEvents.USE_FAIL, SoundCategory.PLAYERS, 1.0F, 0.8F + playerEntity.getRandom().nextFloat() * 0.4F);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		if(InkPowered.canUse()) {
			tooltip.add(new TranslatableText("item.spectrum.light_staff.tooltip.ink"));
		} else {
			tooltip.add(new TranslatableText("item.spectrum.light_staff.tooltip"));
		}
		tooltip.add(new TranslatableText("item.spectrum.light_staff.tooltip2"));
	}
	
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}
	
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient) {
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SpectrumSoundEvents.LIGHT_STAFF_CHARGING, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
		return ItemUsage.consumeHeldItem(world, user, hand);
	}
	
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		// trigger the items' usage action every x ticks
		if (user instanceof ServerPlayerEntity serverPlayerEntity && user.getItemUseTime() > USE_DURATION && user.getItemUseTime() % USE_DURATION == 0) {
			usage(world, stack, serverPlayerEntity);
		}
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
