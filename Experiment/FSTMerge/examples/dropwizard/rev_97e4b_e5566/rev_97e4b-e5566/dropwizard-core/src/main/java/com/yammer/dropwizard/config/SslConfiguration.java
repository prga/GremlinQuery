package com.yammer.dropwizard.config; 
import org.codehaus.jackson.annotate.JsonProperty; 
import org.hibernate.validator.constraints.NotEmpty; 
import com.google.common.base.Optional; 
import com.google.common.collect.ImmutableList; 

import com.fasterxml.jackson.annotation.JsonProperty; 

import java.io.File; 
import java.util.List; 

@SuppressWarnings("UnusedDeclaration")
public  class  SslConfiguration {
	
    

	

    @JsonProperty
    private String keyStorePassword = null;

	

    @JsonProperty
    private String keyManagerPassword = null;

	

    @JsonProperty
    private String keyStoreType = "JKS";

	

    @JsonProperty
    protected String trustStorePath = null;

	
    
    @JsonProperty
    protected String trustStorePassword = null;

	
    
    @JsonProperty
    private String trustStoreType = "JKS";

	
    
    @JsonProperty
    private Boolean needClientAuth = false;

	

    @NotEmpty
    @JsonProperty
    private ImmutableList<String> supportedProtocols = ImmutableList.of("SSLv3",
                                                                        "TLSv1",
                                                                        "TLSv1.1",
                                                                        "TLSv1.2");

	

    

	

    public Optional<String> getKeyStorePassword() {
        return Optional.fromNullable(keyStorePassword);
    }


	

    public Optional<String> getKeyManagerPassword() {
        return Optional.fromNullable(keyManagerPassword);
    }


	

    public Optional<String> getKeyStoreType() {
        return Optional.fromNullable(keyStoreType);
    }


	

    public Optional<String> getTrustStorePath() {
        return Optional.fromNullable(trustStorePath);
    }

	
    
    public Optional<String> getTrustStorePassword() {
        return Optional.fromNullable(trustStorePassword);
    }

	
    
    public Optional<String> getTrustStoreType() {
        return Optional.fromNullable(trustStoreType);
    }

	

    public Optional<Boolean> getNeedClientAuth() {
        return Optional.fromNullable(needClientAuth);
    }

	

    public ImmutableList<String> getSupportedProtocols() {
        return supportedProtocols;
    }


	
    @JsonProperty
    private File keyStore = null;

	

    @JsonProperty
    private String certAlias = null;

	

    public Optional<File> getKeyStore() {
        return Optional.fromNullable(keyStore);
    }

	

    public void setKeyStore(File keyStore) {
        this.keyStore = keyStore;
    }

	

    public void setKeyStorePassword(String password) {
        this.keyStorePassword = password;
    }

	

    public void setKeyManagerPassword(String keyManagerPassword) {
        this.keyManagerPassword = keyManagerPassword;
    }

	

    public void setKeyStoreType(String keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

	

    public Optional<String> getCertAlias() {
        return Optional.fromNullable(certAlias);
    }

	

    public void setCertAlias(String alias) {
        this.certAlias = alias;
    }

	

    public void setSupportedProtocols(List<String> protocols) {
        this.supportedProtocols = ImmutableList.copyOf(protocols);
    }


}
