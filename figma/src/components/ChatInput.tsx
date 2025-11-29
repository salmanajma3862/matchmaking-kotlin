import { Send, Image, Smile, Mic } from 'lucide-react';
import { useState } from 'react';

interface ChatInputProps {
  onSend: (message: string) => void;
  onSendAudio?: () => void;
  onSendPhoto?: () => void;
}

export function ChatInput({ onSend, onSendAudio, onSendPhoto }: ChatInputProps) {
  const [message, setMessage] = useState('');
  const [isRecording, setIsRecording] = useState(false);

  const handleSend = () => {
    if (message.trim()) {
      onSend(message);
      setMessage('');
    }
  };

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  const handleAudioRecord = () => {
    setIsRecording(!isRecording);
    if (!isRecording) {
      // Start recording simulation
      setTimeout(() => {
        setIsRecording(false);
        onSendAudio?.();
      }, 2000);
    }
  };

  return (
    <div className="border-t border-gray-200 bg-white p-4">
      <div className="flex items-center gap-2">
        <button 
          onClick={onSendPhoto}
          className="p-2 text-gray-500 hover:text-pink-500 transition-colors"
        >
          <Image className="w-5 h-5" />
        </button>
        <button className="p-2 text-gray-500 hover:text-pink-500 transition-colors">
          <Smile className="w-5 h-5" />
        </button>
        <input
          type="text"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          onKeyPress={handleKeyPress}
          placeholder="Type a message..."
          className="flex-1 px-4 py-2 bg-gray-100 rounded-full outline-none focus:ring-2 focus:ring-pink-500"
        />
        {message.trim() ? (
          <button
            onClick={handleSend}
            className="p-2 bg-gradient-to-r from-pink-500 to-rose-500 text-white rounded-full hover:shadow-lg transition-shadow"
          >
            <Send className="w-5 h-5" />
          </button>
        ) : (
          <button
            onClick={handleAudioRecord}
            className={`p-2 rounded-full transition-all ${
              isRecording
                ? 'bg-red-500 text-white animate-pulse'
                : 'bg-gradient-to-r from-pink-500 to-rose-500 text-white hover:shadow-lg'
            }`}
          >
            <Mic className="w-5 h-5" />
          </button>
        )}
      </div>
      {isRecording && (
        <div className="mt-2 flex items-center justify-center gap-2 text-red-500 text-sm">
          <div className="w-2 h-2 bg-red-500 rounded-full animate-pulse" />
          Recording...
        </div>
      )}
    </div>
  );
}
