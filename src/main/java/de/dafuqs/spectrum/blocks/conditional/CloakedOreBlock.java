package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.interfaces.Cloakable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Pair;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import java.util.Hashtable;
import java.util.List;

public abstract class CloakedOreBlock extends OreBlock implements Cloakable {

    private final boolean deepSlateOre;

    public CloakedOreBlock(Settings settings, UniformIntProvider uniformIntProvider, boolean deepSlateOre) {
        super(settings, uniformIntProvider);
        this.deepSlateOre = deepSlateOre;
    }

    @Override
    public Hashtable<BlockState, BlockState> getBlockStateCloaks() {
        Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
        if(deepSlateOre) {
            hashtable.put(this.getDefaultState(), Blocks.DEEPSLATE.getDefaultState());
        } else {
            hashtable.put(this.getDefaultState(), Blocks.STONE.getDefaultState());
        }
        return hashtable;
    }

    @Override
    public Pair<Item, Item> getItemCloak() {
        if(deepSlateOre) {
            return new Pair<>(this.asItem(), Blocks.DEEPSLATE.asItem());
        } else {
            return new Pair<>(this.asItem(), Blocks.STONE.asItem());
        }
    }

    @Deprecated
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return getCloakedDroppedStacks(state, builder);
    }

}
