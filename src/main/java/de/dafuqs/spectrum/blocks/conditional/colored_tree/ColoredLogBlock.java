package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import com.mojang.serialization.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.deeper_down.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.storage.loot.*;
import net.neoforged.neoforge.common.*;
import org.jspecify.annotations.*;

import java.util.*;
import java.util.function.*;

public class ColoredLogBlock extends StrippingLootPillarBlock implements RevelationAware, ColoredTree {
	
	private static final Map<InkColor, ColoredLogBlock> LOGS = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	public ColoredLogBlock(Properties settings, Supplier<? extends RotatedPillarBlock> strippedBlock, InkColor color, ResourceKey<LootTable> strippingLootTableKey) {
		super(settings, strippedBlock, strippingLootTableKey);
		this.color = color;
		LOGS.put(color, this);
		RevelationAware.register(this);
	}

	@Override
	public @Nullable MapCodec<? extends ColoredLogBlock> codec() {
		// TODO: make the codec
		return null;
	}
	
	// sneakily turn into vanilla log if stripped
	@Override
	public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
		BlockState newState = super.getToolModifiedState(state, context, itemAbility, simulate);
		
		if(itemAbility == ItemAbilities.AXE_STRIP && newState != null && !this.isVisibleTo(context.getPlayer())) {
			if(newState.getBlock() instanceof RevelationAware revelationAware) {
				return revelationAware.getBlockStateCloaks().get(newState);
			}
		}
		return newState;
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return ColoredTree.getTreeCloakAdvancementIdentifier(ColoredTree.TreePart.LOG, this.color);
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> map = new Hashtable<>();
		for (Direction.Axis axis : RotatedPillarBlock.AXIS.getPossibleValues()) {
			map.put(this.defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis), Blocks.OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis));
		}
		return map;
	}
	
	@Override
	public Tuple<Item, Item> getItemCloak() {
		return new Tuple<>(this.asItem(), Blocks.OAK_LOG.asItem());
	}
	
	@Override
	public InkColor getColor() {
		return this.color;
	}

	public static @Nullable ColoredLogBlock byColor(InkColor color) {
		return LOGS.get(color);
	}
	
	public static Collection<ColoredLogBlock> all() {
		return LOGS.values();
	}
	
}
