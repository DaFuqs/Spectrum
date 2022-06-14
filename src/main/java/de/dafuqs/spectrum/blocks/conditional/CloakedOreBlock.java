package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.revelationary.api.revelations.RevelationAware;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class CloakedOreBlock extends OreBlock implements RevelationAware {
	
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
	
	@Deprecated
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		// workaround: since onStacksDropped() has no way of checking if it was
		// triggered by a player we have to cache that information here
		PlayerEntity lootPlayerEntity = RevelationAware.getLootPlayerEntity(builder);
		if (lootPlayerEntity != null && isVisibleTo(lootPlayerEntity)) {
			dropXP = true;
		} else {
			dropXP = false;
		}
		
		return super.getDroppedStacks(state, builder);
	}
	
	@Override
	public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack) {
		if (dropXP) {
			super.onStacksDropped(state, world, pos, stack);
		}
	}
	
}
