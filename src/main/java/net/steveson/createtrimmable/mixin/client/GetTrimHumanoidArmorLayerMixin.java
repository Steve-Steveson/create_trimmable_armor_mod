package net.steveson.createtrimmable.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.logging.LogUtils;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.armor.AllArmorMaterials;
import com.simibubi.create.foundation.item.CustomRenderedArmorItem;
import com.simibubi.create.foundation.mixin.accessor.HumanoidArmorLayerAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Mixin(value = ArmorFeatureRenderer.class, priority = 100)
public class GetTrimHumanoidArmorLayerMixin<A extends BipedEntityModel> {
//    @Shadow
//    @Final
//    private A innerModel;
    @Unique
    private static final Logger LOGGER = LogUtils.getLogger();

    @Shadow
    @Final
    private SpriteAtlasTexture armorTrimsAtlas;


    @Inject(
            method = "renderArmor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"
            ),
            cancellable = true
    )
    private void create$onRenderArmorPiece(MatrixStack poseStack, VertexConsumerProvider bufferSource, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<?> model, CallbackInfo ci, @Local ItemStack itemStack) {
        if (itemStack.getItem() instanceof CustomRenderedArmorItem renderer) {
            renderer.renderArmorPiece((ArmorFeatureRenderer<?, ?, ?>) (Object) this, poseStack, bufferSource, entity, slot, light, model, itemStack);

            HumanoidArmorLayerAccessor accessor = (HumanoidArmorLayerAccessor) this;
            BipedEntityModel<?> innerModel = accessor.create$getInnerModel();

            boolean isStackedTrimsEnabled = FabricLoader.getInstance().isModLoaded("stacked_trims");
            if (isStackedTrimsEnabled &&
                    itemStack.getNbt() != null &&
                    itemStack.getNbt().getList("Trims",10) != null) {
                try {
                    Class<?> clazz = Class.forName(
                            "io.github.apfelrauber.stacked_trims.ArmorTrimList"
                    );

                    Method method = clazz.getMethod(
                            "getTrims",
                            DynamicRegistryManager.class,
                            ItemStack.class
                    );

                    net.minecraft.registry.DynamicRegistryManager registryManager = entity.getWorld().getRegistryManager();

                    Object result = method.invoke(
                            null,
                            registryManager,
                            itemStack
                    );

                    if (result instanceof Optional<?>) {
                        ((Optional<?>) result).ifPresent((armorTrimsCapture)-> {
                            if (armorTrimsCapture instanceof List<?>) {
                                List<ArmorTrim> armorTrims = (List<ArmorTrim>)((List<?>)armorTrimsCapture ) ;
//                                Collections.reverse(armorTrims);
                                for (ArmorTrim armorTrim : armorTrims) {
                                    renderTrim(ArmorMaterials.NETHERITE, poseStack, bufferSource, light, armorTrim, (A) model, false);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    LOGGER.error("Failed to call Multiple Armor Trims", e);
                }
            } else {
                ArmorTrim.getTrim(entity.getWorld().getRegistryManager(), itemStack).ifPresent(trim ->
                        this.renderTrim(ArmorMaterials.NETHERITE, poseStack, bufferSource, light, trim, (A) model, false));
            }

            if (itemStack.hasGlint()) {
                this.renderGlint(poseStack, bufferSource, light, (A) model);
                this.renderGlint(poseStack, bufferSource, light, (A) innerModel);
//                this.renderGlintDirect(poseStack, bufferSource, light, model);
//                this.renderGlintDirect(poseStack, bufferSource, light, model);
            }
            ci.cancel();
        }
    }


    @Inject(
            method = "renderArmor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/trim/ArmorTrim;getTrim(Lnet/minecraft/registry/DynamicRegistryManager;Lnet/minecraft/item/ItemStack;)Ljava/util/Optional;"
            ),
            cancellable = true
    )
    private void cta$onRenderArmorPiece(MatrixStack poseStack, VertexConsumerProvider bufferSource, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<?> model, CallbackInfo ci, @Local ItemStack itemStack) {
        if (itemStack.isOf(AllItems.COPPER_BACKTANK.get())) {
            boolean isStackedTrimsEnabled = FabricLoader.getInstance().isModLoaded("stacked_trims");
            if (isStackedTrimsEnabled &&
                    itemStack.getNbt() != null &&
                    itemStack.getNbt().getList("Trims",10) != null) {
                try {
                    Class<?> clazz = Class.forName(
                            "io.github.apfelrauber.stacked_trims.ArmorTrimList"
                    );

                    Method method = clazz.getMethod(
                            "getTrims",
                            DynamicRegistryManager.class,
                            ItemStack.class
                    );

                    net.minecraft.registry.DynamicRegistryManager registryManager = entity.getWorld().getRegistryManager();

                    Object result = method.invoke(
                            null,
                            registryManager,
                            itemStack
                    );

                    if (result instanceof Optional<?>) {
                        ((Optional<?>) result).ifPresent((armorTrimsCapture)-> {
                            if (armorTrimsCapture instanceof List<?>) {
                                List<ArmorTrim> armorTrims = (List<ArmorTrim>)((List<?>)armorTrimsCapture ) ;
//                                Collections.reverse(armorTrims);
                                for (ArmorTrim armorTrim : armorTrims) {
                                    renderSmallTrim(AllArmorMaterials.COPPER, poseStack, bufferSource, light, armorTrim, (A) model);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    LOGGER.error("Failed to call Multiple Armor Trims", e);
                }
            } else {
                ArmorTrim.getTrim(entity.getWorld().getRegistryManager(), itemStack).ifPresent(trim ->
                        this.renderSmallTrim(AllArmorMaterials.COPPER, poseStack, bufferSource, light, trim, (A) model));
            }

            if (itemStack.hasGlint()) {
                this.renderGlint(poseStack, bufferSource, light, (A) model);
            }
            ci.cancel();
        }
    }


    @Unique
    private void renderSmallTrim(ArmorMaterial pArmorMaterial, MatrixStack pPoseStack, VertexConsumerProvider pBuffer, int pPackedLight, ArmorTrim pTrim, net.minecraft.client.model.Model pModel) {
        Identifier defaultTexture = pTrim.getGenericModelId(pArmorMaterial);
        String defaultPath = defaultTexture.getPath();

        String[] defaultPathSplit = defaultPath.split("/");
        String defaultFileName = defaultPathSplit[defaultPathSplit.length - 1];

        String newPath = "trims/models/armor/card_" + defaultFileName;
        Identifier newTexture = Create.asResource(newPath);


        String[] defaultFileNameSplit = defaultFileName.split("_");
        String newPNGpath = "trims/models/armor/card_" + defaultFileNameSplit[0];

        Identifier newTexturePNG = Create.asResource("textures/" + newPNGpath + ".png");

        boolean textureExists = MinecraftClient.getInstance().getResourceManager().getResource(newTexturePNG).isPresent();


        Sprite textureatlassprite = this.armorTrimsAtlas.getSprite(defaultTexture);
        if (textureExists) {
            textureatlassprite = this.armorTrimsAtlas.getSprite(newTexture);
        }

        VertexConsumer vertexConsumer = textureatlassprite.getTextureSpecificVertexConsumer(pBuffer.getBuffer(TexturedRenderLayers.getArmorTrims()));
        pModel.render(pPoseStack, vertexConsumer, pPackedLight, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Shadow
    private void renderTrim(
            ArmorMaterial material, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorTrim trim, A model, boolean leggings
    ) {

    }

    @Shadow
    private void renderGlint(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, A model) {
    }

//    private void renderGlintDirect(MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, net.minecraft.client.model.Model model) {
//        model.render(poseStack, bufferSource.getBuffer(RenderLayer.getArmorEntityGlint()), packedLight, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
//    }
}