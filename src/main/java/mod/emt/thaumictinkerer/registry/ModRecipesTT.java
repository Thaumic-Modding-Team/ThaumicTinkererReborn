package mod.emt.thaumictinkerer.registry;

import com.google.common.collect.Lists;
import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.recipes.NecromancyRecipe;
import mod.emt.thaumictinkerer.config.ConfigHandlerTT;
import mod.emt.thaumictinkerer.recipes.NecromancyRecipeRegistry;
import mod.emt.thaumictinkerer.utils.helpers.ItemHelper;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.*;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.entities.monster.*;
import thaumcraft.common.entities.monster.tainted.EntityTaintCrawler;
import thaumcraft.common.entities.monster.tainted.EntityTaintSwarm;
import thaumcraft.common.lib.crafting.DustTriggerMultiblock;
import thaumcraft.common.lib.crafting.InfusionEnchantmentRecipe;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

@SuppressWarnings("ConstantConditions")
public class ModRecipesTT {
    private static final ResourceLocation defaultGroup = new ResourceLocation("");

    public static void initRecipes(RegistryEvent.Register<IRecipe> event) {
        initNecromancyPlatform();
        initArcaneWorkbenchRecipes();
        initCraftingRecipes();
        initCrucibleRecipes();
        initInfusionRecipes();
        initInfusionEnchantmentRecipes();
        initNecromancyRecipes();
    }

    private static void initNecromancyPlatform() {
        if(!ConfigHandlerTT.necromancyTablet.enable)
            return;

        Part PEDI = new Part(BlocksTC.pedestalArcane, null);
        Part NECR = new Part(new ItemStack(ModBlocksTT.NECROMANCY_TABLET, 1, 0), new ItemStack(ModBlocksTT.NECROMANCY_TABLET, 1, 1));
        Part QUAR = new Part(ModBlocksTT.ARCANE_QUARTZ_BLOCK, null);
        Part NETH = new Part(Blocks.NETHER_BRICK, null);

        Part[][][] blueprint = new Part[][][]{
                {
                        {null, null, null, PEDI, null, null, null},
                        {null, PEDI, null, null, null, PEDI, null},
                        {null, null, null, null, null, null, null},
                        {PEDI, null, null, NECR, null, null, PEDI},
                        {null, null, null, null, null, null, null},
                        {null, PEDI, null, null, null, PEDI, null},
                        {null, null, null, PEDI, null, null, null}
                },
                {
                        {null, null, NETH, NETH, NETH, null, null},
                        {null, NETH, NETH, QUAR, NETH, NETH, null},
                        {NETH, NETH, QUAR, QUAR, QUAR, NETH, NETH},
                        {NETH, QUAR, QUAR, QUAR, QUAR, QUAR, NETH},
                        {NETH, NETH, QUAR, QUAR, QUAR, NETH, NETH},
                        {null, NETH, NETH, QUAR, NETH, NETH, null},
                        {null, null, NETH, NETH, NETH, null, null}
                }
        };
        IDustTrigger.registerDustTrigger(new DustTriggerMultiblock("TT_NECROMANCY", blueprint));
        ThaumcraftApi.addMultiblockRecipeToCatalog(new ResourceLocation(ThaumicTinkerer.MOD_ID, "necromancy_altar"), new ThaumcraftApi.BluePrint(
                "TT_NECROMANCY",
                blueprint,
                new ItemStack(Blocks.NETHER_BRICK, 24),
                new ItemStack(ModBlocksTT.ARCANE_QUARTZ_BLOCK, 13),
                new ItemStack(ModBlocksTT.NECROMANCY_TABLET)
        ));
    }

