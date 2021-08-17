import React, { Component } from "react";
import ReactDOM from 'react-dom';
import "core-js/stable";
import "regenerator-runtime/runtime";

import '../css/main.css'
import '../css/artifactory.css'

import NavigationAndContent from "./NavigationAndContent";

class Main extends Component {
    render() {
        return (
            <div id="page" className="container-fluid">
                <header id="header" className="row align-items-end">
                    <div id="logo">
                        <img src="../images/logo.png" />
                    </div>
                    <div id="header-content" className="col-lg-8">
                        <a href="https://github.com/blackducksoftware">Github</a> | <a href="https://eng-jenkins-dev01.dc1.lan/job/integration-builds-v2/">Jenkins</a>
                    </div>
                </header>
                <div id="middle" className="row">
                    <NavigationAndContent />
                </div>
                <footer id="footer" className="row">
                    <div className="col-lg-12">
                        What is wroooong with you? | Weeeeee did it! | Billings'ed!!! | Never slather yourself in BBQ sauce in a den of hyenas | Piss Whale | Team Shit | Grapist | Sexy Fox | Bridge of Cheese
                    </div>
                </footer>
            </div>
        );
    }
}

ReactDOM.render(
    <Main />,
    document.getElementById('react-mountpoint')
);
