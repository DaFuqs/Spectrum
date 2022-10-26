package de.dafuqs.spectrum.recipe.spirit_instiller;

import com.mojang.authlib.GameProfile;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.spirit_instiller.SpiritInstillerBlockEntity;
import de.dafuqs.spectrum.cca.HardcoreDeathComponent;
import de.dafuqs.spectrum.recipe.spirit_instiller.spawner.SpawnerCreatureChangeRecipe;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

public abstract class HardcorePlayerRevivalRecipe extends SpiritInstillerRecipe {
	
	public static final RecipeSerializer<SpawnerCreatureChangeRecipe> SERIALIZER = new SpecialRecipeSerializer<>(SpawnerCreatureChangeRecipe::new);
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("milestones/unlock_spawner_manipulation");
	
	// Identifier id, String group, IngredientStack centerIngredient, IngredientStack bowlIngredient1, IngredientStack bowlIngredient2, ItemStack outputItemStack, int craftingTime, float experience, boolean noBenefitsFromYieldAndEfficiencyUpgrades, Identifier requiredAdvancementIdentifier)
	public HardcorePlayerRevivalRecipe(Identifier identifier) {
		super(identifier, "", IngredientStack.of(Ingredient.ofItems(Blocks.PLAYER_HEAD.asItem())), IngredientStack.of(Ingredient.ofItems(Items.NETHER_STAR)), IngredientStack.of(Ingredient.ofItems(Items.ENCHANTED_GOLDEN_APPLE)), ItemStack.EMPTY, 2400, 100, true, UNLOCK_IDENTIFIER);
	}
	
	@Override
	public ItemStack craft(Inventory inv) {
		if (inv instanceof SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
			GameProfile gameProfile = getSkullOwner(inv.getStack(ISpiritInstillerRecipe.CENTER_INGREDIENT));
			if(gameProfile != null) {
				ServerPlayerEntity revivedPlayer = SpectrumCommon.minecraftServer.getPlayerManager().getPlayer(gameProfile.getName());
				if(revivedPlayer != null) {
					HardcoreDeathComponent.removeHardcoreDeath(gameProfile);
					revivedPlayer.changeGameMode(SpectrumCommon.minecraftServer.getDefaultGameMode());
					FabricDimensions.teleport(revivedPlayer, (ServerWorld) spiritInstillerBlockEntity.getWorld(), new TeleportTarget(Vec3d.ofCenter(spiritInstillerBlockEntity.getPos().up()), new Vec3d(0, 0, 0), revivedPlayer.getYaw(), revivedPlayer.getPitch()));
				}
			}
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean canCraftWithStacks(ItemStack instillerStack, ItemStack leftBowlStack, ItemStack rightBowlStack) {
		if(instillerStack.isOf(Blocks.PLAYER_HEAD.asItem())) {
			GameProfile gameProfile = getSkullOwner(instillerStack);
			if(gameProfile == null) {
				return false;
			}
			return HardcoreDeathComponent.hasHardcoreDeath(gameProfile);
		}
		return false;
	}
	
	@Nullable
	private GameProfile getSkullOwner(ItemStack instillerStack) {
		GameProfile gameProfile = null;
		NbtCompound nbtCompound = instillerStack.getNbt();
		if(nbtCompound != null) {
			if (nbtCompound.contains("SkullOwner", 10)) {
				gameProfile = NbtHelper.toGameProfile(nbtCompound.getCompound("SkullOwner"));
			} else if (nbtCompound.contains("SkullOwner", 8) && !StringUtils.isBlank(nbtCompound.getString("SkullOwner"))) {
				gameProfile = new GameProfile(null, nbtCompound.getString("SkullOwner"));
			}
		}
		return gameProfile;
	}
	
}
