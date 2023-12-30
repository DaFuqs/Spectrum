package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.item.map.MapBannerMarker;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(MapState.class)
public interface MapStateAccessor {

    @Accessor(value = "showIcons")
    boolean getShowIcons();

    @Accessor(value = "unlimitedTracking")
    boolean getUnlimitedTracking();

    @Accessor(value = "banners")
    Map<String, MapBannerMarker> getBanners();

    @Accessor(value = "icons")
    Map<String, MapIcon> getIcons();

    @Accessor(value = "iconCount")
    int getIconCount();

    @Accessor(value = "iconCount")
    void setIconCount(int iconCount);

    @Invoker("markIconsDirty")
    void invokeMarkIconsDirty();

    @Invoker("removeIcon")
    void invokeRemoveIcon(String id);

}
