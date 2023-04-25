package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.blocks.enchanter.*;
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
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class NaturesStaffItem extends Item implements EnchanterEnchantable, InkPowered {
	
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
	
	public static final InkColor USED_COLOR = InkColors.LIME;
	public static final int BASE_COST = 20;
	public static ItemStack ITEM_COST = new ItemStack(SpectrumItems.VEGETAL, 1);
	
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
			int chancePercent = (int) Math.round(2.0 / (2 + efficiencyLevel) * 100);
			if (InkPowered.canUseClient()) {
				tooltip.add(Text.translatable("item.spectrum.natures_staff.tooltip_with_ink_and_chance", chancePercent));
			} else {
				tooltip.add(Text.translatable("item.spectrum.natures_staff.tooltip_with_chance", chancePercent));
			}
		}
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (world.isClient) {
			startSoundInstance(user);
		}
		return ItemUsage.consumeHeldItem(world, user, hand);
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
		if (remainingUseTicks % 10 == 0 && world.isClient) {
			usageTickClient();
		}
	}
	
	@Environment(EnvType.CLIENT)
	public void usageTickClient() {
		if (MinecraftClient.getInstance().crosshairTarget.getType() == HitResult.Type.BLOCK) {
			MinecraftClient.getInstance().interactionManager.interactBlock(
					MinecraftClient.getInstance().player,
					MinecraftClient.getInstance().player.getActiveHand(),
					(BlockHitResult) MinecraftClient.getInstance().crosshairTarget
			);
		}
	}
	
	public int getInkCost(ItemStack itemStack) {
		int efficiency = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, itemStack);
		return Math.max(5, BASE_COST - 2 * efficiency);
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		PlayerEntity user = context.getPlayer();
		
		if (user != null && user.getItemUseTime() > 2) {
			World world = context.getWorld();
			BlockPos blockPos = context.getBlockPos();
			
			if (world.isClient) {
				if (context.getPlayer().isCreative() || InkPowered.hasAvailableInk(user, USED_COLOR, getInkCost(context.getStack())) || context.getPlayer().getInventory().contains(ITEM_COST)) {
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
			} else if (user.getItemUseTime() % 10 == 0) {
				ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
				boolean paid = player.isCreative(); // free for creative players
				if (!paid) { // try pay with ink
					paid = InkPowered.tryDrainEnergy(context.getPlayer(), USED_COLOR, getInkCost(context.getStack()));
				}
				if (!paid) {  // try pay with item
					int efficiencyLevel = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, context.getStack());
					paid = (efficiencyLevel == 0 && InventoryHelper.removeFromInventoryWithRemainders(context.getPlayer(), ITEM_COST)) || (context.getWorld().random.nextFloat() > (2.0 / (2 + efficiencyLevel)) || InventoryHelper.removeFromInventoryWithRemainders(context.getPlayer(), ITEM_COST));
				}
				
				if (paid) {
					BlockState blockState = world.getBlockState(blockPos);
					
					if (blockState.getBlock() instanceof NaturesStaffTriggered naturesStaffTriggered && naturesStaffTriggered.canUseNaturesStaff(world, blockPos, blockState)) {
						if (naturesStaffTriggered.onNaturesStaffUse(world, blockPos, blockState, player)) {
							BoneMealItem.createParticles(world, blockPos, 3);
							world.syncWorldEvent(2005, blockPos, 0);
						}
						return ActionResult.success(false);
					}
					
					// loaded as convertible? => convert
					BlockState destinationState = NaturesStaffConversionDataLoader.getConvertedBlockState(blockState.getBlock());
					if (destinationState != null) {
						if (destinationState.getBlock() instanceof Waterloggable) {
							if ((world.getFluidState(blockPos.north()).isIn(FluidTags.WATER)
									|| world.getFluidState(blockPos.east()).isIn(FluidTags.WATER)
									|| world.getFluidState(blockPos.south()).isIn(FluidTags.WATER)
									|| world.getFluidState(blockPos.west()).isIn(FluidTags.WATER))) {
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
						BlockState targetState = blockState.getBlock().getDefaultState();
						if (world.getBlockState(targetPos).isAir() && !world.isOutOfHeightLimit(targetPos.getY()) && blockState.getBlock().canPlaceAt(targetState, world, targetPos)) {
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
			} else {
				playDenySound(world, context.getPlayer());
			}
		}
		
		return ActionResult.PASS;
	}
	
	private void playDenySound(@NotNull World world, @NotNull PlayerEntity playerEntity) {
		world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SpectrumSoundEvents.USE_FAIL, SoundCategory.PLAYERS, 1.0F, 0.8F + playerEntity.getRandom().nextFloat() * 0.4F);
	}
	
	@Override
	public boolean canAcceptEnchantment(Enchantment enchantment) {
		return enchantment == Enchantments.EFFICIENCY;
	}
	
	@Override
	public int getEnchantability() {
		return 10;
	}
	
	@Override
	public List<InkColor> getUsedColors() {
		return List.of(USED_COLOR);
	}
	
}
