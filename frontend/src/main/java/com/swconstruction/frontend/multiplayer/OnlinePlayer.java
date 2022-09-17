package com.swconstruction.frontend.multiplayer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OnlinePlayer {

    // Ip
    private String publicAddress;
    // Listening port
    private Integer serverPort;
    // Sending port (Server port of the opponent)
    private Integer clientPort;
    // Player's username
    private String username;

    @JsonCreator
    public OnlinePlayer(@JsonProperty("publicAddress") String publicAddress,
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
