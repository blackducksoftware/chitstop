import React from 'react';

import Token from "./Token";

const TokenList = ({ vmKey, tokenArray }) => {
    return (
        <div>
            <h4>
                {vmKey}
            </h4>
            <ul className="list-group">
                {tokenArray.map(item => (
                    <Token key={item.token} data={item} />
                ))}
            </ul>
        </div>
    )
}

export default TokenList
