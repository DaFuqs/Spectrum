package de.dafuqs.spectrum.explosion;

import de.dafuqs.spectrum.registries.SpectrumRegistries;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class ExplosionEffectModifier {

    public final Identifier id;
    public final ExplosionEffectFamily family;

    protected ExplosionEffectModifier(Identifier id, ExplosionEffectFamily family) {
        this.id = id;
        this.family = family;
    }

    @ApiStatus.OverrideOnly
    public boolean compatibleWithArchetype(Archetype archetype) {
        return family.acceptsArchetype(archetype);
    }

    @ApiStatus.OverrideOnly
    public abstract void applyToEntities(Archetype archetype, @NotNull List<Entity> entity);

    @ApiStatus.OverrideOnly
    public abstract void applyToBlocks(Archetype archetype, @NotNull World world, @NotNull List<BlockPos> blocks);

    @ApiStatus.OverrideOnly
    public float getBlastPowerModifer(Archetype archetype, BlockEntity blockEntity) {
        return  1F;
    }

    @ApiStatus.OverrideOnly
    public float getDropChanceModifier(Archetype archetype, BlockEntity blockEntity) {
        return  1F;
    }

    @ApiStatus.OverrideOnly
    public float getBlastRadiusModifer(Archetype archetype, BlockEntity blockEntity) {
        return 1F;
    }

    @ApiStatus.OverrideOnly
    public float getDamageModifer(Archetype archetype, BlockEntity blockEntity) {
        return 1F;
    }

    @ApiStatus.OverrideOnly
    public Optional<DamageSource> getDamageSource(Archetype archetype, BlockEntity blockEntity) {
        return Optional.empty();
    }

    public static List<ExplosionEffectModifier> decode(NbtCompound nbt) {
        var modifiers = new ArrayList<ExplosionEffectModifier>();

        if (!nbt.contains("explosionModifiers"))
            return modifiers;

        var encodedModifiers = (NbtCompound) nbt.get("explosionModifiers");
        var count = encodedModifiers.getInt("count");

        for (int i = 0; i < count; i++) {
            modifiers.add(SpectrumRegistries.EXPLOSION_EFFECT_MODIFIERS.get(Identifier.tryParse(encodedModifiers.getString("exMod_" + i))));
        }

        return modifiers;
    }

    public static void encode(NbtCompound nbt, List<ExplosionEffectModifier> explosionEffectModifiers) {
        var encodedModifiers = new NbtCompound();
        encodedModifiers.putInt("count", explosionEffectModifiers.size());

        for (int i = 0; i < explosionEffectModifiers.size(); i++) {
            encodedModifiers.putString("exMod_" + i, explosionEffectModifiers.get(i).id.toString());
        }

        nbt.put("explosionModifiers", encodedModifiers);
    }

    public static Optional<List<ExplosionEffectModifier>> decodeStack(ItemStack stack) {
        var nbt = stack.getNbt();

        if (nbt == null || !nbt.contains("explosionModifiers"))
            return Optional.empty();

        return Optional.of(decode(nbt));
    }

    public static void encodeStack(ItemStack stack, List<ExplosionEffectModifier> effectModifiers) {
        encode(stack.getNbt(), effectModifiers);
    }

    @Override
    public int hashCode() {
        return 31 * id.hashCode() + family.hashCode();
    }
}
