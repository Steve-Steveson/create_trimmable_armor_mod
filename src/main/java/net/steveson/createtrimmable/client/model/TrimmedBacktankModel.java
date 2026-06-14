package net.steveson.createtrimmable.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.block.entity.BlockEntityType;

public enum TrimmedBacktankModel implements SimpleStaticBlockEntityModel<TrimmedBacktankModel, BacktankBlockEntity> {
    INSTANCE;

    @Override
    public TextureAtlasSprite render(BacktankBlockEntity blockEntity, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay) {
        return null;
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
}
