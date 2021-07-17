package de.dafuqs.pigment.items.misc;

import de.dafuqs.pigment.PigmentCommon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import vazkii.patchouli.api.PatchouliAPI;

public class ManualItem extends Item {


	public ManualItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient && user instanceof ServerPlayerEntity) {

			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) user;

			// if the player has never opened the book before
			// automatically open the introduction page
			if(isNewPlayer(serverPlayerEntity)) {
				openManual(serverPlayerEntity, new Identifier(PigmentCommon.MOD_ID, "general/intro"), 0);
			} else {
				openManual(serverPlayerEntity);
			}

			user.incrementStat(Stats.USED.getOrCreateStat(this));

			return TypedActionResult.success(user.getStackInHand(hand));
		} else {
			return TypedActionResult.consume(user.getStackInHand(hand));
		}
	}

	private boolean isNewPlayer(ServerPlayerEntity serverPlayerEntity) {
		return serverPlayerEntity.getStatHandler().getStat(Stats.USED, this) == 0;
	}

	private void openManual(ServerPlayerEntity serverPlayerEntity) {
		PatchouliAPI.get().openBookGUI(serverPlayerEntity, new Identifier(PigmentCommon.MOD_ID, "manual"));
	}

	private void openManual(ServerPlayerEntity serverPlayerEntity, Identifier entry, int page) {
		PatchouliAPI.get().openBookEntry(serverPlayerEntity, new Identifier(PigmentCommon.MOD_ID, "manual"), entry, page);
	}


}