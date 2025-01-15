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
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.lookup.v1.block.*;
import net.fabricmc.fabric.api.screenhandler.v1.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.network.listener.*;
import net.minecraft.network.packet.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.registry.*;
import net.minecraft.screen.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.property.Properties;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.apache.commons.lang3.*;
import org.jetbrains.annotations.*;

import java.util.Optional;
import java.util.*;
import java.util.function.Predicate;

@SuppressWarnings("UnstableApiUsage")
public class PastelNodeBlockEntity extends BlockEntity implements FilterConfigurable, ExtendedScreenHandlerFactory, Stampable, PastelUpgradeable {

	public static final int MAX_FILTER_SLOTS = 25;
	public static final int SLOTS_PER_ROW = 5;
	public static final int DEFAULT_FILTER_SLOT_ROWS = 1;
	public static final int RANGE = 12;

	@NotNull
	protected UUID nodeId = UUID.randomUUID();
	protected Optional<UUID> networkUUID = Optional.empty();
	protected Optional<PastelUpgradeSignature> outerRing, innerRing, redstoneRing;
	protected long lastTransferTick = 0;
	protected final long cachedRedstonePowerTick = 0;
	protected boolean cachedUnpowered = true;
	protected PastelNetwork.NodePriority priority = PastelNetwork.NodePriority.GENERIC;
	protected long itemCountUnderway = 0;

	// upgrade impl stuff
	protected boolean lit, triggerTransfer, triggered, waiting, lamp, sensor;
	protected int transferCount = PastelTransmissionLogic.DEFAULT_MAX_TRANSFER_AMOUNT;
	protected int transferTime = PastelTransmissionLogic.DEFAULT_TRANSFER_TICKS_PER_NODE;
	protected int filterSlotRows = DEFAULT_FILTER_SLOT_ROWS;

	protected BlockApiCache<Storage<ItemVariant>, Direction> connectedStorageCache = null;
	protected Direction cachedDirection = null;

	private final List<ItemVariant> filterItems;
	float rotationTarget, crystalRotation, lastRotationTarget, heightTarget, crystalHeight, lastHeightTarget, alphaTarget, ringAlpha, lastAlphaTarget;
	long creationStamp = -1, interpTicks, interpLength = -1, spinTicks;
	private State state;

