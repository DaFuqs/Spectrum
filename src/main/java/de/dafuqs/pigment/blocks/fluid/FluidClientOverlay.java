package de.dafuqs.pigment.blocks.fluid;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.registries.PigmentBlocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;

public class FluidClientOverlay {
    private static final Identifier TEXTURE_IN_LIQUID_CRYSTAL = new Identifier(PigmentCommon.MOD_ID + ":textures/misc/liquid_crystal_overlay.png");
    private static final Identifier TEXTURE_IN_MUD = new Identifier(PigmentCommon.MOD_ID + ":textures/misc/mud_overlay.png");

    public static boolean checkPigmentFluidOverlay(PlayerEntity player, BlockPos pos, MatrixStack matrixStack) {
        if (player.world.getBlockState(new BlockPos(player.getCameraPosVec(1))).getBlock() == PigmentBlocks.LIQUID_CRYSTAL) {
            renderOverlay(matrixStack, TEXTURE_IN_LIQUID_CRYSTAL, 0.42F);
            return true;
        } else if(player.world.getBlockState(new BlockPos(player.getCameraPosVec(1))).getBlock() == PigmentBlocks.MUD) {
            renderOverlay(matrixStack, TEXTURE_IN_MUD, 0.98F);
            return true;
        }
        return false;
    }

    public static void renderOverlay(MatrixStack matrixStack, Identifier textureIdentifier, float alpha) {
        MinecraftClient minecraftIn = MinecraftClient.getInstance();
        minecraftIn.getTextureManager().bindTexture(textureIdentifier);
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        float f = minecraftIn.player.getBrightnessAtEyes();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        float f7 = -minecraftIn.player.getYaw() / 64.0F;
        float f8 = minecraftIn.player.getPitch() / 64.0F;
        Matrix4f matrix4f = matrixStack.peek().getModel();
        bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
        bufferbuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).color(f, f, f, alpha).texture(4.0F + f7, 4.0F + f8).next();
        bufferbuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).color(f, f, f, alpha).texture(0.0F + f7, 4.0F + f8).next();
        bufferbuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).color(f, f, f, alpha).texture(0.0F + f7, 0.0F + f8).next();
        bufferbuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).color(f, f, f, alpha).texture(4.0F + f7, 0.0F + f8).next();
        bufferbuilder.end();
        BufferRenderer.draw(bufferbuilder);
        RenderSystem.disableBlend();
    }


}