package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.item.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import oshi.util.tuples.*;

import java.util.*;

public class ExchangeStaffItem extends BuildingStaffItem implements ExtendedEnchantable {

	public static final int INK_COST_PER_BLOCK = 5;
	public static final int CREATIVE_RANGE = 5;

	public ExchangeStaffItem(Settings settings) {
		super(settings);
	}

	// The range grows with the players' progression
	// this way the item is not overpowered at the start
	// but not useless at the end
	// this way the player does not need to craft 5 tiers
	// of staffs that each do basically feel the same
	public static int getRange(PlayerEntity playerEntity) {
		if (playerEntity == null || playerEntity.isCreative()) {
			return CREATIVE_RANGE;
		} else {
			Optional<PedestalRecipeTier> highestUnlockedRecipeTier = PedestalRecipeTier.getHighestUnlockedRecipeTier(playerEntity);
			if (highestUnlockedRecipeTier.isPresent()) {
				switch (highestUnlockedRecipeTier.get()) {
					case COMPLEX -> {
						return 5;
					}
					case ADVANCED -> {
						return 4;
					}
					default -> {
						return 3;
					}
				}
			} else {
				return 2;
			}
		}
	}

	@Override
	public boolean canInteractWith(BlockState state, BlockView world, BlockPos pos, PlayerEntity player) {
		return super.canInteractWith(state, world, pos, player) && state.getHardness(world, pos) < 20;
	}

	public static Optional<Block> getStoredBlock(@NotNull ItemStack exchangeStaffItemStack) {
		NbtCompound compound = exchangeStaffItemStack.getOrCreateNbt();
		if (compound.contains("TargetBlock")) {
			String targetBlockString = compound.getString("TargetBlock");
			Block targetBlock = Registries.BLOCK.get(new Identifier(targetBlockString));
			if (targetBlock != Blocks.AIR) {
				return Optional.of(targetBlock);
			}
		}
		return Optional.empty();
	}

	public static boolean exchange(World world, BlockPos pos, @NotNull PlayerEntity player, @NotNull Block targetBlock,
			ItemStack exchangeStaffItemStack, Direction side) {
		return exchange(world, pos, player, targetBlock, exchangeStaffItemStack, false, side);
	}

	public static boolean exchange(World world, BlockPos pos, @NotNull PlayerEntity player, @NotNull Block targetBlock,
			ItemStack exchangeStaffItemStack, boolean single, Direction side) {
		Triplet<Block, Item, Integer> replaceData = countSuitableReplacementItems(player, targetBlock, single,
				INK_COST_PER_BLOCK);

		long blocksToReplaceCount = replaceData.getC();
		if (blocksToReplaceCount == 0) {
			return false;
		}
		targetBlock = replaceData.getA();
		Item consumedItem = replaceData.getB();

		int range = getRange(player);
		List<BlockPos> targetPositions = BuildingHelper.getConnectedBlocks(world, pos, blocksToReplaceCount, range);
		if (targetPositions.isEmpty()) {
			return false;
		}

		int blocksReplaced = 0;
		if (!world.isClient) {
			List<ItemStack> stacks = new ArrayList<>();
			BlockState stateToPlace;
			for (BlockPos targetPosition : targetPositions) {

				// Require both place and break permissions in order to swap blocks
				if (!GenericClaimModsCompat.canModify(world, pos, player))
					continue;

				if (!player.isCreative()) {
					BlockState droppedStacks = world.getBlockState(targetPosition);
					stacks.addAll(Block.getDroppedStacks(droppedStacks, (ServerWorld) world, targetPosition,
							world.getBlockEntity(targetPosition), player, exchangeStaffItemStack));
				}
				world.setBlockState(targetPosition, Blocks.AIR.getDefaultState());

				stateToPlace = targetBlock.getPlacementState(new BuildingStaffPlacementContext(world, player,
						new BlockHitResult(Vec3d.ofBottomCenter(targetPosition), side, targetPosition, false)));
				if (stateToPlace != null && stateToPlace.canPlaceAt(world, targetPosition)) {
					if (blocksReplaced == 0) {
						world.playSound(null, player.getBlockPos(), stateToPlace.getSoundGroup().getPlaceSound(),
								SoundCategory.PLAYERS, stateToPlace.getSoundGroup().getVolume(),
								stateToPlace.getSoundGroup().getPitch());
					}

					if (!world.setBlockState(targetPosition, stateToPlace)) {
						ItemEntity itemEntity = new ItemEntity(world, targetPosition.getX(), targetPosition.getY(),
								targetPosition.getZ(), new ItemStack(consumedItem));
						itemEntity.setOwner(player.getUuid());
						itemEntity.resetPickupDelay();
						world.spawnEntity(itemEntity);
					}
				}

				blocksReplaced++;
			}

			if (!player.isCreative()) {
				InventoryHelper.removeFromInventoryWithRemainders(player,
						new ItemStack(consumedItem, targetPositions.size()));
				for (ItemStack stack : stacks) {
					player.getInventory().offerOrDrop(stack);
				}
				InkPowered.tryDrainEnergy(player, USED_COLOR, (long) targetPositions.size() * INK_COST_PER_BLOCK);
			}

		}

		return true;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		MinecraftClient client = MinecraftClient.getInstance();
		super.appendTooltip(stack, world, tooltip, context);
		addInkPoweredTooltip(tooltip);
		tooltip.add(Text.translatable("item.spectrum.exchanging_staff.tooltip.range", getRange(client.player))
				.formatted(Formatting.GRAY));

		Optional<Block> optionalBlock = getStoredBlock(stack);
		if (optionalBlock.isPresent()) {
			tooltip.add(
					Text.translatable("item.spectrum.exchanging_staff.tooltip.target", optionalBlock.get().getName())
							.formatted(Formatting.GRAY));
		}
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		PlayerEntity player = context.getPlayer();

		if (player == null) {
			return ActionResult.FAIL;
		}

		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState targetBlockState = world.getBlockState(pos);
		Block targetBlock = targetBlockState.getBlock();

		if (!canInteractWith(targetBlockState, context.getWorld(), context.getBlockPos(), context.getPlayer())) {
			world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.PLAYERS, 1.0F,
					1.0F);
			return ActionResult.FAIL;
		}

