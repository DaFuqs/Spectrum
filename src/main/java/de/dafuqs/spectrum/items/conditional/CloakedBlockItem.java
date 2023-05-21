package de.dafuqs.spectrum.items.conditional;

import de.dafuqs.revelationary.api.revelations.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

import java.util.*;

public class CloakedBlockItem extends BlockItem implements RevelationAware {
	
	final Identifier cloakAdvancementIdentifier;
	final Item cloakItem;
	
	public CloakedBlockItem(Block block, Settings settings, Identifier cloakAdvancementIdentifier, Item cloakItem) {
		super(block, settings);
		this.cloakAdvancementIdentifier = cloakAdvancementIdentifier;
		this.cloakItem = cloakItem;
		RevelationAware.register(this);
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return cloakAdvancementIdentifier;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		return new Hashtable<>();
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this, cloakItem);
	}
	
}
