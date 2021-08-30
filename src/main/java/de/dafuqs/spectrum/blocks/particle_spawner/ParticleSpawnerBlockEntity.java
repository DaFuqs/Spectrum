package de.dafuqs.spectrum.blocks.particle_spawner;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.inventories.ParticleSpawnerScreenHandler;
import de.dafuqs.spectrum.networking.SpectrumC2SPackets;
import de.dafuqs.spectrum.particle.effect.ParticleSpawnerParticleEffect;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ParticleSpawnerBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory {

    public Identifier particleSpriteIdentifier;
    public float particlesPerSecond; // >1 = every xth tick
    public Vec3f particleSourcePosition;
    public Vec3f particleSourcePositionVariance;
    public Vec3f velocity;
    public Vec3f velocityVariance;
    public Vec3f color;
    public float scale;
    public float scaleVariance;
    public int lifetimeTicks;
    public int lifetimeVariance;
    public float gravity;
    public boolean collisions;

    public boolean initialized = false;

    public ParticleSpawnerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpectrumBlockEntityRegistry.PARTICLE_SPAWNER, blockPos, blockState);

        List<DefaultParticleType> availableParticleEffects = new ArrayList<>();
        availableParticleEffects.add(ParticleTypes.FLAME);
        availableParticleEffects.add(ParticleTypes.BUBBLE);

        this.particleSpriteIdentifier = new Identifier(SpectrumCommon.MOD_ID, "particle/sparklestone_sparkle");
        this.particlesPerSecond = 10.0F;
        this.particleSourcePosition = new Vec3f(0, 1, 0);
        this.particleSourcePositionVariance = new Vec3f(0.5F, 0, 0.5F);
        this.velocity = new Vec3f(0, 0.1F, 0);
        this.velocityVariance = new Vec3f(0.0F, 0.1F, 0);
        this.color = new Vec3f(1.0F, 1.0F, 1.0F);
        this.scale = 1.0F;
        this.scaleVariance = 0.2F;
        this.lifetimeTicks = 20;
        this.lifetimeVariance = 10;
        this.gravity = 1.0F;
        this.collisions = true;
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, ParticleSpawnerBlockEntity blockEntity) {
        if(world.getBlockState(pos).get(ParticleSpawnerBlock.POWERED).equals(true)) {
            if(blockEntity.initialized) {
                blockEntity.spawnParticles();
            } else {
                // when joined the server all values are 0
                // => request block entity settings from the server
                if(world.getTime() % 100 == 0) {
                    PacketByteBuf packetByteBuf = PacketByteBufs.create();
                    packetByteBuf.writeBlockPos(blockEntity.pos);
                    ClientPlayNetworking.send(SpectrumC2SPackets.REQUEST_PARTICLE_SPAWNER_SETTINGS_PACKET_ID, packetByteBuf);
                }
            }
        }
    }

    private void spawnParticles() {
        float particlesToSpawn = particlesPerSecond / 20F;
        while (particlesToSpawn > 1 || world.random.nextFloat() < particlesToSpawn) {
            spawnParticle(pos, world.random);
            particlesToSpawn--;
        }
    }

    private void spawnParticle(@NotNull BlockPos pos, Random random) {
        double randomOffsetX = particleSourcePositionVariance.getX() == 0 ? 0 : particleSourcePositionVariance.getX() - random.nextDouble() * particleSourcePositionVariance.getX() * 2.0D;
        double randomOffsetY = particleSourcePositionVariance.getY() == 0 ? 0 : particleSourcePositionVariance.getY() - random.nextDouble() * particleSourcePositionVariance.getY() * 2.0D;
        double randomOffsetZ = particleSourcePositionVariance.getZ() == 0 ? 0 : particleSourcePositionVariance.getZ() - random.nextDouble() * particleSourcePositionVariance.getZ() * 2.0D;

        double randomVelocityX = velocityVariance.getX() == 0 ? 0 : velocityVariance.getX() - random.nextDouble() * velocityVariance.getX() * 2.0D;
        double randomVelocityY = velocityVariance.getY() == 0 ? 0 : velocityVariance.getY() - random.nextDouble() * velocityVariance.getY() * 2.0D;
        double randomVelocityZ = velocityVariance.getZ() == 0 ? 0 : velocityVariance.getZ() - random.nextDouble() * velocityVariance.getZ() * 2.0D;

        float randomScale = this.scaleVariance == 0 ? this.scale : (float) (this.scale + this.scaleVariance - random.nextDouble() * this.scaleVariance * 2.0D);
        int randomLifetime = this.lifetimeVariance == 0 ? this.lifetimeTicks : (int) (this.lifetimeTicks + this.lifetimeVariance - random.nextDouble() * this.lifetimeVariance * 2.0D);

        if(randomScale > 0 && randomLifetime > 0) {
            MinecraftClient.getInstance().player.getEntityWorld().addParticle(
                    new ParticleSpawnerParticleEffect(this.particleSpriteIdentifier, this.gravity, this.color, randomScale, randomLifetime, this.collisions),
                    (double) pos.getX() + 0.5 + particleSourcePosition.getX() + randomOffsetX,
                    (double) pos.getY() + 0.5 + particleSourcePosition.getY() + randomOffsetY,
                    (double) pos.getZ() + 0.5 + particleSourcePosition.getZ() + randomOffsetZ,
                    velocity.getX() + randomVelocityX,
                    velocity.getY() + randomVelocityY,
                    velocity.getZ() + randomVelocityZ
            );
        }
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.putString("particle_identifier", this.particleSpriteIdentifier.toString());
        tag.putFloat("particles_per_tick", this.particlesPerSecond);
        tag.putFloat("source_pos_x", this.particleSourcePosition.getX());
        tag.putFloat("source_pos_y", this.particleSourcePosition.getY());
        tag.putFloat("source_pos_z", this.particleSourcePosition.getZ());
        tag.putFloat("source_pos_variance_x", this.particleSourcePositionVariance.getX());
        tag.putFloat("source_pos_variance_y", this.particleSourcePositionVariance.getY());
        tag.putFloat("source_pos_variance_z", this.particleSourcePositionVariance.getZ());
        tag.putFloat("source_velocity_x", this.velocity.getX());
        tag.putFloat("source_velocity_y", this.velocity.getY());
        tag.putFloat("source_velocity_z", this.velocity.getZ());
        tag.putFloat("source_velocity_variance_x", this.velocityVariance.getX());
        tag.putFloat("source_velocity_variance_y", this.velocityVariance.getY());
        tag.putFloat("source_velocity_variance_z", this.velocityVariance.getZ());
        tag.putFloat("particle_color_r", this.color.getX());
        tag.putFloat("particle_color_g", this.color.getY());
        tag.putFloat("particle_color_b", this.color.getZ());
        tag.putFloat("particle_scale", this.scale);
        tag.putFloat("particle_scale_variance", this.scaleVariance);
        tag.putInt("particle_lifetime", this.lifetimeTicks);
        tag.putInt("particle_lifetime_variance", this.lifetimeVariance);
        tag.putFloat("particle_gravity", this.gravity);
        tag.putBoolean("particle_collisions", this.collisions);
        return tag;
    }

    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        if(tag.getString("particle_identifier").isEmpty()) {
            this.initialized = false;
        } else {
            this.particleSpriteIdentifier = new Identifier(tag.getString("particle_identifier"));
            this.particlesPerSecond = tag.getFloat("particles_per_tick");
            this.particleSourcePosition = new Vec3f(tag.getFloat("source_pos_x"), tag.getFloat("source_pos_y"), tag.getFloat("source_pos_z"));
            this.particleSourcePositionVariance = new Vec3f(tag.getFloat("source_pos_variance_x"), tag.getFloat("source_pos_variance_y"), tag.getFloat("source_pos_variance_z"));
            this.velocity = new Vec3f(tag.getFloat("source_velocity_x"), tag.getFloat("source_velocity_y"), tag.getFloat("source_velocity_z"));
            this.velocityVariance = new Vec3f(tag.getFloat("source_velocity_variance_x"), tag.getFloat("source_velocity_variance_y"), tag.getFloat("source_velocity_variance_z"));
            this.color = new Vec3f(tag.getFloat("particle_color_r"), tag.getFloat("particle_color_g"), tag.getFloat("particle_color_b"));
            this.scale = tag.getFloat("particle_scale");
            this.scaleVariance = tag.getFloat("particle_scale_variance");
            this.lifetimeTicks = tag.getInt("particle_lifetime");
            this.lifetimeVariance = tag.getInt("particle_lifetime_variance");
            this.gravity = tag.getFloat("particle_gravity");
            this.collisions = tag.getBoolean("particle_collisions");
            this.initialized = true;
        }
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ParticleSpawnerScreenHandler(syncId, inv, this);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("block.spectrum.particle_spawner");
    }

    public void writeSettings(@NotNull PacketByteBuf packetByteBuf) {
        packetByteBuf.writeString(this.particleSpriteIdentifier.toString());
        packetByteBuf.writeFloat(this.particlesPerSecond);
        packetByteBuf.writeFloat(this.particleSourcePosition.getX());
        packetByteBuf.writeFloat(this.particleSourcePosition.getY());
        packetByteBuf.writeFloat(this.particleSourcePosition.getZ());
        packetByteBuf.writeFloat(this.particleSourcePositionVariance.getX());
        packetByteBuf.writeFloat(this.particleSourcePositionVariance.getY());
        packetByteBuf.writeFloat(this.particleSourcePositionVariance.getZ());
        packetByteBuf.writeFloat(this.velocity.getX());
        packetByteBuf.writeFloat(this.velocity.getY());
        packetByteBuf.writeFloat(this.velocity.getZ());
        packetByteBuf.writeFloat(this.velocityVariance.getX());
        packetByteBuf.writeFloat(this.velocityVariance.getY());
        packetByteBuf.writeFloat(this.velocityVariance.getZ());
        packetByteBuf.writeFloat(this.scale);
        packetByteBuf.writeFloat(this.scaleVariance);
        packetByteBuf.writeInt(this.lifetimeTicks);
        packetByteBuf.writeInt(this.lifetimeVariance);
        packetByteBuf.writeFloat(this.gravity);
        packetByteBuf.writeBoolean(this.collisions);
    }

    public void applySettings(PacketByteBuf buf) {
        Identifier particleSpriteIdentifier = new Identifier(buf.readString());
        float particlesPerSecond = buf.readFloat();
        Vec3f particleSourcePosition = new Vec3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
        Vec3f particleSourcePositionVariance = new Vec3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
        Vec3f velocity = new Vec3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
        Vec3f velocityVariance = new Vec3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
        Vec3f color = new Vec3f(1.0F, 1.0F, 1.0F);
        float scale = buf.readFloat();
        float scaleVariance = buf.readFloat();
        int lifetimeTicks = buf.readInt();
        int lifetimeVariance = buf.readInt();
        float gravity = buf.readFloat();
        boolean collisions = buf.readBoolean();

        applySettings(particleSpriteIdentifier, particlesPerSecond, particleSourcePosition, particleSourcePositionVariance,
                velocity, velocityVariance, color, scale, scaleVariance, lifetimeTicks, lifetimeVariance, gravity, collisions
        );
    }


    public void applySettings(Identifier particleSpriteIdentifier, float particlesPerSecond, Vec3f ParticleSourcePosition, Vec3f particleSourcePositionVariance, Vec3f velocity, Vec3f velocityVariance,
                              Vec3f color, float scale, float scaleVariance, int lifetimeTicks, int lifetimeVariance, float gravity, boolean collisions) {
        this.particleSpriteIdentifier = particleSpriteIdentifier;
        this.particlesPerSecond = particlesPerSecond;
        this.particleSourcePosition = ParticleSourcePosition;
        this.particleSourcePositionVariance = particleSourcePositionVariance;
        this.velocity = velocity;
        this.velocityVariance = velocityVariance;
        this.color = color;
        this.scale = scale;
        this.scaleVariance = scaleVariance;
        this.lifetimeTicks = lifetimeTicks;
        this.lifetimeVariance = lifetimeVariance;
        this.gravity = gravity;
        this.collisions = collisions;

        this.initialized = true;
        this.markDirty();
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

}
