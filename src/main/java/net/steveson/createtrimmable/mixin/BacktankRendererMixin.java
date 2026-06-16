package net.steveson.createtrimmable.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import com.simibubi.create.content.equipment.armor.BacktankRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockState;
import net.steveson.createtrimmable.client.model.AddonPartialModels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

@Mixin(BacktankRenderer.class)

public class BacktankRendererMixin {

    @Inject(method = "renderSafe(Lcom/simibubi/create/content/equipment/armor/BacktankBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
            at = @At(value = "TAIL"), remap = false)
    protected void renderSafe(BacktankBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay, CallbackInfo ci, @Local(name = "blockState") BlockState blockState) {

        SuperByteBuffer trim = CachedBuffers.partialFacing(

//                getTrimModel("diamond_ore") //NO
//                getTrimModel("mechanical_press/head") //works
//                getTrimModel("mechanical_press/block") //NO
//                getTrimModel("netherite_backtank/block_cogs") //works
//                getTrimModel("netherite_backtank/block") //NO
//                getTrimModel("backtank_dune") //NO
//                AllPartialModels.MECHANICAL_PRESS_HEAD //woks
//                AddonPartialModels.ROPE_PULLEY_JEI //works
                AddonPartialModels.TEST_TRIM_DUNE //works, but Z-fighting
                , blockState,
                blockState.getValue(HORIZONTAL_FACING));
//        SuperByteBuffer trim = CachedBuffers.partial(getTrimModel(blockState), blockState);
        trim
                .scale(1 + .0001f)
//                .center()
                .translate(1 - .00005,0 - .00005,1 - .00005)
                .rotateYDegrees(180
//                        + AngleHelper.horizontalAngle(blockState.getValue(BacktankBlock.HORIZONTAL_FACING))
                )
                .light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.cutout()))
//                .renderInto(ms, buffer.getBuffer(RenderType.entityCutout(new ResourceLocation(CreateTrimmableMod.MOD_ID, "block/" + "backtank_dune"))))
        ;

        SuperByteBuffer trim2 = CachedBuffers.partialFacing(AddonPartialModels.TEST_TRIM_HOST, blockState, blockState.getValue(HORIZONTAL_FACING));
        trim2
                .scale(1 + .0001f * 2)
                .translate(1 - .00005 * 2,0 - .00005 * 2,1 - .00005 * 2)
//                .center().translate(.5,-.5,.5)
                .rotateYDegrees(180)
                .light(light).renderInto(ms, buffer.getBuffer(RenderType.cutout()));

        SuperByteBuffer trim3 = CachedBuffers.partialFacing(AddonPartialModels.CARD_CHEST_ITEM, blockState, blockState.getValue(HORIZONTAL_FACING));
        trim3
                .scale(1 + .0001f * 2)
                .translate(1 - .00005 * 2,0 - .00005 * 2,1 - .00005 * 2)
//                .center().translate(.5,-.5,.5)
                .rotateYDegrees(180)
                .light(light).renderInto(ms, buffer.getBuffer(RenderType.cutout()));
    }


//    @Unique
////    private static PartialModel getTrimModel(BlockState state) {
////
////        return PartialModel.of(new ResourceLocation(CreateTrimmableMod.MOD_ID, "block/backtank_dune"));
////    }
//    private static PartialModel getTrimModel(String path) {
////        return PartialModel.of(Create.asResource("block/" + path));
//        return PartialModel.of(new ResourceLocation(Create.ID,"block/" + path));
////        return PartialModel.of(new ResourceLocation(CreateTrimmableMod.MOD_ID,"block/" + path));
////        return PartialModel.of(new ResourceLocation("block/" + path));
//    }

}
