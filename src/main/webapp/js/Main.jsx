import React, { Component } from "react";
import "core-js/stable";
import "regenerator-runtime/runtime";
import ReactDOM from 'react-dom';
import { BrowserRouter as Router, Link, Route, Switch } from "react-router-dom";

import '../css/main.css'

import TokenList from './TokenList'
import ArtifactoryProducts from "./ArtifactoryProducts";

class Main extends Component {
    render() {
        function Home() {
            return <h2>Home</h2>;
        }

        return (
            <Router>
                <div>
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
                </div>
            </Router>
        );
    }
}

ReactDOM.render(
    <Main />,
    document.getElementById('react-mountpoint')
);
