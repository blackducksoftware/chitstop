import React, { useEffect, useState } from 'react';

const ArtifactoryProducts = () => {
    const [products, setProducts] = useState([]);

    useEffect(() => {
        async function fetchProducts() {
            const result = await fetch('/api/artifactory/products/all');
            setProducts(await result.json());
        }

        fetchProducts()
    }, []);

    return (
        <div className="artifactoryProducts">
            {products.map(item => (
                <div key={item.name}>
                    <span>{item.name}:</span>
                    <span>{item.repoKey}:</span>
                    <span>{item.itemPathToCheck}:</span>
                    <span>{item.propertyPrefix}</span>
                </div>
            ))}
        </div>
    )
}

export default ArtifactoryProducts
