package mod.emt.thaumictinkerer.registry;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.IAddition;
import mod.emt.thaumictinkerer.api.IProxy;
import mod.emt.thaumictinkerer.api.block.IBlockAddition;
import mod.emt.thaumictinkerer.api.item.IItemAddition;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectRegistryEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = ThaumicTinkerer.MOD_ID)
public class RegistrarTT {
    private static final List<IAddition> ADDITIONS = new ArrayList<>();

    public static void addAdditionToRegister(IAddition addition) {
        if(addition != null && addition.isEnabled()) {
            ADDITIONS.add(addition);
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        getBlockAdditions().forEach(block -> block.registerBlock(registry));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        getItemAdditions().forEach(item -> item.registerItem(registry));
        getAdditions().forEach(IAddition::registerOreDicts);
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        ModRecipesTT.initRecipes(event);
        IForgeRegistry<IRecipe> registry = event.getRegistry();
        getAdditions().forEach(addition -> addition.registerRecipe(registry));
    }

    @SubscribeEvent
    public static void registerEnchants(RegistryEvent.Register<Enchantment> event) {
        //TODO
    }

    @SubscribeEvent
    public static void registerAspects(AspectRegistryEvent event) {
        Map<ItemStack, AspectList> aspectMap = new HashMap<>();
        getAdditions().forEach(addition -> addition.registerAspects(event.register, aspectMap));
        aspectMap.forEach((stack, list) -> {
            if (!stack.isEmpty()) {
                event.register.registerObjectTag(stack, list);
            }
        });
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        getAdditions().forEach(addition -> addition.registerModel(event));
    }

    public static List<IAddition> getAdditions() {
        return ADDITIONS;
    }

    public static List<IBlockAddition> getBlockAdditions() {
        return getAdditions().stream().filter(addition -> addition instanceof IBlockAddition)
                .map(addition -> (IBlockAddition) addition).collect(Collectors.toList());
    }

    public static List<IItemAddition> getItemAdditions() {
        return getAdditions().stream().filter(addition -> addition instanceof IItemAddition)
                .map(addition -> (IItemAddition) addition).collect(Collectors.toList());
    }

    public static List<IProxy> getProxyAdditions() {
        return getAdditions().stream().filter(addition -> addition instanceof IProxy)
                .map(addition -> (IProxy) addition).collect(Collectors.toList());
    }

    static {
        ModBlocksTT.initBlocks();
        ModItemsTT.initItems();
        ModEnchantsTT.initEnchants();
    }
}
