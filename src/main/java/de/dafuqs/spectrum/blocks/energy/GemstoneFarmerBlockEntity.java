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
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventListener;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class GemstoneFarmerBlockEntity extends LootableContainerBlockEntity implements PlayerOwnedWithName, QueuedBlockPosEventTransferListener.Callback {
	
	private static final int RANGE = 12;
	private final QueuedBlockPosEventTransferListener blockPosEventTransferListener;
	private DefaultedList<ItemStack> inventory;
	private boolean listenerPaused;
	
	private UUID ownerUUID;
	private String ownerName;
	
	public GemstoneFarmerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntityRegistry.GEMSTONE_FARMER, blockPos, blockState);
		this.blockPosEventTransferListener = new QueuedBlockPosEventTransferListener(new BlockPositionSource(this.pos), RANGE, this);
		this.inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
		this.listenerPaused = false;
	}
	
	protected Text getContainerName() {
		if(hasOwner()) {
			return new TranslatableText("block.spectrum.gemstone_farmer").append(new TranslatableText("container.spectrum.owned_by_player", this.ownerName));
		} else {
			return new TranslatableText("block.spectrum.gemstone_farmer");
		}
	}
	
	public QueuedBlockPosEventTransferListener getEventListener() {
		return this.blockPosEventTransferListener;
	}
	
	public static void tick(World world, BlockPos pos, BlockState state, GemstoneFarmerBlockEntity blockEntity) {
		if (!world.isClient) {
			blockEntity.blockPosEventTransferListener.tick(world);
			if(world.getTime() % 1000 == 0) {
				blockEntity.listenerPaused = false; // try to reset from time to time, to search for new clusters, even if full
			}
		}
	}
	
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
	}
	
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (!this.serializeLootTable(nbt)) {
			Inventories.writeNbt(nbt, this.inventory);
		}
		nbt.putBoolean("ListenerPaused", this.listenerPaused);
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
		super.setStack(slot, stack);
		if(this.listenerPaused) {
			this.listenerPaused = false;
			harvestExistingClusters();
		}
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
		return event == SpectrumGameEvents.GEMSTONE_FARMER_FARMABLE_GROWN && !this.listenerPaused;
	}
	
	@Override
	public void triggerEvent(World world, GameEventListener listener, Object entry) {
		if(listener instanceof QueuedBlockPosEventTransferListener && this.world != null) {
			BlockPos eventPos = ((QueuedBlockPosEventTransferListener.BlockPosEventEntry) entry).eventSourceBlockPos;
			BlockState eventState = world.getBlockState(eventPos);
			if(eventState.isIn(SpectrumBlockTags.GEMSTONE_FARMER_FARMABLE)) {
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
							SpectrumAdvancementCriteria.GEMSTONE_FARMER_COLLECTING.trigger(serverPlayerEntity, drop);
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
			if (world.getBlockState(currPos).isIn(SpectrumBlockTags.GEMSTONE_FARMER_FARMABLE)) {
				this.blockPosEventTransferListener.acceptEvent(world, currPos, SpectrumGameEvents.GEMSTONE_FARMER_FARMABLE_GROWN, this.pos);
			}
		}
	}
}
