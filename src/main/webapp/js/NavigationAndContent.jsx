import React from 'react';
import { BrowserRouter as Router } from "react-router-dom";
import Content from "./Content";
import LeftNav from "./LeftNav";

const NavigationAndContent = () => {
    return (
        <Router>
            <div id="side-bar" className="border-box">
                <LeftNav />
            </div>
            <div id="content" className="border-box">
                <Content />
            </div>
        </Router>
    )
}

export default NavigationAndContent
