package de.dafuqs.spectrum.helpers;

import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

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
