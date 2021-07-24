package com.synopsys.integration.chitstop.service.artifactory.artifactfinder;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.synopsys.integration.chitstop.rest.model.ArtifactoryProductDetails;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryChildItem;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryClient;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryFile;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryItem;
import com.synopsys.integration.chitstop.service.artifactory.ArtifactoryPath;
import com.synopsys.integration.chitstop.service.utility.HttpUrlCreator;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.rest.HttpUrl;

@Component
public class NestedArtifactFinder implements ArtifactFinder {
    private final ArtifactoryClient artifactoryClient;

    @Autowired
    public NestedArtifactFinder(ArtifactoryClient artifactoryClient) {
        this.artifactoryClient = artifactoryClient;
    }

    @Override
    public Optional<HttpUrl> artifactByVersion(ArtifactoryProductDetails artifactoryProductDetails, String version) throws IntegrationException {
        ArtifactoryPath productPath = ArtifactoryPath.createArtifactsPath(artifactoryProductDetails);

        ArtifactoryPath folderPath = productPath.append(version);

        ArtifactoryItem folderItem = artifactoryClient.findItem(folderPath);

        return
            folderItem
                .getChildren()
                .stream()
                .filter(child -> !child.isFolder())
                .filter(ITEM_MATCHES.apply(artifactoryProductDetails, version))
                .findFirst()
                .map(ArtifactoryChildItem::getUri)
                .map(folderPath::append)
                .flatMap(artifactoryClient::findFileSafely)
                .map(ArtifactoryFile::getDownloadUri)
                .flatMap(HttpUrlCreator::createSafely);
    }

}
