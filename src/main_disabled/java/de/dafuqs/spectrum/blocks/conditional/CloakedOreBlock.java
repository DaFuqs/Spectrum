package de.dafuqs.spectrum.blocks.conditional;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.storage.loot.*;

import java.util.*;

public class CloakedOreBlock extends DropExperienceBlock implements RevelationAware {

	public static final MapCodec<CloakedOreBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			IntProvider.codec(0, 10).fieldOf("experience").forGetter(b -> ((ExperienceDroppingBlockAccessor) b).getXpRange()),
			propertiesCodec(),
			ResourceLocation.CODEC.fieldOf("advancement").forGetter(CloakedOreBlock::getCloakAdvancementIdentifier),
			BlockState.CODEC.fieldOf("cloak").forGetter(b -> b.getBlockStateCloaks().get(b.defaultBlockState()))
	).apply(instance, CloakedOreBlock::new));

	protected static boolean dropXP;
	protected final ResourceLocation cloakAdvancementIdentifier;
	protected final BlockState cloakBlockState;
	
	public CloakedOreBlock(IntProvider experienceDropped, Properties settings, ResourceLocation cloakAdvancementIdentifier, BlockState cloakBlockState) {
		super(experienceDropped, settings);
		this.cloakAdvancementIdentifier = cloakAdvancementIdentifier;
		this.cloakBlockState = cloakBlockState;
		RevelationAware.register(this);
	}

	@Override
	public MapCodec<? extends CloakedOreBlock> codec() {
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

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		// workaround: since onStacksDropped() has no way of checking if it was
		// triggered by a player we have to cache that information here
		Player lootPlayerEntity = RevelationAware.getLootPlayerEntity(builder);
		dropXP = lootPlayerEntity != null && isVisibleTo(lootPlayerEntity);
		
		return super.getDrops(state, builder);
	}

	@Override
	public void spawnAfterBreak(BlockState state, ServerLevel world, BlockPos pos, ItemStack stack, boolean dropExperience) {
		super.spawnAfterBreak(state, world, pos, stack, dropExperience && dropXP);
	}
	
}
