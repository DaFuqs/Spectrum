package de.dafuqs.spectrum.items.conditional;

import de.dafuqs.spectrum.interfaces.Cloakable;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.Hashtable;

public class CloakedItem extends Item implements Cloakable {
	
	Identifier cloakAdvancementIdentifier;
	Item cloakItem;
	
	public CloakedItem(Settings settings, Identifier cloakAdvancementIdentifier, Item cloakItem) {
		super(settings);
		this.cloakAdvancementIdentifier = cloakAdvancementIdentifier;
		this.cloakItem = cloakItem;
		
		registerCloak();
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return cloakAdvancementIdentifier;
	}
	
	@Override
	public Hashtable<BlockState, BlockState> getBlockStateCloaks() {
		return new Hashtable<>();
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this, cloakItem);
	}
	
}
