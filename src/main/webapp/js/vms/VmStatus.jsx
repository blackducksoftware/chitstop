import React from 'react';

const VmStatus = ({ data }) =>
    (
        <tr>
            <td>{data.blackDuckUrl}</td>
            <td>{data.version}</td>
            <td>{String(data.live)}</td>
            <td>{String(data.ready)}</td>
        </tr>
    );

export default VmStatus