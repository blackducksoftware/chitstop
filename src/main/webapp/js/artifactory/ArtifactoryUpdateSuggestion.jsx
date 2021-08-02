import React from 'react';
import UpdateRecommended from "./UpdateRecommended";
import UpdateNotRecommended from "./UpdateNotRecommended";

const ArtifactoryUpdateSuggestion = ({ activeProduct, updateRecommended }) => {
    if (!activeProduct) {
        return <div></div>
    }

    if (updateRecommended) {
        return <UpdateRecommended />
    } else {
        return <UpdateNotRecommended />
    }
}

export default ArtifactoryUpdateSuggestion;
