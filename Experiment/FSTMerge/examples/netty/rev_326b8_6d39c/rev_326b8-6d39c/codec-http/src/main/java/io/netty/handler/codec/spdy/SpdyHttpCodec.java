/*
 * Copyright 2012 The Netty Project
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
package io.netty.handler.codec.spdy; 

import io.netty.channel.CombinedChannelHandler; 

import io.netty.channel.ChannelDownstreamHandler; 
import io.netty.channel.ChannelEvent; 
import io.netty.channel.ChannelHandlerContext; 
import io.netty.channel.ChannelUpstreamHandler; 

/**
 * A combination of {@link SpdyHttpDecoder} and {@link SpdyHttpEncoder}
 * @apiviz.has io.netty.handler.codec.sdpy.SpdyHttpDecoder
 * @apiviz.has io.netty.handler.codec.spdy.SpdyHttpEncoder
 */
public  class  SpdyHttpCodec  extends CombinedChannelHandler   {
	

    /**
     * Creates a new instance with the specified decoder options.
     */
    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772183750/fstmerge_var1_3014799589461540718
=======
public SpdyHttpCodec(int maxContentLength) {
        super(new SpdyHttpDecoder(maxContentLength), new SpdyHttpEncoder());
    }
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772183750/fstmerge_var2_2278317535707830306


	

    

	
    ~~FSTMerge~~ private final SpdyHttpEncoder encoder; ##FSTMerge## private final SpdyHttpEncoder encoder = new SpdyHttpEncoder(); ##FSTMerge##

	

    

	

    

	

    /**
     * Creates a new instance with the specified decoder options.
     */
    public SpdyHttpCodec(int version, int maxContentLength) {
        decoder = new SpdyHttpDecoder(version, maxContentLength);
        encoder = new SpdyHttpEncoder(version);
    }


}
