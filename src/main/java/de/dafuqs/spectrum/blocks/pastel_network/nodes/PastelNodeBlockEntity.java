package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import com.google.common.base.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.pastel.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.fabric.api.lookup.v1.block.*;
import net.fabricmc.fabric.api.screenhandler.v1.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.protocol.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.server.network.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import org.apache.commons.lang3.*;
import org.jetbrains.annotations.*;

import java.util.Optional;
import java.util.*;
import java.util.function.Predicate;

public class PastelNodeBlockEntity extends BlockEntity implements FilterConfigurable, ExtendedScreenHandlerFactory<FilterConfigurable.ExtendedData>, PastelUpgradeable {
	
	public static final int MAX_FILTER_SLOTS = 25;
	public static final int SLOTS_PER_ROW = 5;
	public static final int DEFAULT_FILTER_SLOT_ROWS = 1;
	public static final int RANGE = 12;
	
	@NotNull
	protected UUID nodeId = UUID.randomUUID();
	protected Optional<UUID> networkUUID = Optional.empty();
	protected Optional<PastelUpgradeSignature> outerRing, innerRing, redstoneRing;
	protected Optional<DyeColor> color = Optional.empty();
	
	// TODO: move these to ServerPastelNetwork?
	protected long lastTransferTick = 0;
	protected final long cachedRedstonePowerTick = 0;
	protected boolean cachedUnpowered = true;
	protected PastelNetwork.NodePriority priority = PastelNetwork.NodePriority.GENERIC;
	protected long itemCountUnderway = 0;
	
	
	// upgrade impl stuff
	protected boolean lit, triggerTransfer, triggered, waiting, lamp, sensor, updated;
	protected int transferCount = PastelTransmissionLogic.DEFAULT_MAX_TRANSFER_AMOUNT;
	protected int transferTime = PastelTransmissionLogic.DEFAULT_TRANSFER_TICKS_PER_NODE;
	protected int filterSlotRows = DEFAULT_FILTER_SLOT_ROWS;
	
	protected BlockApiCache<Storage<ItemVariant>, Direction> connectedStorageCache = null;
	protected Direction cachedDirection = null;

	protected Boolean isInitialized = false;
	
	private final List<ItemVariant> filterItems;
	float rotationTarget, crystalRotation, lastRotationTarget, heightTarget, crystalHeight, lastHeightTarget, alphaTarget, ringAlpha, lastAlphaTarget;
	long creationStamp = -1, interpTicks, interpLength = -1, spinTicks;
	private ConnectionState connectionState;
	
