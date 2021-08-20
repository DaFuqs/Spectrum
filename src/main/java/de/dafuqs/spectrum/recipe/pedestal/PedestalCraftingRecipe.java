package de.dafuqs.spectrum.recipe.pedestal;

import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlock;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlockItem;
import de.dafuqs.spectrum.enums.GemstoneColor;
import de.dafuqs.spectrum.progression.ClientPedestalRecipeToastManager;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PedestalCraftingRecipe implements Recipe<Inventory> {

    protected final Identifier id;
    @Nullable
    protected final String group;

    protected final int width;
    protected final int height;

    protected final int tier;
    protected final DefaultedList<Ingredient> craftingInputs;
    protected final HashMap<GemstoneColor, Integer> gemstoneDustInputs;
    protected final ItemStack output;
    protected final float experience;
    protected final int craftingTime;

    protected final List<Identifier> requiredAdvancementIdentifiers;
    protected final Identifier unlockedAdvancementOnCraft;
    protected final boolean showToastOnUnlock;

    public PedestalCraftingRecipe(Identifier id, String group, int tier, int width, int height, DefaultedList<Ingredient> craftingInputs, HashMap<GemstoneColor, Integer> gemstoneDustInputs, ItemStack output, float experience, int craftingTime, List<Identifier> requiredAdvancementIdentifiers, Identifier unlockedAdvancementOnCraft, boolean showToastOnUnlock) {
        this.id = id;
        this.group = group;
        this.tier = tier;

        this.width = width;
        this.height = height;

        this.craftingInputs = craftingInputs;
        this.gemstoneDustInputs = gemstoneDustInputs;
        this.output = output;
        this.experience = experience;
        this.craftingTime = craftingTime;

        this.requiredAdvancementIdentifiers = requiredAdvancementIdentifiers;
        this.unlockedAdvancementOnCraft = unlockedAdvancementOnCraft;
        this.showToastOnUnlock = showToastOnUnlock;

        if(SpectrumClient.minecraftClient != null) {
            registerInClientToastManager();
        }
    }

    @Environment(EnvType.CLIENT)
    private void registerInClientToastManager() {
        ClientPedestalRecipeToastManager.registerUnlockablePedestalRecipe(this);
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        if(!matchesGrid(inv)) {
            return false;
        }

        int topazPowderAmount = this.gemstoneDustInputs.get(GemstoneColor.CYAN);
        int amethystPowderAmount = this.gemstoneDustInputs.get(GemstoneColor.MAGENTA);
        int citrinePowderAmount = this.gemstoneDustInputs.get(GemstoneColor.YELLOW);
        int onyxPowderAmount = this.gemstoneDustInputs.get(GemstoneColor.BLACK);
        int moonstonePowderAmount = this.gemstoneDustInputs.get(GemstoneColor.WHITE);

        return ((topazPowderAmount == 0 || isStackAtLeast(inv.getStack(9), SpectrumItems.TOPAZ_POWDER, topazPowderAmount))
            && (amethystPowderAmount == 0 || isStackAtLeast(inv.getStack(10), SpectrumItems.AMETHYST_POWDER, amethystPowderAmount))
            && (citrinePowderAmount == 0 || isStackAtLeast(inv.getStack(11), SpectrumItems.CITRINE_POWDER, citrinePowderAmount))
            && (onyxPowderAmount == 0 || isStackAtLeast(inv.getStack(12), SpectrumItems.ONYX_POWDER, onyxPowderAmount))
            && (moonstonePowderAmount == 0 || isStackAtLeast(inv.getStack(13), SpectrumItems.MOONSTONE_POWDER, moonstonePowderAmount)));
    }

    public boolean matchesGrid(Inventory inv) {
        for(int i = 0; i <= 3 - this.width; ++i) {
            for(int j = 0; j <= 3 - this.height; ++j) {
                if (this.matchesPattern(inv, i, j, true)) {
                    return true;
                }

                if (this.matchesPattern(inv, i, j, false)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean matchesPattern(Inventory inv, int offsetX, int offsetY, boolean flipped) {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                int k = i - offsetX;
                int l = j - offsetY;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    if (flipped) {
                        ingredient = this.craftingInputs.get(this.width - k - 1 + l * this.width);
                    } else {
                        ingredient = this.craftingInputs.get(k + l * this.width);
                    }
                }

                if (!ingredient.test(inv.getStack(i + j * 3))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof PedestalCraftingRecipe) {
            return ((PedestalCraftingRecipe) object).getId().equals(this.getId());
        }
        return false;
    }

    private boolean isStackAtLeast(ItemStack sourceItemStack, Item item, int amount) {
        return sourceItemStack.getItem().equals(item) && sourceItemStack.getCount() >= amount;
    }

    public DefaultedList<Ingredient> getIngredients() {
        return craftingInputs;
    }

    @Override
    public ItemStack craft(Inventory inv) {
        return this.output.copy();
    }

    public DefaultedList<Ingredient> getPreviewInputs() {
        DefaultedList<Ingredient> defaultedList = DefaultedList.of();
        defaultedList.addAll(this.craftingInputs);
        return defaultedList;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST);
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SpectrumRecipeTypes.PEDESTAL_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return SpectrumRecipeTypes.PEDESTAL;
    }

    public int getGemstonePowderAmount(GemstoneColor gemstoneColor) {
        return gemstoneDustInputs.getOrDefault(gemstoneColor, 0);
    }

    public int getCraftingTime() {
        return craftingTime;
    }

    public float getExperience() {
        return this.experience;
    }

    public boolean unlocksAdvancementOnCraft() {
        return unlockedAdvancementOnCraft != null;
    }

    @Nullable
    public Identifier getUnlockedAdvancementOnCraft() {
        return unlockedAdvancementOnCraft;
    }

    public boolean shouldShowToastOnUnlock() {
        return this.showToastOnUnlock;
    }

    public HashMap<GemstoneColor, Integer> getGemstoneDustInputs() {
        return this.gemstoneDustInputs;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    /**
     * Returns a sound event matching for this recipe.
     * Dependent on the amount of gemstone dust used in it
     * @return The sound effect to play when this recipe is finished
     */
    public SoundEvent getSoundEvent(Random random) {
        List<SoundEvent> choices = new ArrayList<>();

        for(int i = 0; i < this.gemstoneDustInputs.get(GemstoneColor.MAGENTA); i++) {
            choices.add(SpectrumSoundEvents.PEDESTAL_CRAFT_AMETHYST);
        }
        for(int i = 0; i < this.gemstoneDustInputs.get(GemstoneColor.YELLOW); i++) {
            choices.add(SpectrumSoundEvents.PEDESTAL_CRAFT_CITRINE);
        }
        for(int i = 0; i < this.gemstoneDustInputs.get(GemstoneColor.CYAN); i++) {
            choices.add(SpectrumSoundEvents.PEDESTAL_CRAFT_TOPAZ);
        }
        for(int i = 0; i < this.gemstoneDustInputs.get(GemstoneColor.BLACK); i++) {
            choices.add(SpectrumSoundEvents.PEDESTAL_CRAFT_ONYX);
        }
        for(int i = 0; i < this.gemstoneDustInputs.get(GemstoneColor.WHITE); i++) {
            choices.add(SpectrumSoundEvents.PEDESTAL_CRAFT_MOONSTONE);
        }

        if(choices.size() == 0) {
            return SpectrumSoundEvents.PEDESTAL_CRAFT_GENERIC;
        } else {
            return choices.get(random.nextInt(choices.size()));
        }
    }

    public boolean canCraft(UUID playerUUID) {
        ServerPlayerEntity serverPlayerEntity = SpectrumCommon.minecraftServer.getPlayerManager().getPlayer(playerUUID);
        if(serverPlayerEntity == null) {
            return false;
        } else {
            return canCraft(serverPlayerEntity);
        }
    }

    private boolean hasUnlockedRequiredTier(PlayerEntity playerEntity) {
        switch (this.tier) {
            case 1 -> {
                return Support.hasAdvancement(playerEntity, new Identifier(SpectrumCommon.MOD_ID, "place_pedestal"));
            }
            case 2 -> {
                return Support.hasAdvancement(playerEntity, new Identifier(SpectrumCommon.MOD_ID, "spectrum_midgame"));
            }
            case 3 -> {
                return Support.hasAdvancement(playerEntity, new Identifier(SpectrumCommon.MOD_ID, "spectrum_lategame"));
            }
            default -> {
                return false;
            }
        }
    }

    public static PedestalBlock.PedestalVariant getPedestalVariantForOutput(ItemStack outputItemStack) {
        if(outputItemStack.getItem() instanceof PedestalBlockItem) {
            return ((PedestalBlockItem) outputItemStack.getItem()).getVariant();
        } else {
            return null;
        }
    }

    private boolean hasUnlockedRequiredAdvancements(PlayerEntity playerEntity) {
        for(Identifier advancementIdentifier : this.requiredAdvancementIdentifiers) {
            if(!Support.hasAdvancement(playerEntity, advancementIdentifier)) {
                return false;
            }
        }
        return true;
    }

    public boolean canCraft(PlayerEntity playerEntity) {
        return hasUnlockedRequiredTier(playerEntity) && hasUnlockedRequiredAdvancements(playerEntity);
    }

    /**
     * The advancement the player has to have to let the recipe be craftable in the pedestal
     * @return The advancement identifier. A null value means the player is always able to craft this recipe
     */
    @Nullable
    public List<Identifier> getRequiredAdvancementIdentifiers() {
        return requiredAdvancementIdentifiers;
    }

}
