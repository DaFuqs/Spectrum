package de.dafuqs.spectrum.blocks.conditional;

import com.mojang.serialization.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.blocks.decoration.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class FourLeafCloverBlock extends CloverBlock implements RevelationAware {
	
	public static final MapCodec<FourLeafCloverBlock> CODEC = simpleCodec(FourLeafCloverBlock::new);
	
	public FourLeafCloverBlock(Properties settings) {
		super(settings);
		RevelationAware.register(this);
	}

	@Override
	public MapCodec<? extends FourLeafCloverBlock> codec() {
		return CODEC;
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return SpectrumAdvancements.REVEAL_FOUR_LEAF_CLOVER;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> map = new Hashtable<>();
		map.put(this.defaultBlockState(), SpectrumBlocks.CLOVER.defaultBlockState());
		return map;
	}
	
	@Override
	public Tuple<Item, Item> getItemCloak() {
		return new Tuple<>(this.asItem(), SpectrumBlocks.CLOVER.asItem());
	}
	
}
