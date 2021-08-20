package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ShootingStarEntity extends Entity {

    private static final TrackedData<ItemStack> STACK;
    private int age;
    public final float hoverHeight;

    public ShootingStarEntity(EntityType<? extends ShootingStarEntity> entityType, World world) {
        super(entityType, world);
        this.hoverHeight = (float)(Math.random() * 3.141592653589793D * 2.0D);
    }

    public ShootingStarEntity(World world, double x, double y, double z) {
        this(SpectrumEntityTypes.SHOOTING_STAR, world);
        this.setPosition(x, y, z);
        this.setYaw(this.random.nextFloat() * 360.0F);
        this.setVelocity(this.random.nextDouble() * 0.2D - 0.1D, 0.2D, this.random.nextDouble() * 0.2D - 0.1D);
    }

    public ShootingStarEntity(World world, double x, double y, double z, ItemStack stack) {
        this(world, x, y, z);
        this.setStack(stack);
    }

    public ShootingStarEntity(World world) {
        this(world, 0, 0, 0);
    }

    @Environment(EnvType.CLIENT)
    private ShootingStarEntity(ShootingStarEntity entity) {
        super(entity.getType(), entity.world);
        this.setStack(entity.getStack().copy());
        this.copyPositionAndRotation(entity);
        this.age = entity.age;
        this.hoverHeight = entity.hoverHeight;
    }

    public static void doShootingStarSpawns(ServerWorld serverWorld) {
        if(SpectrumCommon.CONFIG.ShootingStarWorlds.contains(serverWorld.getRegistryKey().getValue().toString())) {
            if(serverWorld.getTimeOfDay() % 100 == 0 && serverWorld.getTimeOfDay() > 13000 && serverWorld.getTimeOfDay() < 22000) {
                for (PlayerEntity playerEntity : serverWorld.getEntitiesByType(EntityType.PLAYER, Entity::isAlive)) {
                    if (serverWorld.getRandom().nextFloat() < SpectrumCommon.CONFIG.ShootingStarChance) {
                        spawnShootingStar(serverWorld, playerEntity);
                    }
                }
            }
        }
    }

    public static void spawnShootingStar(ServerWorld serverWorld, PlayerEntity playerEntity) {
        ItemStack itemStack = new ItemStack(SpectrumItems.SHOOTING_STAR);
        ShootingStarEntity shootingStarEntity = new ShootingStarEntity(serverWorld, playerEntity.getPos().getX(), playerEntity.getPos().getY() + 200, playerEntity.getPos().getZ(), itemStack);
        shootingStarEntity.addVelocity(3 - shootingStarEntity.random.nextFloat() * 6, 0, 3 - shootingStarEntity.random.nextFloat() * 6);
        serverWorld.spawnEntity(shootingStarEntity);
    }

    protected MoveEffect getMoveEffect() {
        return MoveEffect.NONE;
    }

    protected void initDataTracker() {
        this.getDataTracker().startTracking(STACK, ItemStack.EMPTY);
    }

    public void tick() {
        if (this.getStack().isEmpty()) {
            this.discard();
        } else {
            super.tick();

            this.prevX = this.getX();
            this.prevY = this.getY();
            this.prevZ = this.getZ();
            Vec3d vec3d = this.getVelocity();

            if (!this.hasNoGravity()) {
                this.setVelocity(this.getVelocity().add(0.0D, -0.008D, 0.0D));
            }

            if (this.world.isClient) {
                this.noClip = false;
            } else {
                this.noClip = !this.world.isSpaceEmpty(this, this.getBoundingBox().contract(1.0E-7D), (entity) -> {
                    return true;
                });
                if (this.noClip) {
                    this.pushOutOfBlocks(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getZ());
                }
            }

            if (!this.onGround || /*squaredHorizontalLength(this.getVelocity()) > 9.999999747378752E-6D ||*/ (this.age + this.getId()) % 4 == 0) {
                Vec3d velocity = this.getVelocity();
                world.addParticle(SpectrumParticleTypes.SHOOTING_STAR, this.getX(), this.getY(), this.getZ(), -velocity.x, -velocity.y, -velocity.z);

                this.move(MovementType.SELF, this.getVelocity());
                float g = 0.98F;
                if (this.onGround) {
                    g = this.world.getBlockState(new BlockPos(this.getX(), this.getY() - 1.0D, this.getZ())).getBlock().getSlipperiness() * 0.98F;
                }

                this.setVelocity(this.getVelocity().multiply(g, 0.98D, g));
                if (this.onGround) {
                    // convert to itemEntity
                    ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), this.getStack());
                    this.world.spawnEntity(itemEntity);
                    this.discard();
                }
            }

            this.velocityDirty |= this.updateWaterState();
            if (!this.world.isClient) {
                double d = this.getVelocity().subtract(vec3d).lengthSquared();
                if (d > 0.01D) {
                    this.velocityDirty = true;
                }
            }

            if (!this.world.isClient && this.age >= 6000) {
                this.discard();
            }

        }
    }

    public void writeCustomDataToNbt(NbtCompound tag) {
        tag.putShort("Age", (short)this.age);

        if (!this.getStack().isEmpty()) {
            tag.put("Item", this.getStack().writeNbt(new NbtCompound()));
        }
    }

    public void readCustomDataFromNbt(NbtCompound tag) {
        this.age = tag.getShort("Age");

        NbtCompound compoundTag = tag.getCompound("Item");
        this.setStack(ItemStack.fromNbt(compoundTag));
        if (this.getStack().isEmpty()) {
            this.discard();
        }
    }

    public void onPlayerCollision(PlayerEntity player) {
        if (!this.world.isClient) {
            player.damage(DamageSource.FALLING_BLOCK, 5);

            ItemStack itemStack = this.getStack();
            Item item = itemStack.getItem();
            int i = itemStack.getCount();
            if (player.getInventory().insertStack(itemStack)) {
                if(!player.isDead()) {
                    player.sendPickup(this, i);
                    if (itemStack.isEmpty()) {
                        this.discard();
                        itemStack.setCount(i);
                    }

                    Support.grantAdvancementCriterion((ServerPlayerEntity) player, "midgame/catch_shooting_star", "catch");
                    player.increaseStat(Stats.PICKED_UP.getOrCreateStat(item), i);

                    this.discard();
                }
            }
        }
    }

    public Text getName() {
        Text text = this.getCustomName();
        return (text != null ? text : new TranslatableText(this.getStack().getTranslationKey()));
    }

    public boolean isAttackable() {
        return false;
    }

    public ItemStack getStack() {
        return this.getDataTracker().get(STACK);
    }

    public void setStack(ItemStack stack) {
        this.getDataTracker().set(STACK, stack);
    }

    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if (STACK.equals(data)) {
            this.getStack().setHolder(this);
        }
    }

    @Environment(EnvType.CLIENT)
    public int getAge() {
        return this.age;
    }

    @Environment(EnvType.CLIENT)
    public float method_27314(float f) {
        return ((float)this.getAge() + f) / 20.0F + this.hoverHeight;
    }

    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    public SoundCategory getSoundCategory() {
        return SoundCategory.AMBIENT;
    }

    static {
        STACK = DataTracker.registerData(ShootingStarEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    }
}
