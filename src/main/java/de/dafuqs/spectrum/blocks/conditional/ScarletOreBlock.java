package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.progression.ClientBlockCloaker;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import java.util.Hashtable;
import java.util.List;

public class ScarletOreBlock extends CloakedOreBlock {

    public ScarletOreBlock(Settings settings, UniformIntProvider uniformIntProvider) {
        super(settings, uniformIntProvider, false);
        registerCloak();
    }

    @Override
    public Identifier getCloakAdvancementIdentifier() {
        return new Identifier(SpectrumCommon.MOD_ID, "craft_colored_sapling"); // TODO
    }

    @Override
    public Hashtable<BlockState, BlockState> getBlockStateCloaks() {
        Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
        hashtable.put(this.getDefaultState(), Blocks.NETHERRACK.getDefaultState());
        return hashtable;
    }

    @Override
    public Pair<Item, Item> getItemCloak() {
        return new Pair<>(this.asItem(), Blocks.NETHERRACK.asItem());
    }

}
