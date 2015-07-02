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

import static io.netty.handler.codec.spdy.SpdyCodecUtil.*; 

import io.netty.buffer.ChannelBuffer; 
import io.netty.buffer.ChannelBuffers; 
import io.netty.channel.ChannelFuture; 
import io.netty.channel.ChannelOutboundHandlerContext; 
import io.netty.handler.codec.MessageToStreamEncoder; 
import io.netty.handler.codec.UnsupportedMessageTypeException; 

import java.nio.ByteOrder; 
import java.util.Set; 
import io.netty.channel.Channel; 
import io.netty.channel.ChannelEvent; 
import io.netty.channel.ChannelHandlerContext; 
import io.netty.channel.ChannelStateEvent; 
import io.netty.handler.codec.oneone.OneToOneEncoder; 

/**
 * Encodes a SPDY Data or Control Frame into a {@link ChannelBuffer}.
 */
public  class  SpdyFrameEncoder  extends OneToOneEncoder {
	
    private volatile boolean finished;

	
    private final SpdyHeaderBlockCompressor headerBlockCompressor;

	

    /**
     * Creates a new instance with the default {@code compressionLevel (6)},
     * {@code windowBits (15)}, and {@code memLevel (8)}.
     */
    

	

    /**
     * Creates a new instance with the specified parameters.
     */
    

	


    @Override
    public void disconnect(ChannelOutboundHandlerContext<Object> ctx,
            ChannelFuture future) throws Exception {
        finish();
        super.disconnect(ctx, future);
    }

	

    @Override
    public void close(ChannelOutboundHandlerContext<Object> ctx,
            ChannelFuture future) throws Exception {
        finish();
        super.close(ctx, future);
    }

	

    private void finish() {
        synchronized (headerBlockCompressor) {
            if (!finished) {
                finished = true;
                headerBlockCompressor.end();
            }
        }
    }

	

