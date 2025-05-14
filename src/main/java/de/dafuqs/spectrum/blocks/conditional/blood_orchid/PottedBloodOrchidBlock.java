package de.dafuqs.spectrum.blocks.conditional.blood_orchid;

import de.dafuqs.revelationary.api.revelations.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PottedBloodOrchidBlock extends FlowerPotBlock implements RevelationAware {
	
	public PottedBloodOrchidBlock(Block content, Properties settings) {
		super(content, settings);
		RevelationAware.register(this);
	}

//	@Override
//	public MapCodec<? extends PottedBloodOrchidBlock> getCodec() {
//		//TODO: Make the codec
//		return null;
//	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return BloodOrchidBlock.ADVANCEMENT_IDENTIFIER;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> map = new Hashtable<>();
		map.put(this.defaultBlockState(), Blocks.POTTED_RED_TULIP.defaultBlockState());
		return map;
	}
	
	@Override
	public @Nullable Tuple<Item, Item> getItemCloak() {
		return null; // does not exist in item form
	}
	
}
