package de.dafuqs.spectrum.attachment_types;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.entity.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.server.level.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.attachment.*;
import net.neoforged.neoforge.network.handling.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MiscPlayerDataAttachmentType {
	
	public static final String NAME = "misc_player_data";
	
	public static final Codec<MiscPlayerDataAttachmentType> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.fieldOf("ticky_before_sleep").forGetter(m -> m.ticksBeforeSleep),
			Codec.INT.fieldOf("sleeping_window").forGetter(m -> m.sleepingWindow),
			Codec.INT.fieldOf("sleep_invincibility").forGetter(m -> m.sleepInvincibility),
			MobEffectInstance.CODEC.listOf().fieldOf("sleep_consumable").forGetter(m -> m.sleepAlteringEffects)
	).apply(i, MiscPlayerDataAttachmentType::ofCodec));
	
	public static final AttachmentType<MiscPlayerDataAttachmentType> ATTACHMENT_TYPE = AttachmentType.builder((holder) -> new MiscPlayerDataAttachmentType((Player) holder)).serialize(CODEC).build();
	
	public MiscPlayerDataAttachmentType(@NotNull Player player) {
		this.player = player;
	}
	
	private MiscPlayerDataAttachmentType() {
	}
	
	private Player player;
	
	// Sleep
	private int ticksBeforeSleep = -1, sleepingWindow = -1, sleepInvincibility;
	private double lastSyncedSleepPotency = -2;
	private List<MobEffectInstance> sleepAlteringEffects = List.of();
	
	// Sword mechanics
	private boolean isLunging, bHopWindow, perfectCounter;
	private int parryTicks;
	
	// Gleaming Pin
	private long lastGleamingPinTriggerTick;
	
	public static MiscPlayerDataAttachmentType ofCodec(int ticksBeforeSleep, int sleepingWindow, int sleepInvincibility, List<MobEffectInstance> sleepAlteringEffects) {
		MiscPlayerDataAttachmentType data = new MiscPlayerDataAttachmentType();
		data.ticksBeforeSleep = ticksBeforeSleep;
		data.sleepingWindow = sleepingWindow;
		data.sleepInvincibility = sleepInvincibility;
		data.sleepAlteringEffects = sleepAlteringEffects;
		return data;
	}
	
	public void tick() {
		tickSleep();
		tickSwordMechanics();
		
		if (!player.level().isClientSide()) {
			double fortitude = player.getAttributeValue(SpectrumEntityAttributes.MENTAL_PRESENCE);
			if (lastSyncedSleepPotency != fortitude) {
				lastSyncedSleepPotency = fortitude;
				SyncMentalPresencePayload.sendMentalPresenceSync((ServerPlayer) player, fortitude);
			}
		}
	}
	
	private boolean isInModifiedMotionState() {
		return player.onGround() || player.isSwimming() || player.isFallFlying() || player.getAbilities().flying;
	}
	
	public void initiateLungeState() {
		isLunging = true;
		bHopWindow = true;
	}
	
	public void endLunge() {
		isLunging = false;
		bHopWindow = false;
	}
	
	public boolean isLunging() {
		return isLunging;
	}
	
	public void setParryTicks(int ticks) {
		parryTicks = ticks;
	}
	
	public void markForPerfectCounter() {
		perfectCounter = true;
	}
	
	public boolean consumePerfectCounter() {
		if (perfectCounter) {
			perfectCounter = false;
			return true;
		}
		
		return false;
	}
	
	public boolean isParrying() {
		return parryTicks > 0;
	}
	
	private void tickSwordMechanics() {
		if (parryTicks > 1) {
			parryTicks--;
		} else if (parryTicks == 1) {
			parryTicks = 0;
			consumePerfectCounter();
		}
		
		if (!bHopWindow && isLunging) {
			if (isInModifiedMotionState()) {
				isLunging = false;
			} else {
				bHopWindow = true;
			}
		} else if (isLunging && isInModifiedMotionState()) {
			bHopWindow = false;
		}
	}
	
	public float getFrictionModifiers() {
		return isLunging ? 0.04F : 0F;
	}
	
	private void tickSleep() {
		if (ticksBeforeSleep > 0) {
			ticksBeforeSleep--;
			
			if (ticksBeforeSleep == 0) {
				player.startSleeping(player.blockPosition());
				((PlayerEntityAccessor) player).spectrum$setSleepTimer(0);
				var world = player.level();
				if (!world.isClientSide())
					((ServerLevel) world).updateSleepingPlayerList();
			}
		}
		
		if (sleepInvincibility > 0) {
			sleepInvincibility--;
		}
		
		if (ticksBeforeSleep != 0)
			return;
		
		if (sleepingWindow > 0) {
			sleepingWindow--;
			if (sleepingWindow == 0) {
				failSleep();
			}
		}
	}
	
	private void failSleep() {
		if (!player.level().isClientSide()) {
			player.stopSleeping();
			resetSleepingState(true);
		}
	}
	
	public boolean isSleeping() {
		return ticksBeforeSleep == 0 && sleepingWindow > 0;
	}
	
	public boolean shouldLieDown() {
		return ticksBeforeSleep > 0;
	}
	
	public void notifyHit() {
		if (sleepInvincibility <= 0) {
			resetSleepingState(true);
		}
	}
	
	public void resetSleepingState(boolean applySleepAlteringEffects) {
		if (ticksBeforeSleep == -1) {
			return;
		}
		
		if (applySleepAlteringEffects) {
			for(MobEffectInstance instance : sleepAlteringEffects) {
				player.addEffect(instance);
			}
		}
		
		ticksBeforeSleep = -1;
		sleepingWindow = -1;
		sleepInvincibility = -1;
		sleepAlteringEffects = List.of();
	}
	
	public void setSleepTimers(int wait, int window, int invulnTicks) {
		ticksBeforeSleep = wait;
		sleepingWindow = window;
		sleepInvincibility = invulnTicks;
	}
	
	public void setLastSleepItem(@NotNull ItemStack stack) {
		this.sleepAlteringEffects = stack.getOrDefault(SpectrumDataComponentTypes.SLEEP_ALTERING_EFFECTS, List.of());
	}
	
	public static MiscPlayerDataAttachmentType get(@NotNull Player player) {
		MiscPlayerDataAttachmentType data = player.getData(ATTACHMENT_TYPE);
		if (data.player == null) {
			data.player = player;
		}
		return data;
	}
	
	public void setLastSyncedSleepPotency(double lastSyncedSleepPotency) {
		this.lastSyncedSleepPotency = lastSyncedSleepPotency;
	}
	
	public double getLastSyncedSleepPotency() {
		return lastSyncedSleepPotency;
	}
	
	public void setLastGleamingPinTriggerTick(long tick) {
		this.lastGleamingPinTriggerTick = tick;
	}
	
	public double getLastGleamingPinTriggerTick() {
		return lastGleamingPinTriggerTick;
	}
	
	public record Payload(UUID id, int ticksBeforeSleep, int sleepingWindow, int sleepInvincibility, List<MobEffectInstance> sleepAlteringEffects) implements CustomPacketPayload {
		
		public static final StreamCodec<RegistryFriendlyByteBuf, Payload> CODEC = StreamCodec.composite(
				UUIDUtil.STREAM_CODEC, Payload::id,
				ByteBufCodecs.INT, Payload::ticksBeforeSleep,
				ByteBufCodecs.INT, Payload::sleepingWindow,
				ByteBufCodecs.INT, Payload::sleepInvincibility,
				MobEffectInstance.STREAM_CODEC.apply(ByteBufCodecs.collection(NonNullList::createWithCapacity)), Payload::sleepAlteringEffects,
				Payload::new
		);
		
		public static final Type<MiscPlayerDataAttachmentType.Payload> TYPE = new CustomPacketPayload.Type<>(SpectrumCommon.locate(NAME));
		
		public static void execute(Payload payload, IPayloadContext context) {
			Player player = context.player().level().getPlayerByUUID(payload.id);
			if (player == null)
				return;
			
			MiscPlayerDataAttachmentType data = player.getData(ATTACHMENT_TYPE);
			data.ticksBeforeSleep = payload.ticksBeforeSleep();
			data.sleepingWindow = payload.sleepingWindow();
			data.sleepInvincibility = payload.sleepInvincibility();
			data.sleepAlteringEffects = payload.sleepAlteringEffects;
		}
		
		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}
	
}
