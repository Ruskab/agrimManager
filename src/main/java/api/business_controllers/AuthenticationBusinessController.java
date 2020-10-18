package api.business_controllers;

import api.daos.DaoFactory;
import api.daos.DaoSupplier;
import api.dtos.MechanicDto;
import api.exceptions.NotFoundException;
import front.util.PropertyLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;


public class AuthenticationBusinessController {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationBusinessController.class);

    private static final SecureRandom RAND = new SecureRandom();

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";


    static {
        DaoFactory.setFactory(DaoSupplier.HIBERNATE.createFactory());
    }

    private final MechanicBusinessController mechanicBusinessController = new MechanicBusinessController();

    public String authenticateCredentials(String username, String password) {
        String salt = new PropertyLoader().loadPropertiesFile("config.properties").getProperty("api.salt");
        LOGGER.error("loaded salt: {}", salt);
        List<MechanicDto> mechanics = mechanicBusinessController.searchBy(username);
        MechanicDto mechanic = mechanics.get(0);
        if (!verifyPassword(password, mechanic.getPassword(), salt)) {
            LOGGER.error("Invalid credentials");
            throw NotFoundException.throwBecauseOf("Invalid credentials");
        }
        LOGGER.error("Valid credentials");
        return mechanic.getPassword();
    }

    protected boolean verifyPassword (String password, String key, String salt) {
        Optional<String> optEncrypted = hashPassword(password, salt);
        return optEncrypted.map(s -> s.equals(key)).orElse(false);
    }

    public static Optional<String> hashPassword (String password, String salt) {

        char[] chars = password.toCharArray();
        byte[] bytes = salt.getBytes();

        PBEKeySpec spec = new PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH);

        Arrays.fill(chars, Character.MIN_VALUE);

        try {
            SecretKeyFactory fac = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] securePassword = fac.generateSecret(spec).getEncoded();
            return Optional.of(Base64.getEncoder().encodeToString(securePassword));

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            System.err.println("Exception encountered in hashPassword()");
            return Optional.empty();

        } finally {
            spec.clearPassword();
        }
    }

    public static Optional<String> generateSalt (final int length) {

        if (length < 1) {
            System.err.println("error in generateSalt: length must be > 0");
            return Optional.empty();
        }

        byte[] salt = new byte[length];
        RAND.nextBytes(salt);

        return Optional.of(Base64.getEncoder().encodeToString(salt));
    }



}
