package de.dafuqs.spectrum.blocks.conditional;

import com.mojang.serialization.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.blocks.decoration.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;

import java.util.*;

public class RadiatingEnderBlock extends SpectrumFacingBlock implements RevelationAware {
	
	public static final MapCodec<RadiatingEnderBlock> CODEC = simpleCodec(RadiatingEnderBlock::new);
	
	public RadiatingEnderBlock(Properties settings) {
		super(settings);
		RevelationAware.register(this);
	}
	
	@Override
	public MapCodec<? extends RadiatingEnderBlock> codec() {
		return CODEC;
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return SpectrumAdvancements.REVEAL_RADIATING_ENDER;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		return Map.of(this.defaultBlockState(), Blocks.COBBLESTONE.defaultBlockState());
	}
	
	@Override
	public Tuple<Item, Item> getItemCloak() {
		return new Tuple<>(this.asItem(), Blocks.COBBLESTONE.asItem());
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		Entity entity = builder.getOptionalParameter(LootContextParams.THIS_ENTITY);
		if (entity instanceof EnderMan) {
			return List.of(SpectrumBlocks.RADIATING_ENDER.asItem().getDefaultInstance());
		}
		return super.getDrops(state, builder);
	}
	
}
