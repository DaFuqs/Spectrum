package de.dafuqs.spectrum;

import de.dafuqs.spectrum.misc.SpectrumClientAdvancements;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;

import java.util.Random;

public class Support {

    public static boolean hasTag(BlockState type, Tag<net.minecraft.block.Block> tag) {
        return tag.contains(type.getBlock());
    }

    public static int getWholeIntFromFloatWithChance(float f, Random random) {
        boolean roundUp = (random.nextFloat() < f % 1);
        if(roundUp) {
            return ((int) f) + 1;
        } else {
            return (int) f;
        }
    }

    public static void grantAdvancementCriterion(ServerPlayerEntity serverPlayerEntity, String advancementString, String criterion) {
        ServerAdvancementLoader sal = SpectrumCommon.minecraftServer.getAdvancementLoader();
        PlayerAdvancementTracker tracker = serverPlayerEntity.getAdvancementTracker();

        // grant group advancement
        Identifier advancementIdentifier = new Identifier(SpectrumCommon.MOD_ID, advancementString);
        Advancement advancement = sal.get(advancementIdentifier);
        if (advancement != null) {
            tracker.grantCriterion(advancement, criterion);
        }
    }

    public static boolean hasAdvancement(PlayerEntity playerEntity, Identifier advancementIdentifier) {
        if (playerEntity instanceof ClientPlayerEntity) {
            return SpectrumClientAdvancements.hasDone(advancementIdentifier);
        } else if (playerEntity instanceof ServerPlayerEntity) {
            Advancement advancement = SpectrumCommon.minecraftServer.getAdvancementLoader().get(advancementIdentifier);
            if(advancement == null) {
                SpectrumCommon.log(Level.ERROR, "Player " + playerEntity.getName() + " was getting an advancement check for an advancement that does not exist: " + advancementIdentifier.toString());
                return false;
            } else {
                return ((ServerPlayerEntity) playerEntity).getAdvancementTracker().getProgress(advancement).isDone();
            }
        } else {
            return false;
        }
    }

    public static String getReadableDimensionString(String dimensionKeyString) {
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
}