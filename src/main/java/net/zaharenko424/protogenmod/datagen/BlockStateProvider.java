package net.zaharenko424.protogenmod.datagen;

import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.zaharenko424.protogenmod.ProtogenMod;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

@ParametersAreNonnullByDefault
public class BlockStateProvider extends net.neoforged.neoforge.client.model.generators.BlockStateProvider {

    public BlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ProtogenMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

    }

    private @NotNull ResourceLocation blockLoc(ResourceLocation loc){
        return loc.withPrefix(ModelProvider.BLOCK_FOLDER + "/");
    }

    private @NotNull ResourceLocation blockLoc(DeferredBlock<?> block){
        return blockLoc(block.getId());
    }

    private void blockExisting(DeferredBlock<?> block){
        simpleBlock(block.get(), models().getExistingFile(blockLoc(block)));
    }

    private void blockWithItem(DeferredBlock<?> block){
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }

    private void allDirectionalBlockWithItem(DeferredBlock<?> block){
        ResourceLocation id = block.getId();
        ModelFile file = models().getExistingFile(id);
        getVariantBuilder(block.get()).forAllStates(state -> rotatedModelAr(file, state.getValue(FACING)));
        itemModels().getBuilder(id.getPath()).parent(file);
    }

    private void crossWithItem(DeferredBlock<?> block){
        ResourceLocation id = block.getId();
        simpleBlock(block.get(), models().cross(id.getPath(), blockLoc(id)).renderType("cutout"));
        simpleItem(id, blockLoc(id));
    }

    private ModelFile cube(ResourceLocation textureLoc, String modelId, String str0, String str1, String str2, String str3, String str5, String str4){
        return models().cube(textureLoc+modelId, textureLoc.withSuffix(str0), textureLoc.withSuffix(str1), textureLoc.withSuffix(str2), textureLoc.withSuffix(str3), textureLoc.withSuffix(str4), textureLoc.withSuffix(str5)).texture("particle", textureLoc.withSuffix("0c"));
    }

    /*private void doublePartYBlockWOItem(DeferredBlock<?> block){
        ResourceLocation id = block.getId();
        ModelFile part0 = models().getExistingFile(blockLoc(id));
        ModelFile part_1 = models().getExistingFile(blockLoc(id).withSuffix("_1"));
        getVariantBuilder(block.get()).forAllStates(state ->
                new ConfiguredModel[]{new ConfiguredModel(state.getValue(StateProperties.PART2) == 1 ? part_1 : part0)}
        );
    }

    private void doublePartYBlockWItem(DeferredBlock<?> block){
        ResourceLocation id = block.getId();
        ModelFile part0 = models().getExistingFile(blockLoc(id));
        ModelFile part_1 = models().getExistingFile(blockLoc(id).withSuffix("_1"));
        getVariantBuilder(block.get()).forAllStates(state ->
                new ConfiguredModel[]{new ConfiguredModel(state.getValue(StateProperties.PART2) == 1 ? part_1 : part0)}
        );
        itemModels().getBuilder(id.getPath()).parent(part0);
    }

    private void machineLikeWithItem(DeferredBlock<? extends AbstractMachine> block, boolean sameActiveModel){
        if(!block.get().defaultBlockState().hasProperty(ACTIVE)) return;
        ResourceLocation loc = blockLoc(block.getId());
        ModelFile file = models().getExistingFile(loc);
        ModelFile file_active = sameActiveModel ? file : models().getExistingFile(loc.withSuffix("_active"));
        getVariantBuilder(block.get()).forAllStates(state -> {
            Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);
            if(state.getValue(ACTIVE)){
                return horizontalRotatedModelAr(file_active, direction);
            }
            return horizontalRotatedModelAr(file, direction);
        });
        simpleBlockItem(block.get(), file);
    }*/

    private void hangingSignBlock(DeferredBlock<?> signBlock, DeferredBlock<?> wallSignBlock, ResourceLocation texture) {
        ModelFile sign = models().sign(signBlock.getId().getPath(), texture);
        simpleBlock(signBlock.get(), sign);
        simpleBlock(wallSignBlock.get(), sign);
    }

    private void horizontalDirectionalBlock(DeferredBlock<? extends HorizontalDirectionalBlock> block){
        ModelFile file = models().getExistingFile(blockLoc(block));
        getVariantBuilder(block.get())
                .forAllStates(state-> horizontalRotatedModelAr(file, state.getValue(HorizontalDirectionalBlock.FACING)));
    }

    private void horizontalDirectionalBlockWithItem(DeferredBlock<? extends HorizontalDirectionalBlock> block){
        ModelFile file = models().getExistingFile(blockLoc(block));
        simpleBlockItem(block.get(), file);
        getVariantBuilder(block.get())
                .forAllStates(state-> horizontalRotatedModelAr(file, state.getValue(HorizontalDirectionalBlock.FACING)));
    }

    private ConfiguredModel horizontalRotatedModel(ModelFile file, Direction direction){
        return switch (direction){
            case EAST -> new ConfiguredModel(file, 0, 90, false);
            case SOUTH -> new ConfiguredModel(file, 0, 180, false);
            case WEST -> new ConfiguredModel(file, 0, 270, false);
            default -> new ConfiguredModel(file);
        };
    }

    private ConfiguredModel rotatedModel(ModelFile file, Direction direction){
        return switch (direction){
            case UP -> new ConfiguredModel(file, -90, 0, false);
            case DOWN -> new ConfiguredModel(file, 90, 0, false);
            default -> horizontalRotatedModel(file, direction);
        };
    }

    @Contract("_, _ -> new")
    private ConfiguredModel @NotNull [] horizontalRotatedModelAr(ModelFile file, Direction direction){
        return new ConfiguredModel[]{horizontalRotatedModel(file, direction)};
    }

    @Contract("_, _ -> new")
    private ConfiguredModel @NotNull [] rotatedModelAr(ModelFile file, Direction direction){
        return new ConfiguredModel[]{rotatedModel(file, direction)};
    }

    private void leavesWithItem(DeferredBlock<?> leaves){
        ResourceLocation id = leaves.getId();
        simpleBlockWithItem(leaves.get(), models().withExistingParent(id.getPath(),"block/leaves").texture("all", blockLoc(id)));
    }

    private void logWithItem(DeferredBlock<? extends RotatedPillarBlock> block, @Nullable ResourceLocation side, @Nullable ResourceLocation top){
        ResourceLocation id = block.getId();
        ResourceLocation loc = side != null ? side : blockLoc(id);
        ResourceLocation end = top != null ? top : blockLoc(id).withSuffix("_top");
        ModelFile vertical = models().cubeColumn(id.getPath(), loc, end);
        ModelFile horizontal = models().cubeColumnHorizontal(id.getPath() + "_horizontal", loc, end);
        getVariantBuilder(block.get())
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)
                .modelForState().modelFile(vertical).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(horizontal).rotationX(90).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X)
                .modelForState().modelFile(horizontal).rotationX(90).rotationY(90).addModel();
        itemModels().getBuilder(id.getPath()).parent(vertical);
    }

    private void pillarWithItem(DeferredBlock<?> block, @Nullable ResourceLocation up){
        ResourceLocation loc = blockLoc(block);
        ResourceLocation top = up != null ? up : loc.withSuffix("_up");
        ModelFile file = models().cube(block.getId().getPath(), top, top, loc, loc, loc, loc).texture("particle",loc);
        simpleBlockWithItem(block.get(), file);
    }

    /*private void rotatedDoublePartBlock(DeferredBlock<?> block, @Nullable String subFolder){
        ResourceLocation id = block.getId();
        if(subFolder != null) id = id.withPrefix(subFolder + "/");
        else id = id.withPrefix(id.getPath() + "/");
        ModelFile part0 = models().getExistingFile(blockLoc(id).withSuffix("_0"));
        ModelFile part1 = models().getExistingFile(blockLoc(id).withSuffix("_1"));
        getVariantBuilder(block.get()).forAllStates(state ->
                horizontalRotatedModelAr(state.getValue(StateProperties.PART2) == 1 ? part1 : part0, state.getValue(HorizontalDirectionalBlock.FACING))
        );
    }*/

    private void simpleCubeWithItem(DeferredBlock<?> block){
        ModelFile file = cubeAll(block.get());
        simpleBlockWithItem(block.get(), file);
    }

    private void simpleBlockWithItemExisting(DeferredBlock<?> block){
        ResourceLocation id = block.getId();
        ModelFile model = models().getExistingFile(blockLoc(id));
        getVariantBuilder(block.get()).forAllStates(state -> new ConfiguredModel[]{new ConfiguredModel(model)});
        itemModels().getBuilder(id.getPath()).parent(model);
    }

    private void simpleItem(ResourceLocation id, ResourceLocation texture){
        itemModels().getBuilder(id.getPath()).parent(models().getExistingFile(ResourceLocation.withDefaultNamespace("item/generated"))).texture("layer0", texture);
    }

    private void slabWithItem(DeferredBlock<? extends SlabBlock> slab, ResourceLocation texture){
        slabBlock(slab.get(), texture, texture);
        itemModels().slab(slab.getId().getPath(), texture, texture, texture);
    }

    private void stairsWithItem(DeferredBlock<? extends StairBlock> stairs, ResourceLocation texture){
        stairsBlock(stairs.get(), texture);
        itemModels().stairs(stairs.getId().getPath(), texture, texture, texture);
    }

    /*private void tableModel(){
        ResourceLocation id = blockLoc(TABLE);
        ModelFile file1 = models().getExistingFile(id.withSuffix("_top"));
        ModelFile file2 = models().getExistingFile(id.withSuffix("_leg"));
        getMultipartBuilder(TABLE.get()).part().modelFile(file1).addModel().end()
                .part().modelFile(file2).addModel().condition(Table.LEG_1,true).end()
                .part().modelFile(file2).rotationY(90).addModel().condition(Table.LEG_2,true).end()
                .part().modelFile(file2).rotationY(180).addModel().condition(Table.LEG_3,true).end()
                .part().modelFile(file2).rotationY(270).addModel().condition(Table.LEG_4,true).end();
    }

    private void cryoChamber(DeferredBlock<CryoChamber> block){
        ResourceLocation loc = blockLoc(block.getId().withPrefix("cryo_chamber/"));
        getVariantBuilder(block.get()).forAllStates(state -> horizontalRotatedModelAr(models()
                .getExistingFile(loc.withSuffix("_" + state.getValue(StateProperties.PART12)
                        + (state.getValue(CryoChamber.OPEN) ? "_open" : ""))), state.getValue(HORIZONTAL_FACING)));
    }

    private void whiteboard(){
        DeferredBlock<Whiteboard> block = WHITEBOARD;
        ResourceLocation id = blockLoc(block.getId().withPrefix(block.getId().getPath() + "/"));
        getVariantBuilder(block.get()).forAllStates(state ->
                horizontalRotatedModelAr(models().getExistingFile(id.withSuffix("_" + state.getValue(PART6))), state.getValue(HORIZONTAL_FACING)));
    }

    private void threeByThreeDoor(DeferredBlock<? extends Abstract3By3Door> block){
        ResourceLocation id = blockLoc(block.getId().withPrefix(block.getId().getPath() + "/"));
        getVariantBuilder(block.get()).forAllStates(state ->
                horizontalRotatedModelAr(models().getExistingFile(id.withSuffix("_" + state.getValue(PART9) + (state.getValue(OPEN) ? "_open" : ""))), state.getValue(HORIZONTAL_FACING)));
    }

    private void twoByTwoDoor(DeferredBlock<? extends Abstract2By2Door> block){
        ResourceLocation id = blockLoc(block.getId().withPrefix(block.getId().getPath() + "/"));
        getVariantBuilder(block.get()).forAllStates(state ->
                horizontalRotatedModelAr(models().getExistingFile(id.withSuffix("_" + state.getValue(PART4) + (state.getValue(OPEN) ? "_open" : ""))), state.getValue(HORIZONTAL_FACING)));
    }*/

    private Direction firstMatches(Object2BooleanArrayMap<Direction> map, boolean match){
        ObjectIterator<Object2BooleanMap.Entry<Direction>> iterator = map.object2BooleanEntrySet().fastIterator();
        while(iterator.hasNext()){
            Object2BooleanMap.Entry<Direction> entry = iterator.next();
            if(entry.getBooleanValue() == match) return entry.getKey();
        }
        return Direction.NORTH;
    }

    /*private void wire(DeferredBlock<WireBlock> wire){
        ResourceLocation wireLoc = AChanged.resourceLoc("block/wire/wire");
        ModelFile wire_ = models().getExistingFile(wireLoc);
        ModelFile wire_n = models().getExistingFile(wireLoc.withSuffix("_n"));
        ModelFile wire_u = models().getExistingFile(wireLoc.withSuffix("_u"));
        ModelFile wire_ns = models().getExistingFile(wireLoc.withSuffix("_ns"));
        itemModels().getBuilder(wire.getId().getPath()).parent(wire_ns);
        getMultipartBuilder(wire.get())
                .part().modelFile(wire_).addModel().end()
                .part().modelFile(wire_n).addModel().condition(NORTH, true).end()
                .part().modelFile(wire_n).rotationY(90).addModel().condition(EAST, true).end()
                .part().modelFile(wire_n).rotationY(-180).addModel().condition(SOUTH, true).end()
                .part().modelFile(wire_n).rotationY(-90).addModel().condition(WEST, true).end()
                .part().modelFile(wire_u).addModel().condition(UP, true).end()
                .part().modelFile(wire_u).rotationX(180).addModel().condition(DOWN, true).end();
    }*/

    private void connectedTextureWithItem(DeferredBlock<?> block, String subFolder){
        ResourceLocation textureLoc = block.getId().withPrefix("block/" + subFolder + "/");
        ConfiguredModel c0 = new ConfiguredModel(models().cubeAll(textureLoc + "_c0", textureLoc.withSuffix("0c")));
        itemModels().getBuilder(block.getId().getPath()).parent(c0.model);
        ModelFile c1 = cube(textureLoc,"_c1","0c","4c","1c_u","1c_u","1c_u","1c_u");
        ModelFile c2 = cube(textureLoc,"_c2","4c","4c","2c_ud","2c_ud","2c_ud","2c_ud");
        ModelFile c2angle = cube(textureLoc,"_c2angle","1c_e","4c","2c_uw","2c_ue","1c_u","4c");
        ModelFile c3t1 = cube(textureLoc,"_c3t1","4c","4c","3c_ued","3c_uwd","4c","2c_ud");
        ModelFile c3t2 = cube(textureLoc,"_c3t2","2c_we","4c","3c_uwe","3c_uwe","4c","4c");
        ModelFile c3angle = cube(textureLoc,"_c3angle","2c_ue","4c","2c_uw","4c","2c_ue","4c");
        ModelFile c4x = cube(textureLoc,"_c4x","4c","4c","4c","4c","4c","4c");
        ModelFile c4angle = cube(textureLoc,"_c4angle","4c","4c","4c","3c_uwd","4c","3c_ued");
        ConfiguredModel middle = new ConfiguredModel(models().cubeAll(textureLoc+"_middle", textureLoc.withSuffix("4c")));
        getVariantBuilder(block.get()).forAllStates(state -> {
            boolean u = state.getValue(UP);
            boolean d = state.getValue(DOWN);
            boolean n = state.getValue(NORTH);
            boolean e = state.getValue(EAST);
            boolean s = state.getValue(SOUTH);
            boolean w = state.getValue(WEST);
            if(!u && !d && !n && !e && !s && !w) return new ConfiguredModel[]{c0};

            if(u && !d && !n && !e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c1)};
            if(!u && d && !n && !e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c1,180,0,false)};
            if(!u && !d && n && !e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c1,90,0,false)};
            if(!u && !d && !n && !e && s && !w) return new ConfiguredModel[]{new ConfiguredModel(c1,-90,0,false)};
            if(!u && !d && !n && !e && !s)      return new ConfiguredModel[]{new ConfiguredModel(c1,90,-90,false)};
            if(!u && !d && !n && e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c1,90,90,false)};

            ConfiguredModel model = cornerModel2(u, d, n, e, s, w, c2angle);
            if(model != null) return new ConfiguredModel[]{model};

            if(u && d && !n && !e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c2)};
            if(!u && !d && !n && !s)           return new ConfiguredModel[]{new ConfiguredModel(c2,-90,90,false)};
            if(!u && !d && n && !e && !w)      return new ConfiguredModel[]{new ConfiguredModel(c2,-90,0,false)};

            model = model3(u, d, n, e, s, w, c3t1, c3t2, c3angle);
            if(model != null) return new ConfiguredModel[]{model};

            if(u && d && !n && e && !s)      return new ConfiguredModel[]{new ConfiguredModel(c4x)};
            if(!u && !d)                     return new ConfiguredModel[]{new ConfiguredModel(c4x,90,0,false)};
            if(u && d && n && !e && s && !w) return new ConfiguredModel[]{new ConfiguredModel(c4x,0,90,false)};

            if(u && d && n && !e && !s)      return new ConfiguredModel[]{new ConfiguredModel(c4angle)};
            if(!u && n && !e)                return new ConfiguredModel[]{new ConfiguredModel(c4angle,90,0,false)};
            if(u && !d && n && !e)           return new ConfiguredModel[]{new ConfiguredModel(c4angle,-90,0,false)};
            if(u && d && !n && !e)           return new ConfiguredModel[]{new ConfiguredModel(c4angle,0,-90,false)};
            if(u && d && n && e && !s && !w) return new ConfiguredModel[]{new ConfiguredModel(c4angle,0,90,false)};
            if(u && d && !n && !w)           return new ConfiguredModel[]{new ConfiguredModel(c4angle,0,180,false)};
            if(u && !d && n && s && !w) return new ConfiguredModel[]{new ConfiguredModel(c4angle,-90,180,false)};
            if(!u && n && s && !w)      return new ConfiguredModel[]{new ConfiguredModel(c4angle,90,180,false)};
            if(u && !d && !n)           return new ConfiguredModel[]{new ConfiguredModel(c4angle,-90,-90,false)};
            if(!u && !n)                return new ConfiguredModel[]{new ConfiguredModel(c4angle,-270,-90,false)};
            if(u && !d && !s)           return new ConfiguredModel[]{new ConfiguredModel(c4angle,-90,90,false)};
            if(!u && !s)                return new ConfiguredModel[]{new ConfiguredModel(c4angle,-270,-270,false)};

            return new ConfiguredModel[]{middle};
        });
    }

    private ConfiguredModel cornerModel2(boolean u, boolean d, boolean n, boolean e, boolean s, boolean w, ModelFile file_ue){
        if(u && !d && !n && e && !s && !w) return new ConfiguredModel(file_ue);
        if(u && !d && !n && !e && !s && w) return new ConfiguredModel(file_ue,0,180,false);
        if(!u && d && !n && e && !s && !w) return new ConfiguredModel(file_ue,180,0,false);
        if(!u && d && !n && !e && !s && w) return new ConfiguredModel(file_ue,180,180,false);
        if(u && !d && n && !e && !s && !w) return new ConfiguredModel(file_ue,0,-90,false);
        if(u && !d && !n && !e && s && !w) return new ConfiguredModel(file_ue,0,90,false);
        if(!u && d && n && !e && !s && !w) return new ConfiguredModel(file_ue,180,-90,false);
        if(!u && d && !n && !e && s && !w) return new ConfiguredModel(file_ue,180,90,false);
        if(!u && !d && n && e && !s && !w) return new ConfiguredModel(file_ue,90,0,false);
        if(!u && !d && !n && e && s && !w) return new ConfiguredModel(file_ue,-90,0,false);
        if(!u && !d && n && !e && !s && w) return new ConfiguredModel(file_ue,90,-90,false);
        if(!u && !d && !n && !e && s && w) return new ConfiguredModel(file_ue,-90,90,false);
        return null;
    }

    private ConfiguredModel model3(boolean u, boolean d, boolean n, boolean e, boolean s, boolean w, ModelFile file_wud, ModelFile file_ewu, ModelFile file_esu){
        if(u && d && !n && !e && !s && w)      return new ConfiguredModel(file_wud);
        if(u && d && !n && !e && s && !w)      return new ConfiguredModel(file_wud,0,-90,false);
        if(u && d && !n && e && !s && !w) return new ConfiguredModel(file_wud,0,-180,false);
        if(u && d && n && !e && !s && !w) return new ConfiguredModel(file_wud,0,90,false);
        if(!u && !d && n && !e && s && w)           return new ConfiguredModel(file_wud,-90,0,false);
        if(!u && !d && n && e && s && !w)      return new ConfiguredModel(file_wud,-90,180,false);

        if(u && !d && !n && e && !s && w)      return new ConfiguredModel(file_ewu);
        if(!u && !d && !n && e && s && w)                return new ConfiguredModel(file_ewu,-90,0,false);
        if(!u && d && !n && e && !s && w)      return new ConfiguredModel(file_ewu,180,0,false);
        if(!u && !d && n && e && !s && w)                return new ConfiguredModel(file_ewu,90,0,false);
        if(u && !d && n && !e && s && !w) return new ConfiguredModel(file_ewu,0,90,false);
        if(!u && d && n && !e && s && !w) return new ConfiguredModel(file_ewu,180,90,false);

        if(u && !d && !n && e && s && !w)      return new ConfiguredModel(file_esu);
        if(u && !d && !n && !e && s && w)           return new ConfiguredModel(file_esu,0,90,false);
        if(u && !d && n && e && !s && !w) return new ConfiguredModel(file_esu,90,0,false);
        if(!u && d && n && e && !s && !w) return new ConfiguredModel(file_esu,180,0,false);
        if(!u && d && !n && e && s && !w)      return new ConfiguredModel(file_esu,270,0,false);
        if(!u && d && !n && !e && s && w)           return new ConfiguredModel(file_esu,180,-180,false);
        if(!u && d && n && !e && !s && w)      return new ConfiguredModel(file_esu,-90,-180,false);
        if(u && !d && n && !e && !s && w)      return new ConfiguredModel(file_esu,90,-90,false);
        return null;
    }
}