    @Override
    public void encode(ChannelOutboundHandlerContext<Object> ctx, Object msg,
            ChannelBuffer out) throws Exception {
        if (msg instanceof SpdyDataFrame) {
            SpdyDataFrame spdyDataFrame = (SpdyDataFrame) msg;
            ChannelBuffer data = spdyDataFrame.getData();
            byte flags = spdyDataFrame.isLast() ? SPDY_DATA_FLAG_FIN : 0;
            if (spdyDataFrame.isCompressed()) {
                flags |= SPDY_DATA_FLAG_COMPRESS;
            }
            out.ensureWritableBytes(SPDY_HEADER_SIZE + data.readableBytes());
            out.writeInt(spdyDataFrame.getStreamID() & 0x7FFFFFFF);
            out.writeByte(flags);
            out.writeMedium(data.readableBytes());
            out.writeBytes(data, data.readerIndex(), data.readableBytes());
        } else if (msg instanceof SpdySynStreamFrame) {
            SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame) msg;
            ChannelBuffer data = compressHeaderBlock(
                    encodeHeaderBlock(spdySynStreamFrame));
            byte flags = spdySynStreamFrame.isLast() ? SPDY_FLAG_FIN : 0;
            if (spdySynStreamFrame.isUnidirectional()) {
                flags |= SPDY_FLAG_UNIDIRECTIONAL;
            }
            int headerBlockLength = data.readableBytes();
            int length = headerBlockLength == 0 ? 12 : 10 + headerBlockLength;
            out.ensureWritableBytes(SPDY_HEADER_SIZE + length + data.readableBytes());
            out.writeShort(SPDY_VERSION | 0x8000);
            out.writeShort(SPDY_SYN_STREAM_FRAME);
            out.writeByte(flags);
            out.writeMedium(length);
            out.writeInt(spdySynStreamFrame.getStreamID());
            out.writeInt(spdySynStreamFrame.getAssociatedToStreamID());
            out.writeShort(spdySynStreamFrame.getPriority() << 14);
            if (data.readableBytes() == 0) {
                out.writeShort(0);
            } else {
                out.writeBytes(data, data.readerIndex(), data.readableBytes());
            }
        } else if (msg instanceof SpdySynReplyFrame) {
            SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame) msg;
            ChannelBuffer data = compressHeaderBlock(
                    encodeHeaderBlock(spdySynReplyFrame));
            byte flags = spdySynReplyFrame.isLast() ? SPDY_FLAG_FIN : 0;
            int headerBlockLength = data.readableBytes();
            int length = headerBlockLength == 0 ? 8 : 6 + headerBlockLength;
            out.ensureWritableBytes(SPDY_HEADER_SIZE + length + data.readableBytes());
            out.writeShort(SPDY_VERSION | 0x8000);
            out.writeShort(SPDY_SYN_REPLY_FRAME);
            out.writeByte(flags);
            out.writeMedium(length);
            out.writeInt(spdySynReplyFrame.getStreamID());
            if (data.readableBytes() == 0) {
                out.writeInt(0);
            } else {
                out.writeShort(0);
                out.writeBytes(data, data.readerIndex(), data.readableBytes());
            }
        } else if (msg instanceof SpdyRstStreamFrame) {
            SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame) msg;
            out.ensureWritableBytes(SPDY_HEADER_SIZE + 8);
            out.writeShort(SPDY_VERSION | 0x8000);
            out.writeShort(SPDY_RST_STREAM_FRAME);
            out.writeInt(8);
            out.writeInt(spdyRstStreamFrame.getStreamID());
            out.writeInt(spdyRstStreamFrame.getStatus().getCode());
        } else if (msg instanceof SpdySettingsFrame) {
            SpdySettingsFrame spdySettingsFrame = (SpdySettingsFrame) msg;
            byte flags = spdySettingsFrame.clearPreviouslyPersistedSettings() ?
                SPDY_SETTINGS_CLEAR : 0;
            Set<Integer> IDs = spdySettingsFrame.getIDs();
            int numEntries = IDs.size();
            int length = 4 + numEntries * 8;
            out.ensureWritableBytes(SPDY_HEADER_SIZE + length);
            out.writeShort(SPDY_VERSION | 0x8000);
            out.writeShort(SPDY_SETTINGS_FRAME);
            out.writeByte(flags);
            out.writeMedium(length);
            out.writeInt(numEntries);
            for (Integer ID: IDs) {
                int id = ID.intValue();
                byte ID_flags = (byte) 0;
                if (spdySettingsFrame.persistValue(id)) {
                    ID_flags |= SPDY_SETTINGS_PERSIST_VALUE;
                }
                if (spdySettingsFrame.isPersisted(id)) {
                    ID_flags |= SPDY_SETTINGS_PERSISTED;
                }
                // Chromium Issue 79156
                // SPDY setting ids are not written in network byte order
                // Write id assuming the architecture is little endian
                out.writeByte(id >>  0 & 0xFF);
                out.writeByte(id >>  8 & 0xFF);
                out.writeByte(id >> 16 & 0xFF);
                out.writeByte(ID_flags);
                out.writeInt(spdySettingsFrame.getValue(id));
            }
        } else if (msg instanceof SpdyNoOpFrame) {
            out.ensureWritableBytes(SPDY_HEADER_SIZE);
            out.writeShort(SPDY_VERSION | 0x8000);
            out.writeShort(SPDY_NOOP_FRAME);
            out.writeInt(0);
        } else if (msg instanceof SpdyPingFrame) {
            SpdyPingFrame spdyPingFrame = (SpdyPingFrame) msg;
            out.ensureWritableBytes(SPDY_HEADER_SIZE + 4);
            out.writeShort(SPDY_VERSION | 0x8000);
            out.writeShort(SPDY_PING_FRAME);
            out.writeInt(4);
            out.writeInt(spdyPingFrame.getID());
        } else if (msg instanceof SpdyGoAwayFrame) {
            SpdyGoAwayFrame spdyGoAwayFrame = (SpdyGoAwayFrame) msg;
            out.ensureWritableBytes(SPDY_HEADER_SIZE + 4);
            out.writeShort(SPDY_VERSION | 0x8000);
            out.writeShort(SPDY_GOAWAY_FRAME);
            out.writeInt(4);
            out.writeInt(spdyGoAwayFrame.getLastGoodStreamID());
        } else if (msg instanceof SpdyHeadersFrame) {
            SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame) msg;
            ChannelBuffer data = compressHeaderBlock(
                    encodeHeaderBlock(spdyHeadersFrame));
            int headerBlockLength = data.readableBytes();
            int length = headerBlockLength == 0 ? 4 : 6 + headerBlockLength;
            out.ensureWritableBytes(SPDY_HEADER_SIZE + length + data.readableBytes());
            out.writeShort(SPDY_VERSION | 0x8000);
            out.writeShort(SPDY_HEADERS_FRAME);
            out.writeInt(length);
            out.writeInt(spdyHeadersFrame.getStreamID());
            if (data.readableBytes() != 0) {
                out.writeShort(0);
                out.writeBytes(data, data.readerIndex(), data.readableBytes());
            }
        } else {
            // Unknown message type
            throw new UnsupportedMessageTypeException(msg);
        }
    }

	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772183517/fstmerge_var1_7361072504637988914
