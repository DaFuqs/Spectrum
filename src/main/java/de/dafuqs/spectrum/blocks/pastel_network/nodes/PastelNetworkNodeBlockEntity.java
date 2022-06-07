package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.blocks.pastel_network.PastelNetwork;
import de.dafuqs.spectrum.blocks.pastel_network.SchedulerMap;
import de.dafuqs.spectrum.blocks.pastel_network.TickLooper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.HashSet;

public abstract class PastelNetworkNodeBlockEntity extends BlockEntity {
	
	public static int RANGE = 16;
	final HashSet<BlockPos> receivers = new HashSet<>();
	private final TickLooper tickTimer = new TickLooper(40);
	private final SchedulerMap<BlockPos> particleCooldowns = new SchedulerMap<>();
	private final HashSet<BlockPos> senders = new HashSet<>();
	protected PastelNetwork network;
	protected Inventory connectedInventory;
	
	public PastelNetworkNodeBlockEntity(BlockEntityType blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}
	
	public void tick() {
		tickTimer.tick();
		if (world.isClient) {
			particleCooldowns.tick();
		}
	}
	
	public boolean canSee(PastelNetworkNodeBlockEntity node) {
		return node.pos.isWithinDistance(this.pos, RANGE);
	}
	
	public void transfer(ItemStack itemStack, PastelNetworkNodeBlockEntity node) {
	
	}
	
	public void updateConnectedInventory(World world, BlockPos blockPos, Direction facingDirection) {
		BlockEntity connectedBlockEntity = world.getBlockEntity(blockPos.offset(facingDirection.getOpposite()));
		if (connectedBlockEntity instanceof Inventory connectedInventory) {
			this.connectedInventory = connectedInventory;
		}
	}
	
	public void initialize(World world, BlockPos pos, Direction facingDirection) {
		tickTimer.checkCap();
		updateConnectedInventory(world, pos, facingDirection);
		if (this.network != null) {
			this.network = PastelNetwork.getNetworkForNewNode(this);
		}
		
		int range = this.getRange();
		int range2 = range / 2;
		for (int xOffset = -range; xOffset <= range; xOffset++) {
			for (int yOffset = -range2; yOffset <= 0; yOffset++) {
				for (int zOffset = -range; zOffset <= range; zOffset++) {
					BlockEntity te = world.getBlockEntity(pos.add(xOffset, yOffset, zOffset));
					if (this.canTransferTo(te)) {
						this.receivers.add(te.getPos());
					}
				}
			}
		}
	}
	
	private boolean canTransferTo(BlockEntity blockEntity) {
		return blockEntity instanceof PastelNetworkNodeBlockEntity;
	}
	
	public void remove() {
		if (this.network != null) {
			this.network.removeNode(this);
		}
	}
	
	public int getRange() {
		return 16;
	}
	
	protected final boolean queueParticle(BlockPos blockPos) {
		if (!particleCooldowns.containsKey(blockPos)) {
			particleCooldowns.put(blockPos, 3);
			return true;
		}
		return false;
	}
	
}
