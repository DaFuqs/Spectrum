package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class NaturesStaffItem extends Item implements InkPowered {

	public static final ItemStack ITEM_COST = new ItemStack(SpectrumItems.VEGETAL, 1);
	public static final InkCost INK_COST = new InkCost(InkColors.LIME, 20);
	
	public NaturesStaffItem(Properties settings) {
		super(settings);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		
		int efficiencyLevel = SpectrumEnchantmentHelper.getLevel(context.registries(), Enchantments.EFFICIENCY, stack);
		if (efficiencyLevel == 0) {
			if (InkPowered.canUseClient()) {
				tooltip.add(Component.translatable("item.spectrum.natures_staff.tooltip_with_ink", INK_COST.color().getColoredInkName()));
			} else {
				tooltip.add(Component.translatable("item.spectrum.natures_staff.tooltip"));
			}
		} else {
			int chancePercent = (int) (getInkCostMod(context.registries(), stack) * 100);
			if (InkPowered.canUseClient()) {
				tooltip.add(Component.translatable("item.spectrum.natures_staff.tooltip_with_ink_and_chance", INK_COST.color().getColoredInkName(), chancePercent));
			} else {
				tooltip.add(Component.translatable("item.spectrum.natures_staff.tooltip_with_chance", chancePercent));
			}
		}
		
		tooltip.add(Component.translatable("item.spectrum.natures_staff.tooltip_lure"));
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		if (canUse(user)) {
			if (world.isClientSide) {
				startSoundInstance(user);
			}
			ItemUtils.startUsingInstantly(world, user, hand);
		}
		return super.use(world, user, hand);
	}
	
	@Environment(EnvType.CLIENT)
	public void startSoundInstance(Player user) {
		Minecraft.getInstance().getSoundManager().play(new NaturesStaffUseSoundInstance(user));
	}
	
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return 20000;
	}

	@Override
	public void onUseTick(Level world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		// trigger the item's usage action every x ticks
		if (remainingUseTicks % 10 != 0) {
			return;
		}
		if (!(user instanceof Player player)) {
			user.releaseUsingItem();
			return;
		}
		if (!canUse(player)) {
			user.releaseUsingItem();
		}
		
		if (!world.isClientSide) {
			HitResult hitResult = Support.playerBlockInteractionRaycast(world, user, player);
			if (hitResult.getType() == HitResult.Type.BLOCK) {
				useOn(new UseOnContext(world, player, player.getUsedItemHand(), player.getItemInHand(player.getUsedItemHand()), (BlockHitResult) hitResult));
			}
		}
	}
	
	public float getInkCostMod(HolderLookup.Provider lookup, ItemStack itemStack) {
		return 3.0F / (3.0F + SpectrumEnchantmentHelper.getLevel(lookup, Enchantments.EFFICIENCY, itemStack));
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		
		Player user = context.getPlayer();
		if (user == null) {
			return InteractionResult.FAIL;
		}
		
		if (world.isClientSide) {
			if (canUse(user)) {
				return InteractionResult.PASS;
			} else {
				playDenySound(world, user);
				return InteractionResult.FAIL;
			}
		}
		
		if (user.getTicksUsingItem() < 2) {
			return InteractionResult.PASS;
		}
		
		if (user instanceof ServerPlayer player) {
			ItemStack stack = context.getItemInHand();
			BlockPos blockPos = context.getClickedPos();
			
			if (!GenericClaimModsCompat.canInteract(world, blockPos, user)) {
				playDenySound(world, context.getPlayer());
				return InteractionResult.FAIL;
			}
			
			if (user.getTicksUsingItem() % 10 == 0) {
				spawnParticlesAndEffect(world, context.getClickedPos());

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
						if (destinationState.getBlock() instanceof SimpleWaterloggedBlock) {
							if (touchesWater(world, blockPos)) {
								destinationState = destinationState.setValue(CoralPlantBlock.WATERLOGGED, true);
							} else {
								destinationState = destinationState.setValue(CoralPlantBlock.WATERLOGGED, false);
							}
						}
						world.setBlock(blockPos, destinationState, 3);

						payForUse(player, stack);
						success = true;
					} else if (sourceState.is(SpectrumBlockTags.NATURES_STAFF_STACKABLE)) {
						// blockstate marked as stackable => stack more on top!
						int i = 0;
						BlockState state;
						do {
							state = world.getBlockState(context.getClickedPos().above(i));
							i++;
						} while (state.is(sourceState.getBlock()));
						
						BlockPos targetPos = context.getClickedPos().above(i - 1);
						if (tryPlaceBlock(sourceState, world, targetPos, Direction.DOWN, Direction.UP)) {
							success = true;
						}
					} else if (sourceState.is(SpectrumBlockTags.NATURES_STAFF_SPREADABLE)) {
						RandomSource random = world.getRandom();

						for (int i = 0; i < 5; i++) {
							BlockPos randomOffsetPos = blockPos.offset(random.nextIntBetweenInclusive(-3, 3), random.nextIntBetweenInclusive(-3, 3), random.nextIntBetweenInclusive(-3, 3));
							if (tryPlaceBlock(sourceState, world, randomOffsetPos, Direction.getRandom(random), Direction.getRandom(random))) {
								success = true;
								break;
							}
						}
					} else if (sourceState.isRandomlyTicking() && sourceState.is(SpectrumBlockTags.NATURES_STAFF_TICKABLE)) {
						// random tickable and whitelisted? => tick
						// without whitelist we would be able to tick budding blocks, ...
						
						if (world instanceof ServerLevel) {
							sourceState.randomTick((ServerLevel) world, blockPos, world.random);
						}
						success = true;
					} else if (BoneMealItem.growCrop(Items.BONE_MEAL.getDefaultInstance(), world, blockPos)) {
						// fertilizable => grow!
						success = true;
					} else {
						if (sourceState.isFaceSturdy(world, blockPos, context.getClickedFace())
								&& BoneMealItem.growWaterPlant(Items.BONE_MEAL.getDefaultInstance(), world, blockPos.relative(context.getClickedFace()), context.getClickedFace())) {
							success = true;
						}
					}
				}

				if (success) {
					payForUse(player, stack);
					SpectrumAdvancementCriteria.NATURES_STAFF_USE.trigger(player, sourceState, world.getBlockState(blockPos));
					return InteractionResult.CONSUME;
				}
			}

		}
		
		return InteractionResult.PASS;
	}
	
	private boolean tryPlaceBlock(BlockState blockState, Level world, BlockPos pos, Direction facing, Direction side) {
		BlockState targetState = blockState.getBlock().getStateForPlacement(new DirectionalPlaceContext(world, pos, facing, ItemStack.EMPTY, side));
		if (targetState != null && world.getBlockState(pos).canBeReplaced() && !world.isOutsideBuildHeight(pos.getY()) && targetState.canSurvive(world, pos)) {
			world.setBlockAndUpdate(pos, targetState);
			
			world.levelEvent(null, LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(targetState));
			world.playSound(null, pos, targetState.getSoundType().getPlaceSound(), SoundSource.PLAYERS, 1.0F, 0.9F + world.getRandom().nextFloat() * 0.2F);
			world.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, pos, 15);
			return true;
		}
		return false;
	}
	
	private static boolean touchesWater(Level world, BlockPos blockPos) {
		return world.getFluidState(blockPos.north()).is(FluidTags.WATER)
				|| world.getFluidState(blockPos.east()).is(FluidTags.WATER)
				|| world.getFluidState(blockPos.south()).is(FluidTags.WATER)
				|| world.getFluidState(blockPos.west()).is(FluidTags.WATER);
	}
	
	private static void spawnParticlesAndEffect(Level world, BlockPos blockPos) {
		BlockState blockState = world.getBlockState(blockPos);
		if (blockState.is(SpectrumBlockTags.NATURES_STAFF_STACKABLE)) {
			int i = 0;
			while (world.getBlockState(blockPos.above(i)).is(blockState.getBlock())) {
				world.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, blockPos.above(i), 15);
				i++;
			}
			world.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, blockPos, 15);
			BoneMealItem.addGrowthParticles(world, blockPos.above(i + 1), 5);
			for (int j = 1; world.getBlockState(blockPos.below(j)).is(blockState.getBlock()); j++) {
				world.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, blockPos.below(j), 15);
			}
		} else {
			world.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, blockPos, 15);
		}
	}
	
	private boolean payForUse(Player player, ItemStack stack) {
		boolean paid = player.isCreative(); // free for creative players
		if (!paid) { // try pay with ink
			paid = InkPowered.tryDrainEnergy(player, INK_COST, getInkCostMod(player.level().registryAccess(), stack));
		}
		if (!paid && player.getInventory().contains(ITEM_COST)) {  // try pay with item
			int efficiencyLevel = SpectrumEnchantmentHelper.getLevel(player.level().registryAccess(), Enchantments.EFFICIENCY, stack);
			if (efficiencyLevel == 0) {
				paid = InventoryHelper.removeFromInventoryWithRemainders(player, ITEM_COST);
			} else {
				paid = player.getRandom().nextFloat() > (2.0 / (2 + efficiencyLevel)) || InventoryHelper.removeFromInventoryWithRemainders(player, ITEM_COST);
			}
		}
		return paid;
	}
	
	private static boolean canUse(Player player) {
		return player.isCreative() || InkPowered.hasAvailableInk(player, INK_COST) || player.getInventory().contains(ITEM_COST);
	}
	
	private void playDenySound(@NotNull Level world, @NotNull Player playerEntity) {
		world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SpectrumSoundEvents.USE_FAIL, SoundSource.PLAYERS, 1.0F, 0.8F + playerEntity.getRandom().nextFloat() * 0.4F);
	}
	
	@Override
	public List<InkColor> getUsedColors() {
		return List.of(INK_COST.color());
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return stack.getCount() == 1;
	}
	
	@Override
	public int getEnchantmentValue() {
		return 10;
	}
	
	@Override
	public boolean canBeEnchantedWith(ItemStack stack, Holder<Enchantment> enchantment, EnchantingContext context) {
		return super.canBeEnchantedWith(stack, enchantment, context) || enchantment.is(Enchantments.EFFICIENCY);
	}
	
}
