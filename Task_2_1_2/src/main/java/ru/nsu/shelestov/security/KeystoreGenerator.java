package ru.nsu.shelestov.security;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

public class KeystoreGenerator {
    public static void generateKeystore(String keystorePath, String keystorePassword, 
            String alias) throws Exception {
        
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, null);
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();

        
        X500Name issuerName = new X500Name("CN=TaskServer");
        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 365 * 24 * 60 * 60 * 1000L);

        X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
            issuerName,
            serialNumber,
            startDate,
            endDate,
            issuerName,
            pair.getPublic()
        );

      
        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA")
            .build(pair.getPrivate());
        X509Certificate cert = new JcaX509CertificateConverter()
            .getCertificate(certBuilder.build(signer));

        keyStore.setKeyEntry(alias, pair.getPrivate(), 
            keystorePassword.toCharArray(), 
            new Certificate[]{cert});

        try (FileOutputStream fos = new FileOutputStream(keystorePath)) {
            keyStore.store(fos, keystorePassword.toCharArray());
        }
    }
}