import React from 'react';
import { Link } from "react-router-dom";

const LeftNav = () => {
    return (
        <nav>
            <ul>
                <li>
                    <Link to="/">Home</Link>
                </li>
                <li>
                    <Link to="/vms">VM's</Link>
                </li>
                <li>
                    <Link to="/artifactory">Artifactory</Link>
                </li>
            </ul>
        </nav>
    );
}

export default LeftNav;
