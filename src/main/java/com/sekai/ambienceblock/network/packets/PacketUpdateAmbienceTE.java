package com.sekai.ambienceblock.network.packets;

import com.sekai.ambienceblock.AmbienceMod;
import com.sekai.ambienceblock.client.eventhandler.AmbienceEventHandler;
import com.sekai.ambienceblock.tileentity.AmbienceTileEntity;
import com.sekai.ambienceblock.tileentity.AmbienceTileEntityData;
import com.sekai.ambienceblock.network.NetworkHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateAmbienceTE implements IMessage {
    public BlockPos pos;
    public AmbienceTileEntityData data;

    public PacketUpdateAmbienceTE() {}

    public PacketUpdateAmbienceTE(BlockPos pos, AmbienceTileEntityData data) {
        this.pos = pos;
        this.data = data;
    }

    // TODO: check it it works
    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.data = new AmbienceTileEntityData();
        this.data.fromBuff(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong()); // BlockPos
        data.toBuff(buf);
    }

    /* Message/Packet Handler */
    public static class MessageHandler implements IMessageHandler<PacketUpdateAmbienceTE, IMessage>
    {
        @Override
        public IMessage onMessage(PacketUpdateAmbienceTE pkt, MessageContext ctx) {

            // Server to client
            // Execute the action on the main client thread by adding it as a scheduled task
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> {
                if(mc.world == null)
                    return;

                if(!mc.world.isRemote) // if server world
                    return;

                if(!mc.world.isBlockLoaded(pkt.pos))
                    return;

                TileEntity tile = mc.world.getTileEntity(pkt.pos);

                if(tile == null)
                    return;

                if(!(tile instanceof AmbienceTileEntity))
                    return;

                AmbienceTileEntity tileEntity = (AmbienceTileEntity) tile;
                tileEntity.data = pkt.data;
                AmbienceEventHandler.instance.stopFromTile(tileEntity);
            });

            // Client to server
            // This is the player the packet was sent to the server from
            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
            // Execute the action on the main server thread by adding it as a scheduled task
            serverPlayer.getServerWorld().addScheduledTask(() -> {
                if(serverPlayer.getServerWorld() == null)
                    return;

                if(serverPlayer.getServerWorld().isRemote) // if client world
                    return;

                if(!serverPlayer.getServerWorld().isBlockLoaded(pkt.pos))
                    return;

                TileEntity tile = serverPlayer.getServerWorld().getTileEntity(pkt.pos);

                if(tile == null)
                    return;

                if(!(tile instanceof AmbienceTileEntity))
                    return;

                AmbienceTileEntity tileEntity = (AmbienceTileEntity) tile;
                tileEntity.data = pkt.data;

                NetworkHandler.NET.sendTo(new PacketUpdateAmbienceTE(tileEntity.getPos(), tileEntity.data), serverPlayer);
            });
            return null;
        }
    }
}
