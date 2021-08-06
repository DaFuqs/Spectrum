package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.interfaces.Cloakable;
import de.dafuqs.spectrum.progression.BlockCloakManager;
import de.dafuqs.spectrum.progression.ClientBlockCloaker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
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

    // only drop xp when not cloaked
    @Override
    public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack) {
        // TODO: Don't drop XP if broken by cloaked player
        super.onStacksDropped(state, world, pos, stack);
    }

}
