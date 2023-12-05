package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.blocks.enchanter.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.color.*;
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
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.tag.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class NaturesStaffItem extends Item implements ExtendedEnchantable, InkPowered {
	
	/**
	 * Blocks that have an effect when a Nature's Staff is used on them
	 */
	public interface NaturesStaffTriggered {
		/**
		 * @return if the staff can be used on the state
		 */
		boolean canUseNaturesStaff(World world, BlockPos pos, BlockState state);
		
		/**
		 * @return if effects should play on that pos
		 */
		boolean onNaturesStaffUse(World world, BlockPos pos, BlockState state, PlayerEntity player);
	}
	
	public static final ItemStack ITEM_COST = new ItemStack(SpectrumItems.VEGETAL, 1);
	public static final InkCost INK_COST = new InkCost(InkColors.LIME, 20);
	
	public NaturesStaffItem(Settings settings) {
		super(settings);
	}
	
	/**
	 * Near identical copy of BonemealItem.useOnFertilizable
	 * just with stack decrement removed
	 */
	public static boolean useOnFertilizable(@NotNull World world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		if (blockState.getBlock() instanceof Fertilizable fertilizable) {
			if (fertilizable.isFertilizable(world, pos, blockState, world.isClient)) {
				if (world instanceof ServerWorld) {
					if (fertilizable.canGrow(world, world.random, pos, blockState)) {
						fertilizable.grow((ServerWorld) world, world.random, pos, blockState);
					}
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Near identical copy of BonemealItem.useOnGround
	 * just with stack decrement removed
	 */
	public static boolean useOnGround(@NotNull World world, BlockPos blockPos, @Nullable Direction facing) {
		if (world.getBlockState(blockPos).isOf(Blocks.WATER) && world.getFluidState(blockPos).getLevel() == 8) {
			if (world instanceof ServerWorld) {
				Random random = world.getRandom();
				
				label78:
				for (int i = 0; i < 128; ++i) {
					BlockPos blockPos2 = blockPos;
					BlockState grownState = Blocks.SEAGRASS.getDefaultState();
					
					for (int j = 0; j < i / 16; ++j) {
						blockPos2 = blockPos2.add(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
						if (world.getBlockState(blockPos2).isFullCube(world, blockPos2)) {
							continue label78;
						}
					}
					
					RegistryEntry<Biome> biomeKey = world.getBiome(blockPos2);
					if (biomeKey.isIn(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL)) {
						if (i == 0 && facing != null && facing.getAxis().isHorizontal()) {
							grownState = Registry.BLOCK.getEntryList(BlockTags.WALL_CORALS)
									.flatMap((blocks) -> blocks.getRandom(world.random))
									.map((blockEntry) -> blockEntry.value().getDefaultState())
									.orElse(grownState);
							if (grownState.contains(DeadCoralWallFanBlock.FACING)) {
								grownState = grownState.with(DeadCoralWallFanBlock.FACING, facing);
							}
						} else if (random.nextInt(4) == 0) {
							grownState = Registry.BLOCK.getEntryList(BlockTags.UNDERWATER_BONEMEALS)
									.flatMap((blocks) -> blocks.getRandom(world.random))
									.map((blockEntry) -> blockEntry.value().getDefaultState())
									.orElse(grownState);
						}
					}
					
					if (grownState.isIn(BlockTags.WALL_CORALS, (state) -> state.contains(DeadCoralWallFanBlock.FACING))) {
						for (int k = 0; !grownState.canPlaceAt(world, blockPos2) && k < 4; ++k) {
							grownState = grownState.with(DeadCoralWallFanBlock.FACING, Direction.Type.HORIZONTAL.random(random));
						}
					}
					
					if (grownState.canPlaceAt(world, blockPos2)) {
						BlockState currentState = world.getBlockState(blockPos2);
						if (currentState.isOf(Blocks.WATER) && world.getFluidState(blockPos2).getLevel() == 8) {
							world.setBlockState(blockPos2, grownState, 3);
						} else if (currentState.isOf(Blocks.SEAGRASS) && random.nextInt(10) == 0) {
							((Fertilizable) Blocks.SEAGRASS).grow((ServerWorld) world, random, blockPos2, currentState);
						}
					}
				}
				
			}
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		
		int efficiencyLevel = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, itemStack);
		if (efficiencyLevel == 0) {
			if (InkPowered.canUseClient()) {
				tooltip.add(Text.translatable("item.spectrum.natures_staff.tooltip_with_ink"));
			} else {
				tooltip.add(Text.translatable("item.spectrum.natures_staff.tooltip"));
			}
		} else {
			int chancePercent = (int) (getInkCostMod(itemStack) * 100);
			if (InkPowered.canUseClient()) {
				tooltip.add(Text.translatable("item.spectrum.natures_staff.tooltip_with_ink_and_chance", chancePercent));
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
		
		if (world.isClient) {
			usageTickClient();
		}
	}
	
	@Environment(EnvType.CLIENT)
	@SuppressWarnings("resource")
	public void usageTickClient() {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.crosshairTarget.getType() == HitResult.Type.BLOCK) {
			client.interactionManager.interactBlock(
					client.player,
					client.player.getActiveHand(),
					(BlockHitResult) client.crosshairTarget
			);
		}
	}
	
	public float getInkCostMod(ItemStack itemStack) {
		return 3.0F / (3.0F + EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, itemStack));
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		
		PlayerEntity user = context.getPlayer();
		if (world.isClient) {
			if (user == null) {
				return ActionResult.FAIL;
			}
			if (canUse(user)) {
				return ActionResult.PASS;
			} else {
				playDenySound(world, user);
				return ActionResult.FAIL;
			}
		}
		
		if (user == null || user.getItemUseTime() < 2) {
			return ActionResult.PASS;
		}
		
		if (user instanceof ServerPlayerEntity player) {
			ItemStack stack = context.getStack();
			BlockPos blockPos = context.getBlockPos();
			
			if (GenericClaimModsCompat.isProtected(world, blockPos, user)) {
				playDenySound(world, context.getPlayer());
				return ActionResult.FAIL;
			}
			
			if (user.getItemUseTime() % 10 == 0) {
				BlockState blockState = world.getBlockState(blockPos);
				
				if (blockState.getBlock() instanceof NaturesStaffTriggered naturesStaffTriggered && naturesStaffTriggered.canUseNaturesStaff(world, blockPos, blockState)) {
					if (naturesStaffTriggered.onNaturesStaffUse(world, blockPos, blockState, player)) {
						payForUse(player, stack);
						world.syncWorldEvent(WorldEvents.PLANT_FERTILIZED, blockPos, 0);
						SpectrumAdvancementCriteria.NATURES_STAFF_USE.trigger(player, blockState, world.getBlockState(blockPos));
					}
					return ActionResult.CONSUME;
				}
				
				// loaded as convertible? => convert
				BlockState destinationState = NaturesStaffConversionDataLoader.getConvertedBlockState(blockState.getBlock());
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
					world.syncWorldEvent(WorldEvents.PLANT_FERTILIZED, blockPos, 0);
					SpectrumAdvancementCriteria.NATURES_STAFF_USE.trigger(player, blockState, destinationState);
					
					return ActionResult.CONSUME;
					// fertilizable? => grow
				} else if (useOnFertilizable(world, blockPos)) {
					payForUse(player, stack);
					world.syncWorldEvent(WorldEvents.PLANT_FERTILIZED, blockPos, 0);
					return ActionResult.CONSUME;
					// blockstate marked as stackable? => stack on top!
				} else if (blockState.isIn(SpectrumBlockTags.NATURES_STAFF_STACKABLE)) {
					int i = 0;
					BlockState state;
					do {
						state = world.getBlockState(context.getBlockPos().up(i));
						i++;
					} while (state.isOf(blockState.getBlock()));
					
					BlockPos targetPos = context.getBlockPos().up(i - 1);
					BlockState targetState = blockState.getBlock().getPlacementState(new AutomaticItemPlacementContext(world, blockPos, Direction.DOWN, null, Direction.UP));
					if (targetState != null && world.getBlockState(targetPos).isAir() && !world.isOutOfHeightLimit(targetPos.getY()) && targetState.canPlaceAt(world, targetPos)) {
						world.setBlockState(targetPos, targetState);
						
						world.syncWorldEvent(null, WorldEvents.BLOCK_BROKEN, targetPos, Block.getRawIdFromState(targetState));
						world.playSound(null, targetPos, targetState.getSoundGroup().getPlaceSound(), SoundCategory.PLAYERS, 1.0F, 0.9F + world.getRandom().nextFloat() * 0.2F);
						payForUse(player, stack);
						world.syncWorldEvent(WorldEvents.PLANT_FERTILIZED, targetPos, 0);
						return ActionResult.CONSUME;
					}
					
					// random tickable and whitelisted? => tick
					// without whitelist we would be able to tick budding blocks, ...
				} else if (blockState.hasRandomTicks() && blockState.isIn(SpectrumBlockTags.NATURES_STAFF_TICKABLE)) {
					if (world instanceof ServerWorld) {
						blockState.randomTick((ServerWorld) world, blockPos, world.random);
					}
					payForUse(player, stack);
					world.syncWorldEvent(WorldEvents.PLANT_FERTILIZED, blockPos, 0);
					return ActionResult.CONSUME;
				} else {
					BlockPos blockPos2 = blockPos.offset(context.getSide());
					boolean bl = blockState.isSideSolidFullSquare(world, blockPos, context.getSide());
					if (bl && useOnGround(world, blockPos2, context.getSide())) {
						payForUse(player, stack);
						world.syncWorldEvent(WorldEvents.PLANT_FERTILIZED, blockPos2, 0);
						return ActionResult.CONSUME;
					}
				}
			} else {
				playDenySound(world, context.getPlayer());
			}
		}
		
		return ActionResult.PASS;
	}
	
	private static boolean touchesWater(World world, BlockPos blockPos) {
		return world.getFluidState(blockPos.north()).isIn(FluidTags.WATER)
				|| world.getFluidState(blockPos.east()).isIn(FluidTags.WATER)
				|| world.getFluidState(blockPos.south()).isIn(FluidTags.WATER)
				|| world.getFluidState(blockPos.west()).isIn(FluidTags.WATER);
	}
	
	private static void spawnParticles(ItemUsageContext context, World world, BlockPos blockPos) {
		BlockState blockState = world.getBlockState(blockPos);
		if (blockState.getBlock() instanceof NaturesStaffTriggered naturesStaffTriggered && naturesStaffTriggered.canUseNaturesStaff(world, blockPos, blockState)) {
			BoneMealItem.createParticles(world, blockPos, 3);
		} else if (blockState.isIn(SpectrumBlockTags.NATURES_STAFF_STACKABLE)) {
			int i = 0;
			while (world.getBlockState(context.getBlockPos().up(i)).isOf(blockState.getBlock())) {
				BoneMealItem.createParticles(world, context.getBlockPos().up(i), 3);
				i++;
			}
			BoneMealItem.createParticles(world, context.getBlockPos().up(i + 1), 5);
			for (int j = 1; world.getBlockState(context.getBlockPos().down(j)).isOf(blockState.getBlock()); j++) {
				BoneMealItem.createParticles(world, context.getBlockPos().down(j), 3);
			}
		} else {
			BoneMealItem.createParticles(world, blockPos, 15);
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
	
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		super.appendStacks(group, stacks);
		if (this.isIn(group)) {
			stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(this));
		}
	}
	
}
