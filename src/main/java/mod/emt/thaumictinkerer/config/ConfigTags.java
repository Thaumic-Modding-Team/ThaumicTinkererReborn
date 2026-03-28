package mod.emt.thaumictinkerer.config;

import mod.emt.thaumictinkerer.utils.helpers.ConfigItem;
import mod.emt.thaumictinkerer.utils.helpers.LogHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigTags {
    private static final List<ConfigItem> CONSUMING_BLOCKS = new ArrayList<>();

    public static boolean shouldConsumingVoid(ItemStack stack) {
        return CONSUMING_BLOCKS.stream().anyMatch(item -> item.matches(stack));
    }

    public static void syncConfigs() {
        syncConsumingBlocks();
    }

    private static void syncConsumingBlocks() {
        CONSUMING_BLOCKS.clear();
        Pattern pattern = Pattern.compile("^(?:(\\w+)|(.+?:.+?):?(\\d*))$");
        for(String str : ConfigHandlerTT.infusionEnchantments.consuming.voidedBlocks) {
            try {
                Matcher matcher = pattern.matcher(str);
                if(matcher.find()) {
                    if(matcher.group(1) != null && !matcher.group(1).isEmpty()) {
                        CONSUMING_BLOCKS.add(new ConfigItem(matcher.group(1)));
                    } else {
                        ResourceLocation loc = new ResourceLocation(matcher.group(2));
                        Item item = ForgeRegistries.ITEMS.getValue(loc);
                        if(item == null || item == Items.AIR) {
                            LogHelper.error("No valid registered item found for " + loc);
                            throw new IllegalArgumentException();
                        }

                        if(matcher.group(3) != null && !matcher.group(3).isEmpty()) {
                            CONSUMING_BLOCKS.add(new ConfigItem(item, Integer.parseInt(matcher.group(3))));
                        } else {
                            CONSUMING_BLOCKS.add(new ConfigItem(item));
                        }
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                LogHelper.error("Failed to parse consuming configuration: " + str);
            }
            //TODO: Parse config.
        }
    }
}
