package de.dafuqs.spectrum.blocks.conditional.amaranth;

import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PottedAmaranthBushelBlock extends FlowerPotBlock implements RevelationAware {
	
	public PottedAmaranthBushelBlock(Block content, Properties settings) {
		super(content, settings);
		RevelationAware.register(this);
	}

//	@Override
//	public MapCodec<? extends PottedAmaranthBushelBlock> getCodec() {
//		//TODO: Make the codec
//		return null;
//	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return SpectrumAdvancements.REVEAL_AMARANTH;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> map = new Hashtable<>();
		map.put(this.defaultBlockState(), Blocks.POTTED_FERN.defaultBlockState());
		return map;
	}
	
	@Override
	public @Nullable Tuple<Item, Item> getItemCloak() {
		return null; // does not exist in item form
	}
	
}