    private static void initArcaneWorkbenchRecipes() {
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "arcane_quartz"), new ShapedArcaneRecipe(
                defaultGroup,
                "THAUMIC_TINKERER_BASE",
                5,
                new AspectList(),
                new ItemStack(ModItemsTT.ARCANE_QUARTZ, 8),
                "QQQ",
                "QCQ",
                "QQQ",
                'Q', "gemQuartz",
                'C', ThaumcraftApiHelper.makeCrystal(Aspect.DARKNESS)
        ));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "attractor_item"), new ShapedArcaneRecipe(
                defaultGroup,
                "TT_ATTRACTOR_ITEM",
                75,
                new AspectList().add(Aspect.AIR, 1).add(Aspect.EARTH, 1).add(Aspect.ORDER, 1).add(Aspect.ENTROPY, 1),
                new ItemStack(ModBlocksTT.ATTRACTOR_ITEM),
                " I ",
                "QIQ",
                "WEW",
                'I', "ingotIron",
                'Q', ModItemsTT.ARCANE_QUARTZ,
                'W', BlocksTC.logGreatwood,
                'E', new ItemStack(ItemsTC.nuggets, 1, 10)
        ));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "attractor_mob"), new ShapedArcaneRecipe(
                defaultGroup,
                "TT_ATTRACTOR_MOB",
                75,
                new AspectList().add(Aspect.AIR, 1).add(Aspect.EARTH, 1).add(Aspect.ORDER, 1).add(Aspect.ENTROPY, 1),
                new ItemStack(ModBlocksTT.ATTRACTOR_MOB),
                " I ",
                "QIQ",
                "WEW",
                'I', "ingotThaumium",
                'Q', ModItemsTT.ARCANE_QUARTZ,
                'W', BlocksTC.logGreatwood,
                'E', new ItemStack(ItemsTC.nuggets, 1, 10)
        ));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "attractor_player"), new ShapedArcaneRecipe(
                defaultGroup,
                "TT_ATTRACTOR_PLAYER",
                75,
                new AspectList().add(Aspect.AIR, 1).add(Aspect.EARTH, 1).add(Aspect.ORDER, 1).add(Aspect.ENTROPY, 1),
                new ItemStack(ModBlocksTT.ATTRACTOR_PLAYER),
                " I ",
                "QIQ",
                "WEW",
                'I', "gemDiamond",
                'Q', ModItemsTT.ARCANE_QUARTZ,
                'W', BlocksTC.logGreatwood,
                'E', new ItemStack(ItemsTC.nuggets, 1, 10)
        ));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "dissimulation_block"), new ShapedArcaneRecipe(
                defaultGroup,
                "TT_DISSIMULATION_BLOCK",
                30,
                new AspectList().add(Aspect.ORDER, 1).add(Aspect.ENTROPY, 1),
                new ItemStack(ModBlocksTT.DISSIMULATION_BLOCK, 5),
                "SCS",
                "PSP",
                "SCS",
                'S', BlocksTC.stoneArcane,
                'P', Items.PRISMARINE_SHARD,
                'C', Items.CLAY_BALL
        ));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "escape_sigil"), new ShapedArcaneRecipe(
                defaultGroup,
                "TT_ESCAPE_SIGIL",
                10,
                new AspectList().add(Aspect.ORDER, 1),
                new ItemStack(ModItemsTT.ESCAPE_SIGIL),
                " Q ",
                "QPQ",
                " Q ",
                'Q', ModItemsTT.ARCANE_QUARTZ,
                'P', Items.ENDER_PEARL
        ));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "essentia_funnel"), new ShapedArcaneRecipe(
                defaultGroup,
                "TT_ESSENTIA_FUNNEL",
                60,
                new AspectList().add(Aspect.ORDER, 1).add(Aspect.ENTROPY, 1),
                new ItemStack(ModBlocksTT.ESSENTIA_FUNNEL),
                "ITI",
                "ITI",
                " I ",
                'I', "ingotThaumium",
                'T', BlocksTC.tube
        ));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "ethereal_platform"), new ShapedArcaneRecipe(
                defaultGroup,
                "TT_ETHEREAL_PLATFORM",
                50,
                new AspectList().add(Aspect.AIR, 5).add(Aspect.ORDER, 2).add(Aspect.ENTROPY, 2),
                new ItemStack(ModBlocksTT.ETHEREAL_PLATFORM, 4),
                "ASA",
                "SPS",
                "ASA",
                'A', ThaumcraftApiHelper.makeCrystal(Aspect.AIR),
                'S', BlocksTC.stoneArcane,
                'P', Items.ENDER_PEARL
        ));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "passable_wall"), new ShapedArcaneRecipe(
                defaultGroup,
                "TT_PASSABLE_WALL",
                50,
                new AspectList().add(Aspect.WATER, 5).add(Aspect.ORDER, 2).add(Aspect.ENTROPY, 2),
                new ItemStack(ModBlocksTT.PASSABLE_WALL, 4),
                "WSW",
                "SPS",
                "WSW",
                'W', ThaumcraftApiHelper.makeCrystal(Aspect.WATER),
                'S', BlocksTC.stoneArcane,
                'P', Items.ENDER_PEARL
        ));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "terra_obsidian"), new ShapedArcaneRecipe(
                defaultGroup,
                "TT_TERRA_OBSIDIAN",
                15,
                new AspectList().add(Aspect.ORDER, 2).add(Aspect.EARTH, 2),
                new ItemStack(ModBlocksTT.TERRA_OBSIDIAN),
                " E ",
                "POP",
                " E ",
                'E', ThaumcraftApiHelper.makeCrystal(Aspect.EARTH),
                'P', ThaumcraftApiHelper.makeCrystal(Aspect.PROTECT),
                'O', "obsidian"
        ));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "transvector_binder"), new ShapedArcaneRecipe(
                defaultGroup,
                "TT_TRANSVECTOR_DISLOCATOR",
                25,
                new AspectList().add(Aspect.ORDER, 2).add(Aspect.ENTROPY, 2),
                new ItemStack(ModItemsTT.TRANSVECTOR_BINDER),
                " IP",
                " CI",
                "S  ",
                'I', "ingotIron",
                'P', Items.ENDER_PEARL,
                'C', ItemHelper.getVisCrystalIngredient(),
                'S', "stickWood"
        ));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "transvector_dislocator"), new ShapedArcaneRecipe(
                defaultGroup,
                "TT_TRANSVECTOR_DISLOCATOR",
                100,
                new AspectList().add(Aspect.ORDER, 5),
                new ItemStack(ModBlocksTT.TRANSVECTOR_DISLOCATOR),
                "SMS",
                "GRG",
                "QCQ",
                'S', BlocksTC.stoneArcane,
                'G', ItemsTC.mirroredGlass,
                'R', ItemsTC.visResonator,
                'M', BlocksTC.mirror,
                'Q', ModBlocksTT.ARCANE_QUARTZ_BLOCK,
                'C', ItemsTC.mechanismComplex
        ));
    }

    private static void initCraftingRecipes() {
        GameRegistry.addShapedRecipe(
                new ResourceLocation(ThaumicTinkerer.MOD_ID, "vitium_stone"),
                null,
                new ItemStack(ModBlocksTT.VITIUM_STONE, 8),
                "CCC",
                "C#C",
                "CCC",
                'C', "cobblestone",
                '#', ThaumcraftApiHelper.makeCrystal(Aspect.FLUX)
        );
    }

    private static void initCrucibleRecipes() {
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "aa_prismarine"), new CrucibleRecipe(
                "TT_AQUATIC_ALCHEMY",
                new ItemStack(Items.PRISMARINE_SHARD),
                "paneGlass",
                new AspectList().add(Aspect.WATER, 5).add(Aspect.EARTH, 5)
        ));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "aa_sea_lantern"), new CrucibleRecipe(
                "TT_AQUATIC_ALCHEMY",
                new ItemStack(Blocks.SEA_LANTERN),
                new ItemStack(Blocks.REDSTONE_LAMP),
                new AspectList().add(Aspect.WATER, 40).add(Aspect.ENERGY, 40).add(Aspect.CRYSTAL, 40)
        ));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "aa_sponge"), new CrucibleRecipe(
                "TT_AQUATIC_ALCHEMY",
                new ItemStack(Blocks.SPONGE, 1, 0),
                "wool",
                new AspectList().add(Aspect.WATER, 5).add(Aspect.TRAP, 5).add(Aspect.VOID, 5)
        ));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "everfull_bucket"), new CrucibleRecipe(
                "TT_EVERFULL_BUCKET",
                new ItemStack(ModItemsTT.EVERFULL_BUCKET),
                Items.BUCKET,
                new AspectList().add(Aspect.WATER, 30).add(Aspect.CRAFT, 10).add(Aspect.VOID, 10)
        ));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "umbral_rosebush"), new CrucibleRecipe(
                "TT_UMBRAL_ROSEBUSH",
                new ItemStack(ModBlocksTT.UMBRAL_ROSEBUSH),
                new ItemStack(Blocks.DOUBLE_PLANT, 1, 4),
                new AspectList().add(Aspect.DARKNESS, 30).add(Aspect.LIFE, 30)
        ));
    }

    private static void initInfusionRecipes() {
        ItemStack condorSwordStack = new ItemStack(ModItemsTT.CONDOR_SWORD);
        EnumInfusionEnchantment.addInfusionEnchantment(condorSwordStack, EnumInfusionEnchantment.ARCING, 3);
        if(ModEnchantsTT.EDUCATIONAL != null) {
            EnumInfusionEnchantment.addInfusionEnchantment(condorSwordStack, ModEnchantsTT.EDUCATIONAL, 1);
        }
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "condor_sword"), new InfusionRecipe(
                "TT_CONDOR_SWORD",
                condorSwordStack,
                4,
                new AspectList().add(Aspect.AIR, 150).add(Aspect.MOTION, 150).add(Aspect.AVERSION, 60).add(Aspect.MIND, 60).add(Aspect.ENERGY, 40),
                new ItemStack(ItemsTC.elementalSword),
                ThaumcraftApiHelper.makeCrystal(Aspect.AIR),
                ThaumcraftApiHelper.makeCrystal(Aspect.AIR),
                new ItemStack(ItemsTC.nuggets, 1, 10),
                ModBlocksTT.ARCANE_QUARTZ_BLOCK
        ));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "consuming_sigil"), new InfusionRecipe(
                "TT_CONSUMING_SIGIL",
                new ItemStack(ModItemsTT.CONSUMING_SIGIL),
                2,
                new AspectList().add(Aspect.VOID, 50),
                new ItemStack(ItemsTC.baubles, 1, 1),
                Items.ENDER_PEARL,
                Items.LAVA_BUCKET
        ));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "ender_mirror"), new InfusionRecipe(
                "TT_ENDER_MIRROR",
                new ItemStack(ModItemsTT.ENDER_MIRROR),
                4,
                new AspectList().add(Aspect.MAN, 60).add(Aspect.VOID, 40).add(Aspect.DARKNESS, 40).add(Aspect.EXCHANGE, 25),
                ItemsTC.handMirror,
                "obsidian",
                "obsidian",
                "obsidian",
                "obsidian",
                "obsidian",
                ModItemsTT.ARCANE_QUARTZ,
                ModItemsTT.ARCANE_QUARTZ,
                Items.ENDER_EYE
        ));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "energetic_nitor"), new InfusionRecipe(
                "TT_ENERGETIC_NITOR",
                new ItemStack(ModItemsTT.ENERGETIC_NITOR),
                3,
                new AspectList().add(Aspect.ENERGY, 60).add(Aspect.LIGHT, 45).add(Aspect.FIRE, 45).add(Aspect.AIR, 30),
                "nitor",
                ThaumcraftApiHelper.makeCrystal(Aspect.DARKNESS),
                ThaumcraftApiHelper.makeCrystal(Aspect.SENSES),
                ThaumcraftApiHelper.makeCrystal(Aspect.DARKNESS),
                ThaumcraftApiHelper.makeCrystal(Aspect.SENSES)
        ));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "feline_charm"), new InfusionRecipe(
                "TT_FELINE_CHARM",
                new ItemStack(ModItemsTT.FELINE_CHARM),
                5,
                new AspectList().add(Aspect.ENTROPY, 100).add(Aspect.BEAST, 100).add(Aspect.SOUL, 40),
                new ItemStack(ItemsTC.baubles, 1, 4),
                new ItemStack(ItemsTC.nuggets, 1, 10),
                new ItemStack(Items.FISH, 1, 0),
                "string",
                new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage()),
                new ItemStack(Blocks.SAPLING, 1, 3),
                new ItemStack(Items.SKULL, 1, 4)
        ));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "goliath_ring"), new InfusionRecipe(
                "TT_GOLIATH_RING",
                new ItemStack(ModItemsTT.GOLIATH_RING),
                6,
                new AspectList().add(Aspect.PROTECT, 75).add(Aspect.LIFE, 50),
                new ItemStack(ItemsTC.baubles, 1, 5),
                ThaumcraftApiHelper.makeCrystal(Aspect.PROTECT),
                new ItemStack(Items.SHIELD),
                ThaumcraftApiHelper.makeCrystal(Aspect.PROTECT),
                new ItemStack(Items.DIAMOND_CHESTPLATE)
        ));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "ignium_striker"), new InfusionRecipe(
                "TT_IGNIUM_STRIKER",
                new ItemStack(ModItemsTT.IGNIUM_STRIKER),
                2,
                new AspectList().add(Aspect.FIRE, 40).add(Aspect.METAL, 40).add(Aspect.TOOL, 30).add(Aspect.MAGIC, 10),
                new ItemStack(Items.FLINT_AND_STEEL),
                ThaumcraftApiHelper.makeCrystal(Aspect.FIRE),
                "ingotThaumium",
                ThaumcraftApiHelper.makeCrystal(Aspect.METAL),
                ModItemsTT.ARCANE_QUARTZ
        ));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "recall_stopwatch"), new InfusionRecipe(
                "TT_RECALL_STOPWATCH",
                new ItemStack(ModItemsTT.RECALL_STOPWATCH),
                10,
                new AspectList().add(Aspect.MECHANISM, 100).add(Aspect.EXCHANGE, 100).add(Aspect.MAGIC, 50).add(Aspect.LIFE, 30).add(Aspect.SOUL, 30),
                Items.CLOCK,
                ModItemsTT.ARCANE_QUARTZ,
                ModItemsTT.ESCAPE_SIGIL,
                ModItemsTT.ARCANE_QUARTZ,
                ModItemsTT.ESCAPE_SIGIL,
                ModItemsTT.ARCANE_QUARTZ,
                ModItemsTT.ESCAPE_SIGIL,
                ModItemsTT.ARCANE_QUARTZ,
                ModItemsTT.ESCAPE_SIGIL
        ));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "thaumic_cake"), new InfusionRecipe(
                "TT_THAUMIC_CAKE",
                new ItemStack(ModBlocksTT.THAUMIC_CAKE),
                2,
                new AspectList().add(Aspect.CRAFT, 45).add(Aspect.DESIRE, 30).add(Aspect.ORDER, 30).add(Aspect.FLUX, 15).add(Aspect.ELDRITCH, 15),
                Items.CAKE,
                ItemsTC.salisMundus,
                Items.EGG,
                ThaumcraftApiHelper.makeCrystal(Aspect.DESIRE),
                Items.MILK_BUCKET,
                Items.EGG,
                ThaumcraftApiHelper.makeCrystal(Aspect.LIFE)
        ));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "xp_talisman"), new InfusionRecipe(
                "TT_XP_TALISMAN",
                new ItemStack(ModItemsTT.XP_TALISMAN),
                5,
                new AspectList().add(Aspect.MIND, 100).add(Aspect.DESIRE, 75).add(Aspect.EXCHANGE, 75).add(Aspect.MAGIC, 50),
                new ItemStack(ItemsTC.baubles, 1, 4),
                "ingotGold",
                ModItemsTT.ARCANE_QUARTZ,
                "ingotGold",
                new IngredientNBTTC(new ItemStack(Items.ENCHANTED_BOOK)),
                "ingotGold",
                ModItemsTT.ARCANE_QUARTZ,
                "ingotGold",
                BlocksTC.jarBrain
        ));
    }

    private static void initInfusionEnchantmentRecipes() {
        InfusionEnchantmentRecipe educationalInfusion = new InfusionEnchantmentRecipe(
                ModEnchantsTT.EDUCATIONAL,
                new AspectList().add(Aspect.MIND, 100).add(Aspect.DESIRE, 50).add(Aspect.MAGIC, 30),
                new IngredientNBTTC(new ItemStack(Items.ENCHANTED_BOOK)),
                Blocks.BOOKSHELF,
                ModItemsTT.ARCANE_QUARTZ
        );
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "educational_infusion"), educationalInfusion);
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "educational_infusion_fake1"), new InfusionEnchantmentRecipe(
                educationalInfusion, new ItemStack(Items.WOODEN_SWORD)));
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "educational_infusion_fake2"), new InfusionEnchantmentRecipe(
                educationalInfusion, new ItemStack(Items.WOODEN_PICKAXE)));

        InfusionEnchantmentRecipe projectingInfusion = new InfusionEnchantmentRecipe(
                ModEnchantsTT.PROJECTING,
                new AspectList().add(Aspect.DESIRE, 100).add(Aspect.TOOL, 80),
                new IngredientNBTTC(new ItemStack(Items.ENCHANTED_BOOK)),
                Blocks.PISTON,
                ModItemsTT.ARCANE_QUARTZ
        );
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "projecting_infusion"), projectingInfusion);
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "projecting_infusion_fake"), new InfusionEnchantmentRecipe(
                projectingInfusion, new ItemStack(Items.WOODEN_PICKAXE)));

        InfusionEnchantmentRecipe wrathInfusion = new InfusionEnchantmentRecipe(
                ModEnchantsTT.WRATH,
                new AspectList().add(Aspect.AVERSION, 80).add(Aspect.ENERGY, 60),
                new IngredientNBTTC(new ItemStack(Items.ENCHANTED_BOOK)),
                ModItemsTT.ARCANE_QUARTZ,
                ModItemsTT.ARCANE_QUARTZ,
                ModItemsTT.ARCANE_QUARTZ
        );
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "wrath_infusion"), wrathInfusion);
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "wrath_infusion_fake"), new InfusionEnchantmentRecipe(
                wrathInfusion, new ItemStack(Items.WOODEN_SWORD)));
    }

    public static void initNecromancyRecipes() {
        if(!ConfigHandlerTT.necromancyTablet.enable)
            return;

        /* Peaceful Mobs */
        //Common Animals
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:chicken"), new NecromancyRecipe()
                .setSummonedEntity(EntityChicken.class)
                .setAspects(new AspectList().add(Aspect.BEAST, 5).add(Aspect.FLIGHT, 5).add(Aspect.AIR, 5))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_PEACEFUL))
                .setComponents(
                        new OreIngredient("feather"),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromItem(Items.EGG),
                        Ingredient.fromItem(Items.CHICKEN)
                )
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:cow"), new NecromancyRecipe()
                .setSummonedEntity(EntityCow.class)
                .setAspects(new AspectList().add(Aspect.BEAST, 15).add(Aspect.EARTH, 15))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_PEACEFUL))
                .setComponents(
                        new OreIngredient("leather"),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromItem(Items.MILK_BUCKET),
                        Ingredient.fromItem(Items.BEEF)
                )
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:pig"), new NecromancyRecipe()
                .setSummonedEntity(EntityPig.class)
                .setAspects(new AspectList().add(Aspect.BEAST, 10).add(Aspect.EARTH, 10).add(Aspect.DESIRE, 5))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_PEACEFUL))
                .setComponents(
                        new OreIngredient("leather"),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.merge(Lists.newArrayList(Ingredient.fromItem(ItemsTC.tallow), new OreIngredient("tallow"))),
                        Ingredient.fromItem(Items.PORKCHOP)
                )
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:rabbit"), new NecromancyRecipe()
                .setSummonedEntity(EntityRabbit.class)
                .setAspects(new AspectList().add(Aspect.BEAST, 5).add(Aspect.EARTH, 5).add(Aspect.MOTION, 5))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_PEACEFUL))
                .setComponents(
                        Ingredient.fromItem(Items.RABBIT_HIDE),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.merge(Lists.newArrayList(Ingredient.fromItem(Items.CARROT), Ingredient.fromItem(Items.RABBIT_FOOT))),
                        Ingredient.fromItem(Items.RABBIT)
                )
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:sheep"), new NecromancyRecipe()
                .setSummonedEntity(EntitySheep.class)
                .setAspects(new AspectList().add(Aspect.BEAST, 10).add(Aspect.EARTH, 10))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_PEACEFUL))
                .setComponents(
                        new OreIngredient("wool"),
                        new OreIngredient("wool"),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromItem(Items.MUTTON)
                )
        );

        //Companion Animals
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:donkey"), new NecromancyRecipe()
                .setSummonedEntity(EntityDonkey.class)
                .setAspects(new AspectList().add(Aspect.BEAST, 15).add(Aspect.EARTH, 5).add(Aspect.AIR, 5))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_PEACEFUL))
                .setComponents(
                        Ingredient.fromItem(Item.getItemFromBlock(Blocks.HAY_BLOCK)),
                        Ingredient.fromItem(Item.getItemFromBlock(Blocks.CHEST)),
                        new OreIngredient("leather"),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromItem(Items.SUGAR),
                        Ingredient.fromItem(Items.GOLDEN_CARROT),
                        Ingredient.fromItem(Items.ROTTEN_FLESH)
                )
                .setConsumeComponents(true)
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:horse"), new NecromancyRecipe()
                .setSummonedEntity(EntityHorse.class)
                .setAspects(new AspectList().add(Aspect.BEAST, 15).add(Aspect.EARTH, 5).add(Aspect.AIR, 5))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_PEACEFUL))
                .setComponents(
                        Ingredient.fromItem(Item.getItemFromBlock(Blocks.HAY_BLOCK)),
                        Ingredient.fromItem(Items.LEAD),
                        new OreIngredient("leather"),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromItem(Items.SUGAR),
                        Ingredient.fromItem(Items.GOLDEN_CARROT),
                        Ingredient.fromItem(Items.ROTTEN_FLESH)
                )
                .setConsumeComponents(true)
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:llama"), new NecromancyRecipe()
                .setSummonedEntity(EntityLlama.class)
                .setAspects(new AspectList().add(Aspect.BEAST, 15).add(Aspect.WATER, 5))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_PEACEFUL))
                .setComponents(
                        new OreIngredient("leather"),
                        new OreIngredient("wool"),
                        new OreIngredient("wool"),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER)),
                        Ingredient.fromItem(Items.ROTTEN_FLESH)
                )
                .setConsumeComponents(true)
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:ocelot"), new NecromancyRecipe()
                .setSummonedEntity(EntityOcelot.class)
                .setAspects(new AspectList().add(Aspect.BEAST, 10).add(Aspect.ENTROPY, 10))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_PEACEFUL))
                .setComponents(
                        Ingredient.fromItem(Items.FISH),
                        Ingredient.fromItem(Items.FISH),
                        new OreIngredient("string"),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 3)),
                        Ingredient.fromItem(Items.ROTTEN_FLESH)
                )
                .setConsumeComponents(true)
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:parrot"), new NecromancyRecipe()
                .setSummonedEntity(EntityParrot.class)
                .setAspects(new AspectList().add(Aspect.BEAST, 5).add(Aspect.FLIGHT, 5).add(Aspect.SENSES, 5))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_PEACEFUL))
                .setComponents(
                        new OreIngredient("feather"),
                        new OreIngredient("feather"),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 3)),
                        Ingredient.fromItem(Items.COOKIE),
                        Ingredient.fromItem(Items.ROTTEN_FLESH)
                )
                .setConsumeComponents(true)
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:wolf"), new NecromancyRecipe()
                .setSummonedEntity(EntityWolf.class)
                .setAspects(new AspectList().add(Aspect.BEAST, 15).add(Aspect.EARTH, 10).add(Aspect.AVERSION, 5))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_PEACEFUL))
                .setComponents(
                        Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, 14)),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromItem(Items.MUTTON),
                        Ingredient.fromItem(Items.MUTTON),
                        Ingredient.fromItem(Items.ROTTEN_FLESH)
                )
                .setConsumeComponents(true)
        );

        //Uncommon Animals
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:polar_bear"), new NecromancyRecipe()
                .setSummonedEntity(EntityPolarBear.class)
                .setAspects(new AspectList().add(Aspect.BEAST, 15).add(Aspect.COLD, 10))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_PEACEFUL))
                .setComponents(
                        new OreIngredient("leather"),
                        new OreIngredient("fish"),
                        new OreIngredient("fish"),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromItem(Item.getItemFromBlock(Blocks.PACKED_ICE)),
                        Ingredient.fromItem(Item.getItemFromBlock(Blocks.PACKED_ICE)),
                        Ingredient.fromItem(Items.ROTTEN_FLESH)
                )
                .setConsumeComponents(true)
        );

        /* Hostile */
        //Common Hostiles
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:creeper"), new NecromancyRecipe()
                .setSummonedEntity(EntityCreeper.class)
                .setAspects(new AspectList().add(Aspect.PLANT, 15).add(Aspect.FIRE, 15))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_HOSTILE))
                .setComponents(
                        new OreIngredient("gunpowder"),
                        Ingredient.fromItem(Items.BONE),
                        new OreIngredient("treeLeaves"),
                        Ingredient.fromItem(Item.getItemFromBlock(Blocks.TNT))
                )
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:skeleton"), new NecromancyRecipe()
                .setSummonedEntity(EntitySkeleton.class)
                .setAspects(new AspectList().add(Aspect.UNDEAD, 20).add(Aspect.MAN, 5).add(Aspect.EARTH, 5))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_HOSTILE))
                .setComponents(
                        Ingredient.fromItem(Items.ARROW),
                        Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 15)),
                        Ingredient.fromStacks(new ItemStack(Items.BOW, 1, OreDictionary.WILDCARD_VALUE)),
                        Ingredient.fromItem(Items.BONE)
                )
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:spider"), new NecromancyRecipe()
                .setSummonedEntity(EntitySpider.class)
                .setAspects(new AspectList().add(Aspect.BEAST, 10).add(Aspect.ENTROPY, 10).add(Aspect.TRAP, 10))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_HOSTILE))
                .setComponents(
                        new OreIngredient("string"),
                        Ingredient.fromItem(Items.SPIDER_EYE),
                        Ingredient.fromItem(Item.getItemFromBlock(Blocks.WEB)),
                        Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 15))
                )
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:zombie"), new NecromancyRecipe()
                .setSummonedEntity(EntityZombie.class)
                .setAspects(new AspectList().add(Aspect.UNDEAD, 20).add(Aspect.MAN, 10).add(Aspect.EARTH, 5))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_HOSTILE))
                .setComponents(
                        new OreIngredient("feather"),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromItem(ItemsTC.brain),
                        Ingredient.fromItem(Items.ROTTEN_FLESH)
                )
        );

        //Uncommon Hostiles
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:cave_spider"), new NecromancyRecipe()
                .setSummonedEntity(EntityCaveSpider.class)
                .setAspects(new AspectList().add(Aspect.BEAST, 5).add(Aspect.DEATH, 10).add(Aspect.TRAP, 10))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_HOSTILE))
                .setComponents(
                        new OreIngredient("string"),
                        Ingredient.fromItem(Items.SPIDER_EYE),
                        Ingredient.fromItem(Item.getItemFromBlock(Blocks.WEB)),
                        Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.POISON)),
                        Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 15))
                )
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:husk"), new NecromancyRecipe()
                .setSummonedEntity(EntityHusk.class)
                .setAspects(new AspectList().add(Aspect.UNDEAD, 20).add(Aspect.MAN, 10).add(Aspect.FIRE, 5))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_HOSTILE))
                .setComponents(
                        new OreIngredient("feather"),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromItem(ItemsTC.brain),
                        new OreIngredient("sand"),
                        Ingredient.fromItem(Items.ROTTEN_FLESH)
                )
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:silverfish"), new NecromancyRecipe()
                .setSummonedEntity(EntitySilverfish.class)
                .setAspects(new AspectList().add(Aspect.BEAST, 5).add(Aspect.EARTH, 10))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_HOSTILE))
                .setComponents(
                        Ingredient.fromStacks(new ItemStack(Blocks.STONE, 1, 0)),
                        Ingredient.fromItem(Item.getItemFromBlock(Blocks.COBBLESTONE)),
                        Ingredient.fromStacks(new ItemStack(Blocks.STONEBRICK, 1, 2)),
                        Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 15))
                )
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:stray"), new NecromancyRecipe()
                .setSummonedEntity(EntityStray.class)
                .setAspects(new AspectList().add(Aspect.UNDEAD, 20).add(Aspect.MAN, 5).add(Aspect.TRAP, 5))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_HOSTILE))
                .setComponents(
                        Ingredient.fromItem(Items.ARROW),
                        Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 15)),
                        Ingredient.fromStacks(new ItemStack(Items.BOW, 1, OreDictionary.WILDCARD_VALUE)),
                        Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.SLOWNESS)),
                        Ingredient.fromItem(Items.BONE)
                )
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("thaumcraft:brainy_zombie"), new NecromancyRecipe()
                .setSummonedEntity(EntityBrainyZombie.class)
                .setAspects(new AspectList().add(Aspect.UNDEAD, 20).add(Aspect.MAN, 10).add(Aspect.MIND, 5).add(Aspect.AVERSION, 5))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_HOSTILE))
                .setComponents(
                        new OreIngredient("feather"),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromItem(ItemsTC.brain),
                        Ingredient.fromItem(ItemsTC.brain),
                        Ingredient.fromStacks(new ItemStack(Items.SKULL, 1, 2)),
                        Ingredient.fromItem(Items.ROTTEN_FLESH)
                )
        );

        //Rare Hostiles
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("thaumcraft:furious_zombie"), new NecromancyRecipe()
                .setSummonedEntity(EntityGiantBrainyZombie.class)
                .setAspects(new AspectList().add(Aspect.UNDEAD, 25).add(Aspect.MAN, 15).add(Aspect.MIND, 5).add(Aspect.AVERSION, 10))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_HOSTILE))
                .setComponents(
                        new OreIngredient("feather"),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromItem(ItemsTC.brain),
                        Ingredient.fromItem(ItemsTC.brain),
                        Ingredient.fromItem(ItemsTC.brain),
                        Ingredient.fromItem(ItemsTC.voidSeed),
                        Ingredient.fromStacks(new ItemStack(Items.SKULL, 1, 2)),
                        Ingredient.fromItem(Items.ROTTEN_FLESH)
                )
        );

        // TODO: Add this in the future. Maybe only make it spawn small slimes?
