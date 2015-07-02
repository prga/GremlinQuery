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

package io.netty.channel.socket.http; 

import java.net.InetAddress; 
import java.net.InetSocketAddress; 
import java.net.UnknownHostException; 
import java.util.Random; 
import java.util.concurrent.CountDownLatch; 
import java.util.concurrent.ExecutorService; 
import java.util.concurrent.Executors; 
import java.util.concurrent.ScheduledExecutorService; 
import java.util.concurrent.TimeUnit; 
import java.util.concurrent.atomic.AtomicBoolean; 
import java.util.concurrent.atomic.AtomicInteger; 
import java.util.concurrent.atomic.AtomicReference; 
import java.util.logging.Level; 
import java.util.logging.Logger; 

import io.netty.bootstrap.ClientBootstrap; 
import io.netty.bootstrap.ServerBootstrap; 
import io.netty.buffer.ChannelBuffer; 
import io.netty.buffer.ChannelBuffers; 
import io.netty.channel.Channel; 
import io.netty.channel.ChannelFuture; 
import io.netty.channel.ChannelHandlerContext; 
import io.netty.channel.ChannelPipeline; 
import io.netty.channel.ChannelPipelineFactory; 
import io.netty.channel.ChannelStateEvent; 
import io.netty.channel.Channels; 
import io.netty.channel.MessageEvent; 
import io.netty.channel.SimpleChannelUpstreamHandler; 
import io.netty.channel.group.ChannelGroup; 
import io.netty.channel.group.DefaultChannelGroup; 
import io.netty.channel.socket.ClientSocketChannelFactory; 
import io.netty.channel.socket.ServerSocketChannelFactory; 
import io.netty.channel.socket.SocketChannel; 
import io.netty.channel.socket.nio.NioClientSocketChannelFactory; 
import io.netty.channel.socket.nio.NioServerSocketChannelFactory; 
import io.netty.logging.InternalLogger; 
import io.netty.logging.InternalLoggerFactory; 

