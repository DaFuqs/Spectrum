package de.dafuqs.spectrum.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class TransparencyTestBlock extends Block {


    public TransparencyTestBlock(Settings settings) {
        super(settings);
    }

    private static boolean canSee(ClientPlayerEntity clientPlayerEntity) {
        if (clientPlayerEntity.getArmor() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Environment(EnvType.CLIENT)
    public static boolean shouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction side, BlockPos blockPos) {
        ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
        return canSee(clientPlayerEntity);
    }

}



