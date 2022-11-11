package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.blocks.item_roundel.ItemRoundelBlockEntity;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class PreservationRoundelBlockEntity extends ItemRoundelBlockEntity {
	
	protected static final int INVENTORY_SIZE = 6;
	
	List<Item> limeTriggers = new ArrayList<>() {{
		add(SpectrumItems.CYAN_PIGMENT);
		add(SpectrumItems.YELLOW_PIGMENT);
		add(SpectrumItems.YELLOW_PIGMENT);
	}};
	
	List<Item> greenTriggers = new ArrayList<>() {{
		add(SpectrumItems.CYAN_PIGMENT);
		add(SpectrumItems.CYAN_PIGMENT);
		add(SpectrumItems.MAGENTA_PIGMENT);
		add(SpectrumItems.YELLOW_PIGMENT);
		add(SpectrumItems.YELLOW_PIGMENT);
	}};
	
	public PreservationRoundelBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.PRESERVATION_ROUNDEL, pos, state, INVENTORY_SIZE);
	}
	
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
	}
	
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
	}
	
	public boolean renderStacksAsIndividualItems() {
		return true;
	}
	
}
