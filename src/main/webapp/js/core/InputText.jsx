import React from 'react';

const InputText = ({ elementId, value, setValue, size }) =>
    (<input id={elementId} type="text" className="form-control" value={value} onChange={e => setValue(e.target.value)} size={size} />);

export default InputText
