package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.*;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ProjectileMobBlock extends MobBlock {
	
	protected final EntityType entityType;
	protected final SoundEvent triggerSoundEvent;
	protected final float force;
	protected final float divergence;
	
	public ProjectileMobBlock(Settings settings, EntityType entityType, SoundEvent triggerSoundEvent, float force, float divergence) {
		super(settings);
		this.entityType = entityType;
		this.triggerSoundEvent = triggerSoundEvent;
		this.force = force;
		this.divergence = divergence;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText( "block.spectrum.projectile_mob_block.tooltip", this.entityType.getName()));
	}
	
	@Override
	public void trigger(@NotNull ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		BlockPointer pointer = new BlockPointerImpl(world, blockPos);
		Position outputLocation = DispenserBlock.getOutputLocation(pointer);
		Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
		ProjectileEntity projectileEntity = createProjectile(world, outputLocation);
		projectileEntity.setVelocity(direction.getOffsetX(), ((float) direction.getOffsetY() + 0.1F), direction.getOffsetZ(), this.force, this.divergence);
		world.spawnEntity(projectileEntity);
		world.playSound(null, blockPos, this.triggerSoundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}
	
	public abstract ProjectileEntity createProjectile(ServerWorld world, Position position);
	
}
