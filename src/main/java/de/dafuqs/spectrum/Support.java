package de.dafuqs.spectrum;

import de.dafuqs.spectrum.items.PigmentItem;
import de.dafuqs.spectrum.progression.ClientAdvancements;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.Tag;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Support {

    public static boolean hasTag(@NotNull BlockState blockState, @NotNull Tag<Block> tag) {
        return tag.contains(blockState.getBlock());
    }

    public static Optional<Tag> getFirstMatchingTag(@NotNull Block block, @NotNull List<Tag<Block>> tags) {
        for(Tag tag : tags) {
            if(tag.contains(block)) {
                return Optional.of(tag);
            }
        }
        return Optional.empty();
    }

    public static int getWholeIntFromFloatWithChance(float f, @NotNull Random random) {
        boolean roundUp = (random.nextFloat() < f % 1);
        if(roundUp) {
            return ((int) f) + 1;
        } else {
            return (int) f;
        }
    }

    public static void grantAdvancementCriterion(@NotNull ServerPlayerEntity serverPlayerEntity, String advancementString, String criterion) {
        ServerAdvancementLoader sal = SpectrumCommon.minecraftServer.getAdvancementLoader();
        PlayerAdvancementTracker tracker = serverPlayerEntity.getAdvancementTracker();

        // grant group advancement
        Identifier advancementIdentifier = new Identifier(SpectrumCommon.MOD_ID, advancementString);
        Advancement advancement = sal.get(advancementIdentifier);
        if (advancement != null) {
            tracker.grantCriterion(advancement, criterion);
        } else {
            SpectrumCommon.log(Level.ERROR, "Trying to grant a criterion for an advancement that does not exist: " + advancementIdentifier);
        }
    }

    public static boolean hasAdvancement(PlayerEntity playerEntity, Identifier advancementIdentifier) {
        if (playerEntity instanceof ServerPlayerEntity) {
            Advancement advancement = SpectrumCommon.minecraftServer.getAdvancementLoader().get(advancementIdentifier);
            if (advancement == null) {
                SpectrumCommon.log(Level.ERROR, "Player " + playerEntity.getName() + " was getting an advancement check for an advancement that does not exist: " + advancementIdentifier.toString());
                return false;
            } else {
                return ((ServerPlayerEntity) playerEntity).getAdvancementTracker().getProgress(advancement).isDone();
            }
        } else {
            return ClientAdvancements.hasDone(advancementIdentifier);
        }
    }

    public static @NotNull String getReadableDimensionString(@NotNull String dimensionKeyString) {
        switch (dimensionKeyString) {
            case "minecraft:overworld":
                return "Overworld";
            case "minecraft:nether":
                return "Nether";
            case "minecraft:end":
                return "End";
            default:
                if(dimensionKeyString.contains(":")) {
                    return dimensionKeyString.substring(0, dimensionKeyString.indexOf(":"));
                } else {
                    return dimensionKeyString;
                }
        }
    }

    public static Optional<DyeColor> getDyeColorOfItemStack(ItemStack itemStack) {
        if(!itemStack.isEmpty()) {
            Item item = itemStack.getItem();
            if(item instanceof DyeItem dyeItem) {
                return Optional.of(dyeItem.getColor());
            } else if(item instanceof PigmentItem pigmentItem) {
                return Optional.of(pigmentItem.getColor());
            }
        }
        return Optional.empty();
    }

}