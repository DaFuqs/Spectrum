package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.blocks.enchanter.EnchanterEnchantable;
import de.dafuqs.spectrum.enums.PedestalRecipeTier;
import de.dafuqs.spectrum.helpers.BuildingHelper;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Triplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeStaffItem extends BuildingStaffItem implements EnchanterEnchantable {
	
	public static final int CREATIVE_RANGE = 5;
	
	public ExchangeStaffItem(Settings settings) {
		super(settings);
	}
	
	// The range grows with the players' progression
	// this way the item is not overpowered at the start
	// but not useless at the end
	// this way the player does not need to craft 5 tiers
	// of placementStaffs that each do basically feel the same
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
	
	public static Optional<Block> getBlockTarget(@NotNull ItemStack exchangeStaffItemStack) {
		NbtCompound compound = exchangeStaffItemStack.getOrCreateNbt();
		if (compound.contains("TargetBlock")) {
			String targetBlockString = compound.getString("TargetBlock");
			Block targetBlock = Registry.BLOCK.get(new Identifier(targetBlockString));
			if (targetBlock != Blocks.AIR) {
				return Optional.of(targetBlock);
			}
		}
		return Optional.empty();
	}
	
	public static ActionResult exchange(World world, BlockPos pos, @NotNull PlayerEntity player, @NotNull Block targetBlock, ItemStack exchangeStaffItemStack) {
		return exchange(world, pos, player, targetBlock, exchangeStaffItemStack, false);
	}
	
	public static ActionResult exchange(World world, BlockPos pos, @NotNull PlayerEntity player, @NotNull Block targetBlock, ItemStack exchangeStaffItemStack, boolean single) {
		Item exchangedForBlockItem = targetBlock.asItem();
		BlockState targetBlockState = targetBlock.getDefaultState();
		BlockState placedBlockState = targetBlockState;
		
		int exchangedForBlockItemCount;
		if (player.isCreative()) {
			exchangedForBlockItemCount = Integer.MAX_VALUE;
		} else {
			Triplet<Block, Item, Integer> exchangeData = BuildingHelper.getBuildingItemCountInInventoryIncludingSimilars(player, targetBlock);
			if (targetBlock != exchangeData.getA()) {
				placedBlockState = exchangeData.getA().getDefaultState();
			}
			exchangedForBlockItem = exchangeData.getB();
			exchangedForBlockItemCount = exchangeData.getC();
		}
		
		if (single) {
			exchangedForBlockItemCount = Math.min(1, exchangedForBlockItemCount);
		}
		
		if (exchangedForBlockItemCount > 0) {
			int range = getRange(player);
			List<BlockPos> targetPositions = BuildingHelper.getConnectedBlocks(world, pos, exchangedForBlockItemCount, range);
			if (targetPositions.isEmpty()) {
				return ActionResult.FAIL;
			}
			
			int blocksReplaced = 0;
			if (!world.isClient) {
				List<ItemStack> stacks = new ArrayList<>();
				for (BlockPos targetPosition : targetPositions) {
					if (!player.isCreative()) {
						BlockState droppedStacks = world.getBlockState(targetPosition);
						stacks.addAll(Block.getDroppedStacks(droppedStacks, (ServerWorld) world, targetPosition, world.getBlockEntity(targetPosition), player, exchangeStaffItemStack));
					}
					world.setBlockState(targetPosition, Blocks.AIR.getDefaultState());
					if (targetBlockState.canPlaceAt(world, targetPosition)) {
						world.setBlockState(targetPosition, placedBlockState);
					} else {
						ItemEntity itemEntity = new ItemEntity(world, targetPosition.getX(), targetPosition.getY(), targetPosition.getZ(), new ItemStack(exchangedForBlockItem));
						itemEntity.setOwner(player.getUuid());
						itemEntity.resetPickupDelay();
						world.spawnEntity(itemEntity);
					}
					blocksReplaced++;
				}
				
				if (!player.isCreative()) {
					Item finalExchangedForBlockItem = exchangedForBlockItem;
					player.getInventory().remove(stack -> stack.getItem().equals(finalExchangedForBlockItem), targetPositions.size(), player.getInventory());
					for (ItemStack stack : stacks) {
						Support.givePlayer(player, stack);
					}
				}
				
				if (blocksReplaced > 0) {
					world.playSound(null, player.getBlockPos(), targetBlockState.getSoundGroup().getPlaceSound(), SoundCategory.PLAYERS, targetBlockState.getSoundGroup().getVolume(), targetBlockState.getSoundGroup().getPitch());
				}
			}
			
			return ActionResult.SUCCESS;
		} else {
			return ActionResult.FAIL;
		}
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.exchange_staff.tooltip.range", getRange(MinecraftClient.getInstance().player)).formatted(Formatting.GRAY));
		tooltip.add(new TranslatableText("item.spectrum.exchange_staff.tooltip.crouch").formatted(Formatting.GRAY));
		
		Optional<Block> optionalBlock = getBlockTarget(stack);
		if (optionalBlock.isPresent()) {
			tooltip.add(new TranslatableText("item.spectrum.exchange_staff.tooltip.target", optionalBlock.get().getName()).formatted(Formatting.GRAY));
		}
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		PlayerEntity player = context.getPlayer();
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState targetBlockState = world.getBlockState(pos);
		
		ActionResult result = ActionResult.FAIL;
		if ((player != null && player.isCreative()) || !isBlacklisted(targetBlockState)) {
			Block targetBlock = targetBlockState.getBlock();
			Item targetBlockItem = targetBlockState.getBlock().asItem();
			if (player != null && targetBlockItem != Items.AIR && context.getHand() == Hand.MAIN_HAND) {
				if (player.isSneaking()) {
					if (world instanceof ServerWorld serverWorld) {
						storeBlockAsTarget(context.getStack(), targetBlock);
						world.playSound(null, player.getBlockPos(), SpectrumSoundEvents.EXCHANGE_STAFF_SELECT, SoundCategory.PLAYERS, 1.0F, 1.0F);
						
						Direction side = context.getSide();
						Vec3d sourcePos = new Vec3d(context.getHitPos().getX() + side.getOffsetX() * 0.1, context.getHitPos().getY() + side.getOffsetY() * 0.1, context.getHitPos().getZ() + side.getOffsetZ() * 0.1);
						SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity(serverWorld, sourcePos, SpectrumParticleTypes.SPARKLESTONE_SPARKLE_SMALL, 15, new Vec3d(0, 0, 0), new Vec3d(0.25, 0.25, 0.25));
						result = ActionResult.CONSUME;
					} else {
						result = ActionResult.SUCCESS;
					}
				} else {
					Optional<Block> exchangeBlock = getBlockTarget(context.getStack());
					if (exchangeBlock.isPresent() && exchangeBlock.get().asItem() != Items.AIR && exchangeBlock.get() != targetBlock) {
						result = exchange(world, pos, player, exchangeBlock.get(), context.getStack());
					}
				}
			}
		}
		
		if (result == ActionResult.FAIL && player != null) {
			world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
		return result;
	}
	
	public void storeBlockAsTarget(@NotNull ItemStack exchangeStaffItemStack, Block block) {
		NbtCompound compound = exchangeStaffItemStack.getOrCreateNbt();
		Identifier blockIdentifier = Registry.BLOCK.getId(block);
		compound.putString("TargetBlock", blockIdentifier.toString());
		exchangeStaffItemStack.setNbt(compound);
	}
	
	@Override
	public boolean canAcceptEnchantment(Enchantment enchantment) {
		return enchantment == Enchantments.FORTUNE || enchantment == Enchantments.SILK_TOUCH || enchantment == SpectrumEnchantments.RESONANCE;
	}
	
	@Override
	public int getEnchantability() {
		return 3;
	}
	
}