=======
private static ChannelBuffer encodeHeaderBlock(SpdyHeaderBlock headerFrame)
            throws Exception {
        Set<String> names = headerFrame.getHeaderNames();
        int numHeaders = names.size();
        if (numHeaders == 0) {
            return ChannelBuffers.EMPTY_BUFFER;
        }
        if (numHeaders > SPDY_MAX_NV_LENGTH) {
            throw new IllegalArgumentException(
                    "header block contains too many headers");
        }
        ChannelBuffer headerBlock = ChannelBuffers.dynamicBuffer(
                ByteOrder.BIG_ENDIAN, 256);
        headerBlock.writeShort(numHeaders);
        for (String name: names) {
            byte[] nameBytes = name.getBytes("UTF-8");
            headerBlock.writeShort(nameBytes.length);
            headerBlock.writeBytes(nameBytes);
            int savedIndex = headerBlock.writerIndex();
            int valueLength = 0;
            headerBlock.writeShort(valueLength);
            for (String value: headerFrame.getHeaders(name)) {
                byte[] valueBytes = value.getBytes("UTF-8");
                headerBlock.writeBytes(valueBytes);
                headerBlock.writeByte(0);
                valueLength += valueBytes.length + 1;
            }
            valueLength --;
            if (valueLength > SPDY_MAX_NV_LENGTH) {
                throw new IllegalArgumentException(
                        "header exceeds allowable length: " + name);
            }
            headerBlock.setShort(savedIndex, valueLength);
            headerBlock.writerIndex(headerBlock.writerIndex() - 1);
        }
        return headerBlock;
    }
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772183517/fstmerge_var2_6547495545445850386


	

    private synchronized ChannelBuffer compressHeaderBlock(
            ChannelBuffer uncompressed) throws Exception {
        if (uncompressed.readableBytes() == 0) {
            return ChannelBuffers.EMPTY_BUFFER;
        }
        ChannelBuffer compressed = ChannelBuffers.dynamicBuffer();
        synchronized (headerBlockCompressor) {
            if (!finished) {
                headerBlockCompressor.setInput(uncompressed);
                headerBlockCompressor.encode(compressed);
            }
        }
        return compressed;
    }


	

    

	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772183675/fstmerge_var1_5241803637231100383
