package com.sekai.ambienceblock;

import com.sekai.ambienceblock.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = AmbienceMod.MOD_ID, name = AmbienceMod.NAME, version = AmbienceMod.VERSION, dependencies="required-after:Forge@[14.23.5.2768,)")
public class AmbienceMod
{
    /* Mod Info */
    public static final String MOD_ID = "ambienceblock";
    public static final String NAME = "AmbienceBlock";
    public static final String VERSION = "%VERSION%";

    /* Proxies */
    public static final String CLIENT_PROXY = AmbienceMod.MOD_ID + ".ClientProxy";
    public static final String SERVER_PROXY = AmbienceMod.MOD_ID + ".CommonProxy";

    // Directly reference a log4j logger.
    public static Logger logger;

    /* Items */
    public static Item AMBIENCE_BLOCK_ITEM;

    /* Blocks */
    public static Block AMBIENCE_BLOCK;

    /* Creative Tab */
    public static CreativeTabs AMBIENCE_TAB;

    /* Forge */
    @Mod.Instance(value=AmbienceMod.MOD_ID)
    public static AmbienceMod instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        proxy.preInit(event);

        // Creative Tab
        AMBIENCE_TAB = new CreativeTabs(AmbienceMod.MOD_ID) {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(AMBIENCE_BLOCK);
            }
        };
        AMBIENCE_BLOCK.setCreativeTab(AMBIENCE_TAB);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void onStartServer(FMLServerStartingEvent event)
    {

    }
}
