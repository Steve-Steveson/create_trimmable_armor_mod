package net.steveson.createtrimmable.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.equipment.armor.BacktankArmorLayer;
import com.simibubi.create.content.equipment.armor.BacktankItem;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.level.block.state.BlockState;
import net.steveson.createtrimmable.CreateTrimmableMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BacktankArmorLayer.class)
public  class BacktankArmorLayerMixin {
    @Unique
    private ModelManager mm = Minecraft.getInstance().getModelManager();
    private final TextureAtlas armorTrimAtlas = mm.getAtlas(Sheets.ARMOR_TRIMS_SHEET);





    @Unique
    private void renderTrim(ArmorMaterial pArmorMaterial, PoseStack pPoseStack, MultiBufferSource pBuffer,
                            int pPackedLight, ArmorTrim pTrim, Model pModel) {

        Holder<TrimPattern> pattern = pTrim.pattern();
        String assetPath = pattern.value().assetId().getPath();

        Holder<TrimMaterial> material = pTrim.material();
        // Needs to detect netherite and make darker
        String colorSuffix = material.value().assetName();

        TextureAtlasSprite textureatlassprite = this.armorTrimAtlas.getSprite(new ResourceLocation(
                CreateTrimmableMod.MOD_ID, "trims/block/tank_" + assetPath + "_" + colorSuffix
        ));
//        TextureAtlasSprite textureatlassprite = this.armorTrimAtlas.getSprite(pInnerTexture ? pTrim.innerTexture(pArmorMaterial) : pTrim.outerTexture(pArmorMaterial));
        VertexConsumer vertexconsumer = textureatlassprite.wrap(pBuffer.getBuffer(Sheets.armorTrimsSheet()));
        pModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }


//    @Unique
//    protected net.minecraft.client.model.Model getArmorModelHook(T entity, ItemStack itemStack, EquipmentSlot slot, A model) {
//        return net.minecraftforge.client.ForgeHooksClient.getArmorModel(entity, itemStack, slot, model);
//    }



//    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
//            at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
//
//    public void render(PoseStack ms, MultiBufferSource buffer, int light, LivingEntity entity, float yaw, float pitch,
//                       float pt, float p_225628_8_, float p_225628_9_, float p_225628_10_, CallbackInfo ci,
//                       BacktankItem item
////            , EntityModel entityModel, HumanoidModel model, VertexConsumer vc, BlockState renderedState, SuperByteBuffer backtank, SuperByteBuffer cogs, SuperByteBuffer nob
//    ) {
//
//        net.minecraft.client.model.Model model = getArmorModelHook(pLivingEntity, itemstack, pSlot, pModel);
//
//        ArmorTrim.getTrim(entity.level().registryAccess(), item.).ifPresent((p_289638_) -> {
//            this.renderTrim(ArmorMaterials.LEATHER, ms, buffer, light, p_289638_, model);
//        });
//
//
//        System.out.println();
//    }
}
