import React from 'react';

const ArtifactoryProductDetail = ({ details, fieldKey }) => {
    return (
        <div className="artifactory-product-detail">
            <span className="artifactory-product-detail-key">{fieldKey}: </span>
            <span className="artifactory-product-detail-value">{details[fieldKey]}</span>
        </div>
    );
}

export default ArtifactoryProductDetail;
