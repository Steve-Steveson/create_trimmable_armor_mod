package net.steveson.createtrimmable.client.model;

import com.simibubi.create.Create;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;
import net.steveson.createtrimmable.CreateTrimmableMod;

public class AddonPartialModels {


//    public static final PartialModel CHAIR_LEFT_ARM = block("chair/chair_left_armrest");
//    public static final PartialModel CHAIR_RIGHT_ARM = block("chair/chair_right_armrest");

    public static final PartialModel
            TEST_TRIM_DUNE = block("backtank_dune"),
            TEST_TRIM_HOST = block("backtank_host"),
//
//    SMALL_SPROCKET_COGWHEEL_BLOCK = block("sprocket/small_cogwheel"),
//            LARGE_SPROCKET_COGWHEEL_BLOCK = block("sprocket/large_cogwheel"),
//            SMALL_FLANGED_COGWHEEL_BLOCK = block("flanged_gear/small_cogwheel"),
//            LARGE_FLANGED_COGWHEEL_BLOCK = block("flanged_gear/large_cogwheel"),
//
//
//    CHAIN_ROPE_COIL = block("chain_pulley/chain_coil"), CHAIN_ROPE_HALF = block("chain_pulley/chain_rope_half"),
//            CHAIN_ROPE_HALF_MAGNET = block("chain_pulley/chain_rope_half_magnet"),
//            CHAIN_ROPE = block("chain_pulley/chain_rope"),
//            CHAIN_PULLEY_MAGNET_NO_CHAIN = block("chain_pulley/chain_pulley_magnet_no_chain"),
//            CHAIN_PULLEY_MAGNET_CHAIN = block("chain_pulley/chain_pulley_magnet_chain"),
//            CHAIN_PULLEY_MAGNET_CHAIN_HALF = block("chain_pulley/chain_pulley_magnet_chain_half"),

    ROPE_PULLEY_JEI = createBlock("rope_pulley/item"),
    CARD_CHEST_ITEM = createItem("cardboard_chestplate")
//    ,

//    LARGE_STONE_COG_SHAFTLESS = block("large_stone_cog_shaftless")
    ;


//    public static final Map<Direction, PartialModel> WEATHERED_METAL_GIRDER_BRACKETS = new EnumMap<>(Direction.class);
//
//    static {
//        for (Direction d : Iterate.horizontalDirections) {
//            WEATHERED_METAL_GIRDER_BRACKETS.put(d, block("weathered_metal_girder/bracket_" + Lang.asId(d.name())));
//        }
//    }

//    public static final PartialModel GIRDER_STRUT_SEGMENT = block("girder_strut/girder");
//    public static final PartialModel WEATHERED_GIRDER_STRUT_SEGMENT = block("girder_strut/weathered_girder");

    private static PartialModel block(String path) {
        return PartialModel.of(new ResourceLocation(CreateTrimmableMod.MOD_ID, "block/" + path));
    }

    private static PartialModel createBlock(String path) {
        return PartialModel.of(Create.asResource("block/" + path));
    }
    private static PartialModel createItem(String path) {
        return PartialModel.of(Create.asResource("item/" + path));
    }

    public static void register() {
    }
}
