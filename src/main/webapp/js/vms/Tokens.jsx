import React, { useEffect, useState } from 'react';
import TokenList from "./TokenList";

const Tokens = () => {
    const [vmKeyToTokens, setVmKeyToTokens] = useState({});

    useEffect(() => {
        async function fetchTokens() {
            const result = await fetch('/api/token/all');
            const tokenArray = await result.json();

            const tokenMap = {};
            tokenArray.forEach(token => {
                let mappedTokens = tokenMap[token.tokenDetails.vmKey];
                if (mappedTokens) mappedTokens.push(token); else tokenMap[token.tokenDetails.vmKey] = [token]
            });

            setVmKeyToTokens(tokenMap);
        }

        fetchTokens();
    }, []);

    return (
        <div>
            {Object.keys(vmKeyToTokens).map(vmKey => (
                <TokenList key={vmKey} vmKey={vmKey} tokenArray={vmKeyToTokens[vmKey]} />
            ))}
        </div>
    );
}

export default Tokens;
