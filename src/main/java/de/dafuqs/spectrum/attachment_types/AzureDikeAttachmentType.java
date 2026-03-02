package de.dafuqs.spectrum.attachment_types;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.progression.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.neoforged.neoforge.attachment.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.handling.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AzureDikeAttachmentType {
	
	public static final String NAME = "azure_dike";
	
	public static final Codec<AzureDikeAttachmentType> CODEC = RecordCodecBuilder.create(i -> i.group(
					Codec.FLOAT.fieldOf("max_charges").forGetter(AzureDikeAttachmentType::getMaxCharges),
					Codec.FLOAT.fieldOf("current_charges").forGetter(AzureDikeAttachmentType::getCurrentCharges),
					Codec.INT.fieldOf("ticks_to_replenish_charge").forGetter(AzureDikeAttachmentType::getTicksToReplenishCharge),
					Codec.INT.fieldOf("ticks_to_replenish_charge_after_getting_hit").forGetter(AzureDikeAttachmentType::getTicksToReplenishChargeAfterGettingHit),
					Codec.INT.fieldOf("current_recharge_delay").forGetter(AzureDikeAttachmentType::getCurrentRechargeDelay)
			).apply(i, AzureDikeAttachmentType::new));
	
	public static final StreamCodec<FriendlyByteBuf, AzureDikeAttachmentType> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.FLOAT, AzureDikeAttachmentType::getMaxCharges,
			ByteBufCodecs.FLOAT, AzureDikeAttachmentType::getCurrentCharges,
			ByteBufCodecs.INT, AzureDikeAttachmentType::getTicksToReplenishCharge,
			ByteBufCodecs.INT, AzureDikeAttachmentType::getTicksToReplenishChargeAfterGettingHit,
			ByteBufCodecs.INT, AzureDikeAttachmentType::getTicksToReplenishCharge,
			AzureDikeAttachmentType::new
	);
	
	public static final IAttachmentCopyHandler<AzureDikeAttachmentType> COPY_HANDLER = (dike, holder, provider) -> {
		AzureDikeAttachmentType copy = new AzureDikeAttachmentType();
		copy.maxCharges = dike.maxCharges;
		copy.ticksToReplenishCharge = dike.ticksToReplenishCharge;
		copy.ticksToReplenishChargeAfterGettingHit = dike.ticksToReplenishChargeAfterGettingHit;
		return copy;
	};
	
	public static final AttachmentType<AzureDikeAttachmentType> ATTACHMENT_TYPE =
			AttachmentType.builder(AzureDikeAttachmentType::new)
					.serialize(CODEC)
					.copyOnDeath()
					.copyHandler(COPY_HANDLER)
					.sync(STREAM_CODEC)
					.build();
	
	public record Payload(int entityId, float maxProtection, float currentProtection, int ticksPerPointOfRecharge, int rechargeDelayTicksAfterGettingHit, int currentRechargeDelay) implements CustomPacketPayload {
		
		public Payload(int entityId, AzureDikeAttachmentType attachment) {
			this(entityId, attachment.maxCharges, attachment.currentCharges, attachment.ticksToReplenishCharge, attachment.ticksToReplenishChargeAfterGettingHit, attachment.currentRechargeDelay);
		}
		
		public static final StreamCodec<FriendlyByteBuf, Payload> CODEC = StreamCodec.composite(
				ByteBufCodecs.INT, Payload::entityId,
				ByteBufCodecs.FLOAT, Payload::maxProtection,
				ByteBufCodecs.FLOAT, Payload::currentProtection,
				ByteBufCodecs.INT, Payload::ticksPerPointOfRecharge,
				ByteBufCodecs.INT, Payload::rechargeDelayTicksAfterGettingHit,
				ByteBufCodecs.INT, Payload::currentRechargeDelay,
				Payload::new
		);
		
		public static final CustomPacketPayload.Type<Payload> TYPE = new CustomPacketPayload.Type<>(SpectrumCommon.locate(NAME));
		
		public static void execute(Payload payload, IPayloadContext context) {
			Level level = context.player().level();
			Optional.ofNullable(level.getEntity(payload.entityId))
					.ifPresent(e -> e.setData(ATTACHMENT_TYPE, new AzureDikeAttachmentType(
									payload.maxProtection, payload.currentProtection, payload.ticksPerPointOfRecharge,
									payload.rechargeDelayTicksAfterGettingHit, payload.currentRechargeDelay
							)
					));
		}
		
		@Override
		public @NotNull Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}
	
	public static final ResourceLocation AZURE_DIKE_BAR_TEXTURE = SpectrumCommon.locate("textures/gui/azure_dike_overlay.png");
	
	public final static int BASE_RECHARGE_DELAY_TICKS = 40;
	public final static int BASE_RECHARGE_DELAY_TICKS_AFTER_DAMAGE = 200;
	
	private float maxCharges = 0;
	private int ticksToReplenishCharge = 0;
	private int ticksToReplenishChargeAfterGettingHit = 0;
	
	private float currentCharges = 0;
	private int currentRechargeDelay = 0;
	
	public AzureDikeAttachmentType() {
	}
	
	public AzureDikeAttachmentType(float maxCharges, float currentCharges, int ticksToReplenishCharge, int ticksToReplenishChargeAfterGettingHit, int currentRechargeDelay) {
		this.maxCharges = maxCharges;
		this.ticksToReplenishCharge = ticksToReplenishCharge;
		this.ticksToReplenishChargeAfterGettingHit = ticksToReplenishChargeAfterGettingHit;
		this.currentCharges = currentCharges;
		this.currentRechargeDelay = currentRechargeDelay;
	}
	
	public float getCurrentCharges() {
		return this.currentCharges;
	}
	
	public float getMaxCharges() {
		return this.maxCharges;
	}
	
	public int getTicksToReplenishCharge() {
		return this.ticksToReplenishCharge;
	}
	
	public int getCurrentRechargeDelay() {
		return this.currentRechargeDelay;
	}
	
	public int getTicksToReplenishChargeAfterGettingHit() {
		return this.ticksToReplenishChargeAfterGettingHit;
	}
	
	/**
	 * Uses as much Azure Dike as possible to protect the Provider from incoming damage
	 *
	 * @param provider       The Component Provider
	 * @param incomingDamage The incoming damage
	 * @return All damage that could not be protected from
	 */
	public float absorbDamage(LivingEntity provider, float incomingDamage) {
		AzureDikeAttachmentType azureDike = provider.getData(AzureDikeAttachmentType.ATTACHMENT_TYPE);
		float passedDamage = incomingDamage;
		
		azureDike.currentRechargeDelay = azureDike.ticksToReplenishChargeAfterGettingHit;
		if (azureDike.currentCharges > 0) {
			float absorbedDamage = Math.min(azureDike.currentCharges, incomingDamage);
			azureDike.currentCharges -= absorbedDamage;
			
			passedDamage = incomingDamage - absorbedDamage;
		}
		
		if (incomingDamage - passedDamage > 0.0001F) {
			azureDike.sync(provider);
			if (provider instanceof ServerPlayer player)
				SpectrumAdvancementCriteria.AZURE_DIKE_CHARGE.trigger(player, azureDike.getCurrentCharges(), azureDike.getTicksToReplenishCharge(), -(incomingDamage - passedDamage));
		}
		
		return passedDamage;
	}
	
	public void set(float maxProtection, int rechargeDelayDefault, int fasterRechargeAfterDamageTicks, boolean resetCharge) {
		this.maxCharges = maxProtection;
		this.ticksToReplenishCharge = rechargeDelayDefault;
		this.ticksToReplenishChargeAfterGettingHit = fasterRechargeAfterDamageTicks;
		this.currentRechargeDelay = this.ticksToReplenishCharge;
		if (resetCharge) {
			this.currentCharges = 0;
		} else {
			this.currentCharges = Math.min(this.currentCharges, this.maxCharges);
		}
	}
	
	public void serverTick(LivingEntity livingEntity) {
		if (this.currentRechargeDelay > 0) {
			this.currentRechargeDelay--;
		} else if (this.currentCharges < this.maxCharges) {
			currentCharges = Math.min(maxCharges, currentCharges + 1);
			this.currentRechargeDelay = this.ticksToReplenishCharge;
			
			sync(livingEntity);
			if (livingEntity instanceof ServerPlayer serverPlayerEntity) {
				SpectrumAdvancementCriteria.AZURE_DIKE_CHARGE.trigger(serverPlayerEntity, this.currentCharges, this.ticksToReplenishCharge, 1);
			}
		}
	}
	
	public void sync(LivingEntity provider) {
		if (provider.level() instanceof ServerLevel serverLevel) {
			PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(provider.blockPosition()), new Payload(provider.getId(), this));
		}
	}
	
}
