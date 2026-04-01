package mod.emt.thaumictinkerer.registry;

import com.google.common.collect.Lists;
import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.recipes.NecromancyRecipe;
import mod.emt.thaumictinkerer.recipes.NecromancyRecipeRegistry;
import mod.emt.thaumictinkerer.utils.helpers.ItemHelper;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreIngredient;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IDustTrigger;
import thaumcraft.api.crafting.Part;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.config.ConfigRecipes;
import thaumcraft.common.lib.crafting.DustTriggerMultiblock;

@SuppressWarnings("ConstantConditions")
public class ModRecipesTT {
    private static final ResourceLocation defaultGroup = new ResourceLocation("");

    public static void initRecipes(RegistryEvent.Register<IRecipe> event) {
        initNecromancyPlatform();
        initArcaneWorkbenchRecipes();
        initCraftingRecipes();
        initCrucibleRecipes();
        initInfusionRecipes();
        initNecromancyRecipes();
    }

    private static void initNecromancyPlatform() {
        //TODO: Config disable.

        Part PEDI = new Part(BlocksTC.pedestalArcane, null);
        Part NECR = new Part(new ItemStack(ModBlocksTT.NECROMANCY_TABLET, 1, 0), new ItemStack(ModBlocksTT.NECROMANCY_TABLET, 1, 1));
        Part QUAR = new Part(ModBlocksTT.ARCANE_QUARTZ_BLOCK, null);
        Part NETH = new Part(Blocks.NETHER_BRICK, null);

        Part[][][] blueprint = new Part[][][] {
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
        //TODO: Swap to this
        // IDustTrigger.registerDustTrigger(new DustTriggerMultiblock("TT_NECROMANCY_TABLET", blueprint));
        IDustTrigger.registerDustTrigger(new DustTriggerMultiblock("BASEAUROMANCY", blueprint));
        ThaumcraftApi.addMultiblockRecipeToCatalog(new ResourceLocation(ThaumicTinkerer.MOD_ID, "necromancy_tablet"), new ThaumcraftApi.BluePrint(
                "BASEAUROMANCY",//TODO: swap to this - "TT_NECROMANCY_TABLET",
                blueprint,
                new ItemStack(Blocks.NETHER_BRICK, 24),
                new ItemStack(ModBlocksTT.ARCANE_QUARTZ_BLOCK, 13),
                new ItemStack(ModBlocksTT.NECROMANCY_TABLET)
        ));
    }

    private static void initArcaneWorkbenchRecipes() {
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "arcane_quartz"), new ShapedArcaneRecipe(
                defaultGroup,
                "BASEAUROMANCY",//TODO: Research,
                2,
                new AspectList(),
                ModItemsTT.ARCANE_QUARTZ,
                "QQQ",
                "QCQ",
                "QQQ",
                'Q', "gemQuartz",
                'C', ItemHelper.getVisCrystalIngredient()
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

    // TODO: Research keys and aspect balance
    private static void initCrucibleRecipes() {
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "AA_PRISMARINE"), new CrucibleRecipe(
                "",
                new ItemStack(Items.PRISMARINE_SHARD),
                "paneGlass",
                new AspectList().add(Aspect.WATER, 5).add(Aspect.EARTH, 5)));

        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "AA_SEA_LANTERN"), new CrucibleRecipe(
                "",
                new ItemStack(Blocks.SEA_LANTERN),
                new ItemStack(Blocks.REDSTONE_LAMP),
                new AspectList().add(Aspect.WATER, 5).add(Aspect.EARTH, 5)));

        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "AA_SPONGE"), new CrucibleRecipe(
                "",
                new ItemStack(Blocks.SPONGE, 1, 0),
                "wool",
                new AspectList().add(Aspect.WATER, 5).add(Aspect.EARTH, 5)));
    }

    private static void initInfusionRecipes() {

    }

    public static void initNecromancyRecipes() {
        //TODO: Config disable.



        //Peaceful Mobs
        //Common
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
                        Ingredient.fromItem(Items.CARROT),
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

        // Companion
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

        // Uncommon
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:polar_bear"), new NecromancyRecipe()
                .setSummonedEntity(EntityPolarBear.class)
                .setAspects(new AspectList().add(Aspect.BEAST, 15).add(Aspect.COLD, 10))
                .setCenterIngredient(Ingredient.fromItem(ModItemsTT.ENTITY_SOUL_PEACEFUL))
                .setComponents(
                        new OreIngredient("leather"),
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

        // Rare
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:mooshroom"), new NecromancyRecipe()
                .setSummonedEntity(EntityMooshroom.class)
                .setAspects(new AspectList().add(Aspect.BEAST, 30).add(Aspect.PLANT, 30).add(Aspect.EARTH, 30))
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
//        ThaumcraftApi.registerEntityTag("Villager", (new AspectList()).add(Aspect.MAN, 15), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("VillagerGolem", (new AspectList()).add(Aspect.METAL, 15).add(Aspect.MAN, 5).add(Aspect.MECHANISM, 5).add(Aspect.MAGIC, 5), new ThaumcraftApi.EntityTagsNBT[0]);



        //Hostile
//        ThaumcraftApi.registerEntityTag("CaveSpider", (new AspectList()).add(Aspect.BEAST, 5).add(Aspect.DEATH, 10).add(Aspect.TRAP, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Creeper", (new AspectList()).add(Aspect.PLANT, 15).add(Aspect.FIRE, 15), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Husk", (new AspectList()).add(Aspect.UNDEAD, 20).add(Aspect.MAN, 10).add(Aspect.FIRE, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Skeleton", (new AspectList()).add(Aspect.UNDEAD, 20).add(Aspect.MAN, 5).add(Aspect.EARTH, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("SkeletonHorse", (new AspectList()).add(Aspect.BEAST, 5).add(Aspect.UNDEAD, 10).add(Aspect.EARTH, 5).add(Aspect.AIR, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Silverfish", (new AspectList()).add(Aspect.BEAST, 5).add(Aspect.EARTH, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Slime", (new AspectList()).add(Aspect.LIFE, 10).add(Aspect.WATER, 10).add(Aspect.ALCHEMY, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Spider", (new AspectList()).add(Aspect.BEAST, 10).add(Aspect.ENTROPY, 10).add(Aspect.TRAP, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Stray", (new AspectList()).add(Aspect.UNDEAD, 20).add(Aspect.MAN, 5).add(Aspect.TRAP, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.BrainyZombie", (new AspectList()).add(Aspect.UNDEAD, 20).add(Aspect.MAN, 10).add(Aspect.MIND, 5).add(Aspect.AVERSION, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Witch", (new AspectList()).add(Aspect.MAN, 15).add(Aspect.MAGIC, 5).add(Aspect.ALCHEMY, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Zombie", (new AspectList()).add(Aspect.UNDEAD, 20).add(Aspect.MAN, 10).add(Aspect.EARTH, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("ZombieHorse", (new AspectList()).add(Aspect.BEAST, 10).add(Aspect.UNDEAD, 5).add(Aspect.EARTH, 5).add(Aspect.AIR, 5), new ThaumcraftApi.EntityTagsNBT[0]);


        //Nether
//        ThaumcraftApi.registerEntityTag("Blaze", (new AspectList()).add(Aspect.ELDRITCH, 5).add(Aspect.FIRE, 15).add(Aspect.FLIGHT, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Ghast", (new AspectList()).add(Aspect.UNDEAD, 15).add(Aspect.FIRE, 15), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("LavaSlime", (new AspectList()).add(Aspect.WATER, 5).add(Aspect.FIRE, 10).add(Aspect.ALCHEMY, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("PigZombie", (new AspectList()).add(Aspect.UNDEAD, 15).add(Aspect.FIRE, 15).add(Aspect.BEAST, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.Firebat", (new AspectList()).add(Aspect.BEAST, 5).add(Aspect.FLIGHT, 5).add(Aspect.FIRE, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("WitherSkeleton", (new AspectList()).add(Aspect.UNDEAD, 25).add(Aspect.MAN, 5).add(Aspect.ENTROPY, 10), new ThaumcraftApi.EntityTagsNBT[0]);


        //Alien
//        ThaumcraftApi.registerEntityTag("Enderman", (new AspectList()).add(Aspect.ELDRITCH, 10).add(Aspect.MOTION, 15).add(Aspect.DESIRE, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Endermite", (new AspectList()).add(Aspect.BEAST, 5).add(Aspect.ELDRITCH, 5).add(Aspect.MOTION, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Shulker", (new AspectList()).add(Aspect.ELDRITCH, 10).add(Aspect.TRAP, 5).add(Aspect.FLIGHT, 5).add(Aspect.PROTECT, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.EldritchCrab", (new AspectList()).add(Aspect.ELDRITCH, 10).add(Aspect.BEAST, 10).add(Aspect.TRAP, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.InhabitedZombie", (new AspectList()).add(Aspect.ELDRITCH, 10).add(Aspect.UNDEAD, 10).add(Aspect.MAN, 5), new ThaumcraftApi.EntityTagsNBT[0]);


        //Arcane
//        ThaumcraftApi.registerEntityTag("IllusionIllager", (new AspectList()).add(Aspect.SENSES, 5).add(Aspect.MAGIC, 5).add(Aspect.MAN, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.Pech", (new AspectList()).add(Aspect.MAN, 10).add(Aspect.AURA, 5).add(Aspect.EXCHANGE, 10).add(Aspect.DESIRE, 5), new ThaumcraftApi.EntityTagsNBT[]{new ThaumcraftApi.EntityTagsNBT("PechType", (byte)0)});
//        ThaumcraftApi.registerEntityTag("Thaumcraft.Pech", (new AspectList()).add(Aspect.MAN, 10).add(Aspect.AURA, 5).add(Aspect.EXCHANGE, 10).add(Aspect.AVERSION, 5), new ThaumcraftApi.EntityTagsNBT[]{new ThaumcraftApi.EntityTagsNBT("PechType", (byte)1)});
//        ThaumcraftApi.registerEntityTag("Thaumcraft.Pech", (new AspectList()).add(Aspect.MAN, 10).add(Aspect.AURA, 5).add(Aspect.EXCHANGE, 10).add(Aspect.MAGIC, 5), new ThaumcraftApi.EntityTagsNBT[]{new ThaumcraftApi.EntityTagsNBT("PechType", (byte)2)});
//        ThaumcraftApi.registerEntityTag("VindicationIllager", (new AspectList()).add(Aspect.AVERSION, 5).add(Aspect.MAGIC, 5).add(Aspect.MAN, 10), new ThaumcraftApi.EntityTagsNBT[0]);


        //Tainted
//        ThaumcraftApi.registerEntityTag("Thaumcraft.Taintacle", (new AspectList()).add(Aspect.FLUX, 15).add(Aspect.BEAST, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.TaintacleTiny", (new AspectList()).add(Aspect.FLUX, 5).add(Aspect.BEAST, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.TaintSeed", (new AspectList()).add(Aspect.FLUX, 20).add(Aspect.AURA, 10).add(Aspect.PLANT, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.TaintSeedPrime", (new AspectList()).add(Aspect.PLANT, 30).add(Aspect.BEAST, 30).add(Aspect.FLUX, 30), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.ThaumSlime", (new AspectList()).add(Aspect.LIFE, 5).add(Aspect.WATER, 5).add(Aspect.FLUX, 5).add(Aspect.ALCHEMY, 5), new ThaumcraftApi.EntityTagsNBT[0]);


        //Eldritch
//        ThaumcraftApi.registerEntityTag("WitherBoss", (new AspectList()).add(Aspect.UNDEAD, 50).add(Aspect.ENTROPY, 25).add(Aspect.FIRE, 25), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("EvocationIllager", (new AspectList()).add(Aspect.ELDRITCH, 5).add(Aspect.MAGIC, 5).add(Aspect.MAN, 10), new ThaumcraftApi.EntityTagsNBT[0]);








//        ThaumcraftApi.registerEntityTag("Giant", (new AspectList()).add(Aspect.UNDEAD, 25).add(Aspect.MAN, 15).add(Aspect.EARTH, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Creeper", (new AspectList()).add(Aspect.PLANT, 15).add(Aspect.FIRE, 15).add(Aspect.ENERGY, 15), new ThaumcraftApi.EntityTagsNBT[]{new ThaumcraftApi.EntityTagsNBT("powered", (byte)1)});
//        ThaumcraftApi.registerEntityTag("Guardian", (new AspectList()).add(Aspect.BEAST, 10).add(Aspect.ELDRITCH, 10).add(Aspect.WATER, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Guardian", (new AspectList()).add(Aspect.BEAST, 10).add(Aspect.ELDRITCH, 15).add(Aspect.WATER, 15), new ThaumcraftApi.EntityTagsNBT[]{new ThaumcraftApi.EntityTagsNBT("Elder", true)});
//        ThaumcraftApi.registerEntityTag("Thaumcraft.GiantBrainyZombie", (new AspectList()).add(Aspect.UNDEAD, 25).add(Aspect.MAN, 15).add(Aspect.MIND, 5).add(Aspect.AVERSION, 10), new ThaumcraftApi.EntityTagsNBT[0]);



//        ThaumcraftApi.registerEntityTag("Thaumcraft.EldritchGuardian", (new AspectList()).add(Aspect.ELDRITCH, 20).add(Aspect.DEATH, 20).add(Aspect.UNDEAD, 20), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.CultistKnight", (new AspectList()).add(Aspect.ELDRITCH, 5).add(Aspect.MAN, 15).add(Aspect.AVERSION, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.CultistCleric", (new AspectList()).add(Aspect.ELDRITCH, 5).add(Aspect.MAN, 15).add(Aspect.AVERSION, 5), new ThaumcraftApi.EntityTagsNBT[0]);

//        ThaumcraftApi.registerEntityTag("Thaumcraft.EldritchWarden", (new AspectList()).add(Aspect.ELDRITCH, 40).add(Aspect.DEATH, 40).add(Aspect.UNDEAD, 40), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.EldritchGolem", (new AspectList()).add(Aspect.ELDRITCH, 40).add(Aspect.ENERGY, 40).add(Aspect.MECHANISM, 40), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.CultistLeader", (new AspectList()).add(Aspect.ELDRITCH, 40).add(Aspect.AVERSION, 40).add(Aspect.MAN, 40), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.TaintacleGiant", (new AspectList()).add(Aspect.ELDRITCH, 40).add(Aspect.BEAST, 40).add(Aspect.FLUX, 40), new ThaumcraftApi.EntityTagsNBT[0]);
//
//        for(Aspect tag : Aspect.aspects.values()) {
//            ThaumcraftApi.registerEntityTag("Thaumcraft.Wisp", (new AspectList()).add(tag, 5).add(Aspect.AURA, 5).add(Aspect.FLIGHT, 5), new ThaumcraftApi.EntityTagsNBT[]{new ThaumcraftApi.EntityTagsNBT("Type", tag.getTag())});
//        }
    }
}
