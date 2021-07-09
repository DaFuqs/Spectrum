package de.dafuqs.pigment.entity.entity;

import com.google.common.collect.Lists;
import de.dafuqs.pigment.blocks.gravity.GravitableBlock;
import de.dafuqs.pigment.entity.PigmentEntityTypes;
import de.dafuqs.pigment.registries.PigmentBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.GameRules;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;

public class GravityBlockEntity extends Entity {

    public int floatTime;
    public boolean dropItem;
    public NbtCompound blockEntityData;
    private BlockState blockState;
    private float gravityModifier;
    private boolean hurtEntities;
    private int floatHurtMax;
    private float floatHurtAmount;
    protected static final TrackedData<BlockPos> ORIGIN = DataTracker.registerData(GravityBlockEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);

    public GravityBlockEntity(EntityType<? extends GravityBlockEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.blockState = Blocks.SAND.getDefaultState();
        this.gravityModifier = 1.0F;
        this.dropItem = true;
        this.floatHurtMax = 40;
        this.floatHurtAmount = 2.0F;
    }

    public GravityBlockEntity(World world, double x, double y, double z, BlockState gravityBlockState) {
        this(PigmentEntityTypes.GRAVITY_BLOCK, world);
        this.blockState = gravityBlockState;
        if(gravityBlockState.getBlock() instanceof GravitableBlock) {
            this.gravityModifier = ((GravitableBlock) gravityBlockState.getBlock()).getGravityMod();
        } else {
            this.gravityModifier = 1.0F;
        }
        this.inanimate = true;
        this.updatePosition(x, y, z);
        this.setVelocity(Vec3d.ZERO);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.setOrigin(new BlockPos(this.getPos()));
    }

