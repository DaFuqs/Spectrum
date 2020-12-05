package de.dafuqs.spectrum;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.block.BlockState;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class Support {

    public static boolean hasTag(BlockState type, Tag<net.minecraft.block.Block> tag) {
        return tag.contains(type.getBlock());
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

}
