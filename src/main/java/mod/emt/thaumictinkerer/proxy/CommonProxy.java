package mod.emt.thaumictinkerer.proxy;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.IAddition;
import mod.emt.thaumictinkerer.api.IProxy;
import mod.emt.thaumictinkerer.compat.crafttweaker.CTPlugin;
import mod.emt.thaumictinkerer.compat.datafixes.BlockDataFixer;
import mod.emt.thaumictinkerer.compat.datafixes.ItemDataFixer;
import mod.emt.thaumictinkerer.network.PacketHandler;
import mod.emt.thaumictinkerer.registry.RegistrarTT;
import mod.emt.thaumictinkerer.utils.helpers.CompatHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.FixTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.common.FMLCommonHandler;
import thaumcraft.Thaumcraft;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;

public class CommonProxy {
    public void preInit() {
        PacketHandler.init();
        RegistrarTT.getProxyAdditions().forEach(IProxy::preInit);
        if(CompatHelper.isModTweakerLoaded) {
            MinecraftForge.EVENT_BUS.register(new CTPlugin());
        }
    }

    public void init() {
        RegistrarTT.getProxyAdditions().forEach(IProxy::init);
        registerResearch();

        ModFixs modFixer = FMLCommonHandler.instance().getDataFixer().init(ThaumicTinkerer.MOD_ID, 1);
        modFixer.registerFix(FixTypes.BLOCK_ENTITY, new BlockDataFixer());
        modFixer.registerFix(FixTypes.ITEM_INSTANCE, new ItemDataFixer());
    }

    public void postInit() {
        RegistrarTT.getProxyAdditions().forEach(IProxy::postInit);
    }

    private void registerResearch() {
        ResearchCategories.registerCategory(
                "THAUMIC_TINKERER", "FIRSTSTEPS", new AspectList(),
                new ResourceLocation(ThaumicTinkerer.MOD_ID, "textures/research/r_thaumic_tinkering.png"),
                new ResourceLocation(ThaumicTinkerer.MOD_ID, "textures/gui/research_background.png"),
                new ResourceLocation(Thaumcraft.MODID, "textures/gui/gui_research_back_over.png"));

        ThaumcraftApi.registerResearchLocation(new ResourceLocation(ThaumicTinkerer.MOD_ID, "research/basics"));
        ThaumcraftApi.registerResearchLocation(new ResourceLocation(ThaumicTinkerer.MOD_ID, "research/devices"));
        ThaumcraftApi.registerResearchLocation(new ResourceLocation(ThaumicTinkerer.MOD_ID, "research/equipment"));
        ThaumcraftApi.registerResearchLocation(new ResourceLocation(ThaumicTinkerer.MOD_ID, "research/misc"));

        RegistrarTT.getAdditions().forEach(IAddition::registerResearchLocation);
    }
}
