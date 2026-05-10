package de.dafuqs.spectrum.blocks.pedestal;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class PedestalRecipeInput implements RecipeInput {
	
	private static final List<ItemStack> FULL_GEMSTONE_POWDER_STACKS = List.of(
			new ItemStack(SpectrumItems.TOPAZ_POWDER, 64),
			new ItemStack(SpectrumItems.AMETHYST_POWDER, 64),
			new ItemStack(SpectrumItems.CITRINE_POWDER, 64),
			new ItemStack(SpectrumItems.ONYX_POWDER, 64),
			new ItemStack(SpectrumItems.MOONSTONE_POWDER, 64)
	);
	
	private final Level level;
	private final @Nullable Player player;
	private final CraftingInput craftingGridInput;
	private final List<ItemStack> gemstonePowderStacks;
	
	public PedestalRecipeInput(Level level, CraftingInput craftingGridInput, List<ItemStack> gemstonePowderStacks, @Nullable Player player) {
		this.level = level;
		this.player = player;
		this.craftingGridInput = craftingGridInput;
		this.gemstonePowderStacks = gemstonePowderStacks;
	}
	
	public CraftingInput getCraftingGridInput() {
		return craftingGridInput;
	}
	
	public static PedestalRecipeInput create(Level level, List<ItemStack> stacks, @Nullable Player player) {
		return new PedestalRecipeInput(level, CraftingInput.of(3, 3, stacks.subList(0, 9)), stacks.subList(9, 14), player);
	}
	
	public static PedestalRecipeInput createWithFullGemstonePowder(Level level, List<ItemStack> stacks, @Nullable Player player) {
		return new PedestalRecipeInput(level, CraftingInput.of(3, 3, stacks), FULL_GEMSTONE_POWDER_STACKS, player);
	}
	
	@Override
	public ItemStack getItem(int slot) {
		if (slot < 9) {
			return slot < craftingGridInput.size() ? craftingGridInput.getItem(slot) : ItemStack.EMPTY;
		}
		slot -= 9;
		return slot < gemstonePowderStacks.size() ? gemstonePowderStacks.get(slot) : ItemStack.EMPTY;
	}
	
	@Override
	public int size() {
		return craftingGridInput.size() + gemstonePowderStacks.size();
	}
	
	public int[] getCraftingGridSlots() {
		int size = craftingGridInput.size();
		int[] slots = new int[size];
		for (int i = 0; i < size; i++) {
			slots[i] = i;
		}
		return slots;
	}
	
	public @Nullable Player getPlayer() {
		return this.player;
	}
	
	public Level getLevel() {
		return this.level;
	}
	
}
