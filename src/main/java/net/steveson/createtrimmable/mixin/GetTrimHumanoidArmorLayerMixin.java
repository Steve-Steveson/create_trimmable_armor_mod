package net.steveson.createtrimmable.mixin;

//Copyleft Steve_Steveson, MIT License

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.item.CustomRenderedArmorItem;
import com.simibubi.create.foundation.mixin.accessor.HumanoidArmorLayerAccessor;
import dev.engine_room.flywheel.api.event.EndClientResourceReloadEvent;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraftforge.fml.ModList;
import org.slf4j.Logger;
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

@Mixin(HumanoidArmorLayer.class)
//Class name must be alphabetically before "HumanoidArmorLayerMixin" from base Create
public class GetTrimHumanoidArmorLayerMixin {
    @Unique
    private static final Logger LOGGER = LogUtils.getLogger();

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

                        if (((Optional<?>) result).isPresent()) {
                            List<ArmorTrim> armorTrimList = (List<ArmorTrim>) (((Optional<?>) result).get());
                        }
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

    @Shadow
    private void renderTrim(ArmorMaterial pArmorMaterial, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, ArmorTrim pTrim, net.minecraft.client.model.Model pModel, boolean pInnerTexture) {
    }

    @Shadow
    private void renderGlint(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, net.minecraft.client.model.Model pModel) {
    }
}
