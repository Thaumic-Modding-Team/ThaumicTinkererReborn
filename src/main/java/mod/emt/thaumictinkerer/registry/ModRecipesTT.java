package mod.emt.thaumictinkerer.registry;

import com.google.common.collect.Lists;
import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.recipes.NecromancyRecipe;
import mod.emt.thaumictinkerer.recipes.NecromancyRecipeRegistry;
import mod.emt.thaumictinkerer.utils.helpers.ItemHelper;
import net.minecraft.entity.monster.*;
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
import net.minecraftforge.oredict.OreDictionary;
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
import thaumcraft.common.entities.monster.*;
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
                        Ingredient.fromItem(Items.CARROT),
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
        //TODO: Make this give the best attributes
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
        //TODO: Make this give the best attributes
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

        // TODO: Maybe only make it spawn small slimes?
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

        //Constructs
        //TODO: Add iron golem custom recipe?
        //ThaumcraftApi.registerEntityTag("VillagerGolem", (new AspectList()).add(Aspect.METAL, 15).add(Aspect.MAN, 5).add(Aspect.MECHANISM, 5).add(Aspect.MAGIC, 5), new ThaumcraftApi.EntityTagsNBT[0]);

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

        //Tainted
//        ThaumcraftApi.registerEntityTag("Thaumcraft.TaintSeed", (new AspectList()).add(Aspect.FLUX, 20).add(Aspect.AURA, 10).add(Aspect.PLANT, 5), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.TaintSeedPrime", (new AspectList()).add(Aspect.PLANT, 30).add(Aspect.BEAST, 30).add(Aspect.FLUX, 30), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Thaumcraft.ThaumSlime", (new AspectList()).add(Aspect.LIFE, 5).add(Aspect.WATER, 5).add(Aspect.FLUX, 5).add(Aspect.ALCHEMY, 5), new ThaumcraftApi.EntityTagsNBT[0]);

        //Eldritch
//        ThaumcraftApi.registerEntityTag("Guardian", (new AspectList()).add(Aspect.BEAST, 10).add(Aspect.ELDRITCH, 10).add(Aspect.WATER, 10), new ThaumcraftApi.EntityTagsNBT[0]);
//        ThaumcraftApi.registerEntityTag("Guardian", (new AspectList()).add(Aspect.BEAST, 10).add(Aspect.ELDRITCH, 15).add(Aspect.WATER, 15), new ThaumcraftApi.EntityTagsNBT[]{new ThaumcraftApi.EntityTagsNBT("Elder", true)});
    }
}
