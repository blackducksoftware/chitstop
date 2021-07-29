import React, { useEffect, useState } from 'react';
import ArtifactoryProductDetails from "./ArtifactoryProductDetails";

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
        <div className="artifactory-products">
            {products.map(item => (
                <ArtifactoryProductDetails key={item.name} details={item} />
            ))}
        </div>
    )
}

export default ArtifactoryProducts
