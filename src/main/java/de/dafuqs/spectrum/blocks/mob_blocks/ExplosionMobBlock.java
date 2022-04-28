package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExplosionMobBlock extends MobBlock {
	
	protected float power;
	protected boolean createFire;
	protected Explosion.DestructionType destructionType;
	
	public ExplosionMobBlock(Settings settings, float power, boolean createFire, Explosion.DestructionType destructionType) {
		super(settings);
		this.power = power;
		this.createFire = createFire;
		this.destructionType = destructionType;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText( "block.spectrum.explosion_mob_block.tooltip", power));
	}
	
	@Override
	public void trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
	
	}
	
}
