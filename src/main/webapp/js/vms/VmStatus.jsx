import React from 'react';

const VmStatus = ({ data }) =>
    (
        <tr>
            <th scope="row">{data.blackDuckUrl}</th>
            <td>{data.version}</td>
            <td>{String(data.live)}</td>
            <td>{String(data.ready)}</td>
        </tr>
    );

export default VmStatus