package dev.itsmeow.whisperwoods.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.block.BlockHandOfFate;
import dev.itsmeow.whisperwoods.block.BlockWispLantern;
import dev.itsmeow.whisperwoods.util.WispColors;
import dev.itsmeow.whisperwoods.util.WispColors.WispColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.loot.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.ConstantIntValue;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
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
        protected void addTags() {
            TagAppender<Block> ghostLights = this.tag(ModTags.Blocks.GHOST_LIGHT);
            TagAppender<Block> wispLanterns = this.tag(ModTags.Blocks.WISP_LANTERN);
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
        protected void addTags() {
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
        protected void buildShapelessRecipes(Consumer<FinishedRecipe> consumer) {
            ModBlocks.getBlocks().forEach(blockEntry -> {
                Block block = blockEntry.get();
                if(block instanceof BlockWispLantern) {
                    this.makeLanternRecipe(consumer, (BlockWispLantern) block);
                } else if(block instanceof BlockHandOfFate) {
                    this.makeHOFRecipe(consumer, (BlockHandOfFate) block);
                }
            });
        }

        protected  void makeHOFRecipe(Consumer<FinishedRecipe> consumer, BlockHandOfFate block) {
            ShapedRecipeBuilder.shaped(block.asItem())
                    .define('i', Items.IRON_BARS)
                    .define('b', Items.BLAZE_POWDER)
                    .define('s', Tags.Items.STONE)
                    .pattern("ibi")
                    .pattern(" i ")
                    .pattern("sss")
                    .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                    .save(consumer);
        }

        protected void makeLanternRecipe(Consumer<FinishedRecipe> consumer, BlockWispLantern block) {
            ShapedRecipeBuilder.shaped(block.asItem())
                    .define('l', WispColors.byColor(block.getColor()).getGhostLight().get())
                    .define('n', Tags.Items.NUGGETS_IRON)
                    .pattern("nnn")
                    .pattern("nln")
                    .pattern("nnn")
                    .unlockedBy("has_ghost_light", has(ModTags.Items.GHOST_LIGHT))
                    .save(consumer);
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
                } else if(block.defaultBlockState().getRenderShape() != RenderShape.MODEL) {
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
                Direction dir = state.getValue(BlockStateProperties.FACING);
                return ConfiguredModel.builder()
                .modelFile(
                dir.getAxis().isVertical() ? (dir == Direction.UP ? hanging
                : sitting)
                : wall)
                .rotationY(dir.getAxis().isVertical() ? (((int) state.getValue(BlockWispLantern.HORIZONTAL_FACING).getOpposite().toYRot()) % 360) : (((int) dir.toYRot())) % 360)
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
            .submodel(RenderType.solid(), nestedParent(parent))
            .submodel(RenderType.translucent(), nestedParent("minecraft:block/block")
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
            return WhisperwoodsMod.MODID + "_loot_tables";
        }

        @Override
        protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootContextParamSet>> getTables() {
            return ImmutableList.of(Pair.of(WWBlockLootTables::new, LootContextParamSets.BLOCK), Pair.of(WWEntityLootTables::new, LootContextParamSets.ENTITY));
        }

        @Override
        protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
        }

        public static class WWEntityLootTables extends EntityLoot {
            @Override
            protected void addTables() {
                for(EntityType<?> type : getKnownEntities()) {
                    this.add(type, LootTable.lootTable());
                }
                this.add(ModEntities.HIRSCHGEIST.getEntityType(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1)).add(LootItem.lootTableItem(ModItems.HIRSCHGEIST_SKULL.get())).apply(SetItemCountFunction.setCount(ConstantIntValue.exactly(1)))));
            }

            @Override
            protected Iterable<EntityType<?>> getKnownEntities() {
                return ModEntities.getEntities().values().stream().map(e -> e.getEntityType()).collect(Collectors.toList());
            }
        }

        public static class WWBlockLootTables extends BlockLoot {

            @Override
            protected void addTables() {
                ModBlocks.getBlocks().forEach(this::reg);
            }

            @Override
            protected Iterable<Block> getKnownBlocks() {
                return ModBlocks.getBlocks().stream().map(RegistryObject::get).collect(Collectors.toList());
            }

            protected void reg(RegistryObject<Block> block) {
                this.dropSelf(block.get());
            }

        }

    }
}
