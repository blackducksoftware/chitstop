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
public class RepoArtifactFinder implements ArtifactFinder {
    private final ArtifactoryClient artifactoryClient;

    @Autowired
    public RepoArtifactFinder(ArtifactoryClient artifactoryClient) {
        this.artifactoryClient = artifactoryClient;
    }

    @Override
    public Optional<HttpUrl> artifactByVersion(ArtifactoryProductDetails artifactoryProductDetails, String version) throws IntegrationException {
        ArtifactoryPath artifactoryPath = ArtifactoryPath.createArtifactsPath(artifactoryProductDetails);

        ArtifactoryItem artifactoryItem = artifactoryClient.findItem(artifactoryPath);
        return artifactoryItem
                   .getChildren()
                   .stream()
                   .filter(child -> !child.isFolder())
                   .filter(ITEM_MATCHES.apply(artifactoryProductDetails, version))
                   .map(ArtifactoryChildItem::getUri)
                   .map(artifactoryPath::append)
                   .map(artifactoryClient::findFileSafely)
                   .flatMap(Optional::stream)
                   .map(ArtifactoryFile::getDownloadUri)
                   .map(HttpUrlCreator::createSafely)
                   .flatMap(Optional::stream)
                   .findFirst();
    }

}
