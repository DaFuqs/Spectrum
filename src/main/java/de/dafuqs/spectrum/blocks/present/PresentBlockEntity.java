package de.dafuqs.spectrum.blocks.present;

import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.DyeColor;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PresentBlockEntity extends BlockEntity {
	
	protected DefaultedList<ItemStack> stacks = DefaultedList.ofSize(PresentItem.MAX_STORAGE_STACKS, ItemStack.EMPTY);
	protected Map<DyeColor, Integer> colors = new HashMap<>();
	protected Optional<String> wrapper = Optional.empty();
	protected int openingTicks = 0;
	
	public PresentBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.PRESENT, pos, state);
	}
	
	public void setDataFromPresentStack(ItemStack stack) {
		List<ItemStack> s = PresentItem.getBundledStacks(stack).toList();
		for(int i = 0; i < PresentItem.MAX_STORAGE_STACKS && i < s.size(); i++) {
			this.stacks.set(i, s.get(i));
		}
		this.colors = PresentItem.getColors(stack);
		this.wrapper = PresentItem.getWrapper(stack);
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		Inventories.readNbt(nbt, this.stacks);
		this.colors = PresentItem.getColors(nbt);
		this.wrapper = PresentItem.getWrapper(nbt);
		if(nbt.contains("OpeningTick", NbtElement.INT_TYPE)) {
			this.openingTicks = nbt.getInt("OpeningTick");
		}
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if(!this.stacks.isEmpty()) { Inventories.writeNbt(nbt, this.stacks); }
		if(!this.colors.isEmpty()) { PresentItem.setColors(nbt, this.colors); }
		this.wrapper.ifPresent(s -> PresentItem.setWrapper(nbt, s));
		if(this.openingTicks > 0) { nbt.putInt("OpeningTick", this.openingTicks); }
	}
	
	@Override
	public void setStackNbt(ItemStack stack) {
		super.setStackNbt(stack);
	}
	
	public int openingTick() {
		openingTicks++;
		return this.openingTicks;
	}
	
}
