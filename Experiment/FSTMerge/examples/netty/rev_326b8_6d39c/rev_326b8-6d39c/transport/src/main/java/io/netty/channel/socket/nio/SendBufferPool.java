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
package io.netty.channel.socket.nio; 

import java.io.IOException; 
import java.lang.ref.SoftReference; 
import java.net.SocketAddress; 
import java.nio.ByteBuffer; 
import java.nio.channels.DatagramChannel; 
import java.nio.channels.WritableByteChannel; 

import io.netty.buffer.ChannelBuffer; 
import io.netty.channel.FileRegion; 
import java.nio.channels.GatheringByteChannel; 
import io.netty.buffer.CompositeChannelBuffer; 
import io.netty.util.internal.DetectionUtil; 

  class  SendBufferPool {
	

    

	

    

	
    

	
    

	

    

	
    

	

    

	

    
    

	

    

	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772200720/fstmerge_var1_849102824128914884
private SendBuffer acquire(ChannelBuffer src) {
        final int size = src.readableBytes();
        if (size == 0) {
            return EMPTY_BUFFER;
        }

        if (src.isDirect()) {
            return new UnpooledSendBuffer(src.toByteBuffer());
        }
        
        if (src instanceof CompositeChannelBuffer && DetectionUtil.javaVersion() >= 7) {
            return new GatheringSendBuffer(src.toByteBuffers());
        }
        
        if (src.readableBytes() > DEFAULT_PREALLOCATION_SIZE) {
            return new UnpooledSendBuffer(src.toByteBuffer());
        }

        Preallocation current = this.current;
        ByteBuffer buffer = current.buffer;
        int remaining = buffer.remaining();
        PooledSendBuffer dst;

        if (size < remaining) {
            int nextPos = buffer.position() + size;
            ByteBuffer slice = buffer.duplicate();
            buffer.position(align(nextPos));
            slice.limit(nextPos);
            current.refCnt ++;
            dst = new PooledSendBuffer(current, slice);
        } else if (size > remaining) {
            this.current = current = getPreallocation();
            buffer = current.buffer;
            ByteBuffer slice = buffer.duplicate();
            buffer.position(align(size));
            slice.limit(size);
            current.refCnt ++;
            dst = new PooledSendBuffer(current, slice);
        } else { // size == remaining
            current.refCnt ++;
            this.current = getPreallocation0();
            dst = new PooledSendBuffer(current, current.buffer);
        }

        ByteBuffer dstbuf = dst.buffer;
        dstbuf.mark();
        src.getBytes(src.readerIndex(), dstbuf);
        dstbuf.reset();
        return dst;
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772200720/fstmerge_var2_2714633993628945782


	

    

	
    
    

	

    

	

      class  Preallocation {
		
        

		
        

		

        


	}

	

      class  PreallocationRef  extends SoftReference<Preallocation> {
		
        

		

        


	}

	

     interface  SendBuffer {
		
        

		
        

		
        

		

        

		
        

		

        


	}

	

      class  UnpooledSendBuffer   {
		

        

		
        

		

        

		

        

		

        

		

        

		

        

		

        

		

        


	}

	

      class  PooledSendBuffer   {
		

        

		
        

		
        

		

        

		

        

		

        

		

        

		

        

		

        

		

        


	}

	
    
      class  FileSendBuffer   {
		

        

		
        

		


        

		

        

		

        

		

        

		

        

		

        

		

        


	}

	

      class  EmptySendBuffer   {
		

        

		

        

		

        

		

        

		

        

		

        

		

        


	}

	

     

    class  GatheringSendBuffer  implements SendBuffer {
		

        private final ByteBuffer[] buffers;

		
        private final int last;

		
        private long written;

		
        private final int total;

		

        GatheringSendBuffer(ByteBuffer[] buffers) {
            this.buffers = buffers;
            this.last = buffers.length - 1;
            int total = 0;
            for (ByteBuffer buf: buffers) {
                total += buf.remaining();
            }
            this.total = total;
        }

		
        
        @Override
        public boolean finished() {
            return !buffers[last].hasRemaining();
        }

		

        @Override
        public long writtenBytes() {
            return written;
        }

		

        @Override
        public long totalBytes() {
            return total;
        }

		

        @Override
        public long transferTo(WritableByteChannel ch) throws IOException {
            if (ch instanceof GatheringByteChannel) {
                 long w = ((GatheringByteChannel) ch).write(buffers);
                 written += w;
                 return w;
            } else {
                int send = 0;
                for (ByteBuffer buf: buffers) {
                    if (buf.hasRemaining()) {
                        int w = ch.write(buf);
                        if (w == 0) {
                            break;
                        } else {
                            send += w;
                        }
                    }
                }
                written += send;
                return send;
            }
        }

		

        @Override
        public long transferTo(DatagramChannel ch, SocketAddress raddr) throws IOException {
            int send = 0;
            for (ByteBuffer buf: buffers) {
                if (buf.hasRemaining()) {
                    int w = ch.send(buf, raddr);
                    if (w == 0) {
                        break;
                    } else {
                        send += w;
                    }
                }
            }
            written += send;

            return send;
        }

		

        @Override
        public void release() {
            // nothing todo
        }


	}


}
