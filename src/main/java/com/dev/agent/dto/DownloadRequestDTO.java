package com.dev.agent.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class DownloadRequestDTO {

    private String url;
    private List<String> agentAddresses;
}
