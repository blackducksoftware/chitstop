import React from 'react';

const Token = ({ data }) =>
    (
        <li className="list-group-item vms-token-details">
            <div>
                {data.tokenDetails.username} ({data.tokenDetails.scope})
            </div>
            <div>
                {data.token}
            </div>
        </li>
    )

export default Token