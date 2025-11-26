import { ImageWithFallback } from './figma/ImageWithFallback';
import { MessageCircle, Heart } from 'lucide-react';

interface MatchProfileCardProps {
  id: string;
  name: string;
  age: number;
  image: string;
  location: string;
  isNew?: boolean;
  onMessage: () => void;
  onViewProfile: () => void;
}

export function MatchProfileCard({ name, age, image, location, isNew, onMessage, onViewProfile }: MatchProfileCardProps) {
  return (
    <div 
      onClick={onViewProfile}
      className="relative rounded-2xl overflow-hidden shadow-md hover:shadow-xl transition-shadow bg-white cursor-pointer"
    >
      {/* Image */}
      <div className="relative aspect-[3/4] overflow-hidden">
        <ImageWithFallback
          src={image}
          alt={name}
          className="w-full h-full object-cover"
        />
        {isNew && (
          <div className="absolute top-3 right-3 bg-pink-500 text-white px-3 py-1 rounded-full text-xs flex items-center gap-1">
            <Heart className="w-3 h-3 fill-white" />
            New
          </div>
        )}
        {/* Gradient overlay */}
        <div className="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent"></div>
        
        {/* Info */}
        <div className="absolute bottom-0 left-0 right-0 p-4 text-white">
          <div className="mb-2">
            <span className="text-xl">{name}, {age}</span>
          </div>
          <p className="text-sm text-white/90">{location}</p>
        </div>
      </div>

      {/* Message button */}
      <button
        onClick={(e) => {
          e.stopPropagation();
          onMessage();
        }}
        className="absolute bottom-4 right-4 bg-white text-pink-500 p-3 rounded-full shadow-lg hover:shadow-xl transition-all hover:scale-110"
      >
        <MessageCircle className="w-5 h-5" />
      </button>
    </div>
  );
}