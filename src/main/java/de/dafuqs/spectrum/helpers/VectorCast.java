package de.dafuqs.spectrum.helpers;

import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.*;

import java.util.*;
import java.util.function.*;

public class VectorCast {
	
	protected final Vec3 start, end;
    protected float radius;
	
	public VectorCast(Vec3 start, Vec3 end, float radius) {
        this.start = start;
        this.end = end;
        this.radius = radius;
    }
	
	public List<CollisionResult<Entity>> castForEntities(ServerLevel world, Predicate<Entity> preCollisionTestFiltering, Entity... except) {
        var ray = getRelativeToOrigin(end);
		var casterBox = new AABB(start, end).inflate(ray.length() / 2);
		
		var entities = world.getEntitiesOfClass(Entity.class, casterBox, preCollisionTestFiltering);

        var exceptSet = Arrays.asList(except);

        return entities.stream()
                .filter(entity -> !exceptSet.contains(entity))
                .map(entity -> processEntity(ray, entity, world))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
	
	public List<CollisionResult<BlockPos>> castForBlocks(ServerLevel world, Entity except, BiPredicate<ServerLevel, BlockPos> preCollisionTestFiltering) {
		var blockStart = BlockPos.containing(start);
		var blockEnd = BlockPos.containing(end);
		var ray = getRelativeToOrigin(end);
		
		var iterableBlocks = BlockPos.betweenClosed(blockStart, blockEnd);
		var collisions = new ArrayList<CollisionResult<BlockPos>>();
	
		iterableBlocks.forEach(blockPos -> {
			if (!preCollisionTestFiltering.test(world, blockEnd))
				return;
		
			var collisionResult = processBlock(ray, blockPos, world);

            collisionResult.ifPresent(collisions::add);
        });

        return collisions;
    }
	
	private Optional<CollisionResult<Entity>> processEntity(Vec3 ray, Entity entity, ServerLevel world) {
        var hit = false;
		Vec3 closestPointToIntercept;

        collider: {
			var hitbox = entity.getBoundingBox().inflate(radius);

            if (hitbox.contains(end)) {
                closestPointToIntercept = end;
                hit = true;
                break collider;
            }

            if (hitbox.contains(start)) {
                closestPointToIntercept = start;
                hit = true;
                break collider;
            }

            var orientation = getOrientation();
            var entityOrigin = getRelativeToOrigin(hitbox.getCenter());
			
			var product = ray.dot(entityOrigin);

            var vectorAngle = Math.acos(product / (ray.length() * entityOrigin.length()));
            var entityOffset = Math.abs(Math.cos(vectorAngle) * entityOrigin.length());
			
			closestPointToIntercept = new Vec3(
					entityOffset * Math.sin(orientation.getLongitude()) * Math.cos(orientation.getLatitude()) + start.x,
					entityOffset * Math.sin(orientation.getLongitude()) * Math.sin(orientation.getLatitude()) + start.y,
					entityOffset * Math.cos(orientation.getLongitude()) + start.z
			);

            hit = hitbox.contains(closestPointToIntercept);
        }

        if (hit) {
            return Optional.of(new CollisionResult<>(world, entity, entity instanceof LivingEntity ? CollisionResult.CollisionType.LIVING : CollisionResult.CollisionType.NON_LIVING, closestPointToIntercept));
        }

        return Optional.empty();
    }
	
	private Optional<CollisionResult<BlockPos>> processBlock(Vec3 ray, BlockPos pos, ServerLevel world) {
        var hit = false;
		Vec3 closestPointToIntercept;

        collider: {

            if (blockContains(pos, end)) {
                closestPointToIntercept = end;
                hit = true;
                break collider;
            }

            if (blockContains(pos, start)) {
                closestPointToIntercept = start;
                hit = true;
                break collider;
            }

            var orientation = getOrientation();
			var blockCenter = getRelativeToOrigin(Vec3.atCenterOf(pos));
			
			var product = ray.dot(blockCenter);

            var vectorAngle = Math.acos(product / (ray.length() * blockCenter.length()));
            var entityOffset = Math.cos(vectorAngle) * blockCenter.length();
			
			closestPointToIntercept = new Vec3(
					entityOffset * Math.sin(orientation.getLatitude()) * Math.cos(orientation.getLongitude()) + start.x,
					entityOffset * Math.sin(orientation.getLatitude()) * Math.sin(orientation.getLongitude()) + start.y,
					entityOffset * Math.cos(orientation.getLatitude()) + start.z
			);

            hit = blockContains(pos, closestPointToIntercept);
        }

        if (hit) {
            return Optional.of(new CollisionResult<>(world, pos, CollisionResult.CollisionType.BLOCK, closestPointToIntercept));
        }

        return Optional.empty();
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
	
	public boolean blockContains(BlockPos pos, Vec3 point) {
		return pos.getX() - radius <= point.x() && point.x() <= pos.getX() + 1 + radius &&
				pos.getY() - radius <= point.y() && point.y() <= pos.getY() + 1 + radius &&
				pos.getZ() - radius <= point.z() && point.z() <= pos.getZ() + 1 + radius;
    }

    public Orientation getOrientation() {
        var vector = getRelativeToOrigin(end);
        return Orientation.fromVector(vector);
    }
	
	public Vec3 getRelativeToOrigin(Vec3 vector) {
        return vector.subtract(start);
    }
}
