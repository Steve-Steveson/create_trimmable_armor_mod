package net.steveson.createtrimmable.mixin.client;

//Copyleft Steve_Steveson 2026, MIT License
//Special thanks to my brother, Steve_StevesonB, for helping me with Stacked Trims compatibility.

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.logging.LogUtils;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.armor.AllArmorMaterials;
import com.simibubi.create.foundation.item.CustomRenderedArmorItem;
import com.simibubi.create.foundation.mixin.accessor.HumanoidArmorLayerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraftforge.fml.ModList;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;
import java.util.*;

@Mixin(HumanoidArmorLayer.class)
//Class name must be alphabetically before "HumanoidArmorLayerMixin" from base Create
public class GetTrimHumanoidArmorLayerMixin {
    @Unique
    private static final Logger LOGGER = LogUtils.getLogger();

    @Shadow
    @Final
    private TextureAtlas armorTrimAtlas;


    @Inject(
            method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"
            ),
            cancellable = true
    )
    private void create$onRenderArmorPiece(PoseStack poseStack, MultiBufferSource bufferSource, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<?> model, CallbackInfo ci, @Local ItemStack stack) {
        if (stack.getItem() instanceof CustomRenderedArmorItem renderer) {
            renderer.renderArmorPiece((HumanoidArmorLayer<?, ?, ?>) (Object) this, poseStack, bufferSource, entity, slot, light, model, stack);

            HumanoidArmorLayerAccessor accessor = (HumanoidArmorLayerAccessor) this;
            HumanoidModel<?> innerModel = accessor.create$getInnerModel();

            boolean isStackedTrimsEnabled = ModList.get().isLoaded("stacked_armor_trims");
            if (isStackedTrimsEnabled && stack.getOrCreateTag().contains("Trims")) {
                try {
                    Class<?> clazz = Class.forName(
                            "io.github.apfelrauber.stacked_trims.ArmorTrimList"
                    );

                    Method method = clazz.getMethod(
                            "getTrims",
                            RegistryAccess.class,
                            ItemStack.class
                    );

                    net.minecraft.core.RegistryAccess registryAccess = entity.level().registryAccess();

                    Object result = method.invoke(
                            null,
                            registryAccess,
                            stack
                    );

                    if (result instanceof Optional<?>) {
                        ((Optional<?>) result).ifPresent((armorTrimsCapture)-> {
                            if (armorTrimsCapture instanceof List<?>) {
                                List<ArmorTrim> armorTrims = (List<ArmorTrim>)((List<?>)armorTrimsCapture ) ;
                                Collections.reverse(armorTrims);
                                for (ArmorTrim armorTrim : armorTrims) {
                                    renderTrim(ArmorMaterials.NETHERITE, poseStack, bufferSource, light, armorTrim, model, false);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    LOGGER.error("Failed to call Multiple Armor Trims", e);
                }
            } else {
                ArmorTrim.getTrim(entity.level().registryAccess(), stack).ifPresent((p_289638_) -> {
                    this.renderTrim(ArmorMaterials.NETHERITE, poseStack, bufferSource, light, p_289638_, model, false);
                });
            }

            if (stack.hasFoil()) {
                this.renderGlint(poseStack, bufferSource, light, model);
                this.renderGlint(poseStack, bufferSource, light, innerModel);
            }
            ci.cancel();
        }
    }


    @Inject(
            method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/armortrim/ArmorTrim;getTrim(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/item/ItemStack;)Ljava/util/Optional;"
            ),
            cancellable = true
    )
    private void cta$onRenderArmorPiece(PoseStack poseStack, MultiBufferSource bufferSource, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<?> model, CallbackInfo ci, @Local ItemStack stack) {
        if (stack.is(AllItems.COPPER_BACKTANK.get())) {
            boolean isStackedTrimsEnabled = ModList.get().isLoaded("stacked_armor_trims");
            if (isStackedTrimsEnabled && stack.getOrCreateTag().contains("Trims")) {
                try {
                    Class<?> clazz = Class.forName(
                            "io.github.apfelrauber.stacked_trims.ArmorTrimList"
                    );

                    Method method = clazz.getMethod(
                            "getTrims",
                            RegistryAccess.class,
                            ItemStack.class
                    );

                    net.minecraft.core.RegistryAccess registryAccess = entity.level().registryAccess();

                    Object result = method.invoke(
                            null,
                            registryAccess,
                            stack
                    );

                    if (result instanceof Optional<?>) {
                        ((Optional<?>) result).ifPresent((armorTrimsCapture)-> {
                            if (armorTrimsCapture instanceof List<?>) {
                                List<ArmorTrim> armorTrims = (List<ArmorTrim>)((List<?>)armorTrimsCapture ) ;
                                Collections.reverse(armorTrims);
                                for (ArmorTrim armorTrim : armorTrims) {
                                    renderSmallTrim(AllArmorMaterials.COPPER, poseStack, bufferSource, light, armorTrim, model);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    LOGGER.error("Failed to call Multiple Armor Trims", e);
                }
            } else {
                ArmorTrim.getTrim(entity.level().registryAccess(), stack).ifPresent((p_289638_) -> {
                    this.renderSmallTrim(AllArmorMaterials.COPPER, poseStack, bufferSource, light, p_289638_, model);
                });
            }

            if (stack.hasFoil()) {
                this.renderGlint(poseStack, bufferSource, light, model);
            }
            ci.cancel();
        }
    }


    @Unique
    private void renderSmallTrim(ArmorMaterial pArmorMaterial, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, ArmorTrim pTrim, net.minecraft.client.model.Model pModel) {
        ResourceLocation defaultTexture = pTrim.outerTexture(pArmorMaterial);
        String defaultPath = defaultTexture.getPath();
//        // prints "trims/models/armor/tide_lapis"
//        System.out.println(defaultPath);
        String[] defaultPathSplit = defaultPath.split("/");
        String defaultFileName = defaultPathSplit[defaultPathSplit.length - 1];
//        // prints "tide_lapis"
//        System.out.println(defaultFileName);
        String newPath = "trims/models/armor/card_" + defaultFileName;
        ResourceLocation newTexture = Create.asResource(newPath);
//        // prints "create:trims/models/armor/card_tide_lapis"
//        System.out.println(newTexture);

        String[] defaultFileNameSplit = defaultFileName.split("_");
        String newPNGpath = "trims/models/armor/card_" + defaultFileNameSplit[0];

//        //here to force a failure for testing only
//        if (defaultFileNameSplit[0].equalsIgnoreCase("tide")) {
//            newPNGpath = "trims/models/armor/cardo_" + defaultFileNameSplit[0];
//        }

        ResourceLocation newTexturePNG = Create.asResource("textures/" + newPNGpath + ".png");
//        // prints "create:textures/trims/models/armor/card_tide.png"
//        System.out.println(newTexturePNG);

        boolean textureExists = Minecraft.getInstance().getResourceManager().getResource(newTexturePNG).isPresent();
//        System.out.println(textureExists);


        TextureAtlasSprite textureatlassprite = this.armorTrimAtlas.getSprite(defaultTexture);
        if (textureExists) {
            textureatlassprite = this.armorTrimAtlas.getSprite(newTexture);
        }

        VertexConsumer vertexconsumer = textureatlassprite.wrap(pBuffer.getBuffer(Sheets.armorTrimsSheet()));
        pModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }


    @Shadow
    private void renderTrim(ArmorMaterial pArmorMaterial, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, ArmorTrim pTrim, net.minecraft.client.model.Model pModel, boolean pInnerTexture) {
    }

    @Shadow
    private void renderGlint(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, net.minecraft.client.model.Model pModel) {
    }
}
