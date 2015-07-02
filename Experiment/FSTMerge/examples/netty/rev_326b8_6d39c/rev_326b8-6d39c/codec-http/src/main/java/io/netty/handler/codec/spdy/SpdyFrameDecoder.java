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
import io.netty.channel.ChannelInboundHandlerContext; 
import io.netty.handler.codec.StreamToMessageDecoder; 
import io.netty.channel.Channel; 
import io.netty.channel.ChannelHandlerContext; 
import io.netty.handler.codec.frame.FrameDecoder; 
import io.netty.channel.Channels; 
import io.netty.handler.codec.frame.TooLongFrameException; 

/**
 * Decodes {@link ChannelBuffer}s into SPDY Data and Control Frames.
 */
public  class  SpdyFrameDecoder  extends FrameDecoder {
	
    private final int maxChunkSize;

	
    

	
    private final int maxHeaderSize;

	

    private final SpdyHeaderBlockDecompressor headerBlockDecompressor;

	

    /**
     * Creates a new instance with the default {@code maxChunkSize (8192)},
     * {@code maxFrameSize (65536)}, and {@code maxHeaderSize (16384)}.
     */
    

	

    /**
     * Creates a new instance with the specified parameters.
     */
    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772183063/fstmerge_var1_7010308497934951852
public SpdyFrameDecoder(int version, int maxChunkSize, int maxHeaderSize) {
        super(false);
        if (version < SPDY_MIN_VERSION || version > SPDY_MAX_VERSION) {
            throw new IllegalArgumentException(
                    "unsupported version: " + version);
        }
=======
public SpdyFrameDecoder(
            int maxChunkSize, int maxFrameSize, int maxHeaderSize) {
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772183063/fstmerge_var2_3321147181790486026
        if (maxChunkSize <= 0) {
            throw new IllegalArgumentException(
                    "maxChunkSize must be a positive integer: " + maxChunkSize);
        }
        if (maxHeaderSize <= 0) {
            throw new IllegalArgumentException(
                    "maxHeaderSize must be a positive integer: " + maxHeaderSize);
        }
        spdyVersion = version;
        this.maxChunkSize = maxChunkSize;
        this.maxHeaderSize = maxHeaderSize;
        headerBlockDecompressor = SpdyHeaderBlockDecompressor.newInstance(version);
        state = State.READ_COMMON_HEADER;
    }


	

    @Override
    public Object decodeLast(ChannelInboundHandlerContext<Byte> ctx,
            ChannelBuffer in) throws Exception {
        try {
            return decode(ctx, in);
        } finally {
            headerBlockDecompressor.end();
        }
    }

	

    @Override
    public Object decode(ChannelInboundHandlerContext<Byte> ctx,
            ChannelBuffer in) throws Exception {
        // Must read common header to determine frame length
        if (in.readableBytes() < SPDY_HEADER_SIZE) {
            return null;
        }

        // Get frame length from common header
        int frameOffset  = in.readerIndex();
        int lengthOffset = frameOffset + SPDY_HEADER_LENGTH_OFFSET;
        int dataLength   = getUnsignedMedium(in, lengthOffset);
        int frameLength  = SPDY_HEADER_SIZE + dataLength;

        // Throw exception if frameLength exceeds maxFrameSize
        if (frameLength > maxFrameSize) {
            throw new SpdyProtocolException(
                    "Frame length exceeds " + maxFrameSize + ": " + frameLength);
        }

        // Wait until entire frame is readable
        if (in.readableBytes() < frameLength) {
            return null;
        }

        // Read common header fields
        boolean control = (in.getByte(frameOffset) & 0x80) != 0;
        int flagsOffset = frameOffset + SPDY_HEADER_FLAGS_OFFSET;
        byte flags = in.getByte(flagsOffset);

        if (control) {
            // Decode control frame common header
            int version = getUnsignedShort(in, frameOffset) & 0x7FFF;

            // Spdy versioning spec is broken
            if (version != SPDY_VERSION) {
                in.skipBytes(frameLength);
                throw new SpdyProtocolException(
                        "Unsupported version: " + version);
            }

            int typeOffset = frameOffset + SPDY_HEADER_TYPE_OFFSET;
            int type = getUnsignedShort(in, typeOffset);
            in.skipBytes(SPDY_HEADER_SIZE);

            int readerIndex = in.readerIndex();
            in.skipBytes(dataLength);
            return decodeControlFrame(type, flags, in.slice(readerIndex, dataLength));
        } else {
            // Decode data frame common header
            int streamID = getUnsignedInt(in, frameOffset);
            in.skipBytes(SPDY_HEADER_SIZE);

            // Generate data frames that do not exceed maxChunkSize
            int numFrames = dataLength / maxChunkSize;
            if (dataLength % maxChunkSize != 0) {
                numFrames ++;
            }
            SpdyDataFrame[] frames = new SpdyDataFrame[numFrames];
            for (int i = 0; i < numFrames; i++) {
                int chunkSize = Math.min(maxChunkSize, dataLength);
                SpdyDataFrame spdyDataFrame = new DefaultSpdyDataFrame(streamID);
                spdyDataFrame.setCompressed((flags & SPDY_DATA_FLAG_COMPRESS) != 0);
                spdyDataFrame.setData(in.readBytes(chunkSize));
                dataLength -= chunkSize;
                if (dataLength == 0) {
                    spdyDataFrame.setLast((flags & SPDY_DATA_FLAG_FIN) != 0);
                }
                frames[i] = spdyDataFrame;
            }

            return frames;
        }
    }

	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772183115/fstmerge_var1_3703934215807988882
=======
private Object decodeControlFrame(int type, byte flags, ChannelBuffer data)
            throws Exception {
        int streamID;
        boolean last;

        switch (type) {
        case SPDY_SYN_STREAM_FRAME:
            if (data.readableBytes() < 12) {
                throw new SpdyProtocolException(
                        "Received invalid SYN_STREAM control frame");
            }
            streamID = getUnsignedInt(data, data.readerIndex());
            int associatedToStreamID = getUnsignedInt(data, data.readerIndex() + 4);
            byte priority = (byte) (data.getByte(data.readerIndex() + 8) >> 6 & 0x03);
            data.skipBytes(10);

            SpdySynStreamFrame spdySynStreamFrame =
                new DefaultSpdySynStreamFrame(streamID, associatedToStreamID, priority);

            last = (flags & SPDY_FLAG_FIN) != 0;
            boolean unid = (flags & SPDY_FLAG_UNIDIRECTIONAL) != 0;
            spdySynStreamFrame.setLast(last);
            spdySynStreamFrame.setUnidirectional(unid);

            decodeHeaderBlock(spdySynStreamFrame, data);

            return spdySynStreamFrame;

        case SPDY_SYN_REPLY_FRAME:
            if (data.readableBytes() < 8) {
                throw new SpdyProtocolException(
                        "Received invalid SYN_REPLY control frame");
            }
            streamID = getUnsignedInt(data, data.readerIndex());
            data.skipBytes(6);

            SpdySynReplyFrame spdySynReplyFrame =
                new DefaultSpdySynReplyFrame(streamID);

            last = (flags & SPDY_FLAG_FIN) != 0;
            spdySynReplyFrame.setLast(last);

            decodeHeaderBlock(spdySynReplyFrame, data);

            return spdySynReplyFrame;

        case SPDY_RST_STREAM_FRAME:
            if (flags != 0 || data.readableBytes() != 8) {
                throw new SpdyProtocolException(
                        "Received invalid RST_STREAM control frame");
            }
            streamID = getUnsignedInt(data, data.readerIndex());
            int statusCode = getSignedInt(data, data.readerIndex() + 4);
            if (statusCode == 0) {
                throw new SpdyProtocolException(
                        "Received invalid RST_STREAM status code");
            }

            return new DefaultSpdyRstStreamFrame(streamID, statusCode);

        case SPDY_SETTINGS_FRAME:
            if (data.readableBytes() < 4) {
                throw new SpdyProtocolException(
                        "Received invalid SETTINGS control frame");
            }
            // Each ID/Value entry is 8 bytes
            // The number of entries cannot exceed SPDY_MAX_LENGTH / 8;
            int numEntries = getUnsignedInt(data, data.readerIndex());
            if (numEntries > (SPDY_MAX_LENGTH - 4) / 8 ||
                data.readableBytes() != numEntries * 8 + 4) {
                throw new SpdyProtocolException(
                        "Received invalid SETTINGS control frame");
            }
            data.skipBytes(4);

            SpdySettingsFrame spdySettingsFrame = new DefaultSpdySettingsFrame();

            boolean clear = (flags & SPDY_SETTINGS_CLEAR) != 0;
            spdySettingsFrame.setClearPreviouslyPersistedSettings(clear);

            for (int i = 0; i < numEntries; i ++) {
                // Chromium Issue 79156
                // SPDY setting ids are not written in network byte order
                // Read id assuming the architecture is little endian
                int ID = data.readByte() & 0xFF |
                         (data.readByte() & 0xFF) << 8 |
                         (data.readByte() & 0xFF) << 16;
                byte ID_flags = data.readByte();
                int value = getSignedInt(data, data.readerIndex());
                data.skipBytes(4);

                if (!spdySettingsFrame.isSet(ID)) {
                    boolean persistVal = (ID_flags & SPDY_SETTINGS_PERSIST_VALUE) != 0;
                    boolean persisted  = (ID_flags & SPDY_SETTINGS_PERSISTED) != 0;
                    spdySettingsFrame.setValue(ID, value, persistVal, persisted);
                }
            }

            return spdySettingsFrame;

        case SPDY_NOOP_FRAME:
            if (data.readableBytes() != 0) {
                throw new SpdyProtocolException(
                        "Received invalid NOOP control frame");
            }

            return null;

        case SPDY_PING_FRAME:
            if (data.readableBytes() != 4) {
                throw new SpdyProtocolException(
                        "Received invalid PING control frame");
            }
            int ID = getSignedInt(data, data.readerIndex());

            return new DefaultSpdyPingFrame(ID);

        case SPDY_GOAWAY_FRAME:
            if (data.readableBytes() != 4) {
                throw new SpdyProtocolException(
                        "Received invalid GOAWAY control frame");
            }
            int lastGoodStreamID = getUnsignedInt(data, data.readerIndex());

            return new DefaultSpdyGoAwayFrame(lastGoodStreamID);

        case SPDY_HEADERS_FRAME:
            // Protocol allows length 4 frame when there are no name/value pairs
            if (data.readableBytes() == 4) {
                streamID = getUnsignedInt(data, data.readerIndex());
                return new DefaultSpdyHeadersFrame(streamID);
            }

            if (data.readableBytes() < 8) {
                throw new SpdyProtocolException(
                        "Received invalid HEADERS control frame");
            }
            streamID = getUnsignedInt(data, data.readerIndex());
            data.skipBytes(6);

            SpdyHeadersFrame spdyHeadersFrame = new DefaultSpdyHeadersFrame(streamID);

            decodeHeaderBlock(spdyHeadersFrame, data);

            return spdyHeadersFrame;

        case SPDY_WINDOW_UPDATE_FRAME:
            return null;

        default:
            return null;
        }
    }
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772183115/fstmerge_var2_6247626205346343366


	

    

	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772183236/fstmerge_var1_5306111254718332576
=======
private void decodeHeaderBlock(SpdyHeaderBlock headerFrame, ChannelBuffer headerBlock)
            throws Exception {
        if (headerBlock.readableBytes() == 2 &&
            headerBlock.getShort(headerBlock.readerIndex()) == 0) {
            return;
        }

        headerBlockDecompressor.setInput(headerBlock);
        ChannelBuffer decompressed = ChannelBuffers.dynamicBuffer(8192);
        headerBlockDecompressor.decode(decompressed);

        if (decompressed.readableBytes() < 2) {
            throw new SpdyProtocolException(
                    "Received invalid header block");
        }
        int headerSize = 0;
        int numEntries = decompressed.readUnsignedShort();
        for (int i = 0; i < numEntries; i ++) {
            if (!ensureBytes(decompressed, 2)) {
                throw new SpdyProtocolException(
                        "Received invalid header block");
            }
            int nameLength = decompressed.readUnsignedShort();
            if (nameLength == 0) {
                headerFrame.setInvalid();
                return;
            }
            headerSize += nameLength;
            if (headerSize > maxHeaderSize) {
                throw new SpdyProtocolException(
                        "Header block exceeds " + maxHeaderSize);
            }
            if (!ensureBytes(decompressed, nameLength)) {
                throw new SpdyProtocolException(
                        "Received invalid header block");
            }
            byte[] nameBytes = new byte[nameLength];
            decompressed.readBytes(nameBytes);
            String name = new String(nameBytes, "UTF-8");
            if (headerFrame.containsHeader(name)) {
                throw new SpdyProtocolException(
                        "Received duplicate header name: " + name);
            }
            if (!ensureBytes(decompressed, 2)) {
                throw new SpdyProtocolException(
                        "Received invalid header block");
            }
            int valueLength = decompressed.readUnsignedShort();
            if (valueLength == 0) {
                headerFrame.setInvalid();
                return;
            }
            headerSize += valueLength;
            if (headerSize > maxHeaderSize) {
                throw new SpdyProtocolException(
                        "Header block exceeds " + maxHeaderSize);
            }
            if (!ensureBytes(decompressed, valueLength)) {
                throw new SpdyProtocolException(
                        "Received invalid header block");
            }
            byte[] valueBytes = new byte[valueLength];
            decompressed.readBytes(valueBytes);
            int index = 0;
            int offset = 0;
            while (index < valueLength) {
                while (index < valueBytes.length && valueBytes[index] != (byte) 0) {
                    index ++;
                }
                if (index < valueBytes.length && valueBytes[index + 1] == (byte) 0) {
                    // Received multiple, in-sequence NULL characters
                    headerFrame.setInvalid();
                    return;
                }
                String value = new String(valueBytes, offset, index - offset, "UTF-8");
                headerFrame.addHeader(name, value);
                index ++;
                offset = index;
            }
        }
    }
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772183236/fstmerge_var2_8948104365293903551


	

    

	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772183343/fstmerge_var1_2980588682562011086
@Override
    protected Object decode(
            ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer)
            throws Exception {
        switch(state) {
        case READ_COMMON_HEADER:
            state = readCommonHeader(buffer);
            if (state == State.FRAME_ERROR) {
                if (version != spdyVersion) {
                    fireProtocolException(ctx, "Unsupported version: " + version);
                } else {
                    fireInvalidControlFrameException(ctx);
                }
            }

            // FrameDecoders must consume data when producing frames
            // All length 0 frames must be generated now
            if (length == 0) {
                if (state == State.READ_DATA_FRAME) {
                    if (streamID == 0) {
                        state = State.FRAME_ERROR;
                        fireProtocolException(ctx, "Received invalid data frame");
                        return null;
                    }

                    SpdyDataFrame spdyDataFrame = new DefaultSpdyDataFrame(streamID);
                    spdyDataFrame.setLast((flags & SPDY_DATA_FLAG_FIN) != 0);
                    state = State.READ_COMMON_HEADER;
                    return spdyDataFrame;
                }
                // There are no length 0 control frames
                state = State.READ_COMMON_HEADER;
            }

            return null;

        case READ_CONTROL_FRAME:
            try {
                Object frame = readControlFrame(buffer);
                if (frame != null) {
                    state = State.READ_COMMON_HEADER;
                }
                return frame;
            } catch (IllegalArgumentException e) {
                state = State.FRAME_ERROR;
                fireInvalidControlFrameException(ctx);
            }
            return null;

        case READ_SETTINGS_FRAME:
            if (spdySettingsFrame == null) {
                // Validate frame length against number of entries
                if (buffer.readableBytes() < 4) {
                    return null;
                }
                int numEntries = getUnsignedInt(buffer, buffer.readerIndex());
                buffer.skipBytes(4);
                length -= 4;

                // Each ID/Value entry is 8 bytes
                if ((length & 0x07) != 0 || length >> 3 != numEntries) {
                    state = State.FRAME_ERROR;
                    fireInvalidControlFrameException(ctx);
                    return null;
                }

                spdySettingsFrame = new DefaultSpdySettingsFrame();

                boolean clear = (flags & SPDY_SETTINGS_CLEAR) != 0;
                spdySettingsFrame.setClearPreviouslyPersistedSettings(clear);
            }

            int readableEntries = Math.min(buffer.readableBytes() >> 3, length >> 3);
            for (int i = 0; i < readableEntries; i ++) {
                int ID;
                byte ID_flags;
                if (version < 3) {
                    // Chromium Issue 79156
                    // SPDY setting ids are not written in network byte order
                    // Read id assuming the architecture is little endian
                    ID = buffer.readByte() & 0xFF |
                        (buffer.readByte() & 0xFF) << 8 |
                        (buffer.readByte() & 0xFF) << 16;
                    ID_flags = buffer.readByte();
                } else {
                    ID_flags = buffer.readByte();
                    ID = getUnsignedMedium(buffer, buffer.readerIndex());
                    buffer.skipBytes(3);
                }
                int value = getSignedInt(buffer, buffer.readerIndex());
                buffer.skipBytes(4);

                // Check for invalid ID -- avoid IllegalArgumentException in setValue
                if (ID == 0) {
                    state = State.FRAME_ERROR;
                    spdySettingsFrame = null;
                    fireInvalidControlFrameException(ctx);
                    return null;
                }

                if (!spdySettingsFrame.isSet(ID)) {
                    boolean persistVal = (ID_flags & SPDY_SETTINGS_PERSIST_VALUE) != 0;
                    boolean persisted  = (ID_flags & SPDY_SETTINGS_PERSISTED) != 0;
                    spdySettingsFrame.setValue(ID, value, persistVal, persisted);
                }
            }

            length -= 8 * readableEntries;
            if (length == 0) {
                state = State.READ_COMMON_HEADER;
                Object frame = spdySettingsFrame;
                spdySettingsFrame = null;
                return frame;
            }
            return null;

        case READ_HEADER_BLOCK_FRAME:
            try {
                spdyHeaderBlock = readHeaderBlockFrame(buffer);
                if (spdyHeaderBlock != null) {
                    if (length == 0) {
                        state = State.READ_COMMON_HEADER;
                        Object frame = spdyHeaderBlock;
                        spdyHeaderBlock = null;
                        return frame;
                    }
                    state = State.READ_HEADER_BLOCK;
                }
                return null;
            } catch (IllegalArgumentException e) {
                state = State.FRAME_ERROR;
                fireInvalidControlFrameException(ctx);
                return null;
            }

        case READ_HEADER_BLOCK:
            int compressedBytes = Math.min(buffer.readableBytes(), length);
            length -= compressedBytes;

            try {
                decodeHeaderBlock(buffer.readSlice(compressedBytes));
            } catch (Exception e) {
                state = State.FRAME_ERROR;
                spdyHeaderBlock = null;
                decompressed = null;
                Channels.fireExceptionCaught(ctx, e);
                return null;
            }

            if (spdyHeaderBlock != null && spdyHeaderBlock.isInvalid()) {
                Object frame = spdyHeaderBlock;
                spdyHeaderBlock = null;
                decompressed = null;
                if (length == 0) {
                    state = State.READ_COMMON_HEADER;
                }
                return frame;
            }

            if (length == 0) {
                Object frame = spdyHeaderBlock;
                spdyHeaderBlock = null;
                state = State.READ_COMMON_HEADER;
                return frame;
            }
            return null;

        case READ_DATA_FRAME:
            if (streamID == 0) {
                state = State.FRAME_ERROR;
                fireProtocolException(ctx, "Received invalid data frame");
                return null;
            }

            // Generate data frames that do not exceed maxChunkSize
            int dataLength = Math.min(maxChunkSize, length);

            // Wait until entire frame is readable
            if (buffer.readableBytes() < dataLength) {
                return null;
            }

            SpdyDataFrame spdyDataFrame = new DefaultSpdyDataFrame(streamID);
            spdyDataFrame.setData(buffer.readBytes(dataLength));
            length -= dataLength;

            if (length == 0) {
                spdyDataFrame.setLast((flags & SPDY_DATA_FLAG_FIN) != 0);
                state = State.READ_COMMON_HEADER;
            }
            return spdyDataFrame;

        case DISCARD_FRAME:
            int numBytes = Math.min(buffer.readableBytes(), length);
            buffer.skipBytes(numBytes);
            length -= numBytes;
            if (length == 0) {
                state = State.READ_COMMON_HEADER;
            }
            return null;

        case FRAME_ERROR:
            buffer.skipBytes(buffer.readableBytes());
            return null;

        default:
            throw new Error("Shouldn't reach here.");
        }
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772183343/fstmerge_var2_4083836224017029360


	

    private final int spdyVersion;

	

    private State state;

	
    private SpdySettingsFrame spdySettingsFrame;

	
    private SpdyHeaderBlock spdyHeaderBlock;

	

    // SPDY common header fields
    private byte flags;

	
    private int length;

	
    private int version;

	
    private int type;

	
    private int streamID;

	

    // Header block decoding fields
    private int headerSize;

	
    private int numHeaders;

	
    private ChannelBuffer decompressed;

	

    private static enum  State {
        READ_COMMON_HEADER , 
        READ_CONTROL_FRAME , 
        READ_SETTINGS_FRAME , 
        READ_HEADER_BLOCK_FRAME , 
        READ_HEADER_BLOCK , 
        READ_DATA_FRAME , 
        DISCARD_FRAME , 
        FRAME_ERROR}

	

    /**
     * Creates a new instance with the specified {@code version} and the default
     * {@code maxChunkSize (8192)} and {@code maxHeaderSize (16384)}.
     */
    public SpdyFrameDecoder(int version) {
        this(version, 8192, 16384);
    }

	

    private State readCommonHeader(ChannelBuffer buffer) {
        // Wait until entire header is readable
        if (buffer.readableBytes() < SPDY_HEADER_SIZE) {
            return State.READ_COMMON_HEADER;
        }

        int frameOffset  = buffer.readerIndex();
        int flagsOffset  = frameOffset + SPDY_HEADER_FLAGS_OFFSET;
        int lengthOffset = frameOffset + SPDY_HEADER_LENGTH_OFFSET;
        buffer.skipBytes(SPDY_HEADER_SIZE);

        // Read common header fields
        boolean control = (buffer.getByte(frameOffset) & 0x80) != 0;
        flags  = buffer.getByte(flagsOffset);
        length = getUnsignedMedium(buffer, lengthOffset);

        if (control) {
            // Decode control frame common header
            version = getUnsignedShort(buffer, frameOffset) & 0x7FFF;

            int typeOffset = frameOffset + SPDY_HEADER_TYPE_OFFSET;
            type = getUnsignedShort(buffer, typeOffset);

            // Check version first then validity
            if (version != spdyVersion || !isValidControlFrameHeader()) {
                return State.FRAME_ERROR;
            }

            // Make sure decoder will produce a frame or consume input
            State nextState;
            if (willGenerateControlFrame()) {
                switch (type) {
                case SPDY_SYN_STREAM_FRAME:
                case SPDY_SYN_REPLY_FRAME:
                case SPDY_HEADERS_FRAME:
                    nextState = State.READ_HEADER_BLOCK_FRAME;
                    break;

                case SPDY_SETTINGS_FRAME:
                    nextState = State.READ_SETTINGS_FRAME;
                    break;

                default:
                    nextState = State.READ_CONTROL_FRAME;
                }
            } else if (length != 0) {
                nextState = State.DISCARD_FRAME;
            } else {
                nextState = State.READ_COMMON_HEADER;
            }
            return nextState;
        } else {
            // Decode data frame common header
            streamID = getUnsignedInt(buffer, frameOffset);

            return State.READ_DATA_FRAME;
        }
    }

	

    private Object readControlFrame(ChannelBuffer buffer) {
        int streamID;
        int statusCode;
        switch (type) {
        case SPDY_RST_STREAM_FRAME:
            if (buffer.readableBytes() < 8) {
                return null;
            }

            streamID = getUnsignedInt(buffer, buffer.readerIndex());
            statusCode = getSignedInt(buffer, buffer.readerIndex() + 4);
            buffer.skipBytes(8);

            return new DefaultSpdyRstStreamFrame(streamID, statusCode);

        case SPDY_PING_FRAME:
            if (buffer.readableBytes() < 4) {
                return null;
            }

            int ID = getSignedInt(buffer, buffer.readerIndex());
            buffer.skipBytes(4);

            return new DefaultSpdyPingFrame(ID);

        case SPDY_GOAWAY_FRAME:
            int minLength = version < 3 ? 4 : 8;
            if (buffer.readableBytes() < minLength) {
                return null;
            }

            int lastGoodStreamID = getUnsignedInt(buffer, buffer.readerIndex());
            buffer.skipBytes(4);

            if (version < 3) {
                return new DefaultSpdyGoAwayFrame(lastGoodStreamID);
            }

            statusCode = getSignedInt(buffer, buffer.readerIndex());
            buffer.skipBytes(4);

            return new DefaultSpdyGoAwayFrame(lastGoodStreamID, statusCode);

        case SPDY_WINDOW_UPDATE_FRAME:
            if (buffer.readableBytes() < 8) {
                return null;
            }

            streamID = getUnsignedInt(buffer, buffer.readerIndex());
            int deltaWindowSize = getUnsignedInt(buffer, buffer.readerIndex() + 4);
            buffer.skipBytes(8);

            return new DefaultSpdyWindowUpdateFrame(streamID, deltaWindowSize);

        default:
            throw new Error("Shouldn't reach here.");
        }
    }

	

    private SpdyHeaderBlock readHeaderBlockFrame(ChannelBuffer buffer) {
        int minLength;
        int streamID;
        switch (type) {
        case SPDY_SYN_STREAM_FRAME:
            minLength = version < 3 ? 12 : 10;
            if (buffer.readableBytes() < minLength) {
                return null;
            }

            int offset = buffer.readerIndex();
            streamID = getUnsignedInt(buffer, offset);
            int associatedToStreamID = getUnsignedInt(buffer, offset + 4);
            byte priority = (byte) (buffer.getByte(offset + 8) >> 5 & 0x07);
            if (version < 3) {
                priority >>= 1;
            }
            buffer.skipBytes(10);
            length -= 10;

            // SPDY/2 requires 16-bits of padding for empty header blocks
            if (version < 3 && length == 2 && buffer.getShort(buffer.readerIndex()) == 0) {
                buffer.skipBytes(2);
                length = 0;
            }

            SpdySynStreamFrame spdySynStreamFrame =
                    new DefaultSpdySynStreamFrame(streamID, associatedToStreamID, priority);
            spdySynStreamFrame.setLast((flags & SPDY_FLAG_FIN) != 0);
            spdySynStreamFrame.setUnidirectional((flags & SPDY_FLAG_UNIDIRECTIONAL) != 0);

            return spdySynStreamFrame;

        case SPDY_SYN_REPLY_FRAME:
            minLength = version < 3 ? 8 : 4;
            if (buffer.readableBytes() < minLength) {
                return null;
            }

            streamID = getUnsignedInt(buffer, buffer.readerIndex());
            buffer.skipBytes(4);
            length -= 4;

            // SPDY/2 has 16-bits of unused space
            if (version < 3) {
                buffer.skipBytes(2);
                length -= 2;
            }

            // SPDY/2 requires 16-bits of padding for empty header blocks
            if (version < 3 && length == 2 && buffer.getShort(buffer.readerIndex()) == 0) {
                buffer.skipBytes(2);
                length = 0;
            }

            SpdySynReplyFrame spdySynReplyFrame = new DefaultSpdySynReplyFrame(streamID);
            spdySynReplyFrame.setLast((flags & SPDY_FLAG_FIN) != 0);

            return spdySynReplyFrame;

        case SPDY_HEADERS_FRAME:
            if (buffer.readableBytes() < 4) {
                return null;
            }

            // SPDY/2 allows length 4 frame when there are no name/value pairs
            if (version < 3 && length > 4 && buffer.readableBytes() < 8) {
                return null;
            }

            streamID = getUnsignedInt(buffer, buffer.readerIndex());
            buffer.skipBytes(4);
            length -= 4;

            // SPDY/2 has 16-bits of unused space
            if (version < 3 && length != 0) {
                buffer.skipBytes(2);
                length -= 2;
            }

            // SPDY/2 requires 16-bits of padding for empty header blocks
            if (version < 3 && length == 2 && buffer.getShort(buffer.readerIndex()) == 0) {
                buffer.skipBytes(2);
                length = 0;
            }

            SpdyHeadersFrame spdyHeadersFrame = new DefaultSpdyHeadersFrame(streamID);
            spdyHeadersFrame.setLast((flags & SPDY_FLAG_FIN) != 0);

            return spdyHeadersFrame;

        default:
            throw new Error("Shouldn't reach here.");
        }
    }

	

    private boolean ensureBytes(int bytes) throws Exception {
        if (decompressed.readableBytes() >= bytes) {
            return true;
        }
        // Perhaps last call to decode filled output buffer
        headerBlockDecompressor.decode(decompressed);
        return decompressed.readableBytes() >= bytes;
    }

	

    private int readLengthField() {
        if (version < 3) {
            return decompressed.readUnsignedShort();
        } else {
            return decompressed.readInt();
        }
    }

	

    private void decodeHeaderBlock(ChannelBuffer buffer) throws Exception {
        if (decompressed == null) {
            // First time we start to decode a header block
            // Initialize header block decoding fields
            headerSize = 0;
            numHeaders = -1;
            decompressed = ChannelBuffers.dynamicBuffer(8192);
        }

        // Accumulate decompressed data
        headerBlockDecompressor.setInput(buffer);
        headerBlockDecompressor.decode(decompressed);

        if (spdyHeaderBlock == null) {
            // Only decompressing data to keep decompression context in sync
            decompressed = null;
            return;
        }

        int lengthFieldSize = version < 3 ? 2 : 4;

        if (numHeaders == -1) {
            // Read number of Name/Value pairs
            if (decompressed.readableBytes() < lengthFieldSize) {
                return;
            }
            numHeaders = readLengthField();
            if (numHeaders < 0) {
                spdyHeaderBlock.setInvalid();
                return;
            }
        }

        while (numHeaders > 0) {
            int headerSize = this.headerSize;
            decompressed.markReaderIndex();

            // Try to read length of name
            if (!ensureBytes(lengthFieldSize)) {
                decompressed.resetReaderIndex();
                decompressed.discardReadBytes();
                return;
            }
            int nameLength = readLengthField();

            // Recipients of a zero-length name must issue a stream error
            if (nameLength <= 0) {
                spdyHeaderBlock.setInvalid();
                return;
            }
            headerSize += nameLength;
            if (headerSize > maxHeaderSize) {
                throw new TooLongFrameException(
                        "Header block exceeds " + maxHeaderSize);
            }

            // Try to read name
            if (!ensureBytes(nameLength)) {
                decompressed.resetReaderIndex();
                decompressed.discardReadBytes();
                return;
            }
            byte[] nameBytes = new byte[nameLength];
            decompressed.readBytes(nameBytes);
            String name = new String(nameBytes, "UTF-8");

            // Check for identically named headers
            if (spdyHeaderBlock.containsHeader(name)) {
                spdyHeaderBlock.setInvalid();
                return;
            }

            // Try to read length of value
            if (!ensureBytes(lengthFieldSize)) {
                decompressed.resetReaderIndex();
                decompressed.discardReadBytes();
                return;
            }
            int valueLength = readLengthField();

            // Recipients of illegal value fields must issue a stream error
            if (valueLength <= 0) {
                spdyHeaderBlock.setInvalid();
                return;
            }
            headerSize += valueLength;
            if (headerSize > maxHeaderSize) {
                throw new TooLongFrameException(
                        "Header block exceeds " + maxHeaderSize);
            }

            // Try to read value
            if (!ensureBytes(valueLength)) {
                decompressed.resetReaderIndex();
                decompressed.discardReadBytes();
                return;
            }
            byte[] valueBytes = new byte[valueLength];
            decompressed.readBytes(valueBytes);

            // Add Name/Value pair to headers
            int index = 0;
            int offset = 0;
            while (index < valueLength) {
                while (index < valueBytes.length && valueBytes[index] != (byte) 0) {
                    index ++;
                }
                if (index < valueBytes.length && valueBytes[index + 1] == (byte) 0) {
                    // Received multiple, in-sequence NULL characters
                    // Recipients of illegal value fields must issue a stream error
                    spdyHeaderBlock.setInvalid();
                    return;
                }
                String value = new String(valueBytes, offset, index - offset, "UTF-8");

                try {
                    spdyHeaderBlock.addHeader(name, value);
                } catch (IllegalArgumentException e) {
                    // Name contains NULL or non-ascii characters
                    spdyHeaderBlock.setInvalid();
                    return;
                }
                index ++;
                offset = index;
            }
            numHeaders --;
            this.headerSize = headerSize;
        }
        decompressed = null;
    }

	

    private boolean isValidControlFrameHeader() {
        switch (type) {
        case SPDY_SYN_STREAM_FRAME:
            return version < 3 ? length >= 12 : length >= 10;

        case SPDY_SYN_REPLY_FRAME:
            return version < 3 ? length >= 8 : length >= 4;

        case SPDY_RST_STREAM_FRAME:
            return flags == 0 && length == 8;

        case SPDY_SETTINGS_FRAME:
            return length >= 4;

        case SPDY_NOOP_FRAME:
            return length == 0;

        case SPDY_PING_FRAME:
            return length == 4;

        case SPDY_GOAWAY_FRAME:
            return version < 3 ? length == 4 : length == 8;

        case SPDY_HEADERS_FRAME:
            if (version < 3) {
                return length == 4 || length >= 8;
            } else {
                return length >= 4;
            }

        case SPDY_WINDOW_UPDATE_FRAME:
            return length == 8;

        case SPDY_CREDENTIAL_FRAME:
        default:
            return true;
        }
    }

	

    private boolean willGenerateControlFrame() {
        switch (type) {
        case SPDY_SYN_STREAM_FRAME:
        case SPDY_SYN_REPLY_FRAME:
        case SPDY_RST_STREAM_FRAME:
        case SPDY_SETTINGS_FRAME:
        case SPDY_PING_FRAME:
        case SPDY_GOAWAY_FRAME:
        case SPDY_HEADERS_FRAME:
        case SPDY_WINDOW_UPDATE_FRAME:
            return true;

        case SPDY_NOOP_FRAME:
        case SPDY_CREDENTIAL_FRAME:
        default:
            return false;
        }
    }

	

    private void fireInvalidControlFrameException(ChannelHandlerContext ctx) {
        String message = "Received invalid control frame";
        switch (type) {
        case SPDY_SYN_STREAM_FRAME:
            message = "Received invalid SYN_STREAM control frame";
            break;

        case SPDY_SYN_REPLY_FRAME:
            message = "Received invalid SYN_REPLY control frame";
            break;

        case SPDY_RST_STREAM_FRAME:
            message = "Received invalid RST_STREAM control frame";
            break;

        case SPDY_SETTINGS_FRAME:
            message = "Received invalid SETTINGS control frame";
            break;

        case SPDY_NOOP_FRAME:
            message = "Received invalid NOOP control frame";
            break;

        case SPDY_PING_FRAME:
            message = "Received invalid PING control frame";
            break;

        case SPDY_GOAWAY_FRAME:
            message = "Received invalid GOAWAY control frame";
            break;

        case SPDY_HEADERS_FRAME:
            message = "Received invalid HEADERS control frame";
            break;

        case SPDY_WINDOW_UPDATE_FRAME:
            message = "Received invalid WINDOW_UPDATE control frame";
            break;

        case SPDY_CREDENTIAL_FRAME:
            message = "Received invalid CREDENTIAL control frame";
            break;
        }
        fireProtocolException(ctx, message);
    }

	

    private static void fireProtocolException(ChannelHandlerContext ctx, String message) {
        Channels.fireExceptionCaught(ctx, new SpdyProtocolException(message));
    }


}
