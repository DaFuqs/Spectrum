package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.util.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import javax.annotation.*;
import java.util.*;
import java.util.function.*;

@Mixin(Cat.class)
public abstract class CatMixin extends TamableAnimal {
	
	protected CatMixin(EntityType<? extends TamableAnimal> entityType, Level level) {
		super(entityType, level);
	}
	
	@Inject(method = "setVariant(Lnet/minecraft/core/Holder;)V", at = @At("HEAD"))
	public void setVariant(Holder<CatVariant> variant, CallbackInfo ci) {
		if(variant.equals(SpectrumCatVariants.CATUS)) {
			this.setPathfindingMalus(PathType.WATER_BORDER, 0.0F);
			
			goalSelector.getAvailableGoals().stream()
					.filter( wrappedGoal -> wrappedGoal != null && wrappedGoal.getGoal() instanceof WaterAvoidingRandomStrollGoal)
					.forEach(wrappedGoal -> goalSelector.removeGoal(wrappedGoal.getGoal()));
			goalSelector.addGoal(4, new TryFindWaterGoal((Cat) (Object) this));
			goalSelector.addGoal(5, new CatusStrollGoal((Cat) (Object) this, 1.0F));
		} else if(variant.equals(SpectrumCatVariants.FELIS)) {
			goalSelector.getAvailableGoals().stream()
					.filter(wrappedGoal -> wrappedGoal != null && wrappedGoal.getGoal() instanceof TemptGoal)
					.forEach(wrappedGoal -> goalSelector.removeGoal(wrappedGoal.getGoal()));
			goalSelector.addGoal(4, new TemptGoal(this, 1.75, (stack) -> stack.is(ItemTags.CAT_FOOD), false));
		}
	}
	
	private static class CatusStrollGoal extends RandomStrollGoal {
		
		private static final int HORIZONTAL_RADIUS = 10;
		private static final int VERTICAL_DISTANCE_SOLID = 2;
		
		private static final int HORIZONTAL_RADIUS_WATER = 7;
		private static final int VERTICAL_DISTANCE_WATER = 1;
		
		public CatusStrollGoal(PathfinderMob mob, double speedModifier) {
			super(mob, speedModifier);
		}
		
		@Nullable
		protected Vec3 getPosition() {
			List<BlockPos> validShore = new ArrayList<>();
			
			for(BlockPos pos : BlockPos.betweenClosed(Mth.floor(this.mob.getX() - HORIZONTAL_RADIUS_WATER), Mth.floor(this.mob.getY() - VERTICAL_DISTANCE_WATER), Mth.floor(this.mob.getZ() - HORIZONTAL_RADIUS_WATER), Mth.floor(this.mob.getX() + HORIZONTAL_RADIUS_WATER), this.mob.getBlockY() + VERTICAL_DISTANCE_WATER, Mth.floor(this.mob.getZ() + HORIZONTAL_RADIUS_WATER))) {
				if (this.mob.level().getFluidState(pos).is(FluidTags.WATER)) {
					for(Direction direction : Direction.Plane.HORIZONTAL) {
						BlockPos relativePos = pos.relative(direction);
						BlockState relativeState = this.mob.level().getBlockState(relativePos);
						if(relativeState.isSolid()) {
							validShore.add(relativePos);
						}
					}
				}
			}
			
			if(!validShore.isEmpty()) {
				RandomSource random = this.mob.getRandom();
				return Vec3.atCenterOf(validShore.get(random.nextInt(validShore.size())));
			}
			
			return DefaultRandomPos.getPos(this.mob, HORIZONTAL_RADIUS, VERTICAL_DISTANCE_SOLID);
		}
		
	}
	
}
