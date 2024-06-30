package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.item.Stampable;
import de.dafuqs.spectrum.helpers.BlockReference;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

import java.util.Optional;

public class BlendingStampItem extends Item {

    public static final String DATA = Stampable.BLENDING_DATA_TAG;

    public BlendingStampItem(Settings settings) {
        super(settings);
    }

    /**
     * This is set up such that it can easily be extended for other uses later.
     */
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var stack = context.getStack();
        var world = context.getWorld();
        var pos = context.getBlockPos();
        var player = context.getPlayer();
        var reference = BlockReference.of(world, pos);

        var potentialData = Stampable.loadBlendingData(world, stack.getOrCreateNbt().getCompound(DATA));

        if (potentialData.isPresent()) {
            var potentialTarget = getData(player, reference, world);

            if (potentialTarget.isEmpty())
                return ActionResult.PASS;

            var source = potentialData.get();
            var target = potentialTarget.get();

            if (!source.verifyStampData(target) || !target.canUserStamp(player))
                return ActionResult.FAIL;

            var interactable = target.source();

            var targetChanged = interactable.handleImpression(source.stamper(), Optional.ofNullable(player), source.reference(), world);

            if (!targetChanged)
                return ActionResult.FAIL;

            source.notifySourceOfChange(target);

            //Allow for 'rolling' linking for flow.
            if (player != null && player.isSneaking()) {
                var newSource = target.source().recordData(Optional.of(player), reference, world);
                saveToNbt(stack, newSource);
            }

            return ActionResult.success(world.isClient());
        }
        else {
            var data = getData(player, reference, world);
            if (data.isPresent() && data.get().canUserStamp(player)) {
                saveToNbt(stack, data.get());
                return ActionResult.success(world.isClient());
            }
        }

        return super.useOnBlock(context);
    }

    private void saveToNbt(ItemStack stack, Stampable.BlendingData data) {
        var stackNbt = stack.getOrCreateNbt();
        stackNbt.put(DATA, Stampable.saveBlendingData(data));
    }

    private Optional<Stampable.BlendingData> getData(PlayerEntity player, BlockReference reference, World world) {
        var data = Optional.<Stampable.BlendingData>empty();

        findData: {
            if (reference.getState().getBlock() instanceof Stampable interactable)
                data = Optional.ofNullable(interactable.recordData(Optional.ofNullable(player), reference, world));

            if (data.isPresent())
                break findData;

            data = reference.tryGetBlockEntity().map(be -> {
                if (be instanceof Stampable interactable)
                    return interactable.recordData(Optional.ofNullable(player), reference, world);
                return null;
            });
        }

        return data;
    }
}