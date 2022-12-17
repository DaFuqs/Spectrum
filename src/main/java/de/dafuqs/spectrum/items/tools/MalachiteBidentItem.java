package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.items.SocketableItem;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;

public class MalachiteBidentItem extends TridentItem implements SocketableItem {
	
	public enum Variant {
		GLASS,
		FEROCIOUS_MOONSTONE,
		FRACTAL_MOONSTONE
	}
	
	private final Variant variant;
	
	public MalachiteBidentItem(Settings settings, Variant variant) {
		super(settings);
		this.variant = variant;
	}
	
	@Override
	public ItemStack socket(ItemStack socketableStack, ItemStack stackToSocket) {
		if(!stackToSocket.isOf(SpectrumItems.MOONSTONE_CORE)) {
			return ItemStack.EMPTY;
		}
		stackToSocket.decrement(1);
		
		ItemStack newStack = SpectrumItems.FEROCIOUS_MOONSTONE_CREST_BIDENT.getDefaultStack();
		newStack.setNbt(socketableStack.getNbt());
		return newStack;
	}
	
}
