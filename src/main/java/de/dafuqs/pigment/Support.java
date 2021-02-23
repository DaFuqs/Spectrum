package de.dafuqs.pigment;

import de.dafuqs.pigment.misc.PigmentClientAdvancements;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import java.util.Random;

public class Support {

    public static boolean hasTag(BlockState type, Tag<net.minecraft.block.Block> tag) {
        return tag.contains(type.getBlock());
    }

    public static void grantAdvancementCriterion(ServerPlayerEntity serverPlayerEntity, String advancementString, String criterion) {
        ServerAdvancementLoader sal = PigmentCommon.minecraftServer.getAdvancementLoader();
        PlayerAdvancementTracker tracker = serverPlayerEntity.getAdvancementTracker();

        // grant group advancement
        Identifier advancementIdentifier = new Identifier(PigmentCommon.MOD_ID, advancementString);
        Advancement advancement = sal.get(advancementIdentifier);
        if (advancement != null) {
            tracker.grantCriterion(advancement, criterion);
        }
    }

    public static int getWholeIntFromFloatWithChance(float f, Random random) {
        boolean roundUp = (random.nextFloat() < f % 1);
        if(roundUp) {
            return ((int) f) + 1;
        } else {
            return (int) f;
        }
    }

    public static boolean hasAdvancement(PlayerEntity playerEntity, Identifier advancementIdentifier) {
        if (playerEntity instanceof ClientPlayerEntity) {
            return PigmentClientAdvancements.hasDone(advancementIdentifier);
        } else if (playerEntity instanceof ServerPlayerEntity) {
            Advancement advancement = PigmentCommon.minecraftServer.getAdvancementLoader().get(advancementIdentifier);
            return ((ServerPlayerEntity) playerEntity).getAdvancementTracker().getProgress(advancement).isDone();
        } else {
            return false;
        }
    }

}