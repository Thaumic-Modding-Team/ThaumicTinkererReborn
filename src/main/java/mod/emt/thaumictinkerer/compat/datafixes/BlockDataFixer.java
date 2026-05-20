package mod.emt.thaumictinkerer.compat.datafixes;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IFixableData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import thaumcraft.Thaumcraft;

import java.util.HashMap;
import java.util.Map;

public class BlockDataFixer implements IFixableData {
    private static final Map<ResourceLocation, ResourceLocation> BLOCK_NAME_MAPPINGS = new HashMap<>();

    static {
        // Transvector Dislocator and Transvector Interface use the same ids in the original mod
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "black_quartz_block"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "arcane_quartz_block"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "black_quartz_block_chiseled"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "chiseled_arcane_quartz_block"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "black_quartz_block_pillar"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "arcane_quartz_pillar"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "black_quartz_block_slab"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "arcane_quartz_slab"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "black_quartz_block_stairs"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "arcane_quartz_stairs"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "dissimulation"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "dissimulation_block"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "funnel"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "essentia_funnel"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "magnet"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "attractor_item"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "mob_magnet"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "attractor_mob"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "nitor_vapor"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "energetic_nitor"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "repairer"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "thaumic_restorer"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "summoner"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "necromancy_tablet"));

        // These blocks do not exist in this mod, turn them to something else
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "animation_tablet"), new ResourceLocation("minecraft", "observer"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "chlorophyte_ore"), new ResourceLocation("minecraft", "iron_ore"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "enchantment_pillar"), new ResourceLocation(Thaumcraft.MODID, "pillar_arcane"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "dummy_nitor"), new ResourceLocation("minecraft", "air"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "example"), new ResourceLocation("minecraft", "air"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "infused_farmland"), new ResourceLocation("minecraft", "farmland"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "infused_grain_block"), new ResourceLocation("minecraft", "air"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "infusion_spark"), new ResourceLocation("minecraft", "air"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "light_gas"), new ResourceLocation("minecraft", "air"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "osmotic_enchanter"), new ResourceLocation("minecraft", "enchanting_table"));
        BLOCK_NAME_MAPPINGS.put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "shadow_gas"), new ResourceLocation("minecraft", "air"));
    }

    public BlockDataFixer() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public int getFixVersion() {
        return 1;
    }

    @Override
    public @NotNull NBTTagCompound fixTagCompound(@NotNull NBTTagCompound compound) {
        return compound;
    }

    @SubscribeEvent
    public void missingBlockMapping(RegistryEvent.MissingMappings<Block> event) {
        for (RegistryEvent.MissingMappings.Mapping<Block> entry : event.getAllMappings()) {
            ResourceLocation oldName = entry.key;
            ResourceLocation newName = BLOCK_NAME_MAPPINGS.get(oldName);
            if (newName != null) {
                Block newBlock = ForgeRegistries.BLOCKS.getValue(newName);
                if (newBlock != null) {
                    entry.remap(newBlock);
                }
            }
        }
    }

    @SubscribeEvent
    public void missingItemBlockMapping(RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> entry : event.getAllMappings()) {
            ResourceLocation oldName = entry.key;
            ResourceLocation newName = BLOCK_NAME_MAPPINGS.get(oldName);
            if (newName != null) {
                Item newItem = ForgeRegistries.ITEMS.getValue(newName);
                if (newItem != null) {
                    entry.remap(newItem);
                }
            }
        }
    }
}
