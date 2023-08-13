package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.explosion.Archetype;
import de.dafuqs.spectrum.explosion.ExplosionEffectModifier;
import de.dafuqs.spectrum.registries.SpectrumRegistries;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EffectModifierCarryingBlockItem extends BlockItem implements ExplosiveArchetypeProvider {

    private final Archetype archetype;

    public EffectModifierCarryingBlockItem(Block block, Archetype archetype, Settings settings) {
        super(block, settings);
        this.archetype = archetype;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        var nbt = stack.getOrCreateNbt();
        if (nbt.contains("outputDisplay")) {
            tooltip.add(SpectrumRegistries.EXPLOSION_EFFECT_MODIFIERS.get(Identifier.tryParse(nbt.getString("outputDisplay"))).getName());
        }

        var effects = ExplosionEffectModifier.decodeStack(stack);
        if (effects.isEmpty())
            return;

        for (ExplosionEffectModifier explosionEffectModifier : effects.get()) {
            tooltip.add(explosionEffectModifier.getName());
        }

        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public Archetype getArchetype() {
        return archetype;
    }
}