	public PastelNodeBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.PASTEL_NODE, blockPos, blockState);
		this.filterItems = NonNullList.withSize(MAX_FILTER_SLOTS, ItemVariant.blank());
		this.outerRing = Optional.empty();
		this.innerRing = Optional.empty();
		this.redstoneRing = Optional.empty();
	}
	
	public @Nullable Storage<ItemVariant> getConnectedStorage() {
		if (connectedStorageCache == null) {
			BlockState state = this.getBlockState();
			if (!(state.getBlock() instanceof PastelNodeBlock)) {
				return null;
			}
			cachedDirection = state.getValue(PastelNodeBlock.FACING);
			connectedStorageCache = BlockApiCache.create(ItemStorage.SIDED, (ServerLevel) level, this.getBlockPos().relative(cachedDirection.getOpposite()));
		}
		return connectedStorageCache.find(cachedDirection);
	}
	
	public static void tick(@NotNull Level world, BlockPos pos, BlockState state, PastelNodeBlockEntity node) {
		if (!node.isInitialized && !world.isClientSide()) { // kinda onLoad()?
			node.getServerNetwork().ifPresent(network -> network.initializeNode(node));
			node.isInitialized = true;
		}

		if (node.lamp && state.getValue(BlockStateProperties.LIT) != node.canTransfer()) {
			world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LIT, node.cachedUnpowered));
		}
		
		//Trigger transfer logic needs to be ticked here
		if (node.triggerTransfer) {
			var powered = world.hasNeighborSignal(pos);
			
			if (node.waiting && !powered) {
				node.waiting = false;
			}
			
			if (!node.triggered && !node.waiting && powered) {
				node.triggered = true;
			}
		}
		
		if (world.isClientSide()) {
			if (node.networkUUID.isEmpty()) {
				node.changeConnectionState(ConnectionState.DISCONNECTED);
				node.interpLength = 17;
			} else if (!node.canTransfer()) {
				node.changeConnectionState(ConnectionState.INACTIVE);
				node.interpLength = 21;
			} else if (node.spinTicks > 0) {
				node.changeConnectionState(ConnectionState.ACTIVE);
				node.interpLength = 17;
			} else {
				node.changeConnectionState(ConnectionState.CONNECTED);
				node.interpLength = 13;
			}
			
			if (node.interpTicks < node.interpLength)
				node.interpTicks++;
			
			if (node.spinTicks > 0)
				node.spinTicks--;
		} else if (!node.updated) {
			node.updateUpgrades();
			node.updated = true;
		}
	}
	
	public void changeConnectionState(ConnectionState connectionState) {
		if (this.connectionState != connectionState) {
			this.connectionState = connectionState;
			lastRotationTarget = crystalRotation;
			lastHeightTarget = crystalHeight;
			lastAlphaTarget = ringAlpha;
			interpTicks = 0;
		}
	}
	
	public Optional<PastelUpgradeSignature> getInnerRing() {
		return innerRing;
	}
	
	public Optional<PastelUpgradeSignature> getOuterRing() {
		return outerRing;
	}
	
	public Optional<PastelUpgradeSignature> getRedstoneRing() {
		return redstoneRing;
	}
	
	public PastelNetwork.NodePriority getPriority() {
		return priority;
	}
	
	// outer goes first, then inner, then redstone
	public boolean tryInteractRings(ItemStack item, PastelNodeType type) {
		var upgrade = SpectrumPastelUpgrades.of(item);
		
		if (upgrade.category.isRedstone()) {
			if (redstoneRing.isEmpty()) {
				redstoneRing = Optional.of(upgrade);
				return true;
			}
			
			return false;
		}
		
		if (outerRing.isEmpty() && type.hasOuterRing()) {
			outerRing = Optional.of(upgrade);
			return true;
		} else if (innerRing.isEmpty()) {
			innerRing = Optional.of(upgrade);
			return true;
		}
		
		return false;
	}
	
	// inverted order of adding them
	public ItemStack tryRemoveUpgrade() {
		var stack = ItemStack.EMPTY;
		
		if (redstoneRing.isPresent()) {
			stack = redstoneRing.get().upgradeItem.getDefaultInstance();
			redstoneRing = Optional.empty();
		} else if (innerRing.isPresent()) {
			stack = innerRing.get().upgradeItem.getDefaultInstance();
			innerRing = Optional.empty();
		} else if (outerRing.isPresent()) {
			stack = outerRing.get().upgradeItem.getDefaultInstance();
			outerRing = Optional.empty();
		}
		
		if (!stack.isEmpty()) {
			level.playLocalSound(worldPosition, SpectrumSoundEvents.SHATTER_LIGHT, SoundSource.BLOCKS, 0.25F, 0.9F + level.getRandom().nextFloat() * 0.2F, true);
			setChanged();
		}
		return stack;
	}
	
	public void updateUpgrades() {
		transferCount = PastelTransmissionLogic.DEFAULT_MAX_TRANSFER_AMOUNT;
		transferTime = PastelTransmissionLogic.DEFAULT_TRANSFER_TICKS_PER_NODE;
		var oldFilterSlotCount = filterSlotRows;
		filterSlotRows = DEFAULT_FILTER_SLOT_ROWS;
		triggerTransfer = false;
		lit = false;
		lamp = false;
		sensor = false;
		var oldPriority = priority;
		priority = PastelNetwork.NodePriority.GENERIC;
		
		//First one processed can't compound because it has nothing to compound on
		outerRing.ifPresent(r -> apply(r, Collections.emptyList()));
		innerRing.ifPresent(r -> apply(r, outerRing.map(List::of).orElse(Collections.emptyList())));
		redstoneRing.ifPresent(r -> apply(r, Collections.emptyList()));
		
		// Sanity
		transferCount = Math.max(transferCount, 1);
		transferTime = Mth.clamp(transferTime, 2, 100);
		filterSlotRows = Mth.clamp(filterSlotRows, 1, 5);
		
		if (lit && lamp) {
			lit = false;
		}
		
		if (level != null) {
			networkUUID.ifPresent(uuid -> ServerPastelNetworkManager.get((ServerLevel) level).getNetwork(uuid).ifPresent(n -> n.updateNodePriority(this, oldPriority)));
			if (getBlockState().getValue(BlockStateProperties.LIT) != lit)
				level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BlockStateProperties.LIT, lit));
		}
		
		if (filterSlotRows < oldFilterSlotCount) {
			for (int i = getDrawnSlots(); i < filterItems.size(); i++) {
				filterItems.set(i, ItemVariant.blank());
			}
		}
	}
	
	@Override
	public void notifySensor() {
		if (level != null) {
			var state = getBlockState();
			level.setBlockAndUpdate(worldPosition, state.setValue(BlockStateProperties.POWERED, true));
			if (!level.getBlockTicks().hasScheduledTick(worldPosition, state.getBlock())) {
				level.scheduleTick(worldPosition, state.getBlock(), 2);
			}
		}
	}
	
	public long getMaxTransferredAmount() {
		return transferCount;
	}
	
	public int getTransferTime() {
		return transferTime;
	}
	
	public float getRedstoneAlphaMult() {
		return redstoneRing.isPresent() ? 0.5F : 0.25F;
	}
	
	public boolean canTransfer() {
		var result = redstoneRing.map(r -> r.preProcessor
				.apply(new PastelUpgradeSignature.RedstoneContext(this, level, worldPosition, cachedUnpowered))).orElse(InteractionResult.PASS);
		
		if (result == InteractionResult.SUCCESS)
			return true;
		
		if (result == InteractionResult.FAIL)
			return false;
		
		long time = this.getLevel().getGameTime();
		if (time > this.cachedRedstonePowerTick && !getBlockState().getValue(PastelNodeBlock.REDSTONE_EMITTING)) {
			this.cachedUnpowered = level.getBestNeighborSignal(this.worldPosition) == 0;
		}
		
		boolean notPowered = redstoneRing.map(r -> {
			var post = r.postProcessor.apply(new PastelUpgradeSignature.RedstoneContext(this, level, worldPosition, cachedUnpowered));
			
			if (post == InteractionResult.SUCCESS)
				return true;
			
			if (post == InteractionResult.FAIL)
				return false;
			
			return cachedUnpowered;
		}).orElse(cachedUnpowered);
		
		var canTransfer = this.getLevel().getGameTime() > lastTransferTick;
		if (triggerTransfer) {
			return triggered && canTransfer;
		}
		
		return canTransfer && notPowered;
	}
	
	public void markTransferred() {
		if (triggerTransfer) {
			markTriggered();
		}
		
		this.lastTransferTick = level.getGameTime();
		this.setChanged();
	}
	
	@Override
	protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		
		this.nodeId = nbt.contains("NodeID") ? nbt.getUUID("NodeID") : UUID.randomUUID();
		this.networkUUID = nbt.contains("NetworkUUID") ? Optional.of(nbt.getUUID("NetworkUUID")) : Optional.empty();
		this.triggered = nbt.contains("Triggered") && nbt.getBoolean("Triggered");
		this.waiting = nbt.contains("Waiting") && nbt.getBoolean("Waiting");
		this.creationStamp = nbt.contains("creationStamp") ? nbt.getLong("creationStamp") : 0;
		this.lastTransferTick = nbt.contains("LastTransferTick", Tag.TAG_LONG) ? nbt.getLong("LastTransferTick") : 0;
		this.itemCountUnderway = nbt.contains("ItemCountUnderway", Tag.TAG_LONG) ? nbt.getLong("ItemCountUnderway") : 0;
		this.color = nbt.contains("ColorId", Tag.TAG_INT) ? Optional.of(DyeColor.byId(nbt.getInt("ColorId"))) : Optional.empty();
		this.outerRing = nbt.contains("OuterRing") ? Optional.ofNullable(SpectrumRegistries.PASTEL_UPGRADE.get(ResourceLocation.tryParse(nbt.getString("OuterRing")))) : Optional.empty();
		this.innerRing = nbt.contains("InnerRing") ? Optional.ofNullable(SpectrumRegistries.PASTEL_UPGRADE.get(ResourceLocation.tryParse(nbt.getString("InnerRing")))) : Optional.empty();
		this.redstoneRing = nbt.contains("RedstoneRing") ? Optional.ofNullable(SpectrumRegistries.PASTEL_UPGRADE.get(ResourceLocation.tryParse(nbt.getString("RedstoneRing")))) : Optional.empty();
		
		if (this.getNodeType().usesFilters()) {
			FilterConfigurable.readFilterNbt(nbt, this.filterItems);
		}
	}
	
	@Override
	protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		if (creationStamp != -1) {
			nbt.putLong("creationStamp", creationStamp);
		}
		if (this.networkUUID.isPresent()) {
			nbt.putUUID("NetworkUUID", this.networkUUID.get());
		}
		if (this.color.isPresent()) {
			nbt.putInt("ColorId", this.color.get().getId());
		}
		nbt.putUUID("NodeID", this.nodeId);
		nbt.putBoolean("Triggered", this.triggered);
		nbt.putBoolean("Waiting", this.waiting);
		nbt.putLong("LastTransferTick", this.lastTransferTick);
		nbt.putLong("ItemCountUnderway", this.itemCountUnderway);
		if (this.getNodeType().usesFilters()) {
			FilterConfigurable.writeFilterNbt(nbt, this.filterItems);
		}
		outerRing.ifPresent(r -> nbt.putString("OuterRing", SpectrumPastelUpgrades.toString(r)));
		innerRing.ifPresent(r -> nbt.putString("InnerRing", SpectrumPastelUpgrades.toString(r)));
		redstoneRing.ifPresent(r -> nbt.putString("RedstoneRing", SpectrumPastelUpgrades.toString(r)));
	}
	
	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
		Optional<ServerPastelNetwork> network = getServerNetwork();
		network.ifPresent(serverPastelNetwork -> PastelNetworkEdgeSyncPayload.send(serverPastelNetwork, worldPosition));
		
		CompoundTag nbtCompound = new CompoundTag();
		this.saveAdditional(nbtCompound, registryLookup);
		return nbtCompound;
	}
	
	// triggered when the chunk is unloaded, or the world quit
	@Override
	public void setRemoved() {
		super.setRemoved();
		if (!level.isClientSide()) {
			Pastel.getServerInstance().removeNode(this, NodeRemovalReason.UNLOADED);
		}
	}
	
	public @NotNull UUID getNodeId() {
		return nodeId;
	}
	
	public void onBroken() {
		if (level != null && !level.isClientSide) {
			Pastel.getServerInstance().removeNode(this, NodeRemovalReason.BROKEN);
		}
	}
	
	public PastelNodeType getNodeType() {
		if (this.getBlockState().getBlock() instanceof PastelNodeBlock pastelNodeBlock) {
			return pastelNodeBlock.pastelNodeType;
		}
		return PastelNodeType.CONNECTION;
	}
	
	public void setNetworkUUID(@Nullable UUID uuid) {
		this.networkUUID = Optional.ofNullable(uuid);
		if (this.getLevel() != null && !this.getLevel().isClientSide()) {
			this.setChanged();
			this.updateInClientWorld();
		}
	}
	
	public long getItemCountUnderway() {
		return this.itemCountUnderway;
	}
	
	public void addItemCountUnderway(long count) {
		this.itemCountUnderway += count;
		this.itemCountUnderway = Math.max(0, this.itemCountUnderway);
		this.setChanged();
	}
	
	// interaction methods
	public void updateInClientWorld() {
		((ServerLevel) level).getChunkSource().blockChanged(worldPosition);
	}
	
	@Override
	public List<ItemVariant> getItemFilters() {
		return this.filterItems;
	}
	
	@Override
	public void setFilterItem(int slot, ItemVariant item) {
		this.filterItems.set(slot, item);
	}
	
	public Predicate<ItemVariant> getTransferFilterTo(PastelNodeBlockEntity other) {
		if (this.getNodeType().usesFilters() && !this.hasEmptyFilter()) {
			if (other.getNodeType().usesFilters() && !other.hasEmptyFilter()) {
				// unionize both filters
				return Predicates.and(this::filter, other::filter);
			} else {
				return this::filter;
			}
		} else if (other.getNodeType().usesFilters() && !other.hasEmptyFilter()) {
			return other::filter;
		} else {
			return itemVariant -> true;
		}
	}
	
	private boolean filter(ItemVariant variant) {
		return filterItems
				.stream()
				.anyMatch(filterItem -> {
					ItemStack filterStack = filterItem.toStack();
					
					if (!filterStack.has(DataComponents.CUSTOM_NAME) || !filterStack.is(SpectrumItemTags.TAG_FILTERING_ITEMS))
						return filterStack.getItem() == variant.getItem();
					
					var name = StringUtils.trim(filterStack.getHoverName().getString());
					
					// This is to allow nbt filtering without item / tag filtering.
					if (StringUtils.equalsAnyIgnoreCase(name, "*", "any", "all", "everything", "c:*", "c:any", "c:all", "c:everything"))
						return true;
					
					var id = ResourceLocation.tryParse(StringUtils.remove(name, '#')); // let's be nice and remove any pound signs
					if (id == null)
						return false;
					
					var tag = SpectrumCommon.CACHED_ITEM_TAG_MAP.computeIfAbsent(id, tagId -> BuiltInRegistries.ITEM.getTagNames()
							.filter(t -> t.location().equals(tagId))
							.findFirst()
							.orElse(null));
					
					if (tag == null)
						return false;
					
					return variant.getItem().builtInRegistryHolder().is(tag);
				});
	}
	
	public long getCreationStamp() {
		return creationStamp;
	}
	
	@Override
	public Component getDisplayName() {
		return Component.translatable("block.spectrum.pastel_node");
	}
	
	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
		return new FilteringScreenHandler(syncId, inv, new ExtendedData(this));
	}
	
	@Override
	public int getFilterRows() {
		return filterSlotRows;
	}
	
	@Override
	public int getDrawnSlots() {
		return getFilterRows() * SLOTS_PER_ROW;
	}
	
	@Override
	public FilterConfigurable.ExtendedData getScreenOpeningData(ServerPlayer player) {
		return new FilterConfigurable.ExtendedData(this);
	}
	
	public boolean equals(Object obj) {
		return obj instanceof PastelNodeBlockEntity blockEntity && this.worldPosition.equals(blockEntity.worldPosition);
	}
	
	public int hashCode() {
		return this.worldPosition.hashCode();
	}
	
	public ConnectionState getState() {
		return connectionState;
	}
	
	public Optional<DyeColor> getColor() {
		return this.color;
	}
	
	public boolean setColor(Optional<DyeColor> color, @Nullable Entity user) {
		if (this.color == color)
			return false;
		
		this.color = color;
		if (!level.isClientSide()) {
			connectToNearbyNodes(user);
		}
		
		return true;
	}
	
	public void connectToNearbyNodes(@Nullable Entity user) {
		// remove from existing network, if it had one and join new networks
		Pastel.getServerInstance().removeNode(this, NodeRemovalReason.DISCONNECT);
		this.setNetworkUUID(null);
		
		// scan for all connectable nearby nodes
		Map<Optional<ServerPastelNetwork>, List<PastelNodeBlockEntity>> connectableNodes = new Object2ObjectArrayMap<>();
		ServerPastelNetwork biggestNetwork = null;
		for (BlockPos pos : BlockPos.withinManhattan(this.getBlockPos(), RANGE, RANGE, RANGE)) {
			Optional<PastelNodeBlockEntity> blockEntity = level.getBlockEntity(pos, SpectrumBlockEntities.PASTEL_NODE);
			if (blockEntity.isPresent() && canConnect(this, blockEntity.get())) {
				PastelNodeBlockEntity connectableNode = blockEntity.get();
				Optional<ServerPastelNetwork> connectableNetwork = connectableNode.getServerNetwork();
				if (connectableNodes.containsKey(connectableNetwork)) {
					connectableNodes.get(connectableNetwork).add(connectableNode);
				} else {
					List<PastelNodeBlockEntity> newList = new ArrayList<>();
					newList.add(connectableNode);
					connectableNodes.put(connectableNetwork, newList);
				}
				if (connectableNetwork.isPresent()) {
					if (biggestNetwork == null) {
						biggestNetwork = connectableNetwork.get();
					} else if (connectableNetwork.get().size() > biggestNetwork.size()) {
						biggestNetwork = connectableNetwork.get();
					}
				}
			}
		}
		
		ServerPastelNetwork network = null;
		int foundNetworkCount = connectableNodes.size() - (connectableNodes.containsKey(Optional.empty()) ? 1 : 0);
		
		// no other nodes in sight
		if (connectableNodes.isEmpty()) {
			// no nodes to connect to.
		} else if (foundNetworkCount == 0) {
			// there are other nodes, but none of those have a network yet
			// => create one!
			
			network = Pastel.getServerInstance().createNetwork((ServerLevel) level, this);
			for (PastelNodeBlockEntity entry : connectableNodes.get(Optional.empty())) {
				try {
					network.addEdge(this, entry); // Sometimes throws 'IllegalStateException("Attempted to add an edge to a foreign network")' (why? idk. better safe than sorry)
				} catch (Exception e) {
					SpectrumCommon.logWarning("PastelNodeBlockEntity can't connectToNearbyNodes: " + e.getMessage());
					e.printStackTrace();
				}
			}
		} else if (foundNetworkCount == 1) {
			// there is exactly one other network
			// => add this node to it
			
			List<PastelNodeBlockEntity> nodesWithoutNetwork = null;
			for (Map.Entry<Optional<ServerPastelNetwork>, List<PastelNodeBlockEntity>> entry : connectableNodes.entrySet()) {
				Optional<ServerPastelNetwork> currentNetwork = entry.getKey();
				if (currentNetwork.equals(Optional.empty())) {
					nodesWithoutNetwork = entry.getValue();
				} else {
					network = currentNetwork.get();
					for (PastelNodeBlockEntity currentNode : entry.getValue()) {
						currentNetwork.get().addEdge(currentNode, this);
					}
				}
			}
			
			if (nodesWithoutNetwork != null) {
				for (PastelNodeBlockEntity nodeWithoutNetwork : nodesWithoutNetwork) {
					network.addEdge(this, nodeWithoutNetwork);
				}
			}
		} else {
			// there are multiple networks and potentially even nodes without a network yet around!
			// => connect to the biggest one, merge the others into it and then connect nodes without a pre-existing network
			List<PastelNodeBlockEntity> biggestNetworkNodes = connectableNodes.get(Optional.of(biggestNetwork));
			for (PastelNodeBlockEntity currentNode : biggestNetworkNodes) {
				biggestNetwork.addEdge(currentNode, this);
			}
			
			for (Map.Entry<Optional<ServerPastelNetwork>, List<PastelNodeBlockEntity>> entry : connectableNodes.entrySet()) {
				Optional<ServerPastelNetwork> currentNetwork = entry.getKey();
				if (!currentNetwork.equals(Optional.of(biggestNetwork))) {
					if (currentNetwork.isPresent()) {
						biggestNetwork.incorporate(currentNetwork.get(), this.getBlockPos());
					}
					for (PastelNodeBlockEntity currentNode : entry.getValue()) {
						biggestNetwork.addEdge(this, currentNode);
					}
				}
			}
		}
		
		if (network != null) {
			network.markDirty(this.getBlockPos());
			if (user instanceof ServerPlayer serverPlayer) {
				SpectrumAdvancementCriteria.PASTEL_NETWORK_CREATING.trigger(serverPlayer, network);
			}
		}
	}
	
	public boolean canConnect(PastelNodeBlockEntity first, PastelNodeBlockEntity second) {
		return first != second && first.getColor().equals(second.getColor()) && first.getBlockPos().closerThan(second.getBlockPos(), RANGE);
	}
	
	public Optional<ServerPastelNetwork> getServerNetwork() {
		if (this.networkUUID.isPresent()) {
			return Pastel.getServerInstance().getNetwork(this.networkUUID.get());
		}
		return Optional.empty();
	}
	
	public int getPastelNetworkColor() {
		Optional<DyeColor> color = getColor();
		return color.isPresent() ? color.get().getTextureDiffuseColor() : SpectrumColorHelper.getRandomColor(getNodeId().hashCode());
	}
	
	enum ConnectionState {
		DISCONNECTED,
		CONNECTED,
		ACTIVE,
		INACTIVE
	}
	
	public void setSpinTicks(long spinTicks) {
		this.spinTicks = spinTicks;
	}
	
	@Override
	public void markLit() {
		lit = true;
	}
	
	@Override
	public void markLamp() {
		this.lamp = true;
	}
	
	@Override
	public void markTriggerTransfer() {
		triggerTransfer = true;
	}
	
	@Override
	public void markSensor() {
		sensor = true;
	}
	
	@Override
	public void markTriggered() {
		triggered = false;
		waiting = true;
	}
	
	@Override
	public boolean isTriggerTransfer() {
		return triggerTransfer;
	}
	
	@Override
	public boolean isSensor() {
		return sensor;
	}
	
	@Override
	public void applySlotUpgrade(PastelUpgradeSignature upgrade) {
		filterSlotRows += getNodeType().hasOuterRing() ? upgrade.slotRows : upgrade.slotRows * 2;
	}
	
	@Override
	public void applySimple(PastelUpgradeSignature upgrade) {
		transferCount += upgrade.stack;
		transferTime += upgrade.speed;
	}
	
	@Override
	public void applyCompounding(PastelUpgradeSignature upgrade) {
		transferCount = Math.round(transferCount * upgrade.stackMult);
		transferTime = Math.round(transferTime * upgrade.speedMult);
	}
	
	@Override
	public void upgradePriority() {
		if (priority == PastelNetwork.NodePriority.GENERIC) {
			priority = PastelNetwork.NodePriority.MODERATE;
		} else {
			priority = PastelNetwork.NodePriority.HIGH;
		}
	}
	
	@Override
	public String toString() {
		return this.getNodeType().toString() + "-" +
				this.getColor().toString() + "-" +
				this.getBlockPos().toString() + "-" +
				this.getNodeId();
	}
	
}
