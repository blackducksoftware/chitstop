import React, { useEffect, useState } from 'react';
import TokenList from "./TokenList";

const Tokens = () => {
    const [vmKeyToTokens, setVmKeyToTokens] = useState({});

    useEffect(() => {
        async function fetchTokens() {
            const result = await fetch('/api/token/all');
            const tokenArray = await result.json();

            const tokenMapTOTALCRAP = {};
            tokenArray.forEach(token => {
                let mappedTokens = tokenMapTOTALCRAP[token.tokenDetails.vmKey];
                if (mappedTokens) tokenMapTOTALCRAP.push(token); else tokenMapTOTALCRAP[token.tokenDetails.vmKey] = [token]
            });

            const tokenMap = tokenArray.reduce((map, token) => {
                let mappedTokens = map[token.tokenDetails.vmKey];
                if (mappedTokens) mappedTokens.push(token); else map[token.tokenDetails.vmKey] = [token]
                return map;
            }, {});

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
