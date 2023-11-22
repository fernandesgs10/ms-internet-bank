package br.com.internet.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VersionDTO {
    private String version;
    private String groupId;
    private String artifactId;
}
