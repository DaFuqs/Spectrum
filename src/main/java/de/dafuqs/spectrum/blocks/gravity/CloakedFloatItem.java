package de.dafuqs.spectrum.blocks.gravity;

import de.dafuqs.spectrum.interfaces.Cloakable;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.Hashtable;

public class CloakedFloatItem extends FloatItem implements Cloakable {
	
	Identifier cloakAdvancementIdentifier;
	Item cloakItem;
	
	public CloakedFloatItem(Settings settings, float gravityMod, Identifier cloakAdvancementIdentifier, Item cloakItem) {
		super(settings, gravityMod);
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