    @Override
    public void updatePosition(double x, double y, double z) {
        if (dataTracker == null) {
            super.updatePosition(x, y, z);
        } else {
            BlockPos origin = getOrigin();
            VoxelShape colShape = blockState.getCollisionShape(world, origin);
            if (colShape.isEmpty()) {
                colShape = blockState.getOutlineShape(world, origin);
            }
            if (colShape.isEmpty()) {
                super.updatePosition(x, y, z);
            } else {
                this.setPos(x, y, z);
                Box box = colShape.getBoundingBox();
                this.setBoundingBox(box.offset(getPos().subtract(new Vec3d(MathHelper.lerp(0.5D, box.minX, box.maxX), 0, MathHelper.lerp(0.5D, box.minZ, box.maxZ)))));
            }
        }
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Environment(EnvType.CLIENT)
    public BlockPos getOrigin() {
        return this.dataTracker.get(ORIGIN);
    }

    public void setOrigin(BlockPos origin) {
        this.dataTracker.set(ORIGIN, origin);
        this.updatePosition(getX(), getY(), getZ());
    }

    @Override
    public boolean collides() {
        return !this.isRemoved() && !blockState.getCollisionShape(world, getOrigin()).isEmpty();
    }

    @Override
    public boolean isCollidable() {
        return this.collides();
    }

    @Override
    public boolean collidesWith(Entity other) {
        return !(other instanceof GravityBlockEntity) && super.collidesWith(other);
    }

    @Override
    public void tick() {

    }

    /**
     * Because this entity moves other entities, including the player, this entity has
     * to tick after the all other entities have ticked to prevent them phasing though this
     */
    public void postTickEntities() {
        if (this.blockState.isAir()) {
            this.discard();
        } else {
            Block block = this.blockState.getBlock();
            BlockPos blockPos;
            if (this.floatTime++ == 0) {
                blockPos = this.getBlockPos();
                if (this.world.getBlockState(blockPos).isOf(block)) {
                    this.world.removeBlock(blockPos, false);
                } else if (!this.world.isClient) {
                    this.discard();
                    return;
                }
            }

            if (!this.hasNoGravity()) {
                if (this.floatTime > 100) {
                    this.setVelocity(this.getVelocity().add(0.0D, (gravityModifier / 10), 0.0D));
                } else {
                    this.setVelocity(this.getVelocity().add(0.0D, Math.min(Math.sin((Math.PI * this.age) / 100D), 1) * (gravityModifier / 10), 0.0D));
                }
            }

            Box oldBox = getBoundingBox();

            this.move(MovementType.SELF, this.getVelocity());

            Box newBox = getBoundingBox();
            List<Entity> otherEntities = this.world.getOtherEntities(this, oldBox.union(newBox));
            for (Entity entity : otherEntities) {
                if (!(entity instanceof GravityBlockEntity) && !entity.noClip) {
                    if (entity.getY() < newBox.maxY) {
                        entity.updatePosition(entity.getPos().x, newBox.maxY, entity.getPos().z);
                    }
                }
            }

            if (!this.world.isClient) {
                blockPos = this.getBlockPos();
                boolean isConcrete = this.blockState.getBlock() instanceof ConcretePowderBlock;
                boolean shouldSolidify = isConcrete && this.world.getFluidState(blockPos).isIn(FluidTags.WATER);
                double speed = this.getVelocity().lengthSquared();

                if (isConcrete && speed > 1.0D) {
                    BlockHitResult blockHitResult = this.world.raycast(
                            new RaycastContext(new Vec3d(this.prevX, this.prevY, this.prevZ),
                            new Vec3d(this.getX(), this.getY(), this.getZ()), RaycastContext.ShapeType.COLLIDER,
                            RaycastContext.FluidHandling.SOURCE_ONLY, this));

                    if (blockHitResult.getType() != HitResult.Type.MISS && this.world.getFluidState(blockHitResult.getBlockPos()).isIn(FluidTags.WATER)) {
                        blockPos = blockHitResult.getBlockPos();
                        shouldSolidify = true;
                    }
                }

                if (!this.verticalCollision && !shouldSolidify) {
                    if (!this.world.isClient) {
                        if (this.floatTime > 100 && blockPos.getY() < 1) {
                            if (this.dropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                                this.dropItem(block);
                            }
                            this.discard();
                        } else if (floatTime > 500) {
                             this.discard();
                        }
                    }
                } else {
                    BlockState blockState = this.world.getBlockState(blockPos);
                    this.setVelocity(this.getVelocity().multiply(0.7, 0.5, 0.7));
                    this.discard();
                    boolean canReplace = blockState.canReplace(new AutomaticItemPlacementContext(this.world, blockPos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
                    if (!canReplace) {
                        canReplace = blockState.canReplace(new AutomaticItemPlacementContext(this.world, blockPos, Direction.UP, ItemStack.EMPTY, Direction.DOWN));
                    }
                    boolean canPlace = this.blockState.canPlaceAt(this.world, blockPos);

                    if (canReplace && canPlace) {
                        if (this.blockState.contains(Properties.WATERLOGGED) && this.world.getFluidState(blockPos).getFluid() == Fluids.WATER) {
                            this.blockState = this.blockState.with(Properties.WATERLOGGED, true);
                        }

                        if (this.world.setBlockState(blockPos, this.blockState, 3)) {
                            if (block instanceof GravitableBlock) {
                                ((GravitableBlock) block).onEndFloating(this.world, blockPos, this.blockState, blockState);
                            }

                            if (this.blockEntityData != null && this.blockState.getBlock() instanceof BlockWithEntity) {
                                BlockEntity blockEntity = this.world.getBlockEntity(blockPos);
                                if (blockEntity != null) {
                                    NbtCompound compoundTag = blockEntity.writeNbt(new NbtCompound());

                                    for (String keyName : this.blockEntityData.getKeys()) {
                                        NbtElement tag = this.blockEntityData.get(keyName);
                                        if (tag != null && !"x".equals(keyName) && !"y".equals(keyName) && !"z".equals(keyName)) {
                                            compoundTag.put(keyName, tag.copy());
                                        }
                                    }

                                    BlockEntity.createFromNbt(blockPos, this.blockState, compoundTag);
                                    blockEntity.markDirty();
                                }
                            }
                        } else if (this.dropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                            this.dropItem(block);
                        }
                    } else if (this.dropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                        this.dropItem(block);
                    }
                }
            }

            this.setVelocity(this.getVelocity().multiply(0.98D));
        }
    }

    @Override
    public boolean handleFallDamage(float distance, float damageMultiplier, DamageSource damageSource) {
        if (this.hurtEntities) {
            int i = MathHelper.ceil(distance - 1.0F);
            if (i > 0) {
                List<Entity> list = Lists.newArrayList(this.world.getOtherEntities(this, this.getBoundingBox()));
                DamageSource damagesource = DamageSource.FALLING_BLOCK;

                for (Entity entity : list) {
                    entity.damage(damagesource, Math.min(MathHelper.floor(i * this.floatHurtAmount), this.floatHurtMax));
                }
            }
        }
        return false;
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound compound) {
        compound.put("BlockState", NbtHelper.fromBlockState(this.blockState));
        compound.putInt("Time", this.floatTime);
        compound.putBoolean("DropItem", this.dropItem);
        compound.putBoolean("HurtEntities", this.hurtEntities);
        compound.putFloat("FallHurtAmount", this.floatHurtAmount);
        compound.putInt("FallHurtMax", this.floatHurtMax);
        if (this.blockEntityData != null) compound.put("TileEntityData", this.blockEntityData);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound compound) {
        this.blockState = NbtHelper.toBlockState(compound.getCompound("BlockState"));
        this.floatTime = compound.getInt("Time");
        if (compound.contains("HurtEntities", 99)) {
            this.hurtEntities = compound.getBoolean("HurtEntities");
            this.floatHurtAmount = compound.getFloat("FallHurtAmount");
            this.floatHurtMax = compound.getInt("FallHurtMax");
        } else if (this.blockState.isIn(BlockTags.ANVIL)) {
            this.hurtEntities = true;
        }

        if (compound.contains("DropItem", 99)) this.dropItem = compound.getBoolean("DropItem");

        if (compound.contains("TileEntityData", 10)) this.blockEntityData = compound.getCompound("TileEntityData");

        if (this.blockState.isAir()) this.blockState = PigmentBlocks.PALETUR_FRAGMENT_BLOCK.getDefaultState();
    }

    @Environment(EnvType.CLIENT)
    public World getWorldObj() {
        return this.world;
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    @Override
    public void populateCrashReport(CrashReportSection section) {
        super.populateCrashReport(section);
        section.add("Immitating BlockState", this.blockState.toString());
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    @Override
    public boolean entityDataRequiresOperator() {
        return true;
    }

    public void setFallingBlockPos(BlockPos pos) {
        this.dataTracker.set(ORIGIN, pos);
    }

    @Environment(EnvType.CLIENT)
    public BlockPos getFallingBlockPos() {
        return this.dataTracker.get(ORIGIN);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(ORIGIN, BlockPos.ORIGIN);
    }

    public interface ICPEM {
        void postTick();
    }

    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this, Block.getRawIdFromState(this.getBlockState()));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        this.blockState = Block.getStateFromRawId(packet.getEntityData());
        if(blockState.getBlock() instanceof GravitableBlock) {
            this.gravityModifier = ((GravitableBlock) blockState.getBlock()).getGravityMod();
        } else {
            this.gravityModifier = 1.0F;
        }
        this.inanimate = true;
        double d = packet.getX();
        double e = packet.getY();
        double f = packet.getZ();
        this.setPosition(d, e + (double)((1.0F - this.getHeight()) / 2.0F), f);
        this.setFallingBlockPos(this.getBlockPos());
    }

}