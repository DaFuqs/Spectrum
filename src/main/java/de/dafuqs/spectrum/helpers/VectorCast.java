package de.dafuqs.spectrum.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class VectorCast {

    private static final AllPass allPass = new AllPass();
    protected final Vec3d start, end;
    protected float radius;

    public VectorCast(Vec3d start, Vec3d end, float radius) {
        this.start = start;
        this.end = end;
        this.radius = radius;
    }

    public List<CollisionResult<Entity>> castForEntities(ServerWorld world, Predicate<Entity> preCollisionTestFiltering, Entity ... except) {
        var ray = getRelativeToOrigin(end);
        var casterBox = new Box(start, end).expand(ray.length() / 2);

        var entities = world.getEntitiesByClass(Entity.class, casterBox, preCollisionTestFiltering);

        var exceptSet = Arrays.asList(except);

        return entities.stream()
                .filter(entity -> !exceptSet.contains(entity))
                .map(entity -> processEntity(ray, entity, world))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public List<CollisionResult<BlockPos>> castForBlocks(ServerWorld world, Entity except, BiPredicate<ServerWorld, BlockPos> preCollisionTestFiltering) {
        var blockStart = new BlockPos(start);
        var blockEnd = new BlockPos(end);
        var ray = getRelativeToOrigin(end);

        var iterableBlocks = BlockPos.iterate(blockStart, blockEnd);
        var collisions = new ArrayList<CollisionResult<BlockPos>>();

        iterableBlocks.forEach(blockPos -> {
            if (!preCollisionTestFiltering.test(world, blockEnd))
                return;

            var collisionResult = processBlock(ray, blockPos, world);

            collisionResult.ifPresent(collisions::add);
        });

        return collisions;
    }

    private Optional<CollisionResult<Entity>> processEntity(Vec3d ray, Entity entity, ServerWorld world) {
        var hit = false;
        Vec3d closestPointToIntercept;

        collider: {
            var hitbox = entity.getBoundingBox().expand(radius);

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

            var product = ray.dotProduct(entityOrigin);

            var vectorAngle = Math.acos(product / (ray.length() * entityOrigin.length()));
            var entityOffset = Math.abs(Math.cos(vectorAngle) * entityOrigin.length());

            closestPointToIntercept = new Vec3d(
                    entityOffset * Math.sin(orientation.getLongitude()) * Math.cos(orientation.getLattitude()) + start.x,
                    entityOffset * Math.sin(orientation.getLongitude()) * Math.sin(orientation.getLattitude()) + start.y,
                    entityOffset * Math.cos(orientation.getLongitude()) + start.z
            );

            hit = hitbox.contains(closestPointToIntercept);
        }

        if (hit) {
            return Optional.of(new CollisionResult<>(world, entity, entity instanceof LivingEntity ? CollisionResult.CollisionType.LIVING : CollisionResult.CollisionType.NON_LIVING, closestPointToIntercept));
        }

        return Optional.empty();
    }

    private Optional<CollisionResult<BlockPos>> processBlock(Vec3d ray, BlockPos pos, ServerWorld world) {
        var hit = false;
        Vec3d closestPointToIntercept;

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
            var blockCenter = getRelativeToOrigin(Vec3d.ofCenter(pos));

            var product = ray.dotProduct(blockCenter);

            var vectorAngle = Math.acos(product / (ray.length() * blockCenter.length()));
            var entityOffset = Math.cos(vectorAngle) * blockCenter.length();

            closestPointToIntercept = new Vec3d(
                    entityOffset * Math.sin(orientation.getLattitude()) * Math.cos(orientation.getLongitude()) + start.x,
                    entityOffset * Math.sin(orientation.getLattitude()) * Math.sin(orientation.getLongitude()) + start.y,
                    entityOffset * Math.cos(orientation.getLattitude()) + start.z
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

    public boolean blockContains(BlockPos pos, Vec3d point) {
        return pos.getX() - radius <= point.getX() && point.getX() <= pos.getX() + 1 + radius &&
                pos.getY() - radius <= point.getY() && point.getY() <= pos.getY() + 1 + radius &&
                pos.getZ() - radius <= point.getZ() && point.getZ() <= pos.getZ() + 1 + radius;
    }

    public Orientation getOrientation() {
        var vector = getRelativeToOrigin(end);
        return Orientation.fromVector(vector);
    }

    public Vec3d getRelativeToOrigin(Vec3d vector) {
        return vector.subtract(start);
    }

    private static class AllPass implements TypeFilter<Entity, Entity> {

        @Nullable
        @Override
        public Entity downcast(Entity obj) {
            return obj;
        }

        @Override
        public Class<? extends Entity> getBaseClass() {
            return Entity.class;
        }
    }
}
