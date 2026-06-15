package net.steveson.createtrimmable.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.logging.LogUtils;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.steveson.createtrimmable.CreateTrimmableMod;
import net.steveson.createtrimmable.client.RenderHelpers;
import org.slf4j.Logger;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;
import static net.steveson.createtrimmable.client.RenderHelpers.BLOCKS_ATLAS;

public enum TrimmedBacktankModel implements SimpleStaticBlockEntityModel<TrimmedBacktankModel, BacktankBlockEntity> {
    INSTANCE;

    private final ModelManager MODEL_MANAGER = Minecraft.getInstance().getModelManager();
    private final TextureAtlas armorTrimAtlas = MODEL_MANAGER.getAtlas(Sheets.ARMOR_TRIMS_SHEET);

    private static final Logger LOGGER = LogUtils.getLogger();


    @Override
    public TextureAtlasSprite render(BacktankBlockEntity blockEntity, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay) {
        final BlockState state = blockEntity.getBlockState();
        boolean isNetherite = AllBlocks.NETHERITE_BACKTANK.has(state);
        ResourceLocation createModTexture = new ResourceLocation(Create.ID, isNetherite ? "block/netherite_backtank" : "block/copper_backtank");

        CompoundTag compoundTag = blockEntity.getVanillaTag();
        boolean enchanted = compoundTag.contains("Enchantments");
        boolean trimmed = compoundTag.contains("Trim") || compoundTag.contains("Trims");


        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(BLOCKS_ATLAS).apply(createModTexture);


        if (trimmed) {
            Tag pTrim = compoundTag.get("Trim");
            System.out.println("My Trim Is : " + pTrim);
//            ArmorTrim armortrim = ArmorTrim.getTrim();
            ArmorTrim armortrim = ArmorTrim.CODEC.parse(RegistryOps.create(NbtOps.INSTANCE, blockEntity.getLevel().registryAccess()), pTrim)
                    .resultOrPartial(LOGGER::error)
                    .orElse((ArmorTrim)null)
                    ;

            System.out.println("My Trim Is : " + armortrim);



            Holder<TrimPattern> pattern = armortrim.pattern();
            String assetPath = pattern.value().assetId().getPath();

            Holder<TrimMaterial> material = armortrim.material();
            // Needs to detect netherite and make darker
            String colorSuffix = material.value().assetName();

            System.out.println("My Trim Pattern Is : " + pattern);
            System.out.println("My Trim Material Is : " + material);

            System.out.println("My Trim Pattern Is : " + assetPath);
            System.out.println("My Trim Material Is : " + colorSuffix);

            ResourceLocation trimRL = new ResourceLocation(
                    CreateTrimmableMod.MOD_ID, "trims/items/tank_" + assetPath + "_" + colorSuffix);

            System.out.println("My Trim Resource Location Is : " + trimRL);

            TextureAtlasSprite textureatlassprite = this.armorTrimAtlas.getSprite(trimRL);

            RenderHelpers.renderTexturedCuboid(poseStack, buffer, textureatlassprite, packedLight, packedOverlay, 0,0,0,1,1,1,
                    16, 16,16, true);
        }

//        RenderHelpers.renderProgrammableTexturedCuboid(poseStack, buffer, sprite, packedLight, packedOverlay, 0.25f,0.25f,0.25f,0.75f,0.625f,0.75f,
//                4, 3,4, true);
        RenderHelpers.renderTexturedCuboid(poseStack, buffer, sprite, packedLight, packedOverlay, 0,0,0,1,1,1,
                16, 16,16, true);

//        renderModel(poseStack, buffer, packedLight);


//        System.out.println(
//                "I AM THE PRINT LINE! MY DIRECTION IS: " +
//                state.getValue(HORIZONTAL_FACING)
//        );
//        System.out.println(
//                "I THINK I AM NETHERITE! THIS STATEMENT IS: " + isNetherite
//        );
        System.out.println(
                "THIS MIGHT BE ABSOLUTE GIBBERISH: " + compoundTag
        );
        if (enchanted) System.out.println("I AM ENCHANTED");
        if (trimmed) System.out.println("I AM TRIMMED");

        return sprite;
    }

    @Override
    public BlockEntityType<BacktankBlockEntity> type() {
        return AllBlockEntityTypes.BACKTANK.get();
    }

    @Override
    public int faces(BacktankBlockEntity blockEntity) {
        return 23;
//        return 23 + 58;
    }

    private void renderModel(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, ArmorItem pArmorItem,
                             net.minecraft.client.model.Model pModel, boolean pWithGlint, float pRed, float pGreen, float pBlue, ResourceLocation armorResource) {
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.armorCutoutNoCull(armorResource));
        pModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, pRed, pGreen, pBlue, 1.0F);
    }


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
}
