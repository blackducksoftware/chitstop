import React, { useEffect, useState } from 'react';
import ArtifactoryProductProperties from "./ArtifactoryProductProperties";

const ArtifactoryProducts = () => {
    const [products, setProducts] = useState([]);
    const [activeProduct, activateProduct] = useState();

    useEffect(() => {
        async function fetchProducts() {
            const result = await fetch('/api/artifactory/products/all');
            setProducts(await result.json());
        }

        fetchProducts();
    }, []);

    return (
        <div className="artifactory">
            <div className="artifactory-products">
                {products.map(product => (
                    <div key={product.name} className="artifactory-product">
                        <button className="artifactory-product-name" onClick={() => activateProduct(product.name)}>{product.name}</button>
                    </div>
                ))}
            </div>
            <ArtifactoryProductProperties activeProduct={activeProduct} activateProduct={activateProduct} />
        </div>
    )
}

export default ArtifactoryProducts
