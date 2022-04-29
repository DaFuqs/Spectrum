package de.dafuqs.spectrum.blocks.mob_blocks;

import de.dafuqs.spectrum.mixin.accessors.SlimeEntityAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SlimeSizingMobBlock extends MobBlock {
	
	protected static final int MAX_SIZE = 8; // Huge Chungus
	protected int range;
	
	public SlimeSizingMobBlock(Settings settings, int range) {
		super(settings);
		this.range = range;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText( "block.spectrum.slime_sizing_mob_block.tooltip"));
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		List<SlimeEntity> slimeEntities = world.getNonSpectatingEntities(SlimeEntity.class, Box.of(Vec3d.ofCenter(blockPos), range, range, range));
		for(SlimeEntity slimeEntity : slimeEntities) {
			if(slimeEntity.getSize() < MAX_SIZE) {
				int newSize = slimeEntity.getSize() +1;
				((SlimeEntityAccessor) slimeEntity).invokeSetSize(newSize, true);
			}
		}
		return true;
	}
	
}
