package de.dafuqs.spectrum.entity.entity;

import com.mojang.datafixers.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public interface PackEntity<T extends LivingEntity> {
	boolean hasOthersInGroup();
	
	boolean hasLeader();
	
	boolean isCloseEnoughToLeader();
	
	void leaveGroup();
	
	void moveTowardLeader();
	
	int getMaxGroupSize();
	
	void joinGroupOf(T groupLeader);
	
	default boolean canHaveMoreInGroup() {
		return true;
	}
	
	void pullInOthers(Stream<? extends PackEntity> stream);
	
	class FollowClanLeaderGoal<T extends LivingEntity & PackEntity<? super T>> extends Goal {
		
		private static final int MIN_SEARCH_DELAY = 200;
		private final T entity;
		private int moveDelay;
		private int checkSurroundingDelay;
		
		public FollowClanLeaderGoal(T entity) {
			this.entity = entity;
			this.checkSurroundingDelay = this.getSurroundingSearchDelay(entity);
		}
		
		protected int getSurroundingSearchDelay(T fish) {
			return toGoalTicks(MIN_SEARCH_DELAY + fish.getRandom().nextInt(MIN_SEARCH_DELAY) % 20);
		}
		
		@Override
		public boolean canStart() {
			if (this.entity.hasOthersInGroup()) {
				return false;
			} else if (this.entity.hasLeader()) {
				return true;
			} else if (this.checkSurroundingDelay > 0) {
				--this.checkSurroundingDelay;
				return false;
			} else {
				this.checkSurroundingDelay = this.getSurroundingSearchDelay(this.entity);
				List<T> list = (List<T>) this.entity.world.getEntitiesByClass(this.entity.getClass(), this.entity.getBoundingBox().expand(8.0, 8.0, 8.0),
						(Predicate<LivingEntity>) livingEntity -> livingEntity instanceof PackEntity packEntity && (packEntity.canHaveMoreInGroup() || !packEntity.hasLeader())
				);
				T packEntity = DataFixUtils.orElse(list.stream().filter(T::canHaveMoreInGroup).findAny(), this.entity);
				packEntity.pullInOthers(list.stream().filter((e) -> !e.hasLeader()));
				return this.entity.hasLeader();
			}
		}
		
		@Override
		public boolean shouldContinue() {
			return this.entity.hasLeader() && this.entity.isCloseEnoughToLeader();
		}
		
		@Override
		public void start() {
			this.moveDelay = 0;
		}
		
		@Override
		public void stop() {
			this.entity.leaveGroup();
		}
		
		@Override
		public void tick() {
			if (--this.moveDelay <= 0) {
				this.moveDelay = this.getTickCount(10);
				this.entity.moveTowardLeader();
			}
		}
		
	}
}
