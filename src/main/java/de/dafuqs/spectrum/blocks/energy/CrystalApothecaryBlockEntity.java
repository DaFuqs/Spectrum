package de.dafuqs.spectrum.blocks.energy;

import de.dafuqs.spectrum.enums.ProgressionStage;
import de.dafuqs.spectrum.events.QueuedBlockPosEventTransferListener;
import de.dafuqs.spectrum.events.SpectrumGameEvents;
import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.interfaces.PlayerOwnedWithName;
import de.dafuqs.spectrum.inventories.GenericSpectrumContainerScreenHandler;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventListener;

import java.util.*;

public class CrystalApothecaryBlockEntity extends LootableContainerBlockEntity implements PlayerOwnedWithName, QueuedBlockPosEventTransferListener.Callback {
	
	public static final Map<BlockState, Pair<Item, Integer>> UNLOADED_COMPENSATION_MAP = new HashMap<>() {{
		put(Blocks.BUDDING_AMETHYST.getDefaultState(), new Pair<>(Items.AMETHYST_SHARD, 2));
		put(SpectrumBlocks.BUDDING_TOPAZ.getDefaultState(), new Pair<>(SpectrumItems.TOPAZ_SHARD, 2));
		put(SpectrumBlocks.BUDDING_CITRINE.getDefaultState(), new Pair<>(SpectrumItems.CITRINE_SHARD, 2));
		put(SpectrumBlocks.BUDDING_ONYX.getDefaultState(), new Pair<>(SpectrumItems.ONYX_SHARD, 2));
		put(SpectrumBlocks.BUDDING_MOONSTONE.getDefaultState(), new Pair<>(SpectrumItems.MOONSTONE_SHARD, 2));
	}};
	
	private static final int RANGE = 12;
	private final QueuedBlockPosEventTransferListener blockPosEventTransferListener;
	private DefaultedList<ItemStack> inventory;
	private boolean listenerPaused;
	
	private UUID ownerUUID;
	private String ownerName;
	
	protected long compensationWorldTime;
	