	public PastelNodeBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.PASTEL_NODE, blockPos, blockState);
		this.filterItems = DefaultedList.ofSize(MAX_FILTER_SLOTS, ItemVariant.blank());
		this.outerRing = Optional.empty();
		this.innerRing = Optional.empty();
		this.redstoneRing = Optional.empty();
	}

	public @Nullable Storage<ItemVariant> getConnectedStorage() {
		if (connectedStorageCache == null) {
			BlockState state = this.getCachedState();
			if (!(state.getBlock() instanceof PastelNodeBlock)) {
				return null;
			}
			cachedDirection = state.get(PastelNodeBlock.FACING);
			connectedStorageCache = BlockApiCache.create(ItemStorage.SIDED, (ServerWorld) world, this.getPos().offset(cachedDirection.getOpposite()));
		}
		return connectedStorageCache.find(cachedDirection);
	}

	public static void tick(@NotNull World world, BlockPos pos, BlockState state, PastelNodeBlockEntity node) {
		if (node.lamp && state.get(Properties.LIT) != node.canTransfer()) {
			world.setBlockState(pos, state.with(Properties.LIT, node.cachedUnpowered));
		}

		//Trigger transfer logic needs to be ticked here
		if (node.triggerTransfer) {
			var powered = world.isReceivingRedstonePower(pos);

			if (node.waiting && !powered) {
				node.waiting = false;
			}

			if (!node.triggered && !node.waiting && powered) {
				node.triggered = true;
			}
		}

		if (world.isClient()) {
			if (node.networkUUID.isEmpty()) {
				node.changeState(State.DISCONNECTED);
				node.interpLength = 17;
			} else if (!node.canTransfer()) {
				node.changeState(State.INACTIVE);
				node.interpLength = 21;
			} else if (node.spinTicks > 0) {
				node.changeState(State.ACTIVE);
				node.interpLength = 17;
			} else {
				node.changeState(State.CONNECTED);
				node.interpLength = 13;
			}

			if (node.interpTicks < node.interpLength)
				node.interpTicks++;

			if (node.spinTicks > 0)
				node.spinTicks--;
		}
	}

	public void changeState(State state) {
		if (this.state != state) {
			this.state = state;
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
			stack = redstoneRing.get().upgradeItem.getDefaultStack();
			redstoneRing = Optional.empty();
		} else if (innerRing.isPresent()) {
			stack = innerRing.get().upgradeItem.getDefaultStack();
			innerRing = Optional.empty();
		} else if (outerRing.isPresent()) {
			stack = outerRing.get().upgradeItem.getDefaultStack();
			outerRing = Optional.empty();
		}

		if (!stack.isEmpty()) {
			world.playSoundAtBlockCenter(pos, SpectrumSoundEvents.SHATTER_LIGHT, SoundCategory.BLOCKS, 0.25F, 0.9F + world.getRandom().nextFloat() * 0.2F, true);
			markDirty();
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
		priority = PastelNetwork.NodePriority.GENERIC;

		//First one processed can't compound because it has nothing to compound on
		outerRing.ifPresent(r -> apply(r, Collections.emptyList()));
		innerRing.ifPresent(r -> apply(r, outerRing.map(List::of).orElse(Collections.emptyList())));
		redstoneRing.ifPresent(r -> apply(r, Collections.emptyList()));

		// Sanity
		transferCount = Math.max(transferCount, 1);
		transferTime = MathHelper.clamp(transferTime, 2, 100);
		filterSlotRows = MathHelper.clamp(filterSlotRows, 1, 5);

		if (lit && lamp) {
			lit = false;
		}
		
		if (world != null && getCachedState().get(Properties.LIT) != lit) {
			networkUUID.ifPresent(uuid -> ServerPastelNetworkManager.get((ServerWorld) world).getNetwork(uuid));
			world.setBlockState(pos, getCachedState().with(Properties.LIT, lit));
		}

		if (filterSlotRows < oldFilterSlotCount) {
			for (int i = getDrawnSlots(); i < filterItems.size(); i++) {
				filterItems.set(i, ItemVariant.blank());
			}
		}
	}

	@Override
	public void notifySensor() {
		if (world != null) {
			var state = getCachedState();
			world.setBlockState(pos, state.with(Properties.POWERED, true));
			if (!world.getBlockTickScheduler().isQueued(pos, state.getBlock())) {
				world.scheduleBlockTick(pos, state.getBlock(), 2);
			}
		}
	}

	public long getMaxTransferredAmount() {
		return transferCount;
	}

	public int getTransferTime() {
		return transferTime;
	}

	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		if (creationStamp == -1) {
			creationStamp = (world.getTime() + world.getRandom().nextInt(7)) % 20;
		}
		
		if (!world.isClient && this.networkUUID.isPresent()) {
			this.networkUUID = Optional.of(Pastel.getServerInstance().joinOrCreateNetwork(this, this.networkUUID.get()).getUUID());
		}
	}

	public float getRedstoneAlphaMult() {
		return redstoneRing.isPresent() ? 0.5F : 0.25F;
	}

	public boolean canTransfer() {
		var result = redstoneRing.map(r -> r.preProcessor
				.apply(new PastelUpgradeSignature.RedstoneContext(this, world, pos, cachedUnpowered))).orElse(ActionResult.PASS);

		if (result == ActionResult.SUCCESS)
			return true;

		if (result == ActionResult.FAIL)
			return false;

		long time = this.getWorld().getTime();
		if (time > this.cachedRedstonePowerTick && !getCachedState().get(PastelNodeBlock.EMITTING)) {
			this.cachedUnpowered = world.getReceivedRedstonePower(this.pos) == 0;
		}

		boolean notPowered = redstoneRing.map(r -> {
			var post = r.postProcessor.apply(new PastelUpgradeSignature.RedstoneContext(this, world, pos, cachedUnpowered));

			if (post == ActionResult.SUCCESS)
				return true;

			if (post == ActionResult.FAIL)
				return false;

			return cachedUnpowered;
		}).orElse(cachedUnpowered);

		var canTransfer = this.getWorld().getTime() > lastTransferTick;
		if (triggerTransfer) {
			return triggered && canTransfer;
		}

		return canTransfer && notPowered;
	}

	public void markTransferred() {
		if (triggerTransfer) {
			markTriggered();
		}

		this.lastTransferTick = world.getTime();
		this.markDirty();
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		if (nbt.contains("NetworkUUID")) {
			this.networkUUID = Optional.of(nbt.getUuid("NetworkUUID"));
		}
		if (nbt.contains("Triggered")) {
			this.triggered = nbt.getBoolean("Triggered");
		}
		if (nbt.contains("Waiting")) {
			this.waiting = nbt.getBoolean("Waiting");
		}
		if (nbt.contains("creationStamp")) {
			this.creationStamp = nbt.getLong("creationStamp");
		}
		if (nbt.contains("NodeID")) {
			this.nodeId = nbt.getUuid("NodeID");
		}
		if (nbt.contains("LastTransferTick", NbtElement.LONG_TYPE)) {
			this.lastTransferTick = nbt.getLong("LastTransferTick");
		}
		if (nbt.contains("ItemCountUnderway", NbtElement.LONG_TYPE)) {
			this.itemCountUnderway = nbt.getLong("ItemCountUnderway");
		}
		if (nbt.contains("OuterRing")) {
			outerRing = Optional.ofNullable(SpectrumRegistries.PASTEL_UPGRADE.get(Identifier.tryParse(nbt.getString("OuterRing"))));
		} else {
			outerRing = Optional.empty();
		}
		if (nbt.contains("InnerRing")) {
			innerRing = Optional.ofNullable(SpectrumRegistries.PASTEL_UPGRADE.get(Identifier.tryParse(nbt.getString("InnerRing"))));
		} else {
			innerRing = Optional.empty();
		}
		if (nbt.contains("RedstoneRing")) {
			redstoneRing = Optional.ofNullable(SpectrumRegistries.PASTEL_UPGRADE.get(Identifier.tryParse(nbt.getString("RedstoneRing"))));
		} else {
			redstoneRing = Optional.empty();
		}
		if (this.getNodeType().usesFilters()) {
			FilterConfigurable.readFilterNbt(nbt, this.filterItems);
		}
		updateUpgrades();
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (creationStamp != -1) {
			nbt.putLong("creationStamp", creationStamp);
		}
		if (this.networkUUID.isPresent()) {
			nbt.putUuid("NetworkUUID", this.networkUUID.get());
		}
		nbt.putUuid("NodeID", this.nodeId);
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
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		Optional<ServerPastelNetwork> network = getServerNetwork();
		if (network.isPresent()) {
			SpectrumS2CPacketSender.syncPastelNetworkEdges(network.get(), pos);
		}
		
		NbtCompound nbtCompound = new NbtCompound();
		this.writeNbt(nbtCompound);
		return nbtCompound;
	}

	// triggered when the chunk is unloaded, or the world quit
	@Override
	public void markRemoved() {
		super.markRemoved();
		if (!world.isClient()) {
			Pastel.getServerInstance().removeNode(this, NodeRemovalReason.UNLOADED);
		}
	}
	
	public @NotNull UUID getNodeId() {
		return nodeId;
	}
	
	public void onBroken() {
		if (!world.isClient) {
			Pastel.getServerInstance().removeNode(this, NodeRemovalReason.BROKEN);
		}
	}

	public boolean canConnect(PastelNodeBlockEntity node) {
		return this.pos.isWithinDistance(node.pos, RANGE);
	}

	public PastelNodeType getNodeType() {
		if (this.getCachedState().getBlock() instanceof PastelNodeBlock pastelNodeBlock) {
			return pastelNodeBlock.pastelNodeType;
		}
		return PastelNodeType.CONNECTION;
	}
	
	public void setNetworkUUID(UUID uuid) {
		this.networkUUID = Optional.of(uuid);
		if (this.getWorld() != null && !this.getWorld().isClient()) {
			updateInClientWorld();
			this.markDirty();
		}
	}

	public long getItemCountUnderway() {
		return this.itemCountUnderway;
	}

	public void addItemCountUnderway(long count) {
		this.itemCountUnderway += count;
		this.itemCountUnderway = Math.max(0, this.itemCountUnderway);
		this.markDirty();
	}

	// interaction methods
	public void updateInClientWorld() {
		((ServerWorld) world).getChunkManager().markForUpdate(pos);
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
					if (LoreHelper.hasLore(filterStack)) {
						if (variant.getNbt() == null)
							return false;

						for (Text text : LoreHelper.getLoreList(filterStack)) {
							if (!testNBTPredicates(text.getString(), filterStack, variant))
								return false;
						}
					}

					if (!filterStack.hasCustomName() || !filterStack.isIn(SpectrumItemTags.TAG_FILTERING_ITEMS))
						return filterStack.getItem() == variant.getItem();

					var name = StringUtils.trim(filterStack.getName().getString());

					// This is to allow nbt filtering without item / tag filtering.
					if (StringUtils.equalsAnyIgnoreCase(name, "*", "any", "all", "everything", "c:*", "c:any", "c:all", "c:everything"))
						return true;

					var id = Identifier.tryParse(StringUtils.remove(name, '#')); // let's be nice and remove any pound signs for the dumb idiots
					if (id == null)
						return false;

					var tag = SpectrumCommon.CACHED_ITEM_TAG_MAP.computeIfAbsent(id, tagId -> Registries.ITEM.streamTags()
							.filter(t -> t.id().equals(tagId))
							.findFirst()
							.orElse(null));

					if (tag == null)
						return false;

					return variant.getItem().getRegistryEntry().isIn(tag);
				});
	}

	public static final String GREATER_THAN_KEYWORD = "above";
	public static final String LESSER_THAN_KEYWORD = "below";
	public static final String DAMAGED_KEYWORD = "damaged";
	public static final String NOT_DAMAGED_KEYWORD = "not damaged";
	public static final String STRING_EMPTY_KEYWORD = "is empty";
	public static final String STRING_NOT_EMPTY_KEYWORD = "is not empty";
	public static final String ENCHANTMENT_MATCH_KEYWORD = "with";
	public static final String ENCHANTMENT_LEVEL_KEYWORD = "level";

	public boolean testNBTPredicates(String description, ItemStack stack, ItemVariant variant) {
		var tested = variant.getNbt();
		var cleanString = StringUtils.trim(description).toLowerCase();
		var pieces = StringUtils.splitByWholeSeparator(cleanString, null);
		var target = pieces[0];
		var predicateString = StringUtils.remove(cleanString, target); // We don't want ambiguity when checking for keywords
		var source = stack.getNbt(); //No need to check if it has nbt, to get here it already had to have some.
		boolean nullSourceFilter = false;

		// A few corrections for ease of use
		if (StringUtils.equalsAnyIgnoreCase(target, "durability", "uses"))
			target = ItemStack.DAMAGE_KEY;

		if (StringUtils.equalsAnyIgnoreCase(target, "enchs", "enchants", "enchantment")) {
			target = ItemStack.ENCHANTMENTS_KEY;
		}

		// Exit early if it just is not there
		assert source != null;
		if (source.contains(target) && !tested.contains(target)) {
			return false;
		}

		// Null-source filtering
		if (!source.contains(target) && tested.contains(target)) {
			nullSourceFilter = true;
		}

		// By now we know that the target exists in both places, but what is it?
		var sourceData = source.get(target);
		var testedData = tested.get(target);

		// Duh
		assert nullSourceFilter || sourceData != null;
		assert testedData != null;
		if (!nullSourceFilter && sourceData.getType() != testedData.getType())
			return false;

		boolean lessThan = StringUtils.containsIgnoreCase(predicateString, LESSER_THAN_KEYWORD);
		boolean moreThan = StringUtils.containsIgnoreCase(predicateString, GREATER_THAN_KEYWORD);

		// Enchantments are so fucking cursed
		if (target.equals(ItemStack.ENCHANTMENTS_KEY) || target.equals(EnchantedBookItem.STORED_ENCHANTMENTS_KEY)) {
			if (testedData.getType() != NbtElement.LIST_TYPE)
				return false;

			var testedEnchants = EnchantmentHelper.fromNbt((NbtList) testedData);

			if (StringUtils.containsIgnoreCase(predicateString, ENCHANTMENT_MATCH_KEYWORD)) {
				var noKeyWordString = StringUtils.remove(predicateString, ENCHANTMENT_MATCH_KEYWORD);
				var potentialEnchants = StringUtils.splitByWholeSeparator(noKeyWordString, null);

				Optional<Enchantment> enchantment = Optional.empty();
				for (String potentialEnchant : potentialEnchants) {
					enchantment = Registries.ENCHANTMENT.getOrEmpty(Identifier.tryParse(potentialEnchant));
					if (enchantment.isPresent())
						break;
				}

				if (enchantment.isEmpty())
					return false;

				if (!testedEnchants.containsKey(enchantment.get()))
					return false;

				if (StringUtils.containsIgnoreCase(predicateString, ENCHANTMENT_LEVEL_KEYWORD)) {
					return testedEnchants.get(enchantment.get()) == Math.round(getNumber(noKeyWordString));
				}

				if (lessThan) {
					return testedEnchants.get(enchantment.get()) < Math.round(getNumber(noKeyWordString));
				}

				if (moreThan) {
					return testedEnchants.get(enchantment.get()) > Math.round(getNumber(noKeyWordString));
				}

				return true;
			}

			if (nullSourceFilter)
				return true;

			return EnchantmentHelper.get(stack).keySet().stream().allMatch(testedEnchants::containsKey);
		}

		switch (testedData.getType()) {
			case NbtElement.NUMBER_TYPE: {
				var testedNum = ((AbstractNbtNumber) testedData).doubleValue();

				// Special damage keywords - durability is weird and counts up as it decreases
				if (target.equals(ItemStack.DAMAGE_KEY)) {
					if (StringUtils.containsIgnoreCase(predicateString, DAMAGED_KEYWORD)) {
						return testedNum > 0;
					}

					if (StringUtils.containsIgnoreCase(predicateString, NOT_DAMAGED_KEYWORD)) {
						return MathHelper.approximatelyEquals(testedNum, 0);
					}
				}

				if (lessThan) {
					double comparator;
					comparator = getNumber(predicateString);

					return testedNum < comparator;
				}

				if (moreThan) {
					double comparator;
					comparator = getNumber(predicateString);

					return testedNum < comparator;
				}

				if (nullSourceFilter)
					return true;

				return MathHelper.approximatelyEquals(((AbstractNbtNumber) sourceData).doubleValue(), testedNum);
			}
			case NbtElement.STRING_TYPE: {
				var testedString = testedData.asString();

				if (StringUtils.containsIgnoreCase(predicateString, STRING_EMPTY_KEYWORD)) {
					return StringUtils.isBlank(testedString);
				}

				if (StringUtils.containsIgnoreCase(predicateString, STRING_NOT_EMPTY_KEYWORD)) {
					return StringUtils.isNotBlank(testedString);
				}

				if (nullSourceFilter)
					return true;

				return StringUtils.equalsIgnoreCase(sourceData.asString(), testedString);
			}
			default: {
				if (nullSourceFilter)
					return true;
				
				// Last resort that will work 50% of the time maybe not really
				return sourceData.asString().equals(testedData.asString());
			}
		}
	}

	private static double getNumber(String predicateString) {
		double comparator;
		try {
			comparator = Double.parseDouble(StringUtils.getDigits(predicateString));
		} catch (NumberFormatException ignored) {
			comparator = 0;
		}
		return comparator;
	}

	public long getCreationStamp() {
		return creationStamp;
	}

	@Override
	public Text getDisplayName() {
		return Text.translatable("block.spectrum.pastel_node");
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
		return new FilteringScreenHandler(syncId, inv, this);
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
	public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
		FilterConfigurable.writeScreenOpeningData(buf, this);
	}

	public boolean equals(Object obj) {
		return obj instanceof PastelNodeBlockEntity blockEntity && this.pos.equals(blockEntity.pos);
	}

	public int hashCode() {
		return this.pos.hashCode();
	}

	@Override
	public StampData recordStampData(Optional<PlayerEntity> user, BlockReference reference, World world) {
		return new StampData(user.map(Entity::getUuid), reference, this);
	}

	@Override
	public boolean handleImpression(Optional<UUID> stamper, Optional<PlayerEntity> user, BlockReference reference, World world) {
		var otherNode = (PastelNodeBlockEntity) reference.tryGetBlockEntity().orElseThrow(() -> new IllegalStateException("Attempted to connect a non-existent node - what did you do?!"));
		
		if (otherNode == this) {
			return false;
		}
		if (!otherNode.canConnect(this))
			return false;
		
		boolean success;
		if (otherNode.networkUUID.isPresent() && otherNode.networkUUID.equals(this.networkUUID)) {
			var thisNetwork = getServerNetwork();
			success = thisNetwork.get().removeEdge(this, otherNode) || thisNetwork.get().addEdge(this, otherNode);
		} else {
			Pastel.getServerInstance().connectNodes(this, otherNode);
			success = true;
		}
		
		if (success && this.networkUUID.isPresent() && user.isPresent() && user.get() instanceof ServerPlayerEntity serverPlayer) {
			SpectrumAdvancementCriteria.PASTEL_NETWORK_CREATING.trigger(serverPlayer, Pastel.getServerInstance().getNetwork(this.networkUUID.get()).get());
		}
		
		return success;
	}
	
	public Optional<ServerPastelNetwork> getServerNetwork() {
		if (this.networkUUID.isPresent()) {
			return Pastel.getServerInstance().getNetwork(this.networkUUID.get());
		}
		return Optional.empty();
	}

	@Override
	public void clearImpression() {
		Pastel.getServerInstance().removeNode(this, NodeRemovalReason.DISCONNECT);
		networkUUID = Optional.empty();
		if (!this.world.isClient()) {
			updateInClientWorld();
			markDirty();
		}
	}

	public State getState() {
		return state;
	}

	@Override
	public StampDataCategory getStampCategory() {
		return SpectrumStampDataCategories.PASTEL;
	}

	@Override
	public boolean canUserStamp(Optional<PlayerEntity> stamper) {
		return true;
	}

	@Override
	public void onImpressedOther(StampData data, boolean success) {
	}

	public enum State {
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
}