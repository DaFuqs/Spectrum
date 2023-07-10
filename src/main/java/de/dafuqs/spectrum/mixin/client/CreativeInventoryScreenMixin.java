package de.dafuqs.spectrum.mixin.client;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin {
	
	@Shadow
	@Final
	private static Identifier TEXTURE;
	
	@Shadow
	private static int selectedTab;
	
	@Unique
	private boolean spectrum$spectrumGroupIsGettingRendered = false;
	
	@ModifyArg(method = "drawBackground", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V", ordinal = 1))
	private Identifier injectCustomGroupTexture(Identifier original) {
		if (!isSpectrumGroupSelected()) return original;
		return SpectrumItemGroups.ITEM_GROUP_BACKGROUND_TEXTURE_IDENTIFIER;
	}
	
	@ModifyArg(method = "drawBackground", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V", ordinal = 2))
	private Identifier injectCustomScrollbarTexture(Identifier original) {
		if (!isSpectrumGroupSelected()) return original;
		this.spectrum$spectrumGroupIsGettingRendered = true;
		return SpectrumItemGroups.ITEM_GROUP_BACKGROUND_TEXTURE_IDENTIFIER;
	}
	
	@ModifyArg(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 1), index = 3)
	private int injectCustomScrollbarTextureU(int original) {
		if (!isSpectrumGroupSelected()) return original;
		return original - 232;
	}
	
	@ModifyArg(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 1), index = 4)
	private int injectCustomScrollbarTextureV(int original) {
		if (!isSpectrumGroupSelected()) return original;
		return 136;
	}
	
	@Inject(method = "drawBackground", at = @At("RETURN"))
	private void releaseGroupInstance(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo ci) {
		spectrum$spectrumGroupIsGettingRendered = false;
	}
	
	@Inject(method = "renderTabIcon", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemGroup;getIcon()Lnet/minecraft/item/ItemStack;"))
	private void injectCustomTabTexture(MatrixStack matrices, ItemGroup group, CallbackInfo ci) {
		if (!isSpectrumGroup(group)) return;
		spectrum$spectrumGroupIsGettingRendered = true;
		RenderSystem.setShaderTexture(0, SpectrumItemGroups.ITEM_GROUP_BACKGROUND_TEXTURE_IDENTIFIER);
	}
	
	@ModifyArg(method = "renderTabIcon", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"), index = 3)
	private int injectCustomTabTextureLocation(int original) {
		if (!spectrum$spectrumGroupIsGettingRendered) return original;
		return ItemGroup.GROUPS[selectedTab].getColumn() == 0 ? 195 : 223;
	}
	
	@Inject(method = "renderTabIcon", at = @At("RETURN"))
	private void restoreTabTexture(MatrixStack matrices, ItemGroup group, CallbackInfo ci) {
		if (!isSpectrumGroup(group)) return;
		spectrum$spectrumGroupIsGettingRendered = false;
		RenderSystem.setShaderTexture(0, TEXTURE);
	}
	
	@Unique
	private boolean isSpectrumGroupSelected() {
		return isSpectrumGroup(ItemGroup.GROUPS[selectedTab]);
	}
	
	@Unique
	private boolean isSpectrumGroup(ItemGroup group) {
		return group == SpectrumItemGroups.MAIN;
	}
	
}