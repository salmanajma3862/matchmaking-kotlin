import { ArrowLeft, MoreVertical, Video, Phone } from 'lucide-react';
import { MessageBubble } from './MessageBubble';
import { ChatInput } from './ChatInput';
import { ImageWithFallback } from './figma/ImageWithFallback';

interface Message {
  id: string;
  text?: string;
  timestamp: string;
  isSent: boolean;
  isEdited?: boolean;
  replyTo?: {
    text: string;
    sender: string;
  };
  type?: 'text' | 'audio' | 'photo';
  audioDuration?: string;
}

interface MessageThreadProps {
  name: string;
  image: string;
  messages: Message[];
  onBack: () => void;
  onSendMessage: (message: string) => void;
}

export function MessageThread({ name, image, messages, onBack, onSendMessage }: MessageThreadProps) {
  const handleSendAudio = () => {
    console.log('Audio message sent');
    // In a real app, this would handle the audio recording and sending
  };

  const handleSendPhoto = () => {
    console.log('Photo message sent');
    // In a real app, this would open file picker and send photo
  };

  return (
    <div className="flex flex-col h-full">
      {/* Header */}
      <div className="flex items-center justify-between p-4 border-b border-gray-200 bg-white">
        <div className="flex items-center gap-3">
          <button onClick={onBack} className="p-2 hover:bg-gray-100 rounded-full transition-colors">
            <ArrowLeft className="w-5 h-5" />
          </button>
          <div className="w-10 h-10 rounded-full overflow-hidden">
            <ImageWithFallback
              src={image}
              alt={name}
              className="w-full h-full object-cover"
            />
          </div>
          <span>{name}</span>
        </div>
        <div className="flex items-center gap-2">
          <button className="p-2 text-gray-600 hover:bg-gray-100 rounded-full transition-colors">
            <Phone className="w-5 h-5" />
          </button>
          <button className="p-2 text-gray-600 hover:bg-gray-100 rounded-full transition-colors">
            <Video className="w-5 h-5" />
          </button>
          <button className="p-2 text-gray-600 hover:bg-gray-100 rounded-full transition-colors">
            <MoreVertical className="w-5 h-5" />
          </button>
        </div>
      </div>

      {/* Messages */}
      <div className="flex-1 overflow-y-auto p-4 bg-gray-50">
        <div className="max-w-3xl mx-auto">
          {messages.map((msg) => (
            <MessageBubble
              key={msg.id}
              message={msg.text}
              timestamp={msg.timestamp}
              isSent={msg.isSent}
              isEdited={msg.isEdited}
              replyTo={msg.replyTo}
              type={msg.type}
              audioDuration={msg.audioDuration}
              senderImage={!msg.isSent ? image : undefined}
              senderName={!msg.isSent ? name : undefined}
            />
          ))}
        </div>
      </div>

      {/* Input */}
      <ChatInput 
        onSend={onSendMessage} 
        onSendAudio={handleSendAudio}
        onSendPhoto={handleSendPhoto}
      />
    </div>
  );
}