/**
 * Tests HTTP tunnel soaking
 */
  class  HttpTunnelSoakTester {
	

    

	

    static final Logger LOG = Logger.getLogger(HttpTunnelSoakTester.class
            .getName());

	

    

	

    

	

    

	

    

	

    

	

    

	

    

	

    

	

    

	

    

	

    

	

    

	

    static {
        SEND_STREAM = new byte[MAX_WRITE_SIZE + 127];
        for (int i = 0; i < SEND_STREAM.length; i ++) {
            SEND_STREAM[i] = (byte) (i % 127);
        }
    }

	

    

	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772203982/fstmerge_var1_4275880019209793439
private void configureProxy() {
        String proxyHost = System.getProperty("http.proxyHost");
        if (proxyHost != null && proxyHost.length() != 0) {
            int proxyPort = Integer.getInteger("http.proxyPort", 80);
            InetAddress chosenAddress = chooseAddress(proxyHost);
            InetSocketAddress proxyAddress =
                    new InetSocketAddress(chosenAddress, proxyPort);
            if (!proxyAddress.isUnresolved()) {
                clientBootstrap.setOption(
                        HttpTunnelClientChannelConfig.PROXY_ADDRESS_OPTION,
                        proxyAddress);
                logger.info("Using " + proxyAddress +
                        " as a proxy for this test run");
            } else {
                logger.error("Failed to resolve proxy address " +
                        proxyAddress);
            }
        } else {
            logger.info("No proxy specified, will connect to server directly");
        }
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772203982/fstmerge_var2_1646887213206961559


	

    

	

    

	

    

	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772204181/fstmerge_var1_8836508341971581424
public void run() throws InterruptedException {
        logger.info("binding server channel");
        Channel serverChannel =
                serverBootstrap.bind(new InetSocketAddress(SERVER_PORT));
        channels.add(serverChannel);
        logger.info(String.format("server channel bound to {0}",
                serverChannel.getLocalAddress()));

        SocketChannel clientChannel = createClientChannel();
        if (clientChannel == null) {
            logger.error("no client channel - bailing out");
            return;
        }

        channels.add(clientChannel);
        c2sDataSender.setChannel(clientChannel);

        executor.execute(c2sDataSender);

        if (!c2sDataSender.waitForFinish(5, TimeUnit.MINUTES)) {
            logger.error("Data send from client to server failed");
        }

        if (!s2cDataSender.waitForFinish(5, TimeUnit.MINUTES)) {
            logger.error("Data send from server to client failed");
        }

        logger.info("Waiting for verification to complete");
        if (!c2sVerifier.waitForCompletion(30L, TimeUnit.SECONDS)) {
            logger.warn("Timed out waiting for verification of client-to-server stream");
        }

        if (!s2cVerifier.waitForCompletion(30L, TimeUnit.SECONDS)) {
            logger.warn("Timed out waiting for verification of server-to-client stream");
        }

        logger.info("closing client channel");
        closeChannel(clientChannel);
        logger.info("server channel status: " +
                (serverChannel.isOpen()? "open" : "closed"));
        logger.info("closing server channel");
        closeChannel(serverChannel);
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772204181/fstmerge_var2_453299517234615731


	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772204231/fstmerge_var1_8989833641458417941
private void closeChannel(Channel channel) {
        try {
            if (!channel.close().await(5L, TimeUnit.SECONDS)) {
                logger.warn("Failed to close connection within reasonable amount of time");
            }
        } catch (InterruptedException e) {
            logger.error("Interrupted while closing connection");
        }

    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772204231/fstmerge_var2_6685366217886742221


	

    <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772204282/fstmerge_var1_4103902728531341092
private SocketChannel createClientChannel() {
        InetSocketAddress serverAddress =
                new InetSocketAddress("localhost", SERVER_PORT);
        ChannelFuture clientChannelFuture =
                clientBootstrap.connect(serverAddress);
        try {
            if (!clientChannelFuture.await(1000, TimeUnit.MILLISECONDS)) {
                logger.error("did not connect within acceptable time period");
                return null;
            }
        } catch (InterruptedException e) {
            logger.error("Interrupted while waiting for client connect to be established");
            return null;
        }

        if (!clientChannelFuture.isSuccess()) {
            logger.error("did not connect successfully",
                    clientChannelFuture.getCause());
            return null;
        }

        HttpTunnelClientChannelConfig config =
                (HttpTunnelClientChannelConfig) clientChannelFuture
                .getChannel().getConfig();
        config.setWriteBufferHighWaterMark(2 * 1024 * 1024);
        config.setWriteBufferLowWaterMark(1024 * 1024);

        return (SocketChannel) clientChannelFuture.getChannel();
    }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772204282/fstmerge_var2_8907746894922960345


	

    

	

    

	

    

	

      class  DataVerifier  extends SimpleChannelUpstreamHandler {
		
        

		

        

		

        

		

        

		

        

		

        <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772204535/fstmerge_var1_6821074260105808641
@Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
                throws Exception {
            ChannelBuffer bytesToVerify = (ChannelBuffer) e.getMessage();

            while (bytesToVerify.readable()) {
                byte readByte = bytesToVerify.readByte();
                if (readByte != expectedNext) {
                    logger.error(String.format(
                            "{0}: received a byte out of sequence. Expected {1}, got {2}",
                            new Object[] { name, expectedNext, readByte }));
                    System.exit(-1);
                    return;
                }

                expectedNext = (expectedNext + 1) % 127;
                verifiedBytes ++;
            }

            if (verifiedBytes >= BYTES_TO_SEND) {
                completionLatch.countDown();
            }
        }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772204535/fstmerge_var2_5457245411951081681


		

        

		

        


	}

	

      class  SendThrottle  extends SimpleChannelUpstreamHandler {
		
        

		

        

		

        


	}

	

      class  DataSender   {
		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        

		

        <<<<<<< /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772204950/fstmerge_var1_6952877316651084472
@Override
        public void run() {
            if (!running.compareAndSet(false, true)) {
                logger.warn(String.format(
                        "{0}: Attempt made to run duplicate sender!", name));
                return;
            }

            if (finishLatch.getCount() == 0) {
                logger.error(String.format(
                        "{0}: Attempt made to run after completion!", name));
            }

            if (firstRun) {
                firstRun = false;
                runStartTime = System.currentTimeMillis();
                logger.info(String.format("{0}: sending data", name));
            }

            while (totalBytesSent < BYTES_TO_SEND) {
                if (!writeEnabled.get()) {
                    running.set(false);
                    return;
                }

                ChannelBuffer randomBytesForSend =
                        createRandomSizeBuffer(nextWriteByte);
                totalBytesSent += randomBytesForSend.readableBytes();

                channel.get().write(
                        ChannelBuffers.wrappedBuffer(randomBytesForSend));

                numWrites ++;
                if (numWrites % 100 == 0) {
                    logger.info(String.format(
                            "{0}: {1} writes dispatched, totalling {2} bytes",
                            new Object[] { name, numWrites, totalBytesSent }));
                }
            }

            logger.info(String.format("{0}: completed send cycle", name));

            long runEndTime = System.currentTimeMillis();
            long totalTime = runEndTime - runStartTime;
            long totalKB = totalBytesSent / 1024;
            double rate = totalKB / (totalTime / 1000.0);
            logger.info(String.format("{0}: Sent {1} bytes", new Object[] { name,
                    totalBytesSent }));
            logger.info(String.format("{0}: Average throughput: {1} KB/s",
                    new Object[] { name, rate }));

            finishLatch.countDown();
            running.set(false);
        }
=======
>>>>>>> /mnt/Vbox/FSTMerge/binary/fstmerge_tmp1390772204950/fstmerge_var2_1887602865573094362


		

        

		

        


	}

	

    private static final InternalLogger logger =
        InternalLoggerFactory.getInstance(HttpTunnelSoakTester.class);

	

    static {
        SEND_STREAM = new byte[MAX_WRITE_SIZE + 127];
        for (int i = 0; i < SEND_STREAM.length; i ++) {
            SEND_STREAM[i] = (byte) (i % 127);
        }
    }


}
