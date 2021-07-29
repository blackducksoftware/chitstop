import React from 'react';
import ArtifactoryProductDetail from "./ArtifactoryProductDetail";

const ArtifactoryProductDetails = ({ details }) => {
    let fieldsWithoutName = Object.keys(details);
    fieldsWithoutName.splice(fieldsWithoutName.indexOf('name'), 1);

    return (
        <div className="artifactory-product-details">
            <span className="artifactory-product-details-name">{details.name}</span>
            {fieldsWithoutName.map(item => (
                <ArtifactoryProductDetail key={item} details={details} fieldKey={item} />
            ))}
        </div>
    );
}

export default ArtifactoryProductDetails;
