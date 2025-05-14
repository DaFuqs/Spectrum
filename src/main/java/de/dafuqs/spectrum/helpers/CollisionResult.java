package de.dafuqs.spectrum.helpers;

import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;

public record CollisionResult<T>(Level world, T collision, CollisionType type, Vec3 collisionPoint) {

    public boolean sanityCheck() {
        if (type != CollisionType.BLOCK) {
            var collisionBox = ((Entity) collision).getBoundingBox();
            return collisionBox.contains(collisionPoint);
        }
        else {
			var pos = BlockPos.containing(collisionPoint);
			return world.getBlockState(pos).getInteractionShape(world, pos).toAabbs().stream().anyMatch(box -> box.contains(collisionPoint));
        }
    }

    public enum CollisionType {
        LIVING,
        NON_LIVING,
        BLOCK
    }
}
