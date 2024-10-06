package de.dafuqs.spectrum.cca;

import de.dafuqs.additionalentityattributes.*;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.api.entity.PlayerEntityAccessor;
import de.dafuqs.spectrum.api.item.SleepAlteringItem;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Because not every niche thing can have its own component
 */
public class MiscPlayerDataComponent implements AutoSyncedComponent, CommonTickingComponent {

    public static final ComponentKey<MiscPlayerDataComponent> MISC_PLAYER_DATA_COMPONENT = ComponentRegistry.getOrCreate(SpectrumCommon.locate("misc_player_data"), MiscPlayerDataComponent.class);
    public static final int MAX_DRAGONROT_TICKS = 10 * 60 * 20;
    private final PlayerEntity player;

    // Sleep
    private int ticksBeforeSleep = -1, sleepingWindow = -1, sleepInvincibility;
    private double lastSyncedSleepPotency = -2;
    private Optional<SleepAlteringItem> sleepConsumable = Optional.empty();

    // Rot
    private int dragonrotTicks = 0;
    private boolean isBeingAfflictedByDragonrot;

    // Sword mechanics
    private boolean isLunging, bHopWindow, perfectCounter;
    private int parryTicks;

    public MiscPlayerDataComponent(PlayerEntity player) {
        this.player = player;
    }


    @Override
    public void tick() {
        tickSleep();
        tickSwordMechanics();
    }

    @Override
    public void serverTick() {
        CommonTickingComponent.super.serverTick();
        //tickDragonrotSwampEnvironment();

        var fortitude = player.getAttributeValue(SpectrumEntityAttributes.MENTAL_PRESENCE);
        if (lastSyncedSleepPotency != fortitude) {
            lastSyncedSleepPotency = fortitude;
            SpectrumS2CPacketSender.sendMentalPresenceSync((ServerPlayerEntity) player, fortitude);
        }
    }

    private void tickDragonrotSwampEnvironment() {
        if (!player.hasStatusEffect(SpectrumStatusEffects.IMMUNITY) && player.getWorld().getBiome(player.getBlockPos()).matchesKey(SpectrumBiomes.DRAGONROT_SWAMP)) {
            if (dragonrotTicks < MAX_DRAGONROT_TICKS / 2)
                dragonrotTicks++;

            if (!isBeingAfflictedByDragonrot && dragonrotTicks > 20 && !(player.isSpectator() || player.isCreative())) {
                isBeingAfflictedByDragonrot = true;
                player.sendMessage(Text.translatable("biome.spectrum.dragonrot_swamp.effect_start"), true);
            }

            if (dragonrotTicks >= 60 * 20) {
                applyEnvironmentalLifeDrain(5);
                if (player.getWorld().getTime() % 20 == 0)
                    player.damage(SpectrumDamageTypes.dragonrot(player.getWorld()), 1);
            }
            else if (dragonrotTicks >= 40 * 20) {
                applyEnvironmentalLifeDrain(3);
            }
            else if (dragonrotTicks >= 20 * 20) {
                applyEnvironmentalLifeDrain(1);
            }
            else if (dragonrotTicks >= 10 * 20) {
                applyEnvironmentalLifeDrain(0);
            }
        }
        else if (dragonrotTicks > 0) {
            dragonrotTicks = Math.max(0, dragonrotTicks - 3);
            if (isBeingAfflictedByDragonrot) {
                isBeingAfflictedByDragonrot = false;
                player.sendMessage(Text.translatable("biome.spectrum.dragonrot_swamp.effect_end"), true);
            }
        }
    }

    private boolean isInModifiedMotionState() {
        return player.isOnGround() || player.isSwimming() || player.isFallFlying() || player.getAbilities().flying;
    }

    private void applyEnvironmentalLifeDrain(int amplifier) {
        if (player.age % 20 != 0 || player.hasStatusEffect(SpectrumStatusEffects.IMMUNITY) || player.isSpectator() || player.isCreative())
            return;

        var effect = player.getStatusEffect(SpectrumStatusEffects.LIFE_DRAIN);

        if (effect == null) {
            player.addStatusEffect(new StatusEffectInstance(SpectrumStatusEffects.LIFE_DRAIN, 5 * 20, amplifier, true, false, true));
            dragonrotTicks = Math.max(12 * 20, dragonrotTicks / 2);
            return;
        }

        if (effect.getDuration() < 3 * 20) {
            ((StatusEffectInstanceAccessor) effect).setDuration(5 * 20);

            if (effect.getAmplifier() != amplifier)
                ((StatusEffectInstanceAccessor) effect).setAmplifier(amplifier);

            ((ServerWorld) player.getWorld()).getChunkManager().sendToNearbyPlayers(player, new EntityStatusEffectS2CPacket(player.getId(), effect));
        }
    }

    public boolean tryIncrementDragonrotTicks(int ticks) {
        if (player.getWorld().getBiome(player.getBlockPos()).matchesKey(SpectrumBiomes.DRAGONROT_SWAMP)) {
            dragonrotTicks = Math.min(MAX_DRAGONROT_TICKS, dragonrotTicks + ticks);
            return true;
        }
        return false;
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
                player.sleep(player.getBlockPos());
                ((PlayerEntityAccessor) player).setSleepTimer(0);
                var world = player.getWorld();
                if (!world.isClient())
                    ((ServerWorld) world).updateSleepingPlayers();
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
        if (!player.getWorld().isClient()) {
            player.wakeUp();
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
    public void readFromNbt(NbtCompound tag) {
        ticksBeforeSleep = tag.getInt("ticksBeforeSleep");
        sleepingWindow = tag.getInt("sleepingWindow");
        sleepInvincibility = tag.getInt("sleepInvincibility");
        dragonrotTicks = tag.getInt("dragonrotTicks");

        if (tag.contains("sleepConsumable")) {
            sleepConsumable = Optional.of((SleepAlteringItem) Registries.ITEM.get(Identifier.tryParse(tag.getString("sleepConsumable"))));
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("ticksBeforeSleep", ticksBeforeSleep);
        tag.putInt("sleepingWindow", sleepingWindow);
        tag.putInt("sleepInvincibility", sleepInvincibility);
        tag.putInt("dragonrotTicks", dragonrotTicks);

        sleepConsumable
                .map(sleepPenalizingItem -> (Item) sleepPenalizingItem)
                .map(Item::getRegistryEntry)
                .flatMap(RegistryEntry.Reference::getKey)
                .map(RegistryKey::getValue)
                .ifPresent(id -> tag.putString("sleepConsumable", id.toString()));
    }

    public static MiscPlayerDataComponent get(@NotNull PlayerEntity player) {
        return MISC_PLAYER_DATA_COMPONENT.get(player);
    }

    public void setLastSyncedSleepPotency(double lastSyncedSleepPotency) {
        this.lastSyncedSleepPotency = lastSyncedSleepPotency;
    }

    public double getLastSyncedSleepPotency() {
        return lastSyncedSleepPotency;
    }
}
