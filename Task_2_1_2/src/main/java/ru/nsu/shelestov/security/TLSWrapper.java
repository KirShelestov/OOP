package ru.nsu.shelestov.security;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.logging.Logger;

public class TLSWrapper {
    private static final Logger logger = Logger.getLogger(TLSWrapper.class.getName());
    private final SSLContext sslContext;

    public TLSWrapper(String keystorePath, String keystorePassword) throws Exception {
        this.sslContext = createSSLContext(keystorePath, keystorePassword);
    }

    public SSLServerSocket createServerSocket(int port) throws IOException {
        SSLServerSocketFactory factory = sslContext.getServerSocketFactory();
        return (SSLServerSocket) factory.createServerSocket(port);
    }

    public SSLSocket createClientSocket(String host, int port) throws IOException {
        SSLSocketFactory factory = sslContext.getSocketFactory();
        return (SSLSocket) factory.createSocket(host, port);
    }

    private SSLContext createSSLContext(String keystorePath, String keystorePassword) 
            throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(keystorePath)) {
            keyStore.load(fis, keystorePassword.toCharArray());
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, keystorePassword.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return context;
    }
}