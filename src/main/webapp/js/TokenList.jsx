import React, { useEffect, useState } from 'react';

import Token from "./Token";

const TokenList = () => {
    const [count, setCount] = useState(0);
    const [tokens, setTokens] = useState([]);

    useEffect(() => {
        async function fetchTokens() {
            const result = await fetch('/api/token/all');
            setTokens(await result.json());
        }

        fetchTokens()
    }, []);

    return (
        <>
            <div className="tokenList">
                {tokens.map(item => (
                    <Token key={item.token} data={item} />
                ))}
            </div>
            <div>
                <p>You clicked {count} times</p>
                <button onClick={() => setCount(count + 1)}>
                    Click me
                </button>
            </div>
        </>
    )
}

export default TokenList
