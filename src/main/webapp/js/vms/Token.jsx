import React from 'react';

const Token = ({ data }) =>
    (
        <div className="token">
            <div>
                <span className="label">
                    Username:
                </span>
                <span className="value">
                    {data.tokenDetails.username}
                </span>
            </div>
            <div>
                <span className="label">
                    Scope:
                </span>
                <span className="value">
                    {data.tokenDetails.scope}
                </span>
            </div>
            <div>
                <span className="label">
                    Token:
                </span>
                <span className="value">
                    {data.token}
                </span>
            </div>
        </div>
    )

export default Token