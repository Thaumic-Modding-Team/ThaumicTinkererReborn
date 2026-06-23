package mod.emt.thaumictinkerer.config;

import mod.emt.thaumictinkerer.utils.ConfigItem;
import mod.emt.thaumictinkerer.utils.helpers.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigTags {
    private static final List<ConfigItem> CONSUMING_WHITELIST = new ArrayList<>();
    private static final Set<Block> DISLOCATOR_BLACKLIST = new HashSet<>();
    private static final Map<EntityEntry, Double> ENTITY_JEI_SCALES = new HashMap<>();

    public static boolean shouldConsumingVoidDrop(ItemStack stack) {
        return CONSUMING_WHITELIST.stream().anyMatch(item -> item.matches(stack));
    }

    public static boolean canDislocatorSwap(World world, BlockPos pos, IBlockState state) {
        return state.getBlockHardness(world, pos) > -1 && !DISLOCATOR_BLACKLIST.contains(state.getBlock());
    }

    public static double getJeiEntityRenderScale(EntityEntry entityEntry) {
        return ENTITY_JEI_SCALES.getOrDefault(entityEntry, 1.0);
    }

    public static void syncConfigs() {
        syncConsumingWhitelist();
        syncDislocatorBlacklist();
        syncEntityJeiScales();
    }

    private static void syncConsumingWhitelist() {
        CONSUMING_WHITELIST.clear();
        Pattern pattern = Pattern.compile("^(?:(\\w+)|(.+?:.+?):?(\\d*))$");
        for(String str : ConfigHandlerTT.consumingSigil.voidedMaterials) {
            try {
                Matcher matcher = pattern.matcher(str);
                if(matcher.find()) {
                    if(matcher.group(1) != null && !matcher.group(1).isEmpty()) {
                        CONSUMING_WHITELIST.add(new ConfigItem(matcher.group(1)));
                    } else {
                        ResourceLocation loc = new ResourceLocation(matcher.group(2));
                        Item item = ForgeRegistries.ITEMS.getValue(loc);
                        if(item == null || item == Items.AIR) {
                            LogHelper.error("No valid registered item found for " + loc);
                            throw new IllegalArgumentException();
                        }

                        if(matcher.group(3) != null && !matcher.group(3).isEmpty()) {
                            CONSUMING_WHITELIST.add(new ConfigItem(item, Integer.parseInt(matcher.group(3))));
                        } else {
                            CONSUMING_WHITELIST.add(new ConfigItem(item));
                        }
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                LogHelper.error("Failed to parse consuming configuration: " + str);
            }
        }
    }

    private static void syncDislocatorBlacklist() {
        DISLOCATOR_BLACKLIST.clear();
        DISLOCATOR_BLACKLIST.add(Blocks.BARRIER);
        DISLOCATOR_BLACKLIST.add(Blocks.BED);
        DISLOCATOR_BLACKLIST.add(Blocks.BEDROCK);
        DISLOCATOR_BLACKLIST.add(Blocks.CHAIN_COMMAND_BLOCK);
        DISLOCATOR_BLACKLIST.add(Blocks.COMMAND_BLOCK);
        DISLOCATOR_BLACKLIST.add(Blocks.END_GATEWAY);
        DISLOCATOR_BLACKLIST.add(Blocks.END_PORTAL);
        DISLOCATOR_BLACKLIST.add(Blocks.END_PORTAL_FRAME);
        DISLOCATOR_BLACKLIST.add(Blocks.PISTON_EXTENSION);
        DISLOCATOR_BLACKLIST.add(Blocks.PORTAL);
        DISLOCATOR_BLACKLIST.add(Blocks.REPEATING_COMMAND_BLOCK);
        DISLOCATOR_BLACKLIST.add(Blocks.STRUCTURE_BLOCK);
        DISLOCATOR_BLACKLIST.add(Blocks.STRUCTURE_VOID);

        for(String str : ConfigHandlerTT.transvectorDislocator.blockBlacklist) {
            ResourceLocation loc = new ResourceLocation(str);
            Block block = ForgeRegistries.BLOCKS.getValue(loc);
            if(block != null && block != Blocks.AIR) {
                DISLOCATOR_BLACKLIST.add(block);
            } else {
                LogHelper.error("No registered block found for " + str);
            }
        }
    }

    private static void syncEntityJeiScales() {
        ENTITY_JEI_SCALES.clear();
        Pattern pattern = Pattern.compile("^(.+?)=(\\d*.?\\d*)$");
        for(String str : ConfigHandlerTT.necromancyTablet.scaleOverrides) {
            try {
                Matcher matcher = pattern.matcher(str);
                if(matcher.find()) {
                    ResourceLocation loc = new ResourceLocation(matcher.group(1));
                    double scale = Double.parseDouble(matcher.group(2));
                    EntityEntry entry = ForgeRegistries.ENTITIES.getValue(loc);
                    if(entry != null) {
                        ENTITY_JEI_SCALES.put(entry, scale);
                    } else {
                        throw new IllegalArgumentException("No entity found for " + str);
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                LogHelper.error("failed to parse necromancy tablet scale override: " + str);
            }
        }
    }
}
