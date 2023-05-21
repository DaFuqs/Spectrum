package de.dafuqs.spectrum.blocks.energy;

import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.events.*;
import de.dafuqs.spectrum.events.listeners.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.interfaces.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.loot.context.*;
import net.minecraft.nbt.*;
import net.minecraft.screen.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.state.property.Properties;
import net.minecraft.text.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import net.minecraft.world.event.listener.*;

import java.util.*;

public class CrystalApothecaryBlockEntity extends LootableContainerBlockEntity implements PlayerOwnedWithName, BlockPosEventQueue.Callback {
	
	private static final int RANGE = 12;
	private static final ItemStack HARVEST_ITEMSTACK = ItemStack.EMPTY;
	
	private final BlockPosEventQueue blockPosEventTransferListener;
	protected long compensationWorldTime;
	private DefaultedList<ItemStack> inventory;
	private boolean listenerPaused;
	private UUID ownerUUID;
	private String ownerName;
	
	public CrystalApothecaryBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.CRYSTAL_APOTHECARY, blockPos, blockState);
		this.blockPosEventTransferListener = new BlockPosEventQueue(new BlockPositionSource(this.pos), RANGE, this);
		this.inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
		this.listenerPaused = false;
		this.compensationWorldTime = -1;
	}
	
	public static void tick(World world, BlockPos pos, BlockState state, CrystalApothecaryBlockEntity blockEntity) {
		if (!world.isClient) {
			blockEntity.blockPosEventTransferListener.tick(world);
			if (world.getTime() % 1000 == 0) {
				blockEntity.listenerPaused = false; // try to reset from time to time, to search for new clusters, even if full
			}
			if (blockEntity.compensationWorldTime > 0) {
				long compensationTicks = world.getTime() - blockEntity.compensationWorldTime;
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
	private static void compensateGemstoneClusterDropsForUnloadedTicks(World world, BlockPos blockPos, CrystalApothecaryBlockEntity blockEntity, long ticksToCompensate) {
		Map<Block, Integer> matches = new HashMap<>();
		
		// search for blocks in working range and sum them up
		Collection<Block> compensationBlocks = CrystalApothecarySimulationsDataLoader.COMPENSATIONS.keySet();
		for (BlockPos pos : BlockPos.iterateOutwards(blockPos, RANGE, RANGE, RANGE)) {
			BlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			if (compensationBlocks.contains(block)) {
				int validBlocks = countValidGemstoneClusterBlocksAroundBlockPos(world, pos, CrystalApothecarySimulationsDataLoader.COMPENSATIONS.get(block).validNeighbors());
				if (matches.containsKey(block)) {
					matches.put(block, matches.get(block) + validBlocks);
				} else {
					matches.put(block, validBlocks);
				}
			}
		}
		
		// for each of those blocks generate some loot
		double gameRuleTickModifier = world.getGameRules().get(GameRules.RANDOM_TICK_SPEED).get() / 3.0;
		for (Map.Entry<Block, Integer> match : matches.entrySet()) {
			CrystalApothecarySimulationsDataLoader.SimulatedBlockGrowthEntry drop = CrystalApothecarySimulationsDataLoader.COMPENSATIONS.get(match.getKey());
			
			int compensatedItemCount = (int) (drop.compensatedStack().getCount() * match.getValue() * gameRuleTickModifier * ticksToCompensate) / drop.ticksForCompensationLootPerValidNeighbor();
			compensatedItemCount *= 0.8 + world.random.nextFloat() * 0.4;
			if (compensatedItemCount > 0) {
				ItemStack compensatedStack = drop.compensatedStack().copy();
				compensatedStack.setCount(compensatedItemCount);
				
				ItemStack remainingStack = InventoryHelper.smartAddToInventory(compensatedStack, blockEntity, null);
				if (!remainingStack.isEmpty()) {
					break; // overflow will be voided
				}
			}
		}
	}
	
	public static int countValidGemstoneClusterBlocksAroundBlockPos(World world, BlockPos blockPos, Collection<Block> allowedBlocks) {
		int count = 0;
		for (Direction direction : Direction.values()) {
			BlockState offsetState = world.getBlockState(blockPos.offset(direction));
			if (offsetState.isAir() || offsetState.getBlock() == Blocks.WATER || allowedBlocks.contains(offsetState.getBlock())) {
				count++;
			}
		}
		return count;
	}
	
	@Override
	protected Text getContainerName() {
		if (hasOwner()) {
			return Text.translatable("block.spectrum.crystal_apothecary").append(Text.translatable("container.spectrum.owned_by_player", this.ownerName));
		} else {
			return Text.translatable("block.spectrum.crystal_apothecary");
		}
	}
	
	public BlockPosEventQueue getEventListener() {
		return this.blockPosEventTransferListener;
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		if (!this.deserializeLootTable(nbt)) {
			Inventories.readNbt(nbt, this.inventory);
		}
		if (nbt.contains("OwnerUUID")) {
			this.ownerUUID = nbt.getUuid("OwnerUUID");
		} else {
			this.ownerUUID = null;
		}
		if (nbt.contains("ListenerPaused")) {
			this.listenerPaused = nbt.getBoolean("ListenerPaused");
		}
		if (nbt.contains("OwnerName")) {
			this.ownerName = nbt.getString("OwnerName");
		} else {
			this.ownerName = null;
		}
		if (nbt.contains("LastWorldTime")) {
			this.compensationWorldTime = nbt.getLong("LastWorldTime");
		}
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (!this.serializeLootTable(nbt)) {
			Inventories.writeNbt(nbt, this.inventory);
		}
		nbt.putBoolean("ListenerPaused", this.listenerPaused);
		if (this.world != null) {
			nbt.putLong("LastWorldTime", this.world.getTime());
		}
		if (this.ownerUUID != null) {
			nbt.putUuid("OwnerUUID", this.ownerUUID);
		}
		if (this.ownerName != null) {
			nbt.putString("OwnerName", this.ownerName);
		}
	}
	
	@Override
	public int size() {
		return 27;
	}
	
	@Override
	public void setStack(int slot, ItemStack stack) {
		if (stack.isEmpty() && this.listenerPaused) {
			this.listenerPaused = false;
			harvestExistingClusters();
		}
		super.setStack(slot, stack);
	}
	
	@Override
	protected DefaultedList<ItemStack> getInvStackList() {
		return this.inventory;
	}
	
	@Override
	protected void setInvStackList(DefaultedList<ItemStack> list) {
		this.inventory = list;
	}
	
	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return GenericSpectrumContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this, ProgressionStage.MIDGAME);
	}
	
	@Override
	public boolean canAcceptEvent(World world, GameEventListener listener, GameEvent.Message message, Vec3d sourcePos) {
		return message.getEvent() == SpectrumGameEvents.BLOCK_CHANGED && !this.listenerPaused && message.getEmitter().affectedState().isIn(SpectrumBlockTags.CRYSTAL_APOTHECARY_HARVESTABLE);
	}
	
	@Override
	public void triggerEvent(World world, GameEventListener listener, Object entry) {
		if (listener instanceof BlockPosEventQueue && this.world != null) {
			BlockPos eventPos = ((BlockPosEventQueue.EventEntry) entry).eventSourceBlockPos;
			BlockState eventState = world.getBlockState(eventPos);
			if (eventState.isIn(SpectrumBlockTags.CRYSTAL_APOTHECARY_HARVESTABLE)) {
				// harvest
				LootContext.Builder builder = (new LootContext.Builder((ServerWorld) this.world))
						.random(this.world.random)
						.parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(eventPos))
						.parameter(LootContextParameters.TOOL, HARVEST_ITEMSTACK)
						.optionalParameter(LootContextParameters.THIS_ENTITY, getOwnerIfOnline())
						.optionalParameter(LootContextParameters.BLOCK_ENTITY, eventState.hasBlockEntity() ? this.world.getBlockEntity(eventPos) : null);
				
				List<ItemStack> drops = eventState.getDroppedStacks(builder);
				boolean anyDropsUsed = drops.size() == 0;
				for (ItemStack drop : drops) {
					if (hasOwner()) {
						PlayerEntity owner = getOwnerIfOnline();
						if (owner instanceof ServerPlayerEntity serverPlayerEntity) {
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
					world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, eventPos, Block.getRawIdFromState(eventState)); // block break particles & sound
					if (eventState.getBlock() instanceof Waterloggable && eventState.get(Properties.WATERLOGGED)) {
						world.setBlockState(eventPos, Blocks.WATER.getDefaultState());
					} else {
						world.setBlockState(eventPos, Blocks.AIR.getDefaultState());
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
	public void setOwner(PlayerEntity playerEntity) {
		this.ownerUUID = playerEntity.getUuid();
		this.ownerName = playerEntity.getName().getString();
	}
	
	public void harvestExistingClusters() {
		if (world instanceof ServerWorld serverWorld) {
			for (BlockPos currPos : BlockPos.iterateOutwards(this.pos, RANGE, RANGE, RANGE)) {
				if (world.getBlockState(currPos).isIn(SpectrumBlockTags.CRYSTAL_APOTHECARY_HARVESTABLE)) {
					this.blockPosEventTransferListener.acceptEvent(serverWorld,
							new GameEvent.Message(SpectrumGameEvents.BLOCK_CHANGED, Vec3d.ofCenter(currPos), GameEvent.Emitter.of(world.getBlockState(currPos)),
									this.blockPosEventTransferListener, Vec3d.ofCenter(this.pos)), Vec3d.ofCenter(this.pos));
				}
			}
		}
	}
}
