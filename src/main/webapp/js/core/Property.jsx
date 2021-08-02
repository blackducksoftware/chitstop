import React from 'react';
import Ellipsify from "./Ellipsify";

const Property = ({ data }) =>
    (
        <div>
            <span className="label">
                {data.key}:
            </span>
            <span className="value">
                <Ellipsify text={data.value} maxlength={20} chopLeft={true} />
            </span>
        </div>
    );

export default Property