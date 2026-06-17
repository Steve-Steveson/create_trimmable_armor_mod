package net.steveson.createtrimmable.mixin;

import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.armor.AllArmorMaterials;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimPattern;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiFunction;
import java.util.function.Function;

@Mixin(ArmorTrim.class)
public abstract class AdjustedArmorTrimMixin {
    @Shadow
    @Final
    private Holder<TrimPattern> pattern;

    @Shadow
    @Final
    private Function<ArmorMaterial, ResourceLocation> innerTexture;

    @Shadow
    @Final
    private Function<ArmorMaterial, ResourceLocation> outerTexture;



    @Shadow
    protected abstract String getColorPaletteSuffix(ArmorMaterial pArmorMaterial);


    @Unique
    private final BiFunction<Boolean, ArmorMaterial, ResourceLocation> create$textureCardboard = Util.memoize((inner, material) -> {
        String assetPath = pattern.value().assetId().getPath();
        String colorSuffix = getColorPaletteSuffix(material);
        return Create.asResource("trims/models/armor/card_" + assetPath + (inner ? "_leggings_" : "_") + colorSuffix);
    });

    @Inject(method = "innerTexture", at = @At("HEAD"), cancellable = true)
    private void create$swapTexturesForCardboardTrimsInner(ArmorMaterial pArmorMaterial, CallbackInfoReturnable<ResourceLocation> cir) {
        if (pArmorMaterial == AllArmorMaterials.CARDBOARD) {
            String assetPath = pattern.value().assetId().getPath();
            boolean textureExists = Minecraft.getInstance().getResourceManager()
                    .getResource(Create.asResource("textures/trims/models/armor/card_" + assetPath + ".png")).isPresent();

            if (textureExists) {
                cir.setReturnValue(create$textureCardboard.apply(true, pArmorMaterial));
            } else {
                cir.setReturnValue(this.innerTexture.apply(pArmorMaterial));
            }
        }
    }

    @Inject(method = "outerTexture", at = @At("HEAD"), cancellable = true)
    private void create$swapTexturesForCardboardTrimsOuter(ArmorMaterial pArmorMaterial, CallbackInfoReturnable<ResourceLocation> cir) {
        if (pArmorMaterial == AllArmorMaterials.CARDBOARD) {
            String assetPath = pattern.value().assetId().getPath();
            boolean textureExists = Minecraft.getInstance().getResourceManager()
                    .getResource(Create.asResource("textures/trims/models/armor/card_" + assetPath + ".png")).isPresent();

            if (textureExists) {
                cir.setReturnValue(create$textureCardboard.apply(false, pArmorMaterial));
            } else {
                cir.setReturnValue(this.outerTexture.apply(pArmorMaterial));
            }
        }
    }
}