//        ThaumcraftApi.registerEntityTag("Slime", (new AspectList()).add(Aspect.LIFE, 10).add(Aspect.WATER, 10).add(Aspect.ALCHEMY, 5), new ThaumcraftApi.EntityTagsNBT[0]);

        /* Arcane Mobs */
        //NPCs
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:villager"), new NecromancyRecipe()
                .setSummonedEntity(EntityVillager.class)
                .setAspects(new AspectList().add(Aspect.MAN, 15))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_ARCANE))
                .setComponents(
                        new OreIngredient("blockEmerald"),
                        Ingredient.fromStacks(new ItemStack(Items.GOLDEN_APPLE, 1, 0)),
                        Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.HEALING)),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromStacks(new ItemStack(Items.SKULL, 1, 0), new ItemStack(Items.SKULL, 1, 2)),
                        Ingredient.fromItem(ItemsTC.brain),
                        Ingredient.fromItem(Items.ROTTEN_FLESH)
                )
                .setConsumeComponents(true)
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("thaumcraft:pech"), new NecromancyRecipe()
                .setSummonedEntity(EntityPech.class)
                .setAspects(new AspectList().add(Aspect.MAN, 10).add(Aspect.AURA, 5).add(Aspect.EXCHANGE, 10).add(Aspect.DESIRE, 5))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_ARCANE))
                .setComponents(
                        new OreIngredient("blockGold"),
                        Ingredient.fromStacks(new ItemStack(ItemsTC.nuggets, 1, 10)),
                        Ingredient.fromItem(ItemsTC.fabric),
                        new OreIngredient("enderpearl"),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromStacks(new ItemStack(Items.SKULL, 1, 0), new ItemStack(Items.SKULL, 1, 2)),
                        Ingredient.fromItem(ItemsTC.brain),
                        Ingredient.fromItem(Items.ROTTEN_FLESH)
                )
                .setConsumeComponents(true)
        );

        //Hostile NPCs
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:evoker"), new NecromancyRecipe()
                .setSummonedEntity(EntityEvoker.class)
                .setAspects(new AspectList().add(Aspect.ELDRITCH, 5).add(Aspect.MAGIC, 5).add(Aspect.MAN, 10))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_ARCANE))
                .setComponents(
                        new OreIngredient("blockGold"),
                        Ingredient.fromItem(ItemsTC.fabric),
                        Ingredient.fromItem(ItemsTC.fabric),
                        Ingredient.fromStacks(new ItemStack(ItemsTC.nuggets, 1, 10)),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromStacks(new ItemStack(Items.SKULL, 1, 0), new ItemStack(Items.SKULL, 1, 2)),
                        Ingredient.fromItem(ItemsTC.brain),
                        Ingredient.fromItem(Items.ROTTEN_FLESH)
                )
                .setConsumeComponents(true)
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:vindicator"), new NecromancyRecipe()
                .setSummonedEntity(EntityVindicator.class)
                .setAspects(new AspectList().add(Aspect.AVERSION, 5).add(Aspect.MAGIC, 5).add(Aspect.MAN, 10))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_ARCANE))
                .setComponents(
                        new OreIngredient("blockEmerald"),
                        new OreIngredient("leather"),
                        new OreIngredient("leather"),
                        Ingredient.fromStacks(new ItemStack(Items.IRON_AXE, 1, OreDictionary.WILDCARD_VALUE)),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromStacks(new ItemStack(Items.SKULL, 1, 0), new ItemStack(Items.SKULL, 1, 2)),
                        Ingredient.fromItem(ItemsTC.brain),
                        Ingredient.fromItem(Items.ROTTEN_FLESH)
                )
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:witch"), new NecromancyRecipe()
                .setSummonedEntity(EntityWitch.class)
                .setAspects(new AspectList().add(Aspect.MAN, 15).add(Aspect.MAGIC, 5).add(Aspect.ALCHEMY, 10))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_ARCANE))
                .setComponents(
                        new OreIngredient("dustRedstone"),
                        new OreIngredient("dustGlowstone"),
                        new OreIngredient("gunpowder"),
                        Ingredient.fromItem(Items.SUGAR),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromStacks(new ItemStack(Items.SKULL, 1, 0), new ItemStack(Items.SKULL, 1, 2)),
                        Ingredient.fromItem(ItemsTC.brain),
                        Ingredient.fromItem(Items.ROTTEN_FLESH)
                )
        );

        //Rare Animals
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:mooshroom"), new NecromancyRecipe()
                .setSummonedEntity(EntityMooshroom.class)
                .setAspects(new AspectList().add(Aspect.BEAST, 15).add(Aspect.PLANT, 15).add(Aspect.EARTH, 15))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_ARCANE))
                .setComponents(
                        new OreIngredient("leather"),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromItem(Items.MUSHROOM_STEW),
                        Ingredient.fromItem(Item.getItemFromBlock(Blocks.RED_MUSHROOM)),
                        Ingredient.fromItem(Item.getItemFromBlock(Blocks.RED_MUSHROOM)),
                        Ingredient.fromItem(Item.getItemFromBlock(Blocks.RED_MUSHROOM)),
                        Ingredient.fromStacks(new ItemStack(ItemsTC.nuggets, 1, 10)),
                        Ingredient.fromItem(Items.BEEF)
                )
                .setConsumeComponents(true)
        );

        // TODO: Add these horses in a future update
        //        ThaumcraftApi.registerEntityTag("SkeletonHorse", (new AspectList()).add(Aspect.BEAST, 5).add(Aspect.UNDEAD, 10).add(Aspect.EARTH, 5).add(Aspect.AIR, 5), new ThaumcraftApi.EntityTagsNBT[0]);
        //        ThaumcraftApi.registerEntityTag("ZombieHorse", (new AspectList()).add(Aspect.BEAST, 10).add(Aspect.UNDEAD, 5).add(Aspect.EARTH, 5).add(Aspect.AIR, 5), new ThaumcraftApi.EntityTagsNBT[0]);

        /* Nether Mobs */
        //Common Nether Hostiles
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:zombie_pigman"), new NecromancyRecipe()
                .setSummonedEntity(EntityPigZombie.class)
                .setAspects(new AspectList().add(Aspect.UNDEAD, 15).add(Aspect.FIRE, 15).add(Aspect.BEAST, 10))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_DEMONIC))
                .setComponents(
                        new OreIngredient("blockGold"),
                        new OreIngredient("leather"),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromStacks(new ItemStack(Items.GOLDEN_SWORD, 1, OreDictionary.WILDCARD_VALUE)),
                        Ingredient.fromItem(ItemsTC.brain),
                        Ingredient.fromItem(Items.ROTTEN_FLESH),
                        Ingredient.fromItem(Items.PORKCHOP)
                )
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("thaumcraft:firebat"), new NecromancyRecipe()
                .setSummonedEntity(EntityFireBat.class)
                .setAspects(new AspectList().add(Aspect.BEAST, 5).add(Aspect.FLIGHT, 5).add(Aspect.FIRE, 10))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_DEMONIC))
                .setComponents(
                        new OreIngredient("leather"),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromItem(Items.BLAZE_POWDER),
                        Ingredient.fromItem(Items.BLAZE_POWDER),
                        new OreIngredient("gunpowder"),
                        new OreIngredient("gunpowder"),
                        Ingredient.fromItem(Items.ROTTEN_FLESH)
                )
        );

        //Uncommon Nether Hostiles
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:blaze"), new NecromancyRecipe()
                .setSummonedEntity(EntityBlaze.class)
                .setAspects(new AspectList().add(Aspect.ELDRITCH, 5).add(Aspect.FIRE, 15).add(Aspect.FLIGHT, 5))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_DEMONIC))
                .setComponents(
                        Ingredient.fromItem(Item.getItemFromBlock(Blocks.NETHER_BRICK)),
                        Ingredient.fromItem(Items.FIRE_CHARGE),
                        Ingredient.fromItem(Items.BLAZE_ROD),
                        Ingredient.fromItem(Items.BLAZE_ROD),
                        Ingredient.fromItem(Items.BLAZE_ROD),
                        Ingredient.fromItem(Items.BLAZE_ROD),
                        Ingredient.fromItem(Items.BLAZE_POWDER)
                )
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:magma_cube"), new NecromancyRecipe()
                .setSummonedEntity(EntityMagmaCube.class)
                .setAspects(new AspectList().add(Aspect.WATER, 5).add(Aspect.FIRE, 10).add(Aspect.ALCHEMY, 5))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_DEMONIC))
                .setComponents(
                        Ingredient.fromItem(Items.MAGMA_CREAM),
                        Ingredient.fromItem(Items.MAGMA_CREAM),
                        Ingredient.fromItem(Items.MAGMA_CREAM),
                        Ingredient.fromItem(Item.getItemFromBlock(Blocks.MAGMA)),
                        Ingredient.fromItem(Item.getItemFromBlock(Blocks.MAGMA)),
                        Ingredient.fromItem(Item.getItemFromBlock(Blocks.MAGMA)),
                        Ingredient.fromItem(Item.getItemFromBlock(Blocks.MAGMA)),
                        Ingredient.fromItem(Items.BLAZE_POWDER)
                )
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:wither_skeleton"), new NecromancyRecipe()
                .setSummonedEntity(EntityWitherSkeleton.class)
                .setAspects(new AspectList().add(Aspect.UNDEAD, 25).add(Aspect.MAN, 5).add(Aspect.ENTROPY, 10))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_DEMONIC))
                .setComponents(
                        new OreIngredient("blockCoal"),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromItem(Items.BONE),
                        Ingredient.fromStacks(new ItemStack(Items.STONE_SWORD, 1, OreDictionary.WILDCARD_VALUE)),
                        Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 15)),
                        Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 15)),
                        Ingredient.fromStacks(new ItemStack(Items.SKULL, 1, 1))
                )
        );

        /* Alien Mobs */
        //Common End Hostiles
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:enderman"), new NecromancyRecipe()
                .setSummonedEntity(EntityEnderman.class)
                .setAspects(new AspectList().add(Aspect.ELDRITCH, 10).add(Aspect.MOTION, 15).add(Aspect.DESIRE, 5))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_ALIEN))
                .setComponents(
                        new OreIngredient("grass"),
                        new OreIngredient("netherrack"),
                        new OreIngredient("endstone"),
                        Ingredient.fromItem(Items.ENDER_EYE)
                )
        );

        //Rare End Hostiles
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:endermite"), new NecromancyRecipe()
                .setSummonedEntity(EntityEndermite.class)
                .setAspects(new AspectList().add(Aspect.BEAST, 5).add(Aspect.ELDRITCH, 5).add(Aspect.MOTION, 5))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_ALIEN))
                .setComponents(
                        new OreIngredient("endstone"),
                        Ingredient.fromItem(Item.getItemFromBlock(Blocks.END_BRICKS)),
                        Ingredient.fromItem(ItemsTC.salisMundus),
                        Ingredient.fromItem(Items.ENDER_EYE)
                )
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:shulker"), new NecromancyRecipe()
                .setSummonedEntity(EntityShulker.class)
                .setAspects(new AspectList().add(Aspect.ELDRITCH, 10).add(Aspect.TRAP, 5).add(Aspect.FLIGHT, 5).add(Aspect.PROTECT, 5))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_ALIEN))
                .setComponents(
                        Ingredient.fromItem(Item.getItemFromBlock(Blocks.PURPUR_BLOCK)),
                        Ingredient.fromItem(Item.getItemFromBlock(Blocks.PURPUR_BLOCK)),
                        Ingredient.merge(Lists.newArrayList(Ingredient.fromItem(Items.SKULL), new OreIngredient("skull"))),
                        Ingredient.fromItem(ItemsTC.salisMundus),
                        Ingredient.fromItem(Items.ENDER_EYE)
                )
                .setConsumeComponents(true)
        );

        //Common Eldritch Hostiles
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:eldritch_crab"), new NecromancyRecipe()
                .setSummonedEntity(EntityEldritchCrab.class)
                .setAspects(new AspectList().add(Aspect.ELDRITCH, 10).add(Aspect.BEAST, 10).add(Aspect.TRAP, 10))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_ALIEN))
                .setComponents(
                        Ingredient.fromItem(ItemsTC.voidSeed),
                        Ingredient.fromStacks(new ItemStack(Items.SKULL, 1, 0), new ItemStack(Items.SKULL, 1, 1), new ItemStack(Items.SKULL, 1, 2)),
                        Ingredient.fromStacks(new ItemStack(ItemsTC.crimsonPlateHelm, 1, OreDictionary.WILDCARD_VALUE)),
                        Ingredient.fromItem(Items.ROTTEN_FLESH),
                        Ingredient.fromItem(Items.ENDER_EYE)
                )
        );

        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("thaumcraft:eldritch_crab"), new NecromancyRecipe()
                .setSummonedEntity(EntityEldritchCrab.class)
                .setAspects(new AspectList().add(Aspect.ELDRITCH, 10).add(Aspect.BEAST, 10).add(Aspect.TRAP, 10))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_ALIEN))
                .setComponents(
                        Ingredient.fromItem(ItemsTC.voidSeed),
                        Ingredient.fromStacks(new ItemStack(Items.SKULL, 1, 0), new ItemStack(Items.SKULL, 1, 1), new ItemStack(Items.SKULL, 1, 2)),
                        Ingredient.fromStacks(new ItemStack(ItemsTC.crimsonPlateHelm, 1, OreDictionary.WILDCARD_VALUE)),
                        Ingredient.fromItem(Items.ROTTEN_FLESH),
                        Ingredient.fromItem(Items.ENDER_EYE)
                )
        );

        /* Tainted Mobs */
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("thaumcraft:taint_crawler"), new NecromancyRecipe()
                .setSummonedEntity(EntityTaintCrawler.class)
                .setAspects(new AspectList().add(Aspect.BEAST, 5).add(Aspect.FLUX, 10))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_TAINTED))
                .setComponents(
                        Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 15)),
                        Ingredient.fromItem(Items.SPIDER_EYE),
                        Ingredient.fromStacks(ThaumcraftApiHelper.makeCrystal(Aspect.FLUX)),
                        Ingredient.fromStacks(ThaumcraftApiHelper.makeCrystal(Aspect.FLUX))
                )
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("thaumcraft:taint_swarm"), new NecromancyRecipe()
                .setSummonedEntity(EntityTaintSwarm.class)
                .setAspects(new AspectList().add(Aspect.AIR, 5).add(Aspect.FLIGHT, 5).add(Aspect.FLUX, 15))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_TAINTED))
                .setComponents(
                        Ingredient.fromItem(ItemsTC.salisMundus),
                        Ingredient.fromItem(Items.SPIDER_EYE),
                        Ingredient.fromStacks(ThaumcraftApiHelper.makeCrystal(Aspect.FLUX)),
                        Ingredient.fromStacks(ThaumcraftApiHelper.makeCrystal(Aspect.FLUX))
                )
        );
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("thaumcraft:thaumic_slime"), new NecromancyRecipe()
                .setSummonedEntity(EntityThaumicSlime.class)
                .setAspects(new AspectList().add(Aspect.LIFE, 15).add(Aspect.WATER, 15).add(Aspect.FLUX, 15).add(Aspect.ALCHEMY, 15))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_TAINTED))
                .setComponents(
                        Ingredient.fromItem(ItemsTC.salisMundus),
                        new OreIngredient("slimeball"),
                        Ingredient.fromStacks(ThaumcraftApiHelper.makeCrystal(Aspect.FLUX)),
                        Ingredient.fromStacks(ThaumcraftApiHelper.makeCrystal(Aspect.FLUX))
                )
                .setConsumeComponents(true)
        );

        /* Eldritch Mobs */
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("thaumcraft:eldritch_guardian"), new NecromancyRecipe()
                .setSummonedEntity(EntityEldritchGuardian.class)
                .setAspects(new AspectList().add(Aspect.UNDEAD, 40).add(Aspect.DEATH, 40).add(Aspect.ELDRITCH, 40))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_ELDRITCH))
                .setComponents(
                        Ingredient.fromItem(ItemsTC.salisMundus),
                        Ingredient.fromItem(ItemsTC.voidSeed),
                        Ingredient.fromItem(Items.ENDER_EYE),
                        Ingredient.fromStacks(ThaumcraftApiHelper.makeCrystal(Aspect.ELDRITCH))
                )
                .setConsumeComponents(true)
        );
    }
}
