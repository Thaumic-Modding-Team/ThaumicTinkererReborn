package mod.emt.thaumictinkerer.compat.datafixes;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
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

public class ItemDataFixer implements IFixableData {
    private static final Map<ResourceLocation, ResourceLocation> ITEM_NAME_MAPPINGS  = new HashMap<>();

    static {
        // Energetic Nitor, Transvector Dislocator and Transvector Interface use the same ids in the original mod
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "black_quartz"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "arcane_quartz"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "black_quartz_block"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "arcane_quartz_block"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "black_quartz_block_chiseled"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "chiseled_arcane_quartz_block"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "black_quartz_block_pillar"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "arcane_quartz_pillar"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "black_quartz_block_slab"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "arcane_quartz_slab"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "black_quartz_block_slab_double"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "arcane_quartz_slab_double"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "black_quartz_block_stairs"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "arcane_quartz_stairs"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "cat_amulet"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "feline_charm"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "connector"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "transvector_binder"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "dissimulation"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "dissimulation_block"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "experience_charm"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "xp_talisman"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "funnel"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "essentia_funnel"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "magnet"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "attractor_item"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "mob_magnet"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "attractor_mob"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "nitor_vapor"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "energetic_nitor"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "repairer"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "thaumic_restorer"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "summoner"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "necromancy_tablet"));

        // These items do not exist in this mod, turn them to something else
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "animation_tablet"), new ResourceLocation("minecraft", "observer"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "block_talisman"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "consuming_sigil"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "blood_sword"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "condor_sword"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "chlorophyte_ore"), new ResourceLocation("minecraft", "iron_ore"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "cleaning_talisman"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "consuming_sigil"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "condensed_mob_aspect"), new ResourceLocation("minecraft", "air"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "dummy_nitor"), new ResourceLocation("minecraft", "air"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "enchantment_pillar"), new ResourceLocation(Thaumcraft.MODID, "pillar_arcane"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "example"), new ResourceLocation("minecraft", "air"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "form_revealer"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "transvector_binder"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "gas_light_item"), new ResourceLocation(Thaumcraft.MODID, "phial"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "gas_remover"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "arcane_quartz"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "gas_shadow_item"), new ResourceLocation(Thaumcraft.MODID, "phial"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "infused_farmland"), new ResourceLocation("minecraft", "farmland"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "infused_grain_block"), new ResourceLocation("minecraft", "air"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "infused_seeds"), new ResourceLocation("minecraft", "seeds"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "infusion_spark"), new ResourceLocation("minecraft", "air"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "light_gas"), new ResourceLocation("minecraft", "air"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "mob_aspect"), new ResourceLocation("minecraft", "air"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "osmotic_enchanter"), new ResourceLocation("minecraft", "enchanting_table"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "shadow_gas"), new ResourceLocation("minecraft", "air"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "share_book"), new ResourceLocation(Thaumcraft.MODID, "thaumonomicon"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "soul_mould"), new ResourceLocation(ThaumicTinkerer.MOD_ID, "escape_sigil"));
        ITEM_NAME_MAPPINGS .put(new ResourceLocation(ThaumicTinkerer.MOD_ID, "spellbinding_cloth"), new ResourceLocation(Thaumcraft.MODID, "fabric"));
    }

    public ItemDataFixer() {
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
    public void missingItemMapping(RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> entry : event.getAllMappings()) {
            ResourceLocation oldName = entry.key;
            ResourceLocation newName = ITEM_NAME_MAPPINGS.get(oldName);
            if (newName != null) {
                Item newItem = ForgeRegistries.ITEMS.getValue(newName);
                if (newItem != null) {
                    entry.remap(newItem);
                }
            }
        }
    }
}
