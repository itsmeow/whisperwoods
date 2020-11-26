package dev.itsmeow.whisperwoods.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.block.BlockHandOfFate;
import dev.itsmeow.whisperwoods.block.BlockWispLantern;
import dev.itsmeow.whisperwoods.util.WispColors;
import dev.itsmeow.whisperwoods.util.WispColors.WispColor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder.Perspective;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.client.model.generators.loaders.MultiLayerModelBuilder;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = WhisperwoodsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        WWBlockTagsProvider blockTags = new WWBlockTagsProvider(event.getGenerator(), event.getExistingFileHelper());
        event.getGenerator().addProvider(blockTags);
        event.getGenerator().addProvider(new WWItemTagsProvider(event.getGenerator(), blockTags, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new WWBlockStateProvider(event.getGenerator(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(new WWItemModelProvider(event.getGenerator(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(new WWLootTableProvider(event.getGenerator()));
        event.getGenerator().addProvider(new WWRecipeProvider(event.getGenerator()));
        ModEntities.H.gatherData(event.getGenerator(), event.getExistingFileHelper());
    }

    public static class WWBlockTagsProvider extends BlockTagsProvider {

        public WWBlockTagsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, WhisperwoodsMod.MODID, existingFileHelper);
        }

        @Override
        public String getName() {
            return WhisperwoodsMod.MODID + "_block_tags";
        }

        @Override
        protected void registerTags() {
            Builder<Block> ghostLights = this.getOrCreateBuilder(ModTags.Blocks.GHOST_LIGHT);
            Builder<Block> wispLanterns = this.getOrCreateBuilder(ModTags.Blocks.WISP_LANTERN);
            for(WispColor color : WispColors.values()) {
                ghostLights.add(color.getGhostLight().get());
                wispLanterns.add(color.getLantern().get());
            }
        }
    }

    public static class WWItemTagsProvider extends ItemTagsProvider {

        public WWItemTagsProvider(DataGenerator generator, WWBlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
            super(generator, blockTagProvider, WhisperwoodsMod.MODID, existingFileHelper);
        }

        @Override
        public String getName() {
            return WhisperwoodsMod.MODID + "_item_tags";
        }

        @Override
        protected void registerTags() {
            this.copy(ModTags.Blocks.GHOST_LIGHT, ModTags.Items.GHOST_LIGHT);
            this.copy(ModTags.Blocks.WISP_LANTERN, ModTags.Items.WISP_LANTERN);
        }
    }

    public static class WWItemModelProvider extends ItemModelProvider {

        public WWItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, WhisperwoodsMod.MODID, existingFileHelper);
        }

        @Override
        public String getName() {
            return WhisperwoodsMod.MODID + "_items";
        }

        @Override
        protected void registerModels() {
            ModItems.getItems().forEach(itemEntry -> {
                Item item = itemEntry.get();
                if(!(item instanceof BlockItem)) {
                    this.withExistingParent(item.getRegistryName().getPath().toString(), "minecraft:item/generated").texture("layer0", WhisperwoodsMod.MODID + ":items/" + item.getRegistryName().getPath().toString());
                }
            });
        }
        
    }

    public static class WWRecipeProvider extends RecipeProvider {

        public WWRecipeProvider(DataGenerator generator) {
            super(generator);
        }

        @Override
        public String getName() {
            return WhisperwoodsMod.MODID + "_recipes";
        }

        @Override
        protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
            ModBlocks.getBlocks().forEach(blockEntry -> {
                Block block = blockEntry.get();
                if(block instanceof BlockWispLantern) {
                    this.makeLanternRecipe(consumer, (BlockWispLantern) block);
                } else if(block instanceof BlockHandOfFate) {
                    this.makeHOFRecipe(consumer, (BlockHandOfFate) block);
                }
            });
        }

        protected  void makeHOFRecipe(Consumer<IFinishedRecipe> consumer, BlockHandOfFate block) {
            ShapedRecipeBuilder.shapedRecipe(block.asItem())
                    .key('i', Items.IRON_BARS)
                    .key('b', Items.BLAZE_POWDER)
                    .key('s', Tags.Items.STONE)
                    .patternLine("ibi")
                    .patternLine(" i ")
                    .patternLine("sss")
                    .addCriterion("has_blaze_powder", hasItem(Items.BLAZE_POWDER))
                    .build(consumer);
        }

        protected void makeLanternRecipe(Consumer<IFinishedRecipe> consumer, BlockWispLantern block) {
            ShapedRecipeBuilder.shapedRecipe(block.asItem())
                    .key('l', WispColors.byColor(block.getColor()).getGhostLight().get())
                    .key('n', Tags.Items.NUGGETS_IRON)
                    .patternLine("nnn")
                    .patternLine("nln")
                    .patternLine("nnn")
                    .addCriterion("has_ghost_light", hasItem(ModTags.Items.GHOST_LIGHT))
                    .build(consumer);
        }

    }

    public static class WWBlockStateProvider extends BlockStateProvider {

        public WWBlockStateProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, WhisperwoodsMod.MODID, existingFileHelper);
        }

        @Override
        public String getName() {
            return WhisperwoodsMod.MODID + "_blocks";
        }

        @Override
        protected void registerStatesAndModels() {
            ModBlocks.getBlocks().forEach(blockEntry -> {
                Block block = blockEntry.get();
                if(block instanceof BlockWispLantern) {
                    this.buildWispLanternState(block);
                } else if(block.getDefaultState().getRenderType() != BlockRenderType.MODEL) {
                    this.soulSand(block);
                }
            });
        }

        protected void soulSand(Block block) {
            this.itemModels().withExistingParent(block.getRegistryName().getPath().toString(), "minecraft:item/generated").texture("layer0", WhisperwoodsMod.MODID + ":items/" + block.getRegistryName().getPath().toString());
            // Not working. Don't do anything for now.
            //this.getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(this.models().getExistingFile(new ResourceLocation("minecraft:block/soul_sand"))).build());
        }

        protected void buildWispLanternState(Block block) {
            BlockModelBuilder hanging = wispLanternModel(block, "hanging");
            BlockModelBuilder sitting = wispLanternModel(block, "sitting");
            BlockModelBuilder wall = wispLanternModel(block, "wall");
            this.itemModels().getBuilder(block.getRegistryName().getPath().toString()).parent(hanging);
            this.getVariantBuilder(block)
            .forAllStatesExcept(state -> {
                Direction dir = state.get(BlockStateProperties.FACING);
                return ConfiguredModel.builder()
                .modelFile(
                dir.getAxis().isVertical() ? (dir == Direction.UP ? hanging
                : sitting)
                : wall)
                .rotationY(dir.getAxis().isVertical() ? (((int) state.get(BlockWispLantern.HORIZONTAL_FACING).getOpposite().getHorizontalAngle()) % 360) : (((int) dir.getHorizontalAngle())) % 360)
                .build();
            }, BlockStateProperties.WATERLOGGED);
        }

        protected BlockModelBuilder wispLanternModel(Block block, String ext) {
            String lensName = block.getRegistryName().getPath().replace("wisp_lantern", "wisp_lantern_lens");
            ResourceLocation parent = rl("block/wisp_lantern_" + ext);
            final Map<Direction, Float[]> uvs = new HashMap<Direction, Float[]>();
            uvs.put(Direction.NORTH, new Float[] { 0.5F, 12.5F, 3.5F, 15.5F });
            uvs.put(Direction.EAST, new Float[] { 0F, 12.5F, 0.5F, 15.5F });
            uvs.put(Direction.SOUTH, new Float[] { 4F, 12.5F, 7F, 15.5F });
            uvs.put(Direction.WEST, new Float[] { 3.5F, 12.5F, 4F, 15.5F });
            uvs.put(Direction.UP, new Float[] { 3.5F, 12.5F, 0.5F, 12F });
            uvs.put(Direction.DOWN, new Float[] { 6.5F, 12F, 3.5F, 12.5F });
            return this.models().withExistingParent(block.getRegistryName().getPath() + "_" + ext, "minecraft:block/block")
            .texture("particle", rl("blocks/wisp_lantern"))
            .transforms()
            .transform(Perspective.THIRDPERSON_LEFT)
            .rotation(69F, 0F, 0F)
            .translation(0F, -3.25F, -3.25F)
            .scale(0.6F)
            .end()
            .transform(Perspective.THIRDPERSON_RIGHT)
            .rotation(69F, 0F, 0F)
            .translation(0F, -3.25F, -3.25F)
            .scale(0.6F)
            .end()
            .end()
            .customLoader(MultiLayerModelBuilder::begin)
            .submodel(RenderType.getSolid(), nestedParent(parent))
            .submodel(RenderType.getTranslucent(), nestedParent("minecraft:block/block")
            .element()
            .from(5, 2, ext.equals("wall") ? 7.8F : 3.8F)
            .to(11, 8, ext.equals("wall") ? 8.8F : 4.8F)
            .rotation().angle(0).axis(Axis.Z).origin(0, 8, 0).end()
            .allFaces((dir, b) -> b.texture("#lens").uvs(uvs.get(dir)[0], uvs.get(dir)[1], uvs.get(dir)[2], uvs.get(dir)[3]))
            .end()
            .texture("lens", rl("blocks/" + lensName)))
            .end();
        }

        protected ExistingModelFile blockModelExt(Block block, String ext) {
            return this.models().getExistingFile(blockModelExtRL(block, ext));
        }

        protected ResourceLocation blockModelExtRL(Block block, String ext) {
            return rl("block/" + block.getRegistryName().getPath() + "_" + ext);
        }

        protected BlockModelBuilder nestedParent(String parent) {
            return nestedParent(new ResourceLocation(parent));
        }

        protected BlockModelBuilder nestedParent(ResourceLocation parent) {
            return this.models().nested().parent(this.models().getExistingFile(parent));
        }
        
        protected static ResourceLocation rl(String text) {
            return new ResourceLocation(WhisperwoodsMod.MODID, text);
        }
    }

    public static class WWLootTableProvider extends LootTableProvider {

        public WWLootTableProvider(DataGenerator generator) {
            super(generator);
        }

        @Override
        public String getName() {
            return WhisperwoodsMod.MODID + "_block_loot_tables";
        }

        @Override
        protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootParameterSet>> getTables() {
            return ImmutableList.of(Pair.of(WWBlockLootTables::new, LootParameterSets.BLOCK));
        }

        @Override
        protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
        }

        public static class WWBlockLootTables extends BlockLootTables {

            @Override
            protected void addTables() {
                ModBlocks.getBlocks().forEach(this::reg);
            }

            @Override
            protected Iterable<Block> getKnownBlocks() {
                return ModBlocks.getBlocks().stream().map(RegistryObject::get).collect(Collectors.toList());
            }

            protected void reg(RegistryObject<Block> block) {
                this.registerDropSelfLootTable(block.get());
            }

        }

    }
}
