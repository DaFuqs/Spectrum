package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.blocks.item_roundel.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;

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
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		this.requiredItems = new ArrayList<>();
		if (nbt.contains("RequiredItems", Tag.TAG_LIST)) {
			for (Tag e : nbt.getList("RequiredItems", Tag.TAG_STRING)) {
				Item item = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(e.getAsString()));
				if (item != Items.AIR) {
					this.requiredItems.add(item);
				}
			}
		}
		this.controllerOffset = null;
		if (nbt.contains("ControllerOffset", Tag.TAG_INT_ARRAY)) {
			int[] offset = nbt.getIntArray("ControllerOffset");
			this.controllerOffset = new Vec3i(offset[0], offset[1], offset[2]);
		}
		otherRoundelOffsets = new ArrayList<>();
		if (nbt.contains("OtherRoundelOffsets", Tag.TAG_LIST)) {
			for (Tag e : nbt.getList("OtherRoundelOffsets", Tag.TAG_INT_ARRAY)) {
				int[] intArray = ((IntArrayTag) e).getAsIntArray();
				otherRoundelOffsets.add(new Vec3i(intArray[0], intArray[1], intArray[2]));
			}
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		if (!this.requiredItems.isEmpty()) {
			ListTag itemList = new ListTag();
			for (Item requiredItem : this.requiredItems) {
				itemList.add(StringTag.valueOf(BuiltInRegistries.ITEM.getKey(requiredItem).toString()));
			}
			nbt.put("RequiredItems", itemList);
		}
		if (this.controllerOffset != null) {
			nbt.putIntArray("ControllerOffset", new int[]{this.controllerOffset.getX(), this.controllerOffset.getY(), this.controllerOffset.getZ()});
		}
		if (!this.otherRoundelOffsets.isEmpty()) {
			ListTag offsetList = new ListTag();
			for (Vec3i offset : this.otherRoundelOffsets) {
				offsetList.add(new IntArrayTag(new int[]{offset.getX(), offset.getY(), offset.getZ()}));
			}
			nbt.put("OtherRoundelOffsets", offsetList);
		}
	}
	
	@Override
	public boolean onlyOpCanSetNbt() {
		return true;
	}
	
	@Override
	public void inventoryChanged() {
		super.inventoryChanged();
		if (level instanceof ServerLevel && controllerOffset != null && inventoryAndConnectedOnesMatchRequirement()) {
			BlockEntity blockEntity = level.getBlockEntity(Support.directionalOffset(this.worldPosition, this.controllerOffset, level.getBlockState(this.worldPosition).getValue(PreservationControllerBlock.FACING)));
			if (blockEntity instanceof PreservationControllerBlockEntity controller) {
				// grant advancement
				controller.openExit();
			}
		}
	}
	
	public boolean inventoryAndConnectedOnesMatchRequirement() {
		if (!inventoryMatchesRequirement() || level == null) {
			return false;
		}
		
		
		for (Vec3i otherRoundelOffset : this.otherRoundelOffsets) {
			BlockPos otherRoundelPos = Support.directionalOffset(this.worldPosition, otherRoundelOffset, level.getBlockState(this.worldPosition).getValue(PreservationControllerBlock.FACING));
			if (level.getBlockEntity(otherRoundelPos) instanceof PreservationRoundelBlockEntity preservationRoundelBlockEntity) {
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
		
		for (int i = 0; i < getContainerSize(); i++) {
			ItemStack slotStack = getItem(i);
			if (!slotStack.isEmpty()) {
				int usedCount = 0;
				for (int j = 0; j < requirements.size(); j++) {
					if (slotStack.is(requirements.get(j))) {
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
		
		if (requirements.isEmpty() && level != null) {
			PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) level, Vec3.atCenterOf(worldPosition), ParticleTypes.HAPPY_VILLAGER, 10, new Vec3(0.25, 0.5, 0.25), new Vec3(0.1, 0.1, 0.1));
			level.playSound(null, worldPosition, SpectrumSoundEvents.NEW_RECIPE, SoundSource.BLOCKS, 1.0F, 1.0F);
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
	public void setOwner(Player playerEntity) {
		this.lastInteractedPlayer = playerEntity.getUUID();
		setChanged();
	}
	
}
