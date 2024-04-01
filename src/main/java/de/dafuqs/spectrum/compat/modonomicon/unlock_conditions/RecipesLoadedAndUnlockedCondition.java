package de.dafuqs.spectrum.compat.modonomicon.unlock_conditions;

import com.google.gson.*;
import com.klikli_dev.modonomicon.book.conditions.*;
import com.klikli_dev.modonomicon.book.conditions.context.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.*;

public class RecipesLoadedAndUnlockedCondition extends BookCondition {
    
    protected static final String TOOLTIP = "book.condition.tooltip." + SpectrumCommon.MOD_ID + ".recipes_loaded_and_unlocked";
    
    protected List<Identifier> recipeIDs;
    
    public RecipesLoadedAndUnlockedCondition(Text tooltip, List<Identifier> recipeIDs) {
        super(tooltip);
        this.recipeIDs = recipeIDs;
    }
    
    public static RecipesLoadedAndUnlockedCondition fromJson(JsonObject json) {
        List<Identifier> recipeIDs = new ArrayList<>();
        
        JsonArray array = JsonHelper.getArray(json, "recipe_ids");
        for (JsonElement element : array) {
            recipeIDs.add(new Identifier(element.getAsString()));
        }
        Text tooltip = tooltipFromJson(json);
        return new RecipesLoadedAndUnlockedCondition(tooltip, recipeIDs);
    }
    
    public static RecipesLoadedAndUnlockedCondition fromNetwork(PacketByteBuf buffer) {
        Text tooltip = buffer.readBoolean() ? buffer.readText() : null;
        int recipeCount = buffer.readInt();
        List<Identifier> recipeIDs = new ArrayList<>();
        for (int i = 0; i < recipeCount; i++) {
            recipeIDs.add(buffer.readIdentifier());
        }
        return new RecipesLoadedAndUnlockedCondition(tooltip, recipeIDs);
    }
    
    @Override
    public Identifier getType() {
        return ModonomiconCompat.RECIPE_LOADED_AND_UNLOCKED;
    }
    
    @Override
    public void toNetwork(PacketByteBuf buffer) {
        buffer.writeBoolean(this.tooltip != null);
        if (this.tooltip != null) {
            buffer.writeText(this.tooltip);
        }
        buffer.writeInt(this.recipeIDs.size());
        for (Identifier identifier : this.recipeIDs) {
            buffer.writeIdentifier(identifier);
        }
    }
    
    @Override
    public boolean test(BookConditionContext context, PlayerEntity player) {
        for (Identifier recipeID : this.recipeIDs) {
            Optional<? extends Recipe<?>> optionalRecipe = player.getWorld().getRecipeManager().get(recipeID);
            if (optionalRecipe.isPresent()) {
                Recipe<?> recipe = optionalRecipe.get();
                if (recipe instanceof GatedRecipe gatedRecipe) {
                    if (gatedRecipe.canPlayerCraft(player)) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public List<Text> getTooltip(PlayerEntity player, BookConditionContext context) {
        if (this.tooltip == null) {
            this.tooltip = Text.translatable(TOOLTIP, this.recipeIDs);
        }
        return super.getTooltip(player, context);
    }
}
