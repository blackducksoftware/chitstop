import React from 'react';
import { BrowserRouter as Router } from "react-router-dom";
import Content from "./Content";
import LeftNav from "./LeftNav";

const NavigationAndContent = () => {
    return (
        <Router>
            <div id="side-bar" className="col-lg-2">
                <LeftNav />
            </div>
            <div id="content" className="col-lg-10">
                <Content />
            </div>
        </Router>
    )
}

export default NavigationAndContent
