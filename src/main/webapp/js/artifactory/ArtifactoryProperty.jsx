import React from 'react';

const ArtifactoryProperty = ({ data }) =>
    (
        <div className="artifactory-property">
            <div className="artifactory-property-key">
                <label>{data.key}</label>
            </div>
            <div className="artifactory-property-value">
                {data.value}
            </div>
        </div>
    );

export default ArtifactoryProperty