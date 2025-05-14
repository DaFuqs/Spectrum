package de.dafuqs.spectrum.blocks.gravity;

import de.dafuqs.revelationary.api.revelations.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class CloakedFloatItem extends FloatItem implements RevelationAware {
	
	final ResourceLocation cloakAdvancementIdentifier;
	final Item cloakItem;
	
	public CloakedFloatItem(Properties settings, float gravityMod, ResourceLocation cloakAdvancementIdentifier, Item cloakItem) {
		super(settings, gravityMod);
		this.cloakAdvancementIdentifier = cloakAdvancementIdentifier;
		this.cloakItem = cloakItem;
		RevelationAware.register(this);
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return cloakAdvancementIdentifier;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		return new Hashtable<>();
	}
	
	@Override
	public Tuple<Item, Item> getItemCloak() {
		return new Tuple<>(this, cloakItem);
	}
	
}
