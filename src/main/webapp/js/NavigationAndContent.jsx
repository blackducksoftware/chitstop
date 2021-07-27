import React from 'react';
import { BrowserRouter as Router } from "react-router-dom";
import Content from "./Content";
import LeftNav from "./LeftNav";

const NavigationAndContent = () => {
    return (
        <Router>
            <div id="left-navigation">
                <LeftNav />
            </div>
            <div id="content">
                <Content />
            </div>
        </Router>
    )
}

export default NavigationAndContent
