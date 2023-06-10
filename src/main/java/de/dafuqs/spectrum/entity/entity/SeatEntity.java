package de.dafuqs.spectrum.entity.entity;

import com.google.common.collect.UnmodifiableIterator;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SeatEntity extends Entity {

    private static final TrackedData<Integer> EMPTY_TICKS = DataTracker.registerData(SeatEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Optional<BlockState>> CUSHION = DataTracker.registerData(SeatEntity.class, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_STATE);
    private double offset = 0;

    public SeatEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public SeatEntity(World world, double offset) {
        super(SpectrumEntityTypes.SEAT, world);
        this.offset = offset;
    }


    @Override
    public void tick() {
        super.tick();

        var block = world.getBlockState(getBlockPos()).getBlock();
        var cushion = getCushion();

        if (cushion.isEmpty()) {
            setRemoved(RemovalReason.DISCARDED);
            return;
        }

        var state = cushion.get();

        if (!state.isOf(block)) {
            var iter = BlockPos.iterateOutwards(getBlockPos(), 1, 1, 1);
            var fail = true;

            for (BlockPos pos : iter) {
                var check = world.getBlockState(pos).getBlock();
                if (state.isOf(check)) {
                    updatePosition(pos.getX() + 0.5, pos.getY() + offset, pos.getZ() + 0.5);
                    fail = false;
                    break;
                }
            }

            if (fail)
                incrementEmptyTicks();
        }

        if (getFirstPassenger() == null)
            incrementEmptyTicks();
        else if (state.isOf(block)){
            setEmptyTicks(0);
        }

        if (getEmptyTicks() > 10) {
            setRemoved(RemovalReason.DISCARDED);
        }
    }

    public Optional<BlockState> getCushion() {
        return dataTracker.get(CUSHION);
    }

    public void setCushion(@NotNull BlockState state) {
        dataTracker.set(CUSHION, Optional.of(state));
    }

    public void setEmptyTicks(int ticks) {
        dataTracker.set(EMPTY_TICKS, ticks);
    }

    public int getEmptyTicks() {
        return dataTracker.get(EMPTY_TICKS);
    }

    public void incrementEmptyTicks() {
        setEmptyTicks(getEmptyTicks() + 1);
    }

    @Override
    protected void initDataTracker() {
        dataTracker.startTracking(EMPTY_TICKS, 0);
        dataTracker.startTracking(CUSHION, Optional.empty());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        setEmptyTicks(nbt.getInt("emptyTicks"));

        var state = NbtHelper.toBlockState(nbt.getCompound("BlockState"));
        dataTracker.set(CUSHION, Optional.ofNullable(state.isAir() ? null : state));

        offset = nbt.getDouble("offset");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("emptyTicks", getEmptyTicks());
        nbt.put("BlockState", NbtHelper.fromBlockState(dataTracker.get(CUSHION).orElse(Blocks.AIR.getDefaultState())));
        nbt.putDouble("offset", offset);
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public void pushAwayFrom(Entity entity) {}

    @Override
    public void move(MovementType movementType, Vec3d movement) {
        if (movementType != MovementType.PISTON)
            return;

        super.move(movementType, movement);
    }

    @Nullable
    private Vec3d locateSafeDismountingPos(Vec3d offset, LivingEntity passenger) {
        double d = this.getX() + offset.x;
        double e = this.getBoundingBox().minY + 0.5;
        double f = this.getZ() + offset.z;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        UnmodifiableIterator var10 = passenger.getPoses().iterator();

        while(var10.hasNext()) {
            EntityPose entityPose = (EntityPose)var10.next();
            mutable.set(d, e, f);
            double g = this.getBoundingBox().maxY + 0.75;

            while(true) {
                double h = this.world.getDismountHeight(mutable);
                if ((double)mutable.getY() + h > g) {
                    break;
                }

                if (Dismounting.canDismountInBlock(h)) {
                    Box box = passenger.getBoundingBox(entityPose);
                    Vec3d vec3d = new Vec3d(d, (double)mutable.getY() + h, f);
                    if (Dismounting.canPlaceEntityAt(this.world, passenger, box.offset(vec3d))) {
                        passenger.setPose(entityPose);
                        return vec3d;
                    }
                }

                mutable.move(Direction.UP);
                if (!((double)mutable.getY() < g)) {
                    break;
                }
            }
        }

        return null;
    }

    @Override
    public void requestTeleport(double destX, double destY, double destZ) {}

    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        Vec3d vec3d = getPassengerDismountOffset((double)this.getWidth(), (double)passenger.getWidth(), this.getYaw() + (passenger.getMainArm() == Arm.RIGHT ? 90.0F : -90.0F));
        Vec3d vec3d2 = this.locateSafeDismountingPos(vec3d, passenger);
        if (vec3d2 != null) {
            return vec3d2;
        } else {
            Vec3d vec3d3 = getPassengerDismountOffset((double)this.getWidth(), (double)passenger.getWidth(), this.getYaw() + (passenger.getMainArm() == Arm.LEFT ? 90.0F : -90.0F));
            Vec3d vec3d4 = this.locateSafeDismountingPos(vec3d3, passenger);
            return vec3d4 != null ? vec3d4 : this.getPos();
        }
    }
}
