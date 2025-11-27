import { ImageWithFallback } from './figma/ImageWithFallback';

interface ConversationItemProps {
  id: string;
  name: string;
  image: string;
  lastMessage: string;
  timestamp: string;
  unread?: boolean;
  isActive?: boolean;
  onClick: () => void;
}

export function ConversationItem({
  name,
  image,
  lastMessage,
  timestamp,
  unread,
  isActive,
  onClick,
}: ConversationItemProps) {
  return (
    <button
      onClick={onClick}
      className={`w-full flex items-center gap-3 p-4 hover:bg-gray-50 transition-colors ${
        isActive ? 'bg-pink-50' : ''
      }`}
    >
      <div className="relative flex-shrink-0">
        <div className="w-14 h-14 rounded-full overflow-hidden">
          <ImageWithFallback
            src={image}
            alt={name}
            className="w-full h-full object-cover"
          />
        </div>
        {unread && (
          <div className="absolute bottom-0 right-0 w-3 h-3 bg-pink-500 rounded-full ring-2 ring-white"></div>
        )}
      </div>
      <div className="flex-1 min-w-0 text-left">
        <div className="flex items-center justify-between mb-1">
          <span className={unread ? 'text-gray-900' : 'text-gray-700'}>{name}</span>
          <span className="text-xs text-gray-500">{timestamp}</span>
        </div>
        <p className={`text-sm truncate ${unread ? 'text-gray-900' : 'text-gray-500'}`}>
          {lastMessage}
        </p>
      </div>
    </button>
  );
}
