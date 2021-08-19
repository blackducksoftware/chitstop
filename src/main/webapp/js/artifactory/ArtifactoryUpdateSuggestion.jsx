import React from 'react';

const ArtifactoryUpdateSuggestion = ({ activeProduct, updateRecommended }) => {
    if (!activeProduct) {
        return <div></div>
    }

    const className = updateRecommended ?
        "artifactory-product-update-recommended" :
        "artifactory-product-update-not-recommended";

    const content = updateRecommended ?
        "Please consider updating - the existing property does not match the suggestion." :
        "The existing property matches the suggestion. No update is recommended. If you just released, make sure the artifact is correctly published in Artifactory.";

    return <div className={className}>{content}</div>
}

export default ArtifactoryUpdateSuggestion;
