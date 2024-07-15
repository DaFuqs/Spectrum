package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.blocks.deeper_down.flora.AbyssalVinesBlock;
import de.dafuqs.spectrum.helpers.BlockReference;
import de.dafuqs.spectrum.items.ItemWithTooltip;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.event.GameEvent;

import java.util.Optional;

public class FissurePlumItem extends ItemWithTooltip {

    public FissurePlumItem(Settings settings, String tooltip) {
        super(settings, tooltip);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var world = context.getWorld();
        var pos = context.getBlockPos().offset(context.getSide());
        var player = Optional.ofNullable(context.getPlayer());

        if (!player.map(Entity::isSneaking).orElse(true))
            return ActionResult.PASS;

        var state = SpectrumBlocks.ABYSSAL_VINES.getDefaultState();
        var roof = BlockReference.of(world, pos.up());

        if (!SpectrumBlocks.ABYSSAL_VINES.canPlantOnTop(world.getBlockState(pos), world, pos) || !world.isAir(pos))
            return ActionResult.PASS;

        if (roof.isOf(SpectrumBlocks.ABYSSAL_VINES)) {
            state = state.with(AbyssalVinesBlock.LIFE_STAGE, roof.getProperty(AbyssalVinesBlock.LIFE_STAGE));
            roof.setProperty(AbyssalVinesBlock.LIFE_STAGE, AbyssalVinesBlock.LifeStage.STALK);
            roof.update(world);
        }

        world.setBlockState(pos, state);
        world.playSound(player.get(), pos, SoundEvents.BLOCK_CAVE_VINES_PLACE, SoundCategory.BLOCKS, 1, MathHelper.nextBetween(world.getRandom(), 0.8F, 1.2F));
        world.emitGameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Emitter.of(player.get(), state));
        if (player.map(p -> !p.getAbilities().creativeMode).orElse(true))
            context.getStack().decrement(1);

        return ActionResult.success(world.isClient());
    }
}
