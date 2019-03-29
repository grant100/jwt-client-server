package ms.auth.poc.services;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import ms.auth.poc.AuthnServerProperties;
import ms.auth.poc.security.KeyProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
public class AuthnService {
    private String idToken;
    private String clientCredential;
    private KeyProperties keyProperties;
    private AuthnServerProperties authnServerProperties;

    public AuthnService(KeyProperties keyProperties, AuthnServerProperties authnServerProperties) {
        this.keyProperties = keyProperties;
        this.authnServerProperties = authnServerProperties;
    }

    public String loadIdToken() {
        if (isExpired(this.clientCredential)) {
            getClientCredential();
        }

        if (isExpired(this.idToken)) {
            getIdToken();
        }
        return this.idToken;
    }

    public String getClientCredential() {
        try {
            Calendar now = Calendar.getInstance();
            long time = now.getTimeInMillis();
            Date expiry = new Date(time + (1800 * 1000));
            KeyPair keyPair = getKeyPair();
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
            this.clientCredential = com.auth0.jwt.JWT
                    .create()
                    .withIssuer("client.node")
                    .withIssuedAt(new Date())
                    .withAudience(authnServerProperties.getAudience())
                    .withExpiresAt(expiry)
                    .sign(algorithm);
        } catch (Exception uee) {
            //
        }
        return clientCredential;
    }

    public String getIdToken() {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + this.clientCredential);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = template.exchange(authnServerProperties.getTokenUrl(), HttpMethod.POST, entity, String.class);
        this.idToken = response.getBody();
        return this.idToken;
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

    public boolean isExpired(String token) {
        if (isNullOrEmpty(token)) {
            return true;
        }

        DecodedJWT decodedJWT = com.auth0.jwt.JWT.decode(token);
        return decodedJWT.getExpiresAt().before(new Date());
    }

    public boolean isNullOrEmpty(String token) {
        return token == null || token.isEmpty();
    }
}
