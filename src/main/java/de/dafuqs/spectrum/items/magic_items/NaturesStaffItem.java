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
import net.minecraft.particle.*;
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
					BlockState blockState = Blocks.SEAGRASS.getDefaultState();
					
					for (int j = 0; j < i / 16; ++j) {
						blockPos2 = blockPos2.add(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
						if (world.getBlockState(blockPos2).isFullCube(world, blockPos2)) {
							continue label78;
						}
					}
					
					RegistryEntry<Biome> j = world.getBiome(blockPos2);
					if (j.matchesKey(BiomeKeys.WARM_OCEAN)) {
						if (i == 0 && facing != null && facing.getAxis().isHorizontal()) {
							blockState = Registry.BLOCK.getEntryList(BlockTags.WALL_CORALS).flatMap((blocks) -> blocks.getRandom(world.random)).map((blockEntry) -> (blockEntry.value()).getDefaultState()).orElse(blockState);
							if (blockState.contains(DeadCoralWallFanBlock.FACING)) {
								blockState = blockState.with(DeadCoralWallFanBlock.FACING, facing);
							}
						} else if (random.nextInt(4) == 0) {
							blockState = Registry.BLOCK.getEntryList(BlockTags.UNDERWATER_BONEMEALS).flatMap((blocks) -> blocks.getRandom(world.random)).map((blockEntry) -> (blockEntry.value()).getDefaultState()).orElse(blockState);
						}
					}
					
					if (blockState.isIn(BlockTags.WALL_CORALS, (state) -> state.contains(DeadCoralWallFanBlock.FACING))) {
						for (int k = 0; !blockState.canPlaceAt(world, blockPos2) && k < 4; ++k) {
							blockState = blockState.with(DeadCoralWallFanBlock.FACING, Direction.Type.HORIZONTAL.random(random));
						}
					}
					
					if (blockState.canPlaceAt(world, blockPos2)) {
						BlockState k = world.getBlockState(blockPos2);
						if (k.isOf(Blocks.WATER) && world.getFluidState(blockPos2).getLevel() == 8) {
							world.setBlockState(blockPos2, blockState, 3);
						} else if (k.isOf(Blocks.SEAGRASS) && random.nextInt(10) == 0) {
							((Fertilizable) Blocks.SEAGRASS).grow((ServerWorld) world, random, blockPos2, k);
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
		if (player instanceof ServerPlayerEntity) {
			if (!payForUse(player, stack)) {
				return;
			}
		} else {
			if (!canUse(player)) {
				user.stopUsingItem();
			}
		}
		
		if (world.isClient) {
			usageTickClient();
		}
	}
	
	@Environment(EnvType.CLIENT)
	@SuppressWarnings("resource")
	public void usageTickClient() {
		if (MinecraftClient.getInstance().crosshairTarget.getType() == HitResult.Type.BLOCK) {
			MinecraftClient.getInstance().interactionManager.interactBlock(
					MinecraftClient.getInstance().player,
					MinecraftClient.getInstance().player.getActiveHand(),
					(BlockHitResult) MinecraftClient.getInstance().crosshairTarget
			);
		}
	}
	
	public float getInkCostMod(ItemStack itemStack) {
		return 3.0F / (3.0F + EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, itemStack));
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		PlayerEntity user = context.getPlayer();
		
		if (user != null && user.getItemUseTime() > 2) {
			World world = context.getWorld();
			BlockPos blockPos = context.getBlockPos();
			
			if (GenericClaimModsCompat.isProtected(world, blockPos, user)) {
				return ActionResult.FAIL;
			}
			
			if (world.isClient) {
				spawnParticles(context, world, blockPos);
			} else if (user.getItemUseTime() % 10 == 0) {
				ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
				
				BlockState blockState = world.getBlockState(blockPos);
				
				if (blockState.getBlock() instanceof NaturesStaffTriggered naturesStaffTriggered && naturesStaffTriggered.canUseNaturesStaff(world, blockPos, blockState)) {
					if (naturesStaffTriggered.onNaturesStaffUse(world, blockPos, blockState, player)) {
						//BoneMealItem.createParticles(world, blockPos, 3);
						world.syncWorldEvent(2005, blockPos, 0);
					}
					return ActionResult.success(false);
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
					world.syncWorldEvent(2005, blockPos, 0);
					
					if (user instanceof ServerPlayerEntity serverPlayerEntity) {
						SpectrumAdvancementCriteria.NATURES_STAFF_USE.trigger(serverPlayerEntity, blockState, destinationState);
					}
					
					return ActionResult.success(false);
					// fertilizable? => grow
				} else if (useOnFertilizable(world, blockPos)) {
					world.syncWorldEvent(2005, blockPos, 0);
					return ActionResult.success(false);
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
					if (world.getBlockState(targetPos).isAir() && !world.isOutOfHeightLimit(targetPos.getY()) && targetState.canPlaceAt(world, targetPos)) {
						world.setBlockState(targetPos, targetState);
						for (int j = 0; j < 20; j++) {
							((ServerWorld) world).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, targetState), targetPos.getX() + world.getRandom().nextFloat(), targetPos.getY() + world.getRandom().nextFloat(), targetPos.getZ() + world.getRandom().nextFloat(), 1, 0, 0, 0, 0.1);
						}
						world.playSound(null, targetPos, targetState.getSoundGroup().getPlaceSound(), SoundCategory.PLAYERS, 1.0F, 0.9F + world.getRandom().nextFloat() * 0.2F);
						world.syncWorldEvent(2005, targetPos, 0);
						return ActionResult.success(false);
					}
					
					// random tickable and whitelisted? => tick
					// without whitelist we would be able to tick budding blocks, ...
				} else if (blockState.hasRandomTicks() && blockState.isIn(SpectrumBlockTags.NATURES_STAFF_TICKABLE)) {
					if (world instanceof ServerWorld) {
						blockState.randomTick((ServerWorld) world, blockPos, world.random);
					}
					world.syncWorldEvent(2005, blockPos, 0);
					return ActionResult.success(false);
				} else {
					BlockPos blockPos2 = blockPos.offset(context.getSide());
					boolean bl = blockState.isSideSolidFullSquare(world, blockPos, context.getSide());
					if (bl && useOnGround(world, blockPos2, context.getSide())) {
						world.syncWorldEvent(2005, blockPos2, 0);
						return ActionResult.success(false);
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
