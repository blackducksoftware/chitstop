import React from 'react';
import InputText from "../core/InputText";
import Textarea from "../core/Textarea";

const ArtifactoryProductInputs = ({ propertyKey, setPropertyKey, propertyValue, setPropertyValue, productProperties }) => {
    const currentValue = productProperties
        .filter(item => item.key == propertyKey)
        .map(item => item.value)
        .join();

    return (
        <div>
            <div>
                <InputText value={propertyKey} setValue={setPropertyKey} size={40} />
            </div>
            <div>
                Current value:
                <div className="artifactory-property-value">
                    {currentValue}
                </div>
            </div>
            <div>
                Suggested change:
                <Textarea value={propertyValue} setValue={setPropertyValue} rows={4} columns={80} />
            </div>
        </div>
    );
}

export default ArtifactoryProductInputs;
