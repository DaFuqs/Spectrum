package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EntitySummoningMobBlock extends MobBlock {
	
	protected final EntityType entityType;
	
	public EntitySummoningMobBlock(Settings settings, EntityType entityType) {
		super(settings);
		this.entityType = entityType;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText( "block.spectrum.entity_summoning_mob_block.tooltip", entityType.getName()));
	}
	
	@Override
	public void trigger(World world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		// TODO
	}
	
}
