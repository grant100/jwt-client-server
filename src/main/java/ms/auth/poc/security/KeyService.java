package ms.auth.poc.security;

import com.auth0.jwt.algorithms.Algorithm;
import ms.auth.poc.AuthnServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;
import java.util.Date;

@Service
public class KeyService {
    private KeyProperties keyProperties;
    private AuthnServerProperties authnServerProperties;

    @Autowired
    public KeyService(KeyProperties keyProperties, AuthnServerProperties authnServerProperties) {
        this.keyProperties = keyProperties;
        this.authnServerProperties = authnServerProperties;
    }

    public KeyPair getKeyPair() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
        String alias = keyProperties.getAlias();
        String password = keyProperties.getPassword();
        String keystorePath = keyProperties.getKeystorePath();

        FileInputStream is = new FileInputStream(keystorePath);

        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(is, password.toCharArray());

        Key key = keystore.getKey(alias, password.toCharArray());

        KeyPair keyPair = null;
        if (key instanceof PrivateKey) {
            Certificate cert = keystore.getCertificate(alias);
            keyPair = new KeyPair((RSAPublicKey) cert.getPublicKey(), (RSAPrivateKey) key);
        }
        return keyPair;
    }

    public String getClientCredential(){
        String token = null;
        try {
            Calendar now = Calendar.getInstance();
            long time = now.getTimeInMillis();
            Date expiry = new Date(time + (1800 * 1000));
            //Algorithm algorithm = Algorithm.HMAC256("j98Nbf765bdiwD5ngks829450ykh287f7vydhGDS1");
            KeyPair keyPair = getKeyPair();
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey)keyPair.getPublic(),(RSAPrivateKey)keyPair.getPrivate());
            token = com.auth0.jwt.JWT
                    .create()
                    .withIssuer("client.node")
                    .withIssuedAt(new Date())
                    .withAudience(authnServerProperties.getAudience())
                    .withExpiresAt(expiry)
                    .sign(algorithm);
        } catch (Exception uee) {
            //
        }
        return token;
    }
}
