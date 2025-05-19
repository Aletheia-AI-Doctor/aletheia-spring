package dev.aletheia.doctor.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class DigitalSignService {
    @Value("${signature.public}")
    private String publicKey;

    @Value("${signature.private}")
    private String privateKey;

    private KeyPair getKeyPair() throws NoSuchAlgorithmException, InvalidKeySpecException {

        return new KeyPair(
                KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey))),
                KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey))));
    }

    public String signData(String data) throws Exception {
        KeyPair keyPair = getKeyPair();
        Signature sig = Signature.getInstance("SHA1WithRSA");
        sig.initSign(keyPair.getPrivate());
        sig.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = sig.sign();
        return new String(signatureBytes, StandardCharsets.UTF_8);
    }

    public boolean verifySignature(String data, String signature) throws Exception {
        KeyPair keyPair = getKeyPair();
        Signature sig = Signature.getInstance("SHA1WithRSA");
        sig.initVerify(keyPair.getPublic());
        sig.update(data.getBytes(StandardCharsets.UTF_8));
        return sig.verify(signature.getBytes(StandardCharsets.UTF_8));
    }
}