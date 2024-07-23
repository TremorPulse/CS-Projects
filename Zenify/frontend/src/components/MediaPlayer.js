import React, { useState, useEffect } from 'react';
import AudioVideoPlayer from './AudioVideoPlayer';

function MediaPlayer() {
    const [contents, setContents] = useState([]);

    useEffect(() => {
        fetch('http://localhost:8000/contents')
            .then(response => response.json())
            .then(data => setContents(data))
            .catch(err => console.error('Error fetching data: ', err));
    }, []);

    return (
        <div>
            {contents.map((content, idx) => (
                <AudioVideoPlayer key={idx} content={content} />
            ))}
        </div>
    );
}

export default MediaPlayer;
