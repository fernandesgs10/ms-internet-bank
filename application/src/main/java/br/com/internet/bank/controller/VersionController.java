package br.com.internet.bank.controller;

import br.com.internet.bank.dto.VersionDTO;
import lombok.SneakyThrows;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileReader;

@RestController
public class VersionController {

    @SneakyThrows
    @GetMapping("/v1/internet-bank/version")
    public VersionDTO getVersion() {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("pom.xml"));

        return VersionDTO.builder()
                .version(model.getVersion())
                .artifactId(model.getArtifactId())
                .groupId(model.getGroupId())
                .build();
    }

}
