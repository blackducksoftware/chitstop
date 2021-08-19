import React from 'react';

const Textarea = ({ elementId, value, setValue, rows, columns }) =>
    (<textarea id={elementId} className="form-control" value={value} onChange={e => setValue(e.target.value)} rows={rows} cols={columns} />);

export default Textarea