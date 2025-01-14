package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.item.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.registry.tag.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class NaturesStaffItem extends Item implements ExtendedEnchantable, InkPowered {

	public static final ItemStack ITEM_COST = new ItemStack(SpectrumItems.VEGETAL, 1);
	public static final InkCost INK_COST = new InkCost(InkColors.LIME, 20);
	
	public NaturesStaffItem(Settings settings) {
		super(settings);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		
		int efficiencyLevel = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, itemStack);
		if (efficiencyLevel == 0) {
			if (InkPowered.canUseClient()) {
				tooltip.add(Text.translatable("item.spectrum.natures_staff.tooltip_with_ink", INK_COST.getColor().getColoredInkName()));
			} else {
				tooltip.add(Text.translatable("item.spectrum.natures_staff.tooltip"));
			}
		} else {
			int chancePercent = (int) (getInkCostMod(itemStack) * 100);
			if (InkPowered.canUseClient()) {
				tooltip.add(Text.translatable("item.spectrum.natures_staff.tooltip_with_ink_and_chance", INK_COST.getColor().getColoredInkName(), chancePercent));
			} else {
				tooltip.add(Text.translatable("item.spectrum.natures_staff.tooltip_with_chance", chancePercent));
			}
		}
		
		tooltip.add(Text.translatable("item.spectrum.natures_staff.tooltip_lure"));
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (canUse(user)) {
			if (world.isClient) {
				startSoundInstance(user);
			}
			ItemUsage.consumeHeldItem(world, user, hand);
		}
		return super.use(world, user, hand);
	}
	
	@Environment(EnvType.CLIENT)
	public void startSoundInstance(PlayerEntity user) {
		MinecraftClient.getInstance().getSoundManager().play(new NaturesStaffUseSoundInstance(user));
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 20000;
	}

	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		// trigger the item's usage action every x ticks
		if (remainingUseTicks % 10 != 0) {
			return;
		}
		if (!(user instanceof PlayerEntity player)) {
			user.stopUsingItem();
			return;
		}
		if (!canUse(player)) {
			user.stopUsingItem();
		}
		
		if (!world.isClient) {
			HitResult hitResult = Support.playerInteractionRaycast(world, user, player);
			if (hitResult.getType() == HitResult.Type.BLOCK) {
				useOnBlock(new ItemUsageContext(world, player, player.getActiveHand(), player.getStackInHand(player.getActiveHand()), (BlockHitResult) hitResult));
			}
		}
	}
	
	public float getInkCostMod(ItemStack itemStack) {
		return 3.0F / (3.0F + EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, itemStack));
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		
		PlayerEntity user = context.getPlayer();
		if (user == null) {
			return ActionResult.FAIL;
		}

		if (world.isClient) {
			if (canUse(user)) {
				return ActionResult.PASS;
			} else {
				playDenySound(world, user);
				return ActionResult.FAIL;
			}
		}

		if (user.getItemUseTime() < 2) {
			return ActionResult.PASS;
		}
		
		if (user instanceof ServerPlayerEntity player) {
			ItemStack stack = context.getStack();
			BlockPos blockPos = context.getBlockPos();
			
			if (!GenericClaimModsCompat.canInteract(world, blockPos, user)) {
				playDenySound(world, context.getPlayer());
				return ActionResult.FAIL;
			}

			if (user.getItemUseTime() % 10 == 0) {
				spawnParticlesAndEffect(world, context.getBlockPos());

				boolean success = false;
				BlockState sourceState = world.getBlockState(blockPos);

				if (sourceState.getBlock() instanceof NaturesStaffTriggered naturesStaffTriggered && naturesStaffTriggered.canUseNaturesStaff(world, blockPos, sourceState)) {
					if (naturesStaffTriggered.onNaturesStaffUse(world, blockPos, sourceState, player)) {
						success = true;
					}
				} else {
					// loaded as convertible? => convert
					BlockState destinationState = NaturesStaffConversionDataLoader.getConvertedBlockState(sourceState.getBlock());
					if (destinationState != null) {
						if (destinationState.getBlock() instanceof Waterloggable) {
							if (touchesWater(world, blockPos)) {
								destinationState = destinationState.with(CoralBlock.WATERLOGGED, true);
							} else {
								destinationState = destinationState.with(CoralBlock.WATERLOGGED, false);
							}
						}
						world.setBlockState(blockPos, destinationState, 3);

						payForUse(player, stack);
						success = true;
					} else if (sourceState.isIn(SpectrumBlockTags.NATURES_STAFF_STACKABLE)) {
						// blockstate marked as stackable => stack more on top!
						int i = 0;
						BlockState state;
						do {
							state = world.getBlockState(context.getBlockPos().up(i));
							i++;
						} while (state.isOf(sourceState.getBlock()));

						BlockPos targetPos = context.getBlockPos().up(i - 1);
						if (tryPlaceBlock(sourceState, world, targetPos, Direction.DOWN, Direction.UP)) {
							success = true;
						}
					} else if (sourceState.isIn(SpectrumBlockTags.NATURES_STAFF_SPREADABLE)) {
						Random random = world.getRandom();

						for (int i = 0; i < 5; i++) {
							BlockPos randomOffsetPos = blockPos.add(random.nextBetween(-3, 3), random.nextBetween(-3, 3), random.nextBetween(-3, 3));
							if (tryPlaceBlock(sourceState, world, randomOffsetPos, Direction.random(random), Direction.random(random))) {
								success = true;
								break;
							}
						}
					} else if (sourceState.hasRandomTicks() && sourceState.isIn(SpectrumBlockTags.NATURES_STAFF_TICKABLE)) {
						// random tickable and whitelisted? => tick
						// without whitelist we would be able to tick budding blocks, ...

						if (world instanceof ServerWorld) {
							sourceState.randomTick((ServerWorld) world, blockPos, world.random);
						}
						success = true;
					} else if (BoneMealItem.useOnFertilizable(Items.BONE_MEAL.getDefaultStack(), world, blockPos)) {
						// fertilizable => grow!
						success = true;
					} else {
						if (sourceState.isSideSolidFullSquare(world, blockPos, context.getSide())
								&& BoneMealItem.useOnGround(Items.BONE_MEAL.getDefaultStack(), world, blockPos.offset(context.getSide()), context.getSide())) {
							success = true;
						}
					}
				}

				if (success) {
					payForUse(player, stack);
					SpectrumAdvancementCriteria.NATURES_STAFF_USE.trigger(player, sourceState, world.getBlockState(blockPos));
					return ActionResult.CONSUME;
				}
			}

		}
		
		return ActionResult.PASS;
	}

	private boolean tryPlaceBlock(BlockState blockState, World world, BlockPos pos, Direction facing, Direction side) {
		BlockState targetState = blockState.getBlock().getPlacementState(new AutomaticItemPlacementContext(world, pos, facing, ItemStack.EMPTY, side));
		if (targetState != null && world.getBlockState(pos).isReplaceable() && !world.isOutOfHeightLimit(pos.getY()) && targetState.canPlaceAt(world, pos)) {
			world.setBlockState(pos, targetState);

			world.syncWorldEvent(null, WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(targetState));
			world.playSound(null, pos, targetState.getSoundGroup().getPlaceSound(), SoundCategory.PLAYERS, 1.0F, 0.9F + world.getRandom().nextFloat() * 0.2F);
			world.syncWorldEvent(WorldEvents.PLANT_FERTILIZED, pos, 0); // the particle here is jank
			return true;
		}
		return false;
	}

	private static boolean touchesWater(World world, BlockPos blockPos) {
		return world.getFluidState(blockPos.north()).isIn(FluidTags.WATER)
				|| world.getFluidState(blockPos.east()).isIn(FluidTags.WATER)
				|| world.getFluidState(blockPos.south()).isIn(FluidTags.WATER)
				|| world.getFluidState(blockPos.west()).isIn(FluidTags.WATER);
	}

	private static void spawnParticlesAndEffect(World world, BlockPos blockPos) {
		BlockState blockState = world.getBlockState(blockPos);
		if (blockState.isIn(SpectrumBlockTags.NATURES_STAFF_STACKABLE)) {
			int i = 0;
			while (world.getBlockState(blockPos.up(i)).isOf(blockState.getBlock())) {
				world.syncWorldEvent(WorldEvents.PLANT_FERTILIZED, blockPos.up(i), 0);
				i++;
			}
			world.syncWorldEvent(WorldEvents.PLANT_FERTILIZED, blockPos, 0);
			BoneMealItem.createParticles(world, blockPos.up(i + 1), 5);
			for (int j = 1; world.getBlockState(blockPos.down(j)).isOf(blockState.getBlock()); j++) {
				world.syncWorldEvent(WorldEvents.PLANT_FERTILIZED, blockPos.down(j), 0);
			}
		} else {
			world.syncWorldEvent(WorldEvents.PLANT_FERTILIZED, blockPos, 0);
		}
	}
	
	private boolean payForUse(PlayerEntity player, ItemStack stack) {
		boolean paid = player.isCreative(); // free for creative players
		if (!paid) { // try pay with ink
			paid = InkPowered.tryDrainEnergy(player, INK_COST, getInkCostMod(stack));
		}
		if (!paid && player.getInventory().contains(ITEM_COST)) {  // try pay with item
			int efficiencyLevel = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, stack);
			if (efficiencyLevel == 0) {
				paid = InventoryHelper.removeFromInventoryWithRemainders(player, ITEM_COST);
			} else {
				paid = player.getRandom().nextFloat() > (2.0 / (2 + efficiencyLevel)) || InventoryHelper.removeFromInventoryWithRemainders(player, ITEM_COST);
			}
		}
		return paid;
	}
	
	private static boolean canUse(PlayerEntity player) {
		return player.isCreative() || InkPowered.hasAvailableInk(player, INK_COST) || player.getInventory().contains(ITEM_COST);
	}
	
	private void playDenySound(@NotNull World world, @NotNull PlayerEntity playerEntity) {
		world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SpectrumSoundEvents.USE_FAIL, SoundCategory.PLAYERS, 1.0F, 0.8F + playerEntity.getRandom().nextFloat() * 0.4F);
	}
	
	@Override
	public List<InkColor> getUsedColors() {
		return List.of(INK_COST.getColor());
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return stack.getCount() == 1;
	}
	
	@Override
	public boolean acceptsEnchantment(Enchantment enchantment) {
		return enchantment == Enchantments.EFFICIENCY;
	}
	
	@Override
	public int getEnchantability() {
		return 10;
	}
	
}
