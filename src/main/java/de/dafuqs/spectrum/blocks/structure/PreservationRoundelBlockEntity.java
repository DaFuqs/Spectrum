package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.blocks.item_roundel.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.interfaces.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.*;

import java.util.*;

public class PreservationRoundelBlockEntity extends ItemRoundelBlockEntity implements PlayerOwned {
	
	protected static final int INVENTORY_SIZE = 6;
	
	private UUID lastInteractedPlayer;
	protected Vec3i controllerOffset = new Vec3i(2, 2, 2);
	protected List<Item> requiredItems = new ArrayList<>();
	protected List<Vec3i> otherRoundelOffsets = new ArrayList<>();
	
	public PreservationRoundelBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.PRESERVATION_ROUNDEL, pos, state, INVENTORY_SIZE);
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.requiredItems = new ArrayList<>();
		if (nbt.contains("RequiredItems", NbtElement.LIST_TYPE)) {
			for (NbtElement e : nbt.getList("RequiredItems", NbtElement.STRING_TYPE)) {
				Item item = Registry.ITEM.get(Identifier.tryParse(e.asString()));
				if (item != Items.AIR) {
					this.requiredItems.add(item);
				}
			}
		}
		this.controllerOffset = null;
		if (nbt.contains("ControllerOffset", NbtElement.INT_ARRAY_TYPE)) {
			int[] offset = nbt.getIntArray("ControllerOffset");
			this.controllerOffset = new Vec3i(offset[0], offset[1], offset[2]);
		}
		otherRoundelOffsets = new ArrayList<>();
		if (nbt.contains("OtherRoundelOffsets", NbtElement.LIST_TYPE)) {
			for (NbtElement e : nbt.getList("OtherRoundelOffsets", NbtElement.INT_ARRAY_TYPE)) {
				int[] intArray = ((NbtIntArray) e).getIntArray();
				otherRoundelOffsets.add(new Vec3i(intArray[0], intArray[1], intArray[2]));
			}
		}
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (!this.requiredItems.isEmpty()) {
			NbtList itemList = new NbtList();
			for (Item requiredItem : this.requiredItems) {
				itemList.add(NbtString.of(Registry.ITEM.getId(requiredItem).toString()));
			}
			nbt.put("RequiredItems", itemList);
		}
		if (this.controllerOffset != null) {
			nbt.putIntArray("ControllerOffset", new int[]{this.controllerOffset.getX(), this.controllerOffset.getY(), this.controllerOffset.getZ()});
		}
		if (!this.otherRoundelOffsets.isEmpty()) {
			NbtList offsetList = new NbtList();
			for (Vec3i offset : this.otherRoundelOffsets) {
				offsetList.add(new NbtIntArray(new int[]{offset.getX(), offset.getY(), offset.getZ()}));
			}
			nbt.put("OtherRoundelOffsets", offsetList);
		}
	}
	
	@Override
	public boolean copyItemDataRequiresOperator() {
		return true;
	}
	
	@Override
	public void inventoryChanged() {
		super.inventoryChanged();
		if (!world.isClient && controllerOffset != null && inventoryAndConnectedOnesMatchRequirement()) {
			BlockEntity blockEntity = world.getBlockEntity(Support.directionalOffset(this.pos, this.controllerOffset, world.getBlockState(this.pos).get(PreservationControllerBlock.FACING)));
			if (blockEntity instanceof PreservationControllerBlockEntity controller) {
				// grant advancement
				controller.openExit();
			}
		}
	}
	
	public boolean inventoryAndConnectedOnesMatchRequirement() {
		if (!inventoryMatchesRequirement()) {
			return false;
		}
		
		
		for (Vec3i otherRoundelOffset : this.otherRoundelOffsets) {
			BlockPos otherRoundelPos = Support.directionalOffset(this.pos, otherRoundelOffset, world.getBlockState(this.pos).get(PreservationControllerBlock.FACING));
			if (world.getBlockEntity(otherRoundelPos) instanceof PreservationRoundelBlockEntity preservationRoundelBlockEntity) {
				if (!preservationRoundelBlockEntity.inventoryMatchesRequirement()) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public boolean inventoryMatchesRequirement() {
		if (this.requiredItems.isEmpty()) {
			return false;
		}
		
		List<Item> requirements = new ArrayList<>(this.requiredItems);
		
		for (int i = 0; i < size(); i++) {
			ItemStack slotStack = getStack(i);
			if (!slotStack.isEmpty()) {
				int usedCount = 0;
				for (int j = 0; j < requirements.size(); j++) {
					if (slotStack.getItem().equals(requirements.get(j))) {
						requirements.remove(j);
						j--;
						usedCount++;
						if (slotStack.getCount() == usedCount) {
							break;
						}
					}
				}
				if (usedCount != slotStack.getCount()) {
					return false;
				}
			}
		}
		
		if (requirements.isEmpty()) {
			SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) world, Vec3d.ofCenter(pos), ParticleTypes.HAPPY_VILLAGER, 10, new Vec3d(0.25, 0.5, 0.25), new Vec3d(0.1, 0.1, 0.1));
			world.playSound(null, pos, SpectrumSoundEvents.NEW_RECIPE, SoundCategory.BLOCKS, 1.0F, 1.0F);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean renderStacksAsIndividualItems() {
		return true;
	}
	
	@Override
	public UUID getOwnerUUID() {
		return this.lastInteractedPlayer;
	}
	
	@Override
	public void setOwner(PlayerEntity playerEntity) {
		this.lastInteractedPlayer = playerEntity.getUuid();
	}
	
}
