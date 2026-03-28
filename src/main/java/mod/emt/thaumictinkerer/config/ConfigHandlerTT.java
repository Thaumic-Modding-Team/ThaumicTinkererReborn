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
    @Config.Name("Infusion Enchantments")
    public static InfusionEnchantConfig infusionEnchantments = new InfusionEnchantConfig();
    @Config.Name("Thaumic Restorer")
    public static ThaumicRestorerConfig thaumicRestorer = new ThaumicRestorerConfig();


    public static class InfusionEnchantConfig {
        @Config.Name("Consuming Infusion Enchant")
        public ConsumingEnchantConfig consuming = new ConsumingEnchantConfig();

        @Config.Name("Educational Infusion Enchant")
        public EducationalEnchantConfig educational = new EducationalEnchantConfig();

        @Config.Name("Wrath Infusion Enchant")
        public WrathEnchantConfig wrath = new WrathEnchantConfig();

        public static class ConsumingEnchantConfig {
            @Config.RequiresMcRestart
            public boolean enable = true;

            public String[] voidedBlocks = new String[] {};
        }

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
