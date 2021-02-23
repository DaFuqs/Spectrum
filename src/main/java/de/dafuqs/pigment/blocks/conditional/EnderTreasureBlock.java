package de.dafuqs.pigment.blocks.conditional;

import de.dafuqs.pigment.PigmentBlockCloaker;
import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.Support;
import de.dafuqs.pigment.interfaces.Cloakable;
import de.dafuqs.pigment.misc.PigmentClientAdvancements;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.Advancement;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.List;

public class EnderTreasureBlock extends Block implements Cloakable {

    public EnderTreasureBlock(Settings settings) {
        super(settings);
        setupCloak();
    }

    @Override
    public Identifier getCloakAdvancementIdentifier() {
        return new Identifier(PigmentCommon.MOD_ID, "get_ender_treasure");
    }

    public void setCloaked() {
        PigmentBlockCloaker.swapModel(this.getDefaultState(), Blocks.COBBLESTONE.getDefaultState());
    }

    public void setUncloaked() {
        PigmentBlockCloaker.unswapAllBlockStates(this);
    }

    @Deprecated
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return getCloakedDroppedStacks(state, builder);
    }

}
