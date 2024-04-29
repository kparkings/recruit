package com.arenella.recruit.configs;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
* Configuration for connecting to the Elasticsearch instance
* @author Hp
*/
@Configuration
@EnableElasticsearchRepositories(basePackages = "github.io.truongbn.elasticsearch.repository")
public class ElasticSearchConfig extends ElasticsearchConfiguration {

	@Value("${elasticsearch-use-ssl}")
	private boolean useSSL;
	
	@Value("${elasticsearch-url}")
	private String url;
	
	@Value("${elasticsearch-ca-cert-path}")
	private String caCertPath;
	
	@Value("${elasticsearch-username}")
	private String username;
	
	@Value("${elasticsearch-password}")
	private String password;
	
	/**
	* Refer to ElasticsearchConfiguration 
	*/
    @Override
    public ClientConfiguration clientConfiguration() {
    	
    	if (!useSSL) {
			return ClientConfiguration
						.builder()
							.connectedTo(url)
						.build();
		}
    	
    	try {
	        return ClientConfiguration.builder()
	                .connectedTo(url)
	                .usingSsl(getSSLContext())
	                .withBasicAuth(username, password)
	                .build();
	  }catch(Exception e) {
    		throw new RuntimeException(e);
    	}
    }
    
    /**
    * Reads and creates CA certificate for the Elasticsearch instance from 
    * the local server
    * @return CA Certificate
    */
    private Certificate readCACertFromElasticsearch() {
    	
    	Certificate ca;
    	
    	try {
    		
    		final String hdr = "-----BEGIN CERTIFICATE-----";
    		final String ftr = "-----END CERTIFICATE-----";
    		List<String> certContentLines = Files.readAllLines(Paths.get(caCertPath));
    		String certContent = String.join("",certContentLines);
    		
    		certContent = certContent.replaceAll(hdr, "");
    		certContent = certContent.replaceAll(ftr, "");
    		certContent = certContent.trim();
    		
    		byte[] decode = java.util.Base64.getDecoder().decode(certContent.getBytes());
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
          
            
            try (InputStream certificateInputStream = new ByteArrayInputStream(decode)) {
                ca = cf.generateCertificate(certificateInputStream);
            }
           
    	}catch(Exception e) {
    		throw new RuntimeException(e);
    	}
    	
    	return ca;
    }

    /**
    * Creates SSL Context to connect to Elasticsearch when 
    * security enables
    * @return SSLContext
    * @throws Exception
    */
    private SSLContext getSSLContext() throws Exception{
    	
    	Certificate ca = readCACertFromElasticsearch();
    	
        String 		keyStoreType 	= KeyStore.getDefaultType();
        KeyStore 	keyStore 		= KeyStore.getInstance(keyStoreType);
        
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String 					tmfAlgorithm 	= TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory 	tmf 			= TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);
        return context;
        
    }
	
}