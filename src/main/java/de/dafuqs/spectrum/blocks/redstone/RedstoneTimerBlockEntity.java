package de.dafuqs.spectrum.blocks.redstone;

import de.dafuqs.spectrum.blocks.RedstonePoweredBlock;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;


public class RedstoneTimerBlockEntity extends BlockEntity implements RedstonePoweredBlock {

    public enum TimingStep {
        OneSecond(20, "block.spectrum.redstone_timer.setting.one_second"),
        TenSeconds(10*20, "block.spectrum.redstone_timer.setting.ten_seconds"),
        OneMinute(60*20, "block.spectrum.redstone_timer.setting.one_minute"),
        TenMinutes(60*20*10, "block.spectrum.redstone_timer.setting.ten_minutes"),
        OneHour(60*60*20, "block.spectrum.redstone_timer.setting.one_hour");

        public final int ticks;
        public final String localizationString;

        TimingStep(int ticks, String localizationString) {
            this.ticks = ticks;
            this.localizationString = localizationString;
        }

        public TimingStep next() {
            return values()[(this.ordinal() + 1) % values().length];
        }
    }

    private long startTick;
    private TimingStep activeTiming; // redstone output on
    private TimingStep inactiveTiming; // redstone output off

    public RedstoneTimerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpectrumBlockEntityRegistry.REDSTONE_TIMER, blockPos, blockState);
        this.activeTiming = TimingStep.OneSecond;
        this.inactiveTiming = TimingStep.OneSecond;
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        return tag;
    }

    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
    }

    public void stepTiming(ServerPlayerEntity serverPlayerEntity) {
        if(serverPlayerEntity != null) {
            if(serverPlayerEntity.isSneaking()) {
                // toggle inactive time
                TimingStep newStep = inactiveTiming.next();
                serverPlayerEntity.sendMessage(new TranslatableText("block.spectrum.redstone_timer.setting.inactive").append(new TranslatableText(newStep.localizationString)), false);
                float pitch = 0.5F + newStep.ordinal() * 0.05F;
                world.playSound(serverPlayerEntity, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3F, pitch);
                inactiveTiming = newStep;
            } else {
                // toggle active time
                TimingStep newStep = activeTiming.next();
                serverPlayerEntity.sendMessage(new TranslatableText("block.spectrum.redstone_timer.setting.active").append(new TranslatableText(newStep.localizationString)), false);
                float pitch = 0.5F + newStep.ordinal() * 0.05F;
                world.playSound(serverPlayerEntity, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3F, pitch);
                activeTiming = newStep;
            }

            startTick = this.world.getTime();
        }
    }

    public int getActiveDuration() {
        return this.activeTiming.ticks;
    }

    public int getInactiveDuration() {
        return this.inactiveTiming.ticks;
    }

}
