package mod.emt.thaumictinkerer.config;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(
        modid = ThaumicTinkerer.MOD_ID,
        name = ThaumicTinkerer.MOD_ID + "/" + ThaumicTinkerer.MOD_ID
)
public class ConfigHandlerTT {
    @Config.Name("Sigil of Consuming")
    public static ConsumingSigilConfig consumingSigil = new ConsumingSigilConfig();
    @Config.Name("Infusion Enchantments")
    public static InfusionEnchantConfig infusionEnchantments = new InfusionEnchantConfig();
    @Config.Name("Tablet of Necromancy")
    public static NecromancyTabletConfig necromancyTablet = new NecromancyTabletConfig();
    @Config.Name("Thaumic Restorer")
    public static ThaumicRestorerConfig thaumicRestorer = new ThaumicRestorerConfig();
    @Config.Name("Transvector Dislocator")
    public static TransvectorDislocatorConfig transvectorDislocator = new TransvectorDislocatorConfig();
    @Config.Name("Transvector Interface")
    public static TransvectorInterfaceConfig transvectorInterface = new TransvectorInterfaceConfig();

    public static class ConsumingSigilConfig {
        @Config.Name("Voided Materials")
        @Config.Comment("Materials that will be destroyed when harvested while this sigil is active.")
        public String[] voidedMaterials = new String[] {
                "minecraft:dirt:1",
                "minecraft:dirt:2",
                "minecraft:magma",
                "minecraft:mossy_cobblestone",
                "minecraft:mycelium",
                "minecraft:snow",
                "minecraft:snowball",
                "minecraft:soul_sand",
                "cobblestone",
                "dirt",
                "endstone",
                "grass",
                "gravel",
                "netherrack",
                "sand",
                "sandstone",
                "stone",
                "stoneAndesite",
                "stoneDiorite",
                "stoneGranite"
        };
    }

    public static class InfusionEnchantConfig {
        @Config.Name("Educational Infusion Enchant")
        public EducationalEnchantConfig educational = new EducationalEnchantConfig();

        @Config.Name("Wrath Infusion Enchant")
        public WrathEnchantConfig wrath = new WrathEnchantConfig();

        public static class EducationalEnchantConfig {
            @Config.RequiresMcRestart
            public boolean enable = true;

            @Config.RangeInt(min = 0, max = 100000)
            public int maxXpPerLevel = 10;

            @Config.RangeDouble(min = 0.01, max = 100.0)
            public double xpGainPerLevel = 0.25;
        }

        public static class WrathEnchantConfig {
            @Config.RequiresMcRestart
            public boolean enable = true;
        }
    }

    public static class NecromancyTabletConfig {
        @Config.Name("Enable Tablet of Necromancy")
        @Config.Comment("Enables the Tablet of Necromancy, used to summon creatures using reagents.")
        public boolean enable = true;

        @Config.Name("Entity Scale Overrides")
        @Config.Comment({
                "Specific scale overrides for entity displays in the Tablet of Necromancy JEI display.",
                "  Format: modid:entityid=scale",
                "  Example: minecraft:creeper=0.5"
        })
        public String[] scaleOverrides = new String[] {};
    }

    public static class ThaumicRestorerConfig {
        @Config.RequiresMcRestart
        @Config.Name("Enable Thaumic Restorer")
        @Config.Comment("Enables the Thaumic Restorer, a block that uses essentia to repair items.")
        public boolean enable = true;

        @Config.Name("Dynamic Aspects")
        @Config.Comment({
                "Enables item repair using aspects based on the item type.",
                "  Armor = Praemunio (Protect)",
                "  Weapons = Aversio (Aversion)",
                "  Tools = Instrumentum (Tool)",
                "  Other = Fabrico (Craft)",
                "If set to false, all tools will be repaired with Fabrico (Craft)."
        })
        public boolean dynamicAspects = true;
    }

    public static class TransvectorDislocatorConfig {
        @Config.Name("Block Blacklist")
        public String[] blockBlacklist = new String[] {
                "minecraft:mob_spawner"
        };

        @Config.Name("Transfer Blocks")
        @Config.Comment("Allows the dislocator to transfer blocks.")
        public boolean transferBlocks = true;

        @Config.Name("Transfer Entities")
        @Config.Comment("Allows the dislocator to transfer entities.")
        public boolean transferEntities = true;

        @Config.Name("Transfer Entities - Bosses")
        @Config.Comment("Allows the dislocator to transfer bosses. This setting requires 'Transfer Entities' enabled.")
        public boolean transferBosses = false;

        @Config.Name("Transfer Entities - Players")
        @Config.Comment("Allows the dislocator to transfer players. This setting requires 'Transfer Entities' enabled.")
        public boolean transferPlayers = false;
    }

    public static class TransvectorInterfaceConfig {
        @Config.RequiresMcRestart
        @Config.Name("Enable Transvector Interface")
        @Config.Comment("Enables the Transvector Interface, used to duplicate block face capabilities over a short distance.")
        public boolean enable = true;

        @Config.RangeInt(min = 1, max = 32)
        @Config.Name("Maximum Range")
        @Config.Comment("The maximum distance the Transvector Interface can be placed from the linked block.")
        public int maxRange = 8;
    }

    @Mod.EventBusSubscriber(modid = ThaumicTinkerer.MOD_ID)
    public static class ConfigChangeListener {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.getModID().equals(ThaumicTinkerer.MOD_ID)) {
                ConfigManager.sync(ThaumicTinkerer.MOD_ID, Config.Type.INSTANCE);
                ConfigTags.syncConfigs();
            }
        }
    }
}
