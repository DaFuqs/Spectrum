package de.dafuqs.spectrum.attachment_types;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import net.minecraft.client.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import net.neoforged.api.distmarker.*;
import net.neoforged.neoforge.attachment.*;
import net.neoforged.neoforge.common.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;
import javax.annotation.*;

import java.util.*;

//Primordial fire is so strong because it rends the soul.
public class PrimordialFireAttachmentType {
	
	public static final String NAME = "primordial_fire";
	
	public record Payload(int entityId, long burnTicks) implements CustomPacketPayload {
		
		public static final StreamCodec<FriendlyByteBuf, Payload> CODEC = StreamCodec.composite(
				ByteBufCodecs.INT, Payload::entityId,
				ByteBufCodecs.VAR_LONG, Payload::burnTicks,
				Payload::new
		);
		
		public static final Type<PrimordialFireAttachmentType.Payload> TYPE = new CustomPacketPayload.Type<>(SpectrumCommon.locate(NAME));
		
		public static void execute(Payload payload, IPayloadContext context) {
			Level level = context.player().level();
			Optional.ofNullable(level.getEntity(payload.entityId)).ifPresent(e -> e.setData(ATTACHMENT_TYPE, payload.burnTicks));
		}
		
		@Override
		public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}
	
	public static final AttachmentType<Long> ATTACHMENT_TYPE = AttachmentType
			.builder(() -> 0L)
			.serialize(Codec.LONG)
			.sync(ByteBufCodecs.VAR_LONG)
			.build();
	
	public static final float BASE_PERCENT_DAMAGE = 0.1F;
	
	private static Optional<OnPrimordialFireSoundInstance> soundInstance = Optional.empty();
	
	public static void setPrimordialFireTicks(LivingEntity entity, long ticks) {
		if (entity.getType().is(SpectrumEntityTypeTags.PRIMORDIAL_FIRE_IMMUNE)) {
			return;
		}
		
		entity.setData(ATTACHMENT_TYPE, ticks);
		sync(entity);
	}
	
	public static void addPrimordialFireTicks(LivingEntity entity, int ticks) {
		int i = SpectrumEnchantmentHelper.getEquipmentLevel(entity.level().registryAccess(), Enchantments.FIRE_PROTECTION, entity);
		if (i > 0) {
			ticks -= Mth.floor(ticks * i * 0.15F);
		}
		
		setPrimordialFireTicks(entity, entity.getData(ATTACHMENT_TYPE) + ticks);
	}
	
	private static void sync(LivingEntity entity) {
		if (entity.level() instanceof ServerLevel serverLevel) {
			PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(entity.blockPosition()), new Payload(entity.getId(), entity.getData(ATTACHMENT_TYPE)));
		}
	}
	
	public static boolean isOnPrimordialFire(LivingEntity entity) {
		return entity.getData(ATTACHMENT_TYPE) > 0;
	}
	
	public static boolean putOut(LivingEntity entity) {
		if (entity.getData(ATTACHMENT_TYPE) == 0)
			return false;
		
		entity.setData(ATTACHMENT_TYPE, 0L);
		sync(entity);
		return true;
	}
	
	public static void tick(LivingEntity entity) {
		Level level = entity.level();
		long primordialFireTicks = entity.getData(ATTACHMENT_TYPE);
		if(level.isClientSide) {
			clientTick(entity, primordialFireTicks);
		} else {
			serverTick(entity, primordialFireTicks);
		}
	}
	
	protected static void serverTick(LivingEntity entity, long primordialFireTicks) {
		if (primordialFireTicks == 0)
			return;
		
		var damageScaling = getDamage(entity);
		entity.hurt(SpectrumDamageTypes.primordialFire(entity.level()), damageScaling);
		
		if (entity.tickCount % 20 == 0) {
			entity.hurt(SpectrumDamageTypes.primordialFire(entity.level()), 2.0F);
		}
		
		primordialFireTicks -= entity.getFluidHeight(FluidTags.WATER) > 0 ? 3 : 1;
		entity.setData(ATTACHMENT_TYPE, primordialFireTicks);
		
		// was on fire, but is not any longer
		if (primordialFireTicks <= 0) {
			sync(entity);
		}
	}
	
	public static float getDamage(LivingEntity entity) {
		float baseDamage = BASE_PERCENT_DAMAGE;
		
		//Bosses have great and exceptional souls that can resist a lot more.
		//95% less damage to them
		if (entity.getType().is(Tags.EntityTypes.BOSSES))
			baseDamage /= 20F;
		
		//Fire immune entities can have a lil res, as a treat
		float fireImmunityMultiplier = entity.fireImmune() ? 0.25F : 1;
		return baseDamage * fireImmunityMultiplier * entity.getMaxHealth();
	}
	
	@OnlyIn(Dist.CLIENT)
	protected static void clientTick(LivingEntity entity, long primordialFireTicks) {
		if (primordialFireTicks > 0) {
			if (entity.equals(Minecraft.getInstance().player) && primordialFireTicks > 2 && soundInstance.isEmpty()) {
				soundInstance = Optional.of(new OnPrimordialFireSoundInstance((Player) entity));
				Minecraft.getInstance().getSoundManager().play(soundInstance.get());
			}
			
			double fluidHeight = entity.getFluidTypeHeight(NeoForgeMod.WATER_TYPE.value());
			if (fluidHeight > 0) {
				
				Level world = entity.level();
				RandomSource random = world.random;
				Vec3 pos = entity.position();
				
				for (int i = 0; i < 2; i++) {
					world.addParticle(ParticleTypes.BUBBLE_POP, entity.getRandomX(1), pos.y() + Math.min(fluidHeight, entity.getBbHeight()) * random.nextFloat(), entity.getRandomZ(1), 0.0, 0.04, 0.0);
					world.addParticle(ParticleTypes.SMOKE, entity.getRandomX(1), pos.y() + Math.min(fluidHeight, entity.getBbHeight()) * random.nextFloat(), entity.getRandomZ(1), 0.0, 0.04, 0.0);
				}
				if (world.random.nextInt(12) == 0) {
					entity.playSound(SoundEvents.FIRE_EXTINGUISH, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F);
				}
			}
		} else if (entity.equals(Minecraft.getInstance().player) && soundInstance.isPresent()) {
			soundInstance = Optional.empty();
		}
	}
	
}
