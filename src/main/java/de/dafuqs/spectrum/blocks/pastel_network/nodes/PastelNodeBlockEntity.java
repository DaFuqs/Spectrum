package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.pastel.*;
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
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class PastelNodeBlockEntity extends BlockEntity implements FilterConfigurable, ExtendedScreenHandlerFactory, Stampable, PastelUpgradeable {

    public static final int MAX_FILTER_SLOTS = 25;
    public static final int SLOTS_PER_ROW = 5;
    public static final int DEFAULT_FILTER_SLOT_ROWS = 1;
	public static final int RANGE = 12;

    @Nullable
	protected PastelNetwork parentNetwork;
	protected Optional<UUID> parentID = Optional.empty();
    protected Optional<PastelUpgradeSignature> outerRing, innerRing, redstoneRing;
    protected Set<BlockPos> connectionMemory = new HashSet<>();
	protected long lastTransferTick = 0;
	protected final long cachedRedstonePowerTick = 0;
	protected boolean cachedNoRedstonePower = true;
    protected PastelNetwork.Priority priority = PastelNetwork.Priority.GENERIC;
    protected long itemCountUnderway = 0;

    // upgrade impl stuff
    protected boolean lit, triggerTransfer, triggered, waiting, lamp, sensor;
    protected int transferCount = PastelTransmissionLogic.DEFAULT_MAX_TRANSFER_AMOUNT;
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
        if (node.lamp && state.get(Properties.LIT) != node.canTransfer()) {
            world.setBlockState(pos, state.with(Properties.LIT, node.cachedNoRedstonePower));
        }

        if (world.isClient()) {
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

    public PastelNetwork.Priority getPriority() {
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
        lamp = false;
        sensor = false;
        var oldPriority = priority;
        priority = PastelNetwork.Priority.GENERIC;

        //First one processed can't compound because it has nothing to compound on
        outerRing.ifPresent(r -> apply(r, Collections.emptyList()));
        innerRing.ifPresent(r -> apply(r, outerRing.map(List::of).orElse(Collections.emptyList())));
        redstoneRing.ifPresent(r -> apply(r, Collections.emptyList()));

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
        var result = redstoneRing.map(r -> r.preProcessor
                .apply(new PastelUpgradeSignature.RedstoneContext(this, world, pos, cachedNoRedstonePower))).orElse(ActionResult.PASS);

        if (result == ActionResult.SUCCESS)
            return true;

        if (result == ActionResult.FAIL)
            return false;

        long time = this.getWorld().getTime();
        if (time > this.cachedRedstonePowerTick && !getCachedState().get(PastelNodeBlock.EMITTING)) {
            this.cachedNoRedstonePower = world.getReceivedRedstonePower(this.pos) == 0;
        }

        boolean notPowered = redstoneRing.map(r -> {
            var post = r.postProcessor.apply(new PastelUpgradeSignature.RedstoneContext(this, world, pos, cachedNoRedstonePower));

            if (post == ActionResult.SUCCESS)
                return true;

            if (post == ActionResult.FAIL)
                return false;

            return cachedNoRedstonePower;
        }).orElse(cachedNoRedstonePower);

        if (triggerTransfer) {
            if (waiting && notPowered) {
                waiting = false;
            }

            if (!triggered && !waiting && !notPowered) {
                triggered = true;
            }

            var canTransfer = this.getWorld().getTime() > lastTransferTick;
            return triggered && canTransfer;
        }

        return this.getWorld().getTime() > lastTransferTick && notPowered;
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
        if (nbt.contains("Network")) {
            UUID networkUUID = nbt.getUuid("Network");
            if (this.getWorld() == null) {
                this.parentID = Optional.of(networkUUID);
            } else {
                this.parentNetwork = Pastel.getInstance(world.isClient).JoinOrCreateNetwork(this, networkUUID);
            }
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
        if (nbt.contains("LastTransferTick", NbtElement.LONG_TYPE)) {
            this.lastTransferTick = nbt.getLong("LastTransferTick");
        }
        if (nbt.contains("ItemCountUnderway", NbtElement.LONG_TYPE)) {
            this.itemCountUnderway = nbt.getLong("ItemCountUnderway");
        }
        if(nbt.contains("OuterRing")) {
            outerRing = Optional.ofNullable(SpectrumRegistries.PASTEL_UPGRADE.get(Identifier.tryParse(nbt.getString("OuterRing"))));
        }
        if(nbt.contains("InnerRing")) {
            innerRing = Optional.ofNullable(SpectrumRegistries.PASTEL_UPGRADE.get(Identifier.tryParse(nbt.getString("InnerRing"))));
        }
        if(nbt.contains("RedstoneRing")) {
            redstoneRing = Optional.ofNullable(SpectrumRegistries.PASTEL_UPGRADE.get(Identifier.tryParse(nbt.getString("RedstoneRing"))));
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
        nbt.putBoolean("Triggered", this.triggered);
        nbt.putBoolean("Waiting", waiting);
        nbt.putLong("LastTransferTick", this.lastTransferTick);
        nbt.putLong("ItemCountUnderway", this.itemCountUnderway);
        if (this.getNodeType().usesFilters()) {
            writeFilterNbt(nbt, this.filterItems);
        }
        outerRing.ifPresent(r -> nbt.putString("OuterRing", SpectrumPastelUpgrades.toString(r)));
        innerRing.ifPresent(r -> nbt.putString("InnerRing", SpectrumPastelUpgrades.toString(r)));
        redstoneRing.ifPresent(r -> nbt.putString("RedstoneRing", SpectrumPastelUpgrades.toString(r)));
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
    public StampDataCategory getStampCategory() {
        return SpectrumStampDataCategories.PASTEL;
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
        if (priority == PastelNetwork.Priority.GENERIC) {
            priority = PastelNetwork.Priority.MODERATE;
        } else {
            priority = PastelNetwork.Priority.HIGH;
        }
    }
}