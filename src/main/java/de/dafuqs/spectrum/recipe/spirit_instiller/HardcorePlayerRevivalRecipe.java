package de.dafuqs.spectrum.recipe.spirit_instiller;

import com.mojang.authlib.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.spirit_instiller.*;
import de.dafuqs.spectrum.cca.*;
import net.fabricmc.fabric.api.dimension.v1.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.block.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.recipe.*;
import net.minecraft.server.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.apache.commons.lang3.*;
import org.jetbrains.annotations.*;

public class HardcorePlayerRevivalRecipe extends SpiritInstillerRecipe {
	
	public static final RecipeSerializer<HardcorePlayerRevivalRecipe> SERIALIZER = new SpecialRecipeSerializer<>(HardcorePlayerRevivalRecipe::new);
	
	public HardcorePlayerRevivalRecipe(Identifier identifier) {
		super(identifier, "", false, null,
				IngredientStack.of(Ingredient.ofItems(Blocks.PLAYER_HEAD.asItem())), IngredientStack.of(Ingredient.ofItems(Items.NETHER_STAR)), IngredientStack.of(Ingredient.ofItems(Items.ENCHANTED_GOLDEN_APPLE)),
				ItemStack.EMPTY, 1200, 100, true);
	}
	
	@Override
	public ItemStack craft(Inventory inv) {
		if (inv instanceof SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
			GameProfile gameProfile = getSkullOwner(inv.getStack(SpiritInstillerRecipe.CENTER_INGREDIENT));
			if (gameProfile != null) {
				ServerPlayerEntity revivedPlayer = SpectrumCommon.minecraftServer.getPlayerManager().getPlayer(gameProfile.getName());
				if (revivedPlayer != null) {
					HardcoreDeathComponent.removeHardcoreDeath(gameProfile);
					revivedPlayer.changeGameMode(SpectrumCommon.minecraftServer.getDefaultGameMode());
					
					BlockRotation blockRotation = spiritInstillerBlockEntity.getMultiblockRotation();
					float yaw = 0.0F;
					switch (blockRotation) {
						case NONE -> {
							yaw = -90.0F;
						}
						case CLOCKWISE_90 -> {
							yaw = 0.0F;
						}
						case CLOCKWISE_180 -> {
							yaw = 900.0F;
						}
						case COUNTERCLOCKWISE_90 -> {
							yaw = 180.0F;
						}
					}
					
					FabricDimensions.teleport(revivedPlayer, (ServerWorld) spiritInstillerBlockEntity.getWorld(), new TeleportTarget(Vec3d.ofCenter(spiritInstillerBlockEntity.getPos().up()), new Vec3d(0, 0, 0), yaw, revivedPlayer.getPitch()));
				}
			}
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean canCraftWithStacks(Inventory inventory) {
		ItemStack instillerStack = inventory.getStack(0);
		if (instillerStack.isOf(Blocks.PLAYER_HEAD.asItem())) {
			GameProfile gameProfile = getSkullOwner(instillerStack);
			if (gameProfile == null) {
				return false;
			}
			
			PlayerManager playerManager = SpectrumCommon.minecraftServer.getPlayerManager();
			ServerPlayerEntity playerToRevive = gameProfile.getId() == null ? playerManager.getPlayer(gameProfile.getName()) : playerManager.getPlayer(gameProfile.getId());
			return playerToRevive != null && HardcoreDeathComponent.hasHardcoreDeath(gameProfile);
		}
		return false;
	}
	
	@Nullable
	private GameProfile getSkullOwner(ItemStack instillerStack) {
		GameProfile gameProfile = null;
		NbtCompound nbtCompound = instillerStack.getNbt();
		if (nbtCompound != null) {
			if (nbtCompound.contains("SkullOwner", NbtElement.COMPOUND_TYPE)) {
				gameProfile = NbtHelper.toGameProfile(nbtCompound.getCompound("SkullOwner"));
			} else if (nbtCompound.contains("SkullOwner", NbtElement.STRING_TYPE) && !StringUtils.isBlank(nbtCompound.getString("SkullOwner"))) {
				gameProfile = new GameProfile(null, nbtCompound.getString("SkullOwner"));
			}
		}
		return gameProfile;
	}
	
}
