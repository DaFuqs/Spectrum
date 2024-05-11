package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.api.damage_type.StackTracking;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(DamageSource.class)
public class DamageSourceMixin implements StackTracking {

    @Unique
    private Optional<ItemStack> trackedStack = Optional.empty();

    @Override
    public Optional<ItemStack> spectrum$getTrackedStack() {
        return trackedStack;
    }

    @Override
    public void spectrum$setTrackedStack(@Nullable ItemStack stack) {
        trackedStack = Optional.ofNullable(stack);
    }
}
