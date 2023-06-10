package de.dafuqs.spectrum.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public record CollisionResult<T>(World world, T collision, CollisionType type, Vec3d collisionPoint) {

    public boolean sanityCheck() {
        if (type != CollisionType.BLOCK) {
            var collisionBox = ((Entity) collision).getBoundingBox();
            return collisionBox.contains(collisionPoint);
        }
        else {
            var pos = BlockPos.ofFloored(collisionPoint);
            return world.getBlockState(pos).getRaycastShape(world, pos).getBoundingBoxes().stream().anyMatch(box -> box.contains(collisionPoint));
        }
    }

    public enum CollisionType {
        LIVING,
        NON_LIVING,
        BLOCK;
    }
}
