package net.steveson.createtrimmable.client.model;

/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.math.Transformation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraftforge.client.ForgeRenderTypes;
import net.minecraftforge.client.RenderTypeGroup;
import net.minecraftforge.client.model.CompositeModel;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.client.model.geometry.*;
import net.steveson.createtrimmable.CreateTrimmableMod;
import net.steveson.createtrimmable.client.RenderHelpers;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Map;
import java.util.function.Function;


public record TrimmedBacktankItemModel(@Nullable ArmorTrim trim) implements IUnbakedGeometry<TrimmedBacktankItemModel>
{
    // Depth offsets to prevent Z-fighting, copied from ContainedFluidModel
    public static final Transformation FLUID_TRANSFORM = new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(1.004f, 1.004f, 1.002f), new Quaternionf());
    public static final Transformation COVER_TRANSFORM = new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(1, 1, 1.004f), new Quaternionf());
    public static final Transformation WACKY_TRANSFORM = new Transformation(new Vector3f(0,0,0.25f), new Quaternionf(), new Vector3f(1, 1, 0), new Quaternionf());
    public static final Transformation WACKY_TRANSFORM_2 = new Transformation(new Vector3f(0,0,-0.25f), new Quaternionf(), new Vector3f(1, 1, 0), new Quaternionf());

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation)
    {
        final TextureAtlasSprite baseSprite = spriteGetter.apply(context.getMaterial("armor"));
//        final TextureAtlasSprite overlaySprite = context.hasMaterial("overlay") ? spriteGetter.apply(context.getMaterial("overlay")) : null;
//        final ResourceLocation trimLocation = context.getMaterial("trim").texture();



        final String color = trim != null ? trim.material().get().assetName() : null;
        final String pattern = trim != null ? trim.pattern().get().assetId().getPath() : null;

        final ResourceLocation trimLocation0 = new ResourceLocation(CreateTrimmableMod.MOD_ID, "trims/items/tank_" + pattern + "_" + color);

        if (trim != null) {

            System.out.println("MY MAT IS " + color);
            System.out.println("MY PAT IS " + pattern);
//            System.out.println("MY LOC IS " + trimLocation);
            System.out.println("MY LOC IS " + trimLocation0);
        }

//        final TextureAtlasSprite trimSprite = trim != null ? spriteGetter.apply(new Material(RenderHelpers.BLOCKS_ATLAS, trimLocation.withSuffix("_" + color))) : null;
        final TextureAtlasSprite trimSprite = trim != null ? spriteGetter.apply(new Material(RenderHelpers.BLOCKS_ATLAS, trimLocation0)) : null;



        final var itemContext = StandaloneGeometryBakingContext.builder(context).withGui3d(false).withUseBlockLight(false).build(modelLocation);
        final var builder = CompositeModel.Baked.builder(itemContext, baseSprite, new TrimOverrideHandler(overrides, baker, itemContext, this), context.getTransforms());
        final var normalRenderTypes = new RenderTypeGroup(RenderType.translucent(), ForgeRenderTypes.ITEM_UNSORTED_TRANSLUCENT.get());

        addQuads(modelState, modelLocation, baseSprite, builder, normalRenderTypes, WACKY_TRANSFORM_2);
        addQuads(modelState, modelLocation, baseSprite, builder, normalRenderTypes, WACKY_TRANSFORM);

//        //Adds overlay to leather
//        if (overlaySprite != null)
//        {
//            addQuads(modelState, modelLocation, overlaySprite, builder, normalRenderTypes, FLUID_TRANSFORM);
//        }

        if (trimSprite != null)
        {
            addQuads(modelState, modelLocation, trimSprite, builder, normalRenderTypes, WACKY_TRANSFORM);
            addQuads(modelState, modelLocation, trimSprite, builder, normalRenderTypes, WACKY_TRANSFORM_2);
        }

        builder.setParticle(baseSprite);
        return builder.build();
    }

    private static void addQuads(ModelState modelState, ResourceLocation modelLocation, TextureAtlasSprite trimSprite,
                                 CompositeModel.Baked.Builder builder, RenderTypeGroup normalRenderTypes, @Nullable Transformation transformation)
    {
        var transformedState = transformation == null ? modelState : new SimpleModelState(modelState.getRotation().compose(transformation), modelState.isUvLocked());
        var unbaked = UnbakedGeometryHelper.createUnbakedItemElements(0, trimSprite.contents());
        var quads = UnbakedGeometryHelper.bakeElements(unbaked, material -> trimSprite, transformedState, modelLocation);
        builder.addQuads(normalRenderTypes, quads);
    }



//    private static void addShape(ModelState modelState, ResourceLocation modelLocation, TextureAtlasSprite trimSprite,
//                                 MultiPartBakedModel.Builder builder, RenderTypeGroup normalRenderTypes, @Nullable Transformation transformation)
//    {
//        var transformedState = transformation == null ? modelState : new SimpleModelState(modelState.getRotation().compose(transformation), modelState.isUvLocked());
//        var unbaked = UnbakedGeometryHelper.createUnbakedItemElements(0, trimSprite.contents());
//        var quads = UnbakedGeometryHelper.bakeElements(unbaked, material -> trimSprite, transformedState, modelLocation);
//        builder.add(normalRenderTypes, quads);
//    }



    //appears to handle instantiation from json interpretation
    public static class Loader implements IGeometryLoader<TrimmedBacktankItemModel>
    {
        @Override
        public TrimmedBacktankItemModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException
        {
            return new TrimmedBacktankItemModel(null);
        }
    }
    //_______________________________________________________


    private static final class TrimOverrideHandler extends ItemOverrides
    {
        private final Map<String, BakedModel> cache = Maps.newHashMap(); // contains all the baked models since they'll never change
        private final ItemOverrides nested;
        private final ModelBaker baker;
        private final IGeometryBakingContext owner;
        private final TrimmedBacktankItemModel parent;

        private TrimOverrideHandler(ItemOverrides nested, ModelBaker baker, IGeometryBakingContext owner, TrimmedBacktankItemModel parent)
        {
            this.nested = nested;
            this.baker = baker;
            this.owner = owner;
            this.parent = parent;
        }

        @Override
        public BakedModel resolve(BakedModel originalModel, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed)
        {
            BakedModel overridden = nested.resolve(originalModel, stack, level, entity, seed);
            if (overridden != originalModel || level == null) return overridden;
            return ArmorTrim.getTrim(level.registryAccess(), stack).map(trim -> {
                final String name = trim.material().get().assetName();
                if (!cache.containsKey(name))
                {
                    TrimmedBacktankItemModel unbaked = new TrimmedBacktankItemModel(trim);
                    BakedModel bakedModel = unbaked.bake(owner, baker, Material::sprite, BlockModelRotation.X0_Y0, this, new  ResourceLocation("forge", "bucket_override"));
                    cache.put(name, bakedModel);
                    return bakedModel;
                }
                return cache.get(name);
            }).orElse(originalModel);
        }
    }

}
