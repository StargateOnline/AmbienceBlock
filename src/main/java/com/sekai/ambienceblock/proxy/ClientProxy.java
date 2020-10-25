package com.sekai.ambienceblock.proxy;

import com.sekai.ambienceblock.client.eventhandler.AmbienceEventHandler;
import com.sekai.ambienceblock.client.gui.DebugOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Client proxy
 *
 * This class is responsible for registering item models, block models, entity
 * renders.
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    /**
     * Register mod items, blocks, tile entites and entities, load item,
     * block models and register entity renderer.
     */
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
    }

    /**
     * Subscribe all event listeners to EVENT_BUS and attach any client-side
     * commands to the ClientCommandRegistry.
     */
    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);

        /* Event Handlers */
        MinecraftForge.EVENT_BUS.register(new AmbienceEventHandler());
        MinecraftForge.EVENT_BUS.register(new DebugOverlay());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    { }
}
