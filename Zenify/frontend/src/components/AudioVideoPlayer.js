import React, { useState, useEffect } from 'react';
import axios from 'axios';

function MediaPlayer() {
    const [audioData, setAudioData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        axios.get("http://127.0.0.1:8000/contents/")
            .then(response => {
                setAudioData(response.data); // Set the whole array
                setLoading(false);
            })
            .catch(error => {
                console.error("Error fetching audio data:", error);
                setError("Failed to load audio");
                setLoading(false);
            });
    }, []);

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;
    if (!audioData.length) return <div>No audio found</div>;

    return (
        <div>
            {audioData.map((content, index) => (
                <div key={index}>
                    <h1>{content.title}</h1>
                    <p>{content.description}</p>
                    <iframe width="100%" height="166" scrolling="no" frameBorder="no" allow="autoplay"
                        src={`https://w.soundcloud.com/player/?url=${encodeURIComponent(content.audio_link)}&color=%23ff5500&auto_play=false&hide_related=false&show_comments=true&show_user=true&show_reposts=false&show_teaser=true&visual=true`}>
                        Your browser does not support the audio element.
                    </iframe>
                </div>
            ))}
        </div>
    );
}

export default MediaPlayer;
