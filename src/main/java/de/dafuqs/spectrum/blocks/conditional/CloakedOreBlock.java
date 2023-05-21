package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.revelationary.api.revelations.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.loot.context.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.*;

import java.util.*;

public class CloakedOreBlock extends ExperienceDroppingBlock implements RevelationAware {
	
	protected static boolean dropXP;
	protected final Identifier cloakAdvancementIdentifier;
	protected final BlockState cloakBlockState;
	
	public CloakedOreBlock(Settings settings, UniformIntProvider uniformIntProvider, Identifier cloakAdvancementIdentifier, BlockState cloakBlockState) {
		super(settings, uniformIntProvider);
		this.cloakAdvancementIdentifier = cloakAdvancementIdentifier;
		this.cloakBlockState = cloakBlockState;
		RevelationAware.register(this);
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
		hashtable.put(this.getDefaultState(), cloakBlockState);
		return hashtable;
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return cloakAdvancementIdentifier;
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), cloakBlockState.getBlock().asItem());
	}
	
	@Override
	@Deprecated
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		// workaround: since onStacksDropped() has no way of checking if it was
		// triggered by a player we have to cache that information here
		PlayerEntity lootPlayerEntity = RevelationAware.getLootPlayerEntity(builder);
		dropXP = lootPlayerEntity != null && isVisibleTo(lootPlayerEntity);
		
		return super.getDroppedStacks(state, builder);
	}
	
	@Override
	public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack, boolean dropExperience) {
		super.onStacksDropped(state, world, pos, stack, dropExperience && dropXP);
	}
	
}
