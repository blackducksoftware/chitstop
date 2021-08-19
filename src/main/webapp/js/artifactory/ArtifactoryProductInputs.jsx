import React from 'react';
import InputText from "../core/InputText";
import Textarea from "../core/Textarea";
import ArtifactoryProperty from "./ArtifactoryProperty";

const ArtifactoryProductInputs = ({ propertyKey, setPropertyKey, propertyValue, setPropertyValue, productProperties }) => {
    const currentProperty = productProperties
        .filter(item => item.key == propertyKey)
        .pop() || { key: '', value: '' }

    return (
        <div>
            <div className="form-group">
                <label>Current value</label>
                <ArtifactoryProperty data={currentProperty} />
            </div>
            <div className="form-group">
                <label htmlFor="artifactory-product-property-value">Suggested change</label>
                <InputText elementId="artifactory-product-property-key" value={propertyKey} setValue={setPropertyKey} size={40} />
                <Textarea elementId="artifactory-product-property-value" value={propertyValue} setValue={setPropertyValue} rows={4} columns={80} />
            </div>
        </div>
    );
}

export default ArtifactoryProductInputs;
