package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class MermaidsGemItem extends ItemNameBlockItem implements RevelationAware {
	
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("place_pedestal");
	
	public MermaidsGemItem(Block block, Properties settings) {
		super(block, settings);
		RevelationAware.register(this);
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		return Map.of();
	}
	
	@Override
	public Tuple<Item, Item> getItemCloak() {
		return new Tuple<>(this, Items.KELP);
	}
	
}
