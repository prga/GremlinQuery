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
package io.netty.handler.codec.frame; 

import io.netty.buffer.ChannelBuffer; 
import io.netty.buffer.ChannelBufferFactory; 
import io.netty.buffer.ChannelBuffers; 
import io.netty.channel.Channel; 
import io.netty.channel.ChannelHandlerContext; 

/**
 * A decoder that splits the received {@link ChannelBuffer}s by the fixed number
 * of bytes. For example, if you received the following four fragmented packets:
 * <pre>
 * +---+----+------+----+
 * | A | BC | DEFG | HI |
 * +---+----+------+----+
 * </pre>
 * A {@link FixedLengthFrameDecoder}{@code (3)} will decode them into the
 * following three packets with the fixed length:
 * <pre>
 * +-----+-----+-----+
 * | ABC | DEF | GHI |
 * +-----+-----+-----+
 * </pre>
 */
  class  FixedLengthFrameDecoder  extends FrameDecoder {
	

    

	
    

	

    /**
     * Calls {@link #FixedLengthFrameDecoder(int, boolean)} with <code>false</code>
     */
    

	

    /**
     * Creates a new instance.
     *
     * @param frameLength  the length of the frame
     * @param allocateFullBuffer <code>true</code> if the cumulative {@link ChannelBuffer} should use the {@link #frameLength} as its initial size
     */
    

	
    
    

	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772179499/fstmerge_var1_1726329568535462833
@Override
    protected ChannelBuffer newCumulationBuffer(ChannelHandlerContext ctx, int minimumCapacity) {
        ChannelBufferFactory factory = ctx.getChannel().getConfig().getBufferFactory();
        if (allocateFullBuffer) {
            return factory.getBuffer(frameLength);
        }
        return super.newCumulationBuffer(ctx, minimumCapacity);
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772179499/fstmerge_var2_4747835127843722507



}
