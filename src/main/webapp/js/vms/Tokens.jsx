import React, { useEffect, useState } from 'react';
import TokenList from "./TokenList";

const Tokens = () => {
    const [vmKeyToTokens, setVmKeyToTokens] = useState({});

    useEffect(() => {
        async function fetchTokens() {
            const result = await fetch('/api/token/all');
            const tokenArray = await result.json();

            const tokenMap = tokenArray.reduce((map, token) => {
                let mappedTokens = map[token.tokenDetails.vmKey];
                if (null == mappedTokens) {
                    mappedTokens = [];
                    map[token.tokenDetails.vmKey] = mappedTokens;
                }
                mappedTokens.push(token);
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
