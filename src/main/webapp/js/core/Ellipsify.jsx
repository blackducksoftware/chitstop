import React from 'react';

const Ellipsify = ({ text, maxlength, chopLeft }) => {
    const replacement = '...';

    const cut = () => {
        if (text.length > maxlength) {
            const whereToCut = maxlength - replacement.length;
            if (chopLeft) {
                return replacement + text.substring(text.length - whereToCut)
            } else {
                return text.substring(0, whereToCut) + replacement;
            }
        }
        return text;
    }

    return (
        <span title={text}>{cut()}</span>
    );
}

export default Ellipsify;
