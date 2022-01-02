package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.blocks.pastel_network.PastelNetwork;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class PastelNetworkNodeBlockEntity extends BlockEntity {
	
	public static int RANGE = 16;
	
	public List<PastelNetworkNodeBlockEntity> connectedNodes = new ArrayList<>();
	
	protected PastelNetwork network;
	protected Inventory connectedInventory;
	
	public PastelNetworkNodeBlockEntity(BlockEntityType blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}
	
	public boolean canSee(PastelNetworkNodeBlockEntity node) {
		return node.pos.isWithinDistance(this.pos, RANGE);
	}
	
	public void transfer(ItemStack itemStack, PastelNetworkNodeBlockEntity node) {
	
	}
	
	public void updateConnectedInventory(World world, BlockPos blockPos, Direction facingDirection) {
		BlockEntity connectedBlockEntity = world.getBlockEntity(blockPos.offset(facingDirection.getOpposite()));
		
		if(connectedBlockEntity instanceof Inventory connectedInventory) {
			this.connectedInventory = connectedInventory;
		}
	}
	
	public void add(World world, BlockPos pos, Direction facingDirection) {
		updateConnectedInventory(world, pos, facingDirection);
		this.network = PastelNetwork.getNetworkForNewNode(this);
	}
	
	public void remove() {
		this.network.removeNode(this);
	}
	
}
