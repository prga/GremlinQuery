/*
 * Copyright 2011 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.example.qotm; 

import io.netty.channel.ChannelInboundHandlerContext; 
import io.netty.channel.ChannelInboundMessageHandlerAdapter; 
import io.netty.channel.socket.DatagramPacket; 
import io.netty.util.CharsetUtil; 

import io.netty.channel.ChannelHandlerContext; 
import io.netty.channel.ExceptionEvent; 
import io.netty.channel.MessageEvent; 
import io.netty.channel.SimpleChannelUpstreamHandler; 
import io.netty.logging.InternalLogger; 
import io.netty.logging.InternalLoggerFactory; 

public  class  QuoteOfTheMomentClientHandler  extends SimpleChannelUpstreamHandler {
	


    @Override
    public void messageReceived(
            ChannelInboundHandlerContext<DatagramPacket> ctx, DatagramPacket msg)
            throws Exception {
        String response = msg.data().toString(CharsetUtil.UTF_8);
        if (response.startsWith("QOTM: ")) {
            System.out.println("Quote of the Moment: " + response.substring(6));
            ctx.close();
        }
    }

	

    @Override
    public void exceptionCaught(
            ChannelInboundHandlerContext<DatagramPacket> ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772190463/fstmerge_var1_8449422466595278964
@Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception {
        String msg = (String) e.getMessage();
        if (msg.startsWith("QOTM: ")) {
            logger.info("Quote of the Moment: " + msg.substring(6));
            e.getChannel().close();
        }
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772190463/fstmerge_var2_5851179379030528759


	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772190514/fstmerge_var1_1153634721737001094
@Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        logger.error("Exception caught", e.getCause());
        e.getChannel().close();
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772190514/fstmerge_var2_8285330174940701352


	
    
    private static final InternalLogger logger =
        InternalLoggerFactory.getInstance(QuoteOfTheMomentClientHandler.class);


}
