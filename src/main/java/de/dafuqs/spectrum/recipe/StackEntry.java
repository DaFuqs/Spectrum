package de.dafuqs.spectrum.recipe;

/*import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.registry.Registry;

import java.util.Collection;
import java.util.Collections;

class StackWithNbtEntry implements Ingredient.Entry {
    
    private final ItemStack stack;
    
    StackWithNbtEntry(ItemStack stack) {
        this.stack = stack;
    }

    public Collection<ItemStack> getStacks() {
        return Collections.singleton(this.stack);
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("item", Registry.ITEM.getId(this.stack.getItem()).toString());
        jsonObject.addProperty("nbt", NbtHelper.toNbtProviderString(stack.getOrCreateNbt()));
        return jsonObject;
    }
}*/