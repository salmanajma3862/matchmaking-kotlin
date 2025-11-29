import { useState } from 'react';
import { Play, Pause, Image as ImageIcon } from 'lucide-react';
import { ImageWithFallback } from './figma/ImageWithFallback';

interface MessageBubbleProps {
  message?: string;
  timestamp: string;
  isSent: boolean;
  isEdited?: boolean;
  replyTo?: {
    text: string;
    sender: string;
  };
  type?: 'text' | 'audio' | 'photo';
  audioDuration?: string;
  senderImage?: string;
  senderName?: string;
}

export function MessageBubble({ 
  message, 
  timestamp, 
  isSent, 
  isEdited, 
  replyTo,
  type = 'text',
  audioDuration,
  senderImage,
  senderName
}: MessageBubbleProps) {
  const [isPlaying, setIsPlaying] = useState(false);

  const renderContent = () => {
    if (type === 'audio') {
      return (
        <div className="flex items-center gap-3">
          <button
            onClick={() => setIsPlaying(!isPlaying)}
            className={`p-2 rounded-full transition-colors ${
              isSent
                ? 'bg-white/20 hover:bg-white/30'
                : 'bg-pink-100 hover:bg-pink-200'
            }`}
          >
            {isPlaying ? (
              <Pause className={`w-4 h-4 ${isSent ? 'text-white' : 'text-pink-600'}`} />
            ) : (
              <Play className={`w-4 h-4 ${isSent ? 'text-white' : 'text-pink-600'}`} />
            )}
          </button>
          <div className="flex-1">
            <div className={`h-8 flex items-center gap-0.5`}>
              {Array.from({ length: 20 }).map((_, i) => (
                <div
                  key={i}
                  className={`flex-1 rounded-full ${
                    isSent ? 'bg-white/40' : 'bg-pink-300'
                  }`}
                  style={{
                    height: `${Math.random() * 60 + 40}%`,
                  }}
                />
              ))}
            </div>
          </div>
          <span className={`text-xs ${isSent ? 'text-white/80' : 'text-gray-600'}`}>
            {audioDuration || '0:15'}
          </span>
        </div>
      );
    }

    if (type === 'photo') {
      return (
        <div className="flex items-center gap-3 min-w-[180px]">
          <div className={`p-3 rounded-full ${
            isSent ? 'bg-white/20' : 'bg-pink-100'
          }`}>
            <ImageIcon className={`w-6 h-6 ${isSent ? 'text-white' : 'text-pink-600'}`} />
          </div>
          <div className="flex-1">
            <div className={`${isSent ? 'text-white' : 'text-gray-900'}`}>
              Photo
            </div>
            <div className={`text-xs ${isSent ? 'text-white/70' : 'text-gray-500'}`}>
              Tap to view
            </div>
          </div>
        </div>
      );
    }

    // Text message
    return (
      <>
        {replyTo && (
          <div
            className={`mb-2 pb-2 border-l-2 pl-2 text-xs ${
              isSent ? 'border-white/40' : 'border-gray-400'
            }`}
          >
            <div className={`${isSent ? 'text-white/80' : 'text-gray-600'}`}>
              {replyTo.sender}
            </div>
            <div className={`truncate ${isSent ? 'text-white/70' : 'text-gray-500'}`}>
              {replyTo.text}
            </div>
          </div>
        )}
        <p>{message}</p>
        {isEdited && (
          <span className={`text-xs italic mt-1 block ${isSent ? 'text-white/70' : 'text-gray-500'}`}>
            edited
          </span>
        )}
      </>
    );
  };

  return (
    <div className={`flex ${isSent ? 'justify-end' : 'justify-start'} mb-4`}>
      {/* Profile picture for received messages */}
      {!isSent && senderImage && (
        <div className="w-8 h-8 rounded-full overflow-hidden mr-2 mt-auto mb-6">
          <ImageWithFallback
            src={senderImage}
            alt={senderName || 'User'}
            className="w-full h-full object-cover"
          />
        </div>
      )}

      <div className={`max-w-[70%] ${isSent ? 'order-2' : 'order-1'}`}>
        <div
          className={`rounded-2xl px-4 py-2 ${
            isSent
              ? 'bg-gradient-to-r from-pink-500 to-rose-500 text-white'
              : 'bg-gray-100 text-gray-900'
          }`}
        >
          {renderContent()}
        </div>
        <span className={`text-xs text-gray-400 mt-1 block ${isSent ? 'text-right' : 'text-left'}`}>
          {timestamp}
        </span>
      </div>
    </div>
  );
}
