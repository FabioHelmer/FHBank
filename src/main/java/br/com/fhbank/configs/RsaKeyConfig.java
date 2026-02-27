package br.com.fhbank.configs;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class RsaKeyConfig {

    @Value("${spring.security.jwt.private-key}")
    private Resource privateKey;

    @Value("${spring.security.jwt.public-key}")
    private Resource publicKey;

    @Bean
    RSAPrivateKey rsaPrivateKey() throws Exception {
        return (RSAPrivateKey) PemUtils.readPrivateKey(privateKey.getInputStream());
    }

    @Bean
    RSAPublicKey rsaPublicKey() throws Exception {
        return (RSAPublicKey) PemUtils.readPublicKey(publicKey.getInputStream());
    }
}
