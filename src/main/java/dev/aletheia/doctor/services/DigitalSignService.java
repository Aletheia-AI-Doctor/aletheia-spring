package dev.aletheia.doctor.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

@Service
public class DigitalSignService {
    @Value("${signature.public}")
    private String publicKeyPem;

    @Value("${signature.private}")
    private String privateKeyPem;

    private KeyPair getKeyPair() throws GeneralSecurityException {
        byte[] pubBytes = Base64.getDecoder().decode(publicKeyPem);
        byte[] privBytes = Base64.getDecoder().decode(privateKeyPem);

        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pub = kf.generatePublic(new X509EncodedKeySpec(pubBytes));
        PrivateKey priv = kf.generatePrivate(new PKCS8EncodedKeySpec(privBytes));

        return new KeyPair(pub, priv);
    }

    public String signData(String data) throws GeneralSecurityException {
        KeyPair kp = getKeyPair();
        Signature signer = Signature.getInstance("SHA1withRSA");
        signer.initSign(kp.getPrivate());
        // sign the raw bytes, not Base64‑encoded
        signer.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] sigBytes = signer.sign();
        return Base64.getUrlEncoder().encodeToString(sigBytes);
    }

    public boolean verifySignature(String data, String signature) throws GeneralSecurityException {
        KeyPair kp = getKeyPair();
        Signature verifier = Signature.getInstance("SHA1withRSA");
        verifier.initVerify(kp.getPublic());
        // verify against the same raw bytes
        verifier.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] sigBytes = Base64.getUrlDecoder().decode(signature);
        return verifier.verify(sigBytes);
    }
}