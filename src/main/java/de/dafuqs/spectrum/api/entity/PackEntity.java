package de.dafuqs.spectrum.api.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.mob.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public interface PackEntity<T extends MobEntity & PackEntity<T>> {
	boolean hasOthersInGroup();
	
	@Nullable T getLeader();
	
	boolean isCloseEnoughToLeader();
	
	void leaveGroup();
	
	void moveTowardLeader();
	
	int getMaxGroupSize();
	
	int getGroupSize();
	
	void joinGroupOf(T groupLeader);
	
	default boolean hasLeader() {
		T leader = getLeader();
		return leader != null && leader.isAlive();
	}
	
	default boolean isDifferentPack(PackEntity<T> other) {
		T thisLeader = getLeader();
		if (thisLeader == null) {
			return false;
		}
		T otherLeader = other.getLeader();
		if (otherLeader == null) {
			return false;
		}
		return !Objects.equals(getLeader(), other.getLeader());
	}
	
	default boolean canHaveMoreInGroup() {
		return this.hasOthersInGroup() && getGroupSize() < this.getMaxGroupSize();
	}
	
	class FollowClanLeaderGoal<E extends MobEntity & PackEntity<E>> extends Goal {
		
		private static final int MIN_SEARCH_DELAY = 200;
		private final E entity;
		private int moveDelay;
		private int checkSurroundingDelay;
		
		public FollowClanLeaderGoal(E entity) {
			this.entity = entity;
			this.checkSurroundingDelay = this.getSurroundingSearchDelay(entity);
		}
		
		protected int getSurroundingSearchDelay(E fish) {
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
				createNewPack();
				return this.entity.hasLeader();
			}
		}
		
		@SuppressWarnings("unchecked")
		private void createNewPack() {
			List<E> possiblePackmates = this.entity.getWorld().getEntitiesByClass((Class<E>) this.entity.getClass(), this.entity.getBoundingBox().expand(8.0, 8.0, 8.0),
					(Predicate<LivingEntity>) livingEntity -> livingEntity instanceof PackEntity<?> packEntity && (packEntity.canHaveMoreInGroup() || !packEntity.hasLeader())
			);
			
			// search for an existing leader with a non-full group
			Optional<E> newLeader = possiblePackmates.stream()
					.filter(E::canHaveMoreInGroup)
					.findAny();
			
			if (newLeader.isEmpty()) {
				// promote a new creature to leader
				newLeader = possiblePackmates.stream()
						.filter(e -> !e.hasLeader())
						.findAny();
			}
			
			if (newLeader.isPresent()) {
				E leader = newLeader.get();
				possiblePackmates.stream()
						.filter((e) -> e != leader)
						.filter((e) -> !e.hasLeader())
						.limit(leader.getMaxGroupSize() - leader.getGroupSize())
						.forEach((e) -> e.joinGroupOf(leader));
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
