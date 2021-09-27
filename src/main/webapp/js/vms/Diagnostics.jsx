import React, { useEffect, useState } from 'react';
import VmStatus from "./VmStatus";
import LoadingSpinner from "../core/LoadingSpinner";

const Diagnostics = () => {
    const [statuses, setStatuses] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function fetchDiagnostics() {
            const result = await fetch('/api/vms/diagnostics');
            setStatuses(await result.json());
            setLoading(false);
        }

        fetchDiagnostics();
    }, []);

    if (loading)
        return <LoadingSpinner />

    return (
        <table className="table table-dark table-striped">
            <thead>
            <tr>
                <th scope="col">Black Duck URL</th>
                <th scope="col">Version</th>
                <th scope="col">Live?</th>
                <th scope="col">Ready?</th>
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
