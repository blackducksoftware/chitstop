import React from 'react';

const InputText = ({ value, setValue, size }) =>
    (<input type="text" value={value} onChange={e => setValue(e.target.value)} size={size} />);

export default InputText
