import React, { useEffect, useState } from 'react';
import InputText from "../core/InputText";
import Textarea from "../core/Textarea";
import ArtifactoryProductDetails from "./ArtifactoryProductDetails";
import ArtifactoryProperty from "./ArtifactoryProperty";
import ArtifactoryUpdateSuggestion from "./ArtifactoryUpdateSuggestion";

const ArtifactoryProductProperties = ({ activeProduct, activateProduct }) => {
    const [propertyKey, setPropertyKey] = useState('');
    const [propertyValue, setPropertyValue] = useState('');
    const [updateRecommended, setUpdateRecommended] = useState(false);
    const [productProperties, setProductProperties] = useState([])
    const [product, setProduct] = useState('')

    async function updateArtifactory() {
        await fetch('/api/artifactory/products/latest', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                productName: activeProduct,
                propertyKey: propertyKey,
                downloadUrl: propertyValue
            })
        });
        activateProduct('');
    }

    useEffect(() => {
        async function fetchSuggestedUpdate() {
            const result = await fetch(`/api/artifactory/products/latest?name=${activeProduct}`);
            const productResult = await result.json();
            setPropertyKey(productResult['propertyKey']);
            setPropertyValue(productResult['downloadUrl']);
            setUpdateRecommended(productResult['updateRecommended']);
        }

        async function fetchProductProperties() {
            const result = await fetch(`/api/artifactory/properties?name=${activeProduct}`);
            setProductProperties(await result.json());
        }

        async function fetchProduct() {
            const result = await fetch(`/api/artifactory/products?name=${activeProduct}`);
            setProduct(await result.json());
        }

        if (activeProduct) {
            fetchSuggestedUpdate();
            fetchProductProperties();
            fetchProduct();
        }

        return function cleanup() {
            setPropertyKey('');
            setPropertyValue('');
            setUpdateRecommended(false);
            setProductProperties([]);
            setProduct('');
        };
    }, [activeProduct]);

    return (
        <div className="artifactory-product-properties">
            Existing Properties:
            <div className="artifactory-properties-list">
                {productProperties.map(item => (
                    <ArtifactoryProperty key={item.key} data={item} />
                ))}
            </div>
            <div className="artifactory-properties-form">
                <ArtifactoryUpdateSuggestion activeProduct={activeProduct} updateRecommended={updateRecommended} />
                <div className="artifactory-properties-form-suggestion">
                </div>
                Suggested Change:
                <div className="artifactory-properties-form-inputs">
                    <div className="input">
                        <div>
                            Key:
                        </div>
                        <div>
                            <InputText value={propertyKey} setValue={setPropertyKey} size={40} />
                        </div>
                    </div>
                    <div className="input">
                        <div>
                            Value:
                        </div>
                        <div>
                            <Textarea value={propertyValue} setValue={setPropertyValue} rows={4} columns={80} />
                        </div>
                    </div>
                </div>
                <div className="submit">
                    <button onClick={updateArtifactory}>Update Artifactory</button>
                </div>
            </div>
            <div>
                Product Details:
                <ArtifactoryProductDetails product={product} />
            </div>
        </div>
    );
}

export default ArtifactoryProductProperties;
