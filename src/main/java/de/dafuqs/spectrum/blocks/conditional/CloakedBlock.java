package de.dafuqs.spectrum.blocks.conditional;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.revelationary.api.revelations.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class CloakedBlock extends Block implements RevelationAware {
	
	public static final MapCodec<CloakedBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			propertiesCodec(),
			ResourceLocation.CODEC.fieldOf("advancement").forGetter(CloakedBlock::getCloakAdvancementIdentifier),
			BlockState.CODEC.fieldOf("cloak").forGetter(b -> b.getBlockStateCloaks().get(b.defaultBlockState()))
	).apply(instance, CloakedBlock::new));
	
	protected final ResourceLocation cloakAdvancementIdentifier;
	protected final BlockState cloakBlockState;
	
	public CloakedBlock(Properties settings, ResourceLocation cloakAdvancementIdentifier, BlockState cloakBlockState) {
		super(settings);
		this.cloakAdvancementIdentifier = cloakAdvancementIdentifier;
		this.cloakBlockState = cloakBlockState;
		RevelationAware.register(this);
	}
	
	@Override
	public MapCodec<? extends CloakedBlock> codec() {
		return CODEC;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		return Map.of(this.defaultBlockState(), cloakBlockState);
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return cloakAdvancementIdentifier;
	}
	
	@Override
	public Tuple<Item, Item> getItemCloak() {
		return new Tuple<>(this.asItem(), cloakBlockState.getBlock().asItem());
	}
	
}
