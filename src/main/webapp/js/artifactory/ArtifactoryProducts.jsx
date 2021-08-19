import React, { useEffect, useState } from 'react';
import ArtifactoryProduct from "./ArtifactoryProduct";

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
            <div className="artifactory-products btn-toolbar" role="toolbar">
                <div className="btn-group-vertical btn-group-sm">
                    {products.map(product => (
                        <button key={product.name} className="btn btn-outline-info" onClick={() => activateProduct(product.name)}>{product.name}</button>
                    ))}
                </div>
            </div>
            <ArtifactoryProduct activeProduct={activeProduct} activateProduct={activateProduct} />
        </div>
    )
}

export default ArtifactoryProducts