	public CrystalApothecaryBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntityRegistry.CRYSTAL_APOTHECARY, blockPos, blockState);
		this.blockPosEventTransferListener = new QueuedBlockPosEventTransferListener(new BlockPositionSource(this.pos), RANGE, this);
		this.inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
		this.listenerPaused = false;
		this.compensationWorldTime = -1;
	}
	
	protected Text getContainerName() {
		if(hasOwner()) {
			return new TranslatableText("block.spectrum.crystal_apothecary").append(new TranslatableText("container.spectrum.owned_by_player", this.ownerName));
		} else {
			return new TranslatableText("block.spectrum.crystal_apothecary");
		}
	}
	
	public QueuedBlockPosEventTransferListener getEventListener() {
		return this.blockPosEventTransferListener;
	}
	
	public static void tick(World world, BlockPos pos, BlockState state, CrystalApothecaryBlockEntity blockEntity) {
		if (!world.isClient) {
			blockEntity.blockPosEventTransferListener.tick(world);
			if(world.getTime() % 1000 == 0) {
				blockEntity.listenerPaused = false; // try to reset from time to time, to search for new clusters, even if full
			}
			if(blockEntity.compensationWorldTime > 0) {
				long compensationTicks = world.getTime() - blockEntity.compensationWorldTime;
				if(compensationTicks > 1200) { // only compensate if the time gap is at least 1 minute (lag)
					compensateGemstoneClusterDropsForUnloadedTicks(world, pos, blockEntity, compensationTicks);
				}
				blockEntity.compensationWorldTime = -1;
			}
		}
	}
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		if (!this.deserializeLootTable(nbt)) {
			Inventories.readNbt(nbt, this.inventory);
		}
		if(nbt.contains("OwnerUUID")) {
			this.ownerUUID = nbt.getUuid("OwnerUUID");
		} else {
			this.ownerUUID = null;
		}
		if(nbt.contains("ListenerPaused")) {
			this.listenerPaused = nbt.getBoolean("ListenerPaused");
		}
		if(nbt.contains("OwnerName")) {
			this.ownerName = nbt.getString("OwnerName");
		} else {
			this.ownerName = null;
		}
		if(nbt.contains("LastWorldTime")) {
			this.compensationWorldTime = nbt.getLong("LastWorldTime");
		}
	}
	
	/**
	 * Scans the surrounding area for blocks in COMPENSATION_MAP
	 * and puts items into it's inventory, simulating it working for
	 * a specific amount of ticks, like when getting loaded after being
	 * unloaded after some time
	 *
	 * To not use too much load this function works with estimates,
	 * guessing how much time has passed and how many clusters would have grown
	 *
	 * There are /gamerule randomTickTime random ticks in each 16*16*16 cube per game tick (default: 3)
	 * When a budding block it ticked, there is a 20 % chance to choose a random direction and check if a bud can grow/advance at that pos
	 * Vanilla's and Spectrum's Buds all have 4 growth stages to get fully grown
	 *
	 * All this results in
	 * (<randomTickSpeed> / 16*16*16) * empty_blocks_next_to_budding_blocks * grow_chance * (1 / growth_stages_count)
	 * "grown" clusters per tick.
	 * Take that times the amount of ticks to compensate and take it times the amount of dropped items per each theoretically grown cluster
	 * Finally we add a bit of randomness.
	 *
	 * @param ticksToCompensate The # of ticks to simulate compensation for
	 */
	private static void compensateGemstoneClusterDropsForUnloadedTicks(World world,  BlockPos blockPos, CrystalApothecaryBlockEntity blockEntity, long ticksToCompensate) {
		Map<BlockState, Integer> matches = new HashMap<>();
		
		// search for blocks in working range
		Set<BlockState> compensationBlocks = UNLOADED_COMPENSATION_MAP.keySet();
		for(BlockPos pos : BlockPos.iterateOutwards(blockPos, RANGE, RANGE, RANGE)) {
			BlockState state = world.getBlockState(pos);
			if(compensationBlocks.contains(state)) {
				int validBlocks = countValidGemstoneClusterBlocksAroundBlockPos(world, pos);
				if(matches.containsKey(state)) {
					matches.put(state, matches.get(state) + validBlocks);
				} else {
					matches.put(state, validBlocks);
				}
			}
		}
		
		// for each of those blocks generate some loot
		// (<randomTickSpeed> / 16*16*16) * 20 % * 1/4
		float theoreticallyGrownClustersPerBlock = (world.getGameRules().get(GameRules.RANDOM_TICK_SPEED).get() / 4096F) * 0.05F * ticksToCompensate;
		for(Map.Entry<BlockState, Integer> match : matches.entrySet()) {
			Pair<Item, Integer> drop = UNLOADED_COMPENSATION_MAP.get(match.getKey());
			float theoreticallyGrownClusters = theoreticallyGrownClustersPerBlock * match.getValue();
			float compensatedItemsCount = theoreticallyGrownClusters * drop.getRight() * (0.8F + world.random.nextFloat() * 0.4F);
			if(compensatedItemsCount >= 1) {
				ItemStack compensatedStack = drop.getLeft().getDefaultStack();
				compensatedStack.setCount((int) compensatedItemsCount);
				ItemStack remainingStack = InventoryHelper.smartAddToInventory(compensatedStack, blockEntity, null);
				if(!remainingStack.isEmpty()) {
					break; // overflow will be voided
				}
			}
		}
	}
	
	public static int countValidGemstoneClusterBlocksAroundBlockPos(World world, BlockPos blockPos) {
		int count = 0;
		for(Direction direction : Direction.values()) {
			BlockState offsetState = world.getBlockState(blockPos.offset(direction));
			if(offsetState.isAir() || offsetState.getBlock() == Blocks.WATER || offsetState.isIn(SpectrumBlockTags.GEMSTONE_BUDS)) {
				count++;
			}
		}
		return count;
	}
	
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (!this.serializeLootTable(nbt)) {
			Inventories.writeNbt(nbt, this.inventory);
		}
		nbt.putBoolean("ListenerPaused", this.listenerPaused);
		if(this.world != null) {
			nbt.putLong("LastWorldTime", this.world.getTime());
		}
		if(this.ownerUUID != null) {
			nbt.putUuid("OwnerUUID", this.ownerUUID);
		}
		if(this.ownerName != null) {
			nbt.putString("OwnerName", this.ownerName);
		}
	}
	
	@Override
	public int size() {
		return 27;
	}
	
	@Override
	public void setStack(int slot, ItemStack stack) {
		if(stack.isEmpty() && this.listenerPaused) {
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
	public boolean acceptsEvent(World world, GameEventListener listener, BlockPos pos, GameEvent event, BlockPos sourcePos) {
		return event == SpectrumGameEvents.CRYSTAL_APOTHECARY_HARVESTABLE_GROWN && !this.listenerPaused;
	}
	
	@Override
	public void triggerEvent(World world, GameEventListener listener, Object entry) {
		if(listener instanceof QueuedBlockPosEventTransferListener && this.world != null) {
			BlockPos eventPos = ((QueuedBlockPosEventTransferListener.BlockPosEventEntry) entry).eventSourceBlockPos;
			BlockState eventState = world.getBlockState(eventPos);
			if(eventState.isIn(SpectrumBlockTags.CRYSTAL_APOTHECARY_HARVESTABLE)) {
				// harvest
				BlockEntity blockEntity = eventState.hasBlockEntity() ? this.world.getBlockEntity(eventPos) : null;
				LootContext.Builder builder = (new LootContext.Builder((ServerWorld)this.world))
						.random(this.world.random)
						.parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(eventPos))
						.parameter(LootContextParameters.TOOL, SpectrumItems.FORTUNE_PICKAXE.getDefaultStack())
						.optionalParameter(LootContextParameters.BLOCK_ENTITY, blockEntity);
				
				List<ItemStack> drops = eventState.getDroppedStacks(builder);
				boolean anyDropsUsed = drops.size() == 0;
				for(ItemStack drop : drops) {
					if(hasOwner()) {
						PlayerEntity owner = getPlayerEntityIfOnline(world);
						if(owner instanceof ServerPlayerEntity serverPlayerEntity) {
							SpectrumAdvancementCriteria.CRYSTAL_APOTHECARY_COLLECTING.trigger(serverPlayerEntity, drop);
						}
					}
					ItemStack remainingStack = InventoryHelper.smartAddToInventory(drop, this, null);
					if(remainingStack.isEmpty() || drop.getCount() != remainingStack.getCount()) {
						anyDropsUsed = true;
					}
					// remaining items are voided to not cause lag
				}
				
				if(anyDropsUsed) {
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
		this.ownerName = playerEntity.getName().asString();
	}
	
	public void harvestExistingClusters() {
		for (BlockPos currPos : BlockPos.iterateOutwards(this.pos, RANGE, RANGE, RANGE)) {
			if (world.getBlockState(currPos).isIn(SpectrumBlockTags.CRYSTAL_APOTHECARY_HARVESTABLE)) {
				this.blockPosEventTransferListener.acceptEvent(world, currPos, SpectrumGameEvents.CRYSTAL_APOTHECARY_HARVESTABLE_GROWN, this.pos);
			}
		}
	}
}
