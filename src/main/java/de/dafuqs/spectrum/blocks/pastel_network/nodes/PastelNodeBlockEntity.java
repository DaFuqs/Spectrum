package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import com.google.common.collect.*;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.helpers.BlockReference;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.lookup.v1.block.*;
import net.fabricmc.fabric.api.screenhandler.v1.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.network.listener.*;
import net.minecraft.network.packet.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.registry.Registries;
import net.minecraft.screen.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.property.Properties;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class PastelNodeBlockEntity extends BlockEntity implements FilterConfigurable, ExtendedScreenHandlerFactory, Stampable {

    public static final Map<Item, UpgradeSignature> UPGRADES;
    public static final int MAX_FILTER_SLOTS = 25;
    public static final int SLOTS_PER_ROW = 5;
    public static final int DEFAULT_FILTER_SLOT_ROWS = 1;
	public static final int RANGE = 12;

    public static final UpgradeSignature ALWAYS_ON = UpgradeSignature.redstone(SpectrumItems.PURE_REDSTONE, "always_active");
    public static final UpgradeSignature ALWAYS_OFF = UpgradeSignature.redstone(SpectrumItems.PURE_LAPIS, "always_inactive");
    public static final UpgradeSignature INVERTED = UpgradeSignature.redstone(SpectrumItems.PURE_COAL, "inverted");
    public static final UpgradeSignature SENSOR = UpgradeSignature.redstone(SpectrumItems.PURE_ECHO, "sensor");
    public static final UpgradeSignature TRIGGER = UpgradeSignature.redstone(SpectrumItems.PURE_QUARTZ, "trigger");
    public static final UpgradeSignature LAMP = UpgradeSignature.redstone(SpectrumItems.PURE_GLOWSTONE, "lamp");

    @Nullable
	protected PastelNetwork parentNetwork;
	protected Optional<UUID> parentID = Optional.empty();
    protected Optional<UpgradeSignature> outerRing, innerRing, redstoneRing;
    protected Set<BlockPos> connectionMemory = new HashSet<>();
	protected long lastTransferTick = 0;
	protected final long cachedRedstonePowerTick = 0;
	protected boolean cachedNoRedstonePower = true, lit, triggerTransfer;
    protected PastelNetwork.Priority priority = PastelNetwork.Priority.GENERIC;

	protected long itemCountUnderway = 0;
    protected long transferCount = PastelTransmissionLogic.DEFAULT_MAX_TRANSFER_AMOUNT;
    protected int transferTime = PastelTransmissionLogic.DEFAULT_TRANSFER_TICKS_PER_NODE;
    protected int filterSlotRows = DEFAULT_FILTER_SLOT_ROWS;
	
	protected BlockApiCache<Storage<ItemVariant>, Direction> connectedStorageCache = null;
	protected Direction cachedDirection = null;

    private final List<Item> filterItems;
    float rotationTarget, crystalRotation, lastRotationTarget, heightTarget, crystalHeight, lastHeightTarget, alphaTarget, ringAlpha, lastAlphaTarget;
    long creationStamp = -1, interpTicks, interpLength = -1, spinTicks;
    private State state;

    public PastelNodeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpectrumBlockEntities.PASTEL_NODE, blockPos, blockState);
        this.filterItems = DefaultedList.ofSize(MAX_FILTER_SLOTS, Items.AIR);
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
        if (node.parentNetwork == null) {
            node.changeState(State.DISCONNECTED);
            node.interpLength = 17;
        }
        else if (!node.canTransfer()) {
            node.changeState(State.INACTIVE);
            node.interpLength = 21;
        }
        else if(node.spinTicks > 0) {
            node.changeState(State.ACTIVE);
            node.interpLength = 17;
        }
        else {
            node.changeState(State.CONNECTED);
            node.interpLength = 13;
        }

        if (node.interpTicks < node.interpLength)
            node.interpTicks++;

        if (node.spinTicks > 0)
            node.spinTicks--;

        if (node.getRedstoneRing().map(LAMP::is).orElse(false) && node.getCachedState().get(Properties.LIT) != node.cachedNoRedstonePower) {
            world.setBlockState(pos, node.getCachedState().with(Properties.LIT, node.cachedNoRedstonePower));
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

    public Optional<UpgradeSignature> getInnerRing() {
        return innerRing;
    }

    public Optional<UpgradeSignature> getOuterRing() {
        return outerRing;
    }

    public Optional<UpgradeSignature> getRedstoneRing() {
        return redstoneRing;
    }

    public PastelNetwork.Priority getPriority() {
        return priority;
    }

    // outer goes first, then inner, then redstone
    public boolean tryInteractRings(Item item, PastelNodeType type) {
        var upgrade = UPGRADES.get(item);
        if (upgrade == null)
            return false;

        if (upgrade.redstone) {
            if (redstoneRing.isEmpty()) {
                redstoneRing = Optional.of(upgrade);
                return true;
            }

            return false;
        }

        if (outerRing.isEmpty() && type.hasOuterRing()) {
            outerRing = Optional.of(upgrade);
            return true;
        }
        else if (innerRing.isEmpty()) {
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
        }
        else if (innerRing.isPresent()) {
            stack = innerRing.get().upgradeItem.getDefaultStack();
            innerRing = Optional.empty();
        }
        else if (outerRing.isPresent()) {
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
        var oldPriority = priority;
        priority = PastelNetwork.Priority.GENERIC;

        //First one processed can't compound because it has nothing to compound on
        outerRing.ifPresent(r -> r.apply(this, UpgradeSignature.UpgradeCategory.NON_COMPOUNDING));
        innerRing.ifPresent(r -> r.apply(this, outerRing.map(UpgradeSignature::category).orElse(UpgradeSignature.UpgradeCategory.NON_COMPOUNDING)));
        if (parentNetwork != null)
            parentNetwork.updateNodePriority(this, oldPriority);

        if (world != null && getCachedState().get(Properties.LIT) != lit)
            world.setBlockState(pos, getCachedState().with(Properties.LIT, lit));

        if (filterSlotRows < oldFilterSlotCount) {
            for (int i = getDrawnSlots(); i < filterItems.size(); i++) {
                filterItems.set(i, Items.AIR);
            }
        }

        markDirty();
    }

    public Set<BlockPos> getRememberedConnections() {
        return connectionMemory;
    }

    public void remember(PastelNodeBlockEntity otherNode) {
        if (this == otherNode) {
            throw new IllegalArgumentException("Tried to make a pastel node remember itself");
        }
        connectionMemory.add(otherNode.pos);
        markDirty();
    }

    public void forget(PastelNodeBlockEntity otherNode) {
        connectionMemory.remove(otherNode.pos);
        markDirty();
    }

    public void forgetAll() {
        connectionMemory.clear();
        markDirty();
    }

    public void pulseRedstone() {
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

        if (!world.isClient) {
            if (this.parentID.isPresent() && parentNetwork == null) {
                this.parentNetwork = Pastel.getServerInstance().JoinOrCreateNetwork(this, this.parentID.get());
                this.parentID = Optional.empty();
            }
        }
    }

    public float getRedstoneAlphaMult() {
        return redstoneRing.isPresent() ? 0.5F : 0.25F;
    }

    public boolean canTransfer() {
        if (redstoneRing.map(ALWAYS_OFF::is).orElse(false)) // this exists solely because I thought it would be funny.
            return false;

        if (redstoneRing.map(ALWAYS_ON::is).orElse(false))
            return true;

        long time = this.getWorld().getTime();
        if (time > this.cachedRedstonePowerTick && !redstoneRing.filter(SENSOR::is).map(r -> getCachedState().get(PastelNodeBlock.EMITTING)).orElse(false)) {
            this.cachedNoRedstonePower = world.getReceivedRedstonePower(this.pos) == 0;
        }

        if (redstoneRing.map(TRIGGER::is).orElse(false)) {
            if (triggerTransfer) {
                if (cachedNoRedstonePower) {
                    triggerTransfer = false;
                }
                return false;
            }
            return this.getWorld().getTime() > lastTransferTick && !cachedNoRedstonePower;
        }

        return this.getWorld().getTime() > lastTransferTick && redstoneRing.filter(INVERTED::is).map(r -> !this.cachedNoRedstonePower).orElse(this.cachedNoRedstonePower);
    }

    public void markTransferred() {
        if (redstoneRing.map(TRIGGER::is).orElse(false)) {
            markTrigger();
        }

        this.lastTransferTick = world.getTime();
        this.markDirty();
    }

    public void markTrigger() {
        triggerTransfer = true;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("Network")) {
            UUID networkUUID = nbt.getUuid("Network");
            if (this.getWorld() == null) {
                this.parentID = Optional.of(networkUUID);
            } else {
                this.parentNetwork = Pastel.getInstance(world.isClient).JoinOrCreateNetwork(this, networkUUID);
            }
        }
        if (nbt.contains("Trigger")) {
            this.triggerTransfer = nbt.getBoolean("Trigger");
        }
        if (nbt.contains("creationStamp")) {
            this.creationStamp = nbt.getLong("creationStamp");
        }
        if (nbt.contains("LastTransferTick", NbtElement.LONG_TYPE)) {
            this.lastTransferTick = nbt.getLong("LastTransferTick");
        }
        if (nbt.contains("ItemCountUnderway", NbtElement.LONG_TYPE)) {
            this.itemCountUnderway = nbt.getLong("ItemCountUnderway");
        }
        if(nbt.contains("OuterRing")) {
            outerRing = Optional.of(UPGRADES.get(Registries.ITEM.get(Identifier.tryParse(nbt.getString("OuterRing")))));
        }
        if(nbt.contains("InnerRing")) {
            innerRing = Optional.of(UPGRADES.get(Registries.ITEM.get(Identifier.tryParse(nbt.getString("InnerRing")))));
        }
        if(nbt.contains("RedstoneRing")) {
            redstoneRing = Optional.of(UPGRADES.get(Registries.ITEM.get(Identifier.tryParse(nbt.getString("RedstoneRing")))));
        }
        if (nbt.contains("ConnectionMemory")) {
            connectionMemory = Arrays.stream(nbt.getLongArray("ConnectionMemory")).mapToObj(BlockPos::fromLong).collect(Collectors.toSet());
        }
        if (this.getNodeType().usesFilters()) {
            readFilterNbt(nbt, this.filterItems);
        }
        updateUpgrades();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (this.parentNetwork != null) {
            nbt.putUuid("Network", this.parentNetwork.getUUID());
        }
        if (creationStamp != -1) {
            nbt.putLong("creationStamp", creationStamp);
        }
        nbt.putLong("LastTransferTick", this.lastTransferTick);
        nbt.putLong("ItemCountUnderway", this.itemCountUnderway);
        nbt.putBoolean("Trigger", this.triggerTransfer);
        if (this.getNodeType().usesFilters()) {
            writeFilterNbt(nbt, this.filterItems);
        }
        outerRing.ifPresent(r -> nbt.putString("OuterRing", Registries.ITEM.getId(r.upgradeItem).toString()));
        innerRing.ifPresent(r -> nbt.putString("InnerRing", Registries.ITEM.getId(r.upgradeItem).toString()));
        redstoneRing.ifPresent(r -> nbt.putString("RedstoneRing", Registries.ITEM.getId(r.upgradeItem).toString()));
        nbt.putLongArray("ConnectionMemory", connectionMemory.stream().map(BlockPos::asLong).toList());
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        this.writeNbt(nbtCompound);
        return nbtCompound;
    }

    // triggered when the chunk is unloaded, or the world quit
    @Override
    public void markRemoved() {
        super.markRemoved();
        Pastel.getInstance(world.isClient).removeNode(this, NodeRemovalReason.UNLOADED);
    }

    public void onBroken() {
        Pastel.getInstance(world.isClient).removeNode(this, NodeRemovalReason.BROKEN);
    }

    public boolean canConnect(PastelNodeBlockEntity node) {
        return this.pos.isWithinDistance(node.pos, RANGE);
    }

    @Nullable
    public PastelNetwork getParentNetwork() {
        return this.parentNetwork;
    }

    public PastelNodeType getNodeType() {
        if (this.getCachedState().getBlock() instanceof PastelNodeBlock pastelNodeBlock) {
            return pastelNodeBlock.pastelNodeType;
        }
        return PastelNodeType.CONNECTION;
    }

    public void setParentNetwork(PastelNetwork parentNetwork) {
        this.parentNetwork = parentNetwork;
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
    public List<Item> getItemFilters() {
        return this.filterItems;
    }

    @Override
    public void setFilterItem(int slot, Item item) {
        this.filterItems.set(slot, item);
    }

    public Predicate<ItemVariant> getTransferFilterTo(PastelNodeBlockEntity other) {
        if (this.getNodeType().usesFilters() && !this.hasEmptyFilter()) {
            if (other.getNodeType().usesFilters() && !other.hasEmptyFilter()) {
                // unionize both filters
                return itemVariant -> filterItems.contains(itemVariant.getItem()) && other.filterItems.contains(itemVariant.getItem());
            } else {
                return itemVariant -> filterItems.contains(itemVariant.getItem());
            }
        } else if (other.getNodeType().usesFilters() && !other.hasEmptyFilter()) {
            return itemVariant -> other.filterItems.contains(itemVariant.getItem());
        } else {
            return itemVariant -> true;
        }
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
        var sourceNode = (PastelNodeBlockEntity) reference.tryGetBlockEntity().orElseThrow(() -> new IllegalStateException("Attempted to connect a non-existent node - what did you do?!"));
        var manager = Pastel.getInstance(world.isClient());

        if (!sourceNode.canConnect(this))
            return false;

        if (sourceNode.parentNetwork != null && sourceNode.parentNetwork == this.parentNetwork) {
            if (manager.tryRemoveEdge(this, sourceNode))
                return true;

            return manager.tryAddEdge(this, sourceNode);
        }

        if (sourceNode.parentID.map(uuid -> uuid.equals(this.parentID.orElse(null))).orElse(false)) {
            return false;
        }

        manager.connectNodes(this, sourceNode);

        if (this.parentNetwork != null) {
            user.filter(u -> u instanceof ServerPlayerEntity).ifPresent(p -> {
                SpectrumAdvancementCriteria.PASTEL_NETWORK_CREATING.trigger((ServerPlayerEntity) p, (ServerPastelNetwork) parentNetwork);
            });
        }

        return true;
    }

    @Override
    public void clearImpression() {
        if (parentNetwork != null) {
            Pastel.getInstance(world.isClient()).removeNode(this, NodeRemovalReason.DISCONNECT);
            parentNetwork = null;
            parentID = Optional.empty();
        }
    }

    public State getState() {
        return state;
    }

    @Override
    public Category getStampCategory() {
        return Category.PASTEL_NODE;
    }

    @Override
    public boolean canUserStamp(Optional<PlayerEntity> stamper) {
        return true;
    }

    @Override
    public void onImpressedOther(StampData data, boolean success) {}

    public enum State {
        DISCONNECTED,
        CONNECTED,
        ACTIVE,
        INACTIVE;
    }

    public void setSpinTicks(long spinTicks) {
        this.spinTicks = spinTicks;
    }

    public record UpgradeSignature(Item upgradeItem, Identifier mainPath, Identifier altPath, int stackEffect, int speedEffect, int filterRowEffect, float stackMult, float speedMult, boolean light, boolean priority, boolean redstone, UpgradeCategory category) {

        public static UpgradeSignature of(Item upgradeItem, String name, int stackEffect, int speedEffect, int filterRowEffect, float stackMult, float speedMult, boolean light, boolean priority, UpgradeCategory category) {
            return new UpgradeSignature(upgradeItem,
                    SpectrumCommon.locate("textures/block/pastel_node_inner_ring_" + name + ".png"),
                    SpectrumCommon.locate("textures/block/pastel_node_outer_ring_" + name + ".png"),
                    stackEffect, speedEffect, filterRowEffect, stackMult, speedMult, light, priority, false, category
                    );
        }

        // redstone interaction upgrades have special handling
        public static UpgradeSignature redstone(Item upgradeItem, String name) {
            return new UpgradeSignature(upgradeItem,
                    SpectrumCommon.locate("textures/block/pastel_node_redstone_ring_" + name + ".png"),
                    null,
                    0, 0, 0, 0, 0, false, false, true, UpgradeCategory.NON_COMPOUNDING
            );
        }

        public void apply(PastelNodeBlockEntity node, UpgradeCategory category) {
            if (light) {
                node.lit = true;
            }

            if (redstone)
                return;

            if (category != UpgradeCategory.NON_COMPOUNDING && this.category == category) {
                applyCompounding(node);
            }
            else {
                applyBase(node);
            }

            if (priority) {
                upgradePriority(node);
            }
        }

        public void applyBase(PastelNodeBlockEntity node) {
            node.transferCount += stackEffect;
            node.transferTime += speedEffect;
            node.filterSlotRows += node.getNodeType().hasOuterRing() ? filterRowEffect : filterRowEffect * 2;
        }

        public void applyCompounding(PastelNodeBlockEntity node) {
            node.transferCount *= stackMult;
            node.transferTime *= speedMult;
            node.filterSlotRows += node.getNodeType().hasOuterRing() ? filterRowEffect : filterRowEffect * 2;
        }

        private static void upgradePriority(PastelNodeBlockEntity node) {
            if (node.priority == PastelNetwork.Priority.GENERIC) {
                node.priority = PastelNetwork.Priority.MODERATE;
            }
            else {
                node.priority = PastelNetwork.Priority.HIGH;
            }
        }

        public boolean is(UpgradeSignature other) {
            return this == other;
        }

        public enum UpgradeCategory {
            STACK,
            LATENCY,
            NON_COMPOUNDING
        }
    }

    //TODO: this could maybe be made into a registry for addons to play with. Would need some bound checks tho.
    static {
        var builder = ImmutableMap.<Item, UpgradeSignature>builder();

        builder.put(SpectrumItems.RAW_BLOODSTONE, UpgradeSignature.of(SpectrumItems.RAW_BLOODSTONE, "weak_stack", 3, 0, 0, 2, 1, false, false, UpgradeSignature.UpgradeCategory.STACK));
        builder.put(SpectrumItems.REFINED_BLOODSTONE, UpgradeSignature.of(SpectrumItems.REFINED_BLOODSTONE, "strong_stack", 15, 0, 0, 4, 1, false, false, UpgradeSignature.UpgradeCategory.STACK));
        builder.put(SpectrumItems.RAW_MALACHITE, UpgradeSignature.of(SpectrumItems.RAW_MALACHITE, "weak_speed", 0, -5, 0, 1, 0.8F, false, false, UpgradeSignature.UpgradeCategory.LATENCY));
        builder.put(SpectrumItems.REFINED_MALACHITE, UpgradeSignature.of(SpectrumItems.REFINED_MALACHITE, "strong_speed", 0, -10, 0, 1, 0.5F, false, false, UpgradeSignature.UpgradeCategory.LATENCY));
        builder.put(SpectrumItems.RAW_AZURITE, UpgradeSignature.of(SpectrumItems.RAW_AZURITE, "weak_filter", 0, 0, 1, 1, 1, false, false, UpgradeSignature.UpgradeCategory.LATENCY));
        builder.put(SpectrumItems.REFINED_AZURITE, UpgradeSignature.of(SpectrumItems.REFINED_AZURITE, "strong_filter", 0, 0, 2, 1, 1, false, false, UpgradeSignature.UpgradeCategory.LATENCY));
        builder.put(SpectrumItems.RESONANCE_SHARD, UpgradeSignature.of(SpectrumItems.RESONANCE_SHARD, "rate", 0, 0, 0, 1, 1, false, true, UpgradeSignature.UpgradeCategory.NON_COMPOUNDING));
        builder.put(SpectrumItems.SHIMMERSTONE_GEM, UpgradeSignature.of(SpectrumItems.SHIMMERSTONE_GEM, "light", 0, 0, 0, 1, 1, true, false, UpgradeSignature.UpgradeCategory.NON_COMPOUNDING));
        builder.put(SpectrumItems.PURE_REDSTONE, ALWAYS_ON);
        builder.put(SpectrumItems.PURE_LAPIS, ALWAYS_OFF);
        builder.put(SpectrumItems.PURE_COAL, INVERTED);
        builder.put(SpectrumItems.PURE_ECHO, SENSOR);
        builder.put(SpectrumItems.PURE_QUARTZ, TRIGGER);
        builder.put(SpectrumItems.PURE_GLOWSTONE, LAMP);

        UPGRADES = builder.build();
    }
}