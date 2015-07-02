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
package io.netty.channel.socket; 

import com.sun.nio.sctp.MessageInfo; 
import io.netty.buffer.ByteBuf; 
import io.netty.buffer.ByteBufUtil; 
import io.netty.buffer.Unpooled; 

/**
 * Representation of SCTP Data Chunk
 */
  class  SctpData   {
	
    

	
    

	

    

	

    ~~FSTMerge~~ private final MessageInfo msgInfo; ##FSTMerge## private MessageInfo msgInfo; ##FSTMerge##

	

    /**
     * Essential data that is being carried within SCTP Data Chunk
     * @param protocolIdentifier of payload
     * @param streamIdentifier that you want to send the payload
     * @param payloadBuffer channel buffer
     */
    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1391034021464/fstmerge_var1_218719419293399195
public SctpData(int protocolIdentifier, int streamIdentifier, ByteBuf payloadBuffer) {
        this.protocolIdentifier = protocolIdentifier;
        this.streamIdentifier = streamIdentifier;
        this.payloadBuffer = payloadBuffer;
        this.msgInfo = null;
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1391034021464/fstmerge_var2_785254490491347219


	

    

	

    

	

    

	

    

	

    

	

    

	

    

	

    


}
