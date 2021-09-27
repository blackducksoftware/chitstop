import React from 'react';

import { Route, Switch } from "react-router-dom";

import ArtifactoryProducts from "./artifactory/ArtifactoryProducts";
import NotFound from "./NotFound";
import Tokens from "./vms/Tokens";
import Diagnostics from "./vms/Diagnostics";

const Content = () => {
    function Home() {
        return <h2>Home</h2>;
    }

    return (
        <Switch>
            <Route exact path="/vms">
                <Tokens />
            </Route>
            <Route exact path="/vms/diagnostics">
                <Diagnostics />
            </Route>
            <Route exact path="/artifactory">
                <ArtifactoryProducts />
            </Route>
            <Route exact path="/">
                <Home />
            </Route>
            <Route component={NotFound} />
        </Switch>
    );
}

export default Content;
