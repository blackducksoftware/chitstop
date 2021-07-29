import React, { Component } from "react";
import ReactDOM from 'react-dom';
import "core-js/stable";
import "regenerator-runtime/runtime";

import '../css/main.css'

import NavigationAndContent from "./NavigationAndContent";

class Main extends Component {
    render() {
        return (
            <div>
                <div id="header" className="border-box">
                    <div>
                        <img src="../images/logo.png" />
                    </div>
                    <div id="header-content">
                        <a href="https://github.com/blackducksoftware">Github</a> | <a href="https://eng-jenkins-dev01.dc1.lan/job/integration-builds-v2/">Jenkins</a>
                    </div>
                </div>
                <div id="page">
                    <NavigationAndContent />
                </div>
            </div>
        );
    }
}

ReactDOM.render(
    <Main />,
    document.getElementById('react-mountpoint')
);
