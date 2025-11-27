interface MessageBubbleProps {
  message: string;
  timestamp: string;
  isSent: boolean;
  isEdited?: boolean;
  replyTo?: {
    text: string;
    sender: string;
  };
}

export function MessageBubble({ message, timestamp, isSent, isEdited, replyTo }: MessageBubbleProps) {
  return (
    <div className={`flex ${isSent ? 'justify-end' : 'justify-start'} mb-4`}>
      <div className={`max-w-[70%] ${isSent ? 'order-2' : 'order-1'}`}>
        <div
          className={`rounded-2xl px-4 py-2 ${
            isSent
              ? 'bg-gradient-to-r from-pink-500 to-rose-500 text-white'
              : 'bg-gray-100 text-gray-900'
          }`}
        >
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
        </div>
        <span className={`text-xs text-gray-400 mt-1 block ${isSent ? 'text-right' : 'text-left'}`}>
          {timestamp}
        </span>
      </div>
    </div>
  );
}