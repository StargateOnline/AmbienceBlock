package com.sekai.ambienceblock.blocks;

import com.sekai.ambienceblock.AmbienceMod;
import com.sekai.ambienceblock.client.gui.ambience.AmbienceGUI;
import com.sekai.ambienceblock.tileentity.AmbienceTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class AmbienceBlock extends Block {

    public AmbienceBlock() {
        super(Material.IRON, MapColor.IRON);
        this.setRegistryName(new ResourceLocation(AmbienceMod.MOD_ID, "ambience_block"));
        this.setUnlocalizedName("ambience_block");
        this.setHardness(-1.0F);
        this.setResistance(3600000.0F);
        this.setSoundType(SoundType.METAL);
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new AmbienceTileEntity();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote)
            return true;

        if(Minecraft.getMinecraft().world.getTileEntity(pos) == null)
            return true;

        if(Minecraft.getMinecraft().world.getTileEntity(pos) instanceof AmbienceTileEntity)
            return true;

        Minecraft.getMinecraft().displayGuiScreen(new AmbienceGUI((AmbienceTileEntity)Minecraft.getMinecraft().world.getTileEntity(pos)));
        return true;
    }



}
