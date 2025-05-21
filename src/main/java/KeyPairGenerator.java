import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class KeyPairGenerator {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // Get the RSA key pair generator
        java.security.KeyPairGenerator keyPairGenerator = java.security.KeyPairGenerator.getInstance("RSA");
        // Initialize the generator with a key size (e.g., 2048 bits) and a secure random number generator
        keyPairGenerator.initialize(2048, new SecureRandom());
        // Generate the key pair
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        System.out.println("Generated Public Key: " + Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        System.out.println("Generated Private Key: " + Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
    }
}