package com.swconstruction.multiplayer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Player {
    // IP address
    private String publicAddress;
    /// Port number for server and client sockets of the remote player
    private Integer serverPort;
    private Integer clientPort;
    // Username of the player
    private String username;

    // Port number of the player for request
    private Integer requestPort;

    public Integer getRequestPort() {
        return requestPort;
    }

    public void setRequestPort(Integer requestPort) {
        this.requestPort = requestPort;
    }

    @JsonCreator
    public Player(@JsonProperty("publicAddress") String publicAddress,
                  @JsonProperty("serverPort") Integer serverPort,
                  @JsonProperty("clientPort") Integer clientPort,
                  @JsonProperty("username") String username) {
        this.publicAddress = publicAddress;
        this.serverPort = serverPort;
        this.clientPort = clientPort;
        this.username = username;
    }

    public String getPublicAddress() {
        return publicAddress;
    }

    public void setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public Integer getClientPort() {
        return clientPort;
    }

    public void setClientPort(Integer clientPort) {
        this.clientPort = clientPort;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
