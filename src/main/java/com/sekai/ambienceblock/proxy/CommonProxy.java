package com.sekai.ambienceblock.proxy;

import com.sekai.ambienceblock.AmbienceMod;
import com.sekai.ambienceblock.blocks.AmbienceBlock;
import com.sekai.ambienceblock.tileentity.AmbienceTileEntity;
import com.sekai.ambienceblock.network.NetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;

/**
 * Common proxy
 *
 * This class is responsible for registering items, blocks, entities,
 * capabilities and event listeners on both sides (that's why it's a common
 * proxy).
 */
public class CommonProxy
{
    public static File configFile;

    /**
     * Registers network messages (and their handlers), items, blocks, tile entities
     */
    public void preInit(FMLPreInitializationEvent event)
    {
        /* Configuration */
        configFile = new File(event.getModConfigurationDirectory(), AmbienceMod.NAME);

        /* Block */
        Block ambienceBlock = new AmbienceBlock();
        ForgeRegistries.BLOCKS.register(AmbienceMod.AMBIENCE_BLOCK = ambienceBlock);
        ForgeRegistries.ITEMS.register(AmbienceMod.AMBIENCE_BLOCK_ITEM = new ItemBlock(ambienceBlock).setRegistryName(ambienceBlock.getRegistryName()));

        /* Tile Entity */
        GameRegistry.registerTileEntity(AmbienceTileEntity.class, new ResourceLocation(AmbienceTileEntity.KEY));

        /* Network */
        NetworkHandler.register();
    }

    public void init(FMLInitializationEvent event)
    {

    }

    public void postInit(FMLPostInitializationEvent event)
    { }


}
