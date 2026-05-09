package de.dafuqs.spectrum.blocks.energy;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.events.*;
import de.dafuqs.spectrum.events.listeners.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.phys.*;
import javax.annotation.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class CrystalApothecaryBlockEntity extends RandomizableContainerBlockEntity implements PlayerOwnedWithName, BlockPosEventQueue.Callback<BlockPosEventQueue.Entry> {
	
	private static final int RANGE = 12;
	private static final ItemStack HARVEST_ITEMSTACK = ItemStack.EMPTY;
	
	private final BlockPosEventQueue blockPosEventTransferListener;
	protected long compensationWorldTime;
	private NonNullList<ItemStack> inventory;
	private boolean listenerPaused;
	private UUID ownerUUID;
	private String ownerName;
	
	public CrystalApothecaryBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.CRYSTAL_APOTHECARY.get(), blockPos, blockState);
		this.blockPosEventTransferListener = new BlockPosEventQueue(new BlockPositionSource(this.worldPosition), RANGE, this);
		this.inventory = NonNullList.withSize(27, ItemStack.EMPTY);
		this.listenerPaused = false;
		this.compensationWorldTime = -1;
	}
	
	@SuppressWarnings("unused")
	public static void tick(Level world, BlockPos pos, BlockState state, CrystalApothecaryBlockEntity blockEntity) {
		if (!world.isClientSide) {
			blockEntity.blockPosEventTransferListener.tick(world);
			if (world.getGameTime() % 1000 == 0) {
				blockEntity.listenerPaused = false; // try to reset from time to time, to search for new clusters, even if full
			}
			if (blockEntity.compensationWorldTime > 0) {
				long compensationTicks = world.getGameTime() - blockEntity.compensationWorldTime;
				if (compensationTicks > 1200) { // only compensate if the time gap is at least 1 minute (lag)
					compensateGemstoneClusterDropsForUnloadedTicks(world, pos, blockEntity, compensationTicks);
				}
				blockEntity.compensationWorldTime = -1;
			}
		}
	}
	
	/**
	 * Scans the surrounding area for blocks in COMPENSATION_MAP
	 * and puts items into it's inventory, simulating it working for
	 * a specific amount of ticks, like when getting loaded after being
	 * unloaded after some time
	 * This function works with estimates, guessing how much time has passed and how many clusters would have grown
	 */
	private static void compensateGemstoneClusterDropsForUnloadedTicks(Level world, BlockPos blockPos, CrystalApothecaryBlockEntity blockEntity, long ticksToCompensate) {
		Map<Block, Integer> matches = new HashMap<>();
		
		// search for blocks in working range and sum them up
		Collection<Block> compensationBlocks = CrystalApothecarySimulationsDataLoader.COMPENSATIONS.keySet();
		
		streamAffectedBlocks(blockPos).forEach(currPos -> {
			BlockState state = world.getBlockState(currPos);
			Block block = state.getBlock();
			if (compensationBlocks.contains(block)) {
				int validBlocks = countValidGemstoneClusterBlocksAroundBlockPos(world, currPos, CrystalApothecarySimulationsDataLoader.COMPENSATIONS.get(block).validNeighbors());
				if (matches.containsKey(block)) {
					matches.put(block, matches.get(block) + validBlocks);
				} else {
					matches.put(block, validBlocks);
				}
			}
		});
		
		List<ItemStack> needsAwarding = new ArrayList<>();
		int totalNeedsAwarding = 0;
		
		// for each of those blocks generate some loot
		double gameRuleTickModifier = world.getGameRules().getRule(GameRules.RULE_RANDOMTICKING).get() / 3.0;
		for (Map.Entry<Block, Integer> match : matches.entrySet()) {
			CrystalApothecarySimulationsDataLoader.SimulatedBlockGrowthEntry drop = CrystalApothecarySimulationsDataLoader.COMPENSATIONS.get(match.getKey());
			
			int compensatedItemCount = (int) (drop.compensatedStack().getCount() * match.getValue() * gameRuleTickModifier * ticksToCompensate) / drop.ticksForCompensationLootPerValidNeighbor();
			compensatedItemCount *= (int) (0.8 + world.random.nextFloat() * 0.4);
			if (compensatedItemCount > 0) {
				ItemStack compensatedStack = drop.compensatedStack().copy();
				compensatedStack.setCount(compensatedItemCount);
				needsAwarding.add(compensatedStack);
				totalNeedsAwarding += compensatedItemCount;
			}
		}
		
		if (needsAwarding.size() <= 1) {
			for (ItemStack awardStack : needsAwarding) {
				InventoryHelper.smartAddToInventory(awardStack, blockEntity, null);
			}
			return;
		}
		
		for (int slot = 0; slot < blockEntity.getContainerSize(); slot++) {
			if (totalNeedsAwarding <= 0) {
				break;
			}
			ItemStack currentStack = blockEntity.getItem(slot);
			if (currentStack.isEmpty()) {
				int selector = world.random.nextInt(totalNeedsAwarding);
				int acc = 0;
				for (ItemStack awardStack : needsAwarding) {
					acc += awardStack.getCount();
					if (selector < acc) {
						int maxCount = Math.min(blockEntity.getMaxStackSize(), awardStack.getMaxStackSize());
						int toAward = Math.min(maxCount, awardStack.getCount());
						ItemStack newStack = awardStack.copy();
						newStack.setCount(toAward);
						awardStack.shrink(toAward);
						blockEntity.setItem(slot, newStack);
						totalNeedsAwarding -= toAward;
						break;
					}
				}
			} else {
				int maxCount = Math.min(blockEntity.getMaxStackSize(), currentStack.getMaxStackSize());
				if (currentStack.getCount() > maxCount) {
					continue;
				}
				int wantsAdded = maxCount - currentStack.getCount();
				for (ItemStack awardStack : needsAwarding) {
					if (wantsAdded <= 0) {
						break;
					}
					if (ItemStack.isSameItemSameComponents(currentStack, awardStack)) {
						int canAdd = Math.min(wantsAdded, awardStack.getCount());
						awardStack.shrink(canAdd);
						currentStack.grow(canAdd);
						totalNeedsAwarding -= canAdd;
						wantsAdded -= canAdd;
					}
				}
			}
		}
	}
	
	public static int countValidGemstoneClusterBlocksAroundBlockPos(Level world, BlockPos blockPos, Collection<Block> allowedBlocks) {
		Set<BlockPos> count = new TreeSet<>();
		for (Direction direction : Direction.values()) {
			BlockState offsetState = world.getBlockState(blockPos.relative(direction));
			if (offsetState.isAir() || offsetState.getBlock() == Blocks.WATER || allowedBlocks.contains(offsetState.getBlock())) {
				count.add(blockPos.relative(direction));
			}
		}
		return count.size();
	}
	
	@Override
	protected Component getDefaultName() {
		if (hasOwner()) {
			return Component.translatable("block.spectrum.crystal_apothecary.owner", this.ownerName);
		} else {
			return Component.translatable("block.spectrum.crystal_apothecary");
		}
	}
	
	public BlockPosEventQueue getEventListener() {
		return this.blockPosEventTransferListener;
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(nbt)) {
			ContainerHelper.loadAllItems(nbt, this.inventory, registryLookup);
		}
		this.ownerUUID = PlayerOwnedWithName.readOwnerUUID(nbt);
		if (nbt.contains("ListenerPaused")) {
			this.listenerPaused = nbt.getBoolean("ListenerPaused");
		}
		this.ownerName = PlayerOwnedWithName.readOwnerName(nbt);
		if (nbt.contains("LastWorldTime")) {
			this.compensationWorldTime = nbt.getLong("LastWorldTime");
		}
	}
	
	@Override
	protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		if (!this.trySaveLootTable(nbt)) {
			ContainerHelper.saveAllItems(nbt, this.inventory, registryLookup);
		}
		nbt.putBoolean("ListenerPaused", this.listenerPaused);
		if (this.getLevel() != null) {
			nbt.putLong("LastWorldTime", this.getLevel().getGameTime());
		}
		PlayerOwned.writeOwnerUUID(nbt, this.ownerUUID);
		PlayerOwnedWithName.writeOwnerName(nbt, this.ownerName);
	}
	
	@Override
	public int getContainerSize() {
		return 27;
	}
	
	@Override
	public void setItem(int slot, ItemStack stack) {
		if (stack.isEmpty()) {
			this.listenerPaused = false;
			harvestExistingClusters();
		}
		super.setItem(slot, stack);
	}
	
	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.inventory;
	}
	
	@Override
	protected void setItems(NonNullList<ItemStack> list) {
		this.inventory = list;
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return GenericSpectrumContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this, ScreenBackgroundVariant.MIDGAME);
	}
	
	@Override
	public boolean canAcceptEvent(Level world, GameEventListener listener, GameEvent.ListenerInfo message, Vec3 sourcePos) {
		return message.gameEvent() == SpectrumGameEvents.BLOCK_CHANGED && !this.listenerPaused && message.context().affectedState().is(SpectrumBlockTags.CRYSTAL_APOTHECARY_HARVESTABLE);
	}
	
	@Override
	public void triggerEvent(Level world, GameEventListener listener, BlockPosEventQueue.Entry entry) {
		if (listener instanceof BlockPosEventQueue && this.getLevel() != null) {
			BlockPos eventPos = entry.eventSourceBlockPos();
			BlockState eventState = world.getBlockState(eventPos);
			if (eventState.is(SpectrumBlockTags.CRYSTAL_APOTHECARY_HARVESTABLE)) {
				// harvest
				LootParams.Builder builder = new LootParams.Builder((ServerLevel) world)
						.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(eventPos))
						.withParameter(LootContextParams.TOOL, HARVEST_ITEMSTACK)
						.withOptionalParameter(LootContextParams.THIS_ENTITY, getOwnerIfOnline(world))
						.withOptionalParameter(LootContextParams.BLOCK_ENTITY, eventState.hasBlockEntity() ? this.getLevel().getBlockEntity(eventPos) : null);
				
				List<ItemStack> drops = eventState.getDrops(builder);
				boolean anyDropsUsed = drops.isEmpty();
				for (ItemStack drop : drops) {
					if (hasOwner()) {
						Player owner = getOwnerIfOnline(world);
						if (owner instanceof ServerPlayer serverPlayerEntity) {
							SpectrumAdvancementCriteria.CRYSTAL_APOTHECARY_COLLECTING.trigger(serverPlayerEntity, drop);
						}
					}
					ItemStack remainingStack = InventoryHelper.smartAddToInventory(drop, this, null);
					if (remainingStack.isEmpty() || drop.getCount() != remainingStack.getCount()) {
						anyDropsUsed = true;
					}
					// remaining items are voided to not cause lag
				}
				
				if (anyDropsUsed) {
					world.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, eventPos, Block.getId(eventState)); // block break particles & sound
					if (eventState.getBlock() instanceof SimpleWaterloggedBlock && eventState.getValue(BlockStateProperties.WATERLOGGED)) {
						world.setBlockAndUpdate(eventPos, Blocks.WATER.defaultBlockState());
					} else {
						world.setBlockAndUpdate(eventPos, Blocks.AIR.defaultBlockState());
					}
				} else {
					this.listenerPaused = true;
				}
			}
		}
	}
	
	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public String getOwnerName() {
		return this.ownerName;
	}
	
	@Override
	public void setOwner(Player playerEntity) {
		this.ownerUUID = playerEntity.getUUID();
		this.ownerName = playerEntity.getName().getString();
		setChanged();
	}
	
	protected void harvestExistingClusters() {
		if (level instanceof ServerLevel serverWorld) {
			streamAffectedBlocks(this.worldPosition).forEach(currPos -> {
				if (level.getBlockState(currPos).is(SpectrumBlockTags.CRYSTAL_APOTHECARY_HARVESTABLE)) {
					blockPosEventTransferListener.acceptEvent(serverWorld,
							new GameEvent.ListenerInfo(SpectrumGameEvents.BLOCK_CHANGED, Vec3.atCenterOf(currPos), GameEvent.Context.of(level.getBlockState(currPos)),
									blockPosEventTransferListener, Vec3.atCenterOf(worldPosition)), Vec3.atCenterOf(worldPosition));
				}
			});
		}
	}
	
	private static Stream<BlockPos> streamAffectedBlocks(BlockPos worldPosition) {
		return BlockPos.withinManhattanStream(worldPosition, RANGE, RANGE, RANGE)
				.filter(blockPos -> blockPos.closerThan(worldPosition, RANGE));
	}
	
}
