import React from 'react';

const ArtifactoryProductDetail = ({ product, fieldKey }) => {
    return (
        <div className="artifactory-product-detail">
            <span className="artifactory-product-detail-key">{fieldKey}: </span>
            <span className="artifactory-product-detail-value">{product[fieldKey]}</span>
        </div>
    );
}

export default ArtifactoryProductDetail;
