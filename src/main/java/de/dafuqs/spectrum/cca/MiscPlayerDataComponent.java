package de.dafuqs.spectrum.cca;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.entity.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;
import org.ladysnake.cca.api.v3.component.sync.*;
import org.ladysnake.cca.api.v3.component.tick.*;

import java.util.*;

/**
 * Because not every niche thing can have its own component
 */
public class MiscPlayerDataComponent implements AutoSyncedComponent, CommonTickingComponent {
    
    public static final org.ladysnake.cca.api.v3.component.ComponentKey<MiscPlayerDataComponent> MISC_PLAYER_DATA_COMPONENT = org.ladysnake.cca.api.v3.component.ComponentRegistry.getOrCreate(SpectrumCommon.locate("misc_player_data"), MiscPlayerDataComponent.class);
	private final Player player;

    // Sleep
    private int ticksBeforeSleep = -1, sleepingWindow = -1, sleepInvincibility;
    private double lastSyncedSleepPotency = -2;
    private Optional<SleepAlteringItem> sleepConsumable = Optional.empty();

    // Sword mechanics
    private boolean isLunging, bHopWindow, perfectCounter;
    private int parryTicks;
	
	public MiscPlayerDataComponent(Player player) {
        this.player = player;
    }


    @Override
    public void tick() {
        tickSleep();
        tickSwordMechanics();
    }

    @Override
    public void serverTick() {
        org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent.super.serverTick();

        var fortitude = player.getAttributeValue(SpectrumEntityAttributes.MENTAL_PRESENCE);
        if (lastSyncedSleepPotency != fortitude) {
            lastSyncedSleepPotency = fortitude;
			SyncMentalPresencePayload.sendMentalPresenceSync((ServerPlayer) player, fortitude);
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
        }
        else if (parryTicks == 1) {
            parryTicks = 0;
            consumePerfectCounter();
        }

        if (!bHopWindow && isLunging) {
            if (isInModifiedMotionState()) {
                isLunging = false;
            }
            else {
                bHopWindow = true;
            }
        }
        else if (isLunging && isInModifiedMotionState()) {
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
                ((PlayerEntityAccessor) player).setSleepTimer(0);
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

    public void resetSleepingState(boolean canApplyPenalties) {
        if (ticksBeforeSleep == -1)
            return;

        ticksBeforeSleep = -1;
        sleepingWindow = -1;
        sleepInvincibility = -1;

        if (canApplyPenalties)
            sleepConsumable.ifPresent(p -> p.applyPenalties(player));

        sleepConsumable = Optional.empty();
    }

    public void setSleepTimers(int wait, int window, int invulnTicks) {
        ticksBeforeSleep = wait;
        sleepingWindow = window;
        sleepInvincibility = invulnTicks;
    }
    
    public void setLastSleepItem(@NotNull SleepAlteringItem item) {
        this.sleepConsumable = Optional.of(item);
    }

    @Override
	public void readFromNbt(CompoundTag tag, HolderLookup.@NotNull Provider wrapperLookup) {
        ticksBeforeSleep = tag.getInt("ticksBeforeSleep");
        sleepingWindow = tag.getInt("sleepingWindow");
        sleepInvincibility = tag.getInt("sleepInvincibility");

        if (tag.contains("sleepConsumable")) {
			sleepConsumable = Optional.of((SleepAlteringItem) BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(tag.getString("sleepConsumable"))));
        }
    }

    @Override
	public void writeToNbt(CompoundTag tag, HolderLookup.@NotNull Provider wrapperLookup) {
        tag.putInt("ticksBeforeSleep", ticksBeforeSleep);
        tag.putInt("sleepingWindow", sleepingWindow);
        tag.putInt("sleepInvincibility", sleepInvincibility);

        sleepConsumable
                .map(sleepPenalizingItem -> (Item) sleepPenalizingItem)
				.map(Item::builtInRegistryHolder)
				.flatMap(Holder.Reference::unwrapKey)
				.map(ResourceKey::location)
                .ifPresent(id -> tag.putString("sleepConsumable", id.toString()));
    }
	
	public static MiscPlayerDataComponent get(@NotNull Player player) {
        return MISC_PLAYER_DATA_COMPONENT.get(player);
    }

    public void setLastSyncedSleepPotency(double lastSyncedSleepPotency) {
        this.lastSyncedSleepPotency = lastSyncedSleepPotency;
    }

    public double getLastSyncedSleepPotency() {
        return lastSyncedSleepPotency;
    }
}
