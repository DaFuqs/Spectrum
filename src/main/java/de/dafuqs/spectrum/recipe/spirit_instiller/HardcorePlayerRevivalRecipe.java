package de.dafuqs.spectrum.recipe.spirit_instiller;

import com.mojang.authlib.GameProfile;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.spirit_instiller.SpiritInstillerBlockEntity;
import de.dafuqs.spectrum.cca.HardcoreDeathComponent;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

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
	public boolean canCraftWithStacks(ItemStack instillerStack, ItemStack leftBowlStack, ItemStack rightBowlStack) {
		if (instillerStack.isOf(Blocks.PLAYER_HEAD.asItem())) {
			GameProfile gameProfile = getSkullOwner(instillerStack);
			if (gameProfile == null) {
				return false;
			}
			
			ServerPlayerEntity playerToRevive = SpectrumCommon.minecraftServer.getPlayerManager().getPlayer(gameProfile.getId());
			playerToRevive = playerToRevive != null ? playerToRevive : SpectrumCommon.minecraftServer.getPlayerManager().getPlayer(gameProfile.getName());
			return playerToRevive != null && HardcoreDeathComponent.hasHardcoreDeath(gameProfile);
		}
		return false;
	}
	
	@Nullable
	private GameProfile getSkullOwner(ItemStack instillerStack) {
		GameProfile gameProfile = null;
		NbtCompound nbtCompound = instillerStack.getNbt();
		if (nbtCompound != null) {
			if (nbtCompound.contains("SkullOwner", 10)) {
				gameProfile = NbtHelper.toGameProfile(nbtCompound.getCompound("SkullOwner"));
			} else if (nbtCompound.contains("SkullOwner", 8) && !StringUtils.isBlank(nbtCompound.getString("SkullOwner"))) {
				gameProfile = new GameProfile(null, nbtCompound.getString("SkullOwner"));
			}
		}
		return gameProfile;
	}
	
}
