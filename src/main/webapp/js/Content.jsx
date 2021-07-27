import React from 'react';

import { Route, Switch } from "react-router-dom";

import TokenList from "./TokenList";
import ArtifactoryProducts from "./ArtifactoryProducts";

const Content = () => {
    function Home() {
        return <h2>Home</h2>;
    }

    return (
        <Switch>
            <Route path="/vms">
                <TokenList />
            </Route>
            <Route path="/artifactory">
                <ArtifactoryProducts />
            </Route>
            <Route path="/">
                <Home />
            </Route>
        </Switch>
    );
}

export default Content;
