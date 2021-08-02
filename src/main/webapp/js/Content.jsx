import React from 'react';

import { Route, Switch } from "react-router-dom";

import TokenList from "./vms/TokenList";
import ArtifactoryProducts from "./artifactory/ArtifactoryProducts";
import NotFound from "./NotFound";

const Content = () => {
    function Home() {
        return <h2>Home</h2>;
    }

    return (
        <Switch>
            <Route exact path="/vms">
                <TokenList />
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
