import React from 'react';

const Textarea = ({ value, setValue, rows, columns }) =>
    (<textarea value={value} onChange={e => setValue(e.target.value)} rows={rows} cols={columns} />);

export default Textarea