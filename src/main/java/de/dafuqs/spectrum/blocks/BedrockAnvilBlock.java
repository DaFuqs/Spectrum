package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.inventories.BedrockAnvilScreenHandler;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BedrockAnvilBlock extends AnvilBlock {

    private static final Text TITLE = new TranslatableText("container.spectrum.bedrock_anvil");

    public BedrockAnvilBlock(Settings settings) {
        super(settings);
    }

    // Heavier => More damage
    protected void configureFallingBlockEntity(FallingBlockEntity entity) {
        entity.setHurtEntities(3.0F, 64);
    }

    @Nullable
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> {
            return new BedrockAnvilScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos));
        }, TITLE);
    }

}
