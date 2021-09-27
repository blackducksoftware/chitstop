import React, { useEffect, useState } from 'react';
import VmStatus from "./VmStatus";

const Diagnostics = () => {
    const [statuses, setStatuses] = useState([]);

    useEffect(() => {
        async function fetchDiagnostics() {
            const result = await fetch('/api/vms/diagnostics');
            setStatuses(await result.json());
        }

        fetchDiagnostics();
    }, []);

    return (
        <table>
            <thead>
            <tr>
                <th>Black Duck URL</th>
                <th>Version</th>
                <th>Live?</th>
                <th>Ready?</th>
            </tr>
            </thead>
            <tbody>
            {statuses.map(status => (
                <VmStatus key={status.blackDuckUrl} data={status} />
            ))}
            </tbody>
        </table>
    );
}

export default Diagnostics;
