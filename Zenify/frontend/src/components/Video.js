import React, { useEffect, useRef } from 'react';
import videojs from 'video.js';

const VideoPlayer = ({ src }) => {
    const videoRef = useRef(null);

    useEffect(() => {
        const vjsPlayer = videojs(videoRef.current);
        vjsPlayer.src({ type: 'video/mp4', src });
        vjsPlayer.play();

        return () => {
            vjsPlayer.dispose();
        };
    }, [src]);

    return (
        <div data-vjs-player>
            <video ref={videoRef} className="video-js vjs-big-play-centered" controls preload="auto" width="640" height="264">
                <source src={src} type="video/mp4" />
            </video>
        </div>
    );
};

export default VideoPlayer;