@Override
    protected Object encode(
            ChannelHandlerContext ctx, Channel channel, Object msg)
            throws Exception {

        if (msg instanceof SpdyDataFrame) {

            SpdyDataFrame spdyDataFrame = (SpdyDataFrame) msg;
            ChannelBuffer data = spdyDataFrame.getData();
            byte flags = spdyDataFrame.isLast() ? SPDY_DATA_FLAG_FIN : 0;
            ChannelBuffer header = ChannelBuffers.buffer(
                    ByteOrder.BIG_ENDIAN, SPDY_HEADER_SIZE);
            header.writeInt(spdyDataFrame.getStreamID() & 0x7FFFFFFF);
            header.writeByte(flags);
            header.writeMedium(data.readableBytes());
            return ChannelBuffers.wrappedBuffer(header, data);

        } else if (msg instanceof SpdySynStreamFrame) {

            SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame) msg;
            ChannelBuffer data = compressHeaderBlock(
                    encodeHeaderBlock(version, spdySynStreamFrame));
            byte flags = spdySynStreamFrame.isLast() ? SPDY_FLAG_FIN : 0;
            if (spdySynStreamFrame.isUnidirectional()) {
                flags |= SPDY_FLAG_UNIDIRECTIONAL;
            }
            int headerBlockLength = data.readableBytes();
            int length;
            if (version < 3) {
                length = headerBlockLength == 0 ? 12 : 10 + headerBlockLength;
            } else {
                length = 10 + headerBlockLength;
            }
            ChannelBuffer frame = ChannelBuffers.buffer(
                    ByteOrder.BIG_ENDIAN, SPDY_HEADER_SIZE + length);
            frame.writeShort(version | 0x8000);
            frame.writeShort(SPDY_SYN_STREAM_FRAME);
            frame.writeByte(flags);
            frame.writeMedium(length);
            frame.writeInt(spdySynStreamFrame.getStreamID());
            frame.writeInt(spdySynStreamFrame.getAssociatedToStreamID());
            if (version < 3) {
                // Restrict priorities for SPDY/2 to between 0 and 3
                byte priority = spdySynStreamFrame.getPriority();
                if (priority > 3) {
                    priority = 3;
                }
                frame.writeShort((priority & 0xFF) << 14);
            } else {
                frame.writeShort((spdySynStreamFrame.getPriority() & 0xFF) << 13);
            }
            if (version < 3 && data.readableBytes() == 0) {
                frame.writeShort(0);
            }
            return ChannelBuffers.wrappedBuffer(frame, data);

        } else if (msg instanceof SpdySynReplyFrame) {

            SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame) msg;
            ChannelBuffer data = compressHeaderBlock(
                    encodeHeaderBlock(version, spdySynReplyFrame));
            byte flags = spdySynReplyFrame.isLast() ? SPDY_FLAG_FIN : 0;
            int headerBlockLength = data.readableBytes();
            int length;
            if (version < 3) {
                length = headerBlockLength == 0 ? 8 : 6 + headerBlockLength;
            } else {
                length = 4 + headerBlockLength;
            }
            ChannelBuffer frame = ChannelBuffers.buffer(
                    ByteOrder.BIG_ENDIAN, SPDY_HEADER_SIZE + length);
            frame.writeShort(version | 0x8000);
            frame.writeShort(SPDY_SYN_REPLY_FRAME);
            frame.writeByte(flags);
            frame.writeMedium(length);
            frame.writeInt(spdySynReplyFrame.getStreamID());
            if (version < 3) {
                if (data.readableBytes() == 0) {
                    frame.writeInt(0);
                } else {
                    frame.writeShort(0);
                }
            }
            return ChannelBuffers.wrappedBuffer(frame, data);

        } else if (msg instanceof SpdyRstStreamFrame) {

            SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame) msg;
            ChannelBuffer frame = ChannelBuffers.buffer(
                    ByteOrder.BIG_ENDIAN, SPDY_HEADER_SIZE + 8);
            frame.writeShort(version | 0x8000);
            frame.writeShort(SPDY_RST_STREAM_FRAME);
            frame.writeInt(8);
            frame.writeInt(spdyRstStreamFrame.getStreamID());
            frame.writeInt(spdyRstStreamFrame.getStatus().getCode());
            return frame;

        } else if (msg instanceof SpdySettingsFrame) {

            SpdySettingsFrame spdySettingsFrame = (SpdySettingsFrame) msg;
            byte flags = spdySettingsFrame.clearPreviouslyPersistedSettings() ?
                SPDY_SETTINGS_CLEAR : 0;
            Set<Integer> IDs = spdySettingsFrame.getIDs();
            int numEntries = IDs.size();
            int length = 4 + numEntries * 8;
            ChannelBuffer frame = ChannelBuffers.buffer(
                    ByteOrder.BIG_ENDIAN, SPDY_HEADER_SIZE + length);
            frame.writeShort(version | 0x8000);
            frame.writeShort(SPDY_SETTINGS_FRAME);
            frame.writeByte(flags);
            frame.writeMedium(length);
            frame.writeInt(numEntries);
            for (Integer ID: IDs) {
                int id = ID.intValue();
                byte ID_flags = (byte) 0;
                if (spdySettingsFrame.persistValue(id)) {
                    ID_flags |= SPDY_SETTINGS_PERSIST_VALUE;
                }
                if (spdySettingsFrame.isPersisted(id)) {
                    ID_flags |= SPDY_SETTINGS_PERSISTED;
                }
                if (version < 3) {
                    // Chromium Issue 79156
                    // SPDY setting ids are not written in network byte order
                    // Write id assuming the architecture is little endian
                    frame.writeByte(id >>  0 & 0xFF);
                    frame.writeByte(id >>  8 & 0xFF);
                    frame.writeByte(id >> 16 & 0xFF);
                    frame.writeByte(ID_flags);
                } else {
                    frame.writeByte(ID_flags);
                    frame.writeMedium(id);
                }
                frame.writeInt(spdySettingsFrame.getValue(id));
            }
            return frame;

        } else if (msg instanceof SpdyNoOpFrame) {

            ChannelBuffer frame = ChannelBuffers.buffer(
                    ByteOrder.BIG_ENDIAN, SPDY_HEADER_SIZE);
            frame.writeShort(version | 0x8000);
            frame.writeShort(SPDY_NOOP_FRAME);
            frame.writeInt(0);
            return frame;

        } else if (msg instanceof SpdyPingFrame) {

            SpdyPingFrame spdyPingFrame = (SpdyPingFrame) msg;
            ChannelBuffer frame = ChannelBuffers.buffer(
                    ByteOrder.BIG_ENDIAN, SPDY_HEADER_SIZE + 4);
            frame.writeShort(version | 0x8000);
            frame.writeShort(SPDY_PING_FRAME);
            frame.writeInt(4);
            frame.writeInt(spdyPingFrame.getID());
            return frame;

        } else if (msg instanceof SpdyGoAwayFrame) {

            SpdyGoAwayFrame spdyGoAwayFrame = (SpdyGoAwayFrame) msg;
            int length = version < 3 ? 4 : 8;
            ChannelBuffer frame = ChannelBuffers.buffer(
                    ByteOrder.BIG_ENDIAN, SPDY_HEADER_SIZE + length);
            frame.writeShort(version | 0x8000);
            frame.writeShort(SPDY_GOAWAY_FRAME);
            frame.writeInt(length);
            frame.writeInt(spdyGoAwayFrame.getLastGoodStreamID());
            if (version >= 3) {
                frame.writeInt(spdyGoAwayFrame.getStatus().getCode());
            }
            return frame;

        } else if (msg instanceof SpdyHeadersFrame) {

            SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame) msg;
            ChannelBuffer data = compressHeaderBlock(
                    encodeHeaderBlock(version, spdyHeadersFrame));
            byte flags = spdyHeadersFrame.isLast() ? SPDY_FLAG_FIN : 0;
            int headerBlockLength = data.readableBytes();
            int length;
            if (version < 3) {
                length = headerBlockLength == 0 ? 4 : 6 + headerBlockLength;
            } else {
                length = 4 + headerBlockLength;
            }
            ChannelBuffer frame = ChannelBuffers.buffer(
                    ByteOrder.BIG_ENDIAN, SPDY_HEADER_SIZE + length);
            frame.writeShort(version | 0x8000);
            frame.writeShort(SPDY_HEADERS_FRAME);
            frame.writeByte(flags);
            frame.writeMedium(length);
            frame.writeInt(spdyHeadersFrame.getStreamID());
            if (version < 3 && data.readableBytes() != 0) {
                frame.writeShort(0);
            }
            return ChannelBuffers.wrappedBuffer(frame, data);

        } else if (msg instanceof SpdyWindowUpdateFrame) {

            SpdyWindowUpdateFrame spdyWindowUpdateFrame = (SpdyWindowUpdateFrame) msg;
            ChannelBuffer frame = ChannelBuffers.buffer(
                    ByteOrder.BIG_ENDIAN, SPDY_HEADER_SIZE + 8);
            frame.writeShort(version | 0x8000);
            frame.writeShort(SPDY_WINDOW_UPDATE_FRAME);
            frame.writeInt(8);
            frame.writeInt(spdyWindowUpdateFrame.getStreamID());
            frame.writeInt(spdyWindowUpdateFrame.getDeltaWindowSize());
            return frame;
        }

        // Unknown message type
        return msg;
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772183675/fstmerge_var2_2256815790940957802


	

    private final int version;

	

    /**
     * Creates a new instance with the specified {@code version} and the
     * default {@code compressionLevel (6)}, {@code windowBits (15)},
     * and {@code memLevel (8)}.
     */
    public SpdyFrameEncoder(int version) {
        this(version, 6, 15, 8);
    }

	

    /**
     * Creates a new instance with the specified parameters.
     */
    public SpdyFrameEncoder(int version, int compressionLevel, int windowBits, int memLevel) {
        super();
        if (version < SPDY_MIN_VERSION || version > SPDY_MAX_VERSION) {
            throw new IllegalArgumentException(
                    "unknown version: " + version);
        }
        this.version = version;
        headerBlockCompressor = SpdyHeaderBlockCompressor.newInstance(
                version, compressionLevel, windowBits, memLevel);
    }

	

    private static void writeLengthField(int version, ChannelBuffer buffer, int length) {
        if (version < 3) {
            buffer.writeShort(length);
        } else {
            buffer.writeInt(length);
        }
    }

	

    private static void setLengthField(int version, ChannelBuffer buffer, int writerIndex, int length) {
        if (version < 3) {
            buffer.setShort(writerIndex, length);
        } else {
            buffer.setInt(writerIndex, length);
        }
    }

	

    private static ChannelBuffer encodeHeaderBlock(int version, SpdyHeaderBlock headerFrame)
            throws Exception {
        Set<String> names = headerFrame.getHeaderNames();
        int numHeaders = names.size();
        if (numHeaders == 0) {
            return ChannelBuffers.EMPTY_BUFFER;
        }
        if (numHeaders > SPDY_MAX_NV_LENGTH) {
            throw new IllegalArgumentException(
                    "header block contains too many headers");
        }
        ChannelBuffer headerBlock = ChannelBuffers.dynamicBuffer(
                ByteOrder.BIG_ENDIAN, 256);
        writeLengthField(version, headerBlock, numHeaders);
        for (String name: names) {
            byte[] nameBytes = name.getBytes("UTF-8");
            writeLengthField(version, headerBlock, nameBytes.length);
            headerBlock.writeBytes(nameBytes);
            int savedIndex = headerBlock.writerIndex();
            int valueLength = 0;
            writeLengthField(version, headerBlock, valueLength);
            for (String value: headerFrame.getHeaders(name)) {
                byte[] valueBytes = value.getBytes("UTF-8");
                headerBlock.writeBytes(valueBytes);
                headerBlock.writeByte(0);
                valueLength += valueBytes.length + 1;
            }
            valueLength --;
            if (valueLength > SPDY_MAX_NV_LENGTH) {
                throw new IllegalArgumentException(
                        "header exceeds allowable length: " + name);
            }
            setLengthField(version, headerBlock, savedIndex, valueLength);
            headerBlock.writerIndex(headerBlock.writerIndex() - 1);
        }
        return headerBlock;
    }


}