		ItemStack staffStack = context.getStack();

		if (player.isSneaking()) {
			// storing that block as target
			if (world instanceof ServerWorld serverWorld) {
				storeBlockAsTarget(staffStack, targetBlock);

				Direction side = context.getSide();
				Vec3d sourcePos = new Vec3d(context.getHitPos().getX() + side.getOffsetX() * 0.1,
						context.getHitPos().getY() + side.getOffsetY() * 0.1,
						context.getHitPos().getZ() + side.getOffsetZ() * 0.1);
				SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity(serverWorld, sourcePos,
						SpectrumParticleTypes.SHIMMERSTONE_SPARKLE_SMALL, 15, Vec3d.ZERO, new Vec3d(0.25, 0.25, 0.25));
				world.playSound(null, player.getBlockPos(), SpectrumSoundEvents.EXCHANGING_STAFF_SELECT,
						SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
			return ActionResult.success(world.isClient);
		} else {
			// exchanging
			Optional<Block> storedBlock = getStoredBlock(staffStack);
			if (storedBlock.isPresent()
					&& storedBlock.get() != targetBlock
					&& storedBlock.get().asItem() != Items.AIR
					&& exchange(world, pos, player, storedBlock.get(), staffStack, context.getSide())) {

				return ActionResult.success(world.isClient);
			}
		}

		world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.PLAYERS, 1.0F,
				1.0F);
		return ActionResult.FAIL;
	}

	public void storeBlockAsTarget(@NotNull ItemStack exchangeStaffItemStack, Block block) {
		NbtCompound compound = exchangeStaffItemStack.getOrCreateNbt();
		Identifier blockIdentifier = Registries.BLOCK.getId(block);
		compound.putString("TargetBlock", blockIdentifier.toString());
		exchangeStaffItemStack.setNbt(compound);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return stack.getCount() == 1;
	}

	@Override
	public boolean acceptsEnchantment(Enchantment enchantment) {
		return enchantment == Enchantments.FORTUNE || enchantment == Enchantments.SILK_TOUCH
				|| enchantment == SpectrumEnchantments.RESONANCE;
	}

	@Override
	public int getEnchantability() {
		return 3;
	}

	@Override
	public List<InkColor> getUsedColors() {
		return List.of(USED_COLOR);
	}

}
