import React from 'react';
import ArtifactoryProductDetail from "./ArtifactoryProductDetail";

const ArtifactoryProductDetails = ({ product }) => {
    let fieldsWithoutName = Object.keys(product);
    fieldsWithoutName.splice(fieldsWithoutName.indexOf('name'), 1);

    return (
        <div className="artifactory-product-details">
            {fieldsWithoutName.map(item => (
                <ArtifactoryProductDetail key={item} product={product} fieldKey={item} />
            ))}
        </div>
    );
}

export default ArtifactoryProductDetails;
