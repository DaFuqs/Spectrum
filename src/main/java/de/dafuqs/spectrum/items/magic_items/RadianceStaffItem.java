package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.blocks.enchanter.EnchanterEnchantable;
import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.world.ServerWorld;
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

public class RadianceStaffItem extends Item implements EnchanterEnchantable {
	
	public static int USE_DURATION = 12;
	public static int REACH_STEP_DISTANCE = 4;
	public static int MAX_REACH_STEPS = 8;
	public static int MIN_LIGHT_LEVEL = 10;
	
	public static ItemStack COST = new ItemStack(SpectrumItems.SPARKLESTONE_GEM, 1);
	
	public RadianceStaffItem(Settings settings) {
		super(settings);
	}
	
	public static boolean placeLight(World world, BlockPos targetPos, PlayerEntity playerEntity, ItemStack stack) {
		BlockState targetBlockState = world.getBlockState(targetPos);
		if (targetBlockState.isAir()) {
			if (EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0 || InventoryHelper.removeFromInventory(playerEntity, COST)) {
				world.setBlockState(targetPos, SpectrumBlocks.WAND_LIGHT_BLOCK.getDefaultState(), 3);
				return true;
			}
		} else if (targetBlockState.isOf(Blocks.WATER)) {
			if (EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0 || InventoryHelper.removeFromInventory(playerEntity, COST)) {
				world.setBlockState(targetPos, SpectrumBlocks.WAND_LIGHT_BLOCK.getDefaultState().with(WATERLOGGED, true), 3);
				return true;
			}
		}
		return false;
	}
	
	public static void playSoundAndParticles(World world, BlockPos targetPos, PlayerEntity playerEntity, int useTimes, int iteration) {
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
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		if (EnchantmentHelper.getLevel(Enchantments.INFINITY, itemStack) == 0) {
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
		if (world instanceof ServerWorld && user.getItemUseTime() > USE_DURATION && user.getItemUseTime() % USE_DURATION == 0) {
			usage(world, stack, user);
		}
	}
	
	public void usage(World world, ItemStack stack, LivingEntity user) {
		if (user instanceof PlayerEntity playerEntity) {
			int useTimes = (user.getItemUseTime() / USE_DURATION);
			int maxCheckDistance = Math.min(MAX_REACH_STEPS, useTimes);
			
			BlockPos sourcePos = user.getBlockPos();
			Vec3d cameraVec = user.getRotationVec(0);
			
			for (int iteration = 1; iteration < maxCheckDistance; iteration++) {
				BlockPos targetPos = sourcePos.add(cameraVec.x * (double) iteration * REACH_STEP_DISTANCE, cameraVec.y * (double) iteration * REACH_STEP_DISTANCE, cameraVec.z * (double) iteration * 4);
				targetPos = targetPos.add(iteration - world.getRandom().nextInt(2 * iteration), iteration - world.getRandom().nextInt(2 * iteration), iteration - world.getRandom().nextInt(2 * iteration));
				
				if (world.getLightLevel(LightType.BLOCK, targetPos) < MIN_LIGHT_LEVEL) {
					if (placeLight(world, targetPos, playerEntity, stack)) {
						playSoundAndParticles(world, targetPos, playerEntity, useTimes, iteration);
					} else {
						playDenySound(world, playerEntity);
					}
					break;
				}
			}
		}
	}
	
	@Override
	public boolean canAcceptEnchantment(Enchantment enchantment) {
		return enchantment == Enchantments.INFINITY;
	}
	
	@Override
	public int getEnchantability() {
		return 8;
	}
	
}
