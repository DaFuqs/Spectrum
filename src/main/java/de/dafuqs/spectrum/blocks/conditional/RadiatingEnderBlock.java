package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.item.*;
import net.minecraft.loot.context.*;
import net.minecraft.util.*;

import java.util.*;

public class RadiatingEnderBlock extends Block implements RevelationAware {
	
	public RadiatingEnderBlock(Settings settings) {
		super(settings);
		RevelationAware.register(this);
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return SpectrumAdvancements.REVEAL_RADIATING_ENDER;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		return Map.of(this.getDefaultState(), Blocks.COBBLESTONE.getDefaultState());
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), Blocks.COBBLESTONE.asItem());
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
		Entity entity = builder.getOptional(LootContextParameters.THIS_ENTITY);
		if (entity instanceof EndermanEntity) {
			return List.of(SpectrumBlocks.RADIATING_ENDER.asItem().getDefaultStack());
		}
		return super.getDroppedStacks(state, builder);
	}
	
}
