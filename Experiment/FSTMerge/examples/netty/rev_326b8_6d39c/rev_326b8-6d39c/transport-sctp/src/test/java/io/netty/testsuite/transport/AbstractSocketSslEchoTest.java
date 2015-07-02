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
package io.netty.testsuite.transport; 

import io.netty.bootstrap.ClientBootstrap; 
import io.netty.bootstrap.ServerBootstrap; 
import io.netty.buffer.ChannelBuffer; 
import io.netty.buffer.ChannelBuffers; 
import io.netty.channel.*; 
import io.netty.channel.sctp.codec.SctpFrameDecoder; 
import io.netty.channel.sctp.codec.SctpFrameEncoder; 
import io.netty.handler.execution.ExecutionHandler; 
import io.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor; 
import io.netty.handler.ssl.SslHandler; 
import io.netty.logging.InternalLogger; 
import io.netty.logging.InternalLoggerFactory; 
import io.netty.testsuite.util.SctpTestUtil; 
import io.netty.util.internal.ExecutorUtil; 
import org.junit.AfterClass; 
import org.junit.Assume; 
import org.junit.BeforeClass; 
import org.junit.Test; 

import javax.net.ssl.*; 
import java.io.ByteArrayInputStream; 
import java.io.IOException; 
import java.io.InputStream; 
import java.net.InetSocketAddress; 
import java.security.InvalidAlgorithmParameterException; 
import java.security.KeyStore; 
import java.security.KeyStoreException; 
import java.security.Security; 
import java.security.cert.CertificateException; 
import java.security.cert.X509Certificate; 
import java.util.Random; 
import java.util.concurrent.Executor; 
import java.util.concurrent.ExecutorService; 
import java.util.concurrent.Executors; 
import java.util.concurrent.atomic.AtomicReference; 

import static org.junit.Assert.assertEquals; 
import static org.junit.Assert.assertTrue; 

  class  AbstractSocketSslEchoTest {
	
    

	

    

	
    

	//could not test jumbo frames

    

	
    

	

    static {
        random.nextBytes(data);
    }

	

    

	

    

	

    

	
    

	

    

	

    

	

      class  EchoHandler  extends SimpleChannelUpstreamHandler {
		
        

		
        

		
        

		
        

		

        

		

        

		

        

		

        


	}

	

      class  BogusSslContextFactory {
		

        

		
        

		
        

		

        static {
            String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
            if (algorithm == null) {
                algorithm = "SunX509";
            }

            SSLContext serverContext = null;
            SSLContext clientContext = null;
            try {
                KeyStore ks = KeyStore.getInstance("JKS");
                ks.load(BogusKeyStore.asInputStream(),
                        BogusKeyStore.getKeyStorePassword());

                // Set up key manager factory to use our key store
                KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
                kmf.init(ks, BogusKeyStore.getCertificatePassword());

                // Initialize the SSLContext to work with our key managers.
                serverContext = SSLContext.getInstance(PROTOCOL);
                serverContext.init(kmf.getKeyManagers(), null, null);
            } catch (Exception e) {
                throw new Error(
                        "Failed to initialize the server-side SSLContext", e);
            }

            try {
                clientContext = SSLContext.getInstance(PROTOCOL);
                clientContext.init(null, BogusTrustManagerFactory.getTrustManagers(), null);
            } catch (Exception e) {
                throw new Error(
                        "Failed to initialize the client-side SSLContext", e);
            }

            SERVER_CONTEXT = serverContext;
            CLIENT_CONTEXT = clientContext;
        }

		

        

		

        

		

        static {
            String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
            if (algorithm == null) {
                algorithm = "SunX509";
            }

            SSLContext serverContext = null;
            SSLContext clientContext = null;
            try {
                KeyStore ks = KeyStore.getInstance("JKS");
                ks.load(BogusKeyStore.asInputStream(),
                        BogusKeyStore.getKeyStorePassword());

                // Set up key manager factory to use our key store
                KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
                kmf.init(ks, BogusKeyStore.getCertificatePassword());

                // Initialize the SSLContext to work with our key managers.
                serverContext = SSLContext.getInstance(PROTOCOL);
                serverContext.init(kmf.getKeyManagers(), null, null);
            } catch (Exception e) {
                throw new Error(
                        "Failed to initialize the server-side SSLContext", e);
            }

            try {
                clientContext = SSLContext.getInstance(PROTOCOL);
                clientContext.init(null, BogusTrustManagerFactory.getTrustManagers(), null);
            } catch (Exception e) {
                throw new Error(
                        "Failed to initialize the client-side SSLContext", e);
            }

            SERVER_CONTEXT = serverContext;
            CLIENT_CONTEXT = clientContext;
        }


	}

	

    /**
     * Bogus {@link javax.net.ssl.TrustManagerFactorySpi} which accepts any certificate
     * even if it is invalid.
     */
      class  BogusTrustManagerFactory  extends TrustManagerFactorySpi {
		

        ~~FSTMerge~~ private static final TrustManager DUMMY_TRUST_MANAGER = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            @Override
            public void checkClientTrusted(
                    X509Certificate[] chain, String authType) throws CertificateException {
                // Always trust - it is an example.
                // You should do something in the real world.
                // You will reach here only if you enabled client certificate auth,
                // as described in SecureChatSslContextFactory.
                logger.error("UNKNOWN CLIENT CERTIFICATE: " + chain[0].getSubjectDN());
            }

            @Override
            public void checkServerTrusted(
                    X509Certificate[] chain, String authType) throws CertificateException {
                // Always trust - it is an example.
                // You should do something in the real world.
                logger.error("UNKNOWN SERVER CERTIFICATE: " + chain[0].getSubjectDN());
            }
        }; ##FSTMerge## private static final TrustManager DUMMY_TRUST_MANAGER = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            @Override
            public void checkClientTrusted(
                    X509Certificate[] chain, String authType) throws CertificateException {
                // Always trust - it is an example.
                // You should do something in the real world.
                // You will reach here only if you enabled client certificate auth,
                // as described in SecureChatSslContextFactory.
                System.err.println(
                        "UNKNOWN CLIENT CERTIFICATE: " + chain[0].getSubjectDN());
            }

            @Override
            public void checkServerTrusted(
                    X509Certificate[] chain, String authType) throws CertificateException {
                // Always trust - it is an example.
                // You should do something in the real world.
                System.err.println(
                        "UNKNOWN SERVER CERTIFICATE: " + chain[0].getSubjectDN());
            }
        }; ##FSTMerge##

		

        

		

        

		

        

		

        


	}

	

    /**
     * A bogus key store which provides all the required information to
     * create an example SSL connection.
     *
     * To generate a bogus key store:
     * <pre>
     * keytool  -genkey -alias bogus -keysize 2048 -validity 36500
     *          -keyalg RSA -dname "CN=bogus"
     *          -keypass secret -storepass secret
     *          -keystore cert.jks
     * </pre>
     */
      class  BogusKeyStore {
		
        

		

        

		

        

		

        

		

        


	}

	

    static {
        random.nextBytes(data);
    }


}
