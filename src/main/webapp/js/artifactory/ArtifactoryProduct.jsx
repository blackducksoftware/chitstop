import React, { useEffect, useState } from 'react';
import ArtifactoryProductDetails from "./ArtifactoryProductDetails";
import ArtifactoryProperty from "./ArtifactoryProperty";
import ArtifactoryUpdateSuggestion from "./ArtifactoryUpdateSuggestion";
import ArtifactoryProductInputs from "./ArtifactoryProductInputs";

const ArtifactoryProduct = ({ activeProduct, activateProduct }) => {
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
        <div className="artifactory-product">
            <h2>{activeProduct}</h2>
            <div className="artifactory-product-form">
                <ArtifactoryUpdateSuggestion activeProduct={activeProduct} updateRecommended={updateRecommended} />
                <div className="artifactory-product-form-inputs">
                    <ArtifactoryProductInputs propertyKey={propertyKey} setPropertyKey={setPropertyKey} propertyValue={propertyValue} setPropertyValue={setPropertyValue} productProperties={productProperties} />
                </div>
                <div className="submit">
                    <button className="btn btn-primary" onClick={updateArtifactory}>Update Artifactory</button>
                </div>
            </div>
            <div className="small-spacer"></div>
            <div className="artifactory-product-properties">
                All existing properties:
                <ul className="list-group">
                    {productProperties.map(item => (
                        <li key={item.key} className="list-group-item">
                            <ArtifactoryProperty data={item} />
                        </li>
                    ))}
                </ul>
            </div>
            <div className="small-spacer"></div>
            <div>
                Product Details:
                <ArtifactoryProductDetails product={product} />
            </div>
        </div>
    );
}

export default ArtifactoryProduct;
