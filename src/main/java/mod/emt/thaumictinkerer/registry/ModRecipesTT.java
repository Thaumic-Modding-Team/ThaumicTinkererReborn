package mod.emt.thaumictinkerer.registry;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.recipes.NecromaticRecipe;
import mod.emt.thaumictinkerer.recipes.NecromancyRecipeRegistry;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IDustTrigger;
import thaumcraft.api.crafting.Part;
import thaumcraft.common.lib.crafting.DustTriggerMultiblock;

@SuppressWarnings("ConstantConditions")
public class ModRecipesTT {

    public static void initRecipes(RegistryEvent.Register<IRecipe> event) {
        initNecromancyPlatform();
        initArcaneWorkbenchRecipes();
        initCraftingRecipes();
        initCrucibleRecipes();
        initInfusionRecipes();
        initNecromancyRecipes();
    }

    private static void initNecromancyPlatform() {
        Part PEDI = new Part(BlocksTC.pedestalArcane, null);
        Part NECR = new Part(ModBlocksTT.NECROMANCY_TABLET, new ItemStack(ModBlocksTT.NECROMANCY_TABLET, 1, 1));
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
//        ThaumcraftApi.registerEntityTag("Zombie", (new AspectList()).add(Aspect.UNDEAD, 20).add(Aspect.MAN, 10).add(Aspect.EARTH, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Husk", (new AspectList()).add(Aspect.UNDEAD, 20).add(Aspect.MAN, 10).add(Aspect.FIRE, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Giant", (new AspectList()).add(Aspect.UNDEAD, 25).add(Aspect.MAN, 15).add(Aspect.EARTH, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Skeleton", (new AspectList()).add(Aspect.UNDEAD, 20).add(Aspect.MAN, 5).add(Aspect.EARTH, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("WitherSkeleton", (new AspectList()).add(Aspect.UNDEAD, 25).add(Aspect.MAN, 5).add(Aspect.ENTROPY, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Creeper", (new AspectList()).add(Aspect.PLANT, 15).add(Aspect.FIRE, 15), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Creeper", (new AspectList()).add(Aspect.PLANT, 15).add(Aspect.FIRE, 15).add(Aspect.ENERGY, 15), new ThaumcraftApi.EntityTagsNBT[]{new ThaumcraftApi.EntityTagsNBT("powered", (byte)1)});
//        ThaumcraftApi.registerEntityTag("Horse", (new AspectList()).add(Aspect.BEAST, 15).add(Aspect.EARTH, 5).add(Aspect.AIR, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Donkey", (new AspectList()).add(Aspect.BEAST, 15).add(Aspect.EARTH, 5).add(Aspect.AIR, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Mule", (new AspectList()).add(Aspect.BEAST, 15).add(Aspect.EARTH, 5).add(Aspect.AIR, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("SkeletonHorse", (new AspectList()).add(Aspect.BEAST, 5).add(Aspect.UNDEAD, 10).add(Aspect.EARTH, 5).add(Aspect.AIR, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("ZombieHorse", (new AspectList()).add(Aspect.BEAST, 10).add(Aspect.UNDEAD, 5).add(Aspect.EARTH, 5).add(Aspect.AIR, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Pig", (new AspectList()).add(Aspect.BEAST, 10).add(Aspect.EARTH, 10).add(Aspect.DESIRE, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Sheep", (new AspectList()).add(Aspect.BEAST, 10).add(Aspect.EARTH, 10), new ThaumcraftApi.EntityTagsNBT[0]);
        NecromancyRecipeRegistry.addRecipe(new ResourceLocation("minecraft:cow"), new NecromaticRecipe(
                EntityCow.class,
                new AspectList().add(Aspect.BEAST, 15).add(Aspect.EARTH, 15),
                Ingredient.fromItem(Items.ROTTEN_FLESH),
                Ingredient.fromItem(Items.MILK_BUCKET),
                Ingredient.fromItem(Items.LEATHER),
                Ingredient.fromItem(Items.BEEF),
                Ingredient.fromItem(Items.BONE)
        ));
//        ThaumcraftApi.registerEntityTag("Cow", (new AspectList()).add(Aspect.BEAST, 15).add(Aspect.EARTH, 15), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("MushroomCow", (new AspectList()).add(Aspect.BEAST, 15).add(Aspect.PLANT, 15).add(Aspect.EARTH, 15), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("SnowMan", (new AspectList()).add(Aspect.COLD, 10).add(Aspect.MAN, 5).add(Aspect.MECHANISM, 5).add(Aspect.MAGIC, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Ozelot", (new AspectList()).add(Aspect.BEAST, 10).add(Aspect.ENTROPY, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Chicken", (new AspectList()).add(Aspect.BEAST, 5).add(Aspect.FLIGHT, 5).add(Aspect.AIR, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Squid", (new AspectList()).add(Aspect.BEAST, 5).add(Aspect.WATER, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Wolf", (new AspectList()).add(Aspect.BEAST, 15).add(Aspect.EARTH, 10).add(Aspect.AVERSION, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Bat", (new AspectList()).add(Aspect.BEAST, 5).add(Aspect.FLIGHT, 5).add(Aspect.DARKNESS, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Spider", (new AspectList()).add(Aspect.BEAST, 10).add(Aspect.ENTROPY, 10).add(Aspect.TRAP, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Slime", (new AspectList()).add(Aspect.LIFE, 10).add(Aspect.WATER, 10).add(Aspect.ALCHEMY, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Ghast", (new AspectList()).add(Aspect.UNDEAD, 15).add(Aspect.FIRE, 15), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("PigZombie", (new AspectList()).add(Aspect.UNDEAD, 15).add(Aspect.FIRE, 15).add(Aspect.BEAST, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Enderman", (new AspectList()).add(Aspect.ELDRITCH, 10).add(Aspect.MOTION, 15).add(Aspect.DESIRE, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("CaveSpider", (new AspectList()).add(Aspect.BEAST, 5).add(Aspect.DEATH, 10).add(Aspect.TRAP, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Silverfish", (new AspectList()).add(Aspect.BEAST, 5).add(Aspect.EARTH, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Blaze", (new AspectList()).add(Aspect.ELDRITCH, 5).add(Aspect.FIRE, 15).add(Aspect.FLIGHT, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("LavaSlime", (new AspectList()).add(Aspect.WATER, 5).add(Aspect.FIRE, 10).add(Aspect.ALCHEMY, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("WitherBoss", (new AspectList()).add(Aspect.UNDEAD, 50).add(Aspect.ENTROPY, 25).add(Aspect.FIRE, 25), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Witch", (new AspectList()).add(Aspect.MAN, 15).add(Aspect.MAGIC, 5).add(Aspect.ALCHEMY, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Villager", (new AspectList()).add(Aspect.MAN, 15), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("VillagerGolem", (new AspectList()).add(Aspect.METAL, 15).add(Aspect.MAN, 5).add(Aspect.MECHANISM, 5).add(Aspect.MAGIC, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Guardian", (new AspectList()).add(Aspect.BEAST, 10).add(Aspect.ELDRITCH, 10).add(Aspect.WATER, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Guardian", (new AspectList()).add(Aspect.BEAST, 10).add(Aspect.ELDRITCH, 15).add(Aspect.WATER, 15), new ThaumcraftApi.EntityTagsNBT[]{new ThaumcraftApi.EntityTagsNBT("Elder", true)});
//        ThaumcraftApi.registerEntityTag("Rabbit", (new AspectList()).add(Aspect.BEAST, 5).add(Aspect.EARTH, 5).add(Aspect.MOTION, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Endermite", (new AspectList()).add(Aspect.BEAST, 5).add(Aspect.ELDRITCH, 5).add(Aspect.MOTION, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("PolarBear", (new AspectList()).add(Aspect.BEAST, 15).add(Aspect.COLD, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Shulker", (new AspectList()).add(Aspect.ELDRITCH, 10).add(Aspect.TRAP, 5).add(Aspect.FLIGHT, 5).add(Aspect.PROTECT, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("EvocationIllager", (new AspectList()).add(Aspect.ELDRITCH, 5).add(Aspect.MAGIC, 5).add(Aspect.MAN, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("VindicationIllager", (new AspectList()).add(Aspect.AVERSION, 5).add(Aspect.MAGIC, 5).add(Aspect.MAN, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("IllusionIllager", (new AspectList()).add(Aspect.SENSES, 5).add(Aspect.MAGIC, 5).add(Aspect.MAN, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Llama", (new AspectList()).add(Aspect.BEAST, 15).add(Aspect.WATER, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Parrot", (new AspectList()).add(Aspect.BEAST, 5).add(Aspect.FLIGHT, 5).add(Aspect.SENSES, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Stray", (new AspectList()).add(Aspect.UNDEAD, 20).add(Aspect.MAN, 5).add(Aspect.TRAP, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Vex", (new AspectList()).add(Aspect.ELDRITCH, 5).add(Aspect.FLIGHT, 5).add(Aspect.MAGIC, 5).add(Aspect.MAN, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Stray", (new AspectList()).add(Aspect.UNDEAD, 20).add(Aspect.MAN, 5).add(Aspect.TRAP, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.Firebat", (new AspectList()).add(Aspect.BEAST, 5).add(Aspect.FLIGHT, 5).add(Aspect.FIRE, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.Pech", (new AspectList()).add(Aspect.MAN, 10).add(Aspect.AURA, 5).add(Aspect.EXCHANGE, 10).add(Aspect.DESIRE, 5), new ThaumcraftApi.EntityTagsNBT[]{new ThaumcraftApi.EntityTagsNBT("PechType", (byte)0)});
//        ThaumcraftApi.registerEntityTag("Thaumcraft.Pech", (new AspectList()).add(Aspect.MAN, 10).add(Aspect.AURA, 5).add(Aspect.EXCHANGE, 10).add(Aspect.AVERSION, 5), new ThaumcraftApi.EntityTagsNBT[]{new ThaumcraftApi.EntityTagsNBT("PechType", (byte)1)});
//        ThaumcraftApi.registerEntityTag("Thaumcraft.Pech", (new AspectList()).add(Aspect.MAN, 10).add(Aspect.AURA, 5).add(Aspect.EXCHANGE, 10).add(Aspect.MAGIC, 5), new ThaumcraftApi.EntityTagsNBT[]{new ThaumcraftApi.EntityTagsNBT("PechType", (byte)2)});
//        ThaumcraftApi.registerEntityTag("Thaumcraft.ThaumSlime", (new AspectList()).add(Aspect.LIFE, 5).add(Aspect.WATER, 5).add(Aspect.FLUX, 5).add(Aspect.ALCHEMY, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.BrainyZombie", (new AspectList()).add(Aspect.UNDEAD, 20).add(Aspect.MAN, 10).add(Aspect.MIND, 5).add(Aspect.AVERSION, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.GiantBrainyZombie", (new AspectList()).add(Aspect.UNDEAD, 25).add(Aspect.MAN, 15).add(Aspect.MIND, 5).add(Aspect.AVERSION, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.Taintacle", (new AspectList()).add(Aspect.FLUX, 15).add(Aspect.BEAST, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.TaintSeed", (new AspectList()).add(Aspect.FLUX, 20).add(Aspect.AURA, 10).add(Aspect.PLANT, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.TaintSeedPrime", (new AspectList()).add(Aspect.FLUX, 25).add(Aspect.AURA, 15).add(Aspect.PLANT, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.TaintacleTiny", (new AspectList()).add(Aspect.FLUX, 5).add(Aspect.BEAST, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.TaintSwarm", (new AspectList()).add(Aspect.FLUX, 15).add(Aspect.AIR, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.MindSpider", (new AspectList()).add(Aspect.FLUX, 5).add(Aspect.FIRE, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.EldritchGuardian", (new AspectList()).add(Aspect.ELDRITCH, 20).add(Aspect.DEATH, 20).add(Aspect.UNDEAD, 20), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.CultistKnight", (new AspectList()).add(Aspect.ELDRITCH, 5).add(Aspect.MAN, 15).add(Aspect.AVERSION, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.CultistCleric", (new AspectList()).add(Aspect.ELDRITCH, 5).add(Aspect.MAN, 15).add(Aspect.AVERSION, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.EldritchCrab", (new AspectList()).add(Aspect.ELDRITCH, 10).add(Aspect.BEAST, 10).add(Aspect.TRAP, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.InhabitedZombie", (new AspectList()).add(Aspect.ELDRITCH, 10).add(Aspect.UNDEAD, 10).add(Aspect.MAN, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.TaintSeed", (new AspectList()).add(Aspect.PLANT, 20).add(Aspect.BEAST, 20).add(Aspect.FLUX, 20), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.TaintSeedPrime", (new AspectList()).add(Aspect.PLANT, 30).add(Aspect.BEAST, 30).add(Aspect.FLUX, 30), new ThaumcraftApi.EntityTagsNBT[0]);
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
