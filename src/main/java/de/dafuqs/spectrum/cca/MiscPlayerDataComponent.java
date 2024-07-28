package de.dafuqs.spectrum.cca;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.api.entity.PlayerEntityAccessor;
import de.dafuqs.spectrum.api.item.SleepAlteringItem;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Because not every niche thing can have its own component
 */
public class MiscPlayerDataComponent implements AutoSyncedComponent, CommonTickingComponent {

    public static final ComponentKey<MiscPlayerDataComponent> MISC_PLAYER_DATA_COMPONENT = ComponentRegistry.getOrCreate(SpectrumCommon.locate("misc_player_data"), MiscPlayerDataComponent.class);
    private final PlayerEntity player;
    private int ticksBeforeSleep = -1, sleepingWindow = -1, sleepInvincibility;
    private Optional<SleepAlteringItem> sleepConsumable = Optional.empty();

    public MiscPlayerDataComponent(PlayerEntity player) {
        this.player = player;
    }


    @Override
    public void tick() {
        tickSleep();
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

        if (tag.contains("sleepConsumable")) {
            sleepConsumable = Optional.of((SleepAlteringItem) Registries.ITEM.get(Identifier.tryParse(tag.getString("sleepConsumable"))));
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("ticksBeforeSleep", ticksBeforeSleep);
        tag.putInt("sleepingWindow", sleepingWindow);
        tag.putInt("sleepInvincibility", sleepInvincibility);

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
}
